/*
 * @fileName : JavascriptCodeTypeTest.java
 * @date : 2013. 5. 23.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.web.taglib.bufferedresponse;

import junit.framework.Assert;

import org.junit.Test;

import com.diaimm.april.web.taglib.bufferedresponse.JavascriptCodeType;

/**
 * @author diaimm
 * 
 */
public class JavascriptCodeTypeTest {
	@Test
	public void sortedValuesTest() {
		JavascriptCodeType[] sortedValues = JavascriptCodeType.sortedValues();
		Assert.assertEquals(JavascriptCodeType.BIGPIPE, sortedValues[0]);
		Assert.assertEquals(JavascriptCodeType.JS_TO_BE_BUFFERED, sortedValues[1]);
	}
}
