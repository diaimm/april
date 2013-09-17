package com.coupang.member.component.web.taglib.ifs;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author 이성준
 * @version $Rev$, $Date$
 */
public class ElseTag extends BodyTagSupport {
	private static final long serialVersionUID = -1884552473490104847L;
	private Integer depth = null;
	private final Logger log = LoggerFactory.getLogger(ElseTag.class);

	@Override
	public int doStartTag() throws JspException {
		try {
			Boolean preResult = IfTagValueStackUtils.pop(pageContext);
			this.depth = IfTagValueStackUtils.getDepth(pageContext);
			return preResult == null || preResult ? SKIP_BODY : EVAL_BODY_INCLUDE;
		} catch (Exception e) {
			log.info(e.getMessage(), e);
			return SKIP_BODY;
		}
	}

	@Override
	public int doEndTag() throws JspException {
		while (IfTagValueStackUtils.getDepth(pageContext) > depth) {
			IfTagValueStackUtils.pop(pageContext);
		}

		return SKIP_BODY;
	}
}
