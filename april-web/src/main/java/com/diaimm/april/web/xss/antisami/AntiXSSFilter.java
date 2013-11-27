/*
 * @fileName : AntiXSSFilter.java
 * @date : 2013. 6. 10.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.web.xss.antisami;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;

/**
 * @author diaimm
 * 
 */
public class AntiXSSFilter implements Filter {
	private static final String DEFAULT_POLICY_FILE_LOCATION = "antisamy-ebay.xml";
	private String policyConfigLocation = DEFAULT_POLICY_FILE_LOCATION;
	private Policy policy;
	private FilterConfig filterConfig;

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		if (StringUtils.isNotBlank(this.filterConfig.getInitParameter("policyConfigLocation"))) {
			this.policyConfigLocation = this.filterConfig.getInitParameter("policyConfigLocation");
		}

		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			URL policyConfig = classLoader.getResource(policyConfigLocation);
			this.policy = Policy.getInstance(policyConfig);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(new AntiXSSRequestWrapper((HttpServletRequest) request, this.policy), response);
	}

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {

	}
}
