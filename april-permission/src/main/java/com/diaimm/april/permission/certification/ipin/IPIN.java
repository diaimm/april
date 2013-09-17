/**
 * 
 */
package com.diaimm.april.permission.certification.ipin;

/**
 * IPIN 인증 통신 Entity
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 6. 21.
 */
public class IPIN {
	private String VNumber = ""; // 가상주민번호
	private String Name = ""; // 인증 고객 실명
	private String DupInfo = ""; // 중복가입 확인 정보
	/*
	 * 연령 코드
	 * 	0 : 9세 미만
	 * 	1 : 9세 이상 12세 미만
	 * 	2 : 12세 이상 14세 미만
	 * 	3 : 14세 이상 15세 미만
	 * 	4 : 15세 이상 18세 미만
	 * 	5 : 18세 이상 19세 미만
	 * 	6 : 19세 이상 20세 미만
	 * 	7 : 20세 이상
	 */
	private String AgeCode = "";
	/*
	 * 성별 코드
	 * 	0:여성
	 * 	1:남성
	 */
	private String GenderCode = ""; // 성별코드
	private String BirthDate = ""; // 생년월일
	/*
	 * 국적 정보
	 * 	0:내국인
	 * 	1:외국인
	 */
	private String NationalInfo = ""; // 국적정보
	private String CPRequestNum = ""; // CP 요청번호
	/*
	 * 고객의 본인확인 수단
	 * 	0 : 공인인증서
	 * 	1 : 카드
	 * 	2 : 핸드폰
	 * 	3 : 대면확인
	 * 	4 : 기타
	 */
	private String AuthInfo = ""; // 본인 확인 수단
	private String CoInfo1 = ""; // 연결정보(CI)
	private String CIUpdate = ""; // 연결정보(CI) 갱신횟수

	private String CipherDateTime = ""; // 암호화 데이터 생성 시간(YYMMDDHHMISS)
	private String CipherIPAddress = ""; // 암호화 데이터 생성 IP 주소

	/**
	 * @return the vNumber
	 */
	public String getVNumber() {
		return VNumber;
	}

	/**
	 * @param vNumber the vNumber to set
	 */
	public void setVNumber(String vNumber) {
		VNumber = vNumber;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		Name = name;
	}

	/**
	 * @return the dupInfo
	 */
	public String getDupInfo() {
		return DupInfo;
	}

	/**
	 * @param dupInfo the dupInfo to set
	 */
	public void setDupInfo(String dupInfo) {
		DupInfo = dupInfo;
	}

	/**
	 * @return the ageCode
	 */
	public String getAgeCode() {
		return AgeCode;
	}

	/**
	 * @param ageCode the ageCode to set
	 */
	public void setAgeCode(String ageCode) {
		AgeCode = ageCode;
	}

	/**
	 * @return the genderCode
	 */
	public String getGenderCode() {
		return GenderCode;
	}

	/**
	 * @param genderCode the genderCode to set
	 */
	public void setGenderCode(String genderCode) {
		GenderCode = genderCode;
	}

	/**
	 * @return the birthDate
	 */
	public String getBirthDate() {
		return BirthDate;
	}

	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(String birthDate) {
		BirthDate = birthDate;
	}

	/**
	 * @return the nationalInfo
	 */
	public String getNationalInfo() {
		return NationalInfo;
	}

	/**
	 * @param nationalInfo the nationalInfo to set
	 */
	public void setNationalInfo(String nationalInfo) {
		NationalInfo = nationalInfo;
	}

	/**
	 * @return the cPRequestNum
	 */
	public String getCPRequestNum() {
		return CPRequestNum;
	}

	/**
	 * @param cPRequestNum the cPRequestNum to set
	 */
	public void setCPRequestNum(String cPRequestNum) {
		CPRequestNum = cPRequestNum;
	}

	/**
	 * @return the authInfo
	 */
	public String getAuthInfo() {
		return AuthInfo;
	}

	/**
	 * @param authInfo the authInfo to set
	 */
	public void setAuthInfo(String authInfo) {
		AuthInfo = authInfo;
	}

	/**
	 * @return the coInfo1
	 */
	public String getCoInfo1() {
		return CoInfo1;
	}

	/**
	 * @param coInfo1 the coInfo1 to set
	 */
	public void setCoInfo1(String coInfo1) {
		CoInfo1 = coInfo1;
	}

	/**
	 * @return the cIUpdate
	 */
	public String getCIUpdate() {
		return CIUpdate;
	}

	/**
	 * @param cIUpdate the cIUpdate to set
	 */
	public void setCIUpdate(String cIUpdate) {
		CIUpdate = cIUpdate;
	}

	/**
	 * @return the cipherDateTime
	 */
	public String getCipherDateTime() {
		return CipherDateTime;
	}

	/**
	 * @param cipherDateTime the cipherDateTime to set
	 */
	public void setCipherDateTime(String cipherDateTime) {
		CipherDateTime = cipherDateTime;
	}

	/**
	 * @return the cipherIPAddress
	 */
	public String getCipherIPAddress() {
		return CipherIPAddress;
	}

	/**
	 * @param cipherIPAddress the cipherIPAddress to set
	 */
	public void setCipherIPAddress(String cipherIPAddress) {
		CipherIPAddress = cipherIPAddress;
	}
}