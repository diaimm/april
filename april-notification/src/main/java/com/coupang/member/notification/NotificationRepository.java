/**
 * 
 */
package com.coupang.member.notification;

/**
 * @author 산토리니 윤영욱 (readytogo@coupang.com), 이성준
 * @version 2013. 6. 18.
 */
public interface NotificationRepository<T extends NotificationMessage> {
	long insert(NotificationMessage notification);
}
