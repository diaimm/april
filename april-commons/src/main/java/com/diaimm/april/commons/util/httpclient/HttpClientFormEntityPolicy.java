/*
 * @fileName : HttpClientFormEntityPolicy.java
 * @date : 2013. 8. 19.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.commons.util.httpclient;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.diaimm.april.commons.util.JaxbObjectMapper;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.diaimm.april.commons.Env;
import com.google.common.collect.Lists;

/**
 * @author diaimm
 * 
 */
public enum HttpClientFormEntityPolicy {
	DEFAULT {
		@Override
		void initRequest(HttpEntityEnclosingRequest request, HttpClientContext context) {
			try {
				request.setEntity(new UrlEncodedFormEntity(makeParameters(context.getParams()), Env.DEFAULT_ENCODING));
			} catch (UnsupportedEncodingException e) {
				// not happens
			}
		}
	},

	JSON {
		@Override
		void initRequest(HttpEntityEnclosingRequest request, HttpClientContext context) {
			request.setHeader(HTTP.CONTENT_TYPE, "application/json");
			request.setEntity(new StringEntity(JaxbObjectMapper.JSON.stringify(context.getParams(), Map.class), ContentType.APPLICATION_JSON));
		}
	};

	List<NameValuePair> makeParameters(Map<String, Object> params) {
		List<NameValuePair> pairs = Lists.newArrayList();
		for (Entry<String, Object> entry : params.entrySet()) {
			if (entry.getValue() != null) {
				pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}
		}
		return pairs;
	}

	abstract void initRequest(HttpEntityEnclosingRequest httpMessage, HttpClientContext context);
}
