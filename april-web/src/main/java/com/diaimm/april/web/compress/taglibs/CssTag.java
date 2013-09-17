/*
 * @(#)CssTag.java / version $Date$
 *
 * Copyright 2011 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.diaimm.april.web.compress.taglibs;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import com.diaimm.april.commons.Env;

/**
 * CSS 태그 라이브러리
 * 
 * @author 이성준
 * @version $Rev$, $Date$
 */
public class CssTag extends AbstractCompressTag {
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void write(JspWriter writer, String fileName) throws IOException {
		writer.print("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + fileName + "\" media=\"all\"");
		writer.print(" charset=\"" + Env.DEFAULT_ENCODING + "\"");
		writer.println("/>");
	}
}
