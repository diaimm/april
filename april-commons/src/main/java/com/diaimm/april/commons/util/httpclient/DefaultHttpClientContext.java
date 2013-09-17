/*
 * @fileName : DefaultHttpClientContext.java
 * @date : 2013. 7. 10.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.diaimm.april.commons.util.httpclient;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author diaimm
 */
public class DefaultHttpClientContext implements HttpClientContext {
	private HttpClientFormEntityPolicy httpClientFormEntityPolicy = HttpClientFormEntityPolicy.DEFAULT;
	private int socketTimeout = DEFAULT_SOCKET_TIMEOUT;
	private int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
	private String protocolScheme = "http";
	private String hostName;
	private String port;
	private String path;
	private String version;
	private Map<String, Object> params;
	private Map<String, Header> headers = new HashMap<String, Header>();

	public void addHeaders(Header... headers) {
		for (Header header : headers) {
			if (StringUtils.isEmpty(header.getName())) {
				this.headers.remove(header.getName());
			} else {
				this.headers.put(header.getName(), header);
			}
		}
	}

	/**
	 * @return the socketTimeout
	 */
	public int getSocketTimeout() {
		return socketTimeout;
	}

	/**
	 * @param socketTimeout the socketTimeout to set
	 */
	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	/**
	 * @return the connectionTimeout
	 */
	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	/**
	 * @param connectionTimeout the connectionTimeout to set
	 */
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @see HttpClientContext#getHeaders()
	 */
	@Override
	public Header[] getHeaders() {
		Collection<Header> headerValues = headers.values();
		Header[] ret = new Header[headerValues.size()];
		headerValues.toArray(ret);
		return ret;
	}

	/**
	 * @see HttpClientContext#getHttpClientFormEntityPolicy()
	 */
	@Override
	public HttpClientFormEntityPolicy getHttpClientFormEntityPolicy() {
		return this.httpClientFormEntityPolicy;
	}

	/**
	 * @see HttpClientContext#getProtocolScheme()
	 */
	@Override
	public String getProtocolScheme() {
		return protocolScheme;
	}

	public void setProtocolScheme(SupportedProtocolScheme protocolScheme) {
		if (protocolScheme == null) {
			return;
		}

		protocolScheme.onProtocolSet(this);
	}

	public static enum SupportedProtocolScheme {
		HTTP("http", "80"),
		HTTPS("http", "443");
		private final String defaultPort;
		private final String protocolScheme;

		SupportedProtocolScheme(String protocolScheme, String defaultPort) {
			this.protocolScheme = protocolScheme;
			this.defaultPort = defaultPort;
		}

		void onProtocolSet(DefaultHttpClientContext context) {
			context.protocolScheme = this.protocolScheme;
			context.port = this.defaultPort;
		}
	}
}