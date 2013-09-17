/*
 * @fileName : ControllerTimeCheckInterceptor.java
 * @date : 2013. 6. 4.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.diaimm.april.web.spring.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author diaimm
 * 
 */
public class RequestTimeCheckInterceptor implements HandlerInterceptor {
	private Logger logger = LoggerFactory.getLogger(RequestTimeCheckInterceptor.class);

	/**
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		request.setAttribute(TimeCheckAttributeKeys.PRE, System.currentTimeMillis());
		return true;
	}

	/**
	 * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		request.setAttribute(TimeCheckAttributeKeys.POST, System.currentTimeMillis());
	}

	/**
	 * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		long currentTimeMillis = System.currentTimeMillis();

		long startTime = (Long) getTimeSafe(request, TimeCheckAttributeKeys.PRE);
		long controllTime = (Long) getTimeSafe(request, TimeCheckAttributeKeys.POST);
		long afterRendering = currentTimeMillis;

		StringBuffer log = new StringBuffer();
		log.append("requestUri : " + request.getRequestURI() + ", Controller Time : " + (controllTime - startTime) + "(msecs) , Html Time : " + (afterRendering - controllTime)
				+ " (msecs) , Total Time : " + (afterRendering - startTime) + " (msecs)");

		logger.debug(log.toString());
	}

	private Object getTimeSafe(HttpServletRequest request, String key) {
		Object attribute = request.getAttribute(key);
		if (attribute == null) {
			return 0L;
		}

		return attribute;
	}

	private static final class TimeCheckAttributeKeys {
		private static final String PRE = TimeCheckAttributeKeys.class.getCanonicalName() + "_PRE";
		private static final String POST = TimeCheckAttributeKeys.class.getCanonicalName() + "_POST";
	}
}
