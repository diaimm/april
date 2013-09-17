/*
 * @(#)LocalFileUploadManager.java / version $Date$
 */
package com.coupang.member.component.web.file.upload;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.coupang.member.commons.util.file.FileUtils;

/**
 * 파일 시스템 파일 업로드 관리자<p>
 *
 * @version $Rev$, $Date$
 */
class LocalFileUploadManager extends AbstractFileUploadManager {
	public static final long DEFAULT_MAX_FILE_SIZE = 3 * MB;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void upload(FileItem[] fileItems) throws Exception {
		if (fileItems == null) {
			return;
		}

		validateFileSize(fileItems);

		for (FileItem fileItem : fileItems) {
			write(fileItem);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void upload(FileItem[] fileItems, String fieldName) throws Exception {
		if (fileItems == null) {
			return;
		}

		validateFileSize(fileItems);

		for (FileItem fileItem : fileItems) {
			if (fileItem.getFieldName().equalsIgnoreCase(fieldName)) {
				write(fileItem);
				break;
			}
		}
	}

	/**
	 * @param fileItems
	 * @param maxSize
	 * @throws Exception
	 * @see FileUploadManager#upload(FileItem[], long)
	 */
	@Override
	public void upload(FileItem[] fileItems, long maxSize) throws Exception {
		if (fileItems == null) {
			return;
		}

		validateFileSize(fileItems, maxSize);

		for (FileItem fileItem : fileItems) {
			write(fileItem);
		}
	}

	/**
	 * @param fileItems
	 * @param fieldName
	 * @param maxSize
	 * @throws Exception
	 * @see FileUploadManager#upload(FileItem[], String, long)
	 */
	@Override
	public void upload(FileItem[] fileItems, String fieldName, long maxSize) throws Exception {
		if (fileItems == null) {
			return;
		}

		validateFileSize(fileItems, maxSize);

		for (FileItem fileItem : fileItems) {
			if (fileItem.getFieldName().equalsIgnoreCase(fieldName)) {
				write(fileItem);
				break;
			}
		}
	}

	private void write(FileItem fileItem) throws IOException {
		if (fileItem == null || fileItem.getMultipartFile() == null) {
			return;
		}

		String filenameToSave = getFilenameToSave(fileItem.getOriginalFilename());
		new File(path).mkdirs();
		File dest = new File(path, filenameToSave);

		if (overwrite) {
			fileItem.getMultipartFile().transferTo(dest);
		} else {
			dest = FileUtils.saveAnyway(fileItem.getMultipartFile().getBytes(), dest, this.uniqueFileNamePolicyFactory.create());
		}

		fileItem.setSavedFilename(dest.getPath());
		fileItem.setUploadedFilename(dest.getName());
	}

	/**
	 * 저장할 파일 이름을 가져온다
	 *
	 * @param filename 파일 이름
	 * @return 저장할 파일 이름
	 */
	private String getFilenameToSave(String filename) {
		String fileName = FilenameUtils.removeExtension(filename);
		String extension = FilenameUtils.getExtension(filename);
		String result = fileName + "." + StringUtils.lowerCase(extension);

		if (prefix != null) {
			return prefix + result;
		}

		return result;
	}
}
