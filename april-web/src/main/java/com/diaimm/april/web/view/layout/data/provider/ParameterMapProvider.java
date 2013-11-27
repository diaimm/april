/*
 * @fileName : ParameterMapProvider.java
 * @date : 2013. 6. 10.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.web.view.layout.data.provider;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.diaimm.april.web.view.layout.data.LayoutDataProvider;

/**
 * @author diaimm
 * 
 */
@SuppressWarnings("rawtypes")
public class ParameterMapProvider implements LayoutDataProvider<Map> {
	/**
	 * @see com.diaimm.april.web.view.layout.data.LayoutDataProvider#valueType()
	 */
	@Override
	public Class<Map> valueType() {
		return Map.class;
	}

	/**
	 * @see com.diaimm.april.web.view.layout.data.LayoutDataProvider#getValue(javax.servlet.http.HttpServletRequest,
	 *      org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public Map<?, ?> getValue(HttpServletRequest request, ModelAndView modelAndView) {
		return request.getParameterMap();
	}
}
