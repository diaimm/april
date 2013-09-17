package com.coupang.member.component.web.view.templateview;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with diaimm(봉구).
 * User: diaimm(봉구)
 * Date: 13. 9. 10
 * Time: 오후 8:55
 */
public class TemplateViewUtil implements TemplateViewEnvironmentAware {
	private TemplateViewUtil() {
		throw new UnsupportedOperationException();
	}

	public static String alertAndGo(HttpServletRequest request, String message, String url) {
		request.setAttribute("alertMessage", message);
		request.setAttribute("rtnUrl", url);

		return PREFIX + "_template_alertAndGo.ftl";
	}

	public static String jsRedirect(HttpServletRequest request, String url) {
		request.setAttribute("rtnUrl", url);

		return PREFIX + "_template_go.ftl";
	}

	public static String alertAndCloseAndOpenerRedirect(HttpServletRequest request, String message, String linkUrl){
		request.setAttribute("alertMessage", message);
		request.setAttribute("linkUrl", linkUrl);

		return PREFIX + "_template_alertAndCloseAndGo.ftl";
	}
}
