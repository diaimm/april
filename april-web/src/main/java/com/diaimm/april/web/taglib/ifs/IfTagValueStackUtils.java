package com.diaimm.april.web.taglib.ifs;

import javax.servlet.jsp.PageContext;

/**
 * 
 * @author 이성준
 * @version $Rev$, $Date$
 */
final class IfTagValueStackUtils {
	private IfTagValueStackUtils() {
		throw new UnsupportedOperationException();
	}

	static void push(Boolean value, PageContext pageContext) {
		getValueStack(pageContext).push(value);
	}

	static Boolean pop(PageContext pageContext) {
		return getValueStack(pageContext).pop();
	}

	static Integer getDepth(PageContext pageContext) {
		return getValueStack(pageContext).getDepth();
	}

	private static IfTagValueStack getValueStack(PageContext pageContext) {
		IfTagValueStack ifTagValueStack = (IfTagValueStack) pageContext.getAttribute(IfTagValueStack.VALUE_STACK_KEY);

		if (ifTagValueStack == null) {
			ifTagValueStack = new IfTagValueStackImpl();
			pageContext.setAttribute(IfTagValueStack.VALUE_STACK_KEY, ifTagValueStack);
		}

		return ifTagValueStack;
	}
}
