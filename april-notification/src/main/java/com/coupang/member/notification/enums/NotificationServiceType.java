package com.coupang.member.notification.enums;

/**
 * 메일을 전송하려고 하는 서비스 타입
 * 넷피온 메일 전송 에서 NotificationServiceType + "_INTERFACE" 테이블 이름이 된다. 
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 6. 25.
 */
public enum NotificationServiceType {
	/**	임시 비밀번호 발급	*/
	CUST_FDPW,
	/**	이메일 변경시 인증 메일	*/
	CUST_CHEM,
	/** 구독회원 등록시 */
	CUST_SUBSCRIPTION,
	/** 신규 회원 가입 메일  */
	CUST_WLCM,
	/** 회원 가입 추천 메일  */
	FRED_RECM,
	/**	회원정보 수정시 개인정보 변경 알림 메일	*/
	CUST_INFORMATION;
}