package com.coupang.member.component.web.compress.version.manager;

import javax.servlet.ServletContext;
import javax.servlet.jsp.PageContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * @author 이성준
 * @version $Rev$, $Date$
 */
public class CompressFileManagerConfigurer implements ServletContextAware, InitializingBean {
	private String rootDir;
	private ServletContext servletContext;

	/**
	 * @see org.springframework.web.context.ServletContextAware#setServletContext(javax.servlet.ServletContext)
	 */
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		CompressFileManager.initCompressFileManagerInstance(rootDir, servletContext);

		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		if (applicationContext instanceof ConfigurableApplicationContext) {
			BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(CompressFileProvider.class);
			BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) ((ConfigurableApplicationContext) applicationContext)
					.getBeanFactory();
			beanDefinitionRegistry.registerBeanDefinition("compressFileProvider", beanDefinitionBuilder.getBeanDefinition());
		}
	}

	/**
	 * @param rootDir
	 *            the rootDir to set
	 */
	public void setRootDir(String rootDir) {
		this.rootDir = rootDir;
	}
}
