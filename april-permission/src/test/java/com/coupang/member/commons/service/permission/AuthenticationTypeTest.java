/*
 * @fileName : AuthenticationTypeTest.java
 * @date : 2013. 6. 5.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.commons.service.permission;

import junit.framework.Assert;

import org.junit.Test;

import com.coupang.member.commons.service.permission.AuthenticationType;

/**
 * @author diaimm
 * 
 */
public class AuthenticationTypeTest {
	@Test
	public void testIt() {
		
		Assert.assertEquals(3, AuthenticationType.values().length);
		Assert.assertNotNull(AuthenticationType.DEFAULT);
		Assert.assertNotNull(AuthenticationType.MOBILE);
	}
}