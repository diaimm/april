/*
 * @fileName : ServiceUserInterceptor.java
 * @date : 2013. 6. 4.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.permission.spring;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.diaimm.april.permission.model.ServiceUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.coupang.commons.ByPhase;
import com.coupang.commons.crypto.AES.AESType;
import com.coupang.commons.crypto.Crypto;
import com.coupang.commons.web.util.CookieBox;
import com.diaimm.april.permission.ServiceUserConstantsAware;
import com.diaimm.april.permission.model.CookieBaseServiceUserFactory;
import com.diaimm.april.permission.model.ServiceUser;
import com.diaimm.april.permission.spring.ServiceUserAuthCookieValueHandler.Cookies;
import com.google.common.io.Closeables;

/**
 * @author diaimm
 * 
 */
public class ServiceUserInterceptor implements HandlerInterceptor, ServiceUserConstantsAware, BeanFactoryAware, InitializingBean {
	public static final String USER_MODEL_ATTRIBUTE_KEY = ServiceUserInterceptor.class.getCanonicalName() + "_USER";
	private BeanFactory beanFactory;

	@Autowired
	private ByPhase byPhase;

	/**
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		ServiceUser serviceUser = CookieBaseServiceUserFactory.DEFAULT_USER;

		String encryptedUserInfo = new CookieBox(request).getValue(ServiceUserAuthCookieValueHandler.Cookies.LOGIN_USER.getKey() + "_" + byPhase.getCookiePostFix());

		// 쿠키 값이 존재할 경우
		try {
			if (StringUtils.isNotBlank(encryptedUserInfo)) {
				String orgCookieValue = Crypto.aes(AESType.AES128).decrypt(encryptedUserInfo, ServiceUserAuthCookieValueHandler.USER_INFO_CRYPT_KEY);
				serviceUser = ServiceUserAuthCookieValueHandler.toServiceUser(orgCookieValue, request);
			}
		} catch (Exception e) {
		}

		request.setAttribute(USER_MODEL_ATTRIBUTE_KEY, serviceUser);

		// 로그인 안하거나 로그인 정보 취득에 실패한 사용자
		if (CookieBaseServiceUserFactory.DEFAULT_USER.equals(serviceUser)) {
			removeAuthCookie(response);
		} else {
			// 로그인 유지를 위하여 쿠키를 다시 쓴다.
			keepAuthCookie(response, serviceUser);
		}

		return true;
	}

	/**
	 * 로그인 쿠키 삭제
	 */
	private void removeAuthCookie(HttpServletResponse response) {
		ServiceUserAuthCookieValueHandler.Cookies.LOGIN_USER.burnCookie(response, "", false, byPhase.getPhase());
		ServiceUserAuthCookieValueHandler.Cookies.MEMERSRL.burnCookie(response, "", false, byPhase.getPhase());
	}

	/**
	 * 로그인 쿠기 유지
	 * 
	 * @param response
	 * @param serviceUser
	 * @throws Exception
	 */
	private void keepAuthCookie(HttpServletResponse response, ServiceUser serviceUser) throws Exception {
		ServiceUserAuthCookieValueHandler.Cookies.LOGIN_USER.burnCookie(response, ServiceUserAuthCookieValueHandler.toCookeiValue(serviceUser), true, byPhase.getPhase());
		ServiceUserAuthCookieValueHandler.Cookies.MEMERSRL.burnCookie(response, serviceUser.getMemberId(), true, byPhase.getPhase());
	}

	/**
	 * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	/**
	 * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

	/**
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Bean
	private ServiceUserArgumentResolver getServiceUserArgumentResolver() {
		return new ServiceUserArgumentResolver();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(ServiceUserArgumentResolver.class);
		BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry)beanFactory;
		beanDefinitionRegistry.registerBeanDefinition("serviceUserArgumentResolver", beanDefinitionBuilder.getBeanDefinition());
		ServiceUserArgumentResolver serviceUserArgumentResolver = beanFactory.getBean(ServiceUserArgumentResolver.class);

		RequestMappingHandlerAdapter requestMappingHandlerAdapter = beanFactory.getBean(RequestMappingHandlerAdapter.class);
		List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>(requestMappingHandlerAdapter.getArgumentResolvers().getResolvers());
		argumentResolvers.add(0, serviceUserArgumentResolver);

		requestMappingHandlerAdapter.setArgumentResolvers(argumentResolvers);
		requestMappingHandlerAdapter.setInitBinderArgumentResolvers(argumentResolvers);

		// Load Cookie key.dat
		InputStream input = new FileInputStream(ServiceUserAuthCookieValueHandler.USER_INFO_CRYPT_KEY_FILE);
		ServiceUserAuthCookieValueHandler.USER_INFO_CRYPT_KEY = Crypto.md5().getBase64(input);

		// Close stream
		Closeables.closeQuietly(input);
	}
}
