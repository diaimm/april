package com.coupang.member.component.web.view.templateview;

import org.springframework.web.servlet.view.UrlBasedViewResolver;

import java.util.Locale;

/**
 * Created with diaimm(봉구).
 * User: diaimm(봉구)
 * Date: 13. 9. 10
 * Time: 오후 8:37
 */
public class TemplateViewResolver extends UrlBasedViewResolver implements TemplateViewEnvironmentAware {

	/**
	 * @see org.springframework.web.servlet.view.UrlBasedViewResolver#canHandle(java.lang.String, java.util.Locale)
	 */
	@Override
	protected boolean canHandle(String viewName, Locale locale) {
		if (viewName.startsWith(PREFIX)) {
			return true;
		}

		return false;
	}

	@Override
	protected Class getViewClass() {
		return TemplateView.class;
	}
}
