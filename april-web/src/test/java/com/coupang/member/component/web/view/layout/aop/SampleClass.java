/*
 * @fileName : SampleClass3.java
 * @date : 2013. 5. 23.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.component.web.view.layout.aop;

import com.coupang.member.component.web.view.layout.annotations.Layout;
import com.coupang.member.component.web.view.layout.annotations.LayoutConfigure;
import com.coupang.member.component.web.view.layout.bean.LayoutConfig;

/**
 * @author diaimm
 * 
 */
public class SampleClass {
	@LayoutConfigure
	public void setupLayout(LayoutConfig config) {

	}

	@Layout(method = "setupLayout")
	public void handlingMethod() {

	}
}
