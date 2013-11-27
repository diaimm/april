/*
 * @fileName : ViewSelector.java
 * @date : 2013. 7. 2.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.web.view.selector;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author diaimm
 * 
 */
public interface ViewNameSelector {
	public String getViewName(HttpServletRequest request, ModelAndView modelAndView);
}
