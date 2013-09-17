/*
 * @fileName : Notifier.java
 * @date : 2013. 6. 18.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.notification;

import java.util.Map;

/**
 * @author diaimm
 * 
 */
public class DefaultNotifier implements Notifier {
	private Map<Class<? extends NotificationRepository<? extends NotificationMessage>>, NotificationRepository<? extends NotificationMessage>> repositories;

	DefaultNotifier() {
		// do nothing
	}

	DefaultNotifier(
			Map<Class<? extends NotificationRepository<? extends NotificationMessage>>, NotificationRepository<? extends NotificationMessage>> repositories) {
		this.repositories = repositories;
	}

	/**
	 * notify
	 */
	@Override
	public <T extends NotificationMessage> long notify(T notificationMessage) {
		NotificationRepository<? extends NotificationMessage> notificationRepository = repositories.get(notificationMessage.getRepositoryType());
		return notificationRepository.insert(notificationMessage);
	}
}
