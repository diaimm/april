package com.diaimm.april.web.spring.interceptor;

import java.lang.annotation.Annotation;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.diaimm.april.commons.util.JaxbObjectMapper;
import com.diaimm.april.web.xss.antisami.AntiXSSRequestWrapper;

public class JsonWebArgumentResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Annotation[] annotations = parameter.getParameterAnnotations();
		for (Annotation annotation : annotations) {
			if (Json.class.isInstance(annotation))
				return true;
		}
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer arg1, NativeWebRequest webRequest, WebDataBinderFactory arg3) throws Exception {
		Json jsonAnnotation = methodParameter.getParameterAnnotation(Json.class);
		if (jsonAnnotation == null) {
			return null;
		}
		HttpServletRequest request = (HttpServletRequest)getOriginalServletRequest(webRequest);
		String jsonParamName = jsonAnnotation.value();
		String jsonParamValue = request.getParameter(jsonParamName);
		if (jsonParamValue == null) {
			throw new NullPointerException("[" + jsonParamName + "] 의 value가 없습니다.");
		}
		return JaxbObjectMapper.JSON.objectify(jsonParamValue, methodParameter.getParameterType());
	}

	private ServletRequest getOriginalServletRequest(NativeWebRequest webRequest) {
		Object nativeRequest = webRequest.getNativeRequest();
		if (nativeRequest instanceof AntiXSSRequestWrapper) {
			return ((AntiXSSRequestWrapper)nativeRequest).getRequest();
		}
		return (ServletRequest)webRequest.getNativeRequest();
	}

}
