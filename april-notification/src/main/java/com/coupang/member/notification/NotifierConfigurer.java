/*
 * @fileName : NotifierAdapter.java
 * @date : 2013. 6. 18.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.notification;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import com.coupang.member.db.mybatis.support.MapperRegistrableConfigurer;
import com.coupang.member.db.mybatis.support.MapperRegistrableConfigurer.MapperPosition;

/**
 * @author diaimm
 */
public class NotifierConfigurer extends MapperRegistrableConfigurer {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Set<String> notificationMesesageClasses = new HashSet<String>();

	{
		// 기본 message types
		notificationMesesageClasses.add("com.coupang.member.notification.model.MailMessage");
		notificationMesesageClasses.add("com.coupang.member.notification.model.SMSMessage");
	}

	/**
	 * @param notificationMesesageClasses the notificationMesesageClasses to set
	 */
	public void setNotificationMesesageClasses(List<String> notificationMesesageClasses) {
		this.notificationMesesageClasses.addAll(notificationMesesageClasses);
	}

	/**
	 * @see com.coupang.member.db.mybatis.support.MapperRegistrableConfigurer#afterMapperIntialized(org.springframework.beans.factory.config.ConfigurableListableBeanFactory, java.util.Map)
	 */
	@Override
	public void afterMapperIntialized(ConfigurableListableBeanFactory beanFactory, Map<Class<?>, MapperPosition> mapperPositions) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(DefaultNotifier.class);
		builder.addConstructorArgValue(toRepositoryMap(mapperPositions));

		// DefaultNotifier가 좀 평범해서.. 겹칠 수 있을듯.. fullname으로 등록하기
		if (beanFactory.containsBean(DefaultNotifier.class.getName()) == false) {
			BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) beanFactory;
			beanDefinitionRegistry.registerBeanDefinition(DefaultNotifier.class.getName(), builder.getBeanDefinition());

			logger.debug("Register Bean [Notifier:" + DefaultNotifier.class.getName() + "]");
		}
	}

	/**
	 * @param mapperPositions
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<Class<? extends NotificationRepository<?>>, NotificationRepository<?>> toRepositoryMap(Map<Class<?>, MapperPosition> mapperPositions) {
		Map<Class<? extends NotificationRepository<?>>, NotificationRepository<?>> ret = new HashMap<Class<? extends NotificationRepository<?>>, NotificationRepository<?>>();
		for (Entry<Class<?>, MapperPosition> mapperEntry : mapperPositions.entrySet()) {
			Class<? extends NotificationRepository<?>> key = (Class<? extends NotificationRepository<?>>) mapperEntry.getKey();
			ret.put(key, (NotificationRepository<?>) mapperEntry.getValue().getInstance());
		}
		return ret;
	}

	/**
	 * @see com.coupang.member.db.mybatis.support.MapperRegistrableConfigurer#getMapperTypes(java.lang.String)
	 */
	@Override
	public List<Class<?>> getMapperTypes(String configedDBQualifierId) {
		List<Class<?>> ret = new ArrayList<Class<?>>();

		for (String messageType : notificationMesesageClasses) {
			try {
				Class<?> messageClass = Class.forName(messageType);
				if (!NotificationMessage.class.isAssignableFrom(messageClass)) {
					throw new IllegalArgumentException("notificationMesesageClasses : NotificationMessage의 구현체만 사용할 수 있습니다.");
				}

				Constructor<?> constructor = messageClass.getDeclaredConstructor();
				constructor.setAccessible(true);
				NotificationMessage newInstance = (NotificationMessage) constructor.newInstance();

				//지정한 DB QualifierId 에 해당하는 매퍼타입을 반환한다. 
				if (StringUtils.equalsIgnoreCase(configedDBQualifierId, newInstance.getDbQualifierId())) {
					ret.add(newInstance.getRepositoryType());
				}
			} catch (Exception e) {
				// exception이 발생해도 error로 로깅하고, 로드할 수 잇는 애들만 들고 로드해보자..
				logger.error(e.getMessage(), e);
			}
		}

		return ret;
	}
}
