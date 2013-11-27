/*
 * @fileName : LayoutDataProvider.java
 * @date : 2013. 6. 10.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.web.view.layout.data;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author diaimm
 * 
 */
public interface LayoutDataProvider<V> {
	Class<? extends V> valueType();

	V getValue(HttpServletRequest request, ModelAndView modelAndView);

	public static class LazyLoader {
		@SuppressWarnings("unchecked")
		public static <T> T wrap(Class<? extends T> targetType, final LayoutDataProvider<?> provider, final HttpServletRequest request,
				final ModelAndView modelAndViewIn) {
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(targetType);
			enhancer.setCallback(new MethodInterceptor() {
				private LayoutDataProvider<?> dataProvider = provider;
				private HttpServletRequest httpServletRequest = request;
				private ModelAndView modelAndView = modelAndViewIn;
				private Object data = null;

				@Override
				public Object intercept(Object callInstance, Method targetMethod, Object[] params, MethodProxy methodProxy) throws Throwable {
					if (this.data == null) {
						this.data = dataProvider.getValue(httpServletRequest, modelAndView);
					}

					Object invokeResult2 = methodProxy.invoke(data, params);
					return invokeResult2;
				}

			});

			try {
				return (T) enhancer.create();
			} catch (Exception e) {
				// proxy 생성에 실패하면 원래 값으로 그냥 리턴한다.
				return (T) provider.getValue(request, modelAndViewIn);
			}
		}
	}
}
