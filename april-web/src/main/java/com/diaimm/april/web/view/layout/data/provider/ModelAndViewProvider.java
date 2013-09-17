/*
 * @fileName : HttpServletRequestProvider.java
 * @date : 2013. 6. 10.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.diaimm.april.web.view.layout.data.provider;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.diaimm.april.web.view.layout.data.LayoutDataProvider;

/**
 * @author diaimm
 * 
 */
public class ModelAndViewProvider implements LayoutDataProvider<ModelAndView> {
	/**
	 * @see com.diaimm.april.web.view.layout.data.LayoutDataProvider#valueType()
	 */
	@Override
	public Class<ModelAndView> valueType() {
		return ModelAndView.class;
	}

	/**
	 * @see com.diaimm.april.web.view.layout.data.LayoutDataProvider#getValue(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public ModelAndView getValue(HttpServletRequest request, ModelAndView modelAndView) {
		return modelAndView;
	}

}
