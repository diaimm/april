package com.diaimm.april.permission.permission;

import com.diaimm.april.permission.permission.annotation.Permission;

/**
 * User: diaimm(봉구)
 * Date: 13. 12. 2
 * Time: 오후 10:35
 */
public interface AuthenticationTypeFactory {
	public AuthenticationType create(Permission permission);
}
