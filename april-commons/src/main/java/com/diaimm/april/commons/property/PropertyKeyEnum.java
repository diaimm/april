/*
 * @fileName : PropertyKey.java
 * @date : 2013. 7. 11.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.commons.property;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author diaimm
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyKeyEnum {
	public static final String PROPERTY_FILE_PATH_DEFAULT = "_^_";

	/**
	 * <pre>
	 * property file의 경로를 입력합니다.
	 * 
	 *  이 메소드에서 리턴되는 prefix를 기준으로 다음 파일들을 찾습니다.
	 *  1. *.properties (phase에 무관한 전체 property)
	 *  2. *-{phase}.properties (지정된 {phase}용 properties) ByPhase$Phase를 참고합니다.
	 *  
	 *  PropertyKeyEnum.PROPERTY_FILE_PATH_DEFAULT 를 리턴하면 다음 경로를 prefix로 사용합니다.
	 * 
	 *  	this.getClass().getName().replace(".", "/");
	 * </pre>
	 * 
	 * @return
	 * @see com.diaimm.april.commons.ByPhase.Phase
	 */
	public String propertiesFilePrefix() default PROPERTY_FILE_PATH_DEFAULT;

	public static class FilePath {
		public static String get(Class<?> annotatedClass) {
			PropertyKeyEnum value = annotatedClass.getAnnotation(PropertyKeyEnum.class);
			if (value == null) {
				return null;
			}

			if (!PROPERTY_FILE_PATH_DEFAULT.equals(value.propertiesFilePrefix())) {
				value.propertiesFilePrefix();
			}

			return annotatedClass.getName().replace(".", File.separator);
		}
	}
}
