<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="
			http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
			http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd
			http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-3.0.xsd">

	<!-- dataSource configuration -->
	<#list dataSources as dataSource>
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
		
		<bean id="${dataSource.id}${dataSource.SESSION_FACTORY.postFix}" class="org.mybatis.spring.SqlSessionFactoryBean">
			<property name="configLocation" value="classpath:${dataSource.myBatisConfigLocation}"/>
			<#if dataSource.myBatisMappingLocations??>
				<#list dataSource.myBatisMappingLocations as myBatisMappingLocation>
					<property name="mapperLocations" value="classpath*:${myBatisMappingLocation}/**/*.xml"/>
				</#list>
			</#if>
			<property name="dataSource" ref="${dataSource.id}${dataSource.DATASOURCE.postFix}"/>
		</bean>
		
		<bean id="${dataSource.id}${dataSource.SESSION_TEMPLATE.postFix}" class="com.coupang.member.db.mybatis.datasource.RoutingSqlSessionTemplate">
			<constructor-arg index="0" value="${dataSource.id}${dataSource.DATASOURCE.postFix}" />
			<constructor-arg index="1" ref="${dataSource.id}${dataSource.SESSION_FACTORY.postFix}" />
		</bean>

		<#if dataSource.mapperBases??>
			<bean id="${dataSource.id}${dataSource.MAPPER_SCANNER.postFix}" class="com.coupang.member.db.mybatis.framework.DatasourceMapperScannerConfigurer">
				<property name="datasourceId" value="${dataSource.id}"/>
				<property name="basePackage" value="${dataSource.mapperBases}" />
				<property name="sqlSessionTemplateBeanName" value="${dataSource.id}${dataSource.SESSION_TEMPLATE.postFix}" />
			</bean>
		</#if>
		
		<bean id="${dataSource.id}${dataSource.TRANSACTION_MANAGER.postFix}" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
			<property name="dataSource" ref="${dataSource.id}${dataSource.DATASOURCE.postFix}" />
			<#if dataSource.useRoutingTransactionManager?? && dataSource.useRoutingTransactionManager>
				<property name="transactionSynchronization" value="2"/>
			</#if>
		</bean>
	</#list>
</beans>
