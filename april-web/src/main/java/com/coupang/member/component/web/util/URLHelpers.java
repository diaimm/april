/**
 * 
 */
package com.coupang.member.component.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.coupang.member.commons.ByPhase;
import com.coupang.member.commons.Env;
import com.coupang.member.commons.property.base.BaseURLProperties;

/**
 * 화면에서 사용하는 변수 정의
 * 
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 6. 7.
 */
public enum URLHelpers {
	CURRENT_PATH {
		@Override
		public String getValue(ByPhase byPhase, HttpServletRequest request) {
			String requestUri = (String)request.getAttribute("javax.servlet.forward.request_uri");
			if (StringUtils.isBlank(requestUri)) {
				requestUri = request.getRequestURI();
			}
			return requestUri;
		}
	},

	CURRENT_URL_BASE {
		@Override
		public String getValue(ByPhase byPhase, HttpServletRequest request) {
			String scheme = request.getScheme();
			int serverPort = request.getServerPort();

			StringBuilder ret = new StringBuilder(scheme).append("://").append(request.getServerName());
			if (("http".equals(scheme) && serverPort != 80) || "https".equals(scheme) && serverPort != 443) {
				ret.append(":").append(serverPort);
			}

			ret.append(CURRENT_PATH.getValue(byPhase, request));

			return ret.toString();
		}
	},

	CURRENT_URL {
		@Override
		public String getValue(ByPhase byPhase, HttpServletRequest request) {
			return CURRENT_URL_BASE.getValue(byPhase, request) + this.getQueryString(request);
		}

		/**
		 * Get parameter Query String
		 * 
		 * @param request
		 * @param urlBuf
		 * @return
		 */
		private String getQueryString(HttpServletRequest request) {
			StringBuffer urlBuf = new StringBuffer();
			Enumeration<?> names = request.getParameterNames();
			int cnt = 0;
			while (names.hasMoreElements()) {
				String name = (String)names.nextElement();
				if (cnt++ == 0) {
					urlBuf.append('?');
				} else {
					urlBuf.append('&');
				}
				if (!"rtnUrl".equals(name)) {
					String[] values = request.getParameterValues(name);
					if (values != null && values.length != 0) {
						for (int i = 0; i < values.length; i++) {
							if (i > 0) {
								urlBuf.append('&');
							}
							urlBuf.append(name);
							urlBuf.append('=');
							try {
								urlBuf.append(URLEncoder.encode(values[i], Env.DEFAULT_ENCODING));
							} catch (UnsupportedEncodingException e) {
								return urlBuf.toString();
							}
						}
					}
				}
			}

			return urlBuf.toString();
		}
	},

	LOGIN_URL_BASE {
		@Override
		public String getValue(ByPhase byPhase, HttpServletRequest request) {
			return byPhase.getProperty(BaseURLProperties.LOGIN) + "/login";
		}
	},

	COUPANG_URL_BASE {
		@Override
		public String getValue(ByPhase byPhase, HttpServletRequest request) {
			return byPhase.getProperty(BaseURLProperties.COUPANG);
		}
	},

	MCOUPANG_URL_BASE {
		@Override
		public String getValue(ByPhase byPhase, HttpServletRequest request) {
			return byPhase.getProperty(BaseURLProperties.MCOUPANG);
		}
	},

	SING_URL_BASE {
		@Override
		public String getValue(ByPhase byPhase, HttpServletRequest request) {
			return byPhase.getProperty(BaseURLProperties.SING);
		}
	},

	WING_URL_BASE {
		@Override
		public String getValue(ByPhase byPhase, HttpServletRequest request) {
			return byPhase.getProperty(BaseURLProperties.WING);
		}
	},

	CDN_URL_BASE {
		@Override
		public String getValue(ByPhase byPhase, HttpServletRequest request) {
			String[] cdnPathsArray = byPhase.getProperties(BaseURLProperties.IMAGE);
			if (ArrayUtils.isNotEmpty(cdnPathsArray)) {
				if (cdnPathsArray.length > 1) {
					return cdnPathsArray[(int)(Math.random() * cdnPathsArray.length)];
				} else {
					return cdnPathsArray[0];
				}
			}

			return "";
		}
	},

	CDN_URL_SWICH_BY_SCHEME {
		@Override
		public String getValue(ByPhase byPhase, HttpServletRequest request) {
			String scheme = request.getScheme();
			int serverPort = request.getServerPort();

			if ("http".equals(scheme)) {
				return byPhase.getProperty(BaseURLProperties.IMAGE_STATIC);
			} else if ("https".equals(scheme) && serverPort == 443) {
				return byPhase.getProperty(BaseURLProperties.IMAGE_SSL);
			} else {
				return "";
			}
		}
	},

	RETURN_URL {
		@Override
		public String getValue(ByPhase byPhase, HttpServletRequest request) {
			String rtnUrl = StringUtils.trimToEmpty(request.getParameter("rtnUrl"));

			return StringUtils.isBlank(rtnUrl) ? DEFAULT_RETURN_URL.getValue(byPhase, request) : rtnUrl;
		}
	},

	DEFAULT_RETURN_URL {
		@Override
		public String getValue(ByPhase byPhase, HttpServletRequest request) {
			return byPhase.getProperty(BaseURLProperties.DEFAULT_RETURN_URL);
		}

	};

	/**
	 * @param platformProperties
	 * @param request
	 * @return
	 */
	public abstract String getValue(ByPhase byPhase, HttpServletRequest request);
}