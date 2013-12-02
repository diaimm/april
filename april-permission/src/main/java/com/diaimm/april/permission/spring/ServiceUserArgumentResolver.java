/*
 * @fileName : ServiceUserArgumentResolver.java
 * @date : 2013. 6. 5.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.permission.spring;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.diaimm.april.permission.model.ServiceUser;

/**
 * @author diaimm
 * 
 */
class ServiceUserArgumentResolver implements HandlerMethodArgumentResolver {
	/**
	 * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#supportsParameter(org.springframework.core.MethodParameter)
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return ServiceUser.class.isAssignableFrom(parameter.getParameterType());
	}

	/**
	 * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#resolveArgument(org.springframework.core.MethodParameter,
	 *      org.springframework.web.method.support.ModelAndViewContainer, org.springframework.web.context.request.NativeWebRequest,
	 *      org.springframework.web.bind.support.WebDataBinderFactory)
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory) throws Exception {
		return (ServiceUser)webRequest.getAttribute(ServiceUserInterceptor.USER_MODEL_ATTRIBUTE_KEY, NativeWebRequest.SCOPE_REQUEST);
	}
}
