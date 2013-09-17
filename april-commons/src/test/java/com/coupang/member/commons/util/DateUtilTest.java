/*
 * @fileName : DateUtilTest.java
 * @date : 2013. 7. 2.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.commons.util;

import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.Test;

import com.coupang.member.commons.util.DateUtil;

/**
 * @author diaimm
 * 
 */
public class DateUtilTest {
	@Test
	public void dateTest() {
		System.out.println(DateUtil.date("yyyyMMdd--HH:mm:ss"));
	}

	@Test
	public void timeUnitTest() {
		Assert.assertEquals(3000, TimeUnit.MILLISECONDS.convert(3, TimeUnit.SECONDS));
		Assert.assertEquals(3, TimeUnit.SECONDS.convert(3, TimeUnit.SECONDS));
		Assert.assertEquals(0, TimeUnit.MINUTES.convert(3, TimeUnit.SECONDS));
	}

	@Test
	public void moveTest() {
		long current = DateUtil.mktime();
		long after3Seconds = DateUtil.move(current, 3, TimeUnit.SECONDS);
		long before3Seconds = DateUtil.move(current, -3, TimeUnit.SECONDS);

		Assert.assertEquals(current + 3000, after3Seconds);
		Assert.assertEquals(current - 3000, before3Seconds);
		System.out.println(DateUtil.date(before3Seconds, "yyyy-MM-dd HH:mm:ss"));
		System.out.println(DateUtil.date(current, "yyyy-MM-dd HH:mm:ss"));
		System.out.println(DateUtil.date(after3Seconds, "yyyy-MM-dd HH:mm:ss"));

		long after3Hours = DateUtil.move(current, 3, TimeUnit.HOURS);
		long before3Hours = DateUtil.move(current, -3, TimeUnit.HOURS);

		Assert.assertEquals(current + 3 * (3600 * 1000), after3Hours);
		Assert.assertEquals(current - 3 * (3600 * 1000), before3Hours);
		System.out.println(DateUtil.date(before3Hours, "yyyy-MM-dd HH:mm:ss"));
		System.out.println(DateUtil.date(current, "yyyy-MM-dd HH:mm:ss"));
		System.out.println(DateUtil.date(after3Hours, "yyyy-MM-dd HH:mm:ss"));
	}

	@Test
	public void getInterval() {
		long current = DateUtil.mktime();
		long after3Seconds = DateUtil.move(current, 3, TimeUnit.SECONDS);

		Assert.assertEquals(0, DateUtil.getInterval(current, after3Seconds, TimeUnit.MINUTES));
		Assert.assertEquals(3, DateUtil.getInterval(current, after3Seconds, TimeUnit.SECONDS));
		Assert.assertEquals(3000, DateUtil.getInterval(current, after3Seconds, TimeUnit.MILLISECONDS));

		long befor3Seconds = DateUtil.move(current, -3, TimeUnit.DAYS);
		Assert.assertEquals(-3, DateUtil.getInterval(current, befor3Seconds, TimeUnit.DAYS));
	}
}
