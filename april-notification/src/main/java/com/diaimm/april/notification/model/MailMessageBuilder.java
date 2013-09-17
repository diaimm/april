/**
 * 
 */
package com.diaimm.april.notification.model;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import com.diaimm.april.commons.Env;
import com.diaimm.april.notification.NotificationBuilder;
import com.diaimm.april.notification.enums.NotificationServiceType;

import com.diaimm.april.notification.NotificationBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author 산토리니 윤영욱 (readytogo@coupang.com), 이성준
 * @version 2013. 6. 19.
 */
public class MailMessageBuilder implements NotificationBuilder<MailMessage> {
	private MailMessage prototype;

	public MailMessageBuilder(NotificationServiceType notificationServiceType) {
		prototype = new MailMessage();
		prototype.setNotificationServiceType(notificationServiceType);
	}

	/**
	 * 프리마커를 통한 메일 템플릿 사용
	 * 
	 * @param configuration
	 * @param templeteFileName
	 * @param attributes
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public MailMessageBuilder contentByFreemarker(Configuration configuration, String templeteFileName, Map<String, Object> attributes)
			throws IOException, TemplateException {
		Writer writer = new StringWriter();

		Template template = configuration.getTemplate(templeteFileName, Env.DEFAULT_ENCODING);
		template.setEncoding(Env.DEFAULT_ENCODING);
		template.process(attributes, writer);

		prototype.setContent(writer.toString());
		return this;
	}

	/**
	 * @param content
	 * @return
	 */
	public MailMessageBuilder content(String content) {
		prototype.setContent(content);
		return this;
	}

	/**
	 * @param subject
	 * @return
	 */
	public MailMessageBuilder subject(String subject) {
		prototype.setSubject(subject);
		return this;
	}

	/**
	 * @param toAddress
	 * @return
	 */
	public MailMessageBuilder toAddress(String toAddress) {
		prototype.setMailToAddr(toAddress);
		return this;
	}

	/**
	 * @param to
	 * @return
	 */
	public MailMessageBuilder to(String to) {
		prototype.setMailTo(to);
		return this;
	}

	/**
	 * @see com.diaimm.april.notification.NotificationBuilder#getData()
	 */
	@Override
	public MailMessage build() {
		return this.prototype;
	}
}
