package com.coupang.member.component.web.compress.version.filetype;

import java.io.File;
import java.io.FilenameFilter;

import com.coupang.member.component.web.compress.version.FileUtils;

/**
 * 파일 필터
 * 
 * @author 이성준
 * @version $Rev$, $Date$
 */
public abstract class FileFilter implements FileType, FilenameFilter {
	private final String prefix;

	/**
	 * 생성자
	 */
	public FileFilter() {
		this.prefix = null;
	}

	/**
	 * 생성자
	 * 
	 * @param prefix
	 *            prefix
	 */
	public FileFilter(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean accept(File dir, String name) {
		return FileUtils.hasSameExtension(name, getExtension()) && FileUtils.hasSamePrefix(name, prefix);
	}
}
