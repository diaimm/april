/*
 * @fileName : MobileDetectingViewNameSelector.java
 * @date : 2013. 7. 2.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.web.view.selector;

import javax.servlet.http.HttpServletRequest;

/**
 * @author diaimm
 * 
 */
public class JspViewNameSelector implements ViewNameSelector {
	private static final String DEFAULT_POSTFIX = ".jsp";
	private static final String PREFIX = "/WEB-INF/views/jsp/";
	private String postFix = DEFAULT_POSTFIX;

	/**
	 * @see ViewNameSelector#getViewName(javax.servlet.http.HttpServletRequest, java.lang.String)
	 */
	@Override
	public String getViewName(HttpServletRequest request, String viewName) {
		return PREFIX + viewName + postFix;
	}
}
