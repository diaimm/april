package com.diaimm.april.web.compress.taglibs;

import com.diaimm.april.commons.Env;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;

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
