/*
 * @fileName : SqlMapClientTemplateInitializer.java
 * @date : 2013. 5. 30.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.db.mybatis;

import com.coupang.member.commons.util.EnumUtils;
import com.coupang.member.commons.util.FreeMarkerTemplateBuilder.AttributeBuilder;
import com.coupang.member.db.mybatis.datasource.RoutingDataSource;
import com.coupang.member.db.mybatis.datasource.RoutingTransactionManager;
import com.coupang.member.db.mybatis.framework.DatasourceMapperScannerConfigurer;
import com.coupang.member.db.util.*;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author diaimm
 */
public class MapperScannerInitializer implements ApplicationContextAware, BeanFactoryPostProcessor {
	static final String SQLMAP_CLIENT_TEMPLATE_INITIALIZE_TEMPLATE_FTL = "sqlmap-client-template-initialize-template.ftl";
	static final String ROUTING_TRANSACTION_MANAGER_INITIALIZE_TEMPLATE_FTL = "routing-transaction-manager-initialize-template.ftl";
	private Logger logger = LoggerFactory.getLogger(MapperScannerInitializer.class);
	private DBPropertyProvider dbPropertyProvider = new DBPropertyProvider.DefaultDBPropertyProvider();
	private boolean useRoutingTransactionManager = true;
	private List<String> properties;
	private ApplicationContext applicationContext;

	/**
	 * @param useRoutingTransactionManager the useRoutingTransactionManager to set
	 */
	public void setUseRoutingTransactionManager(boolean useRoutingTransactionManager) {
		this.useRoutingTransactionManager = useRoutingTransactionManager;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(List<String> properties) {
		this.properties = properties;
	}

	/**
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * @param templateAttributes
	 */
	void initializeRoutingTransactionManager(List<Map<String, Object>> templateAttributes, ConfigurableListableBeanFactory beanFactory) {
		try {
			AttributeBuilder attributeBuilder = DataSourceIntializePropertiesUtils.getDataSourceBeanConfiguration(this.getClass(), templateAttributes, ROUTING_TRANSACTION_MANAGER_INITIALIZE_TEMPLATE_FTL);
			attributeBuilder.set("routing", getRoutingTransactionManagerAttributes());
			String configuration = attributeBuilder.build();
			logger.debug(configuration);
			DataSourceIntializePropertiesUtils.feedToBeanFactory(beanFactory, configuration);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * @return
	 */
	private Map<String, Object> getRoutingTransactionManagerAttributes() {
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("datasourceId", "routingDataSource_auto");
		ret.put("datasourceClass", RoutingDataSource.class.getName());
		ret.put("txClass", RoutingTransactionManager.class.getName());
		return ret;
	}

	/**
	 * @see org.springframework.beans.factory.config.BeanFactoryPostProcessor#postProcessBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		try {
			List<Map<String, Object>> allDataSourcePropertyFiles = getAllDataSourcePropertyFiles();
			DataSourceIntializePropertiesUtils.initializeByTemplate(this.getClass(), allDataSourcePropertyFiles, beanFactory, SQLMAP_CLIENT_TEMPLATE_INITIALIZE_TEMPLATE_FTL);
			if (useRoutingTransactionManager) {
				initializeRoutingTransactionManager(allDataSourcePropertyFiles, beanFactory);
			}

			registMapperBeans(beanFactory);
		} catch (IOException e) {
			throw new BeansExceptionExtension(e.getMessage(), e);
		}
	}

	/**
	 * @param beanFactory
	 */
	private void registMapperBeans(ConfigurableListableBeanFactory beanFactory) {
		Map<String, DatasourceMapperScannerConfigurer> mapperScannerConfigurers = beanFactory.getBeansOfType(DatasourceMapperScannerConfigurer.class);
		if (mapperScannerConfigurers != null) {
			for (Entry<String, DatasourceMapperScannerConfigurer> entry : mapperScannerConfigurers.entrySet()) {
				if (entry.getKey().endsWith("MapperScanner_auto")) {
					entry.getValue().postProcessBeanDefinitionRegistry((BeanDefinitionRegistry) beanFactory);
				}
			}
		}
	}

	/**
	 * @return
	 * @throws IOException
	 */
	List<Map<String, Object>> getAllDataSourcePropertyFiles() throws IOException {
		List<Map<String, Object>> dataSourcePropertyFiles = new ArrayList<Map<String, Object>>();
		for (String propertiFile : properties) {
			for (Properties properties : this.dbPropertyProvider.getProperties(applicationContext, propertiFile)) {
				Map<String, Object> templateAttributes = Maps.newHashMap();
				templateAttributes.putAll(DataSourceIntializePropertiesUtils.toTemplateAttributes(properties, DataSourcePropertyKeys.values()));
				templateAttributes.putAll(DataSourceIntializePropertiesUtils.toTemplateAttributes(properties, MapperScannerPropertyKeys.values()));
				templateAttributes.putAll(EnumUtils.map(BeanNamePostfixes.class)); // bean name postfiex 들 추가

				dataSourcePropertyFiles.add(templateAttributes);

				templateAttributes.put("useRoutingTransactionManager", useRoutingTransactionManager);
				logger.debug("loading property : {}", properties);
			}

		}
		return dataSourcePropertyFiles;
	}

	public DBPropertyProvider getDbPropertyProvider() {
		return dbPropertyProvider;
	}

	public void setDbPropertyProvider(DBPropertyProvider dbPropertyProvider) {
		this.dbPropertyProvider = dbPropertyProvider;
	}

	public static enum BeanNamePostfixes {
		DATASOURCE("DataSource"),
		SESSION_FACTORY("SqlSessionFactory"),
		SESSION_TEMPLATE("SqlSession_auto"),
		MAPPER_SCANNER("MapperScanner_auto"),
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

	static enum MapperScannerPropertyKeys implements DataSourceInitializerPropertyKey {
		//
		MYBATIS_CONFIG_LOCATION("mybatis.configLocation", "myBatisConfigLocation") {
			/**
			 * @see com.coupang.member.db.mybatis.MapperScannerInitializer.MapperScannerPropertyKeys#getValue(java.util.Properties)
			 */
			@Override
			Object getValue(Properties properties) {
				Object ret = super.getValue(properties);
				if (ret == null) {
					return "com/coupang/member/db/mybatis/default-mybatis-config.xml";
				}

				return ret;
			}
		},
		//
		MYBATIS_MAPPING_LOCATION("mybatis.mappingLocation", "myBatisMappingLocations") {
			/**
			 * @see com.coupang.member.db.mybatis.MapperScannerInitializer.MapperScannerPropertyKeys#getValue(java.util.Properties)
			 */
			@Override
			Object getValue(Properties properties) {
				List<String> ret = new ArrayList<String>();
				String propertiValue = (String) super.getValue(properties);
				if (StringUtils.isNotBlank(propertiValue)) {
					String[] locations = propertiValue.split(",");
					for (String location : locations) {
						if (!StringUtils.isBlank(location)) {
							ret.add(location.trim());
						}
					}
				}

				return ret;
			}
		},
		//
		MYBATIS_MAPPER_SCAN("mybatis.mapperBases", "mapperBases");
		private final String propertyKey;
		private final String attributeKey;
		private final boolean required;

		MapperScannerPropertyKeys(String propertyKey, String attributeKey) {
			this(propertyKey, attributeKey, false);
		}

		MapperScannerPropertyKeys(String propertyKey, String attributeKey, boolean required) {
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
}