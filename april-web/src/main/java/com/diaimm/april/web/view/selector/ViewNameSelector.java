/*
 * @fileName : ViewSelector.java
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
public interface ViewNameSelector {
	public String getViewName(HttpServletRequest request, String viewName);
}
