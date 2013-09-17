package com.diaimm.april.web.compress.version;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 파일 처리를 위한 유틸리티 클래스
 * 
 * @author 이성준
 * @version $Rev$, $Date$
 */
public final class FileUtils {
	/**
	 * 파일의 버전 형식 : 파일명_v버전번호.확장자 중 "_v버전번호." 의 형식
	 */
	private static final String VERSION = "_v\\p{Digit}+.";
	private static final String VERSIONED_FILE_NAME = "_v\\p{Graph}+.";

	/**
	 * 생성자 (호출되지 않음)
	 */
	private FileUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 동일한 prefix인가?
	 * 
	 * @param filename
	 *            파일 이름
	 * @param prefix
	 *            prefix
	 * @return 동일 여부
	 */
	public static boolean hasSamePrefix(String filename, String prefix) {
		String pprefix = StringUtils.isEmpty(prefix) ? "" : prefix;

		try {
			Matcher matcher = getFormatFinder(pprefix + VERSION, filename);
			return matcher.find();
		} catch (NullPointerException e) {
			return false;
		}
	}

	/**
	 * 동일한 확장자인가?
	 * 
	 * @param filename
	 *            파일 이름
	 * @param extension
	 *            확장자
	 * @return 동일 여부
	 */
	public static boolean hasSameExtension(String filename, String extension) {
		try {
			return FilenameUtils.getExtension(filename).equalsIgnoreCase(extension);
		} catch (NullPointerException e) {
			return false;
		}
	}

	/**
	 * 파일 이름으로부터 prefix를 가져온다.
	 * 
	 * @param filename
	 *            파일 이름
	 * @param extension
	 *            확장자
	 * @return prefix
	 */
	public static String getPrefix(String filename, String extension) {
		if (StringUtils.isEmpty(filename) || StringUtils.isEmpty(extension)) {
			return null;
		}

		try {
			Matcher matcher = getFormatFinder(VERSIONED_FILE_NAME + extension, filename);
			return matcher.replaceAll("");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 파일 이름으로부터 version을 가져온다.
	 * 
	 * @param filename
	 *            파일 이름
	 * @return version
	 */
	public static String getVersion(String filename) {
		try {
			Matcher matcher = getFormatFinder(VERSION, filename);

			if (matcher.find()) {
				String temp = StringUtils.remove(matcher.group(), "_v");
				return StringUtils.remove(temp, ".");
			}
		} catch (Exception e) {
			// DO NOTHING
			e.getMessage();
		}

		return null;
	}

	/**
	 * 정규표현식으로부터 matcher를 가져온다.
	 * 
	 * @param regex
	 *            정규표현식
	 * @param string
	 *            문자열
	 * @return {@link Matcher}
	 */
	private static Matcher getFormatFinder(String regex, String string) {
		if (StringUtils.isEmpty(string) || StringUtils.isEmpty(regex)) {
			throw new NullPointerException();
		}

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(string);

		return matcher;
	}

	/**
	 * 파일로부터 파일 크기를 가져온다.
	 * 
	 * @param filename
	 *            파일 이름
	 * @return 파일 크기
	 */
	public static long getFilesize(String filename) {
		return new File(filename).length();
	}

	/**
	 * UNIX 파일 이름을 가져온다.
	 * 
	 * @param filename
	 *            파일 이름
	 * @return UNIX 파일 이름
	 */
	public static String getUnixFilename(String filename) {
		return StringUtils.replaceChars(filename, '\\', '/');
	}
}
