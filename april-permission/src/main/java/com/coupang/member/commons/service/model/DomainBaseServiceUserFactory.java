/**
 * 
 */
package com.coupang.member.commons.service.model;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coupang.commons.enums.user.GenderType;
import com.coupang.commons.enums.user.service.MemberLevelType;
import com.coupang.commons.util.DateUtil;

/**
 * 도메인 객체를 통한 ServiceUser 생성
 * 
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 6. 10.
 */
public class DomainBaseServiceUserFactory implements ServiceUserFactory {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	private String memberId;
	private String email;
	private String name;
	private String birthday;
	private GenderType genderType;
	private String realNameVerified;
	private String recommendationJoinToken;
	private String memberLevelType;
	private String superYn;

	/**
	 * 
	 * @param memberAsJsonString
	 */
	public DomainBaseServiceUserFactory(String memberAsJsonString) {
		ObjectMapper ObjectMapper = new ObjectMapper();

		try {
			Map<String, Map<String, String>> memberMap = ObjectMapper.readValue(memberAsJsonString, Map.class);
			Map<String, String> personal = (Map<String, String>)MapUtils.getObject(memberMap, "personal");
			Map<String, String> certification = (Map<String, String>)MapUtils.getObject(memberMap, "certification");

			this.memberId = MapUtils.getString(memberMap, "memberId");
			this.email = MapUtils.getString(personal, "email");
			this.name = MapUtils.getString(personal, "name");
			this.birthday = MapUtils.getString(personal, "birthday");
			this.genderType = GenderType.valueOf(MapUtils.getString(personal, "genderType"));
			this.realNameVerified = MapUtils.getString(certification, "realNameVerified");
			this.recommendationJoinToken = MapUtils.getString(certification, "recommendationJoinToken");
			this.memberLevelType = MapUtils.getString(personal, "memberLevelType");
			// 수퍼권한은 나중에라도 제거하는것이 맞겠다. 현재는 1명의 슈퍼 유저가 있다고 함...
			this.superYn = StringUtils.equals(memberLevelType, MemberLevelType.ADMIN.name()) ? "Y" : "N";

		} catch (Exception e) {
			logger.error("Login Member Json parsing error, memberAsJsonString:" + memberAsJsonString, e);
		}
	}

	/**
	 * @see com.coupang.member.commons.service.model.ServiceUserFactory#createUser()
	 */
	@Override
	public ServiceUser createUser() {
		try {
			DefaultServiceUser serviceUser = new DefaultServiceUser();
			serviceUser.setLastLoginDttm(DateUtil.date(ServiceUser.LOGIN_DATE_FORMAT));
			serviceUser.setMemberId(this.memberId);
			serviceUser.setUserId(this.email);
			serviceUser.setEmail(this.email);
			serviceUser.setName(this.name);
			serviceUser.setBirthDay(this.birthday);
			serviceUser.setGenderType(this.genderType.name());
			serviceUser.setRealNameVerified(this.realNameVerified);
			serviceUser.setRecommendationJoinToken(this.recommendationJoinToken);
			serviceUser.setMemberLevelType(this.memberLevelType);
			serviceUser.setSuperYn(this.superYn);

			return serviceUser;
		} catch (Exception e) {
			logger.error("Create Domain base User fail", e);

			return new DefaultServiceUser();
		}
	}
}
