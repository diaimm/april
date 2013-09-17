package com.coupang.member.db.jpa.hibernate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import com.coupang.member.commons.util.EnumUtils;
import com.coupang.member.commons.util.JaxbObjectMapper;
import com.coupang.member.db.util.BeansExceptionExtension;
import com.coupang.member.db.util.DataSourceInitializerPropertyKey;
import com.coupang.member.db.util.DataSourceIntializePropertiesUtils;
import com.coupang.member.db.util.DataSourcePropertyKeys;
import com.google.common.collect.Maps;

/**
 * Created with diaimm(봉구).
 * User: diaimm(봉구)
 * Date: 13. 8. 28
 * Time: 오전 9:53
 */
public class JPAHibernateEntityManagerInitializer implements ApplicationContextAware, BeanFactoryPostProcessor {
	private static final String LOCAL_CONTAINER_ENTITY_MANAGER_CONFIG_TEMPLATE_FTL = "local-container-entity-manager-config-template.ftl";
	private Logger logger = LoggerFactory.getLogger(JPAHibernateEntityManagerInitializer.class);
	private List<String> properties;
	private ApplicationContext applicationContext;

	/**
	 * property file pathes
	 *
	 * @param properties the properties to set
	 */
	public void setProperties(List<String> properties) {
		this.properties = properties;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		try {
			List<Map<String, Object>> allDataSourcePropertyFiles = getAllDataSourcePropertyFiles();
			DataSourceIntializePropertiesUtils.initializeByTemplate(this.getClass(), allDataSourcePropertyFiles, beanFactory, LOCAL_CONTAINER_ENTITY_MANAGER_CONFIG_TEMPLATE_FTL);
		} catch (IOException e) {
			throw new BeansExceptionExtension(e.getMessage(), e);
		}
	}

	/**
	 * @return
	 * @throws IOException
	 */
	List<Map<String, Object>> getAllDataSourcePropertyFiles() throws IOException {
		List<Map<String, Object>> dataSourcePropertyFiles = new ArrayList<Map<String, Object>>();
		for (String propertiFile : properties) {
			for (Properties properties : DataSourceIntializePropertiesUtils.getPropertiesList(applicationContext, propertiFile)) {
				prepareProperties(properties);

				Map<String, Object> templateAttributes = Maps.newHashMap();
				templateAttributes.putAll(DataSourceIntializePropertiesUtils.toTemplateAttributes(properties, DataSourcePropertyKeys.values()));
				templateAttributes.putAll(DataSourceIntializePropertiesUtils.toTemplateAttributes(properties, JAPHibernatePropertyKeys.values()));
				templateAttributes.putAll(EnumUtils.map(BeanNamePostfixes.class)); // bean name postfiex 들 추가

				dataSourcePropertyFiles.add(templateAttributes);
				logger.debug("loading property : {}", properties);
			}

		}
		return dataSourcePropertyFiles;
	}

	private void prepareProperties(Properties properties) throws IOException {
		/**
		 * untiname을 생성한다.
		 */
		String persistenceFilePath = (String) properties.get(JAPHibernatePropertyKeys.UNIT_MANAGER_PERSISTENCE_FILE.propertyKey);
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(persistenceFilePath);
		String source = IOUtils.toString(inputStream, "UTF-8");
		Persistence persistence = JaxbObjectMapper.XML.objectify(source, Persistence.class);

		if (persistence != null && !CollectionUtils.isEmpty(persistence.units)) {
			properties.put(JAPHibernatePropertyKeys.ENTITY_MANAGER_UNIT_NAME.propertyKey, persistence.units.get(0).name);
		}
	}

	public static enum BeanNamePostfixes {
		PERSISTENCE_UNIT_MANAGER("persistenceUnitManager"),
		ENTITY_MANAGER_FACTORY("EntityManagerFactory"),
		DATASOURCE("DataSource"),
		TRANSACTION_MANAGER("TransactionManager");
		private final String postFix;

		BeanNamePostfixes(String postFix) {
			this.postFix = postFix;
		}

		/**
		 * @return the postFix
		 */
		public String getPostFix() {
			return postFix;
		}

		public String fullName(String datasourceId) {
			return datasourceId + getPostFix();
		}

		public String strip(String id) {
			return id.replace(this.postFix, "");
		}
	}

	static enum JAPHibernatePropertyKeys implements DataSourceInitializerPropertyKey {
		//
		UNIT_MANAGER_PERSISTENCE_FILE("unit_manager.persistenceFile", "persistenceFile", true),
		//
		ENTITY_MANAGER_UNIT_NAME("entity_manager.unitName", "unitName", true),
		//
		VENDER_ADPATER_SHOWSQL("vender_adapter.showSql", "showSql"),
		//
		VENDER_ADPATER_GENERATEDDL("vender_adapter.generateDDL", "generateDDL");
		private final String propertyKey;
		private final String attributeKey;
		private final boolean required;

		JAPHibernatePropertyKeys(String propertyKey, String attributeKey) {
			this(propertyKey, attributeKey, false);
		}

		JAPHibernatePropertyKeys(String propertyKey, String attributeKey, boolean required) {
			this.propertyKey = propertyKey;
			this.attributeKey = attributeKey;
			this.required = required;
		}

		@Override
		public void addToTemplateAttribute(Properties properties, Map<String, Object> attributes) {
			attributes.put(this.attributeKey, getValue(properties));
		}

		/**
		 * @param properties
		 * @return
		 */
		Object getValue(Properties properties) {
			String propertyValue = properties.getProperty(propertyKey);
			if (required && StringUtils.isEmpty(propertyValue)) {
				throw new IllegalArgumentException(propertyKey + " is required");
			}

			if (StringUtils.isEmpty(propertyValue)) {
				return propertyValue;
			}

			return propertyValue.replace("&", "&amp;");
		}
	}

	/**
	 * persistence.xml parsing, name 추출용
	 */
	@XmlRootElement(name = "persistence", namespace = "http://java.sun.com/xml/ns/persistence")
	@XmlAccessorType(XmlAccessType.NONE)
	static class Persistence {
		@XmlElement(name = "persistence-unit", namespace = "http://java.sun.com/xml/ns/persistence")
		private List<PersistenceUnit> units;

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
		}
	}

	@XmlAccessorType(XmlAccessType.NONE)
	static class PersistenceUnit {
		@XmlAttribute(name = "name")
		private String name;

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
		}
	}

}