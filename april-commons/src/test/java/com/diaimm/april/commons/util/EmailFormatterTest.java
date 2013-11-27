/*
 * @fileName : EmailFormatterTest.java
 * @date : 2013. 7. 10.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.commons.util;

import com.diaimm.april.commons.util.WellKnownFormats.EmailFormatter;
import junit.framework.Assert;
import org.junit.Test;

/**
 * @author diaimm
 * 
 */
public class EmailFormatterTest {
	@Test
	public void emailFormatTest() {
		EmailFormatter formatter = new EmailFormatter(WellKnownFormats.EMAIL);

		String format = formatter.format("diaimm", "naver.com");
		Assert.assertEquals("diaimm@naver.com", format);

		String[] unformat = formatter.unformat(format);
		Assert.assertEquals(2, unformat.length);
		Assert.assertEquals("diaimm", unformat[0]);
		Assert.assertEquals("naver.com", unformat[1]);

		unformat = formatter.unformat("diaimm.1.a@naver.ccc.com");
		Assert.assertEquals(2, unformat.length);
		Assert.assertEquals("diaimm.1.a", unformat[0]);
		Assert.assertEquals("naver.ccc.com", unformat[1]);
	}
}
