/*
 * @fileName : MapperScannerConfigurer.java
 * @date : 2013. 6. 3.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.db.mybatis.framework;

import static org.springframework.util.Assert.notNull;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * datasource mapper scanner configuer
 * 
 * @author diaimm
 * 
 */
public class DatasourceMapperScannerConfigurer implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware, BeanNameAware {
	private String basePackage;
	private boolean addToConfig = true;
	private String sqlSessionFactoryBeanName;
	private String sqlSessionTemplateBeanName;
	private ApplicationContext applicationContext;
	private String beanName;
	private boolean processPropertyPlaceHolders;
	private String datasourceId;

	public String getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(String datasourceId) {
		this.datasourceId = datasourceId;
	}

	/**
	 * This property lets you set the base package for your mapper interface files.
	 * <p>
	 * You can set more than one package by using a semicolon or comma as a separator.
	 * <p>
	 * Mappers will be searched for recursively starting in the specified package(s).
	 * 
	 * @param basePackage
	 *            base package name
	 */
	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	/**
	 * Same as {@code MapperFactoryBean#setAddToConfig(boolean)}.
	 * 
	 * @param addToConfig
	 * @see MapperFactoryBean#setAddToConfig(boolean)
	 */
	public void setAddToConfig(boolean addToConfig) {
		this.addToConfig = addToConfig;
	}

	/**
	 * Specifies which {@code SqlSessionTemplate} to use in the case that there is more than one in the spring context. Usually this is only needed
	 * when you have more than one datasource.
	 * <p>
	 * Note bean names are used, not bean references. This is because the scanner loads early during the start process and it is too early to build
	 * mybatis object instances.
	 * 
	 * @since 1.1.0
	 * 
	 * @param sqlSessionTemplateName
	 *            Bean name of the {@code SqlSessionTemplate}
	 */
	public void setSqlSessionTemplateBeanName(String sqlSessionTemplateName) {
		this.sqlSessionTemplateBeanName = sqlSessionTemplateName;
	}

	/**
	 * Specifies which {@code SqlSessionFactory} to use in the case that there is more than one in the spring context. Usually this is only needed
	 * when you have more than one datasource.
	 * <p>
	 * Note bean names are used, not bean references. This is because the scanner loads early during the start process and it is too early to build
	 * mybatis object instances.
	 * 
	 * @since 1.1.0
	 * 
	 * @param sqlSessionFactoryName
	 *            Bean name of the {@code SqlSessionFactory}
	 */
	public void setSqlSessionFactoryBeanName(String sqlSessionFactoryName) {
		this.sqlSessionFactoryBeanName = sqlSessionFactoryName;
	}

	/**
	 * 
	 * @since 1.1.1
	 * 
	 * @param processPropertyPlaceHolders
	 */
	public void setProcessPropertyPlaceHolders(boolean processPropertyPlaceHolders) {
		this.processPropertyPlaceHolders = processPropertyPlaceHolders;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBeanName(String name) {
		this.beanName = name;
	}

	/**
	 * {@inheritDoc}
	 */
	public void afterPropertiesSet() throws Exception {
		notNull(this.basePackage, "Property 'basePackage' is required");
	}

	/**
	 * {@inheritDoc}
	 */
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		// left intentionally blank
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.2
	 */
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		if (this.processPropertyPlaceHolders) {
			processPropertyPlaceHolders();
		}

		ClassPathMapperScanner scanner = new RepositoryClassPathMapperScanner(registry);
		scanner.setAddToConfig(this.addToConfig);
		scanner.setAnnotationClass(Repository.class);
		scanner.setSqlSessionFactoryBeanName(this.sqlSessionFactoryBeanName);
		scanner.setSqlSessionTemplateBeanName(this.sqlSessionTemplateBeanName);
		scanner.setResourceLoader(this.applicationContext);
		scanner.setBeanNameGenerator(new DefaultBeanNameGenerator());
		scanner.registerFilters();
		scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
	}

	/*
	 * BeanDefinitionRegistries are called early in application startup, before BeanFactoryPostProcessors. This means that PropertyResourceConfigurers
	 * will not have been loaded and any property substitution of this class' properties will fail. To avoid this, find any
	 * PropertyResourceConfigurers defined in the context and run them on this class' bean definition. Then update the values.
	 */
	private void processPropertyPlaceHolders() {
		Map<String, PropertyResourceConfigurer> prcs = applicationContext.getBeansOfType(PropertyResourceConfigurer.class);

		if (!prcs.isEmpty() && applicationContext instanceof GenericApplicationContext) {
			BeanDefinition mapperScannerBean = ((GenericApplicationContext)applicationContext).getBeanFactory().getBeanDefinition(beanName);

			// PropertyResourceConfigurer does not expose any methods to explicitly perform
			// property placeholder substitution. Instead, create a BeanFactory that just
			// contains this mapper scanner and post process the factory.
			DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
			factory.registerBeanDefinition(beanName, mapperScannerBean);

			for (PropertyResourceConfigurer prc : prcs.values()) {
				prc.postProcessBeanFactory(factory);
			}

			PropertyValues values = mapperScannerBean.getPropertyValues();

			this.basePackage = updatePropertyValue("basePackage", values);
			this.sqlSessionFactoryBeanName = updatePropertyValue("sqlSessionFactoryBeanName", values);
			this.sqlSessionTemplateBeanName = updatePropertyValue("sqlSessionTemplateBeanName", values);
		}
	}

	private String updatePropertyValue(String propertyName, PropertyValues values) {
		PropertyValue property = values.getPropertyValue(propertyName);

		if (property == null) {
			return null;
		}

		Object value = property.getValue();

		if (value == null) {
			return null;
		} else if (value instanceof String) {
			return value.toString();
		} else if (value instanceof TypedStringValue) {
			return ((TypedStringValue)value).getValue();
		} else {
			return null;
		}

		// org.mybatis.spring.SqlSessionTemplate t= null;
	}

	private class RepositoryClassPathMapperScanner extends ClassPathMapperScanner {
		/**
		 * @param registry
		 */
		public RepositoryClassPathMapperScanner(BeanDefinitionRegistry registry) {
			super(registry);
		}

		/**
		 * Configures parent scanner to search for the right interfaces. It can search for all interfaces or just for those that extends a
		 * markerInterface or/and those annotated with the annotationClass
		 */
		public void registerFilters() {
			super.registerFilters();

			final Class<? extends Annotation> annotationType = Repository.class;
			addExcludeFilter(new AnnotationTypeFilter(annotationType) {
				@Override
				protected boolean matchSelf(MetadataReader metadataReader) {
					ClassMetadata classMetadata = metadataReader.getClassMetadata();
					try {
						Class<?> targetClass = Class.forName(classMetadata.getClassName());
						Repository repository = targetClass.getAnnotation(Repository.class);
						if (repository == null) {
							return true;
						}

						logger.debug(DatasourceMapperScannerConfigurer.this.datasourceId + " vs " + repository.value());
						return !super.matchSelf(metadataReader) || !DatasourceMapperScannerConfigurer.this.datasourceId.equals(repository.value());
					} catch (ClassNotFoundException e) {
					}

					return false;
				}

				@Override
				protected Boolean matchSuperClass(String superClassName) {
					return false;
				}
			});
		}
	}
}
