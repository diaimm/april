package com.diaimm.april.db.util;

import org.springframework.beans.BeansException;

/**
 * Created with diaimm(봉구).
 * User: diaimm(봉구)
 * Date: 13. 8. 28
 * Time: 오전 10:35
 */
public final class BeansExceptionExtension extends BeansException {
	private static final long serialVersionUID = 5074674465697235L;

	/**
	 * @param msg
	 * @param cause
	 */
	public BeansExceptionExtension(String msg, Throwable cause) {
		super(msg, cause);
	}
}
