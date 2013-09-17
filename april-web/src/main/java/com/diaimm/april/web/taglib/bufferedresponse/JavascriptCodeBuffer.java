/**
 * @fileName : ResponseBuffer.java
 * @date : 2013. 5. 22.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.diaimm.april.web.taglib.bufferedresponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author diaimm
 */
final class JavascriptCodeBuffer {
	// html tag 는 '<영문자' or '</영문자' or '<!' 으로 시작하고 '>' 로 끝나는 모든 것
	static final Pattern SCRIPT_PATTERN = Pattern.compile("<(/?)(s|S)(c|C)(r|R)(i|I)(p|P)(t|T)[^>]*>|<!--|-->");
	private PageContext pageContext;

	JavascriptCodeBuffer(PageContext pageContext) {
		this.pageContext = pageContext;
	}

	/**
	 * <pre>
	 * clear시 반환용 buffer에 담는 순서가 중요합니다.
	 * 
	 * 값이 없더라도 null이 리턴되지는 않습니다. 비어있는 StringBuffer가 리턴됩니다.
	 * </pre>
	 * 
	 * @param pageContext
	 * @return
	 */
	void flushTo(StringBuffer ret) {
		for (JavascriptCodeType codeType : JavascriptCodeType.sortedValues()) {
			ret.append(get(codeType));
		}

		clear();
	}

	/**
	 * 패키지 외부에서는 추가만 할 수 있으면 된다.
	 * 
	 * @param scriptCode
	 */
	void append(JavascriptCodeType codeType, String scriptCode) {
		if (StringUtils.isNotEmpty(scriptCode)) {
			Matcher matcher = SCRIPT_PATTERN.matcher(scriptCode);
			scriptCode = matcher.replaceAll("");
		}

		get(codeType).append(scriptCode).append("\r\n");
	}

	StringBuffer get(JavascriptCodeType codeType) {
		StringBuffer ret = (StringBuffer) pageContext.getAttribute(codeType.getContextKey(), PageContext.REQUEST_SCOPE);
		if (ret == null) {
			ret = new StringBuffer();
			set(codeType, ret);
		}

		return ret;
	}

	void set(JavascriptCodeType codeType, StringBuffer ret) {
		pageContext.setAttribute(codeType.getContextKey(), ret, PageContext.REQUEST_SCOPE);
	}

	/**
	 * 버퍼 내용 유무에 상관없이 버퍼를 새로 생성합니다.
	 * 
	 * @param pageContext
	 */
	void clear() {
		for (JavascriptCodeType codeType : JavascriptCodeType.sortedValues()) {
			set(codeType, null);
		}
	}
}
