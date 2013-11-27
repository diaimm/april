/*
 * @fileName : DataViewResolver.java
 * @date : 2013. 5. 24.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.web.view.dataview;

import org.springframework.web.servlet.view.UrlBasedViewResolver;

import java.util.Locale;

/**
 * @author diaimm
 * 
 */
public class DataViewResolver extends UrlBasedViewResolver implements DataViewEnvironmentAware {

	/**
	 * @see org.springframework.web.servlet.view.UrlBasedViewResolver#canHandle(java.lang.String, java.util.Locale)
	 */
	@Override
	protected boolean canHandle(String viewName, Locale locale) {
		if (viewName.startsWith(DataViewEnvironmentAware.PREFIX)) {
			return true;
		}

		return false;
	}
}
