package com.diaimm.april.web.view.dataview;

import javax.servlet.http.HttpServletRequest;

/**
 */
public class DataViewUtil implements DataViewEnvironmentAware {
	public static String generate(HttpServletRequest request, DataType dataType, Object data) {
		request.setAttribute(CONTEXT_KEY, dataType.getViewContenxt(data, data.getClass()));
		return PREFIX + dataType.name();
	}
}
