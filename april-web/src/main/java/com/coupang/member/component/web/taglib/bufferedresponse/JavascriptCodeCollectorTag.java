/**
 * @fileName : JsCodeCollectorTag.java
 * @date : 2013. 5. 22.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.component.web.taglib.bufferedresponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coupang.member.component.web.util.JavascriptMinifier;

/**
 * 
 * @author diaimm
 */
public class JavascriptCodeCollectorTag extends SimpleTagSupport {
	private Logger logger = LoggerFactory.getLogger(JavascriptCodeCollectorTag.class);
	private JSCodeCollectCommand command = JSCodeCollectCommand.COLLECT;
	private boolean compress = false;

	public void setMode(String mode) {
		this.command = JSCodeCollectCommand.byMode(mode);
	}

	public void setCompress(boolean compress) {
		this.compress = compress;
	}

	@Override
	public void doTag() throws JspException {
		if (this.command != null) {
			this.command.execute(this);
		}
	}

	/**
	 * 현재 tag의 body를 js/html로 분리
	 * 
	 * @author diaimm
	 */
	static class ReadBodyContent {
		private String jsCode;
		private String htmlCode;

		public String getJsCode() {
			return jsCode;
		}

		public String getHtmlCode() {
			return htmlCode;
		}

		ReadBodyContent(String bodyContent) {
			Pattern scriptAreaPattern = Pattern.compile("<(no)?(s|S)(c|C)(r|R)(i|I)(p|P)(t|T)[^>]*>.*?</(no)?(s|S)(c|C)(r|R)(i|I)(p|P)(t|T)>", Pattern.DOTALL);
			Matcher matcher = scriptAreaPattern.matcher(bodyContent);

			StringBuffer jsCode = new StringBuffer();
			while (matcher.find()) {
				jsCode.append(matcher.group());
			}

			this.jsCode = jsCode.toString();
			this.htmlCode = matcher.replaceAll("");
		}
	}

	void appendToBuffer(ReadBodyContent readBodyContent) throws IOException {
		if (readBodyContent != null) {
			getJspContext().getOut().println(readBodyContent.getHtmlCode());
			getJavascriptCodeBuffer().append(JavascriptCodeType.JS_TO_BE_BUFFERED, readBodyContent.getJsCode());
		}
	}

	/**
	 * @return
	 */
	private JavascriptCodeBuffer getJavascriptCodeBuffer() {
		return new JavascriptCodeBuffer((PageContext) getJspContext());
	}

	ReadBodyContent readBodyContent() throws IOException {
		if (getJspBody() != null) {
			StringWriter writer = new StringWriter();
			try {
				getJspBody().invoke(writer);
				return new ReadBodyContent(writer.toString());
			} catch (JspException e) {
				logger.error(e.getMessage(), e);
			}
		}

		return null;
	}

	String clearBuffer() {
		StringBuffer cleared = new StringBuffer();
		getJavascriptCodeBuffer().flushTo(cleared);
		return cleared.toString();
	}

	/**
	 * 검증된 API --> 테스트 생략
	 * 
	 * @return
	 * @throws IOException
	 */
	void writeScript(String script) throws IOException {
		getJspContext().getOut().println(script);
	}

	String doCompress(String script) {
		String compressed = null;

		InputStream inputStream = new ByteArrayInputStream(script.getBytes());
		OutputStream outputStream = new ByteArrayOutputStream();

		JavascriptMinifier jsMin = new JavascriptMinifier(inputStream, outputStream);

		try {
			jsMin.jsmin();
			compressed = outputStream.toString();
		} catch (IOException e) {
			logger.debug(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(outputStream);
		}

		return compressed == null ? script : compressed;
	}

	/**
	 * 
	 * @author 이성준
	 * @version $Rev$, $Date$
	 */
	static enum JSCodeCollectCommand {
		COLLECT {
			private final Logger logger = LoggerFactory.getLogger(JSCodeCollectCommand.class);

			@Override
			void execute(JavascriptCodeCollectorTag caller) {
				try {
					ReadBodyContent readBodyContent = caller.readBodyContent();
					if (readBodyContent == null) {
						return;
					}

					caller.appendToBuffer(readBodyContent);
				} catch (IOException e) {
					logger.debug(e.getMessage(), e);
				}
			}
		},
		FLUSH {
			@Override
			void execute(JavascriptCodeCollectorTag caller) {
				String script = caller.clearBuffer();

				if (StringUtils.isEmpty(script)) {
					return;
				}

				if (caller.compress || StringUtils.equalsIgnoreCase(System.getProperty("js.compress", "false"), "true")) {
					script = caller.doCompress(script);
				}

				try {
					caller.writeScript(script);
				} catch (IOException e) {
					caller.logger.debug(e.getMessage(), e);
				}
			}
		};

		static JSCodeCollectCommand byMode(String name) {
			if (StringUtils.isEmpty(name)) {
				return COLLECT;
			}

			try {
				return valueOf(StringUtils.upperCase(name));
			} catch (IllegalArgumentException e) {
				return COLLECT;
			}
		}

		abstract void execute(JavascriptCodeCollectorTag caller);
	}

	/**
	 * @return the logger
	 */
	Logger getLogger() {
		return logger;
	}

	/**
	 * @return the command
	 */
	JSCodeCollectCommand getCommand() {
		return command;
	}

	/**
	 * @return the compress
	 */
	boolean isCompress() {
		return compress;
	}

}
