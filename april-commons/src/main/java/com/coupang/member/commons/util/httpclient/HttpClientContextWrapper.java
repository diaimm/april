/*
 * @fileName : HttpClientContextWrapper.java
 * @date : 2013. 7. 10.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.commons.util.httpclient;

import org.apache.http.Header;

import java.util.Map;

/**
 * DefaultHttpClientContext를 확장할 때 기본 wrapper로 사용합니다.
 *
 * @author diaimm
 */
public class HttpClientContextWrapper implements HttpClientContext {
	private HttpClientContext implict;

	public HttpClientContextWrapper(HttpClientContext implict) {
		this.implict = implict;
	}

	/**
	 * @see com.coupang.member.commons.util.httpclient.HttpClientContext#getVersion()
	 */
	@Override
	public String getVersion() {
		return implict.getVersion();
	}

	/**
	 * @see com.coupang.member.commons.util.httpclient.HttpClientContext#getPath()
	 */
	@Override
	public String getPath() {
		return implict.getPath();
	}

	/**
	 * @see com.coupang.member.commons.util.httpclient.HttpClientContext#getParams()
	 */
	@Override
	public Map<String, Object> getParams() {
		return implict.getParams();
	}

	/**
	 * @see com.coupang.member.commons.util.httpclient.HttpClientContext#getPort()
	 */
	@Override
	public String getPort() {
		return implict.getPort();
	}

	/**
	 * @see com.coupang.member.commons.util.httpclient.HttpClientContext#getHostName()
	 */
	@Override
	public String getHostName() {
		return implict.getHostName();
	}

	/**
	 * @see com.coupang.member.commons.util.httpclient.HttpClientContext#getSocketTimeout()
	 */
	@Override
	public int getSocketTimeout() {
		return implict.getSocketTimeout();
	}

	/**
	 * @see com.coupang.member.commons.util.httpclient.HttpClientContext#getConnectionTimeout()
	 */
	@Override
	public int getConnectionTimeout() {
		return implict.getConnectionTimeout();
	}

	/**
	 * @see com.coupang.member.commons.util.httpclient.HttpClientContext#getHeaders()
	 */
	@Override
	public Header[] getHeaders() {
		return implict.getHeaders();
	}

	/**
	 * @see com.coupang.member.commons.util.httpclient.HttpClientContext#getProtocolScheme()
	 */
	@Override
	public String getProtocolScheme() {
		return implict.getProtocolScheme();
	}

	/**
	 * @see com.coupang.member.commons.util.httpclient.HttpClientContext#getHttpClientFormEntityPolicy()
	 */
	@Override
	public HttpClientFormEntityPolicy getHttpClientFormEntityPolicy() {
		return implict.getHttpClientFormEntityPolicy();
	}
}