/*
 * @fileName : Permission.java
 * @date : 2013. 5. 27.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.permission.permission.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author diaimm
 * 
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission{
	/**
	 * 필요한 퍼미션
	 * 
	 * @return
	 */
	String[] required();

	/**
	 * 권한 없을 때, 권한 인증 방식
	 * 
	 * @return
	 */
	String authType();
}
