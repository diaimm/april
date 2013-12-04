/*
 * @fileName : ExecutionQueryPrintListener.java
 * @date : 2013. 6. 4.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.db.mybatis.framework.listener;

import com.diaimm.april.db.mybatis.framework.QueryExecutionListener;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @author diaimm
 * 
 */
public class ExecutionQueryPrintListener implements QueryExecutionListener {
	private static Logger logger = LoggerFactory.getLogger(ExecutionQueryPrintListener.class);

	@Override
	public void beforeExecution(String sqlId, Connection connection, Configuration configuration, Object[] args) {
	}

	@Override
	public void afterExecution(String sqlId, Connection connection, Configuration configuration, Object[] args) {
		if (!logger.isDebugEnabled()) {
			return;
		}

		logQueryExecution(sqlId, connection, configuration, args, null);
	}

	@Override
	public void onException(String sqlId, Connection connection, Configuration configuration, Object[] args, Throwable throwable) {
		logQueryExecution(sqlId, connection, configuration, args, throwable);
	}

	@Override
	public void afterClose(String sqlId, Configuration configuration, Object[] args) {
	}

	/**
	 * @param sqlId
	 * @param connection
	 * @param configuration
	 * @param args
	 */
	private void logQueryExecution(String sqlId, Connection connection, Configuration configuration, Object[] args, Throwable exception) {
		Object mapParams = QueryBindingHelper.wrapCollection(args[1]);
		MappedStatement mappedStatement = configuration.getMappedStatement(sqlId);
		BoundSql boundSql = mappedStatement.getBoundSql(mapParams);
		String sql = boundSql.getSql();

		StringBuffer log = new StringBuffer();
		log.append("\n query(").append(sqlId).append(")");
		if (exception == null) {
			log.append(" executed normally");
		} else {
			log.append(" execution FAILED");
		}

		boolean queryAppended = false;
		try {
			if (connection != null) {
				StatementHandler handler = configuration.newStatementHandler(new SimpleExecutor(configuration, null), mappedStatement, mapParams,
					RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER, boundSql);
				PreparedStatement preparedStatement = (PreparedStatement)handler.prepare(connection);
				handler.parameterize(preparedStatement);
				String parameterizedQuery = preparedStatement.toString();
				String query = parameterizedQuery.substring(parameterizedQuery.indexOf(":"));
				log.append("\n ").append(query);

				queryAppended = true;
			}
		} catch (Exception e) {
		}

		if (!queryAppended) {
			log.append("\n ").append(sql);
		}

		if (exception == null) {
			logger.debug(log.toString());
		} else {
			logger.error(log.toString(), exception);
		}
	}
}
