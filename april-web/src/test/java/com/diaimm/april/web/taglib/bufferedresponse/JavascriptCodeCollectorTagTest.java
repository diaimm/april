/*
 * @fileName : JavascriptCodeCollectorTagTest.java
 * @date : 2013. 5. 23.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.web.taglib.bufferedresponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.PageContext;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.diaimm.april.web.taglib.bufferedresponse.JavascriptCodeCollectorTag.JSCodeCollectCommand;
import com.diaimm.april.web.taglib.bufferedresponse.JavascriptCodeCollectorTag.ReadBodyContent;

/**
 * @author diaimm
 * 
 */
public class JavascriptCodeCollectorTagTest {
	private Logger logger = LoggerFactory.getLogger(JavascriptCodeCollectorTagTest.class);
	@Mock
	PageContext pageContext;

	@Test
	public void setModeTest() {
		JavascriptCodeCollectorTag target = new JavascriptCodeCollectorTag();
		Assert.assertEquals(JSCodeCollectCommand.COLLECT, target.getCommand());

		target.setMode("flush");
		Assert.assertEquals(JSCodeCollectCommand.FLUSH, target.getCommand());

		target.setMode("collect");
		Assert.assertEquals(JSCodeCollectCommand.COLLECT, target.getCommand());
	}

	@Test
	public void setCompressTest() {
		JavascriptCodeCollectorTag target = new JavascriptCodeCollectorTag();
		Assert.assertFalse(target.isCompress());

		target.setCompress(true);
		Assert.assertTrue(target.isCompress());

		target.setCompress(false);
		Assert.assertFalse(target.isCompress());

	}

	@Test
	public void normalSetterTest() {
		JavascriptCodeCollectorTag target = new JavascriptCodeCollectorTag();
		target.setCompress(true);
		target.setMode("mode");
	}

	@Test
	public void readBodyContentTest() {
		try {
			ReadBodyContent bodyContents = new ReadBodyContent(getSampleHtmlCode());
			logger.debug(bodyContents.getJsCode());
			logger.debug(bodyContents.getHtmlCode());
		} catch (IOException e) {
			Assert.fail();
		}
	}

	@Test
	public void jsCodeExtractTest() throws IOException {
		String sampleHtmlCode = getSampleHtmlCode();
		// Pattern scriptAreaPattern = Pattern.compile("<(s|S)(c|C)(r|R)(i|I)(p|P)(t|T)[^>]*>*<(/)(s|S)(c|C)(r|R)(i|I)(p|P)(t|T)[^>]*>");
		Pattern scriptAreaPattern = Pattern.compile("<(no)?(s|S)(c|C)(r|R)(i|I)(p|P)(t|T)[^>]*>.*?</(no)?(s|S)(c|C)(r|R)(i|I)(p|P)(t|T)>", Pattern.DOTALL);
		Matcher matcher = scriptAreaPattern.matcher(sampleHtmlCode);

		StringBuffer jsCode = new StringBuffer();
		while (matcher.find()) {
			jsCode.append(matcher.group());
		}

		String htmlCode = matcher.replaceAll("");
		logger.debug(jsCode.toString());
		logger.debug("====================================================================================");
		logger.debug(htmlCode);
	}

	/**
	 * @return
	 * @throws IOException
	 */
	private String getSampleHtmlCode() throws IOException {
		InputStream inputStream = getClassLoader().getResourceAsStream("com/diaimm/april/web/taglib/bufferedresponse/JavascrtipCodeCollectorTagTestSampleCode.txt");
		String scriptCode = IOUtils.toString(inputStream, "UTF-8");
		return scriptCode;
	}

	private ClassLoader getClassLoader() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		if (classLoader == null) {
			classLoader = this.getClass().getClassLoader();
		}

		if (classLoader == null) {
			classLoader = ClassLoader.getSystemClassLoader();
		}

		return classLoader;
	}

	@Test
	public void JSCodeCollectCommandTest() {
		Assert.assertEquals(JSCodeCollectCommand.COLLECT, JSCodeCollectCommand.byMode("tawsetas"));
		Assert.assertEquals(JSCodeCollectCommand.COLLECT, JSCodeCollectCommand.byMode("ColLeCT"));
		Assert.assertEquals(JSCodeCollectCommand.FLUSH, JSCodeCollectCommand.byMode("FlUSh"));
	}
}
