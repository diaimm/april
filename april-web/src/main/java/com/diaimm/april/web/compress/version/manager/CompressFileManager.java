package com.diaimm.april.web.compress.version.manager;

import com.diaimm.april.web.compress.version.FileLister;
import com.diaimm.april.web.compress.version.filetype.CssFileFilter;
import com.diaimm.april.web.compress.version.filetype.FileFilter;
import com.diaimm.april.web.compress.version.filetype.JsFileFilter;

import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * CSS와 JS의 압축 파일/일반 파일의 정보를 담는 클래스
 * 
 * @author 이성준
 * @version $Rev$, $Date$
 */
final class CompressFileManager {
	private final Map<String, String> versionControlledFiles = new HashMap<String, String>();
	private final FileFilter[] fileFilters = new FileFilter[] {new JsFileFilter(), new CssFileFilter()};
	private final Logger log = Logger.getLogger(CompressFileManager.class.getName());
	private String rootDir;
	private ServletContext servletContext;

	private CompressFileManager(String rootDir, ServletContext servletContext) {
		this.rootDir = rootDir;
		this.servletContext = servletContext;

		this.init();
	}

	static CompressFileManager getCompressFileManager(String rootDir, ServletContext servletContext) {
		return new CompressFileManager(rootDir, servletContext);
	}

	/**
	 * 초기화한다
	 */
	private void init() {
		String rootDir = getCanonicalRootPath();

		for (FileFilter fileFilter : fileFilters) {
			FileLister fileLister = new FileLister(new File(rootDir));
			Map<String, String> controlledFiles = fileLister.getFilesWithHighestVersion(fileFilter);
			versionControlledFiles.putAll(controlledFiles);
			log.info(fileFilter.getExtension() + "를 위한 정보: " + controlledFiles.toString());
		}
	}

	/**
	 * prefix에 해당하는 파일 목록을 리턴한다. (compress가 true인 경우는 압축된 파일 기준)
	 * 
	 * @param prefix
	 *            파일 이름의 prefix
	 * @return 파일 목록
	 */
	String getUsingFileName(String prefix) {
		String fileName = versionControlledFiles.get(StringUtils.trim(prefix));
		if (StringUtils.isBlank(fileName)) {
			return null;
		}

		//		fileName = fileName.replaceAll("^[" + File.separator + "]", "");

		String base = this.rootDir.replace("[/]*$", "");
		return base + fileName;
		//		return base + File.separator + fileName;
	}

	/**
	 * Document Root 값을 리턴한다.
	 * 
	 * @return Document Root
	 */
	private String getCanonicalRootPath() {
		if (StringUtils.isEmpty(rootDir)) {
			rootDir = "/";
		}

		return servletContext.getRealPath(rootDir);
	}
}
