/*
 * @(#)RenamePolicy.java / version $Date$
 */
package com.diaimm.april.commons.util.file;

import java.io.File;

/**
 * unique file 명 생성 정책
 *
 * @version $Rev$, $Date$
 */
public interface UniqueFileNamePolicy {
	/**
	 * baseFileName을 기준으로 사용가능한 다음 file명을 반환합니다.
	 *
	 * @param baseFile base file객체
	 * @return 다음으로 사용가능한 unique file name
	 */
	File getNextFile(File baseFile);
}
