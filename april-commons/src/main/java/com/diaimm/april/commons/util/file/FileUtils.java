/*
 * @(#)FileUtils.java / version $Date$
 */
package com.diaimm.april.commons.util.file;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @version $Rev$, $Date$
 */
public final class FileUtils {
	private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);
	private static final UniqueFileNamePolicyFactory DEFAULT_UNIQUE_FILENAME_POLICY_FACTORY = new SameNameCountingUniqueFileNamePolicyFactory(-1);

	private FileUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 해당 파일을 삭제합니다.(뭐든...)
	 * 1. file인 경우(target.isFile() == true) : 해당 파일을 삭제합니다.
	 * 2. directory인 경우(== file이 아닌 경우) : 해당 디렉토리의 하위를 순차적으로 모두 삭제하고, 해당 디렉토리를 삭제합니다.
	 * <p/>
	 * 내부적으로 recursive하게 호출됩니다.
	 *
	 * @param target
	 * @return 삭제된 경우 true 그외(파일이 없음) : false
	 */
	public static boolean deleteAnyway(File target) {
		if (!target.exists()) {
			return false;
		}

		if (target.isFile()) {
			target.delete();
			return true;
		}

		// directory 인 경우 하위 파일을 모두 삭제하고, 자신을 삭제합니다.
		File[] files = target.listFiles();
		for (File file : files) {
			deleteAnyway(file);
		}

		return target.delete();
	}

	/**
	 * 지정된 디렉토리로부터 하위의 모든 파일들을 cascade하게 가져온다.
	 *
	 * @param directory
	 * @return
	 * @throws IOException
	 */
	public static List<String> getRecursiveFiles(String directory) throws IOException {
		return getRecursiveFiles(directory, null);
	}

	/**
	 * 지정된 디렉토리로 부터 하위의 모든 파일들을 cascade 하게 가져온다. 단, filter를 통과하는 것들만 가져온다.
	 *
	 * @param directory
	 * @param filenameFilter
	 * @return
	 * @throws IOException
	 */
	public static List<String> getRecursiveFiles(String directory, FilenameFilter filenameFilter) throws IOException {
		File fDir = new File(directory);
		List<String> result = new ArrayList<String>();

		// 현재 디렉토리에 존재하는 파일들의 리스트를 세팅한다.
		if (fDir.isDirectory()) {
			String[] files = (filenameFilter == null) ? fDir.list() : fDir.list(filenameFilter);

			if (!ArrayUtils.isEmpty(files)) {
				for (String file : files) {
					File fFile = new File(file);

					try {
						String filepath = fFile.getCanonicalPath();

						if (fFile.isDirectory()) {
							// 각 파일에 대해서 directory인 경우 해당 directory의 자식들로 recursive
							List<String> childrenFiles = getRecursiveFiles(filepath, filenameFilter);

							LOG.info("from " + filepath);

							for (String curChildFile : childrenFiles) {
								LOG.info(curChildFile);
							}

							result.addAll(childrenFiles);
						} else {
							// 그렇지 않은 경우 filter에 의한 파일 리스트 가져오기.
							result.add(filepath);
						}
					} catch (IOException e) {
						LOG.info("해당 디렉토리({})를 찾을 수 없습니다", file);
						throw new IOException("해당 디렉토리를 찾을 수 없습니다." + file, e);
					}
				}
			}
		} else {
			try {
				result.add(fDir.getCanonicalPath());
			} catch (IOException e) {
				LOG.info("해당 디렉토리({})를 찾을 수 없습니다.", fDir);
			}
		}

		return result;
	}

	/**
	 * unique한 파일명으로 저장합니다.
	 *
	 * @param bytes
	 * @param outputFile
	 * @return
	 * @throws IOException
	 */
	public static File saveAnyway(byte[] bytes, File outputFile) throws IOException {
		return saveAnyway(bytes, outputFile, null);
	}

	public static File saveAnyway(byte[] bytes, File outputFile, UniqueFileNamePolicy uniqueFileNamePolicy) throws IOException {
		if (uniqueFileNamePolicy == null) {
			uniqueFileNamePolicy = DEFAULT_UNIQUE_FILENAME_POLICY_FACTORY.create();
		}

		File uniqueFile = uniqueFileNamePolicy.getNextFile(outputFile);
		save(bytes, uniqueFile);
		return uniqueFile;
	}

	static int save(byte[] bytes, File outputFile) throws IOException {
		InputStream in = new ByteArrayInputStream(bytes);

		outputFile.getParentFile().mkdirs();
		OutputStream out = new FileOutputStream(outputFile);

		try {
			return IOUtils.copy(in, out);
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}

	static int save(File inputFile, File outputFile) throws IOException {
		InputStream in = null;
		OutputStream out = null;

		try {
			in = new FileInputStream(inputFile);
			outputFile.getParentFile().mkdirs();
			out = new FileOutputStream(outputFile);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new IOException(e);
		}

		try {
			return IOUtils.copy(in, out);
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}
}
