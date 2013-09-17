/**
 * @fileName : LayoutElement.java
 * @date : 2013. 3. 12.
 * @author : diaimm. Santorini.
 * @desc : 
 */
package com.coupang.member.component.web.view.layout.bean;

/**
 * @author diaimm
 * @author 산토리니 윤영욱 
 * @version 2013.05.02
 */
public interface LayoutElement {
	/**
	 * Element Type : 레이아웃 화면의 요소에 치환하여 넣어준다.
	 * @return
	 */
	String getType();

	/**
	 * 레이아웃 설정 파일에 설정된 config id
	 * @return
	 */
	String getDefinitionName();
}
