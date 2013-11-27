/*
 * @fileName : ServiceUserAuthCookieValueHander.java
 * @date : 2013. 6. 4.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.permission.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.coupang.commons.ByPhase.Phase;
import com.coupang.commons.Env;
import com.coupang.commons.crypto.AES.AESType;
import com.coupang.commons.crypto.Crypto;
import com.coupang.commons.util.DateUtil;
import com.coupang.commons.web.util.CookieBox;
import com.coupang.commons.web.util.CookieBox.CookieCooker;
import com.diaimm.april.permission.ServiceUserConstantsAware;
import com.diaimm.april.permission.model.CookieBaseServiceUserFactory;
import com.diaimm.april.permission.model.ServiceUser;

/**
 * @author diaimm
 * 
 */
public class ServiceUserAuthCookieValueHandler implements ServiceUserConstantsAware {
	private static final String USER_INFO_VALUE_SEPERATOR = "!@#";
	static final String USER_INFO_CRYPT_KEY_FILE = "/usr/local/coupang/key.dat";
	// ServiceUserInterceptor 빈 초기화에서 쿠키 키값을 넣어줌
	static String USER_INFO_CRYPT_KEY = null;

	/**
	 * 쿠키 -> ServiceUser
	 * 
	 * @param cookieValue
	 * @param request
	 * @return
	 */
	public static ServiceUser toServiceUser(String cookieValue, HttpServletRequest request) {
		CookieBox cookieBox = new CookieBox(request);

		// 쿠팡 회원 정보 빌드 FROM 쿠키
		CookieBaseServiceUserFactory userFactory = new CookieBaseServiceUserFactory(cookieValue.split(USER_INFO_VALUE_SEPERATOR), cookieBox.getValue(Cookies.REMEMBER_ME.getKey(), ""),
			request.getRemoteAddr());

		return userFactory.createUser();
	}

	/**
	 * ServiceUser -> 쿠키
	 * 
	 * @param serviceUser
	 * @return
	 * @throws Exception
	 */
	public static String toCookeiValue(ServiceUser serviceUser) throws Exception {
		StringBuffer ret = new StringBuffer();

		appendToValue(ret, LOGIN_DATA_FORMAT_VERSION);
		appendToValue(ret, DateUtil.date(ServiceUser.LOGIN_DATE_FORMAT));
		appendToValue(ret, serviceUser.getMemberId());
		appendToValue(ret, serviceUser.getId());
		appendToValue(ret, serviceUser.getEmail());
		appendToValue(ret, serviceUser.getName());
		appendToValue(ret, serviceUser.getBirthday());
		appendToValue(ret, null);
		appendToValue(ret, serviceUser.getGenderType());
		appendToValue(ret, serviceUser.getRealNameVerified());
		appendToValue(ret, null);
		appendToValue(ret, serviceUser.getRecommendationJoinToken());
		appendToValue(ret, null);
		appendToValue(ret, serviceUser.getMemberLevelType());
		appendToValue(ret, serviceUser.getAgreeEmail());
		appendToValue(ret, serviceUser.getAgreeSMS());
		appendToValue(ret, serviceUser.getSuperYN());

		// 로그인 사용자 정보 암호화
		return Crypto.aes(AESType.AES128).encrypt(ret.toString(), USER_INFO_CRYPT_KEY);
	}

	/**
	 * 값이 없어도 null 로 채워야 한다. 쿠키 값의 순서로 추출하기 때문이다. 기존 쿠키를 읽어야 하는 이슈도 있어서 쉽게 고칠수 없음
	 * 
	 * @param buffer
	 * @param value
	 */
	private static void appendToValue(StringBuffer buffer, String value) {
		if (buffer.length() != 0) {
			buffer.append(USER_INFO_VALUE_SEPERATOR);
		}

		if (StringUtils.isBlank(value)) {
			value = "null";
		}

		buffer.append(value);
	}

	/**
	 * 쿠팡에서 사용하는 쿠키 값 enum 여기에 있는 쿠키 이외에는 사용하지 안도록 한다.<br>
	 * TODO <font color="red">사용하지 않는 구키는 제외해야함</font>
	 * 
	 * @author 산토리니 윤영욱 (readytogo@coupang.com)
	 * @version 2013. 6. 12.
	 */
	public enum Cookies {
		/**
		 * CPUSR_DV , CPUSR_RL 이런식으로 PostFix 가 붙는데, DB에서 조회하고 있다. -> 현재 버전 DB 를 보지 않고, Spring profiles 값을 사용하도록 한다.
		 */
		LOGIN_USER("CPUSR") {
			private final int maxAge = 3600 * 24 * 365;

			@Override
			public void burnCookie(HttpServletResponse response, String value, boolean isRemember, Phase phase) {
				burnCookieProcess(response, getKey() + "_" + phase.getCookiePostFix(), value, isRemember, this.maxAge);
			}
		},
		REMEMBER_ME("rememberme") {
			private final int maxAge = 60 * 60 * 24 * 365;

			@Override
			public void burnCookie(HttpServletResponse response, String value, boolean isRemember, Phase phase) {
				burnCookieProcess(response, getKey(), (StringUtils.equals(value, "Y") ? value : ""), isRemember, this.maxAge);
			}
		},
		EMAIL("SUID") {
			private final int maxAge = 3600 * 24 * 30;

			@Override
			public void burnCookie(HttpServletResponse response, String value, boolean isRemember, Phase phase) {
				burnCookieProcess(response, getKey(), value, isRemember, this.maxAge);
			}
		},
		MEMERSRL("member_srl") {
			private final int maxAge = -1;

			@Override
			public void burnCookie(HttpServletResponse response, String value, boolean isRemember, Phase phase) {
				burnCookieProcess(response, getKey(), value, isRemember, this.maxAge);
			}

		},
		IS_INIT_LOGIN_USER("ILOGIN") {
			private final int maxAge = -1;

			@Override
			public void burnCookie(HttpServletResponse response, String value, boolean isRemember, Phase phase) {
				burnCookieProcess(response, getKey(), (StringUtils.equals(value, "Y") ? value : ""), isRemember, this.maxAge);
			}

		},
		IS_NEW_MEMBER("NVHP_YN") {
			private final int maxAge = 3600 * 24 * 365;

			@Override
			public void burnCookie(HttpServletResponse response, String value, boolean isRemember, Phase phase) {
				burnCookieProcess(response, getKey(), (StringUtils.equals(value, "Y") ? value : ""), isRemember, this.maxAge);
			}

		},
		// TODO 어디서 사용하는지 확인 필요
		GD1("gd1") {
			private final int maxAge = 1000 * 60 * 60 * 24 * 365;

			@Override
			public void burnCookie(HttpServletResponse response, String value, boolean isRemember, Phase phase) {
				burnCookieProcess(response, getKey(), value, isRemember, this.maxAge);
			}
		},
		RECENT_VISIT_COUPANG("RVCOUPANG_") {
			private final int maxAge = -1;

			@Override
			public void burnCookie(HttpServletResponse response, String value, boolean isRemember, Phase phase) {
				burnCookieProcess(response, getKey(), value, isRemember, this.maxAge);
			}
		};

		private final String cookieKey;

		Cookies(String cookieKey) {
			this.cookieKey = cookieKey;
		}

		public abstract void burnCookie(HttpServletResponse response, String value, boolean isRemember, Phase phase);

		public String getKey() {
			return this.cookieKey;
		}

		/**
		 * 쿠키 굽기
		 * 
		 * @param response
		 * @param key
		 * @param value
		 * @param isRemember
		 */
		private static void burnCookieProcess(HttpServletResponse response, String key, String value, boolean isRemember, int maxAge) {
			response.setHeader("P3P", "CP=\"NOI DEVa TAIa OUR BUS UNI\"");

			CookieCooker loginInfoCooker = CookieBox.getCooker(key, value);
			loginInfoCooker.path("/");
			loginInfoCooker.domain("." + Env.DEFAULT_HOST);
			loginInfoCooker.maxAge(isRemember ? maxAge : 0);
			loginInfoCooker.cook(response);
		}
	}
}
