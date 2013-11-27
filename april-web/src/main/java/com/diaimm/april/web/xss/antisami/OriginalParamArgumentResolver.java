/*
 * @fileName : OriginalHttpServletRequestWebArgumentResolver.java
 * @date : 2013. 6. 10.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.web.xss.antisami;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.ServletRequest;

/**
 * @author diaimm
 * 
 */
public class OriginalParamArgumentResolver implements HandlerMethodArgumentResolver {
	/**
	 * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#supportsParameter(org.springframework.core.MethodParameter)
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Original originalAnnotation = parameter.getParameterAnnotation(Original.class);
		return originalAnnotation != null && originalAnnotation.original();
	}

	/**
	 * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#resolveArgument(org.springframework.core.MethodParameter,
	 *      org.springframework.web.method.support.ModelAndViewContainer, org.springframework.web.context.request.NativeWebRequest,
	 *      org.springframework.web.bind.support.WebDataBinderFactory)
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory) throws Exception {
		if (String.class.isAssignableFrom(parameter.getParameterType())) {
			return getOriginalServletRequest(webRequest).getParameter(getParamName(parameter));
		} else if (String[].class.isAssignableFrom(parameter.getParameterType())) {
			return getOriginalServletRequest(webRequest).getParameterValues(getParamName(parameter));
		}
		return null;
	}

	/**
	 * @param webRequest
	 * @return
	 */
	private ServletRequest getOriginalServletRequest(NativeWebRequest webRequest) {
		Object nativeRequest = webRequest.getNativeRequest();
		if (nativeRequest instanceof AntiXSSRequestWrapper) {
			return ((AntiXSSRequestWrapper)nativeRequest).getRequest();
		}
		return (ServletRequest)webRequest.getNativeRequest();
	}

	/**
	 * @param parameter
	 * @return
	 */
	private String getParamName(MethodParameter parameter) {
		if (parameter.hasParameterAnnotation(Original.class)) {
			Original parameterAnnotation = parameter.getParameterAnnotation(Original.class);
			if (StringUtils.isNotBlank(parameterAnnotation.value())) {
				return parameterAnnotation.value();
			}
		}

		return parameter.getParameterName();
	}
}
