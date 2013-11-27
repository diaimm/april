/*
 * @fileName : ReplicationDriver.java
 * @date : 2013. 6. 19.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.db.jdbc;

import com.mysql.jdbc.NonRegisteringReplicationDriver;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * @author diaimm
 * 
 */
public class ReplicationDriver extends NonRegisteringReplicationDriver implements java.sql.Driver {
	// ~ Static fields/initializers
	// ---------------------------------------------

	//
	// Register ourselves with the DriverManager
	//
	static {
		try {
			java.sql.DriverManager.registerDriver(new ReplicationDriver());
		} catch (SQLException E) {
			throw new RuntimeException("Can't register driver!");
		}
	}

	// ~ Constructors
	// -----------------------------------------------------------

	/**
	 * Construct a new driver and register it with DriverManager
	 * 
	 * @throws SQLException
	 *             if a database error occurs.
	 */
	public ReplicationDriver() throws SQLException {
		// Required for Class.forName().newInstance()
	}

	/**
	 * @see com.mysql.jdbc.NonRegisteringDriver#acceptsURL(java.lang.String)
	 */
	@Override
	public boolean acceptsURL(String url) throws SQLException {
		return url.startsWith("jdbc:mysql:replication");
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}
}
