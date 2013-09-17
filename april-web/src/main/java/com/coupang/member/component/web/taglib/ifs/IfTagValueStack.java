package com.coupang.member.component.web.taglib.ifs;

import org.springframework.util.ClassUtils;

/**
 * 
 * @author 이성준
 * @version $Rev$, $Date$
 */
public interface IfTagValueStack {
	String VALUE_STACK_KEY = ClassUtils.getQualifiedName(IfTagValueStack.class);

	void push(Boolean value);

	Boolean pop();

	Integer getDepth();
}
