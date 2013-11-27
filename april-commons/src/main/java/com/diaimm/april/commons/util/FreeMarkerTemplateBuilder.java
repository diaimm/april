/*
 * @fileName : TemplateMaker.java
 * @date : 2013. 5. 30.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.commons.util;

import com.diaimm.april.commons.Env;
import com.google.common.collect.Maps;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.Map;

/**
 * @author diaimm
 *
 */
public class FreeMarkerTemplateBuilder {
	private final String template;
	private final Map<String, Object> contextRoot = Maps.newHashMap();
	private final AttributeBuilder attributeBuilder = new AttributeBuilder();
	private String root = "";
	private Class<?> clazz = this.getClass();

	public FreeMarkerTemplateBuilder(String rootPath, String template) {
		this.root = rootPath;
		this.template = template;
	}

	public FreeMarkerTemplateBuilder(Class<?> clazz, String template) {
		this.clazz = clazz;
		this.template = template;
	}

	public AttributeBuilder attribute() {
		return attributeBuilder;
	}

	public String build() throws TemplateException, IOException {
		OutputStream outputStream = new ByteArrayOutputStream();
		Writer writer = new OutputStreamWriter(outputStream);
		Template template = FreeMarkerTemplateBuilder.this.getTemplate();
		template.setEncoding(Env.DEFAULT_ENCODING);
		template.process(FreeMarkerTemplateBuilder.this.contextRoot, writer);
		writer.flush();

		return outputStream.toString();
	}

	private Template getTemplate() throws IOException {
		Configuration configuration = new Configuration();
		configuration.setClassForTemplateLoading(clazz, root);
		if (StringUtils.isNotBlank(root)) {
			DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
			Resource path = defaultResourceLoader.getResource(root);
			File file = path.getFile(); // will fail if not resolvable in the file system
			configuration.setTemplateLoader(new FileTemplateLoader(file));
		}
		configuration.setObjectWrapper(new DefaultObjectWrapper());
		return configuration.getTemplate(template, Env.DEFAULT_ENCODING);
	}

	public class AttributeBuilder {
		public AttributeBuilder set(String key, Object value) {
			FreeMarkerTemplateBuilder.this.contextRoot.put(key, value);
			return this;
		}

		public AttributeBuilder remove(String key) {
			FreeMarkerTemplateBuilder.this.contextRoot.remove(key);
			return this;
		}

		public String build() throws TemplateException, IOException {
			return FreeMarkerTemplateBuilder.this.build();
		}
	}
}
