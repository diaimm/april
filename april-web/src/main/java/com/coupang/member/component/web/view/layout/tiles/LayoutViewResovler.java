/**
 * @fileName : LayoutViewResovler.java
 * @date : 2013. 3. 12.
 * @author : diaimm. Santorini.
 * @desc : 
 */
package com.coupang.member.component.web.view.layout.tiles;

import java.util.Locale;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/**
 * @author diaimm
 *
 */
public class LayoutViewResovler extends UrlBasedViewResolver implements Ordered {
	public static final String TILES_MARKER_PREFIX = "tiles:";

	protected View loadView(String viewName, Locale locale) throws Exception {
		if (!viewName.startsWith(TILES_MARKER_PREFIX)) {
			return null;
		}

		String definitionName = viewName.substring(TILES_MARKER_PREFIX.length());
		return (View)getApplicationContext().getAutowireCapableBeanFactory().initializeBean(buildView(definitionName), definitionName);
	}
}
