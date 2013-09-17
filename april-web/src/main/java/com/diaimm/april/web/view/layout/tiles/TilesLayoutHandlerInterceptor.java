/**
 * @fileName : LayoutAdvice.java
 * @date : 2013. 3. 12.
 * @author : diaimm. Santorini.
 * @desc : 
 */
package com.diaimm.april.web.view.layout.tiles;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.diaimm.april.web.view.layout.aop.AbstractLayoutHandlerInterceptor;
import com.diaimm.april.web.view.layout.bean.LayoutConfig;

/**
 * @author diaimm
 * 
 */
public class TilesLayoutHandlerInterceptor extends AbstractLayoutHandlerInterceptor {
	@Override
	public void handoverLayoutConfigName(HttpServletRequest request, ModelAndView modelAndView, String layoutName) {
		modelAndView.setViewName(LayoutViewResovler.TILES_MARKER_PREFIX + layoutName);
	}

	/**
	 * @see com.diaimm.april.web.view.layout.aop.AbstractLayoutHandlerInterceptor#setContentsBodyView(javax.servlet.http.HttpServletRequest,
	 *      org.springframework.web.servlet.ModelAndView, com.diaimm.april.web.view.layout.bean.LayoutConfig)
	 */
	@Override
	public void setContentsBodyView(HttpServletRequest request, ModelAndView modelAndView, LayoutConfig layoutConfig) {
		request.setAttribute(LayoutConfig.LAYOUT_CONTENTS_KEY, getViewNameSelector().getViewName(request, modelAndView));
	}
}
