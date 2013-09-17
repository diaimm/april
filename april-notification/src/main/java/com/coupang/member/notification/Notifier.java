/**
 * 
 */
package com.coupang.member.notification;

/**
 * @author 산토리니 윤영욱 (readytogo@coupang.com), 이성준
 * @version 2013. 6. 19.
 */
public interface Notifier {
	public abstract <T extends NotificationMessage> long notify(T notification);
}
