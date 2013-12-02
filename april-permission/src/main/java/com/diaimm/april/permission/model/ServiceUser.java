/*
 * @fileName : ServiceUser.java
 * @date : 2013. 6. 4.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.permission.model;

import java.util.Date;

/**
 * @author diaimm
 * 
 */
public interface ServiceUser {
	/**
	 * 회원 id
	 * 
	 * @return
	 */
	String getId(); // 

	/**
	 * 회원 접속 IP
	 * @return
	 */
	String getIp();

	/**
	 * 
	 * @return
	 */
	boolean isLogin();

	/**
	 * @return
	 */
	Date getLoginDate();

	/**
	 * @return
	 */
	String getEmail();

	/**
	 * @return
	 */
	String getName();

	/**
	 * @return
	 */
	// String getGener(); 
	Gender getGenderType();

	/**
	 * @return
	 */
	boolean getRealNameVerified();
}
