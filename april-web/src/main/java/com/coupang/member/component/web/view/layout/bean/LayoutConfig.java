/**
 * @fileName : LayoutConfig.java
 * @date : 2013. 3. 12.
 * @author : diaimm. Santorini.
 * @desc : 
 */
package com.coupang.member.component.web.view.layout.bean;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.coupang.member.component.web.view.layout.data.LayoutDataProvider;

/**
 * <pre>
 * 	!!!!!! 구현 및 설정 주의 사항 !!!!!!
 * LayoutConfig의 구현체는 bean으로 등록하되, 반드시 scope 설정을 통해 prototype으로 등록되어야 합니다.
 * </pre>
 * 
 * @author diaimm
 */
public interface LayoutConfig {
	public static final String LAYOUT_CONTENTS_KEY = "__contents__";
	public static final String LAYOUT_CONFIG = "__LAYOUT_CONFIG__";

	/**
	 * 필요한 초기화가 있다면 진행합니다.
	 */
	void initialize();

	/**
	 * 해당 타입의 layout 요소를 반환합니다.
	 * 
	 * @param layoutType
	 * @return
	 */
	LayoutElement getElement(String layoutType);

	/**
	 * 해당 타입의 layout 요소가 존재하는지 확인합니다.
	 * 
	 * @param layoutType
	 * @return
	 */
	boolean hasElement(String layoutType);

	/**
	 * layout 요소를 추가합니다. (set할지 add 할지는 필요에 따라서 결정합니다.)
	 * 
	 * @param layoutElement
	 */
	void addLayoutElement(LayoutElement layoutElement);

	/**
	 * 해당 타입의 layout 요소를 반환합니다.
	 * 
	 * @param layoutType
	 * @return
	 */
	<T extends LayoutElement> T getElement(Class<T> layoutType);

	/**
	 * 해당 타입의 layout 요소를 제거합니다.
	 * 
	 * @param layoutType
	 * @return
	 */
	<T extends LayoutElement> T removeElement(Class<T> layoutType);

	void setHttpServletRequest(HttpServletRequest request);

	void setModelAndView(ModelAndView modelAndView);

	/**
	 * layout에서 필요한 데이터 provider를 추가합니다.
	 * 
	 * @param providers
	 */
	void addLayoutDataProvider(LayoutDataProvider<?>... providers);
}
