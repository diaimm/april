package com.diaimm.april.permission.permission;


@SuppressWarnings("serial")
public class AuthenticationFailException extends RuntimeException {
	private AuthenticationFailType authenticationFailType;
	private String authenticationExceptionMessage;

	public AuthenticationFailException(AuthenticationFailType memberExceptionType) {
		this.authenticationFailType = memberExceptionType;
		this.authenticationExceptionMessage = memberExceptionType.getMessage();
	}

	public AuthenticationFailException(AuthenticationFailType memberExceptionType, String memberExceptionMessage) {
		this.authenticationFailType = memberExceptionType;
		this.authenticationExceptionMessage = memberExceptionMessage;
	}

	public AuthenticationFailType getAuthenticationFailType() {
		return authenticationFailType;
	}

	public void setMemberExceptionType(AuthenticationFailType authenticationFailType) {
		this.authenticationFailType = authenticationFailType;
	}

	public String getMemberExceptionMessage() {
		return authenticationExceptionMessage;
	}

	public void setMemberExceptionMessage(String authenticationExceptionMessage) {
		this.authenticationExceptionMessage = authenticationExceptionMessage;
	}
}