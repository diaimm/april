package com.diaimm.april.db.mybatis.datasource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.JdbcTransactionObjectSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.util.CollectionUtils;

import com.diaimm.april.db.mybatis.MapperScannerInitializer.BeanNamePostfixes;

/**
 * {@link RoutingDataSource}를 위한 {@PlatformTransactionManager}
 * 
 * @author diaimm
 * @version $Rev$, $Date$
 */
public class RoutingTransactionManager implements PlatformTransactionManager, ApplicationContextAware, InitializingBean {
	private Logger logger = LoggerFactory.getLogger(RoutingTransactionManager.class);
	private DataSource dataSource;
	private Map<String, PlatformTransactionManager> transactionManagers;
	private ApplicationContext applicationContext;

	/**
	 * 개별 transaction manager 등록을 위해서만 사용된다.
	 * 
	 * @return
	 */
	Map<String, PlatformTransactionManager> getTransactionManagers() {
		return transactionManagers;
	}

	/**
	 * {@link DataSource}를 설정한다
	 * 
	 * @param dataSource
	 *            {@link DataSource}
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		if (dataSource instanceof RoutingDataSource) {
			RoutingDataSource routingDataSource = (RoutingDataSource) dataSource;
			Map<?, ?> subDataSources = routingDataSource.getTargetDataSources();

			if (CollectionUtils.isEmpty(subDataSources)) {
				throw new IllegalStateException("Property 'dataSource' is instance of RoutingDataSource, but is not initialized correctly.");
			}

			// 순서고정을 위해 TreeMap을 사용합니다.
			transactionManagers = new TreeMap<String, PlatformTransactionManager>();

			for (Entry<?, ?> entry : subDataSources.entrySet()) {
				String dataSourceId = (String) entry.getKey();
				DataSource subDataSource = (DataSource) entry.getValue();

				PlatformTransactionManager newTransactionManager = getSubDatasourceTransactionManager(dataSourceId, subDataSource);
				transactionManagers.put(dataSourceId, newTransactionManager);
			}

			logger.debug("RoutingTransactionManager is initialized... handling datasources : {} ", transactionManagers.keySet());
		} else {
			// 순서고정을 위해 TreeMap을 사용합니다.
			transactionManagers = new TreeMap<String, PlatformTransactionManager>();

			PlatformTransactionManager newTransactionManager = new DataSourceTransactionManager(dataSource);
			transactionManagers.put("defaultDataSourceId", newTransactionManager);
		}
	}

	/**
	 * @param subDataSource
	 * @return
	 */
	private PlatformTransactionManager getSubDatasourceTransactionManager(String dataSourceId, DataSource subDataSource) {
		String id = BeanNamePostfixes.DATASOURCE.strip(dataSourceId);
		PlatformTransactionManager tx = applicationContext.getBean(BeanNamePostfixes.TRANSACTION_MANAGER.fullName(id),
				PlatformTransactionManager.class);
		return tx;
	}

	/**
	 * @return
	 */
	private List<String> getReversedTransactionManagerKeys() {
		List<String> keySet = new ArrayList<String>(transactionManagers.keySet());
		Collections.reverse(keySet);
		return keySet;
	}

	private void clearTransactions(List<Map<String, Object>> failRollbackTargets) {
		for (Map<String, Object> target : failRollbackTargets) {
			DataSourceTransactionManager transactionManager = (DataSourceTransactionManager) target.get("txm");
			DefaultTransactionStatus status = (DefaultTransactionStatus) target.get("status");
			JdbcTransactionObjectSupport transactionObjectSupport = (JdbcTransactionObjectSupport) status.getTransaction();

			DataSourceUtils.resetConnectionAfterTransaction(transactionObjectSupport.getConnectionHolder().getConnection(),
					transactionObjectSupport.getPreviousIsolationLevel());
			DataSourceUtils.releaseConnection(transactionObjectSupport.getConnectionHolder().getConnection(), transactionManager.getDataSource());
			transactionObjectSupport.getConnectionHolder().clear();
		}
	}

	/**
	 * 
	 * @param key
	 * @param transactionDefinition
	 * @param routingTransactionStatus
	 * @param transactionManager
	 * @return
	 * @return
	 */
	TransactionStatus addSubTransactionStatus(String key, TransactionDefinition transactionDefinition,
			RoutingTransactionStatus routingTransactionStatus, PlatformTransactionManager transactionManager) {
		TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
		routingTransactionStatus.addSubTransactionStatus(key, transactionStatus);

		return transactionStatus;
	}

	/**
	 * {@link DataSource}를 리턴한다
	 * 
	 * @return {@link DataSource}
	 */
	public DataSource getDataSource() {
		if (dataSource instanceof RoutingDataSource) {
			RoutingDataSource routingDataSource = (RoutingDataSource) dataSource;
			return routingDataSource.getCurrentDataSource();
		}

		return dataSource;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransactionStatus getTransaction(TransactionDefinition transactionDefinition) throws TransactionException {
		RoutingTransactionStatus routingTransactionStatus = new RoutingTransactionStatus();
		List<Map<String, Object>> failClearTarget = new ArrayList<Map<String, Object>>();

		for (Entry<String, PlatformTransactionManager> transactionManagerEntry : transactionManagers.entrySet()) {
			String key = transactionManagerEntry.getKey();
			PlatformTransactionManager transactionManager = transactionManagerEntry.getValue();

			try {
				TransactionStatus subTransactionStatus = addSubTransactionStatus(key, transactionDefinition, routingTransactionStatus,
						transactionManager);

				Map<String, Object> clearTarget = new HashMap<String, Object>();
				clearTarget.put("txm", transactionManager);
				clearTarget.put("status", subTransactionStatus);
				failClearTarget.add(clearTarget);
			} catch (Exception e) {
				for (Entry<String, PlatformTransactionManager> entry : transactionManagers.entrySet()) {
					if (transactionManager == entry.getValue()) {
						logger.error("Failed to GET TRANSACTION !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! (original message - " + e.getMessage()
								+ ")  ::: " + entry.getKey());
					}
				}

				clearTransactions(failClearTarget);

				throw new TransactionSystemException(e.getMessage(), e);
			}
		}

		return routingTransactionStatus;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commit(TransactionStatus transactionStatus) throws TransactionException {
		RoutingTransactionStatus routingTransactionStatus = (RoutingTransactionStatus) transactionStatus;

		if (routingTransactionStatus.isRollbackOnly()) {
			rollback(routingTransactionStatus);
			return;
		}

		TransactionException transactionException = null;
		for (String key : getReversedTransactionManagerKeys()) {
			PlatformTransactionManager platformTransactionManager = transactionManagers.get(key);
			try {
				TransactionStatus subTransactionStatus = routingTransactionStatus.getTransactionStatus(key);
				platformTransactionManager.commit(subTransactionStatus);
				logger.debug("{} is commited", key);
			} catch (TransactionException e) {
				logger.error(e.getMessage() + " (" + key + ")", e);
				transactionException = e;
			} catch (Exception e) {
				logger.error(e.getMessage() + " (" + key + ")", e);
				transactionException = new TransactionSystemException(e.getMessage(), e);
			}
		}

		if (transactionException != null) {
			throw transactionException;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback(TransactionStatus transactionStatus) throws TransactionException {
		RoutingTransactionStatus routingTransactionStatus = (RoutingTransactionStatus) transactionStatus;

		TransactionException transactionException = null;

		for (String key : getReversedTransactionManagerKeys()) {
			PlatformTransactionManager platformTransactionManager = transactionManagers.get(key);

			try {
				platformTransactionManager.rollback(routingTransactionStatus.getTransactionStatus(key));
				logger.debug("{} is rollbacked", key);
			} catch (TransactionException e) {
				logger.error(e.getMessage(), e);
				transactionException = e;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				transactionException = new TransactionSystemException(e.getMessage(), e);
			}
		}

		if (transactionException != null) {
			throw transactionException;
		}
	}

	/**
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
