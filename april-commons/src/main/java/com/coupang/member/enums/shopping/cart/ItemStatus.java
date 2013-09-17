/**
 * 
 */
package com.coupang.member.enums.shopping.cart;

/**
 * 쇼핑 카트 아이템 상태
 * @author 산토리니 윤영욱 (readytogo@coupang.com)
 * @version 2013. 6. 8.
 */
public enum ItemStatus {
	/**	구매할 수 있는 상품	*/
	CHECKEDIN("C"),
	/**	이미 주문한 상품	*/
	ORDER_PLACED("P"),
	/**	사용자가 삭제한 상품	*/
	DELETED("D"),
	/**	판매기간종료, 품절 등으로 인해 살 수 없게 된 상품	*/
	MISSED("M");

	private String code;

	ItemStatus(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}