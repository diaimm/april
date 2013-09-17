/**
 * @fileName : Layout.java
 * @date : 2013. 3. 12.
 * @author : diaimm. Santorini.
 * @desc : 
 */
package com.diaimm.april.web.view.layout.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author diaimm
 * 
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Layout {
	/**
	 * 프리셑 클래스
	 * 
	 * @return
	 */
	Class<?> preset() default Object.class;

	/**
	 * layout을 수행해주는 method 명
	 * 
	 * @return
	 */
	String method() default DefaultValues.DEFAULT_METHOD;

	/**
	 * 레이아웃 file definition
	 * 
	 * @return
	 */
	String layout() default DefaultValues.NO_LAYOUT;

	public static class DefaultValues {
		public static final String NO_LAYOUT = "NO_LAYOUT";
		public static final String DEFAULT_METHOD = "setupLayout";
		public final static Class<?> NO_PRESET = Object.class;
	}
}
