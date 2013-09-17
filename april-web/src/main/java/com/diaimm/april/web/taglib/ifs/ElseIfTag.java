package com.diaimm.april.web.taglib.ifs;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author 이성준
 * @version $Rev$, $Date$
 */
public class ElseIfTag extends BodyTagSupport {
	private static final long serialVersionUID = -5809075738448729676L;
	private String test;
	private Integer depth = null;
	private final Logger log = LoggerFactory.getLogger(ElseIfTag.class);

	public void setTest(String test) {
		this.test = test;
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			Boolean preResult = IfTagValueStackUtils.pop(pageContext);

			if (preResult == null || preResult) {
				// 이전값을 유지하도록...
				IfTagValueStackUtils.push(preResult, pageContext);
				this.depth = IfTagValueStackUtils.getDepth(pageContext);
				return SKIP_BODY;
			}

			Boolean result = Boolean.valueOf(test);
			IfTagValueStackUtils.push(result, pageContext);
			this.depth = IfTagValueStackUtils.getDepth(pageContext);

			return result ? EVAL_BODY_INCLUDE : SKIP_BODY;
		} catch (Exception e) {
			log.info(e.getMessage(), e);
			return SKIP_BODY;
		}
	}

	@Override
	public int doEndTag() throws JspException {
		// 태그 내에서 다시 stack에 쌓였을 수 있는 값들을 모두 flush 한다.
		while (IfTagValueStackUtils.getDepth(pageContext) > depth) {
			IfTagValueStackUtils.pop(pageContext);
		}

		return SKIP_BODY;
	}
}
