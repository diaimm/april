/**
 * 
 */
package com.coupang.member.commons.util.httpclient;

/**
 * @author stager0909
 * 
 */
public class HttpCallException extends IllegalStateException {
	private static final long serialVersionUID = -2610103179331258909L;

	public HttpCallException(String message) {
		super(message);
	}

	public HttpCallException(String message, Exception exception) {
		super(message, exception);
	}
}
