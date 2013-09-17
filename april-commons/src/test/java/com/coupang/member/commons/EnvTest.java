/*
 * @fileName : EnvTest.java
 * @date : 2013. 6. 3.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.commons;

import junit.framework.Assert;

import org.junit.Test;

import com.coupang.member.commons.Env;

/**
 * @author diaimm
 * 
 */
public class EnvTest {
	@Test
	public void defaultEncodingTest() {
		Assert.assertEquals("UTF-8", Env.DEFAULT_ENCODING);
	}
}
