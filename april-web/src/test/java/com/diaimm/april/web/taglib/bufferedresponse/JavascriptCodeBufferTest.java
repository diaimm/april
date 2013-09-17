/*
 * @fileName : JavascriptCodeBufferTest.java
 * @date : 2013. 5. 23.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.diaimm.april.web.taglib.bufferedresponse;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.el.ELContext;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;

import junit.framework.Assert;

import org.junit.Test;

import com.diaimm.april.web.taglib.bufferedresponse.JavascriptCodeBuffer;
import com.diaimm.april.web.taglib.bufferedresponse.JavascriptCodeType;

/**
 * @author diaimm
 * 
 */
public class JavascriptCodeBufferTest {
	@Test
	public void setTest() {
		PageContext pageContext = new PageContextMock();
		JavascriptCodeBuffer buffer = new JavascriptCodeBuffer(pageContext);
		StringBuffer jsCode = new StringBuffer().append("test");
		buffer.set(JavascriptCodeType.JS_TO_BE_BUFFERED, jsCode);

		Assert.assertEquals("test", buffer.get(JavascriptCodeType.JS_TO_BE_BUFFERED).toString());

		buffer.append(JavascriptCodeType.JS_TO_BE_BUFFERED, "test");
		StringBuffer ret = new StringBuffer();
		buffer.flushTo(ret);
		Assert.assertEquals("testtest\r\n", ret.toString());
	}

	private static class PageContextMock extends PageContext {
		private Map<String, Object> attributeMap = new HashMap<String, Object>();

		/**
		 * @see javax.servlet.jsp.PageContext#forward(java.lang.String)
		 */
		@Override
		public void forward(String arg0) throws ServletException, IOException {
		}

		/**
		 * @see javax.servlet.jsp.PageContext#getException()
		 */
		@Override
		public Exception getException() {
			return null;
		}

		/**
		 * @see javax.servlet.jsp.PageContext#getPage()
		 */
		@Override
		public Object getPage() {
			return null;
		}

		/**
		 * @see javax.servlet.jsp.PageContext#getRequest()
		 */
		@Override
		public ServletRequest getRequest() {

			return null;
		}

		/**
		 * @see javax.servlet.jsp.PageContext#getResponse()
		 */
		@Override
		public ServletResponse getResponse() {

			return null;
		}

		/**
		 * @see javax.servlet.jsp.PageContext#getServletConfig()
		 */
		@Override
		public ServletConfig getServletConfig() {

			return null;
		}

		/**
		 * @see javax.servlet.jsp.PageContext#getServletContext()
		 */
		@Override
		public ServletContext getServletContext() {

			return null;
		}

		/**
		 * @see javax.servlet.jsp.PageContext#getSession()
		 */
		@Override
		public HttpSession getSession() {

			return null;
		}

		/**
		 * @see javax.servlet.jsp.PageContext#handlePageException(java.lang.Exception)
		 */
		@Override
		public void handlePageException(Exception arg0) throws ServletException, IOException {

		}

		/**
		 * @see javax.servlet.jsp.PageContext#handlePageException(java.lang.Throwable)
		 */
		@Override
		public void handlePageException(Throwable arg0) throws ServletException, IOException {

		}

		/**
		 * @see javax.servlet.jsp.PageContext#include(java.lang.String)
		 */
		@Override
		public void include(String arg0) throws ServletException, IOException {

		}

		/**
		 * @see javax.servlet.jsp.PageContext#include(java.lang.String, boolean)
		 */
		@Override
		public void include(String arg0, boolean arg1) throws ServletException, IOException {

		}

		/**
		 * @see javax.servlet.jsp.PageContext#initialize(javax.servlet.Servlet, javax.servlet.ServletRequest, javax.servlet.ServletResponse, java.lang.String, boolean, int, boolean)
		 */
		@Override
		public void initialize(Servlet arg0, ServletRequest arg1, ServletResponse arg2, String arg3, boolean arg4, int arg5, boolean arg6) throws IOException, IllegalStateException, IllegalArgumentException {

		}

		/**
		 * @see javax.servlet.jsp.PageContext#release()
		 */
		@Override
		public void release() {

		}

		/**
		 * @see javax.servlet.jsp.JspContext#findAttribute(java.lang.String)
		 */
		@Override
		public Object findAttribute(String arg0) {

			return null;
		}

		/**
		 * @see javax.servlet.jsp.JspContext#getAttribute(java.lang.String)
		 */
		@Override
		public Object getAttribute(String arg0) {
			return this.attributeMap.get(arg0);
		}

		/**
		 * @see javax.servlet.jsp.JspContext#getAttribute(java.lang.String, int)
		 */
		@Override
		public Object getAttribute(String arg0, int arg1) {
			return this.attributeMap.get(arg0);
		}

		/**
		 * @see javax.servlet.jsp.JspContext#getAttributeNamesInScope(int)
		 */
		@Override
		public Enumeration<String> getAttributeNamesInScope(int arg0) {
			return null;
		}

		/**
		 * @see javax.servlet.jsp.JspContext#getAttributesScope(java.lang.String)
		 */
		@Override
		public int getAttributesScope(String arg0) {

			return 0;
		}

		/**
		 * @see javax.servlet.jsp.JspContext#getELContext()
		 */
		@Override
		public ELContext getELContext() {

			return null;
		}

		/**
		 * @see javax.servlet.jsp.JspContext#getExpressionEvaluator()
		 */
		@Override
		public ExpressionEvaluator getExpressionEvaluator() {

			return null;
		}

		/**
		 * @see javax.servlet.jsp.JspContext#getOut()
		 */
		@Override
		public JspWriter getOut() {

			return null;
		}

		/**
		 * @see javax.servlet.jsp.JspContext#getVariableResolver()
		 */
		@Override
		public VariableResolver getVariableResolver() {
			return null;
		}

		/**
		 * @see javax.servlet.jsp.JspContext#removeAttribute(java.lang.String)
		 */
		@Override
		public void removeAttribute(String arg0) {
			this.attributeMap.remove(arg0);

		}

		/**
		 * @see javax.servlet.jsp.JspContext#removeAttribute(java.lang.String, int)
		 */
		@Override
		public void removeAttribute(String arg0, int arg1) {
			this.attributeMap.remove(arg0);
		}

		/**
		 * @see javax.servlet.jsp.JspContext#setAttribute(java.lang.String, java.lang.Object)
		 */
		@Override
		public void setAttribute(String arg0, Object arg1) {
			this.attributeMap.put(arg0, arg1);
		}

		/**
		 * @see javax.servlet.jsp.JspContext#setAttribute(java.lang.String, java.lang.Object, int)
		 */
		@Override
		public void setAttribute(String arg0, Object arg1, int arg2) {
			this.attributeMap.put(arg0, arg1);
		}

	}
}
