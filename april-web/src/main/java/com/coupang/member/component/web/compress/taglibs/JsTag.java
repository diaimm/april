package com.coupang.member.component.web.compress.taglibs;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import com.coupang.member.commons.Env;

/**
 * JS 태그 라이브러리
 * 
 * @author 이성준
 * @version $Rev$, $Date$
 */
public class JsTag extends AbstractCompressTag {
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void write(JspWriter writer, String fileName) throws IOException {
		writer.print("<script type=\"text/javascript\" src=\"" + fileName + "\"");
		writer.print(" charset=\"" + Env.DEFAULT_ENCODING + "\"");
		writer.println("></script>");
	}
}
