/**
 * 
 */
package com.diaimm.april.notification;

/**
 * 이 인터페이스 구현체는 반드시 기본생성자가 있어야 합니다.(접근제어는 private이어도 상관없음)
 * 
 * @author 산토리니 윤영욱 (readytogo@coupang.com), 이성준
 * @version 2013. 6. 19.
 */
public interface NotificationMessage extends Cloneable {
	/**
	 * <pre>
	 * NotificationBuilder에서 prototype으로 사용할 수 있습니다.
	 * clone()은 현재 객체상태와 동일한 새로운 instance를 리턴하도록 구현합니다.
	 * </pre>
	 * 
	 * @return
	 */
	NotificationMessage clone();

	/**
	 * 현재 message type을 처리할 수 있는 repository를 지정합니다.
	 * 
	 * @return
	 */
	public Class<? extends NotificationRepository<?>> getRepositoryType();

	/**
	 * 현재 message type을 처리할 수 있는 DbQualifierId를 지정합니다.
	 * 
	 * @return
	 */
	public String getDbQualifierId();
}