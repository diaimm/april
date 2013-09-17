package com.diaimm.april.web.compress.version.filetype;

/**
 * 파일 타입
 * 
 * @author 이성준
 * @version $Rev$, $Date$
 */
public interface FileType {
	/**
	 * 해당 파일이 있는 디렉토리 이름을 가져온다.
	 * 
	 * @return 디렉토리 이름
	 */
	String getDirectory();

	/**
	 * 해당 파일의 확장자를 가져온다.
	 * 
	 * @return 확장자
	 */
	String getExtension();
}
