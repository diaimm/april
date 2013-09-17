/**
 * @fileName : LayoutConfigure.java
 * @date : 2013. 3. 12.
 * @author : diaimm. Santorini.
 * @desc : 
 */
package com.coupang.member.component.web.view.layout.annotations;

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
public @interface LayoutConfigure {
	String layout() default Layout.DefaultValues.NO_LAYOUT;
}
