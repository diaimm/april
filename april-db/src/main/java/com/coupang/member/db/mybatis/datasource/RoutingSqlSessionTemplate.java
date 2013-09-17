/*
 * @fileName : ProxySqlSessionTemplate.java
 * @date : 2013. 6. 4.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.db.mybatis.datasource;

import com.coupang.member.db.mybatis.framework.QueryExecutionListener;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.*;
import org.mybatis.spring.MyBatisExceptionTranslator;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.support.PersistenceExceptionTranslator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.reflect.Proxy.newProxyInstance;
import static org.apache.ibatis.reflection.ExceptionUtil.unwrapThrowable;
import static org.mybatis.spring.SqlSessionUtils.closeSqlSession;
import static org.mybatis.spring.SqlSessionUtils.getSqlSession;
import static org.springframework.util.Assert.notNull;

/**
 * @author diaimm
 */
public class RoutingSqlSessionTemplate extends SqlSessionTemplate implements InitializingBean, ApplicationContextAware {
	private final SqlSession sqlSessionProxy;
	private List<QueryExecutionListener> queryExecutionListeners;
	private ApplicationContext applicationContext;
	private String datasourceId;

	/**
	 * Constructs a Spring managed SqlSession with the {@code SqlSessionFactory} provided as an argument.
	 *
	 * @param sqlSessionFactory
	 */
	public RoutingSqlSessionTemplate(String datasourceId, SqlSessionFactory sqlSessionFactory) {
		this(datasourceId, sqlSessionFactory, sqlSessionFactory.getConfiguration().getDefaultExecutorType());
	}

	/**
	 * Constructs a Spring managed SqlSession with the {@code SqlSessionFactory} provided as an argument and the given {@code ExecutorType}
	 * {@code ExecutorType} cannot be changed once the {@code SqlSessionTemplate} is constructed.
	 *
	 * @param sqlSessionFactory
	 * @param executorType
	 */
	public RoutingSqlSessionTemplate(String datasourceId, SqlSessionFactory sqlSessionFactory, ExecutorType executorType) {
		this(datasourceId, sqlSessionFactory, executorType, new MyBatisExceptionTranslator(sqlSessionFactory.getConfiguration().getEnvironment()
				.getDataSource(), true));
	}

	/**
	 * Constructs a Spring managed {@code SqlSession} with the given {@code SqlSessionFactory} and {@code ExecutorType}. A custom
	 * {@code SQLExceptionTranslator} can be provided as an argument so any {@code PersistenceException} thrown by MyBatis can be custom translated to
	 * a {@code RuntimeException} The {@code SQLExceptionTranslator} can also be null and thus no exception translation will be done and MyBatis
	 * exceptions will be thrown
	 *
	 * @param sqlSessionFactory
	 * @param executorType
	 * @param exceptionTranslator
	 */
	public RoutingSqlSessionTemplate(String datasourceId, SqlSessionFactory sqlSessionFactory, ExecutorType executorType,
									 PersistenceExceptionTranslator exceptionTranslator) {
		super(sqlSessionFactory, executorType, exceptionTranslator);

		notNull(sqlSessionFactory, "Property 'sqlSessionFactory' is required");
		notNull(executorType, "Property 'executorType' is required");

		this.sqlSessionProxy = (SqlSession) newProxyInstance(SqlSessionFactory.class.getClassLoader(), new Class[]{SqlSession.class},
				new SqlSessionInterceptor());
		this.datasourceId = datasourceId;
	}

	/**
	 * @param queryExecutionListeners the queryExecutionListeners to set
	 */
	void setQueryExecutionListeners(List<QueryExecutionListener> queryExecutionListeners) {
		this.queryExecutionListeners = queryExecutionListeners;
	}

	/**
	 * {@inheritDoc}
	 */
	public <T> T selectOne(String statement) {
		return this.sqlSessionProxy.<T>selectOne(statement);
	}

	/**
	 * {@inheritDoc}
	 */
	public <T> T selectOne(String statement, Object parameter) {
		return this.sqlSessionProxy.<T>selectOne(statement, parameter);
	}

	/**
	 * {@inheritDoc}
	 */
	public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
		return this.sqlSessionProxy.<K, V>selectMap(statement, mapKey);
	}

	/**
	 * {@inheritDoc}
	 */
	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
		return this.sqlSessionProxy.<K, V>selectMap(statement, parameter, mapKey);
	}

	/**
	 * {@inheritDoc}
	 */
	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
		return this.sqlSessionProxy.<K, V>selectMap(statement, parameter, mapKey, rowBounds);
	}

	/**
	 * {@inheritDoc}
	 */
	public <E> List<E> selectList(String statement) {
		return this.sqlSessionProxy.<E>selectList(statement);
	}

	/**
	 * {@inheritDoc}
	 */
	public <E> List<E> selectList(String statement, Object parameter) {
		return this.sqlSessionProxy.<E>selectList(statement, parameter);
	}

	/**
	 * {@inheritDoc}
	 */
	public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
		return this.sqlSessionProxy.<E>selectList(statement, parameter, rowBounds);
	}

	/**
	 * {@inheritDoc}
	 */
	public void select(String statement, ResultHandler handler) {
		this.sqlSessionProxy.select(statement, handler);
	}

	/**
	 * {@inheritDoc}
	 */
	public void select(String statement, Object parameter, ResultHandler handler) {
		this.sqlSessionProxy.select(statement, parameter, handler);
	}

	/**
	 * {@inheritDoc}
	 */
	public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
		this.sqlSessionProxy.select(statement, parameter, rowBounds, handler);
	}

	/**
	 * {@inheritDoc}
	 */
	public int insert(String statement) {
		return this.sqlSessionProxy.insert(statement);
	}

	/**
	 * {@inheritDoc}
	 */
	public int insert(String statement, Object parameter) {
		return this.sqlSessionProxy.insert(statement, parameter);
	}

	/**
	 * {@inheritDoc}
	 */
	public int update(String statement) {
		return this.sqlSessionProxy.update(statement);
	}

	/**
	 * {@inheritDoc}
	 */
	public int update(String statement, Object parameter) {
		return this.sqlSessionProxy.update(statement, parameter);
	}

	/**
	 * {@inheritDoc}
	 */
	public int delete(String statement) {
		return this.sqlSessionProxy.delete(statement);
	}

	/**
	 * {@inheritDoc}
	 */
	public int delete(String statement, Object parameter) {
		return this.sqlSessionProxy.delete(statement, parameter);
	}

	/**
	 * {@inheritDoc}
	 */
	public <T> T getMapper(Class<T> type) {
		return getConfiguration().getMapper(type, this);
	}

	/**
	 * {@inheritDoc}
	 */
	public void commit() {
		throw new UnsupportedOperationException("Manual commit is not allowed over a Spring managed SqlSession");
	}

	/**
	 * {@inheritDoc}
	 */
	public void commit(boolean force) {
		throw new UnsupportedOperationException("Manual commit is not allowed over a Spring managed SqlSession");
	}

	/**
	 * {@inheritDoc}
	 */
	public void rollback() {
		throw new UnsupportedOperationException("Manual rollback is not allowed over a Spring managed SqlSession");
	}

	/**
	 * {@inheritDoc}
	 */
	public void rollback(boolean force) {
		throw new UnsupportedOperationException("Manual rollback is not allowed over a Spring managed SqlSession");
	}

	/**
	 * {@inheritDoc}
	 */
	public void close() {
		throw new UnsupportedOperationException("Manual close is not allowed over a Spring managed SqlSession");
	}

	/**
	 * {@inheritDoc}
	 */
	public void clearCache() {
		this.sqlSessionProxy.clearCache();
	}

	/**
	 * {@inheritDoc}
	 */
	public Connection getConnection() {
		return this.sqlSessionProxy.getConnection();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 1.0.2
	 */
	public List<BatchResult> flushStatements() {
		return this.sqlSessionProxy.flushStatements();
	}

	/**
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, QueryExecutionListener> queryListeners = applicationContext.getBeansOfType(QueryExecutionListener.class);
		this.queryExecutionListeners = new ArrayList<QueryExecutionListener>(queryListeners.values());
	}

	/**
	 * @param queryExecutionListener
	 */
	public void addListener(QueryExecutionListener queryExecutionListener) {
		this.queryExecutionListeners.add(queryExecutionListener);
	}

	/**
	 * Proxy needed to route MyBatis method calls to the proper SqlSession got from Spring's Transaction Manager It also unwraps exceptions thrown by
	 * {@code Method#invoke(Object, Object...)} to pass a {@code PersistenceException} to the {@code PersistenceExceptionTranslator}.
	 */
	private class SqlSessionInterceptor implements InvocationHandler {
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			final SqlSession sqlSession = getSqlSession(RoutingSqlSessionTemplate.this.getSqlSessionFactory(),
					RoutingSqlSessionTemplate.this.getExecutorType(), RoutingSqlSessionTemplate.this.getPersistenceExceptionTranslator());
			RoutingDataSourceCurrentDatasourceHolder.setContext(RoutingSqlSessionTemplate.this.datasourceId);
			String sqlId = (String) args[0];
			Configuration configuration = sqlSession.getConfiguration();
			Connection connection = sqlSession.getConnection();

			try {
				for (QueryExecutionListener listener : queryExecutionListeners) {
					listener.beforeExecution(sqlId, connection, configuration, args);
				}

				Object result = method.invoke(sqlSession, args);

				for (QueryExecutionListener listener : queryExecutionListeners) {
					listener.afterExecution(sqlId, connection, configuration, args);
				}
				return result;
			} catch (Throwable t) {
				Throwable unwrapped = unwrapThrowable(t);
				if (RoutingSqlSessionTemplate.this.getExecutorType() != null && unwrapped instanceof PersistenceException) {
					Throwable translated = RoutingSqlSessionTemplate.this.getPersistenceExceptionTranslator().translateExceptionIfPossible(
							(PersistenceException) unwrapped);
					if (translated != null) {
						unwrapped = translated;
					}
				}

				for (QueryExecutionListener listener : queryExecutionListeners) {
					listener.onException(sqlId, connection, configuration, args, unwrapped);
				}
				throw unwrapped;
			} finally {
				closeSqlSession(sqlSession, RoutingSqlSessionTemplate.this.getSqlSessionFactory());

				for (QueryExecutionListener listener : queryExecutionListeners) {
					listener.afterClose(sqlId, configuration, args);
				}

				RoutingDataSourceCurrentDatasourceHolder.clear();
			}
		}
	}
}
