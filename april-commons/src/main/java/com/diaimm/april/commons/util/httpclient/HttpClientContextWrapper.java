/*
 * @fileName : HttpClientContextWrapper.java
 * @date : 2013. 7. 10.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.commons.util.httpclient;

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
	 * @see HttpClientContext#getVersion()
	 */
	@Override
	public String getVersion() {
		return implict.getVersion();
	}

	/**
	 * @see HttpClientContext#getPath()
	 */
	@Override
	public String getPath() {
		return implict.getPath();
	}

	/**
	 * @see HttpClientContext#getParams()
	 */
	@Override
	public Map<String, Object> getParams() {
		return implict.getParams();
	}

	/**
	 * @see HttpClientContext#getPort()
	 */
	@Override
	public String getPort() {
		return implict.getPort();
	}

	/**
	 * @see HttpClientContext#getHostName()
	 */
	@Override
	public String getHostName() {
		return implict.getHostName();
	}

	/**
	 * @see HttpClientContext#getSocketTimeout()
	 */
	@Override
	public int getSocketTimeout() {
		return implict.getSocketTimeout();
	}

	/**
	 * @see HttpClientContext#getConnectionTimeout()
	 */
	@Override
	public int getConnectionTimeout() {
		return implict.getConnectionTimeout();
	}

	/**
	 * @see HttpClientContext#getHeaders()
	 */
	@Override
	public Header[] getHeaders() {
		return implict.getHeaders();
	}

	/**
	 * @see HttpClientContext#getProtocolScheme()
	 */
	@Override
	public String getProtocolScheme() {
		return implict.getProtocolScheme();
	}

	/**
	 * @see HttpClientContext#getHttpClientFormEntityPolicy()
	 */
	@Override
	public HttpClientFormEntityPolicy getHttpClientFormEntityPolicy() {
		return implict.getHttpClientFormEntityPolicy();
	}
}