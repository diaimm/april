/*
 * @(#)FileItem.java / version $Date$
 */
package com.diaimm.april.web.file.upload;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.web.multipart.MultipartFile;

/**
 * 업로드 파일 정보
 *
 * @author 이성준
 * @version $Rev$, $Date$
 */
public class FileItem {
	private String fieldName;
	private long fileSize;
	private String contentType;
	private MultipartFile multipartFile;
	private String originalFilename;
	private String uploadedFilename;
	private String savedFilename;

	/**
	 * form 필드 명(INPUT의 name 값)을 리턴한다
	 *
	 * @return 필드 명
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * form 필드 명을 설정한다
	 *
	 * @param fieldName 필드 명
	 */
	void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * 파일 크기를 리턴한다
	 *
	 * @return 파일 크기
	 * @see org.springframework.web.multipart.MultipartFile#getSize()
	 */
	public long getFileSize() {
		return fileSize;
	}

	/**
	 * Content Type을 리턴한다
	 *
	 * @return Content Type
	 * @see org.springframework.web.multipart.MultipartFile#getContentType()
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * {@link org.springframework.web.multipart.MultipartFile} 정보를 리턴한다
	 *
	 * @return {@link org.springframework.web.multipart.MultipartFile} 정보
	 */
	public MultipartFile getMultipartFile() {
		return multipartFile;
	}

	/**
	 * {@link org.springframework.web.multipart.MultipartFile} 정보를 설정한다
	 *
	 * @param multipartFile {@link org.springframework.web.multipart.MultipartFile} 정보
	 */
	void setMultipartFile(MultipartFile multipartFile) {
		this.contentType = multipartFile.getContentType();
		this.fileSize = multipartFile.getSize();
		this.originalFilename = multipartFile.getOriginalFilename();
		this.multipartFile = multipartFile;
	}

	/**
	 * 원래의 파일 이름을 리턴한다
	 *
	 * @return 원래 파일 이름
	 */
	public String getOriginalFilename() {
		return originalFilename;
	}

	/**
	 * 업로드 된 파일 이름을 리턴한다
	 *
	 * @return 업로드 된 파일 이름
	 */
	public String getUploadedFilename() {
		return uploadedFilename;
	}

	/**
	 * 업로드 된 파일 이름을 설정한다
	 *
	 * @param uploadedFilename 업로드 된 파일 이름
	 */
	void setUploadedFilename(String uploadedFilename) {
		this.uploadedFilename = uploadedFilename;
	}

	/**
	 * 저장된 파일 이름을 리턴한다 (전체 경로 기준)
	 *
	 * @return 저장된 파일 이름
	 */
	public String getSavedFilename() {
		return savedFilename;
	}

	/**
	 * 저장된 파일 이름을 설정한다 (전체 경로 기준)
	 *
	 * @param savedFilename 저장된 파일 이름
	 */
	void setSavedFilename(String savedFilename) {
		this.savedFilename = savedFilename;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
