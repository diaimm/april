package com.diaimm.april.web.view.dataview;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: Coupang
 * Date: 13. 9. 12
 * Time: 오후 2:44
 * To change this template use File | Settings | File Templates.
 */
public class DataViewUtil implements DataViewEnvironmentAware{
	public static String generate(HttpServletRequest request, DataType dataType, Object data){
		request.setAttribute(CONTEXT_KEY, dataType.getViewContenxt(data, data.getClass()));
		return PREFIX + dataType.name();
	}
}
