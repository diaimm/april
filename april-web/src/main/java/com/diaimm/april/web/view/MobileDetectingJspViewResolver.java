package com.diaimm.april.web.view;

import com.diaimm.april.web.view.selector.MobileDetectingJspViewNameSelector;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.JstlView;

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
        AbstractUrlBasedView view = (AbstractUrlBasedView) BeanUtils.instantiateClass(getViewClass());
        view.setUrl(getPrefix(viewName) + viewName + getSuffix());
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

    private String getPrefix(String viewName) {
        if (isMobileViewSelected(viewName) && hasMobileView(viewName)) {
            return mobileDetectingJspViewNameSelector.getMobilePrefix();
        }

        return mobileDetectingJspViewNameSelector.getWebPrefix();
    }

    private Boolean hasMobileView(String viewName) {
        return MobileDetectingJspViewNameSelector.getMobileViewFileExistsCache().get(viewName);
    }

    private boolean isMobileViewSelected(String viewName) {
        return (Boolean) RequestContextHolder.getRequestAttributes().getAttribute(MobileDetectingJspViewNameSelector.IS_MOBILE_SELECT_ATTRIBUTE_KEY, RequestAttributes.SCOPE_REQUEST);
    }

    @Override
    protected Class getViewClass() {
        return JstlView.class;
    }

    public void setExposePathVariables(Boolean exposePathVariables) {
        this.exposePathVariables = exposePathVariables;
        super.setExposePathVariables(this.exposePathVariables);
    }
}
