package com.diaimm.april.permission.certification.nice;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import KISINFO.VNO.VNOInterop;

import com.coupang.commons.enums.user.GenderType;

/**
 * NICE 한국신용평가정보 를 통한 인증 - CI/DI 인증 
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 8. 12.
 */
public class NiceSsnClient {
	private static final Logger log = LoggerFactory.getLogger(NiceSsnClient.class);

	private static String siteCode = "B827"; // IPIN 서비스 사이트 코드		(NICE신용평가정보에서 발급한 사이트코드)
	private static String sitePw = "50161373"; // IPIN 서비스 사이트 패스워드	(NICE신용평가정보에서 발급한 사이트패스워드)
	/*
	┌ sFlag 변수에 대한 설명  ─────────────────────────────────────────────────────
		실명확인 서비스 구분값.
		
		JID : 일반실명확인 서비스 (주민등록번호)
		SID : 안심실명확인 서비스 (안심키값)
	└──────────────────────────────────────────────────────────────────
	*/
	private static String flag = "JID";

	public static String getDI(String jumin) {
		// 중복(가입)확인모듈 객체 생성
		VNOInterop vnoInterop = new VNOInterop();

		/* ──── DI 값을 추출하기 위한 부분 Start */
		// Method 결과값(iRtn)에 따라, 프로세스 진행여부를 파악합니다.
		int iRtnDI = vnoInterop.fnRequestDupInfo(siteCode, sitePw, jumin, flag);

		// Method 결과값에 따른 처리사항
		if (iRtnDI == 1) {

			// iRtn 값이 정상이므로, 귀사의 기획의도에 맞게 진행하시면 됩니다.
			// 아래와 같이 getDupInfo 함수를 통해 DI 값(64 Byte)을 추출할 수 있습니다.
			String sDupInfo = vnoInterop.getDupInfo();

			return sDupInfo;
		} else if (iRtnDI == 3) {
			log.error("[사용자 정보와 실명확인 서비스 구분값 매핑 오류]");
			log.error("사용자 정보와 실명확인 서비스 구분값이 서로 일치하도록 매핑하여 주시기 바랍니다.");
		} else if (iRtnDI == -9) {
			log.error("[입력값 오류]");
			log.error("fnRequestDupInfo 함수 처리시, 필요한 4개의 파라미터값의 정보를 정확하게 입력해 주시기 바랍니다.");
		} else if (iRtnDI == -21 || iRtnDI == -31 || iRtnDI == -34) {
			log.error("[통신오류]");
			log.error("서버 네트웍크 및 방확벽 관련하여 아래 IP와 Port(5개)를 오픈해 주셔야 이용 가능합니다.");
			log.error("IP : 203.234.219.72 / Port : 81, 82, 83, 84, 85");
		} else {
			log.error("iRtnDI 값 확인 후, NICE신용평가정보 개발 담당자에게 문의해 주세요.");
		}

		return null;
	}

	public static String getCI(String jumin) {
		// 중복(가입)확인모듈 객체 생성
		VNOInterop vnoInterop = new VNOInterop();

		int iRtnCI = vnoInterop.fnRequestConnInfo(siteCode, sitePw, jumin, flag);

		// Method 결과값에 따른 처리사항
		if (iRtnCI == 1) {

			// iRtn 값이 정상이므로, 귀사의 기획의도에 맞게 진행하시면 됩니다.
			// 아래와 같이 getConnInfo 함수를 통해 CI 값(88 Byte)을 추출할 수 있습니다.
			String sConnInfo = vnoInterop.getConnInfo();
			return sConnInfo;
		} else if (iRtnCI == 3) {
			log.error("[사용자 정보와 실명확인 서비스 구분값 매핑 오류]");
			log.error("사용자 정보와 실명확인 서비스 구분값이 서로 일치하도록 매핑하여 주시기 바랍니다.");
		} else if (iRtnCI == -9) {
			log.error("[입력값 오류]");
			log.error("fnRequestConnInfo 함수 처리시, 필요한 4개의 파라미터값의 정보를 정확하게 입력해 주시기 바랍니다.");
		} else if (iRtnCI == -21 || iRtnCI == -31 || iRtnCI == -34) {
			log.error("[통신오류]");
			log.error("서버 네트웍크 및 방확벽 관련하여 아래 IP와 Port(5개)를 오픈해 주셔야 이용 가능합니다.");
			log.error("IP : 203.234.219.72 / Port : 81, 82, 83, 84, 85");
		} else {
			log.error("iRtnCI 값 확인 후, NICE신용평가정보 개발 담당자에게 문의해 주세요.");
		}

		return null;
	}

	public static String getBirthDay(String jumin) {
		VNOInterop vnoInterop = new VNOInterop();

		/* ──── DI 값을 추출하기 위한 부분 Start */
		// Method 결과값(iRtn)에 따라, 프로세스 진행여부를 파악합니다.
		int iRtnDI = vnoInterop.fnRequestDupInfo(siteCode, sitePw, jumin, flag);

		// Method 결과값에 따른 처리사항
		if (iRtnDI == 1) {

			// iRtn 값이 정상이므로, 귀사의 기획의도에 맞게 진행하시면 됩니다.
			// 아래와 같이 getDupInfo 함수를 통해 DI 값(64 Byte)을 추출할 수 있습니다.
			String birthDay = vnoInterop.getBirthDate();

			return birthDay;
		} else if (iRtnDI == 3) {
			log.error("[사용자 정보와 실명확인 서비스 구분값 매핑 오류]");
			log.error("사용자 정보와 실명확인 서비스 구분값이 서로 일치하도록 매핑하여 주시기 바랍니다.");
		} else if (iRtnDI == -9) {
			log.error("[입력값 오류]");
			log.error("fnRequestDupInfo 함수 처리시, 필요한 4개의 파라미터값의 정보를 정확하게 입력해 주시기 바랍니다.");
		} else if (iRtnDI == -21 || iRtnDI == -31 || iRtnDI == -34) {
			log.error("[통신오류]");
			log.error("서버 네트웍크 및 방확벽 관련하여 아래 IP와 Port(5개)를 오픈해 주셔야 이용 가능합니다.");
			log.error("IP : 203.234.219.72 / Port : 81, 82, 83, 84, 85");
		} else {
			log.error("iRtnDI 값 확인 후, NICE신용평가정보 개발 담당자에게 문의해 주세요.");
		}

		return null;
	}

	/**
	 * 주민 등록 번호를 가공하여 생년월일 추출
	 * @param ssn	'-' 없이 숫자만 입력
	 * @return
	 */
	public static LocalDate getBirthdayFromSsn(String ssn) {
		if (StringUtils.length(ssn) != 13) {
			return null;
		}

		String ssn1 = StringUtils.substring(ssn, 0, 6);
		String ssn2 = StringUtils.substring(ssn, 6);

		if (ssn2.startsWith("1") || ssn2.startsWith("2") || ssn2.startsWith("5") || ssn2.startsWith("6")) {
			return DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate("19" + ssn1.substring(0, 6));
		} else {
			return DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate("20" + ssn1.substring(0, 6));
		}
	}

	/**
	 * 주민 등록 번호를 가공하여 성별 추출
	 * @param ssn
	 * @return
	 */
	public static GenderType getGenderFromSsn(String ssn) {
		if (StringUtils.length(ssn) != 13) {
			return null;
		}

		String ssn1 = StringUtils.substring(ssn, 0, 6);
		String ssn2 = StringUtils.substring(ssn, 6);
		String chkSsn = ssn2.substring(0, 1);

		if (Integer.parseInt(chkSsn) % 2 == 0) {
			return GenderType.FEMALE;
		} else {
			return GenderType.MALE;
		}
	}
}
