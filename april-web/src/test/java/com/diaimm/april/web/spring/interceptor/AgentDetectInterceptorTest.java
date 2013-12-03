package com.diaimm.april.web.spring.interceptor;

import com.diaimm.april.web.util.AgentInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.support.ServletContextScope;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

/**
 * User: diaimm(봉구)
 * Date: 13. 12. 3
 * Time: 오후 11:30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:com/diaimm/april/web/spring/interceptor/AgentDetectInterceptorTest.xml")
public class AgentDetectInterceptorTest {
	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void innerClassConstructorTest() {
		Class<DefaultAgentInfoHolder> defaultAgentInfoHolderClass = DefaultAgentInfoHolder.class;

		try {
			Constructor<DefaultAgentInfoHolder> constructor = defaultAgentInfoHolderClass.getConstructor();
			System.out.println(constructor);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		try {
			Constructor<DefaultAgentInfoHolder> declaredConstructor = defaultAgentInfoHolderClass.getDeclaredConstructor();
			System.out.println(declaredConstructor);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		Constructor<?>[] constructors = defaultAgentInfoHolderClass.getConstructors();
		System.out.println(constructors[0]);
	}

	@Test
	public void initAgentInfoHolderBeanTest() {
		((GenericApplicationContext)applicationContext).getBeanFactory().registerScope("request", getServletScope());
		Assert.assertNotNull(applicationContext.getBean(AgentDetectInterceptor.class));
		Assert.assertNotNull(applicationContext.getBean(AgentDetectInterceptor.AgentInfoHolder.class));
	}

	private ServletContextScope getServletScope() {
		return new ServletContextScope(new ServletContext() {
			@Override
			public String getContextPath() {
				return null;
			}

			@Override
			public ServletContext getContext(String uripath) {
				return null;
			}

			@Override
			public int getMajorVersion() {
				return 0;
			}

			@Override
			public int getMinorVersion() {
				return 0;
			}

			@Override
			public String getMimeType(String file) {
				return null;
			}

			@Override
			public Set getResourcePaths(String path) {
				return null;
			}

			@Override
			public URL getResource(String path) throws MalformedURLException {
				return null;
			}

			@Override
			public InputStream getResourceAsStream(String path) {
				return null;
			}

			@Override
			public RequestDispatcher getRequestDispatcher(String path) {
				return null;
			}

			@Override
			public RequestDispatcher getNamedDispatcher(String name) {
				return null;
			}

			@Override
			public Servlet getServlet(String name) throws ServletException {
				return null;
			}

			@Override
			public Enumeration getServlets() {
				return null;
			}

			@Override
			public Enumeration getServletNames() {
				return null;
			}

			@Override
			public void log(String msg) {

			}

			@Override
			public void log(Exception exception, String msg) {

			}

			@Override
			public void log(String message, Throwable throwable) {

			}

			@Override
			public String getRealPath(String path) {
				return null;
			}

			@Override
			public String getServerInfo() {
				return null;
			}

			@Override
			public String getInitParameter(String name) {
				return null;
			}

			@Override
			public Enumeration getInitParameterNames() {
				return null;
			}

			@Override
			public Object getAttribute(String name) {
				return null;
			}

			@Override
			public Enumeration getAttributeNames() {
				return null;
			}

			@Override
			public void setAttribute(String name, Object object) {

			}

			@Override
			public void removeAttribute(String name) {

			}

			@Override
			public String getServletContextName() {
				return null;
			}
		});
	}

	private static class DefaultAgentInfoHolder implements AgentDetectInterceptor.AgentInfoHolder {
		private AgentInfo agentInfo;

		public DefaultAgentInfoHolder() {
		}

		@Override
		public void init(HttpServletRequest request, HttpServletResponse response) {
		}

		/**
		 * @return the agentInfo
		 */
		@Override
		public AgentInfo get() {
			return agentInfo;
		}
	}
}
