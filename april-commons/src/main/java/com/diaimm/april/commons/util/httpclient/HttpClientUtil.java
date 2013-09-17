/*
 * @fileName : HttpClientUtil.java
 * @date : 2013. 7. 10.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.diaimm.april.commons.util.httpclient;

import com.diaimm.april.commons.Env;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AutoRetryHttpClient;
import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.DefaultServiceUnavailableRetryStrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author diaimm
 */
public class HttpClientUtil {
	/**
	 * http protocol을 통해 외부 api를 호출합니다.
	 *
	 * @param context http call context
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static HttpResponse call(HttpClientContext context) {
		AutoRetryHttpClient autoRetryHttpClient = new AutoRetryHttpClient(new DefaultServiceUnavailableRetryStrategy(3, 1000));
		HttpClient client = new DecompressingHttpClient(autoRetryHttpClient);
		HttpPost request = getInitializedRequest(context);

		try {
			return client.execute(request);
		} catch (Exception e) {
			throw new HttpCallException(e.getMessage(), e);
		}
	}

	/**
	 * @param context
	 * @return
	 */
	private static HttpPost getInitializedRequest(HttpClientContext context) {
		HttpPost request = new HttpPost(getRequestUrl(context));
		HttpClientFormEntityPolicy httpClientFormEntityPolicy = context.getHttpClientFormEntityPolicy();
		if (httpClientFormEntityPolicy == null) {
			httpClientFormEntityPolicy = HttpClientFormEntityPolicy.DEFAULT;
		}
		httpClientFormEntityPolicy.initRequest(request, context);
		return request;
	}

	/**
	 * HttpResponse 로 부터 body를 읽어냅니다.
	 *
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public static String readResponseBody(HttpResponse response) throws IOException {
		return readResponseBody(response, Env.DEFAULT_ENCODING, false);
	}

	/**
	 * HttpResponse로 부터 body를 읽어냅니다.
	 *
	 * @param response
	 * @param charsetName  response charset
	 * @param matchNewLine response에 포함된 newLine을 살려서, \n을 각 라인에 첨부합니다.
	 * @return
	 */
	public static String readResponseBody(HttpResponse response, String charsetName, boolean matchNewLine) {
		InputStream content = null;
		try {
			HttpEntity entity = response.getEntity();
			content = entity.getContent();
			InputStreamReader inputStreamReader = new InputStreamReader(content, charsetName);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			StringBuffer buffer = new StringBuffer();
			String readLine = null;
			while ((readLine = reader.readLine()) != null) {
				buffer.append(readLine);
				if (matchNewLine) {
					buffer.append("\n");
				}
			}
			return buffer.toString();
		} catch (Exception e) {
			throw new HttpCallException(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(content);
		}
	}

	/**
	 * TODO : api server에 대한 정보는 어디다 보관할까?
	 *
	 * @param context
	 * @return
	 */
	public static String getRequestUrl(HttpClientContext context) {
		StringBuilder fullUrl = new StringBuilder();
		fullUrl.append(context.getProtocolScheme()).append("://").append(context.getHostName());
		if (!"80".equals(context.getPort())) {
			fullUrl.append(":").append(context.getPort());
		}

		String path = context.getPath();
		if (StringUtils.isNotBlank(path)) {
			if (path.charAt(0) != '/') {
				fullUrl.append("/");
			}

			fullUrl.append(path);
		}
		return fullUrl.toString();
	}
}