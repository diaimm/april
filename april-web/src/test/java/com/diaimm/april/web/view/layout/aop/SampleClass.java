/*
 * @fileName : SampleClass3.java
 * @date : 2013. 5. 23.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.web.view.layout.aop;

import com.diaimm.april.web.view.layout.annotations.Layout;
import com.diaimm.april.web.view.layout.annotations.LayoutConfigure;
import com.diaimm.april.web.view.layout.bean.LayoutConfig;

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
