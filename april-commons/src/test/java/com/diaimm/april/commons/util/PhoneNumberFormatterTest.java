/*
 * @fileName : PhoneNumberFormatterTest.java
 * @date : 2013. 7. 10.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.commons.util;

import junit.framework.Assert;

import org.junit.Test;

import com.diaimm.april.commons.util.WellKnownFormats;
import com.diaimm.april.commons.util.WellKnownFormats.PhoneNumberFormatter;

/**
 * @author diaimm
 * 
 */
public class PhoneNumberFormatterTest {
	@Test
	public void mobileFormatTest() {
		PhoneNumberFormatter formatter = new PhoneNumberFormatter(WellKnownFormats.MOBILE);
		Assert.assertEquals(null, formatter.format("123", 234, "5511"));
		Assert.assertEquals("010-234-5511", formatter.format("010", 234, "5511"));
		Assert.assertEquals("011-234-5511", formatter.format("011", 234, "5511"));
		Assert.assertEquals(null, formatter.format("012", 234, "5511"));
		Assert.assertEquals(null, formatter.format("013", 234, "5511"));
		Assert.assertEquals(null, formatter.format("014", 234, "5511"));
		Assert.assertEquals(null, formatter.format("015", 234, "5511"));
		Assert.assertEquals("016-234-5511", formatter.format("016", 234, "5511"));
		Assert.assertEquals("017-234-5511", formatter.format("017", 234, "5511"));
		Assert.assertEquals("018-234-5511", formatter.format("018", 234, "5511"));
		Assert.assertEquals("019-234-5511", formatter.format("019", 234, "5511"));

		String[] stripFormat = formatter.unformat("019-234-5511");
		Assert.assertEquals(3, stripFormat.length);
		Assert.assertEquals("019", stripFormat[0]);
		Assert.assertEquals("234", stripFormat[1]);
		Assert.assertEquals("5511", stripFormat[2]);

		String[] stripFormat2 = formatter.unformat("014-234-5511");
		Assert.assertEquals(null, stripFormat2);
	}
}
