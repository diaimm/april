package com.diaimm.april.web.compress.version.filetype;

/**
 * css 파일 필터
 * 
 * @author 이성준
 * @version $Rev$, $Date$
 */
public class CssFileFilter extends FileFilter {
	/**
	 * {@inheritDoc}
	 */
	public String getDirectory() {
		return "css";
	}

	/**
	 * {@inheritDoc}
	 */
	public String getExtension() {
		return "css";
	}
}
