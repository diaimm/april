package com.coupang.member.component.web.spring.interceptor;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.coupang.member.component.web.spring.interceptor.AgentDetectInterceptor.AgentInfoHolder;
import com.coupang.member.component.web.util.AgentInfo;

class AgentArgumentResolver implements HandlerMethodArgumentResolver, ApplicationContextAware {
	private ApplicationContext applicationContext;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return AgentInfo.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		return applicationContext.getBean(AgentInfoHolder.class).get();
	}

	/**
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
