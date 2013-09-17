/**
 * 
 */
package com.diaimm.april.notification.model;

import org.apache.commons.lang.StringUtils;

import com.diaimm.april.notification.NotificationMessage;
import com.diaimm.april.notification.NotificationRepository;
import com.diaimm.april.notification.enums.NotificationServiceType;

/**
 * @author 산토리니 윤영욱 (readytogo@coupang.com), 이성준
 * @version 2013. 6. 19.
 */
final class MailMessage implements NotificationMessage {
	private static final String COUPANG_EMAIL = "noreply@e.coupang.com";
	private static final String COUPANG_EMAIL_NAME = "쿠팡";
	private static final String TABLE_POSTFIX = "_INTERFACE";
	private static final String SEQ_POSTFIX = "_SEQ.NEXTVAL";

	/**	
	 * Notification 으로 가져 갈지 , 하위로 내릴지 고민했으나,
	 * 타입 enum 이 실제 메일 전송 테이블과 대응되는 구조이기 때문에 상위에서 가져가는 걸로 결정함.  
	 */
	private NotificationServiceType notificationServiceType;

	private String subject;
	private String content;
	private String mailTo;
	private String mailToAddr;
	private String mailFrom;
	private String mailFromAddr;

	private String seq;
	private String memberSrl;

	/**
	 * 메일 전송을 위한 테이블 이름
	 * @return
	 */
	public String getTableName() {
		return this.getNotificationServiceType().name() + TABLE_POSTFIX;
	}

	/**
	 * 메일 전송을 위한 시퀀스 nextval
	 * @return
	 */
	public String getSequenceNextValName() {
		return this.getNotificationServiceType().name() + SEQ_POSTFIX;
	}

	/**
	 * @return the seq
	 */
	public String getSeq() {
		return seq;
	}

	/**
	 * @param seq the seq to set
	 */
	public void setSeq(String seq) {
		this.seq = seq;
	}

	/**
	 * @return the memberSrl
	 */
	public String getMemberSrl() {
		return StringUtils.isBlank(this.memberSrl) ? StringUtils.EMPTY : this.memberSrl;
	}

	/**
	 * @param memberSrl the memberSrl to set
	 */
	public void setMemberSrl(String memberSrl) {
		this.memberSrl = memberSrl;
	}

	/**
	 * @return the notificationServiceType
	 */
	public NotificationServiceType getNotificationServiceType() {
		return notificationServiceType;
	}

	/**
	 * @param notificationServiceType the notificationServiceType to set
	 */
	public void setNotificationServiceType(NotificationServiceType notificationServiceType) {
		this.notificationServiceType = notificationServiceType;
	}

	/**
	 * @return the mailFrom
	 */
	public String getMailFrom() {
		return StringUtils.isBlank(this.mailFrom) ? COUPANG_EMAIL_NAME : mailFrom;
	}

	/**
	 * @param mailFrom the mailFrom to set
	 */
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	/**
	 * @return the mailFromAddr
	 */
	public String getMailFromAddr() {
		return StringUtils.isBlank(this.mailFromAddr) ? COUPANG_EMAIL : this.mailFromAddr;
	}

	/**
	 * @param mailFromAddr the mailFromAddr to set
	 */
	public void setMailFromAddr(String mailFromAddr) {
		this.mailFromAddr = mailFromAddr;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the mailTo
	 */
	public String getMailTo() {
		return mailTo;
	}

	/**
	 * @param mailTo the mailTo to set
	 */
	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	/**
	 * @return the mailToAddr
	 */
	public String getMailToAddr() {
		return mailToAddr;
	}

	/**
	 * @param mailToAddr the mailToAddr to set
	 */
	public void setMailToAddr(String mailToAddr) {
		this.mailToAddr = mailToAddr;
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	public MailMessage clone() {
		MailMessage ret = new MailMessage();

		return ret;
	}

	/**
	 * @see com.diaimm.april.notification.NotificationMessage#getRepositoryType()
	 */
	@Override
	public Class<? extends NotificationRepository<?>> getRepositoryType() {
		return MailRepository.class;
	}

	/**
	 * @see com.diaimm.april.notification.NotificationMessage#getDbQualifierId()
	 */
	@Override
	public String getDbQualifierId() {
		return "netpion";
	}
}