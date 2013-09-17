/*
 * @fileName : ServiceUserUtilsTest.java
 * @date : 2013. 7. 2.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.commons.service.util;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import com.coupang.commons.util.DateUtil;
import com.coupang.member.commons.service.util.ServiceUserUtils;

/**
 * @author diaimm
 * 
 */
public class ServiceUserUtilsTest {
	@Test
	public void isSecessionWithin15Test() {
		String before15Days = DateUtil.date(DateUtil.move(-15, TimeUnit.DAYS), ServiceUserUtils.SESSION_DATE_TIME_FORMAT);
		Assert.assertFalse(ServiceUserUtils.isSecessionWithin15("N", before15Days));

		String before14Days = DateUtil.date(DateUtil.move(-14, TimeUnit.DAYS), ServiceUserUtils.SESSION_DATE_TIME_FORMAT);
		Assert.assertTrue(ServiceUserUtils.isSecessionWithin15("N", before14Days));

		String current = DateUtil.date(DateUtil.move(0, TimeUnit.DAYS), ServiceUserUtils.SESSION_DATE_TIME_FORMAT);
		Assert.assertTrue(ServiceUserUtils.isSecessionWithin15("N", current));
	}

	@Test
	public void isUnderAge14Test() {
		long before15Years = DateUtil.move(-365 * 15, TimeUnit.DAYS);
		Assert.assertFalse(ServiceUserUtils.isUnderAge14(DateUtil.date(before15Years, ServiceUserUtils.BIRTHDAY_TIME_FORMAT)));

		long before14Years = DateUtil.move(-365 * 14, TimeUnit.DAYS);
		Assert.assertTrue(ServiceUserUtils.isUnderAge14(DateUtil.date(before14Years, ServiceUserUtils.BIRTHDAY_TIME_FORMAT)));

		long before13Years = DateUtil.move(-365 * 13, TimeUnit.DAYS);
		Assert.assertTrue(ServiceUserUtils.isUnderAge14(DateUtil.date(before13Years, ServiceUserUtils.BIRTHDAY_TIME_FORMAT)));
	}
}
