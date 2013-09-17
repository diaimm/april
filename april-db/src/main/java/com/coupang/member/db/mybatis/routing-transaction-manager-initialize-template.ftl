<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="
			http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
			http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd
			http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-3.0.xsd">

	<!-- routingDataSource configuration -->
	<bean id="${routing.datasourceId}" class="${routing.datasourceClass}">
		<property name="targetDataSources">
			<map key-type="java.lang.String">
				<#list dataSources as dataSource>
					<entry key="${dataSource.id}${dataSource.DATASOURCE.postFix}" value-ref="${dataSource.id}${dataSource.DATASOURCE.postFix}" />
				</#list>
			</map>
		</property>
	</bean>
	
	<!-- routingTransactionManager configuration -->
	<bean id="transactionManager" class="${routing.txClass}">
		<property name="dataSource" ref="${routing.datasourceId}" />
	</bean>
</beans>
