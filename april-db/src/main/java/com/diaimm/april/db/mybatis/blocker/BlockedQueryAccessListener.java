/*
 * @fileName : ExecutionBlockerListener.java
 * @date : 2013. 6. 5.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.diaimm.april.db.mybatis.blocker;

import java.sql.Connection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.diaimm.april.db.mybatis.framework.QueryExecutionListener;
import com.diaimm.april.db.mybatis.framework.listener.QueryBindingHelper;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.springframework.util.CollectionUtils;

/**
 * @author diaimm
 */
class BlockedQueryAccessListener implements QueryExecutionListener {
	private BlockQueryManager blockQueryManager;

	/**
	 * @param blockQueryManager
	 */
	public BlockedQueryAccessListener(BlockQueryManager blockQueryManager) {
		this.blockQueryManager = blockQueryManager;
	}

	/**
	 * @see com.diaimm.april.db.mybatis.framework.QueryExecutionListener#beforeExecution(java.lang.String, java.sql.Connection,
	 *      org.apache.ibatis.session.Configuration, java.lang.Object[])
	 */
	@Override
	public void beforeExecution(String sqlId, Connection connection, Configuration configuration, Object[] args) {
		Object mapParams = QueryBindingHelper.wrapCollection(args[1]);
		MappedStatement mappedStatement = configuration.getMappedStatement(sqlId);
		BoundSql boundSql = mappedStatement.getBoundSql(mapParams);

		// 일단 block된 query command type에 속하는 경우만 처리
		if (blockQueryManager.getBlockedCommandTypes().contains(mappedStatement.getSqlCommandType())) {
			String sql = boundSql.getSql();

			Map<String, Set<SqlCommandType>> extractTableNames = ExtractedTableNameHolder.parse(mappedStatement.getId(), sql);
			Boolean blockedByStatementName = blockQueryManager.getBlockedByStatementName(mappedStatement.getId());
			if (blockedByStatementName == null) {
				for (Entry<String, Set<SqlCommandType>> extractTableNameEntry : extractTableNames.entrySet()) {
					String tableName = extractTableNameEntry.getKey();
					Set<SqlCommandType> blocked = blockQueryManager.getBlocked(mappedStatement.getId(), tableName);
					// block된 query에 access하는 것이라면 exception을 던진다.
					if (!CollectionUtils.isEmpty(blocked)) {
						SqlCommandType anyBlockedCommand = getAnyBlockedCommand(blocked, extractTableNameEntry.getValue());
						if (anyBlockedCommand != null) {
							throw new BlockedQueryExecutionException(anyBlockedCommand, getBlockedDBAccessDefaultMessage());
						}
					}
				}
			} else if (blockedByStatementName) {
				throw new BlockedQueryExecutionException(null, getBlockedDBAccessDefaultMessage());
			}
		}

	}

	/**
	 * @param blocked
	 * @return
	 */
	private SqlCommandType getAnyBlockedCommand(Set<SqlCommandType> blocked, Set<SqlCommandType> targets) {
		for (SqlCommandType target : targets) {
			if (blocked.contains(target)) {
				return target;
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	private String getBlockedDBAccessDefaultMessage() {
		// TODO 적절한 default message를 생성해야 한다.
		return "";
	}

	/**
	 * @see com.diaimm.april.db.mybatis.framework.QueryExecutionListener#afterExecution(java.lang.String, java.sql.Connection,
	 *      org.apache.ibatis.session.Configuration, java.lang.Object[])
	 */
	@Override
	public void afterExecution(String sqlId, Connection connection, Configuration configuration, Object[] args) {

	}

	/**
	 * @see com.diaimm.april.db.mybatis.framework.QueryExecutionListener#onException(java.lang.String, java.sql.Connection,
	 *      org.apache.ibatis.session.Configuration, java.lang.Object[], java.lang.Throwable)
	 */
	@Override
	public void onException(String sqlId, Connection connection, Configuration configuration, Object[] args, Throwable unwrapped) {
	}

	/**
	 * @see com.diaimm.april.db.mybatis.framework.QueryExecutionListener#afterClose(java.lang.String, org.apache.ibatis.session.Configuration,
	 *      java.lang.Object[])
	 */
	@Override
	public void afterClose(String sqlId, Configuration configuration, Object[] args) {

	}

	/**
	 * @return the blockQueryManger
	 */
	BlockQueryManager getBlockQueryManger() {
		return blockQueryManager;
	}

	private static class ExtractedTableNameHolder {
		private static final Object lock = new Object();
		private static final Map<String, Map<String, Set<SqlCommandType>>> cached = new ConcurrentHashMap<String, Map<String, Set<SqlCommandType>>>();

		static Map<String, Set<SqlCommandType>> parse(String sqlId, String sql) {
			if (!cached.containsKey(sqlId)) {
				synchronized (lock) {
					if (!cached.containsKey(sqlId)) {
						cached.put(sqlId, QueryParserUtil.extractTableNames(sql));
					}
				}
			}

			return cached.get(sqlId);
		}
	}
}
