/*
 * @fileName : EnvTest.java
 * @date : 2013. 6. 3.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.commons;

import junit.framework.Assert;
import org.junit.Test;

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
