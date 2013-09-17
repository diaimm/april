/*
 * @fileName : EmailFormatterTest.java
 * @date : 2013. 7. 10.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.commons.util;

import junit.framework.Assert;

import org.junit.Test;

import com.coupang.member.commons.util.WellKnownFormats;
import com.coupang.member.commons.util.WellKnownFormats.EmailFormatter;

/**
 * @author diaimm
 * 
 */
public class EmailFormatterTest {
	@Test
	public void emailFormatTest() {
		EmailFormatter formatter = new EmailFormatter(WellKnownFormats.EMAIL);

		String format = formatter.format("diaimm", "coupang.com");
		Assert.assertEquals("diaimm@coupang.com", format);

		String[] unformat = formatter.unformat(format);
		Assert.assertEquals(2, unformat.length);
		Assert.assertEquals("diaimm", unformat[0]);
		Assert.assertEquals("coupang.com", unformat[1]);

		unformat = formatter.unformat("diaimm.1.a@coupang.ccc.com");
		Assert.assertEquals(2, unformat.length);
		Assert.assertEquals("diaimm.1.a", unformat[0]);
		Assert.assertEquals("coupang.ccc.com", unformat[1]);
	}
}
