/*
 * @fileName : DataTypeTest.java
 * @date : 2013. 5. 24.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.component.web.view.dataview;

import junit.framework.Assert;

import org.junit.Test;

import com.coupang.member.commons.util.JaxbObjectMapper;
import com.coupang.member.component.web.view.dataview.DataType;
import com.coupang.member.component.web.view.dataview.DataType.DefaultDataViewContext;

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
