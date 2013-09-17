/*
 * @fileName : DefaultServiceUserFactory.java
 * @date : 2013. 6. 4.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.commons.service.model;

import java.util.concurrent.TimeUnit;

import com.coupang.commons.util.DateUtil;
import com.coupang.member.commons.service.ServiceUserConstantsAware;

/**
 * @author diaimm
 * 
 */
public class CookieBaseServiceUserFactory implements ServiceUserFactory, ServiceUserConstantsAware {
	public static final ServiceUser DEFAULT_USER = new DefaultServiceUser();
	private static final String DEFAULT_PARSED_VALUE = "";
	private String[] splittedCookieValue;
	private String rememberMeCookieValue;
	private String ip;

	public CookieBaseServiceUserFactory(String[] splittedCookieValue, String rememberMeCookieValue, String ip) {
		this.splittedCookieValue = splittedCookieValue;
		this.rememberMeCookieValue = rememberMeCookieValue;
		this.ip = ip;
	}

	/**
	 * @see com.coupang.member.commons.service.model.ServiceUserFactory#createUser(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public ServiceUser createUser() {
		if (!isValidCookieValue()) {
			return CookieBaseServiceUserFactory.DEFAULT_USER;
		}

		try {
			DefaultServiceUser serviceUser = new DefaultServiceUser();
			serviceUser.setLastLoginDttm(getValueFromParsedCookie(1));
			serviceUser.setMemberId(getValueFromParsedCookie(2));
			serviceUser.setUserId(getValueFromParsedCookie(3));
			serviceUser.setEmail(getValueFromParsedCookie(4));
			serviceUser.setName(getValueFromParsedCookie(5));
			serviceUser.setBirthDay(getValueFromParsedCookie(7));
			serviceUser.setGenderType(getValueFromParsedCookie(8));
			serviceUser.setRealNameVerified(getValueFromParsedCookie(9));
			serviceUser.setRecommendationJoinToken(getValueFromParsedCookie(11));
			serviceUser.setMemberLevelType(getValueFromParsedCookie(13));
			serviceUser.setSuperYn(getValueFromParsedCookie(16));

			serviceUser.setIp(this.ip);

			serviceUser.setRememberYn(this.rememberMeCookieValue);

			if (!isValidUser(serviceUser)) {
				return CookieBaseServiceUserFactory.DEFAULT_USER;
			}

			return serviceUser;
		} catch (Exception e) {
			return DEFAULT_USER;
		}
	}

	/**
	 * @param serviceUser
	 * @return
	 */
	private static boolean isValidUser(ServiceUser serviceUser) {
		// Cookie가 만료되었으면 삭제
		if (DateUtil.move(DateUtil.mktimeQuietly(serviceUser.getLastLoginDttm(), "yyyyMMddHHmmss"), 8, TimeUnit.HOURS) < DateUtil.mktime()) {
			if (!serviceUser.isRemember()) { // 모바일 로그인 유지
				return false;
			}
		}
		return true;
	}

	/**
	 * @param split
	 * @return
	 */
	private boolean isValidCookieValue() {
		// 제거된 쿠키 값이 있음 : nickName , adultYn, companyId -> null 로 처리함
		if (splittedCookieValue.length != 17) {
			return false;
		}

		String cookieVersion = getValueFromParsedCookie(0);
		if (!LOGIN_DATA_FORMAT_VERSION.equals(cookieVersion)) {
			return false;
		}

		return true;
	}

	private String getValueFromParsedCookie(int index) {
		if (splittedCookieValue != null && splittedCookieValue.length >= index + 1) {
			return splittedCookieValue[index];
		} else {
			return DEFAULT_PARSED_VALUE;
		}
	}
}