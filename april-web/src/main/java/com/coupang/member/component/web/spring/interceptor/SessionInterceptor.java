/**
 * 
 */
package com.coupang.member.component.web.spring.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.coupang.member.component.web.util.SessionUtils;

/**
 * 쿠키에 세션키를 저장하여 동일 환경에 의한 접속인지 판단
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 6. 4.
 */
public class SessionInterceptor implements HandlerInterceptor {
	public static final String COOKIE_NAME = "sid";
	public static final String SESSION_KEY = SessionInterceptor.class.getCanonicalName() + "_KEY";
	public static final String TICKET = "TICKET";

	/**
	 * in seconds.
	 */
	final int expireTime = 5 * 24 * 3600;

	// final int diaryExpireTime = 30 * 24 * 3600;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String sid = getSessionId(request);

		// 세션 유효성 검사
		if (SessionUtils.isValid(sid) == false) {
			sid = SessionUtils.generate();
		}

		// Send cookie
		Cookie newCookie = new Cookie(COOKIE_NAME, sid);
		newCookie.setDomain("coupang.com");
		newCookie.setPath("/");
		newCookie.setMaxAge(expireTime);

		// if ("FRONT".equals(PlatformPropertiesValueUtil.getServiceType())) {
		// newCookie.setMaxAge(expireTime);
		// } else {
		// newCookie.setMaxAge(diaryExpireTime);
		// }

		response.addCookie(newCookie);

		// set attribute
		request.setAttribute(SESSION_KEY, sid);

		return true;
	}

	public static String getSessionId(HttpServletRequest request) {
		return getSessionId(request, "");
	}

	static String getSessionId(HttpServletRequest request, String defaultValue) {
		Cookie cookie = findByName(request.getCookies(), COOKIE_NAME);
		return cookie == null ? defaultValue : cookie.getValue();
	}

	static Cookie findByName(Cookie[] cookies, String name) {
		if (cookies != null) {
			for (Cookie each : cookies) {
				if (each.getName().equals(COOKIE_NAME)) {
					return each;
				}
			}
		}
		return null;
	}

	/**
	 *  @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}

	/**
	 *  @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
	}
}