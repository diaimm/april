package com.diaimm.april.db.jpa.hibernate.routing;

import com.diaimm.april.db.routing.RoutingDataSourceCurrentDatasourceHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.weaving.LoadTimeWeaverAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.jdbc.datasource.lookup.SingleDataSourceLookup;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;
import org.springframework.orm.jpa.persistenceunit.SmartPersistenceUnitInfo;
import org.springframework.util.ClassUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <pre>
 * enxteds of org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
 *
 * Created with diaimm(봉구).
 * User: diaimm(봉구)
 * Date: 13. 9. 23
 * Time: 오후 1:33
 *
 * </pre>
 * @see org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
 */
public class RoutingLocalContainerEntityManagerFactoryBean extends AbstractEntityManagerFactoryBean implements ResourceLoaderAware,
	LoadTimeWeaverAware {
	private final DefaultPersistenceUnitManager internalPersistenceUnitManager = new DefaultPersistenceUnitManager();
	private PersistenceUnitManager persistenceUnitManager;
	private PersistenceUnitInfo persistenceUnitInfo;
	private Class<? extends EntityManagerFactory> entityManagerFactoryInterface;
	private EntityManagerFactoryPlusOperations plusOperations;
	private String dataSourceId;
	private BeanFactory beanFactory;

	/**
	 * Set the PersistenceUnitManager to use for obtaining the JPA persistence unit
	 * that this FactoryBean is supposed to build an EntityManagerFactory for.
	 * <p>The default is to rely on the local settings specified on this FactoryBean,
	 * such as "persistenceXmlLocation", "dataSource" and "loadTimeWeaver".
	 * <p>For reuse of existing persistence unit configuration or more advanced forms
	 * of custom persistence unit handling, consider defining a separate
	 * PersistenceUnitManager bean (typically a DefaultPersistenceUnitManager instance)
	 * and linking it in here. <code>persistence.xml</code> location, DataSource
	 * configuration and LoadTimeWeaver will be defined on that separate
	 * DefaultPersistenceUnitManager bean in such a scenario.
	 * @see #setPersistenceXmlLocation
	 * @see #setDataSource
	 * @see #setLoadTimeWeaver
	 * @see org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager
	 */
	public void setPersistenceUnitManager(PersistenceUnitManager persistenceUnitManager) {
		this.persistenceUnitManager = persistenceUnitManager;
	}

	/**
	 * Set the location of the <code>persistence.xml</code> file
	 * we want to use. This is a Spring resource location.
	 * <p>Default is "classpath:META-INF/persistence.xml".
	 * <p><b>NOTE: Only applied if no external PersistenceUnitManager specified.</b>
	 * @param persistenceXmlLocation a Spring resource String
	 * identifying the location of the <code>persistence.xml</code> file
	 * that this LocalContainerEntityManagerFactoryBean should parse
	 * @see #setPersistenceUnitManager
	 */
	public void setPersistenceXmlLocation(String persistenceXmlLocation) {
		this.internalPersistenceUnitManager.setPersistenceXmlLocation(persistenceXmlLocation);
	}

	/**
	 * Set whether to use Spring-based scanning for entity classes in the classpath
	 * instead of using JPA's standard scanning of jar files with <code>persistence.xml</code>
	 * markers in them. In case of Spring-based scanning, no <code>persistence.xml</code>
	 * is necessary; all you need to do is to specify base packages to search here.
	 * <p>Default is none. Specify packages to search for autodetection of your entity
	 * classes in the classpath. This is analogous to Spring's component-scan feature
	 * ({@link org.springframework.context.annotation.ClassPathBeanDefinitionScanner}).
	 * <p><b>NOTE: Only applied if no external PersistenceUnitManager specified.</b>
	 * @param packagesToScan one or more base packages to search, analogous to
	 * Spring's component-scan configuration for regular Spring components
	 * @see #setPersistenceUnitManager
	 */
	public void setPackagesToScan(String... packagesToScan) {
		this.internalPersistenceUnitManager.setPackagesToScan(packagesToScan);
	}

	/**
	 * Specify one or more mapping resources (equivalent to <code>&lt;mapping-file&gt;</code>
	 * entries in <code>persistence.xml</code>) for the default persistence unit.
	 * Can be used on its own or in combination with entity scanning in the classpath,
	 * in both cases avoiding <code>persistence.xml</code>.
	 * <p>Note that mapping resources must be relative to the classpath root,
	 * e.g. "META-INF/mappings.xml" or "com/mycompany/repository/mappings.xml",
	 * so that they can be loaded through <code>ClassLoader.getResource</code>.
	 * <p><b>NOTE: Only applied if no external PersistenceUnitManager specified.</b>
	 * @see #setPersistenceUnitManager
	 */
	public void setMappingResources(String... mappingResources) {
		this.internalPersistenceUnitManager.setMappingResources(mappingResources);
	}

	/**
	 * Specify the JDBC DataSource that the JPA persistence provider is supposed
	 * to use for accessing the database. This is an alternative to keeping the
	 * JDBC configuration in <code>persistence.xml</code>, passing in a Spring-managed
	 * DataSource instead.
	 * <p>In JPA speak, a DataSource passed in here will be used as "jtaDataSource"
	 * on the PersistenceUnitInfo passed to the PersistenceProvider, as well as
	 * overriding data source configuration in <code>persistence.xml</code> (if any).
	 * <p><b>NOTE: Only applied if no external PersistenceUnitManager specified.</b>
	 * @see javax.persistence.spi.PersistenceUnitInfo#getJtaDataSource()
	 * @see #setPersistenceUnitManager
	 */
	public void setJtaDataSource(DataSource jtaDataSource) {
		this.internalPersistenceUnitManager.setDataSourceLookup(new SingleDataSourceLookup(jtaDataSource));
		this.internalPersistenceUnitManager.setDefaultJtaDataSource(jtaDataSource);
	}

	/**
	 * Set the PersistenceUnitPostProcessors to be applied to the
	 * PersistenceUnitInfo used for creating this EntityManagerFactory.
	 * <p>Such post-processors can, for example, register further entity
	 * classes and jar files, in addition to the metadata read from
	 * <code>persistence.xml</code>.
	 * <p><b>NOTE: Only applied if no external PersistenceUnitManager specified.</b>
	 * @see #setPersistenceUnitManager
	 */
	public void setPersistenceUnitPostProcessors(PersistenceUnitPostProcessor... postProcessors) {
		this.internalPersistenceUnitManager.setPersistenceUnitPostProcessors(postProcessors);
	}

	/**
	 * Specify the Spring LoadTimeWeaver to use for class instrumentation according
	 * to the JPA class transformer contract.
	 * <p>It is a not required to specify a LoadTimeWeaver: Most providers will be
	 * able to provide a subset of their functionality without class instrumentation
	 * as well, or operate with their VM agent specified on JVM startup.
	 * <p>In terms of Spring-provided weaving options, the most important ones are
	 * InstrumentationLoadTimeWeaver, which requires a Spring-specific (but very general)
	 * VM agent specified on JVM startup, and ReflectiveLoadTimeWeaver, which interacts
	 * with an underlying ClassLoader based on specific extended methods being available
	 * on it (for example, interacting with Spring's TomcatInstrumentableClassLoader).
	 * <p><b>NOTE:</b> As of Spring 2.5, the context's default LoadTimeWeaver (defined
	 * as bean with name "loadTimeWeaver") will be picked up automatically, if available,
	 * removing the need for LoadTimeWeaver configuration on each affected target bean.</b>
	 * Consider using the <code>context:load-time-weaver</code> XML tag for creating
	 * such a shared LoadTimeWeaver (autodetecting the environment by default).
	 * <p><b>NOTE: Only applied if no external PersistenceUnitManager specified.</b>
	 * Otherwise, the external {@link #setPersistenceUnitManager PersistenceUnitManager}
	 * is responsible for the weaving configuration.
	 * @see org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver
	 * @see org.springframework.instrument.classloading.ReflectiveLoadTimeWeaver
	 */
	public void setLoadTimeWeaver(LoadTimeWeaver loadTimeWeaver) {
		this.internalPersistenceUnitManager.setLoadTimeWeaver(loadTimeWeaver);
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.internalPersistenceUnitManager.setResourceLoader(resourceLoader);
	}

	@Override
	protected EntityManagerFactory createNativeEntityManagerFactory() throws PersistenceException {
		PersistenceUnitManager managerToUse = this.persistenceUnitManager;
		if (this.persistenceUnitManager == null) {
			this.internalPersistenceUnitManager.afterPropertiesSet();
			managerToUse = this.internalPersistenceUnitManager;
		}

		this.persistenceUnitInfo = determinePersistenceUnitInfo(managerToUse);
		JpaVendorAdapter jpaVendorAdapter = getJpaVendorAdapter();
		if (jpaVendorAdapter != null && this.persistenceUnitInfo instanceof SmartPersistenceUnitInfo) {
			((SmartPersistenceUnitInfo)this.persistenceUnitInfo).setPersistenceProviderPackageName(jpaVendorAdapter.getPersistenceProviderRootPackage());
		}

		PersistenceProvider provider = getPersistenceProvider();
		if (provider == null) {
			String providerClassName = this.persistenceUnitInfo.getPersistenceProviderClassName();
			if (providerClassName == null) {
				throw new IllegalArgumentException("No PersistenceProvider specified in EntityManagerFactory configuration, "
					+ "and chosen PersistenceUnitInfo does not specify a provider class name either");
			}
			Class<?> providerClass = ClassUtils.resolveClassName(providerClassName, getBeanClassLoader());
			provider = (PersistenceProvider)BeanUtils.instantiateClass(providerClass);
		}
		if (provider == null) {
			throw new IllegalStateException("Unable to determine persistence provider. " + "Please check configuration of " + getClass().getName()
				+ "; " + "ideally specify the appropriate JpaVendorAdapter class for this provider.");
		}

		if (logger.isInfoEnabled()) {
			logger.info("Building JPA container EntityManagerFactory for persistence unit '" + this.persistenceUnitInfo.getPersistenceUnitName()
				+ "'");
		}
		this.nativeEntityManagerFactory = provider.createContainerEntityManagerFactory(this.persistenceUnitInfo, getJpaPropertyMap());
		postProcessEntityManagerFactory(this.nativeEntityManagerFactory, this.persistenceUnitInfo);

		return this.nativeEntityManagerFactory;
	}

	/**
	 * Determine the PersistenceUnitInfo to use for the EntityManagerFactory
	 * created by this bean.
	 * <p>The default implementation reads in all persistence unit infos from
	 * <code>persistence.xml</code>, as defined in the JPA specification.
	 * If no entity manager name was specified, it takes the first info in the
	 * array as returned by the reader. Otherwise, it checks for a matching name.
	 * @param persistenceUnitManager the PersistenceUnitManager to obtain from
	 * @return the chosen PersistenceUnitInfo
	 */
	protected PersistenceUnitInfo determinePersistenceUnitInfo(PersistenceUnitManager persistenceUnitManager) {
		if (getPersistenceUnitName() != null) {
			return persistenceUnitManager.obtainPersistenceUnitInfo(getPersistenceUnitName());
		} else {
			return persistenceUnitManager.obtainDefaultPersistenceUnitInfo();
		}
	}

	/**
	 * Hook method allowing subclasses to customize the EntityManagerFactory
	 * after its creation via the PersistenceProvider.
	 * <p>The default implementation is empty.
	 * @param emf the newly created EntityManagerFactory we are working with
	 * @param pui the PersistenceUnitInfo used to configure the EntityManagerFactory
	 * @see javax.persistence.spi.PersistenceProvider#createContainerEntityManagerFactory
	 */
	protected void postProcessEntityManagerFactory(EntityManagerFactory emf, PersistenceUnitInfo pui) {
	}

	@Override
	public PersistenceUnitInfo getPersistenceUnitInfo() {
		return this.persistenceUnitInfo;
	}

	@Override
	public String getPersistenceUnitName() {
		if (this.persistenceUnitInfo != null) {
			return this.persistenceUnitInfo.getPersistenceUnitName();
		}
		return super.getPersistenceUnitName();
	}

	/**
	 * Uses the specified persistence unit name as the name of the default
	 * persistence unit, if applicable.
	 * <p><b>NOTE: Only applied if no external PersistenceUnitManager specified.</b>
	 */
	@Override
	public void setPersistenceUnitName(String persistenceUnitName) {
		super.setPersistenceUnitName(persistenceUnitName);
		this.internalPersistenceUnitManager.setDefaultPersistenceUnitName(persistenceUnitName);
	}

	@Override
	public DataSource getDataSource() {
		if (this.persistenceUnitInfo != null) {
			return (this.persistenceUnitInfo.getJtaDataSource() != null ? this.persistenceUnitInfo.getJtaDataSource()
				: this.persistenceUnitInfo.getNonJtaDataSource());
		}
		return (this.internalPersistenceUnitManager.getDefaultJtaDataSource() != null ? this.internalPersistenceUnitManager.getDefaultJtaDataSource()
			: this.internalPersistenceUnitManager.getDefaultDataSource());
	}

	/**
	 * Specify the JDBC DataSource that the JPA persistence provider is supposed
	 * to use for accessing the database. This is an alternative to keeping the
	 * JDBC configuration in <code>persistence.xml</code>, passing in a Spring-managed
	 * DataSource instead.
	 * <p>In JPA speak, a DataSource passed in here will be used as "nonJtaDataSource"
	 * on the PersistenceUnitInfo passed to the PersistenceProvider, as well as
	 * overriding data source configuration in <code>persistence.xml</code> (if any).
	 * Note that this variant typically works for JTA transaction management as well;
	 * if it does not, consider using the explicit {@link #setJtaDataSource} instead.
	 * <p><b>NOTE: Only applied if no external PersistenceUnitManager specified.</b>
	 * @see javax.persistence.spi.PersistenceUnitInfo#getNonJtaDataSource()
	 * @see #setPersistenceUnitManager
	 */
	void setDataSource(DataSource dataSource) {
		this.internalPersistenceUnitManager.setDataSourceLookup(new SingleDataSourceLookup(dataSource));
		this.internalPersistenceUnitManager.setDefaultDataSource(dataSource);
	}

	@Override
	protected EntityManagerFactory createEntityManagerFactoryProxy(EntityManagerFactory emf) {
		this.setDataSource(this.beanFactory.getBean(dataSourceId, DataSource.class));

		ClassLoader beanClassLoader = AbstractEntityManagerFactoryBean.class.getClassLoader();
		Set<Class> ifcs = new LinkedHashSet<Class>();
		if (this.entityManagerFactoryInterface != null) {
			ifcs.add(this.entityManagerFactoryInterface);
		} else {
			ifcs.addAll(ClassUtils.getAllInterfacesForClassAsSet(emf.getClass(), beanClassLoader));
		}
		ifcs.add(EntityManagerFactoryInfo.class);
		if (getJpaDialect() != null && getJpaDialect().supportsEntityManagerFactoryPlusOperations()) {
			this.plusOperations = getJpaDialect().getEntityManagerFactoryPlusOperations(emf);
			ifcs.add(EntityManagerFactoryPlusOperations.class);
		}
		try {
			return (EntityManagerFactory)Proxy.newProxyInstance(beanClassLoader, ifcs.toArray(new Class[ifcs.size()]),
				new ManagedEntityManagerFactoryInvocationHandler(this));
		} catch (IllegalArgumentException ex) {
			if (this.entityManagerFactoryInterface != null) {
				throw new IllegalStateException("EntityManagerFactory interface [" + this.entityManagerFactoryInterface
					+ "] seems to conflict with Spring's EntityManagerFactoryInfo mixin - consider resetting the "
					+ "'entityManagerFactoryInterface' property to plain [javax.persistence.EntityManagerFactory]", ex);
			} else {
				throw new IllegalStateException("Conflicting EntityManagerFactory interfaces - "
					+ "consider specifying the 'jpaVendorAdapter' or 'entityManagerFactoryInterface' property "
					+ "to select a specific EntityManagerFactory interface to proceed with", ex);
			}
		}
	}

	/**
	 * Delegate an incoming invocation from the proxy, dispatching to EntityManagerFactoryInfo /
	 * EntityManagerFactoryPlusOperations / the native EntityManagerFactory accordingly.
	 */
	Object invokeProxyMethod(Method method, Object[] args) throws Throwable {
		if (method.getDeclaringClass().isAssignableFrom(EntityManagerFactoryInfo.class)) {
			return method.invoke(this, args);
		} else if (method.getDeclaringClass().equals(EntityManagerFactoryPlusOperations.class)) {
			return method.invoke(this.plusOperations, args);
		}
		Object retVal = method.invoke(this.nativeEntityManagerFactory, args);
		if (retVal instanceof EntityManager) {
			EntityManager rawEntityManager = (EntityManager)retVal;
			retVal = ExtendedEntityManagerCreator.createApplicationManagedEntityManager(rawEntityManager, this);
		}
		return retVal;
	}

	public void setEntityManagerFactoryInterface(Class<? extends EntityManagerFactory> entityManagerFactoryInterface) {
		this.entityManagerFactoryInterface = entityManagerFactoryInterface;
		super.setEntityManagerFactoryInterface(entityManagerFactoryInterface);
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		super.setBeanFactory(beanFactory);
		this.beanFactory = beanFactory;
	}

	/**
		 * Dynamic proxy invocation handler proxying an EntityManagerFactory to
		 * return a proxy EntityManager if necessary from createEntityManager()
		 * methods.
		 */
	private class ManagedEntityManagerFactoryInvocationHandler implements InvocationHandler, Serializable {
		private final RoutingLocalContainerEntityManagerFactoryBean entityManagerFactoryBean;

		public ManagedEntityManagerFactoryInvocationHandler(RoutingLocalContainerEntityManagerFactoryBean emfb) {
			this.entityManagerFactoryBean = emfb;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			try {
				if (method.getName().equals("equals")) {
					// Only consider equal when proxies are identical.
					return (proxy == args[0]);
				} else if (method.getName().equals("hashCode")) {
					// Use hashCode of EntityManagerFactory proxy.
					return System.identityHashCode(proxy);
				} else if (method.getName().equals("unwrap")) {
					// Handle JPA 2.1 unwrap method - could be a proxy match.
					Class targetClass = (Class)args[0];
					if (targetClass == null || targetClass.isInstance(proxy)) {
						return proxy;
					}
				}

				RoutingDataSourceCurrentDatasourceHolder.setContext(RoutingLocalContainerEntityManagerFactoryBean.this.dataSourceId);

				return this.entityManagerFactoryBean.invokeProxyMethod(method, args);
			} catch (InvocationTargetException ex) {
				throw ex.getTargetException();
			}
		}
	}
}
