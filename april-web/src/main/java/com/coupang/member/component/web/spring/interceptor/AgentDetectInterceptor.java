package com.coupang.member.component.web.spring.interceptor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.coupang.member.commons.Env;
import com.coupang.member.component.web.util.AgentInfo;
import com.coupang.member.component.web.util.CookieBox;
import com.coupang.member.component.web.util.CookieBox.CookieCooker;

/**
 * 모바일 APP, 모바일 WEB, WEB 등 Agent 를 판단하여 <br>
 * prototype 으로 생성된 AgentDetectInterceptor 객체가 가지고 있음<br>
 * 파라미터 리졸버를 통해서 Controller 에서 Agent 객체를 받아서 사용가능함<br>
 * <br>
 * 
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 7. 30.
 */
public class AgentDetectInterceptor implements HandlerInterceptor, InitializingBean, BeanFactoryAware {
	private BeanFactory beanFactory;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		AgentInfoHolder agentHolder = beanFactory.getBean(AgentInfoHolder.class);
		agentHolder.init(request, response);

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		// do nothing
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// do nothing
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) beanFactory;

		initAgentInfoHolderBean(beanDefinitionRegistry);
		initArgumentResolver(beanDefinitionRegistry);
	}

	/**
	 * @param beanDefinitionRegistry
	 */
	private void initAgentInfoHolderBean(BeanDefinitionRegistry beanDefinitionRegistry) {
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(AgentInfoHolder.class);
		beanDefinitionBuilder.setScope("request");
		beanDefinitionRegistry.registerBeanDefinition("agentInfoHolder", beanDefinitionBuilder.getBeanDefinition());
	}

	/**
	 * @param beanDefinitionRegistry
	 */
	private void initArgumentResolver(BeanDefinitionRegistry beanDefinitionRegistry) {
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(AgentArgumentResolver.class);
		beanDefinitionRegistry.registerBeanDefinition("agentArgumentResolver", beanDefinitionBuilder.getBeanDefinition());

		AgentArgumentResolver agentArgumentResolver = beanFactory.getBean(AgentArgumentResolver.class);
		RequestMappingHandlerAdapter requestMappingHandlerAdapter = beanFactory.getBean(RequestMappingHandlerAdapter.class);

		List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>(requestMappingHandlerAdapter
				.getArgumentResolvers().getResolvers());
		argumentResolvers.add(0, agentArgumentResolver);

		requestMappingHandlerAdapter.setArgumentResolvers(argumentResolvers);
		requestMappingHandlerAdapter.setInitBinderArgumentResolvers(argumentResolvers);
	}

	public static class AgentInfoHolder {
		private AgentInfo agentInfo;

		private void init(HttpServletRequest request, HttpServletResponse response) {
			this.agentInfo = new AgentInfo(request);

			// 모바일인 경우 AentInfo 에 필요한 정보를 넣어준다.
			if (agentInfo.isMobile()) {
				CookieBox cookieBox = new CookieBox(request);
				String referer = request.getHeader("referer");

				/*
				 * 01. 강제로 Mobile 에서 Web 으로 표시 여부 결정
				 */
				if (agentInfo.isIPad()) {
					agentInfo.setIsforceAppToWeb(true);
					Cookies.FORCE_WEB_VIEW.burnCookie(response, "Y", true);
				} else if (StringUtils.isBlank(referer) || !referer.contains("coupang.com")) {
					agentInfo.setIsforceAppToWeb(false);
					Cookies.FORCE_WEB_VIEW.burnCookie(response, "N", true);
				} else {
					String forceAppToWebYn = cookieBox.getValue(Cookies.FORCE_WEB_VIEW.getKey());
					if (StringUtils.equalsIgnoreCase(forceAppToWebYn, "Y")) {
						agentInfo.setIsforceAppToWeb(true);
					}
				}

				/*
				 * 02. App : Mobile App2.0 Hybrid by hayden
				 */
				String isApp = cookieBox.getValue(Cookies.IS_APP.getKey());
				if (StringUtils.equalsIgnoreCase(isApp, "Y")) {
					agentInfo.setApp(true);
					agentInfo.setAppReturnUrl(cookieBox.getValue(Cookies.APP_RETURN_URI.getKey()));
					agentInfo.setAppUuid(cookieBox.getValue(Cookies.APP_RETURN_URI.getKey()));
				}
			}
		}

		/**
		 * @return the agentInfo
		 */
		public AgentInfo get() {
			return agentInfo;
		}
	}

	/**
	 * Agent 판단을 위한 쿠키<br>
	 * <br>
	 * 
	 * @author 산토리니 윤영욱 (readytogo@coupang.com)
	 * @version 2013. 7. 30.
	 */
	public enum Cookies {
		FORCE_WEB_VIEW("FoWe") {
			private final int maxAge = -1;

			@Override
			public void burnCookie(HttpServletResponse response, String value, boolean isRemember) {
				burnCookieProcess(response, getKey(), value, isRemember, this.maxAge);
			}
		},
		IS_APP("ISAPP") {
			private final int maxAge = -1;

			@Override
			public void burnCookie(HttpServletResponse response, String value, boolean isRemember) {
				burnCookieProcess(response, getKey(), value, isRemember, this.maxAge);
			}
		},
		APP_RETURN_URI("returnAppURI") {
			private final int maxAge = -1;

			@Override
			public void burnCookie(HttpServletResponse response, String value, boolean isRemember) {
				burnCookieProcess(response, getKey(), value, isRemember, this.maxAge);
			}
		},
		APP_UUID("UUID") {
			private final int maxAge = -1;

			@Override
			public void burnCookie(HttpServletResponse response, String value, boolean isRemember) {
				burnCookieProcess(response, getKey(), value, isRemember, this.maxAge);
			}
		};

		private final String cookieKey;

		Cookies(String cookieKey) {
			this.cookieKey = cookieKey;
		}

		public abstract void burnCookie(HttpServletResponse response, String value, boolean isRemember);

		public String getKey() {
			return this.cookieKey;
		}

		/**
		 * 쿠키 굽기
		 * 
		 * @param response
		 * @param key
		 * @param value
		 * @param isRemember
		 */
		private static void burnCookieProcess(HttpServletResponse response, String key, String value, boolean isRemember, int maxAge) {
			response.setHeader("P3P", "CP=\"NOI DEVa TAIa OUR BUS UNI\"");

			CookieCooker loginInfoCooker = CookieBox.getCooker(key, value);
			loginInfoCooker.path("/");
			loginInfoCooker.domain("." + Env.DEFAULT_HOST);
			loginInfoCooker.maxAge(isRemember ? maxAge : 0);
			loginInfoCooker.cook(response);
		}
	}
}