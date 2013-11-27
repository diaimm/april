/*
 * @fileName : ServiceUser.java
 * @date : 2013. 6. 4.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.permission.model;

/**
 * @author diaimm
 * 
 */
public interface ServiceUser {
	public static final String LOGIN_DATE_FORMAT = "yyyyMMddHHmmss";

	/**
	 * 회원 번호
	 * 
	 * @return
	 */
	//String getSrl();
	String getMemberId();

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
	 * yyyyMMddHHmmss
	 * 
	 * @return
	 */
	//String getLoginDate();
	String getLastLoginDttm();

	/**
	 * 로그인 저장 여부
	 * 
	 * @return
	 */
	boolean isRemember();

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
	String getBirthday();

	/**
	 * @return
	 */
	// String getGener(); 
	String getGenderType();

	/**
	 * @return
	 */
	// String getVerifiedYN(); 
	String getRealNameVerified();

	/**
	 * @return
	 */
	// String getToken(); 
	String getRecommendationJoinToken();

	/**
	 * @return
	 */
	// String getAdminType(); 
	String getMemberLevelType();

	/**
	 * @return
	 */
	// String getMailYN();	
	String getAgreeEmail();

	/**
	 * @return
	 */
	// String getSmsYN();	
	String getAgreeSMS();

	/**
	 * @return
	 */
	String getSuperYN();
}
