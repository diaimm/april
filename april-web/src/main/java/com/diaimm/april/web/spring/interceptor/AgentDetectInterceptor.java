package com.diaimm.april.web.spring.interceptor;

import com.diaimm.april.web.util.AgentInfo;
import com.diaimm.april.web.util.CookieBox;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.Override;
import java.util.ArrayList;
import java.util.List;

/**
 * 모바일 APP, 모바일 WEB, WEB 등 Agent 를 판단하여 <br>
 * prototype 으로 생성된 AgentDetectInterceptor 객체가 가지고 있음<br>
 * 파라미터 리졸버를 통해서 Controller 에서 Agent 객체를 받아서 사용가능함<br>
 * <br>
 * 
 * @version 2013. 7. 30.
 */
public class AgentDetectInterceptor implements HandlerInterceptor, InitializingBean, BeanFactoryAware {
	private BeanFactory beanFactory;
	private String forcedPCViewCookieName = "FoWe";

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
		BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry)beanFactory;

		initAgentInfoHolderBean(beanDefinitionRegistry);
		initArgumentResolver(beanDefinitionRegistry);
	}

	/**
	 * @param beanDefinitionRegistry
	 */
	private void initAgentInfoHolderBean(BeanDefinitionRegistry beanDefinitionRegistry) {
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(DefaultAgentInfoHolder.class);
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

		List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>(
			requestMappingHandlerAdapter.getArgumentResolvers().getResolvers());
		argumentResolvers.add(0, agentArgumentResolver);

		requestMappingHandlerAdapter.setArgumentResolvers(argumentResolvers);
		requestMappingHandlerAdapter.setInitBinderArgumentResolvers(argumentResolvers);
	}

	public void setForcedPCViewCookieName(String forcedPCViewCookieName) {
		this.forcedPCViewCookieName = forcedPCViewCookieName;
	}

	public static interface AgentInfoHolder {
		public void init(HttpServletRequest request, HttpServletResponse response);

		public AgentInfo get();
	}

	private class DefaultAgentInfoHolder implements AgentInfoHolder {
		private AgentInfo agentInfo;

		DefaultAgentInfoHolder() {
		}

		@Override
		public void init(HttpServletRequest request, HttpServletResponse response) {
			this.agentInfo = new AgentInfo(request);

			// 모바일인 경우 AentInfo 에 필요한 정보를 넣어준다.
			if (agentInfo.isMobile()) {
				CookieBox cookieBox = new CookieBox(request);
				String referer = request.getHeader("referer");

				agentInfo.setIsforceAppToWeb("Y".equals(cookieBox.getValue(AgentDetectInterceptor.this.forcedPCViewCookieName)));
			}
		}

		/**
		 * @return the agentInfo
		 */
		@Override
		public AgentInfo get() {
			return agentInfo;
		}
	}
}