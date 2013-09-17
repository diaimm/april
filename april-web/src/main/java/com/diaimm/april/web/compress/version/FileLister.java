package com.diaimm.april.web.compress.version;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.diaimm.april.web.compress.version.filetype.FileFilter;

/**
 * 루트 디렉토리로부터 지정된 {@link FileFilter}를 이용하여 파일의 목록을 가져온다.
 * 
 * @author 이성준
 * @version $Rev$, $Date$
 */
public class FileLister {
	private final File rootDir;

	/**
	 * 생성자
	 * 
	 * @param rootDir
	 *            루트 디렉토리
	 */
	public FileLister(File rootDir) {
		this.rootDir = rootDir;

		if (!rootDir.exists() || !rootDir.isDirectory()) {
			throw new IllegalArgumentException("rootDir(" + rootDir + ") is not exist or is not directory");
		}
	}

	/**
	 * {@link FileFilter}를 이용하여 가장 높은 버전을 가진 파일의 목록을 가져온다.
	 * 
	 * @param fileFilter
	 *            {@link FileFilter} 인스턴스
	 * @return 가장 높은 버전을 가진 파일의 목록
	 */
	public Map<String, String> getFilesWithHighestVersion(FileFilter fileFilter) {
		return getFiles(getFileRoot(fileFilter), getFileRoot(fileFilter), fileFilter);
	}

	/**
	 * {@link FileFilter}에 해당하는 디렉토리를 리턴한다.
	 * 
	 * @param fileFilter
	 *            {@link FileFilter} 인스턴스
	 * @return 디렉토리
	 */
	private File getFileRoot(FileFilter fileFilter) {
		return new File(rootDir, fileFilter.getDirectory());
	}

	/**
	 * {@link FileFilter}로부터 디렉토리 값을 가져온다.
	 * 
	 * @param fileFilter
	 *            {@link FileFilter} 인스턴스
	 * @return 디렉토리
	 */
	private String getDirectory(FileFilter fileFilter) {
		return File.separator + fileFilter.getDirectory() + File.separator;
	}

	/**
	 * 디렉토리로부터 {@link FileFilter}에 해당하는 파일의 목록을 리턴한다.
	 * 
	 * @param currentDir
	 *            현재 디렉토리
	 * @param rootDir
	 *            루트 디렉토리
	 * @param fileFilter
	 *            {@link FileFilter} 인스턴스
	 * @return 파일의 목록
	 */
	private Map<String, String> getFiles(File currentDir, File rootDir, FileFilter fileFilter) {
		Map<String, String> result = new HashMap<String, String>();
		result.putAll(current(currentDir, rootDir, fileFilter));
		result.putAll(child(currentDir, rootDir, fileFilter));
		return result;
	}

	/**
	 * 현재 디렉토리로부터 {@link FileFilter}에 해당하는 파일의 목록을 리턴한다.
	 * 
	 * @param currentDir
	 *            현재 디렉토리
	 * @param rootDir
	 *            루트 디렉토리
	 * @param fileFilter
	 *            {@link FileFilter} 인스턴스
	 * @return 파일의 목록
	 */
	private Map<String, String> current(File currentDir, File rootDir, FileFilter fileFilter) {
		Map<String, String> result = new HashMap<String, String>();
		String dirPrefix = getDirectory(fileFilter);

		if (currentDir.isDirectory()) {
			String[] currentFiles = currentDir.list(fileFilter);

			if (currentFiles == null || currentFiles.length == 0) {
				return result;
			}

			Map<String, List<String>> filesWithVersion = getFilesWithVersion(currentFiles, fileFilter.getExtension());
			Iterator<String> iterator = filesWithVersion.keySet().iterator();

			while (iterator.hasNext()) {
				String prefix = iterator.next();

				try {
					String currentPath = StringUtils.replace(currentDir.getCanonicalPath(), rootDir.getCanonicalPath(), "");
					String highestVersionedFilename = getHighestVersionedFilename(filesWithVersion, prefix, currentPath);

					if (StringUtils.isNotEmpty(highestVersionedFilename)) {
						String key = FileUtils.getUnixFilename(dirPrefix + getPrefix(prefix, currentPath));
						String value = FileUtils.getUnixFilename(dirPrefix + highestVersionedFilename);
						result.put(key + "." + fileFilter.getExtension(), value);
					}
				} catch (IOException e) {
					// DO NOTHING
					e.getMessage();
				}
			}
		}

		return result;
	}

	/**
	 * 하위 디렉토리로부터 {@link FileFilter}에 해당하는 파일의 목록을 리턴한다.
	 * 
	 * @param currentDir
	 *            현재 디렉토리
	 * @param rootDir
	 *            루트 디렉토리
	 * @param fileFilter
	 *            {@link FileFilter} 인스턴스
	 * @return 파일의 목록
	 */
	private Map<String, String> child(File currentDir, File rootDir, FileFilter fileFilter) {
		Map<String, String> result = new HashMap<String, String>();
		String[] childDirs = currentDir.list(new DirectoryFilter());

		if (childDirs != null && childDirs.length > 0) {
			for (String childDir : childDirs) {
				try {
					result.putAll(getFiles(new File(currentDir.getCanonicalPath(), childDir), rootDir, fileFilter));
				} catch (IOException e) {
					// DO NOTHING
					e.getMessage();
				}
			}
		}

		return result;
	}

	/**
	 * 파일 목록으로부터 버전 정보를 가진 파일 목록을 리턴한다.
	 * 
	 * @param filenames
	 *            파일 목록
	 * @param extension
	 *            확장자
	 * @return 버전 정보를 가진 파일 목록
	 */
	private Map<String, List<String>> getFilesWithVersion(String[] filenames, String extension) {
		Map<String, List<String>> result = new HashMap<String, List<String>>();

		for (String filename : filenames) {
			String prefix = FileUtils.getPrefix(filename, extension);

			if (StringUtils.isNotEmpty(prefix)) {
				if (!result.containsKey(prefix)) {
					result.put(prefix, new ArrayList<String>());
				}

				((List<String>) result.get(prefix)).add(filename);
			}
		}

		return result;
	}

	/**
	 * 버전을 가진 파일 목록으로부터 가장 높은 버전을 가진 파일 이름을 리턴한다.
	 * 
	 * @param filesWithVersion
	 *            버전을 가진 파일 목록
	 * @param prefix
	 *            prefix
	 * @param path
	 *            경로
	 * @return 가장 높은 버전을 가진 파일 이름
	 */
	private String getHighestVersionedFilename(Map<String, List<String>> filesWithVersion, String prefix, String path) {
		String highestVersionedFilename = getHighestVersionedFilename(filesWithVersion.get(prefix));

		if (StringUtils.isEmpty(highestVersionedFilename)) {
			return null;
		}

		String filename = (StringUtils.isNotEmpty(path)) ? path + File.separator + highestVersionedFilename : highestVersionedFilename;

		return removeFirstSeperatorCharacter(filename);
	}

	/**
	 * 파일 목록으로부터 가장 높은 버전을 가진 파일 이름을 리턴한다.
	 * 
	 * @param filenames
	 *            파일 목록
	 * @return 가장 높은 버전을 가진 파일 이름
	 */
	private String getHighestVersionedFilename(List<String> filenames) {
		if (filenames == null || filenames.isEmpty() || StringUtils.isEmpty(filenames.get(0))) {
			return null;
		}

		Collections.sort(filenames, new FilenameComparator());

		return filenames.get(0);
	}

	/**
	 * prefix를 리턴한다.
	 * 
	 * @param prefix
	 *            prefix
	 * @param path
	 *            path
	 * @return prefix
	 */
	private String getPrefix(String prefix, String path) {
		String pprefix = prefix;

		if (StringUtils.isNotEmpty(path)) {
			pprefix = path + File.separator + pprefix;
		}

		return removeFirstSeperatorCharacter(pprefix);
	}

	/**
	 * 파일 이름으로부터 첫번째 구분자(/) 를 지운다.
	 * 
	 * @param filename
	 *            파일 이름
	 * @return 구분자가 지워진 파일 이름
	 */
	private String removeFirstSeperatorCharacter(String filename) {
		String ffilename = (filename.charAt(0) == File.separatorChar) ? filename.substring(1) : filename;
		return StringUtils.trim(ffilename);
	}
}
