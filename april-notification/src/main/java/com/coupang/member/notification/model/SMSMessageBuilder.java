/**
 * 
 */
package com.coupang.member.notification.model;

import com.coupang.member.notification.NotificationBuilder;
import com.coupang.member.notification.enums.NotificationPurpose;

/**
 * TODO : SMS instance builder로 구성해주세요.
 * 
 * @author 산토리니 윤영욱 (readytogo@coupang.com), 이성준
 * @version 2013. 6. 19.
 */
public class SMSMessageBuilder implements NotificationBuilder<SMSMessage> {
	private SMSMessage prototype = null;

	public SMSMessageBuilder() {
		prototype = new SMSMessage();
	}

	/**
	 * @param message
	 */
	public SMSMessageBuilder withContents(String message) {
		return withContents(message, false);
	}

	/**
	 * @param message
	 */
	public SMSMessageBuilder withContents(String message, boolean hasUrl) {
		prototype.setMessage(message);
		prototype.setContainsUrl(hasUrl);
		return this;
	}

	/**
	 * @param to
	 */
	public SMSMessageBuilder to(String to) {
		prototype.setToNumber(to);
		return this;
	}

	/**
	 * @param from
	 */
	public SMSMessageBuilder from(String from) {
		prototype.setFromNumber(from);
		return this;
	}

	/**
	 * @param sendDate
	 */
	public SMSMessageBuilder withSendDate(String sendDate) {
		prototype.setSendDate(sendDate);
		return this;
	}

	/**
	 * @param sMSServiceId
	 */
	public SMSMessageBuilder withPurpose(NotificationPurpose notificationPurpose) {
		prototype.setNotificationPurpose(notificationPurpose);
		return this;
	}

	/**
	 * @see com.coupang.member.notification.NotificationBuilder#getData()
	 */
	@Override
	public SMSMessage build() {
		return this.prototype.clone();
	}
}