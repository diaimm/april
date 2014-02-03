package com.diaimm.april.web.compress.version.manager;

/**
 * CSS와 JS의 압축 파일/일반 파일의 정보를 담는 Spring Bean
 * 
 * @author 이성준
 * @version $Rev$, $Date$
 */
public class CompressFileProvider {
	private final CompressFileManager manager;

	CompressFileProvider(CompressFileManager compressFileManager) {
		this.manager = compressFileManager;
	}

	/**
	 * @param prefix
	 * @return
	 */
	public String getUsingFileName(String prefix) {
		return manager.getUsingFileName(prefix);
	}
}
