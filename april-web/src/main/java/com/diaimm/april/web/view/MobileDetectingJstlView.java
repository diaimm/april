package com.diaimm.april.web.view;

import com.diaimm.april.web.view.selector.MobileDetectingJspViewNameSelector;
import org.springframework.web.servlet.view.JstlView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * User: diaimm(봉구)
 * Date: 13. 12. 4
 * Time: 오후 10:49
 */
public class MobileDetectingJstlView extends JstlView {
	private final MobileDetectingJspViewNameSelector mobileDetectingJspViewNameSelector;
	private String viewName = null;

	public MobileDetectingJstlView(MobileDetectingJspViewNameSelector mobileDetectingJspViewNameSelector) {
		this.mobileDetectingJspViewNameSelector = mobileDetectingJspViewNameSelector;
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.setUrl(mobileDetectingJspViewNameSelector.getViewName(request, viewName));
		super.render(model, request, response);
	}

	@Override
	public void setUrl(String url) {
		this.viewName = url;
	}
}
