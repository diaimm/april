/*
 * @fileName : LayoutHandlerInterceptor.java
 * @date : 2013. 5. 23.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.web.view.layout.aop;

import com.diaimm.april.web.view.layout.annotations.Layout.DefaultValues;
import com.diaimm.april.web.view.layout.aop.LayoutHolder.LayoutInfo;
import com.diaimm.april.web.view.layout.bean.LayoutConfig;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.*;

/**
 * @author diaimm
 * 
 */
@Ignore
public class LayoutHandlerInterceptorTest {
	@Test
	public void preHandleTest() {
		AbstractLayoutHandlerInterceptor target = new AbstractLayoutHandlerInterceptor() {
			@Override
			public void handoverLayoutConfigName(HttpServletRequest request, ModelAndView modelAndView, String layoutName) {

			}

			@Override
			public void setContentsBodyView(HttpServletRequest request, ModelAndView modelAndView, LayoutConfig layoutConfig) {
			}
		};
		HttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = new MockHttpServletResponse();
		try {
			Assert.assertTrue(target.preHandle(request, response, null));

			LayoutHolder layoutHolder = new LayoutHolder();
			Assert.assertNull(layoutHolder.layout());
			Assert.assertNull(layoutHolder.method());
			Assert.assertNull(layoutHolder.layoutConfig());
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void generateKeyTest() {
		AbstractLayoutHandlerInterceptor target = new AbstractLayoutHandlerInterceptor() {
			@Override
			public void handoverLayoutConfigName(HttpServletRequest request, ModelAndView modelAndView, String layoutName) {

			}

			@Override
			public void setContentsBodyView(HttpServletRequest request, ModelAndView modelAndView, LayoutConfig layoutConfig) {
			}
		};
		try {
			HandlerMethod handlerMethod = new HandlerMethod(this, "generateKeyTest");
			Assert.assertEquals("com.diaimm.april.web.view.layout.aop.LayoutHandlerInterceptorTest:generateKeyTest",
				target.generateKey(handlerMethod.getBeanType(), "generateKeyTest"));
		} catch (NoSuchMethodException e) {
			Assert.fail();
		}
	}

	@Test
	public void getLayoutConfigureMethodTest() {
		AbstractLayoutHandlerInterceptor target = new AbstractLayoutHandlerInterceptor() {

			@Override
			public void handoverLayoutConfigName(HttpServletRequest request, ModelAndView modelAndView, String layoutName) {
				// TODO Auto-generated method stub

			}

			@Override
			public void setContentsBodyView(HttpServletRequest request, ModelAndView modelAndView, LayoutConfig layoutConfig) {
				// TODO Auto-generated method stub

			}
		};
		try {
			HandlerMethod handlerMethod = new HandlerMethod(new SampleClass(), "handlingMethod");
			Object layoutConfigureMethod1 = target.getLayoutConfigureMethod(handlerMethod.getBeanType(), "setupLayout");
			Object layoutConfigureMethod2 = target.getLayoutConfigureMethod(handlerMethod.getBeanType(), "setupLayout");

			Assert.assertEquals(layoutConfigureMethod1, layoutConfigureMethod2);
		} catch (NoSuchMethodException e) {
		}
	}

	@Test
	public void getLayoutConfigureMethodsTest() {
		AbstractLayoutHandlerInterceptor target = new AbstractLayoutHandlerInterceptor() {
			@Override
			public void handoverLayoutConfigName(HttpServletRequest request, ModelAndView modelAndView, String layoutName) {
			}

			@Override
			public void setContentsBodyView(HttpServletRequest request, ModelAndView modelAndView, LayoutConfig layoutConfig) {
			}
		};
		Set<Method> layoutConfigureMethods = target.getLayoutConfigureMethods(SampleClass.class);
		Assert.assertEquals(1, layoutConfigureMethods.size());
	}

	@Test
	public void getLayoutConfigureMethodInstanceTest() {
		AbstractLayoutHandlerInterceptor target = new AbstractLayoutHandlerInterceptor() {
			@Override
			public void handoverLayoutConfigName(HttpServletRequest request, ModelAndView modelAndView, String layoutName) {
			}

			@Override
			public void setContentsBodyView(HttpServletRequest request, ModelAndView modelAndView, LayoutConfig layoutConfig) {
			}
		};
		try {
			// layout configure method가 1개
			HandlerMethod handlerMethod = new HandlerMethod(new SampleClass(), "handlingMethod");
			Method method1 = target.getLayoutConfigureMethodInstance(handlerMethod.getBeanType(), "setupLayout");
			Assert.assertEquals("setupLayout", method1.getName());

			Method method2 = target.getLayoutConfigureMethodInstance(handlerMethod.getBeanType(), "setupLayout2");
			Assert.assertNull(method2);

			Method method3 = target.getLayoutConfigureMethodInstance(handlerMethod.getBeanType(), "");
			Assert.assertEquals("setupLayout", method3.getName());

			// layout configure method가 2개 이상
			HandlerMethod handlerMethod2 = new HandlerMethod(new SampleClass2(), "handlingMethod");
			Method method21 = target.getLayoutConfigureMethodInstance(handlerMethod2.getBeanType(), "setupLayout");
			Assert.assertEquals("setupLayout", method21.getName());

			Method method22 = target.getLayoutConfigureMethodInstance(handlerMethod2.getBeanType(), "setupLayout2");
			Assert.assertNull(method22);

			Method method23 = target.getLayoutConfigureMethodInstance(handlerMethod2.getBeanType(), "");
			Assert.assertEquals("setupLayout", method23.getName());

			// layout configure method가 0개 이상
			HandlerMethod handlerMethod3 = new HandlerMethod(new SampleClass3(), "handlingMethod");
			Method method31 = target.getLayoutConfigureMethodInstance(handlerMethod3.getBeanType(), "setupLayout");
			Assert.assertNull(method31);

			Method method32 = target.getLayoutConfigureMethodInstance(handlerMethod3.getBeanType(), "setupLayout2");
			Assert.assertNull(method32);

			Method method33 = target.getLayoutConfigureMethodInstance(handlerMethod3.getBeanType(), "");
			Assert.assertNull(method33);
		} catch (NoSuchMethodException e) {
			Assert.fail();
		}
	}

	@Test
	public void postHandleTest() {
		final Set<String> called = new HashSet<String>();
		AbstractLayoutHandlerInterceptor target = new AbstractLayoutHandlerInterceptor() {
			@Override
			LayoutConfig getInitializedLayoutConfig(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
				called.add("getInitializedLayoutConfig");
				return null;
			}

			@Override
			boolean isAvoidLayoutUsePrefix(ModelAndView modelAndView) {
				called.add("isAvoidLayoutUsePrefix");
				return false;
			}

			@Override
			public void handoverLayoutConfigName(HttpServletRequest request, ModelAndView modelAndView, String layoutName) {
				called.add("handoverLayoutConfigName");

			}

			@Override
			public void setContentsBodyView(HttpServletRequest request, ModelAndView modelAndView, LayoutConfig layoutConfig) {
				called.add("setContentsBodyView");
			}

			@Override
			String processPresetLayoutConfigurerMethod(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView,
				LayoutConfig layoutConfig) throws Exception {
				called.add("processPresetLayoutConfigurerMethod");
				return "";
			}

			@Override
			String processControllerLayoutConfigurerMethod(HttpServletRequest request, HttpServletResponse response, Object bean,
				ModelAndView modelAndView, LayoutConfig layoutConfig) throws Exception {
				called.add("processControllerLayoutConfigurerMethod");
				return "";
			}
		};

		try {
			called.clear();
			target.postHandle(null, null, null, null);
			Assert.assertFalse(called.contains("isAvoidLayoutUsePrefix"));
			Assert.assertFalse(called.contains("getInitializedLayoutConfig"));
			Assert.assertFalse(called.contains("processPreset"));
			Assert.assertFalse(called.contains("invokeLayoutConfigurerMethod"));
			Assert.assertFalse(called.contains("handoverLayoutAndViewInfo"));
		} catch (Exception e) {
			Assert.fail();
		}

		try {
			called.clear();

			LayoutHolder layoutHolder = new LayoutHolder();
			layoutHolder.init(new LayoutInfo("test", null, null, null));
			target.postHandle(null, null, null, null);
			Assert.assertFalse(called.contains("isAvoidLayoutUsePrefix"));
			Assert.assertFalse(called.contains("getInitializedLayoutConfig"));
			Assert.assertFalse(called.contains("processPreset"));
			Assert.assertFalse(called.contains("invokeLayoutConfigurerMethod"));
			Assert.assertFalse(called.contains("handoverLayoutAndViewInfo"));
		} catch (Exception e) {
			Assert.fail();
		}

		try {
			called.clear();

			LayoutHolder layoutHolder = new LayoutHolder();
			layoutHolder.init(new LayoutInfo("test", "test", null, null));
			target.postHandle(null, null, null, null);
			Assert.assertTrue(called.contains("isAvoidLayoutUsePrefix"));
			Assert.assertFalse(called.contains("getInitializedLayoutConfig"));
			Assert.assertFalse(called.contains("processPreset"));
			Assert.assertFalse(called.contains("invokeLayoutConfigurerMethod"));
			Assert.assertFalse(called.contains("handoverLayoutAndViewInfo"));
		} catch (Exception e) {
			Assert.fail();
		}

		try {
			called.clear();

			LayoutHolder layoutHolder = new LayoutHolder();
			layoutHolder.init(new LayoutInfo("test", "test", null, null));
			HandlerMethod handlerMethod = new HandlerMethod(new SampleClass(), "handlingMethod");
			target.postHandle(null, null, handlerMethod, null);
			Assert.assertTrue(called.contains("isAvoidLayoutUsePrefix"));
			Assert.assertTrue(called.contains("getInitializedLayoutConfig"));
			Assert.assertTrue(called.contains("handoverLayoutConfigName"));
			Assert.assertTrue(called.contains("setContentsBodyView"));
			Assert.assertTrue(called.contains("processPresetLayoutConfigurerMethod"));
			Assert.assertTrue(called.contains("processControllerLayoutConfigurerMethod"));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void isAvoidLayoutUsePrefixTest() {
		ModelAndView modelAndView = new ModelAndView();
		AbstractLayoutHandlerInterceptor target = new AbstractLayoutHandlerInterceptor() {
			@Override
			public void handoverLayoutConfigName(HttpServletRequest request, ModelAndView modelAndView, String layoutName) {

			}

			@Override
			public void setContentsBodyView(HttpServletRequest request, ModelAndView modelAndView, LayoutConfig layoutConfig) {

			}
		};

		Assert.assertFalse(target.isAvoidLayoutUsePrefix(modelAndView));

		modelAndView.setViewName(UrlBasedViewResolver.REDIRECT_URL_PREFIX + "test");
		Assert.assertTrue(target.isAvoidLayoutUsePrefix(modelAndView));

		modelAndView.setViewName(UrlBasedViewResolver.FORWARD_URL_PREFIX + "test");
		Assert.assertTrue(target.isAvoidLayoutUsePrefix(modelAndView));

		modelAndView.setViewName("asefasefjaslfjesfej;slaf");
		Assert.assertFalse(target.isAvoidLayoutUsePrefix(modelAndView));
	}

	@Test
	public void getLayoutNameTest() {
		AbstractLayoutHandlerInterceptor target = new AbstractLayoutHandlerInterceptor() {
			@Override
			public void handoverLayoutConfigName(HttpServletRequest request, ModelAndView modelAndView, String layoutName) {
			}

			@Override
			public void setContentsBodyView(HttpServletRequest request, ModelAndView modelAndView, LayoutConfig layoutConfig) {
			}
		};

		try {
			Method method = SampleClass.class.getMethod("setupLayout", LayoutConfig.class);
			String layoutName = target.getLayoutConfigName(method);
			Assert.assertEquals(DefaultValues.NO_LAYOUT, layoutName);
		} catch (Exception e) {
			Assert.fail();
		}

		try {
			Method method = SampleClass2.class.getMethod("setupLayout1", LayoutConfig.class);
			String layoutName = target.getLayoutConfigName(method);
			Assert.assertEquals("default1", layoutName);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	private static class MockHttpServletResponse implements HttpServletResponse {
		/**
		 * @see javax.servlet.ServletResponse#flushBuffer()
		 */
		@Override
		public void flushBuffer() throws IOException {
			// TODO Auto-generated method stub

		}

		/**
		 * @see javax.servlet.ServletResponse#getBufferSize()
		 */
		@Override
		public int getBufferSize() {
			// TODO Auto-generated method stub
			return 0;
		}

		/**
		 * @see javax.servlet.ServletResponse#setBufferSize(int)
		 */
		@Override
		public void setBufferSize(int arg0) {
			// TODO Auto-generated method stub

		}

		/**
		 * @see javax.servlet.ServletResponse#getCharacterEncoding()
		 */
		@Override
		public String getCharacterEncoding() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletResponse#setCharacterEncoding(java.lang.String)
		 */
		@Override
		public void setCharacterEncoding(String arg0) {
			// TODO Auto-generated method stub

		}

		/**
		 * @see javax.servlet.ServletResponse#getContentType()
		 */
		@Override
		public String getContentType() {
			return null;
		}

		/**
		 * @see javax.servlet.ServletResponse#setContentType(java.lang.String)
		 */
		@Override
		public void setContentType(String arg0) {
			// TODO Auto-generated method stub

		}

		/**
		 * @see javax.servlet.ServletResponse#getLocale()
		 */
		@Override
		public Locale getLocale() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletResponse#setLocale(java.util.Locale)
		 */
		@Override
		public void setLocale(Locale arg0) {
			// TODO Auto-generated method stub

		}

		/**
		 * @see javax.servlet.ServletResponse#getOutputStream()
		 */
		@Override
		public ServletOutputStream getOutputStream() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletResponse#getWriter()
		 */
		@Override
		public PrintWriter getWriter() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletResponse#isCommitted()
		 */
		@Override
		public boolean isCommitted() {
			// TODO Auto-generated method stub
			return false;
		}

		/**
		 * @see javax.servlet.ServletResponse#reset()
		 */
		@Override
		public void reset() {
			// TODO Auto-generated method stub

		}

		/**
		 * @see javax.servlet.ServletResponse#resetBuffer()
		 */
		@Override
		public void resetBuffer() {
			// TODO Auto-generated method stub

		}

		/**
		 * @see javax.servlet.ServletResponse#setContentLength(int)
		 */
		@Override
		public void setContentLength(int arg0) {
			// TODO Auto-generated method stub

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#addCookie(javax.servlet.http.Cookie)
		 */
		@Override
		public void addCookie(Cookie arg0) {
			// TODO Auto-generated method stub

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#addDateHeader(java.lang.String, long)
		 */
		@Override
		public void addDateHeader(String arg0, long arg1) {
			// TODO Auto-generated method stub

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#addHeader(java.lang.String, java.lang.String)
		 */
		@Override
		public void addHeader(String arg0, String arg1) {
			// TODO Auto-generated method stub

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#addIntHeader(java.lang.String, int)
		 */
		@Override
		public void addIntHeader(String arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#containsHeader(java.lang.String)
		 */
		@Override
		public boolean containsHeader(String arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#encodeRedirectURL(java.lang.String)
		 */
		@Override
		public String encodeRedirectURL(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#encodeRedirectUrl(java.lang.String)
		 */
		@Override
		public String encodeRedirectUrl(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#encodeURL(java.lang.String)
		 */
		@Override
		public String encodeURL(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#encodeUrl(java.lang.String)
		 */
		@Override
		public String encodeUrl(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#sendError(int)
		 */
		@Override
		public void sendError(int arg0) throws IOException {
			// TODO Auto-generated method stub

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#sendError(int, java.lang.String)
		 */
		@Override
		public void sendError(int arg0, String arg1) throws IOException {
			// TODO Auto-generated method stub

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#sendRedirect(java.lang.String)
		 */
		@Override
		public void sendRedirect(String arg0) throws IOException {
			// TODO Auto-generated method stub

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#setDateHeader(java.lang.String, long)
		 */
		@Override
		public void setDateHeader(String arg0, long arg1) {
			// TODO Auto-generated method stub

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#setHeader(java.lang.String, java.lang.String)
		 */
		@Override
		public void setHeader(String arg0, String arg1) {
			// TODO Auto-generated method stub

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#setIntHeader(java.lang.String, int)
		 */
		@Override
		public void setIntHeader(String arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#setStatus(int)
		 */
		@Override
		public void setStatus(int arg0) {
			// TODO Auto-generated method stub

		}

		/**
		 * @see javax.servlet.http.HttpServletResponse#setStatus(int, java.lang.String)
		 */
		@Override
		public void setStatus(int arg0, String arg1) {
			// TODO Auto-generated method stub

		}

	}

	private static class MockHttpServletRequest implements HttpServletRequest {
		/**
		 * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
		 */
		@Override
		public Object getAttribute(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getAttributeNames()
		 */
		@Override
		public Enumeration getAttributeNames() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getCharacterEncoding()
		 */
		@Override
		public String getCharacterEncoding() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
		 */
		@Override
		public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
			// TODO Auto-generated method stub

		}

		/**
		 * @see javax.servlet.ServletRequest#getContentLength()
		 */
		@Override
		public int getContentLength() {
			// TODO Auto-generated method stub
			return 0;
		}

		/**
		 * @see javax.servlet.ServletRequest#getContentType()
		 */
		@Override
		public String getContentType() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getInputStream()
		 */
		@Override
		public ServletInputStream getInputStream() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getLocalAddr()
		 */
		@Override
		public String getLocalAddr() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getLocalName()
		 */
		@Override
		public String getLocalName() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getLocalPort()
		 */
		@Override
		public int getLocalPort() {
			// TODO Auto-generated method stub
			return 0;
		}

		/**
		 * @see javax.servlet.ServletRequest#getLocale()
		 */
		@Override
		public Locale getLocale() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getLocales()
		 */
		@Override
		public Enumeration getLocales() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
		 */
		@Override
		public String getParameter(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getParameterMap()
		 */
		@Override
		public Map getParameterMap() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getParameterNames()
		 */
		@Override
		public Enumeration getParameterNames() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
		 */
		@Override
		public String[] getParameterValues(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getProtocol()
		 */
		@Override
		public String getProtocol() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getReader()
		 */
		@Override
		public BufferedReader getReader() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getRealPath(java.lang.String)
		 */
		@Override
		public String getRealPath(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getRemoteAddr()
		 */
		@Override
		public String getRemoteAddr() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getRemoteHost()
		 */
		@Override
		public String getRemoteHost() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getRemotePort()
		 */
		@Override
		public int getRemotePort() {
			// TODO Auto-generated method stub
			return 0;
		}

		/**
		 * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
		 */
		@Override
		public RequestDispatcher getRequestDispatcher(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getScheme()
		 */
		@Override
		public String getScheme() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getServerName()
		 */
		@Override
		public String getServerName() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.ServletRequest#getServerPort()
		 */
		@Override
		public int getServerPort() {
			// TODO Auto-generated method stub
			return 0;
		}

		/**
		 * @see javax.servlet.ServletRequest#isSecure()
		 */
		@Override
		public boolean isSecure() {
			// TODO Auto-generated method stub
			return false;
		}

		/**
		 * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
		 */
		@Override
		public void removeAttribute(String arg0) {
			// TODO Auto-generated method stub

		}

		/**
		 * @see javax.servlet.ServletRequest#setAttribute(java.lang.String, java.lang.Object)
		 */
		@Override
		public void setAttribute(String arg0, Object arg1) {
			// TODO Auto-generated method stub

		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getAuthType()
		 */
		@Override
		public String getAuthType() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getContextPath()
		 */
		@Override
		public String getContextPath() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getCookies()
		 */
		@Override
		public Cookie[] getCookies() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
		 */
		@Override
		public long getDateHeader(String arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
		 */
		@Override
		public String getHeader(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
		 */
		@Override
		public Enumeration getHeaderNames() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
		 */
		@Override
		public Enumeration getHeaders(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
		 */
		@Override
		public int getIntHeader(String arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getMethod()
		 */
		@Override
		public String getMethod() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getPathInfo()
		 */
		@Override
		public String getPathInfo() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
		 */
		@Override
		public String getPathTranslated() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getQueryString()
		 */
		@Override
		public String getQueryString() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
		 */
		@Override
		public String getRemoteUser() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getRequestURI()
		 */
		@Override
		public String getRequestURI() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getRequestURL()
		 */
		@Override
		public StringBuffer getRequestURL() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
		 */
		@Override
		public String getRequestedSessionId() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getServletPath()
		 */
		@Override
		public String getServletPath() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getSession()
		 */
		@Override
		public HttpSession getSession() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
		 */
		@Override
		public HttpSession getSession(boolean arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
		 */
		@Override
		public Principal getUserPrincipal() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
		 */
		@Override
		public boolean isRequestedSessionIdFromCookie() {
			// TODO Auto-generated method stub
			return false;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
		 */
		@Override
		public boolean isRequestedSessionIdFromURL() {
			// TODO Auto-generated method stub
			return false;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
		 */
		@Override
		public boolean isRequestedSessionIdFromUrl() {
			// TODO Auto-generated method stub
			return false;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
		 */
		@Override
		public boolean isRequestedSessionIdValid() {
			// TODO Auto-generated method stub
			return false;
		}

		/**
		 * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
		 */
		@Override
		public boolean isUserInRole(String arg0) {
			// TODO Auto-generated method stub
			return false;
		}

	}
}
