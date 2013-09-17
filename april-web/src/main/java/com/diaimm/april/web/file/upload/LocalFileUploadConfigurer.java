package com.diaimm.april.web.file.upload;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.diaimm.april.commons.Env;

/**
 * Created with diaimm(봉구).
 * User: diaimm(봉구)
 * Date: 13. 8. 26
 * Time: 오후 3:17
 */
public class LocalFileUploadConfigurer implements BeanFactoryAware, ServletContextAware, InitializingBean {
	private String defaultEncoding = Env.DEFAULT_ENCODING;
	private String baseUplaodPath = System.getProperty("java.io.tmpdir");
	private boolean overwriteExists = Boolean.FALSE;
	private long maxFileSize = LocalFileUploadManager.DEFAULT_MAX_FILE_SIZE;
	private ServletContext servletContext;
	private BeanFactory beanFactory;

	public String getDefaultEncoding() {
		return defaultEncoding;
	}

	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

	public String getBaseUplaodPath() {
		return baseUplaodPath;
	}

	public void setBaseUplaodPath(String baseUplaodPath) {
		this.baseUplaodPath = baseUplaodPath;
	}

	public boolean isOverwriteExists() {
		return overwriteExists;
	}

	public void setOverwriteExists(boolean overwriteExists) {
		this.overwriteExists = overwriteExists;
	}

	public long getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	@Bean
	private FileUploadManager getFileUploadManager() {
		LocalFileUploadManager ret = new LocalFileUploadManager();
		ret.setPath(this.getBaseUplaodPath());
		ret.setOverwrite(this.isOverwriteExists());
		ret.setSize(this.getMaxFileSize());
		return ret;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) beanFactory;

		registCommonsMultipartResolverIfNeed(beanDefinitionRegistry);
		registFileItemArgumentResolver(beanDefinitionRegistry);
	}

	private void registFileItemArgumentResolver(BeanDefinitionRegistry beanDefinitionRegistry) {
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(FileItemArgumentResolver.class);
		beanDefinitionRegistry.registerBeanDefinition("fileItemArgumentResolver", beanDefinitionBuilder.getBeanDefinition());
//
		FileItemArgumentResolver fileItemArguementResolver = beanFactory.getBean(FileItemArgumentResolver.class);
		RequestMappingHandlerAdapter requestMappingHandlerAdapter = beanFactory.getBean(RequestMappingHandlerAdapter.class);

		List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>(requestMappingHandlerAdapter
				.getArgumentResolvers().getResolvers());
		argumentResolvers.add(0, fileItemArguementResolver);

		requestMappingHandlerAdapter.setArgumentResolvers(argumentResolvers);
		requestMappingHandlerAdapter.setInitBinderArgumentResolvers(argumentResolvers);
	}

	private void registCommonsMultipartResolverIfNeed(BeanDefinitionRegistry beanDefinitionRegistry) {
		try {
			beanFactory.getBean("multipartResolver", MultipartResolver.class);
		} catch (NoSuchBeanDefinitionException e) {
			BeanDefinitionBuilder commonsMultipartResolverBeanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(CommonsMultipartResolver.class);
			commonsMultipartResolverBeanDefinitionBuilder.addPropertyValue("ServletContext", this.servletContext);
			commonsMultipartResolverBeanDefinitionBuilder.addPropertyValue("maxUploadSize", this.getMaxFileSize());
			commonsMultipartResolverBeanDefinitionBuilder.addPropertyValue("uploadTempDir", this.getBaseUplaodPath());

			beanDefinitionRegistry.registerBeanDefinition("multipartResolver", commonsMultipartResolverBeanDefinitionBuilder.getBeanDefinition());
		}
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
}
