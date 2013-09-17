/**
 * 
 */
package com.coupang.member.commons.service.model;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coupang.member.commons.service.model.DomainBaseServiceUserFactory;

/**
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 6. 11.
 */
public class DomainBaseServiceUserFactoryTest {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Test
	public void test() {
		String memberAsJsonString = "{\"memberSrl\":\"123123123\",\"personal\":{\"name\":\"testuser\",\"email\":null,\"birthday\":\"20131013\",\"gender\":null,\"phoneNumber\":null,\"password\":null,\"memberLevel\":null,\"memberType\":null,\"registeredDttm\":null,\"subscribedDttm\":null,\"updatedDttm\":null,\"joinServiceDomain\":null,\"joinServiceUsePath\":null},\"certification\":null,\"subscribeInfos\":null,\"recommenderId\":null,\"recommederToken\":null,\"subscription\":false,\"blocked\":false}";
		DomainBaseServiceUserFactory factory = new DomainBaseServiceUserFactory(memberAsJsonString);

		logger.debug(factory.createUser().toString());
	}
}