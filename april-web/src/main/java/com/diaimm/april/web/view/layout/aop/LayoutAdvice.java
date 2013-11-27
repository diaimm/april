/**
 * @fileName : LayoutAdvice.java
 * @date : 2013. 3. 12.
 * @author : diaimm. Santorini.
 * @desc : 
 */
package com.diaimm.april.web.view.layout.aop;

import com.diaimm.april.web.view.layout.annotations.Layout;
import com.diaimm.april.web.view.layout.aop.LayoutHolder.LayoutInfo;
import com.diaimm.april.web.view.layout.bean.LayoutConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;

/**
 * controller의 handler method 호출 전에 사용될 advice, LayoutInfo 를 생성한다.
 * 
 * @author diaimm
 */
public class LayoutAdvice implements MethodBeforeAdvice, ApplicationContextAware {
	private final Logger logger = LoggerFactory.getLogger(LayoutAdvice.class);
	private String lastConfigInstanceID = null;
	private ApplicationContext applicationContext;

	/**
	 * @see org.springframework.aop.MethodBeforeAdvice#before(java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
	 */
	@Override
	public void before(Method method, Object[] args, Object target) throws Throwable {
		Layout layout = method.getAnnotation(Layout.class);
		LayoutConfig layoutConfig = applicationContext.getBean(LayoutConfig.class);
		if (logger.isDebugEnabled()) {
			if (layoutConfig.toString().equals(this.lastConfigInstanceID)) {
				logger.debug(" ------------------------------------------------ LayoutConfig 구현체는 반드시 prototype으로 등록되어야 합니다. ------------------------------------");
				logger.debug("initialized LayoutConfig ... : " + layoutConfig.toString());
			}
			lastConfigInstanceID = layoutConfig.toString();
		}

		LayoutHolder layoutHolder = applicationContext.getBean(LayoutHolder.class);
		layoutHolder.init(new LayoutInfo(layout.layout(), layout.method(), layout.preset(), layoutConfig));
	}

	/**
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
