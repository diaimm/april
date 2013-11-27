/*
 * @fileName : PropertyConfigurerTest.java
 * @date : 2013. 7. 11.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.commons.property;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.diaimm.april.commons.ByPhase;

/**
 * @author diaimm
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:com/diaimm/april/commons/property/PropertyConfigurerTest.xml")
public class PropertyConfigurerTest {
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private ByPhase byPhase;

	@Test
	public void testIt() {

	}
}
