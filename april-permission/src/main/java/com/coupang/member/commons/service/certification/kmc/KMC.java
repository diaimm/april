/**
 * 
 */
package com.coupang.member.commons.service.certification.kmc;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.joda.time.LocalDate;

import com.coupang.commons.enums.user.GenderType;
import com.coupang.commons.enums.user.NationType;

/**
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 6. 24.
 */
public class KMC {
	private final String name;
	private final NationType nation;
	private final GenderType gender;
	private final LocalDate birthday;
	private final String ci;
	private final String di;
	private String ipAddress;

	public KMC(String name, NationType nation, GenderType gender, LocalDate birthday, String ci, String di, String ipAddress) {
		this.name = name;
		this.nation = nation;
		this.gender = gender;
		this.birthday = birthday;
		this.ci = ci;
		this.di = di;
		this.ipAddress = ipAddress;
	}

	/**
	 * 만 14세 미만인지 아닌지 여부 -> 15살 생일 지난 사람 
	 * @return
	 */
	public boolean isOver14() {
		int factor = 0;
		int age = 0;

		try {
			if (this.getBirthday() != null) {
				Calendar today = new GregorianCalendar();
				Calendar birth = new GregorianCalendar();
				birth.setTime(this.getBirthday().toDate());

				//생일이 안지났으면 1살 뺀다.
				if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
					factor = -1;
				}

				age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR) + factor;

				if (age >= 15) {
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}

		return false;
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the nation
	 */
	public NationType getNation() {
		return nation;
	}

	/**
	 * @return the gender
	 */
	public GenderType getGender() {
		return gender;
	}

	/**
	 * @return the birthday
	 */
	public LocalDate getBirthday() {
		return birthday;
	}

	/**
	 * @return the ci
	 */
	public String getCi() {
		return ci;
	}

	/**
	 * @return the di
	 */
	public String getDi() {
		return di;
	}
}