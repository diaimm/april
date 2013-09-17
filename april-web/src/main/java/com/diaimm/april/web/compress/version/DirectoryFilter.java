package com.diaimm.april.web.compress.version;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * 디렉토리 이름을 필터링하기 위한 {@link FilenameFilter}
 * 
 * @author 이성준
 * @version $Rev$, $Date$
 */
class DirectoryFilter implements FilenameFilter {
	/**
	 * {@inheritDoc}
	 */
	public boolean accept(File dir, String name) {
		try {
			String filename = dir.getCanonicalPath() + File.separator + name;
			return new File(filename).isDirectory();
		} catch (IOException e) {
			// DO NOTHING
			e.getMessage();
		}

		return false;
	}
}
