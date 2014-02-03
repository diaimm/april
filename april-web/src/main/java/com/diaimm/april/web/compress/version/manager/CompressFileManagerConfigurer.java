package com.diaimm.april.web.compress.version.manager;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

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
		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		if (applicationContext instanceof ConfigurableApplicationContext) {
			BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(CompressFileProvider.class);
			beanDefinitionBuilder.addConstructorArgValue(CompressFileManager.getCompressFileManager(rootDir, servletContext));
			BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry)((ConfigurableApplicationContext)applicationContext).getBeanFactory();
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
