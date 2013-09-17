/*
 * @(#)AbstractFileUploadManager.java / version $Date$
 */
package com.coupang.member.component.web.file.upload;


import javax.naming.SizeLimitExceededException;

import com.coupang.member.commons.util.file.SameNameCountingUniqueFileNamePolicyFactory;
import com.coupang.member.commons.util.file.UniqueFileNamePolicyFactory;

/**
 * 파일 업로드를 위한 관리자
 *
 * @version $Rev$, $Date$
 */
public abstract class AbstractFileUploadManager implements FileUploadManager {
	protected static final long MB = 1024L * 1024L;
	protected String prefix;
	protected long size = 3 * MB;
	protected long totalSize = 10 * MB;
	protected String path;
	protected boolean overwrite = false;
	protected UniqueFileNamePolicyFactory uniqueFileNamePolicyFactory = new SameNameCountingUniqueFileNamePolicyFactory();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSize(long size) {
		this.size = size;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUniqueFileNamePolicyFactory(UniqueFileNamePolicyFactory uniqueFileNamePolicyFactory) {
		this.uniqueFileNamePolicyFactory = uniqueFileNamePolicyFactory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void upload(FileItem fileItem) throws Exception {
		upload(new FileItem[]{fileItem});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void upload(FileItem fileItem, String filename) throws Exception {
		upload(new FileItem[]{fileItem}, filename);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void upload(FileItem fileItem, long maxSize) throws Exception {
		upload(new FileItem[]{fileItem}, maxSize);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void upload(FileItem fileItem, String filename, long maxSize) throws Exception {
		upload(new FileItem[]{fileItem}, filename, maxSize);
	}

	/**
	 * 파일 크기가 설정된 값을 초과하는지 확인한다
	 *
	 * @param fileItems {@link FileItem} 배열
	 * @throws SizeLimitExceededException 파일 크기가 초과된 경우
	 */
	protected void validateFileSize(FileItem[] fileItems) throws SizeLimitExceededException {
		validateFileSize(fileItems, this.size);
	}

	/**
	 * 파일 크기가 설정된 값을 초과하는지 확인한다
	 * 파일의 maxSize를 지정할수 있다.
	 *
	 * @param fileItems {@link FileItem} 배열
	 * @throws SizeLimitExceededException 파일 크기가 초과된 경우
	 */
	protected void validateFileSize(FileItem[] fileItems, long maxSize) throws SizeLimitExceededException {
		if (fileItems == null) {
			return;
		}

		long totalSize = 0;

		for (FileItem fileItem : fileItems) {
			if (fileItem == null) {
				continue;
			}

			long size = fileItem.getMultipartFile().getSize();

			if (size > 0) {
				totalSize += size;
			}

			if (maxSize >= 0 && size > maxSize) {
				throw new SizeLimitExceededException("최대 파일 크기를 넘었습니다.(" + maxSize + "/" + size + ")");
			}
		}

		if (this.totalSize >= 0 && totalSize > this.totalSize) {
			throw new SizeLimitExceededException("최대 파일 크기를 넘었습니다.(" + this.totalSize + "/" + totalSize + ")");
		}
	}
}
