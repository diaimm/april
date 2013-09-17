/*
 * @fileName : TemplateMakerTest.java
 * @date : 2013. 6. 3.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.commons.util;

import junit.framework.Assert;

import org.junit.Test;

import com.coupang.member.commons.util.FreeMarkerTemplateBuilder;

/**
 * @author diaimm
 * 
 */
public class FreeMarkerTemplateBuilderTest {
	@Test
	public void constructorTest() {
		try {
			FreeMarkerTemplateBuilder target2 = new FreeMarkerTemplateBuilder("/com/coupang/member/commons/util", "sample1.ftl");
			Assert.assertEquals("sample1", target2.build());

			FreeMarkerTemplateBuilder target1 = new FreeMarkerTemplateBuilder(this.getClass(), "sample1.ftl");
			Assert.assertEquals("sample1", target1.build());
			Assert.assertEquals("sample1 : target1", target1.attribute().set("from", "target1").build());
			Assert.assertEquals("sample1", target1.attribute().remove("from").build());
		} catch (Exception e) {
			Assert.fail();
		}
	}
}
