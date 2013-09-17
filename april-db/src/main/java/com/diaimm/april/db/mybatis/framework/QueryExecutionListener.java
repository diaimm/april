/*
 * @fileName : QueryExecutionListener.java
 * @date : 2013. 6. 4.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.diaimm.april.db.mybatis.framework;

import java.sql.Connection;

import org.apache.ibatis.session.Configuration;

/**
 * @author diaimm
 * 
 */
public interface QueryExecutionListener {
	/**
	 * query 실행전 호출
	 * 
	 * @param sqlId
	 * @param connection
	 * @param configuration
	 * @param args
	 */
	void beforeExecution(String sqlId, Connection connection, Configuration configuration, Object[] args);

	/**
	 * query 실행 후 호출
	 * 
	 * @param sqlId
	 * @param connection
	 * @param configuration
	 * @param args
	 */
	void afterExecution(String sqlId, Connection connection, Configuration configuration, Object[] args);

	/**
	 * query 실행중 exception 발생시 호출
	 * 
	 * @param sqlId
	 * @param connection
	 * @param configuration
	 * @param args
	 * @param unwrapped
	 */
	void onException(String sqlId, Connection connection, Configuration configuration, Object[] args, Throwable unwrapped);

	/**
	 * session close 후 호출
	 * 
	 * @param sqlId
	 * @param configuration
	 * @param args
	 */
	void afterClose(String sqlId, Configuration configuration, Object[] args);
}
