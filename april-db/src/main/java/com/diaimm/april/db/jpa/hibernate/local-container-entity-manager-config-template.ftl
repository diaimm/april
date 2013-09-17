<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:aop="http://www.springframework.org/schema/aop"
	   xsi:schemaLocation="http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-2.5.xsd
    	http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
    	http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.5.xsd
    	http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-2.5.xsd"
	   xmlns:p="http://www.springframework.org/schema/p" xmlns:tx="http://www.springframework.org/schema/tx"
	   xmlns:util="http://www.springframework.org/schema/util">

	<!-- initialzing DefaultPersistenceUnitManager... -->
	<bean class="org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor">
		<property name="defaultPersistenceUnitName" value="${dataSources[0].id}"/>
	</bean>

	<bean id="${dataSources[0].PERSISTENCE_UNIT_MANAGER.postFix}" class="org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager">
		<property name="persistenceXmlLocations">
			<list>
				<#list dataSources as dataSource>
					<value>classpath*:${dataSource.persistenceFile}</value>
				</#list>
			</list>
		</property>

		<property name="packagesToScan" value="com.coupang.wakeup.domain.model" />
		<property name="defaultDataSource" ref="${dataSources[0].id}${dataSources[0].DATASOURCE.postFix}"/>
		<property name="dataSources">
			<map>
				<#list dataSources as dataSource>
					<entry key="${dataSource.id}${dataSource.DATASOURCE.postFix}" value-ref="${dataSource.id}${dataSource.DATASOURCE.postFix}"/>
				</#list>
			</map>
		</property>
	</bean>

	<#list dataSources as dataSource>
		<!-- initializing ${dataSource.id}...-->
		<bean id="${dataSource.id}${dataSource.DATASOURCE.postFix}" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
			<property name="driverClassName" value="${dataSource.driverClassName}"/>
			<property name="url" value="${dataSource.url}"/>
			<property name="username" value="${dataSource.username}"/>
			<property name="password" value="${dataSource.password}"/>
			<#if dataSource.initialSize??><property name="initialSize" value="${dataSource.initialSize}"/></#if>
			<#if dataSource.maxActive??><property name="maxActive" value="${dataSource.maxActive}"/></#if>
			<#if dataSource.maxIdle??><property name="maxIdle" value="${dataSource.maxIdle}"/></#if>
			<#if dataSource.minIdle??><property name="minIdle" value="${dataSource.minIdle}"/></#if>
			<#if dataSource.maxWait??><property name="maxWait" value="${dataSource.maxWait}"/></#if>
			<#if dataSource.validationQuery??><property name="validationQuery" value="${dataSource.validationQuery}"/></#if>
			<#if dataSource.connectionProperties??><property name="connectionProperties" value="${dataSource.connectionProperties}"/></#if>
			<#if dataSource.queryTimeout??><property name="queryTimeout" value="${dataSource.queryTimeout}"/></#if>
			<#if dataSource.defaultAutoCommit??><property name="defaultAutoCommit" value="${dataSource.defaultAutoCommit}"/></#if>
			<#if dataSource.testOnBorrow??><property name="testOnBorrow" value="${dataSource.testOnBorrow}"/></#if>
			<#if dataSource.testOnReturn??><property name="testOnReturn" value="${dataSource.testOnReturn}"/></#if>
			<#if dataSource.testWhileIdle??><property name="testWhileIdle" value="${dataSource.testWhileIdle}"/></#if>
			<#if dataSource.timeBetweenEvictionRunsMillis??><property name="timeBetweenEvictionRunsMillis" value="${dataSource.timeBetweenEvictionRunsMillis}"/></#if>
			<#if dataSource.minEvictableIdleTimeMillis??><property name="minEvictableIdleTimeMillis" value="${dataSource.minEvictableIdleTimeMillis}"/></#if>
			<#if dataSource.numTestsPerEvictionRun??><property name="numTestsPerEvictionRun" value="${dataSource.numTestsPerEvictionRun}"/></#if>
			<#if dataSource.useStatementCache??><property name="useStatementCache" value="${dataSource.useStatementCache}"/></#if>
			<#if dataSource.statementCacheSize??><property name="statementCacheSize" value="${dataSource.statementCacheSize}"/></#if>
			<#if dataSource.useCallableStatementCache??><property name="useCallableStatementCache" value="${dataSource.useCallableStatementCache}"/></#if>
		</bean>

		<bean id="${dataSource.id}${dataSource.ENTITY_MANAGER_FACTORY.postFix}" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
			<property name="jpaVendorAdapter">
				<bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter" p:showSql="${dataSource.showSql?default('true')}" p:generateDdl="${dataSource.generateDDL?default('true')}"/>
			</property>
			<property name="persistenceUnitManager" ref="${dataSource.PERSISTENCE_UNIT_MANAGER.postFix}"/>
			<property name="persistenceUnitName" value="${dataSource.unitName}"/>
			<property name="dataSource" ref="${dataSource.id}${dataSource.DATASOURCE.postFix}"/>
		</bean>

		<bean id="${dataSource.id}${dataSource.TRANSACTION_MANAGER}" class="org.springframework.orm.jpa.JpaTransactionManager">
			<property name="entityManagerFactory" ref="${dataSource.id}${dataSource.ENTITY_MANAGER_FACTORY.postFix}" />
		</bean>
	</#list>
</beans>
