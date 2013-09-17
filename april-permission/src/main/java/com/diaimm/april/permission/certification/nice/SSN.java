/**
 * 
 */
package com.diaimm.april.permission.certification.nice;

import org.joda.time.LocalDate;

import com.coupang.commons.enums.user.GenderType;

/**
 * 한국신용평가정보 를 통한 인증 
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 8. 12.
 */
public class SSN {
	private String ci;
	private String di;
	private String name;
	private LocalDate birthday;
	private GenderType gender;

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
	 * @return the birthday
	 */
	public LocalDate getBirthday() {
		return birthday;
	}

	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return the gender
	 */
	public GenderType getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(GenderType gender) {
		this.gender = gender;
	}

}
