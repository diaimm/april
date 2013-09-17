/*
 * @fileName : Member.java
 * @date : 2013. 5. 24.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.db.mybatis.sample;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 회원정보 도메인 모델 (Aggregate Root)
 * 
 * @author diaimm
 * @version
 * 
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013.06.03
 * @see 도메인 추가
 */
public class Member {
	// 식별자
	private long memberSrl;
	// 추천인 정보
	private String recommenderId;
	private String recommederToken;

	/*
	 * setters / getters
	 */

	/**
	 * @return the memberSrl
	 */
	public long getMemberSrl() {
		return memberSrl;
	}

	/**
	 * @param memberSrl
	 *            the memberSrl to set
	 */
	public void setMemberSrl(long memberSrl) {
		this.memberSrl = memberSrl;
	}

	/**
	 * @return the recommenderId
	 */
	public String getRecommenderId() {
		return recommenderId;
	}

	/**
	 * @param recommenderId
	 *            the recommenderId to set
	 */
	public void setRecommenderId(String recommenderId) {
		this.recommenderId = recommenderId;
	}

	/**
	 * @return the recommederToken
	 */
	public String getRecommederToken() {
		return recommederToken;
	}

	/**
	 * @param recommederToken
	 *            the recommederToken to set
	 */
	public void setRecommederToken(String recommederToken) {
		this.recommederToken = recommederToken;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}