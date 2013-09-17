package com.diaimm.april.web.compress.version;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

/**
 * 파일 이름을 비교하여 가장 높은 버전을 가진 파일 이름을 가져오기 위하여 사용한다
 * 
 * @author 이성준
 * @version $Rev$, $Date$
 */
class FilenameComparator implements Comparator<String> {
	/**
	 * {@inheritDoc}
	 */
	public int compare(String filename1, String filename2) {
		String v1 = FileUtils.getVersion(filename1);
		String v2 = FileUtils.getVersion(filename2);

		if (StringUtils.isEmpty(v1) || !StringUtils.isNumeric(v1)) {
			return 1;
		}

		if (StringUtils.isEmpty(v2) || !StringUtils.isNumeric(v2)) {
			return -1;
		}

		return Integer.valueOf(v2).compareTo(Integer.valueOf(v1));
	}
}
