/*
 * @fileName : MobileDetectingViewNameSelector.java
 * @date : 2013. 7. 2.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.diaimm.april.web.view.selector;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

/**
 * @author diaimm
 * 
 */
public class JspViewNameSelector implements ViewNameSelector {
	private static final String DEFAULT_POSTFIX = ".jsp";
	private static final String PREFIX = "/WEB-INF/views/jsp/";
	private String postFix = DEFAULT_POSTFIX;

	/**
	 * @see ViewNameSelector#getViewName(javax.servlet.http.HttpServletRequest,
	 *      org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public String getViewName(HttpServletRequest request, ModelAndView modelAndView) {
		return PREFIX + modelAndView.getViewName() + postFix;
	}
}
