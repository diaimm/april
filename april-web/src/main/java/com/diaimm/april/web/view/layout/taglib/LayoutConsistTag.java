/**
 * @fileName : LayoutConfigTag.java
 * @date : 2013. 5. 21.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.web.view.layout.taglib;

import com.diaimm.april.web.view.layout.bean.LayoutConfig;
import com.diaimm.april.web.view.layout.bean.LayoutElement;
import com.diaimm.april.web.view.layout.bean.LayoutElement;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 *
 * @author diaimm
 */
public class LayoutConsistTag extends SimpleTagSupport {
	private String type;
	private String var = "element";
	private Object oldValue;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doTag() throws JspException, IOException {
		LayoutConfig layoutConfig = (LayoutConfig)getJspContext().getAttribute(LayoutConfig.LAYOUT_CONFIG, PageContext.REQUEST_SCOPE);
		if (layoutConfig == null) {
			return;
		}

		if (layoutConfig.hasElement(type)) {
//			pushOldValue();

			LayoutElement layoutElement = layoutConfig.getElement(type);
			getJspContext().setAttribute(var, layoutElement, PageContext.REQUEST_SCOPE);
			getJspBody().invoke(null);

//			popOldValue();
		}
	}

	/**
	 * var에 지정되어 있던 old value를 복원 합니다.
	 */
	private void popOldValue() {
		getJspContext().setAttribute(var, oldValue);
	}

	/**
	 * var에 지정되어 있던 old value를 backup 합니다.
	 * @return
	 */
	private void pushOldValue() {
		this.oldValue = getJspContext().getAttribute(var);
	}

	/**
	 * @param var the var to set
	 */
	public void setVar(String var) {
		this.var = var;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
