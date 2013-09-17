/*
 * @(#)RenamePolicyFactory.java / version $Date$
 */
package com.coupang.member.commons.util.file;

/**
 * unique file 명을 생성하기 위한 policy factory
 */
public interface UniqueFileNamePolicyFactory {
	/**
	 * {@link UniqueFileNamePolicy}를 리턴한다
	 *
	 * @return {@link UniqueFileNamePolicy}
	 */
	UniqueFileNamePolicy create();
}
