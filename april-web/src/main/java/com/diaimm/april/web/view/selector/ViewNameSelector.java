/*
 * @fileName : ViewSelector.java
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
public interface ViewNameSelector {
	public String getViewName(HttpServletRequest request, ModelAndView modelAndView);
}
