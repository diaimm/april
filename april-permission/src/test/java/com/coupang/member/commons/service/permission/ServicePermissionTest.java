/*
 * @fileName : UserPermissionTest.java
 * @date : 2013. 5. 27.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.commons.service.permission;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.diaimm.april.permission.permission.ServicePermission;

/**
 * @author diaimm
 * 
 */
public class ServicePermissionTest {
	@Test
	public void getRequiredPermissionsTest() {
		List<ServicePermission> requiredPermissions = ServicePermission.REALNAME.getFullPermissionCheckers();
		Assert.assertEquals(ServicePermission.SUBSCRIBE, requiredPermissions.get(0));
		Assert.assertEquals(ServicePermission.MEMBER, requiredPermissions.get(1));
		Assert.assertEquals(ServicePermission.REALNAME, requiredPermissions.get(2));
	}

	@Test
	public void getCurrentUrlTest() {

	}
}
