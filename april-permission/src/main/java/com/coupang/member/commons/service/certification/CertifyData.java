package com.coupang.member.commons.service.certification;

import java.io.Serializable;

import com.coupang.commons.enums.user.GenderType;
import com.coupang.commons.util.ObjectSupport;

/**
 * 인증에서 결과로 반환되는 회원 정보<br> 
 * 암호화 되어 전달한다. <br>
 * <br>
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 8. 12.
 */
public class CertifyData extends ObjectSupport implements Serializable {
	private static final long serialVersionUID = 1L;

	private boolean isSucc = false;
	private String errorMessage;
	private String name;
	private String email;
	private String ci;
	private String di;
	private String birthday;
	private GenderType genderType;
	private String certifySeq;
	private boolean isOver14;
	private String ssn;

	/**
	 * @return the ssn
	 */
	public String getSsn() {
		return ssn;
	}

	/**
	 * @param ssn the ssn to set
	 */
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	/**
	 * @return the isOver14
	 */
	public boolean isOver14() {
		return isOver14;
	}

	/**
	 * @param isOver14 the isOver14 to set
	 */
	public void setOver14(boolean isOver14) {
		this.isOver14 = isOver14;
	}

	/**
	 * @return the certifySeq
	 */
	public String getCertifySeq() {
		return certifySeq;
	}

	/**
	 * @param certifySeq the certifySeq to set
	 */
	public void setCertifySeq(String certifySeq) {
		this.certifySeq = certifySeq;
	}

	/**
	 * @return the birthday
	 */
	public String getBirthday() {
		return birthday;
	}

	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return the genderType
	 */
	public GenderType getGenderType() {
		return genderType;
	}

	/**
	 * @param genderType the genderType to set
	 */
	public void setGenderType(GenderType genderType) {
		this.genderType = genderType;
	}

	/**
	 * @return the isSucc
	 */
	public boolean isSucc() {
		return isSucc;
	}

	/**
	 * @param isSucc the isSucc to set
	 */
	public void setSucc(boolean isSucc) {
		this.isSucc = isSucc;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the ci
	 */
	public String getCi() {
		return ci;
	}

	/**
	 * @param ci the ci to set
	 */
	public void setCi(String ci) {
		this.ci = ci;
	}

	/**
	 * @return the di
	 */
	public String getDi() {
		return di;
	}

	/**
	 * @param di the di to set
	 */
	public void setDi(String di) {
		this.di = di;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

}