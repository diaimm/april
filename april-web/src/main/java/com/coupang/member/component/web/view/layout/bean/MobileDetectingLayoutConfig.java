/*
 * @fileName : MobileDetectionLayoutConfig.java
 * @date : 2013. 7. 3.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.component.web.view.layout.bean;

/**
 * @author diaimm
 * 
 */
public interface MobileDetectingLayoutConfig extends LayoutConfig {
	void setMobileView(boolean isMobile);

	boolean isMobileView();
}