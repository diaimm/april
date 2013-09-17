/**
 * @fileName : ResponseBufferType.java
 * @date : 2013. 5. 22.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.component.web.taglib.bufferedresponse;

import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author diaimm
 */
enum JavascriptCodeType {
	BIGPIPE(0), JS_TO_BE_BUFFERED(1);

	private final int order;

	JavascriptCodeType(int order) {
		this.order = order;
	}

	/**
	 * @return the order
	 */
	int getOrder() {
		return order;
	}

	String getContextKey() {
		return JavascriptCodeBuffer.class.getCanonicalName() + "_" + name();
	}

	static JavascriptCodeType[] sortedValues() {
		JavascriptCodeType[] ret = JavascriptCodeType.values();
		Arrays.sort(ret, new Comparator<JavascriptCodeType>() {
			@Override
			public int compare(JavascriptCodeType o1, JavascriptCodeType o2) {
				return o1.getOrder() - o2.getOrder();
			}
		});

		return ret;
	}
}
