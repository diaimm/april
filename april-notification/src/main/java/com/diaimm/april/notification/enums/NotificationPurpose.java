/**
 * 
 */
package com.diaimm.april.notification.enums;

/**
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 6. 18.
 */
public enum NotificationPurpose {
	JOIN("회원 가입"), // 
	FIND_ACCOUNT("회원 계정 찾기"), //
	FIND_PASSWORD("회원 비밀번호 찾기"), //
	PARTNER_AGREE("파트너 동의"), //
	DELIVERY_AGREE("배송 동의"), //
	UNKNOWN("알려지지않음");

	private String description;

	NotificationPurpose(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
}