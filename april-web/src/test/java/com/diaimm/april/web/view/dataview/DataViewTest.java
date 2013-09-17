/*
 * @fileName : DataViewTest.java
 * @date : 2013. 5. 24.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.diaimm.april.web.view.dataview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author diaimm
 * 
 */
public class DataViewTest {
	@Test
	public void renderMergedOutputModelTest() {
		DataView target = new DataView();
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			target.renderMergedOutputModel(model, new HttpServletRequestMock(), new HttpServletResponseMock());
		} catch (Exception e) {
			Assert.fail();
		}

		ModelAndView modelAndView = new ModelAndView();
		model.put("modelAndView", modelAndView);
		try {
			target.renderMergedOutputModel(model, new HttpServletRequestMock(), new HttpServletResponseMock());
		} catch (Exception e) {
			Assert.fail();
		}

		modelAndView.addObject(DataViewEnvironmentAware.CONTEXT_KEY, new DataViewContext() {
			@Override
			public String getContentType() {
				return "contentType";
			}

			@Override
			public String getResponseBody() {
				return "responseBody";
			}
		});
		try {
			StringWriter writer = new StringWriter();
			final Map<String, Object> callInfo = new HashMap<String, Object>();
			callInfo.put("writer", new PrintWriter(writer));

			HttpServletResponseMock httpServletResponseMock = new HttpServletResponseMock() {
				/**
				 * @see DataViewTest.HttpServletResponseMock#setContentType(java.lang.String)
				 */
				@Override
				public void setContentType(String arg0) {
					callInfo.put("contentType", arg0);
				}

				/**
				 * @see DataViewTest.HttpServletResponseMock#getWriter()
				 */
				@Override
				public PrintWriter getWriter() throws IOException {
					return (PrintWriter) callInfo.get("writer");
				}

			};
			target.renderMergedOutputModel(model, new HttpServletRequestMock(), httpServletResponseMock);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	private static class HttpServletResponseMock implements HttpServletResponse {
		/**
		 * @see javax.servlet.ServletResponse#flushBuffer()
		 */
		@Override
		public void flushBuffer() throws IOException {

		}

		/**
		 * @see javax.servlet.ServletResponse#getBufferSize()
		 */
		@Override
		public int getBufferSize() {

			return 0;
		}

		/**
		 * @see javax.servlet.ServletResponse#getCharacterEncoding()
		 */
		@Override
		public String getCharacterEncoding() {

			return null;
		}

		/**
		 * @see javax.servlet.ServletResponse#getContentType()
		 */
		@Override
		public String getContentType() {

			return null;
		}

		/**
		 * @see javax.servlet.ServletResponse#getLocale()
		 */
		@Override
		public Locale getLocale() {

			return null;
		}

		/**
		 * @see javax.servlet.ServletResponse#getOutputStream()
		 */
		@Override
		public ServletOutputStream getOutputStream() throws IOException {

			return null;
		}

		/**
		 * @see javax.servlet.ServletResponse#getWriter()
		 */
		@Override
		public PrintWriter getWriter() throws IOException {

			return null;
		}

		/**
		 * @see javax.servlet.ServletResponse#isCommitted()
		 */
		@Override
		public boolean isCommitted() {

			return false;
		}

		/**
		 * @see javax.servlet.ServletResponse#reset()
		 */
		@Override
		public void reset() {

		}

		/**
		 * @see javax.servlet.ServletResponse#resetBuffer()
		 */
		@Override
		public void resetBuffer() {

		}

		/**
		 * @see javax.servlet.ServletResponse#setBufferSize(int)
		 */
		@Override
		public void setBufferSize(int arg0) {

		}

		/**
		 * @see javax.servlet.ServletResponse#setCharacterEncoding(java.lang.String)
		 */
		@Override
		public void setCharacterEncoding(String arg0) {

		}

		/**
		 * @see javax.servlet.ServletResponse#setContentLength(int)
		 */
		@Override
		public void setContentLength(int arg0) {

		}

		/**
		 * @see javax.servlet.ServletResponse#setContentType(java.lang.String)
		 */
		@Override
		public void setContentType(String arg0) {

		}

		/**
		 * @see javax.servlet.ServletResponse#setLocale(java.util.Locale)
		 */
		@Override
		public void setLocale(Locale arg0) {

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#addCookie(javax.servlet.http.Cookie)
		 */
		@Override
		public void addCookie(Cookie arg0) {

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#addDateHeader(java.lang.String, long)
		 */
		@Override
		public void addDateHeader(String arg0, long arg1) {

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#addHeader(java.lang.String, java.lang.String)
		 */
		@Override
		public void addHeader(String arg0, String arg1) {

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#addIntHeader(java.lang.String, int)
		 */
		@Override
		public void addIntHeader(String arg0, int arg1) {

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#containsHeader(java.lang.String)
		 */
		@Override
		public boolean containsHeader(String arg0) {

			return false;
		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#encodeRedirectURL(java.lang.String)
		 */
		@Override
		public String encodeRedirectURL(String arg0) {

			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#encodeRedirectUrl(java.lang.String)
		 */
		@Override
		public String encodeRedirectUrl(String arg0) {

			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#encodeURL(java.lang.String)
		 */
		@Override
		public String encodeURL(String arg0) {

			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#encodeUrl(java.lang.String)
		 */
		@Override
		public String encodeUrl(String arg0) {

			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#sendError(int)
		 */
		@Override
		public void sendError(int arg0) throws IOException {

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#sendError(int, java.lang.String)
		 */
		@Override
		public void sendError(int arg0, String arg1) throws IOException {

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#sendRedirect(java.lang.String)
		 */
		@Override
		public void sendRedirect(String arg0) throws IOException {

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#setDateHeader(java.lang.String, long)
		 */
		@Override
		public void setDateHeader(String arg0, long arg1) {

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#setHeader(java.lang.String, java.lang.String)
		 */
		@Override
		public void setHeader(String arg0, String arg1) {

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#setIntHeader(java.lang.String, int)
		 */
		@Override
		public void setIntHeader(String arg0, int arg1) {

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#setStatus(int)
		 */
		@Override
		public void setStatus(int arg0) {

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#setStatus(int, java.lang.String)
		 */
		@Override
		public void setStatus(int arg0, String arg1) {

		}

	}

	private static class HttpServletRequestMock implements HttpServletRequest {
		/**
		 * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
		 */
		@Override
		public Object getAttribute(String arg0) {

			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getAttributeNames()
		 */
		@Override
		public Enumeration getAttributeNames() {

			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getCharacterEncoding()
		 */
		@Override
		public String getCharacterEncoding() {

			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getContentLength()
		 */
		@Override
		public int getContentLength() {

			return 0;
		}

		/**
		 * @see javax.servlet.ServletRequest#getContentType()
		 */
		@Override
		public String getContentType() {

			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getInputStream()
		 */
		@Override
		public ServletInputStream getInputStream() throws IOException {

			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getLocalAddr()
		 */
		@Override
		public String getLocalAddr() {

			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getLocalName()
		 */
		@Override
		public String getLocalName() {

			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getLocalPort()
		 */
		@Override
		public int getLocalPort() {

			return 0;
		}

		/**
		 * @see javax.servlet.ServletRequest#getLocale()
		 */
		@Override
		public Locale getLocale() {

			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getLocales()
		 */
		@Override
		public Enumeration getLocales() {

			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
		 */
		@Override
		public String getParameter(String arg0) {

			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getParameterMap()
		 */
		@Override
		public Map getParameterMap() {

			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getParameterNames()
		 */
		@Override
		public Enumeration getParameterNames() {

			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
		 */
		@Override
		public String[] getParameterValues(String arg0) {

			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getProtocol()
		 */
		@Override
		public String getProtocol() {

			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getReader()
		 */
		@Override
		public BufferedReader getReader() throws IOException {

			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getRealPath(java.lang.String)
		 */
		@Override
		public String getRealPath(String arg0) {

			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getRemoteAddr()
		 */
		@Override
		public String getRemoteAddr() {

			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getRemoteHost()
		 */
		@Override
		public String getRemoteHost() {

			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getRemotePort()
		 */
		@Override
		public int getRemotePort() {

			return 0;
		}

		/**
		 * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
		 */
		@Override
		public RequestDispatcher getRequestDispatcher(String arg0) {

			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getScheme()
		 */
		@Override
		public String getScheme() {

			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getServerName()
		 */
		@Override
		public String getServerName() {

			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getServerPort()
		 */
		@Override
		public int getServerPort() {

			return 0;
		}

		/**
		 * @see javax.servlet.ServletRequest#isSecure()
		 */
		@Override
		public boolean isSecure() {

			return false;
		}

		/**
		 * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
		 */
		@Override
		public void removeAttribute(String arg0) {

		}

		/**
		 * @see javax.servlet.ServletRequest#setAttribute(java.lang.String, java.lang.Object)
		 */
		@Override
		public void setAttribute(String arg0, Object arg1) {

		}

		/**
		 * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
		 */
		@Override
		public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {

		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getAuthType()
		 */
		@Override
		public String getAuthType() {

			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getContextPath()
		 */
		@Override
		public String getContextPath() {

			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getCookies()
		 */
		@Override
		public Cookie[] getCookies() {

			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
		 */
		@Override
		public long getDateHeader(String arg0) {

			return 0;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
		 */
		@Override
		public String getHeader(String arg0) {

			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
		 */
		@Override
		public Enumeration getHeaderNames() {

			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
		 */
		@Override
		public Enumeration getHeaders(String arg0) {

			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
		 */
		@Override
		public int getIntHeader(String arg0) {

			return 0;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getMethod()
		 */
		@Override
		public String getMethod() {

			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getPathInfo()
		 */
		@Override
		public String getPathInfo() {

			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
		 */
		@Override
		public String getPathTranslated() {

			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getQueryString()
		 */
		@Override
		public String getQueryString() {

			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
		 */
		@Override
		public String getRemoteUser() {

			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getRequestURI()
		 */
		@Override
		public String getRequestURI() {

			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getRequestURL()
		 */
		@Override
		public StringBuffer getRequestURL() {

			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
		 */
		@Override
		public String getRequestedSessionId() {

			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getServletPath()
		 */
		@Override
		public String getServletPath() {

			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getSession()
		 */
		@Override
		public HttpSession getSession() {

			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
		 */
		@Override
		public HttpSession getSession(boolean arg0) {

			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
		 */
		@Override
		public Principal getUserPrincipal() {

			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
		 */
		@Override
		public boolean isRequestedSessionIdFromCookie() {

			return false;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
		 */
		@Override
		public boolean isRequestedSessionIdFromURL() {

			return false;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
		 */
		@Override
		public boolean isRequestedSessionIdFromUrl() {

			return false;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
		 */
		@Override
		public boolean isRequestedSessionIdValid() {

			return false;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
		 */
		@Override
		public boolean isUserInRole(String arg0) {

			return false;
		}

	}
}
