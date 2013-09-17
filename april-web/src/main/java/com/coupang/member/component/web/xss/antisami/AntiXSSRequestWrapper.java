/*
 * @fileName : RequestWrapper.java
 * @date : 2013. 6. 10.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.component.web.xss.antisami;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.collections.IteratorEnumeration;
import org.apache.commons.lang.StringUtils;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author diaimm
 * 
 */
public class AntiXSSRequestWrapper extends HttpServletRequestWrapper {
	private final Logger logger = LoggerFactory.getLogger(AntiXSSRequestWrapper.class);
	private final XSSCleanUtil xssCleanUtil;

	/**
	 * @param request
	 */
	public AntiXSSRequestWrapper(HttpServletRequest request, Policy policy) {
		super(request);

		this.xssCleanUtil = new XSSCleanUtil(policy);
	}

	/**
	 * @see javax.servlet.ServletRequestWrapper#getParameter(java.lang.String)
	 */
	@Override
	public String getParameter(String name) {
		return xssCleanUtil.clean(super.getParameter(name));
	}

	/**
	 * @see javax.servlet.ServletRequestWrapper#getParameterMap()
	 */
	@Override
	public Map<?, ?> getParameterMap() {
		Map<?, ?> parameterMap = super.getParameterMap();
		Map<String, Object> newParameterMap = new HashMap<String, Object>();
		for (Entry<?, ?> parameterEntry : parameterMap.entrySet()) {
			String key = (String)parameterEntry.getKey();
			Object value = parameterEntry.getValue();

			String cleanKey = xssCleanUtil.clean(key);
			if (value instanceof String) {
				newParameterMap.put(cleanKey, this.getParameter(key));
			} else if (value instanceof String[]) {
				newParameterMap.put(cleanKey, this.getParameterValues(key));
			} else {
				// 이런 케이스가 있는가 몰라..
				newParameterMap.put(cleanKey, value);
			}
		}

		return newParameterMap;
	}

	/**
	 * @see javax.servlet.ServletRequestWrapper#getParameterNames()
	 */
	@Override
	public Enumeration<?> getParameterNames() {
		return xssCleanUtil.clean(super.getParameterNames());
	}

	/**
	 * @see javax.servlet.ServletRequestWrapper#getParameterValues(java.lang.String)
	 */
	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		return xssCleanUtil.clean(values);
	}

	/**
	 * @see javax.servlet.http.HttpServletRequestWrapper#getHeader(java.lang.String)
	 */
	@Override
	public String getHeader(String name) {
		return xssCleanUtil.clean(super.getHeader(name));
	}

	/**
	 * @see javax.servlet.http.HttpServletRequestWrapper#getHeaderNames()
	 */
	@Override
	public Enumeration<?> getHeaderNames() {
		return xssCleanUtil.clean(super.getHeaderNames());
	}

	/**
	 * @see javax.servlet.http.HttpServletRequestWrapper#getHeaders(java.lang.String)
	 */
	@Override
	public Enumeration<?> getHeaders(String name) {
		return xssCleanUtil.clean(super.getHeaders(name));
	}

	private class XSSCleanUtil {
		private final AntiSamy antiSamy;
		private final Policy policy;

		private XSSCleanUtil(Policy policy) {
			this.antiSamy = new AntiSamy();
			this.policy = policy;
		}

		private String clean(String value) {
			if (StringUtils.isBlank(value)) {
				return value;
			}

			try {
				CleanResults cleanResult = this.antiSamy.scan(value, this.policy);
				return cleanResult.getCleanHTML();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return "";
			}
		}

		/**
		 * @param values
		 * @return
		 */
		private String[] clean(String[] values) {
			if (values == null) {
				return values;
			}

			String[] newValues = new String[values.length];
			for (int index = 0; index < values.length; index++) {
				newValues[index] = clean(values[index]);
			}
			return newValues;
		}

		/**
		 * @param sourceEnumeration
		 * @return
		 */
		private Enumeration<?> clean(Enumeration<?> sourceEnumeration) {
			if (sourceEnumeration == null) {
				return sourceEnumeration;
			}

			final XSSCleanUtil xssCleanUtil = this;

			List<Object> newEnumerationValueAsList = new ArrayList<Object>();
			while (sourceEnumeration.hasMoreElements()) {
				newEnumerationValueAsList.add(sourceEnumeration.nextElement());
			}
			return new IteratorEnumeration(newEnumerationValueAsList.iterator()) {
				/**
				 * @see org.apache.commons.collections.IteratorEnumeration#nextElement()
				 */
				@Override
				public Object nextElement() {
					Object nextElement = super.nextElement();
					if (nextElement instanceof String) {
						return xssCleanUtil.clean((String)nextElement);
					} else if (nextElement instanceof String[]) {
						return xssCleanUtil.clean((String[])nextElement);
					}
					return nextElement;
				}
			};
		}
	}
}
