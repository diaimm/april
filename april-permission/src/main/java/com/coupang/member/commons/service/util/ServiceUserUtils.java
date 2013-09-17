/**
 * 
 */
package com.coupang.member.commons.service.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.coupang.commons.crypto.AES.AESType;
import com.coupang.commons.crypto.Crypto;
import com.coupang.commons.util.DateUtil;

/**
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 6. 4.
 */
public final class ServiceUserUtils {
	public static final String SESSION_DATE_TIME_FORMAT = "yyyyMMddHHmmss";
	public static final String BIRTHDAY_TIME_FORMAT = "yyyyMMdd";
	public static final String REJOIN_USER_VALUE = "-1"; // 재가입자면 recommender값에 "-1"을 넣는다.
	public static final Long FRIEND_RECOMMEND_POINT = 2000L;
	public static final String CASH_CODE_FRIEND_JOIN = "FRIEND.JOIN";

	/**
	 * 세션 만료 여부 - 1분 동안 유효하다.
	 * 
	 * @param timestamp
	 * @return
	 */
	public static boolean isExpired(String timestamp) {
		final long EXPIRE_TIME = 1 * 60 * 60; // 60min

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
		String toyyyyMMddHHmmss = timestamp;
		String fromyyyyMMddHHmmss = formatter.format(Calendar.getInstance(Locale.KOREA).getTime().getTime());

		if (fromyyyyMMddHHmmss == null || fromyyyyMMddHHmmss.length() != 14) {
			return true;
		}
		if (toyyyyMMddHHmmss == null || toyyyyMMddHHmmss.length() != 14) {
			return true;
		}

		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddHHmmss");
		DateTime endDt = fmt.parseDateTime(fromyyyyMMddHHmmss);
		DateTime today = fmt.parseDateTime(toyyyyMMddHHmmss);

		long diff = (endDt.getMillis() - today.getMillis()) / 1000;

		if (diff < EXPIRE_TIME) {
			return false;
		} else {
			return true;
		}
	}
	
	public static String generateMemberTicket(String key) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
		String timestamp = formatter.format(new java.util.Date());
		return Crypto.aes(AESType.AES128).encrypt(timestamp, key);
	}

	public static boolean isInValidMemberTicket(String ticket , String key) {
		try {
			String timestamp = Crypto.aes(AESType.AES128).decrypt(ticket, key);
			if (StringUtils.isEmpty(timestamp) || ServiceUserUtils.isExpired(timestamp)) {
				return true;
			}
		} catch (Exception e) {
			return true;
		}
		return false;
	}

	/**
	 * 복호화된 sms 인증 번호
	 * 
	 * @param encryptedSmsAuthNumber
	 * @param smsAuthEncryptKey
	 * @return
	 */
	public static String getDecryptedSmsAuthNumber(String encryptedSmsAuthNumber, String smsAuthEncryptKey) {
		try {
			return Crypto.aes(AESType.AES128).decrypt(new String(Base64.decodeBase64(encryptedSmsAuthNumber)), smsAuthEncryptKey);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 탈퇴회원 유무
	 */
	public static boolean isSecession(String isMemberYn, String secessionDate) {
		if ("N".equals(isMemberYn) && StringUtils.isNotBlank(secessionDate)) {
			return true;
		}
		return false;
	}

	/**
	 * 탈퇴후 15일 이내인지 여부 판단. 탈퇴회원 && 탈퇴15일 지나면 재가입가능 나머지는 재가입 불가능
	 */
	public static boolean isSecessionWithin15(String isMemberYn, String secessionDate) {
		if (isSecession(isMemberYn, secessionDate)) {
			long sessionDateTimeStamp = DateUtil.mktimeQuietly(secessionDate, SESSION_DATE_TIME_FORMAT);
			if (DateUtil.move(sessionDateTimeStamp, 15, TimeUnit.DAYS) > DateUtil.mktime()) {
				return true;
			}
		}
		return false;
	}

	public static String getFormattedSecessionDate(String secessionDate) {
		if (StringUtils.isEmpty(secessionDate)) {
			return secessionDate;
		}

		return DateUtil.date(DateUtil.mktimeQuietly(secessionDate, SESSION_DATE_TIME_FORMAT), "yyyy년 MM월 dd일 HH시 mm분");
	}

	/**
	 * email 변경 토큰 생성
	 * 
	 * @param memberSrl
	 * @param emailChgSrl
	 * @param chageEmailAddress
	 * @param ecryptKey
	 * @return
	 */
	public static String getEmailChangeToken(String memberSrl, String emailChgSrl, String chageEmailAddress, String ecryptKey) {
		try {
			String smsAuthNumEcryptKey = ecryptKey; // byPhase.getProperty("sms_auth_encrypt_key");
			return Crypto.aes(AESType.AES128).encrypt(memberSrl + ":" + emailChgSrl + ":" + chageEmailAddress, smsAuthNumEcryptKey);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 14세 미만 체크
	 * 
	 * @param birthday
	 * @return
	 */
	public static boolean isUnderAge14(String birthday) {
		long birthdayTimestampe = DateUtil.mktimeQuietly(birthday, BIRTHDAY_TIME_FORMAT);
		Period period = new Period(birthdayTimestampe, DateUtil.mktime(), PeriodType.yearMonthDayTime());

		int age = period.getYears();
		if (age < 14) {
			return true;
		}
		return false;
	}
}