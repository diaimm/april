/*
 * @fileName : DatViewContext.java
 * @date : 2013. 5. 24.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.diaimm.april.web.view.dataview;

/**
 * @author diaimm
 */
interface DataViewContext {
	/**
	 * response의 content type
	 * 
	 * @return
	 */
	String getContentType();

	/**
	 * response body
	 * 
	 * @return
	 */
	String getResponseBody();
}
