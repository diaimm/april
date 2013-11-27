/**
 * @fileName : SitemeshLayoutHandlerInterceptor.java
 * @date : 2013. 5. 22.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.web.view.layout.sitemesh;

import javax.servlet.http.HttpServletRequest;

import com.diaimm.april.web.view.layout.aop.AbstractLayoutHandlerInterceptor;
import com.diaimm.april.web.view.layout.bean.LayoutConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.servlet.ModelAndView;

import com.diaimm.april.web.view.layout.aop.AbstractLayoutHandlerInterceptor;
import com.diaimm.april.web.view.layout.bean.LayoutConfig;

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
		modelAndView.setViewName(getViewNameSelector().getViewName(request, modelAndView));
	}

	/**
	 * @see com.diaimm.april.web.view.layout.aop.AbstractLayoutHandlerInterceptor#handoverLayoutName(org.springframework.web.servlet.ModelAndView,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void handoverLayoutConfigName(HttpServletRequest request, ModelAndView modelAndView, String layoutName) {
		request.setAttribute(DECORATOR_NAME_ATTRIBUTE_KEY, layoutName);
	}
}
