/*
 * @fileName : MemberRepository.java
 * @date : 2013. 5. 31.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.db.mybatis.sample;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author diaimm
 * 
 */
@Repository("login")
public interface SampleRepository {
	long getTest(@Param("memberSrl") long memberSrl);

	Member findById(@Param("memberSrl") long memberSrl);

	long countById(@Param("memberSrl") long memberSrl);

	void insertOneMember(@Param("member") Member member);
}
