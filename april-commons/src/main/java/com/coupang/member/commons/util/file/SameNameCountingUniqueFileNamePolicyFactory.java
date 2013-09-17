/*
 * @(#)DefaultRenamePolicyFactory.java / version $Date$
 */
package com.coupang.member.commons.util.file;

import java.io.File;

/**
 * 기본 파일 이름 변경 정책 Factory
 *
 * 1. xxx.yyy
 * 2. xxx(0).yyy
 * 3. xxx(1).yyy
 *
 * @version $Rev$, $Date$
 */
public class SameNameCountingUniqueFileNamePolicyFactory implements UniqueFileNamePolicyFactory {
	private static final int DEFAULT_MAX_FILE_COUNT = 5;
	private final int maxFileCount;

	/**
	 * 생성자 (기본 retry 횟수는 5)
	 */
	public SameNameCountingUniqueFileNamePolicyFactory() {
		maxFileCount = DEFAULT_MAX_FILE_COUNT;
	}

	/**
	 * 생성자
	 *
	 * @param maxFileCount maxFileCount 횟수 - 1보다 작은 값이 유입되면 SameNameCountingUniqueFileNamePolicyFactory.DEFAULT_MAX_FILE_COUNT가 지정됩니다.
	 */
	public SameNameCountingUniqueFileNamePolicyFactory(int maxFileCount) {
		if (maxFileCount < 1) {
			maxFileCount = DEFAULT_MAX_FILE_COUNT;
		}
		this.maxFileCount = maxFileCount;
	}

	/**
	 * retry 횟수를 리턴한다
	 *
	 * @return retry 횟수
	 */
	public int getMaxFileCount() {
		return maxFileCount;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueFileNamePolicy create() {
		return new SameNameCountingUniqueFileNamePolicy(this.maxFileCount);
	}

	/**
	 * 기본 파일 이름 변경 정책
	 *
	 * @author Web Platform Development Lab
	 * @version $Rev$, $Date$
	 */
	static class SameNameCountingUniqueFileNamePolicy implements UniqueFileNamePolicy {
		private final int maxFileCount;
		private int retryCount = 0;

		/**
		 * 생성자
		 *
		 * @param maxFileCount maxFileCount 횟수
		 */
		SameNameCountingUniqueFileNamePolicy(int maxFileCount) {
			this.maxFileCount = maxFileCount;
		}

		public int getMaxFileCount() {
			return maxFileCount;
		}

		@Override
		public File getNextFile(File baseFile) {
			if (!baseFile.exists()) {
				return baseFile;
			}

			while (getRetryCount() < maxFileCount) {
				String nextFileName = createFileName(baseFile);
				File nextFile = new File(nextFileName);
				if (!nextFile.exists()) {
					return nextFile;
				}
				retryCount = getRetryCount() + 1;
			}

			throw new IllegalStateException(baseFile + " 파일 수가 최대 수(" + maxFileCount + ")를 초과했습니다");
		}

		String createFileName(File baseFile) {
			String baseFileName = baseFile.getName();
			String fileExt = "";
			int fileExtDot = baseFileName.lastIndexOf(".");
			if (fileExtDot > -1) {
				fileExt = baseFileName.substring(fileExtDot);
				baseFileName = baseFileName.substring(0, fileExtDot);
			}

			return baseFile.getParent() + File.separator + baseFileName + "(" + getRetryCount() + ")" + fileExt;
		}

		public int getRetryCount() {
			return retryCount;
		}
	}
}
