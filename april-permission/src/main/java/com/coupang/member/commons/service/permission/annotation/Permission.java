/*
 * @fileName : Permission.java
 * @date : 2013. 5. 27.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.commons.service.permission.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.coupang.member.commons.service.permission.AuthenticationType;
import com.coupang.member.commons.service.permission.ServicePermission;

/**
 * @author diaimm
 * 
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
	/**
	 * 필요한 퍼미션
	 * 
	 * @return
	 */
	ServicePermission required();

	/**
	 * 권한 없을 때, 권한 인증 방식
	 * 
	 * @return
	 */
	AuthenticationType authType() default AuthenticationType.DEFAULT;
}
