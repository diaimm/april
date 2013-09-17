package com.coupang.member.commons.util.httpclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;

import com.coupang.member.commons.Env;

/**
 * request 호출 방식 변경을 대비해서..
 * 
 * @author diaimm
 * 
 */
class HttpRequestUtil {
	private static final int defaultConnetionTimeout = 500;
	private static final int defaultSocketTimeout = 3000;

	public static String getRequestAndresponse(String endPoint, String encode) throws Exception {
		return getRequestAndresponse(endPoint, defaultConnetionTimeout, defaultSocketTimeout, encode, false, null, null);
	}

	public static String getRequestAndresponse(String endPoint, String encode, Map<String, String> header) throws Exception {
		return getRequestAndresponse(endPoint, defaultConnetionTimeout, defaultSocketTimeout, encode, false, null, header);
	}

	public static String getRequestAndresponse(String endPoint, int conTimeOut, int soTimeOut) throws Exception {
		return getRequestAndresponse(endPoint, conTimeOut, soTimeOut, Env.DEFAULT_ENCODING, false, null, null);
	}

	public static String getRequestAndresponse(String endPoint, int conTimeOut, int soTimeOut, String agent) throws Exception {
		return getRequestAndresponse(endPoint, conTimeOut, soTimeOut, Env.DEFAULT_ENCODING, false, agent, null);
	}

	// throws Exception
	public static String getRequestAndresponse(String endPoint, int conTimeOut, int soTimeOut, String encode, boolean isCarriageReturn, String agent,
			Map<String, String> header) throws Exception {
		StringBuffer buffer = new StringBuffer();
		BufferedReader bReader = null;
		HttpClient httpClient = new HttpClient();
		if (StringUtils.isNotEmpty(agent)) {
			httpClient.getParams().setParameter(HttpMethodParams.USER_AGENT, agent);
		}
		HttpMethod method = new GetMethod(endPoint);
		try {
			if (header != null) {
				Iterator<String> itr = header.keySet().iterator();
				while (itr.hasNext()) {
					String key = itr.next();
					if (StringUtils.isNotEmpty(header.get(key))) {
						method.addRequestHeader(new Header(key, header.get(key)));
					}
				}
			} else {
				method.addRequestHeader(new Header("Content-Type", "text/html; charset=" + encode));
			}

			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(conTimeOut);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(soTimeOut);
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(1, false));
			int statusCode = httpClient.executeMethod(method);

			if (statusCode == HttpStatus.SC_OK) {
				bReader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), encode));

				String str;
				while ((str = bReader.readLine()) != null) {
					buffer.append(str);
					if (isCarriageReturn) {
						buffer.append("\n");
					}
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (bReader != null) {
				try {
					bReader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			method.releaseConnection();
		}

		return buffer.toString();
	}
}
