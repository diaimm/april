/**
 * @fileName : SitemeshLayoutHandlerInterceptor.java
 * @date : 2013. 5. 22.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.web.view.layout.sitemesh;

import com.diaimm.april.web.view.layout.aop.AbstractLayoutHandlerInterceptor;
import com.diaimm.april.web.view.layout.bean.LayoutConfig;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author diaimm
 */
public class SitemeshLayoutHandlerInterceptor extends AbstractLayoutHandlerInterceptor {
	private static final String DECORATOR_NAME_ATTRIBUTE_KEY = "controllerConfigDecorator";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setContentsBodyView(HttpServletRequest request, ModelAndView modelAndView, LayoutConfig layoutConfig) {
		modelAndView.setViewName(getViewNameSelector().getViewName(request, modelAndView.getViewName()));
	}

	@Override
	public void handoverLayoutConfigName(HttpServletRequest request, ModelAndView modelAndView, String layoutName) {
		request.setAttribute(DECORATOR_NAME_ATTRIBUTE_KEY, layoutName);
	}
}
