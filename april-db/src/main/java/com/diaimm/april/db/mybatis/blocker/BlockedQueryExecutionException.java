/*
 * @fileName : BlockedQueryExecutionException.java
 * @date : 2013. 6. 5.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.db.mybatis.blocker;

import org.apache.ibatis.mapping.SqlCommandType;

/**
 * @author diaimm
 */
class BlockedQueryExecutionException extends UnsupportedOperationException {
	private static final long serialVersionUID = -1279288407804507721L;
	private SqlCommandType anyBlockedCommand;

	/**
	 * @param anyBlockedCommand
	 * @param exceptionMessage
	 */
	BlockedQueryExecutionException(SqlCommandType anyBlockedCommand, String exceptionMessage) {
		super(exceptionMessage);
		this.anyBlockedCommand = anyBlockedCommand;
	}

	/**
	 * @return the anyBlockedCommand
	 */
	SqlCommandType getAnyBlockedCommand() {
		return anyBlockedCommand;
	}
}
