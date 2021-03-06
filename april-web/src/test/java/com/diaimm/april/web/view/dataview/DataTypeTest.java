/*
 * @fileName : DataTypeTest.java
 * @date : 2013. 5. 24.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.web.view.dataview;

import com.diaimm.april.web.view.dataview.DataType;
import junit.framework.Assert;

import org.junit.Test;

import com.diaimm.april.commons.util.JaxbObjectMapper;
import com.diaimm.april.web.view.dataview.DataType.DefaultDataViewContext;

/**
 * @author diaimm
 * 
 */
public class DataTypeTest {
	@Test
	public void baseTest() {
		Assert.assertEquals(2, DataType.values().length);

		Assert.assertEquals("text/json", DataType.JSON.getContentType());
		Assert.assertEquals(JaxbObjectMapper.JSON, DataType.JSON.getMapper());
		Assert.assertEquals("text/xml", DataType.XML.getContentType());
		Assert.assertEquals(JaxbObjectMapper.XML, DataType.XML.getMapper());

		DefaultDataViewContext context = new DefaultDataViewContext("1", "2");
		Assert.assertEquals("1", context.getContentType());
		Assert.assertEquals("2", context.getResponseBody());
	}
}
