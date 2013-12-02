package com.diaimm.april.permission.permission;

public interface AuthenticationFailType {
	public String getType();

	public String getMessage();

	public String formatMessage(String param);
}
