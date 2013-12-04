package com.diaimm.april.web.view;

import com.diaimm.april.web.view.selector.MobileDetectingJspViewNameSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

/**
 * Created with diaimm(봉구).
 * User: diaimm(봉구)
 * Date: 13. 8. 23
 * Time: 오전 9:56
 */
public class MobileDetectingJspViewResolver extends org.springframework.web.servlet.view.InternalResourceViewResolver {
	@Autowired
	private MobileDetectingJspViewNameSelector mobileDetectingJspViewNameSelector;
	private Boolean exposePathVariables;

	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		MobileDetectingJstlView view = new MobileDetectingJstlView(mobileDetectingJspViewNameSelector);
		view.setUrl(viewName);

		String contentType = getContentType();
		if (contentType != null) {
			view.setContentType(contentType);
		}
		view.setRequestContextAttribute(getRequestContextAttribute());
		view.setAttributesMap(getAttributesMap());
		if (this.exposePathVariables != null) {
			view.setExposePathVariables(exposePathVariables);
		}
		return view;
	}

	private Boolean hasMobileView(String viewName) {
		return MobileDetectingJspViewNameSelector.getMobileViewFileExistsCache().get(viewName);
	}

	private boolean isMobileViewSelected(String viewName) {
		return (Boolean)RequestContextHolder.getRequestAttributes().getAttribute(MobileDetectingJspViewNameSelector.IS_MOBILE_SELECT_ATTRIBUTE_KEY,
			RequestAttributes.SCOPE_REQUEST);
	}

	@Override
	protected Class getViewClass() {
		return MobileDetectingJstlView.class;
	}

	public void setExposePathVariables(Boolean exposePathVariables) {
		this.exposePathVariables = exposePathVariables;
		super.setExposePathVariables(this.exposePathVariables);
	}
}
