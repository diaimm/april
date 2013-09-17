/*
 * @fileName : DefaultServiceUser.java
 * @date : 2013. 6. 4.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.commons.service.model;

import org.apache.commons.lang.StringUtils;

/**
 * @author diaimm
 * 
 */
class DefaultServiceUser implements ServiceUser {
	private String memberId;
	private String lastLoginDttm;
	private String userId;
	private String email;
	private String ip;
	private String name;
	private String birthDay;
	private String memberLevelType;
	private String genderType;
	private String realNameVerified;
	private String recommendationJoinToken;
	private String agreeEmail;
	private String agreeSMS;
	private String superYn;
	private String rememberYn;

	/**
	 * @see com.coupang.member.commons.service.model.ServiceUser#isLogin()
	 */
	@Override
	public boolean isLogin() {
		if (StringUtils.isEmpty(this.memberId)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @see com.coupang.member.commons.service.model.ServiceUser#isRemember()
	 */
	@Override
	public boolean isRemember() {
		return StringUtils.equals(this.rememberYn, "Y");
	}

	/*
	 * getters / setters 
	 */

	/**
	 * @see com.coupang.member.commons.service.model.ServiceUser#getId()
	 */
	@Override
	public String getId() {
		return this.userId;
	}

	/**
	 * @see com.coupang.member.commons.service.model.ServiceUser#getEmail()
	 */
	@Override
	public String getEmail() {
		return this.email;
	}

	/**
	 * @see com.coupang.member.commons.service.model.ServiceUser#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @see com.coupang.member.commons.service.model.ServiceUser#getBirthday()
	 */
	@Override
	public String getBirthday() {
		return this.birthDay;
	}

	/**
	 * @see com.coupang.member.commons.service.model.ServiceUser#getSuperYN()
	 */
	@Override
	public String getSuperYN() {
		return this.superYn;
	}

	/**
	 * @see com.coupang.member.commons.service.model.ServiceUser#getIp()
	 */
	@Override
	public String getIp() {
		return this.ip;
	}

	/**
	 * @param ip the ip to set
	 */
	void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @param userId the userId to set
	 */
	void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @param email the email to set
	 */
	void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param birthDay the birthDay to set
	 */
	void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	/**
	 * @param superYn the superYn to set
	 */
	void setSuperYn(String superYn) {
		this.superYn = superYn;
	}

	/**
	 * @param rememberYn the rememberYn to set
	 */
	public void setRememberYn(String rememberYn) {
		this.rememberYn = rememberYn;
	}

	@Override
	public String getMemberId() {
		return this.memberId;
	}

	/**
	 * @param memberId the memberId to set
	 */
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	/**
	 * @param lastLoginDttm the lastLoginDttm to set
	 */
	public void setLastLoginDttm(String lastLoginDttm) {
		this.lastLoginDttm = lastLoginDttm;
	}

	@Override
	public String getLastLoginDttm() {
		return this.lastLoginDttm;
	}

	@Override
	public String getGenderType() {
		return this.genderType;
	}

	/**
	 * @param genderType the genderType to set
	 */
	public void setGenderType(String genderType) {
		this.genderType = genderType;
	}

	@Override
	public String getRealNameVerified() {
		return this.realNameVerified;
	}

	/**
	 * @param realNameVerified the realNameVerified to set
	 */
	public void setRealNameVerified(String realNameVerified) {
		this.realNameVerified = realNameVerified;
	}

	@Override
	public String getRecommendationJoinToken() {
		return this.recommendationJoinToken;
	}

	/**
	 * @param recommendationJoinToken the recommendationJoinToken to set
	 */
	public void setRecommendationJoinToken(String recommendationJoinToken) {
		this.recommendationJoinToken = recommendationJoinToken;
	}

	@Override
	public String getMemberLevelType() {
		return this.memberLevelType;
	}

	/**
	 * @param memberLevelType the memberLevelType to set
	 */
	public void setMemberLevelType(String memberLevelType) {
		this.memberLevelType = memberLevelType;
	}

	@Override
	public String getAgreeEmail() {
		return this.agreeEmail;
	}

	@Override
	public String getAgreeSMS() {
		return this.agreeSMS;
	}

}