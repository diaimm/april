/*
 * @fileName : MemberApiAccessContext.java
 * @date : 2013. 5. 24.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.diaimm.april.commons.util.httpclient;

import org.apache.http.Header;

import java.util.Map;

/**
 * member api access를 위한 context
 *
 * @author diaimm
 */
public interface HttpClientContext {
	public static final int DEFAULT_CONNECTION_TIMEOUT = 500;
	public static final int DEFAULT_SOCKET_TIMEOUT = 3000;

	/**
	 * socket time out
	 *
	 * @return
	 */
	int getSocketTimeout();

	/**
	 * connection timeout
	 *
	 * @return
	 */
	int getConnectionTimeout();

	/**
	 * api spec version
	 *
	 * @return
	 */
	String getVersion();

	/**
	 * @return
	 */
	String getPath();

	/**
	 * @return
	 */
	Map<String, Object> getParams();

	/**
	 * server port
	 *
	 * @return
	 */
	String getPort();

	/**
	 * server host name
	 *
	 * @return
	 */
	String getHostName();

	/**
	 * @return
	 */
	Header[] getHeaders();

	/**
	 * @return
	 */
	String getProtocolScheme();

	/**
	 * HttpClientFormEntityPolicy 정책을 리턴합니다.
	 *
	 * @return
	 */
	HttpClientFormEntityPolicy getHttpClientFormEntityPolicy();
}
