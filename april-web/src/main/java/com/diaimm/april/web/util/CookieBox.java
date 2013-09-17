/**
 * 
 */
package com.diaimm.april.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.diaimm.april.commons.Env;

/**
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 6. 3.
 */
public final class CookieBox {
	private static final String COOKIE_BOX_ATTRIBUTE_KEY = CookieBox.class.getCanonicalName() + "_COOKIE_INFO";
	private Map<String, Cookie> cookieMap = null;

	public CookieBox(HttpServletRequest request) {
		this.cookieMap = initializeCookieInfos(request);
	}

	/**
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Cookie> initializeCookieInfos(HttpServletRequest request) {
		if (request.getAttribute(COOKIE_BOX_ATTRIBUTE_KEY) == null) {
			Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (int i = 0; i < cookies.length; i++) {
					cookieMap.put(cookies[i].getName(), cookies[i]);
				}
			}

			request.setAttribute(COOKIE_BOX_ATTRIBUTE_KEY, cookieMap);
			return cookieMap;
		}

		return (Map<String, Cookie>) request.getAttribute(COOKIE_BOX_ATTRIBUTE_KEY);
	}

	public static CookieCooker getCooker(String name, String value) {
		return new CookieCooker(name, value);
	}

	public Cookie getCookie(String name) {
		return (Cookie) cookieMap.get(name);
	}

	public String getValue(String name, String defaultValue) {
		String ret = getValue(name);
		if (ret == null) {
			return defaultValue;
		}

		return ret;
	}

	public String getValue(String name) {
		Cookie cookie = (Cookie) cookieMap.get(name);
		if (cookie == null) {
			return null;
		}

		try {
			return URLDecoder.decode(cookie.getValue(), Env.DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			// 발생하지 않음
		}

		return null;
	}

	public boolean exists(String name) {
		return cookieMap.get(name) != null;
	}

	/**
	 * cookie builder
	 * 
	 * @author diaimm
	 */
	public static class CookieCooker {
		private String name;
		private String value;
		private Integer maxAge;
		private String path;
		private String domain;

		private CookieCooker(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public CookieCooker maxAge(int maxAge) {
			this.maxAge = maxAge;
			return this;
		}

		public CookieCooker path(String path) {
			this.path = path;
			return this;
		}

		public CookieCooker domain(String domain) {
			this.domain = domain;
			return this;
		}

		/**
		 * 쿠키를 생성합니다.
		 * 
		 * @return
		 */
		public Cookie build() {
			String encodedValue;
			try {
				encodedValue = URLEncoder.encode(value, Env.DEFAULT_ENCODING);
			} catch (UnsupportedEncodingException e) {
				// 발생하지 않음,
				encodedValue = value;
			}
			Cookie cookie = new Cookie(name, encodedValue);

			if (domain != null) {
				cookie.setDomain(domain);
			}

			if (path != null) {
				cookie.setPath(path);
			}

			if (maxAge != null) {
				cookie.setMaxAge(maxAge);
			}

			return cookie;
		}

		/**
		 * cookie를 굽고, 구워진 쿠키를 return 합니다.
		 * 
		 * @param response
		 * @return
		 */
		public Cookie cook(HttpServletResponse response) {
			Cookie cookie = this.build();
			response.addCookie(cookie);

			return cookie;
		}
	}
}
