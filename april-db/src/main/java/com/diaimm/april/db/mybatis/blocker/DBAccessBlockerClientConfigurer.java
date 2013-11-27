/*
 * @fileName : DBAccessBlockerClientConfigurere.java
 * @date : 2013. 6. 5.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.db.mybatis.blocker;

import com.diaimm.april.db.mybatis.blocker.BlockQueryManager.BlockQueryManagerRequestCommand;
import com.google.common.collect.Lists;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AutoRetryHttpClient;
import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.ibatis.mapping.SqlCommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * @author diaimm
 */
public class DBAccessBlockerClientConfigurer implements InitializingBean, BeanFactoryAware {
	private final Logger logger = LoggerFactory.getLogger(DBAccessBlockerClientConfigurer.class);
	private List<String> targetHosts;
	private String protocol = "http";
	private String urlPostfix = "pang";
	private String clientName = "dbAccessBlockerClient";
	private BeanFactory beanFactory;

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		if (targetHosts == null) {
			throw new BeanCreationException("needs targetHosts");
		}
		logger.debug("protocol : {}", protocol);
		logger.debug("clientName : {}", clientName);
		logger.debug("urlPostfix : {}", urlPostfix);
		logger.debug("targetHosts : {}", targetHosts);

		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(DefaultDBAccessBlockerClient.class);
		beanDefinitionBuilder.addPropertyValue("config", this);

		BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry)beanFactory;
		beanDefinitionRegistry.registerBeanDefinition(clientName, beanDefinitionBuilder.getBeanDefinition());
	}

	/**
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	/**
	 * @param targetHosts the targetHosts to set
	 */
	public void setTargetHosts(List<String> targetHosts) {
		this.targetHosts = targetHosts;
	}

	public void setCoupangFrontHosts(List<String> coupangFrontHosts) {
		if (targetHosts == null) {
			targetHosts = Lists.newArrayList();
		}
		this.targetHosts.addAll(coupangFrontHosts);
	}

	public void setCoupangWingHosts(List<String> coupangWingHosts) {
		if (targetHosts == null) {
			targetHosts = Lists.newArrayList();
		}
		this.targetHosts.addAll(coupangWingHosts);
	}

	public void setCoupangSingHosts(List<String> coupangSingHosts) {
		if (targetHosts == null) {
			targetHosts = Lists.newArrayList();
		}
		this.targetHosts.addAll(coupangSingHosts);
	}

	public void setCoupangLoginHosts(List<String> coupangLoginHosts) {
		if (targetHosts == null) {
			targetHosts = Lists.newArrayList();
		}
		this.targetHosts.addAll(coupangLoginHosts);
	}

	public void setBabypangFrontHosts(List<String> babypangFrontHosts) {
		if (targetHosts == null) {
			targetHosts = Lists.newArrayList();
		}
		this.targetHosts.addAll(babypangFrontHosts);
	}

	public void setBabypangLoginHosts(List<String> babypangLoginHosts) {
		if (targetHosts == null) {
			targetHosts = Lists.newArrayList();
		}
		this.targetHosts.addAll(babypangLoginHosts);
	}

	public void setCoupangApiHosts(List<String> coupangApiHosts) {
		if (targetHosts == null) {
			targetHosts = Lists.newArrayList();
		}
		this.targetHosts.addAll(coupangApiHosts);
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @param urlPostfix the urlPostfix to set
	 */
	public void setUrlPostfix(String urlPostfix) {
		this.urlPostfix = urlPostfix;
	}

	/**
	 * @param clientName the clientName to set
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	/**
	 * @author diaimm
	 */
	private static class DefaultDBAccessBlockerClient implements DBAccessBlockerClient {
		private final Logger logger = LoggerFactory.getLogger(DefaultDBAccessBlockerClient.class);
		private DBAccessBlockerClientConfigurer config;

		/**
		 * @param config the config to set
		 */
		@SuppressWarnings("unused")
		public void setConfig(DBAccessBlockerClientConfigurer config) {
			this.config = config;
		}

		/**
		 * @see DBAccessBlockerClient#block(java.lang.String, org.apache.ibatis.mapping.SqlCommandType[])
		 */
		@Override
		public void block(String tableName, SqlCommandType... sqlCommandType) {
			if (sqlCommandType == null) {
				sqlCommandType = new SqlCommandType[] {SqlCommandType.SELECT, SqlCommandType.INSERT, SqlCommandType.UPDATE, SqlCommandType.DELETE};
			}
			call(DBAccessBlockingRequestHandler.BLOCK, tableName, sqlCommandType);
		}

		/**
		 * @see DBAccessBlockerClient#release(java.lang.String, org.apache.ibatis.mapping.SqlCommandType[])
		 */
		@Override
		public void release(String tableName, SqlCommandType... sqlCommandType) {
			if (sqlCommandType == null) {
				sqlCommandType = new SqlCommandType[] {SqlCommandType.SELECT, SqlCommandType.INSERT, SqlCommandType.UPDATE, SqlCommandType.DELETE};
			}
			call(DBAccessBlockingRequestHandler.RELEASE, tableName, sqlCommandType);
		}

		@Override
		public void block(Set<String> tableNames, SqlCommandType... sqlCommandType) {
			if (CollectionUtils.isEmpty(tableNames)) {
				throw new NullPointerException("tableNames cannot be null");
			}

			call(DBAccessBlockingRequestHandler.BLOCK, toTableName(tableNames), sqlCommandType);
		}

		@Override
		public void release(Set<String> tableNames, SqlCommandType... sqlCommandType) {
			if (CollectionUtils.isEmpty(tableNames)) {
				throw new NullPointerException("tableNames cannot be null");
			}

			call(DBAccessBlockingRequestHandler.RELEASE, toTableName(tableNames), sqlCommandType);
		}

		private String toTableName(Set<String> tableNames) {
			StringBuffer tableName = new StringBuffer();
			for (String tableNameTmp : tableNames) {
				if (tableName.length() > 0) {
					tableName.append(",");
				}

				tableName.append(tableNameTmp);
			}
			return tableName.toString();
		}

		/**
		 * @param tableName
		 * @param handler
		 * @param sqlCommandTypes
		 */
		private void call(DBAccessBlockingRequestHandler handler, String tableName, SqlCommandType... sqlCommandTypes) {
			logger.info("{} tables : {}", handler.name(), tableName);

			for (String host : config.targetHosts) {
				logger.info("host {} try to {}: ", host, handler.name());

				String requestUrl = getRequestUrl(handler, host).toString();

				logger.info("call url : {}", requestUrl);

				HttpClient client = new DecompressingHttpClient(new AutoRetryHttpClient());
				HttpUriRequest request = new HttpGet(requestUrl + "?v=" + BlockQueryManagerRequestCommand.toRequestParamValue(tableName, sqlCommandTypes));

				try {
					client.execute(request);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}

		/**
		 * @param host
		 */
		private StringBuffer getRequestUrl(DBAccessBlockingRequestHandler requestHandler, String host) {
			StringBuffer url = new StringBuffer();
			url.append(config.protocol).append("://").append(host).append(requestHandler.getRequestURI()).append(".").append(config.urlPostfix);

			return url;
		}
	}
}
