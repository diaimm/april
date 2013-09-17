package com.coupang.member.commons.service.permission;

public enum AuthenticationFailType {
	REQUIRE_SMS_AUTH("REQUIRE_SMS_AUTH", "SMS 인증이 필요합니다."),
	REQUIRE_PHONENUMBER("REQUIRE_PHONENUMBER", ""),
	REQUIRE_REAL_NAME_CERTIFICATION("REQUIRE_REAL_NAME_CERTIFICATION", ""),
	REQUIRE_USE_CLAUSE_AGREE("REQUIRE_USE_CLAUSE_AGREE", ""),
	REQUIRE_PERSONAL_INFO_AGREE("REQUIRE_PERSONAL_INFO_AGREE", ""),
	REQUIRE_USER_NAME("REQUIRE_USER_NAME", ""),
	REQUIRE_SSN("REQUIRE_SSN", "주민등록번호가 정확하지 않습니다. 다시 입력해 주세요."),
	ALREADY_JOIN_USER("ALREADY_JOIN_USER", "해당 아이디로 이미 가입되어 있습니다. 해당 아이디로 로그인해주세요."),
	ALREADY_JOIN_EMAIL("ALREADY_JOIN_EMAIL", "이미 사용중인 이메일 주소입니다."),
	ALREADY_JOIN_PHONE("ALREADY_JOIN_PHONE", "이미 사용중인 휴대폰 번호입니다."),
	ALREADY_USE_PASSWORD("ALREADY_USE_PASSWORD", "현재 사용중인 비밀번호 입니다. 다른 비밀번호로 변경해주세요."),
	INVALID_EMAIL("INVALID_EMAIL", "이메일 형식이 잘못되었습니다."),
	INVALID_REAL_NAME_AUTH_KEY("INVALID_REAL_NAME_AUTH_KEY", ""),
	INVALID_IPIN_AUTH_KEY("INVALID_IPIN_AUTH_KEY", "실명인증 처리중 오류가 발생했습니다."),
	INVALID_PASSWORD("INVALID_PASSWORD", "비밀번호가 다릅니다."),
	INVALID_MOBILE_PHONE_NUMBER("INVALID_MOBILE_PHONE_NUMBER", ""),
	INVALID_BIRTHDAY("INVALID_BIRTHDAY", ""),
	EMAIL_EMPTY("EMAIL_EMPTY", "이메일 주소를 입력해주셔야 합니다."),
	GENDER_EMPTY("GENDER_EMPTY", ""),
	PASSWORD_NOT_EQUALS("PASSWORD_NOT_EQUALS", "변경할 비밀번호가 일치하지 않습니다."),
	WITHIN_15_REJOIN("WITHIN_15_REJOIN", ""),
	UNDER_AGE_14("UNDER_AGE_14", "만14세 미만은 가입 및 구매가 불가능합니다."),
	SMS_AUTH_OVER_10("SMS_AUTH_OVER_10", "동일 휴대폰번호로 인증은 1일 10회만 가능합니다."),
	NOT_EXIST_MEMBER_SRL("EXIST_MEMBER_SRL", "회원정보가 존재하지 않습니다."),
	OPERATION_FAILD("OPERATION_FAILD", "회원 가입 실패 하였습니다."),
	REAL_NAME_UNDER_AGE_14("REAL_NAME_UNDER_AGE_14", "14세 미만 입니다.");

	private final String type;
	private final String message;

	AuthenticationFailType(String type, String message) {
		this.type = type;
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	public String formatMessage(String param) {
		return getMessage();
	}
}
