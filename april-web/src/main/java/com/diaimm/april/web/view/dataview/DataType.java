/*
 * @fileName : DataType.java
 * @date : 2013. 5. 24.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.web.view.dataview;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.diaimm.april.commons.util.JaxbObjectMapper;

/**
 * @author diaimm
 * 
 */
public enum DataType {
	//
	JSON("text/json", JaxbObjectMapper.JSON),
	//
	XML("text/xml", JaxbObjectMapper.XML);

	private final String contentType;
	private final JaxbObjectMapper mapper;

	DataType(String contentType, JaxbObjectMapper mapper) {
		this.contentType = contentType;
		this.mapper = mapper;
	}

	<T> DataViewContext getViewContenxt(Object data, Class<T> objectType) {
		return new DefaultDataViewContext(contentType, mapper.stringify(data, objectType));
	}

	/**
	 * @return the contentType
	 */
	String getContentType() {
		return contentType;
	}

	/**
	 * @return the mapper
	 */
	JaxbObjectMapper getMapper() {
		return mapper;
	}

	final static class DefaultDataViewContext implements DataViewContext {
		private final String contentType;
		private final String responseBody;

		DefaultDataViewContext(String contentType, String responseBody) {
			this.contentType = contentType;
			this.responseBody = responseBody;
		}

		@Override
		final public String getContentType() {
			return contentType;
		}

		@Override
		final public String getResponseBody() {
			return responseBody;
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		}
	}
}
