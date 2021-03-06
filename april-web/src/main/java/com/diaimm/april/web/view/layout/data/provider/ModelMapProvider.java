/*
 * @fileName : HttpServletRequestProvider.java
 * @date : 2013. 6. 10.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.web.view.layout.data.provider;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.diaimm.april.web.view.layout.data.LayoutDataProvider;

/**
 * @author diaimm
 * 
 */
public class ModelMapProvider implements LayoutDataProvider<ModelMap> {
	/**
	 * @see com.diaimm.april.web.view.layout.data.LayoutDataProvider#valueType()
	 */
	@Override
	public Class<ModelMap> valueType() {
		return ModelMap.class;
	}

	/**
	 * @see com.diaimm.april.web.view.layout.data.LayoutDataProvider#getValue(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public ModelMap getValue(HttpServletRequest request, ModelAndView modelAndView) {
		return modelAndView.getModelMap();
	}

}
