package com.diaimm.april.db.util;

import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

/**
 * Created with diaimm(봉구).
 * User: diaimm(봉구)
 * Date: 13. 8. 28
 * Time: 오후 1:00
 */
public enum DataSourcePropertyKeys implements DataSourceInitializerPropertyKey {
	//
	ID("spring.qualifierId", "id", true),

	//
	DRIVER_CLASS("datasource.driverClassName", "driverClassName", true),

	//
	URL("datasource.url", "url", true),

	//
	USER("datasource.username", "username", true),

	//
	PASSWORD("datasource.password", "password", true),

	//
	INITIAL_SIZE("datasource.initialSize", "initialSize"),

	//
	MAX_ACTIVE("datasource.maxActive", "maxActive"),

	//
	MAX_IDLE("datasource.maxIdle", "maxIdle"),

	//
	MIN_IDLE("datasource.minIdle", "minIdle"),

	//
	MAX_WAIT("datasource.maxWait", "maxWait"),

	//
	VALIDATION_QUERY("datasource.validationQuery", "validationQuery"),

	//
	CONNECTION_PROPERTIES("datasource.connectionProperties", "connectionProperties"),

	//
	QUERY_TIMEOUT("datasource.queryTimeout", "queryTimeout"),

	//
	DEFAULT_AUTO_COMMIT("datasource.defaultAutoCommit", "defaultAutoCommit"),

	//
	TEST_ON_BORROW("datasource.testOnBorrow", "testOnBorrow"),

	//
	TEST_ON_RETURN("datasource.testOnReturn", "testOnReturn"),

	//
	TEST_WHILE_IDLE("datasource.testWhileIdle", "testWhileIdle"),

	//
	TIME_BETWEEN_EVICTION_RUNS_MILLIS("datasource.timeBetweenEvictionRunsMillis", "timeBetweenEvictionRunsMillis"),

	//
	MIN_EVICTABLE_IDLE_TIME_MILLIS("datasource.minEvictableIdleTimeMillis", "minEvictableIdleTimeMillis"),

	//
	NUM_TESTS_PER_EVICTION_RUN("datasource.numTestsPerEvictionRun", "numTestsPerEvictionRun"),

	//
	USE_STATEMENT_CACHE("datasource.useStatementCache", "useStatementCache"),

	//
	STATEMENT_CACHE_SIZE("datasource.statementCacheSize", "statementCacheSize"),

	//
	USE_CALLABLE_STATEMENT_CACHE("datasource.useCallableStatementCache", "useCallableStatementCache");
	private final String propertyKey;
	private final String attributeKey;
	private final boolean required;

	DataSourcePropertyKeys(String propertyKey, String attributeKey) {
		this(propertyKey, attributeKey, false);
	}

	DataSourcePropertyKeys(String propertyKey, String attributeKey, boolean required) {
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
