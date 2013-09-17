/**
 *
 */
package com.coupang.member.notification.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.coupang.member.commons.util.DateUtil;
import com.coupang.member.notification.NotificationMessage;
import com.coupang.member.notification.NotificationRepository;
import com.coupang.member.notification.enums.NotificationPurpose;

/**
 * @author 산토리니 윤영욱 (readytogo@coupang.com), 이성준
 * @version 2013. 6. 18.
 */
final class SMSMessage implements NotificationMessage {
	private static final String DEFAULT_DATE_FORMAT = "yyyyMMddHHmmss";
	private static final String COUPANG_NUMBER = "1577-7011";
	private String trNum;
	private String sendDate;
	private String toNumber;
	private String fromNumber = COUPANG_NUMBER;
	private String message;
	private boolean containsUrl = Boolean.FALSE;
	private NotificationPurpose notificationPurpose;

	SMSMessage() {
	}

	/**
	 * @param toNumber
	 */
	public SMSMessage(String toNumber) {
		this.toNumber = toNumber;
	}

	/**
	 * 기본값 설정 : 쿠팡 대표 번호
	 *
	 * @return the fromNumber
	 */
	public String getFromNumber() {
		if (StringUtils.isBlank(fromNumber)) {
			return COUPANG_NUMBER;
		}

		return fromNumber;
	}

	/**
	 * @param fromNumber the fromNumber to set
	 */
	public void setFromNumber(String fromNumber) {
		this.fromNumber = fromNumber;
	}

	/**
	 * 메시지 안에 url 정보가 있는지 여부
	 *
	 * @return
	 */
	public String getMessageType() {
		if (containsUrl) {
			return "1";
		}

		return "0";
	}

	/**
	 * 기본값 설정 : 현재일시
	 *
	 * @return the sendDate
	 */
	public String getSendDate() {
		try {
			if (StringUtils.isBlank(sendDate)) {
				throw new Exception();
			}

			SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
			Date sendDateTmp = format.parse(sendDate);
			if (StringUtils.equals(format.format(sendDateTmp), sendDate) == false) {
				throw new Exception();
			}

			return sendDate;
		} catch (Exception e) {
			// 기본값
			return DateUtil.date(DEFAULT_DATE_FORMAT);
		}
	}

	/**
	 * @param sendDate the sendDate to set
	 */
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	/**
	 * @return the trNum
	 */
	public String getTrNum() {
		return trNum;
	}

	/**
	 * @param trNum the trNum to set
	 */
	public void setTrNum(String trNum) {
		this.trNum = trNum;
	}

	/**
	 * @return the toNumber
	 */
	public String getToNumber() {
		return toNumber;
	}

	/**
	 * @param toNumber the toNumber to set
	 */
	public void setToNumber(String toNumber) {
		this.toNumber = toNumber;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the notificationPurpose
	 */
	public NotificationPurpose getNotificationPurpose() {
		return notificationPurpose;
	}

	/**
	 * @param notificationPurpose the notificationPurpose to set
	 */
	public void setNotificationPurpose(NotificationPurpose notificationPurpose) {
		this.notificationPurpose = notificationPurpose;
	}

	/**
	 * @return the hasMessageUrls
	 */
	public boolean isContainsUrl() {
		return containsUrl;
	}

	/**
	 * @param containsUrl the containsUrl to set
	 */
	public void setContainsUrl(boolean containsUrl) {
		this.containsUrl = containsUrl;
	}

	/**
	 * @return
	 */
	public SMSMessage clone() {
		SMSMessage ret = new SMSMessage(this.toNumber);
		ret.trNum = this.trNum;
		ret.sendDate = this.sendDate;
		ret.fromNumber = this.fromNumber;
		ret.message = this.message;
		ret.containsUrl = this.containsUrl;
		ret.notificationPurpose = this.notificationPurpose;

		return ret;
	}

	/**
	 * @see com.coupang.member.notification.NotificationMessage#getRepositoryType()
	 */
	@Override
	public Class<? extends NotificationRepository<?>> getRepositoryType() {
		return SMSRepository.class;
	}

	/**
	 * (
	 *
	 * @see com.coupang.member.notification.NotificationMessage#getDbQualifierId()
	 */
	@Override
	public String getDbQualifierId() {
		return "sms";
	}
}