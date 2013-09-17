package com.coupang.member.component.web.compress.version.filetype;

/**
 * js 파일 필터
 * 
 * @author 이성준
 * @version $Rev$, $Date$
 */
public class JsFileFilter extends FileFilter {
	/**
	 * {@inheritDoc}
	 */
	public String getDirectory() {
		return "js";
	}

	/**
	 * {@inheritDoc}
	 */
	public String getExtension() {
		return "js";
	}
}
