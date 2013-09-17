/*
 * @fileName : DataSourcePropertiesUtils.java
 * @date : 2013. 5. 30.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.db.util;

import com.coupang.member.commons.util.FreeMarkerTemplateBuilder;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.xml.sax.InputSource;

import java.io.*;
import java.util.*;

/**
 * @author diaimm
 */
public class DataSourceIntializePropertiesUtils {
	private static Logger logger = LoggerFactory.getLogger(DataSourceIntializePropertiesUtils.class);

	private DataSourceIntializePropertiesUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * properties에서 freemarker용 attribute map을 생성합니다.
	 *
	 * @param properties
	 * @param keys
	 * @return
	 */
	public static Map<String, Object> toTemplateAttributes(Properties properties, DataSourceInitializerPropertyKey[] keys) {
		Map<String, Object> templateAttributes = new HashMap<String, Object>();
		for (DataSourceInitializerPropertyKey propertyKey : keys) {
			propertyKey.addToTemplateAttribute(properties, templateAttributes);
		}

		return templateAttributes;
	}

	/**
	 * @param templateAttributes
	 */
	public static void initializeByTemplate(Class<?> templateClass, List<Map<String, Object>> templateAttributes, ConfigurableListableBeanFactory beanFactory, String templatePath) {
		try {
			FreeMarkerTemplateBuilder.AttributeBuilder attributeBuilder = DataSourceIntializePropertiesUtils.getDataSourceBeanConfiguration(templateClass, templateAttributes, templatePath);
			String configuration = attributeBuilder.build();
			logger.debug(configuration);
			feedToBeanFactory(beanFactory, configuration);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * @param beanFactory
	 * @param configuration
	 */
	public static void feedToBeanFactory(ConfigurableListableBeanFactory beanFactory, String configuration) {
		if (beanFactory == null) {
			return;
		}
		InputStream inputStream = new ByteArrayInputStream(configuration.getBytes());
		XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader((BeanDefinitionRegistry) beanFactory);
		xmlReader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);
		xmlReader.loadBeanDefinitions(new InputSource(inputStream));
	}

	/**
	 * @param templateAttributes
	 * @return
	 * @throws freemarker.template.TemplateException
	 *
	 * @throws IOException
	 */
	public static FreeMarkerTemplateBuilder.AttributeBuilder getDataSourceBeanConfiguration(Class<?> clazz, List<Map<String, Object>> templateAttributes, String templatePath) throws TemplateException, IOException {
		FreeMarkerTemplateBuilder templateMaker = new FreeMarkerTemplateBuilder(clazz, templatePath);
		return templateMaker.attribute().set("dataSources", templateAttributes);
	}

	/**
	 * 지정된 location으로부터 property 목록을 반환합니다.
	 *
	 * @param applicationContext
	 * @param location           property file의 경로 혹은 해당 file이 포함된 directory 경로
	 * @return
	 * @throws IOException
	 */
	public static List<Properties> getPropertiesList(ApplicationContext applicationContext, String location) throws IOException {
		if (!(location.indexOf(".properties") == location.length() - ".properties".length())) {
			location = location + "/**/*.properties";
		}

		Resource[] resources = applicationContext.getResources(location);
		List<Properties> ret = new ArrayList<Properties>();
		for (Resource resource : resources) {
			if (resource.isReadable()) {
				ret.add(getProperties(applicationContext, resource));
			}
		}

		return ret;
	}

	private static Properties getProperties(ApplicationContext applicationContext, Resource resource) throws IOException {
		Properties props = new Properties();
		props.load(resource.getInputStream());
		return props;
	}
}
