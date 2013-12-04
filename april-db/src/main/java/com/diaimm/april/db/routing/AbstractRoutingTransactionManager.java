package com.diaimm.april.db.routing;

import java.util.*;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.*;
import org.springframework.util.CollectionUtils;

import com.diaimm.april.db.mybatis.MapperScannerInitializer;

/**
 * {@link RoutingDataSource}를 위한 {@PlatformTransactionManager}
 *
 * @author diaimm
 * @version $Rev$, $Date$
 */
public abstract class AbstractRoutingTransactionManager implements PlatformTransactionManager, ApplicationContextAware, InitializingBean {
	public static final String ROUTING_TRANSACTION_MANAGER_INITIALIZE_TEMPLATE_FTL = "routing-transaction-manager-initialize-template.ftl";
	private Logger logger = LoggerFactory.getLogger(AbstractRoutingTransactionManager.class);
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
	 * {@inheritDoc}
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		if (dataSource instanceof RoutingDataSource) {
			RoutingDataSource routingDataSource = (RoutingDataSource)dataSource;
			Map<?, ?> subDataSources = routingDataSource.getTargetDataSources();

			if (CollectionUtils.isEmpty(subDataSources)) {
				throw new IllegalStateException("Property 'dataSource' is instance of RoutingDataSource, but is not initialized correctly.");
			}

			// 순서고정을 위해 TreeMap을 사용합니다.
			transactionManagers = new TreeMap<String, PlatformTransactionManager>();

			for (Entry<?, ?> entry : subDataSources.entrySet()) {
				String dataSourceId = (String)entry.getKey();
				DataSource subDataSource = (DataSource)entry.getValue();

				PlatformTransactionManager newTransactionManager = getSubTransactionManager(dataSourceId);
				transactionManagers.put(dataSourceId, newTransactionManager);
			}

			logger.debug("RoutingTransactionManager is initialized... handling datasources : {} ", transactionManagers.keySet());
		} else {
			// 순서고정을 위해 TreeMap을 사용합니다.
			transactionManagers = new TreeMap<String, PlatformTransactionManager>();

			PlatformTransactionManager newTransactionManager = createNewDefaultTransactionManager(dataSource);
			transactionManagers.put("defaultDataSourceId", newTransactionManager);
		}
	}

	/**
	 * RoutingDataSource가 아닌경우 기본 transaction manager를 생성하도록 지시합니다.
	 * @param dataSource
	 * @return
	 */
	public abstract PlatformTransactionManager createNewDefaultTransactionManager(DataSource dataSource);

	/**
	 * @param dataSourceId
	 * @return
	 */
	private PlatformTransactionManager getSubTransactionManager(String dataSourceId) {
		String id = MapperScannerInitializer.BeanNamePostfixes.DATASOURCE.strip(dataSourceId);
		PlatformTransactionManager tx = getApplicationContext().getBean(MapperScannerInitializer.BeanNamePostfixes.TRANSACTION_MANAGER.fullName(id),
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

	/**
	 * failRollbackTargets 의 transaction들을 clear하도록 시도합니다.
	 * @param failRollbackTargets
	 */
	public abstract void clearTransactions(List<Map<String, Object>> failRollbackTargets);

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
	 * {@link javax.sql.DataSource}를 리턴한다
	 *
	 * @return {@link javax.sql.DataSource}
	 */
	public DataSource getDataSource() {
		if (dataSource instanceof RoutingDataSource) {
			RoutingDataSource routingDataSource = (RoutingDataSource)dataSource;
			return routingDataSource.getCurrentDataSource();
		}

		return dataSource;
	}

	/**
	 * {@link javax.sql.DataSource}를 설정한다
	 *
	 * @param dataSource
	 *            {@link javax.sql.DataSource}
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
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
		RoutingTransactionStatus routingTransactionStatus = (RoutingTransactionStatus)transactionStatus;

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
		RoutingTransactionStatus routingTransactionStatus = (RoutingTransactionStatus)transactionStatus;

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

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
