/*
 * @fileName : ByPhaseProvider.java
 * @date : 2013. 8. 9.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.component.web.view.layout.data.provider;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.coupang.member.commons.ByPhase;
import com.coupang.member.component.web.view.layout.data.LayoutDataProvider;

/**
 * @author diaimm
 * 
 */
public class ByPhaseProvider implements LayoutDataProvider<ByPhase> {
	private ByPhase byPhase;

	public ByPhaseProvider(ByPhase byPhase) {
		this.byPhase = byPhase;
	}

	/**
	 * @see com.coupang.member.component.web.view.layout.data.LayoutDataProvider#valueType()
	 */
	@Override
	public Class<? extends ByPhase> valueType() {
		return ByPhase.class;
	}

	/**
	 * @see com.coupang.member.component.web.view.layout.data.LayoutDataProvider#getValue(javax.servlet.http.HttpServletRequest,
	 *      org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public ByPhase getValue(HttpServletRequest request, ModelAndView modelAndView) {
		return byPhase;
	}
}
