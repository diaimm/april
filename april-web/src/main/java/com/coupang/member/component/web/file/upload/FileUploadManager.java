/*
 * @(#)FileUploadManager.java / version $Date$
 */
package com.coupang.member.component.web.file.upload;

import com.coupang.member.commons.util.file.UniqueFileNamePolicyFactory;

/**
 * 파일 업로드 관리자 인터페이스
 *
 * @version $Rev$, $Date$
 */
public interface FileUploadManager {
	/**
	 * 저장할 파일 이름의 prefix
	 *
	 * @param prefix 파일 이름의 prefix
	 */
	void setPrefix(String prefix);

	/**
	 * 개별 파일의 최대 크기
	 *
	 * @param size 개별 파일의 최대 크기
	 */
	void setSize(long size);

	/**
	 * 전체 파일의 최대 크기
	 *
	 * @param totalSize 전체 파일의 최대 크기
	 */
	void setTotalSize(long totalSize);

	/**
	 * 파일을 저장할 경로
	 *
	 * @param path 파일을 저장할 경로
	 */
	void setPath(String path);

	/**
	 * 동일이름 파일 존재시 overwrite 여부
	 *
	 * @param overwrite overwrite 여부
	 */
	void setOverwrite(boolean overwrite);

	/**
	 * {@link com.coupang.member.commons.util.file.UniqueFileNamePolicyFactory}
	 *
	 * @param renamePolicyFactory {@link UniqueFileNamePolicyFactory}
	 */
	void setUniqueFileNamePolicyFactory(UniqueFileNamePolicyFactory renamePolicyFactory);

	/**
	 * 파일을 업로드한다
	 *
	 * @param fileItem 파일
	 * @throws Exception 파일을 업로드 할 수 없는 경우
	 */
	void upload(FileItem fileItem) throws Exception;

	/**
	 * 필드 이름이 fieldName인 {@link FileItem}을 업로드한다
	 *
	 * @param fileItem  {@link FileItem}
	 * @param fieldName 필드 이름
	 * @throws Exception 파일을 업로드 할 수 없는 경우
	 */
	void upload(FileItem fileItem, String fieldName) throws Exception;

	/**
	 * 파일을 업로드한다
	 * 파일의 maxSize를 지정할수 있다.
	 *
	 * @param fileItem 파일
	 * @throws Exception 파일을 업로드 할 수 없는 경우
	 */
	void upload(FileItem fileItem, long maxSize) throws Exception;

	/**
	 * 필드 이름이 fieldName인 {@link FileItem}을 업로드한다
	 * 파일의 maxSize를 지정할수 있다.
	 *
	 * @param fileItem  {@link FileItem}
	 * @param fieldName 필드 이름
	 * @throws Exception 파일을 업로드 할 수 없는 경우
	 */
	void upload(FileItem fileItem, String fieldName, long maxSize) throws Exception;

	/**
	 * 파일 배열을 업로드한다
	 *
	 * @param fileItems 파일 배열
	 * @throws Exception 파일을 업로드 할 수 없는 경우
	 */
	void upload(FileItem[] fileItems) throws Exception;

	/**
	 * 파일 이름이 filename인 {@link FileItem}을 업로드한다
	 *
	 * @param fileItems {@link FileItem} 배열
	 * @param filename  파일 이름
	 * @throws Exception 파일을 업로드 할 수 없는 경우
	 */
	void upload(FileItem[] fileItems, String filename) throws Exception;

	/**
	 * 파일 배열을 업로드한다
	 *
	 * @param fileItems 파일 배열
	 * @throws Exception 파일을 업로드 할 수 없는 경우
	 */
	void upload(FileItem[] fileItems, long maxSize) throws Exception;

	/**
	 * 파일 이름이 filename인 {@link FileItem}을 업로드한다
	 *
	 * @param fileItems {@link FileItem} 배열
	 * @param filename  파일 이름
	 * @throws Exception 파일을 업로드 할 수 없는 경우
	 */
	void upload(FileItem[] fileItems, String filename, long maxSize) throws Exception;
}
