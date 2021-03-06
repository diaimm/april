/*
 * @fileName : DBAccessBlockerClient.java
 * @date : 2013. 6. 5.
 * @author : diaimm.
 * @desc :
 */
package com.diaimm.april.db.mybatis.blocker;

import org.apache.ibatis.mapping.SqlCommandType;

import java.util.Set;

/**
 * @author diaimm
 */
public interface DBAccessBlockerClient {
	void block(String tableName, SqlCommandType... sqlCommandType);

	void release(String tableName, SqlCommandType... sqlCommandType);

	void block(Set<String> tableNames, SqlCommandType... sqlCommandType);

	void release(Set<String> tableNames, SqlCommandType... sqlCommandType);
}
