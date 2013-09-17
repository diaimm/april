/*
 * @fileName : PropertyConfigurerTest.java
 * @date : 2013. 7. 11.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.commons.property;

import com.coupang.member.commons.ByPhase;
import com.coupang.member.commons.property.base.BaseURLProperties;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * @author diaimm
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:com/coupang/member/commons/property/PropertyConfigurerTest.xml")
public class PropertyConfigurerTest {
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private ByPhase byPhase;

	@Test
	public void classScanningTest() throws IOException {
		String property = byPhase.getProperty(BaseURLProperties.COUPANG);
		Assert.assertEquals(null, property);
	}
}
