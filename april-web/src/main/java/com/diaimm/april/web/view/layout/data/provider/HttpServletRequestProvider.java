/*
 * @fileName : HttpServletRequestProvider.java
 * @date : 2013. 6. 10.
 * @author : diaimm.
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
public class HttpServletRequestProvider implements LayoutDataProvider<HttpServletRequest> {
	/**
	 * @see com.diaimm.april.web.view.layout.data.LayoutDataProvider#valueType()
	 */
	@Override
	public Class<HttpServletRequest> valueType() {
		return HttpServletRequest.class;
	}

	/**
	 * @see com.diaimm.april.web.view.layout.data.LayoutDataProvider#getValue(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public HttpServletRequest getValue(HttpServletRequest request, ModelAndView modelAndView) {
		return request;
	}

}
