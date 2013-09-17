/**
 * 
 */
package com.coupang.member.commons.service.certification.nice;

import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.diaimm.april.permission.certification.nice.NiceSsnClient;

/**
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 8. 13.
 */
public class NiceSsnClientTest {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Test
	public void testGetBirthdayFromSsn_null() {
		logger.debug(NiceSsnClient.getBirthdayFromSsn("156789123").toString(DateTimeFormat.forPattern("yyyyMMdd")));
	}

	@Test
	public void testGetBirthdayFromSsn() {
		logger.debug(NiceSsnClient.getBirthdayFromSsn("8112131012722").toString(DateTimeFormat.forPattern("yyyyMMdd")));
	}
}
