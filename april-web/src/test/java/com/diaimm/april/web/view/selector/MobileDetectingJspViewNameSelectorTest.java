package com.diaimm.april.web.view.selector;

import com.diaimm.april.web.util.AgentInfo;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.http.HttpServletRequest;

/**
 * User: diaimm(봉구)
 * Date: 13. 12. 4
 * Time: 오후 10:58
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:com/diaimm/april/web/view/selector/MobileDetectingJspViewNameSelectorTest.xml")
public class MobileDetectingJspViewNameSelectorTest {
	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void getViewNameTest() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		MobileDetectingJspViewNameSelector target = new MobileDetectingJspViewNameSelector() {
			@Override
			AgentInfo getAgentInfo() {
				return new AgentInfo(request) {
					@Override
					public boolean isMobileView() {
						return true;
					}
				};
			}

			@Override
			boolean isMobileViewFileExists(HttpServletRequest request, String mobileViewPath) {
				return true;
			}
		};
		target.setApplicationContext(applicationContext);
		Assert.assertEquals("/WEB-INF/views_mobile/jsp/test.jsp", target.getViewName(request, "test"));

		target = new MobileDetectingJspViewNameSelector() {
			@Override
			AgentInfo getAgentInfo() {
				return new AgentInfo(request) {
					@Override
					public boolean isMobileView() {
						return false;
					}
				};
			}

			@Override
			boolean isMobileViewFileExists(HttpServletRequest request, String mobileViewPath) {
				return true;
			}
		};
		target.setApplicationContext(applicationContext);
		Assert.assertEquals("/WEB-INF/views/jsp/test.jsp", target.getViewName(request, "test"));


		target = new MobileDetectingJspViewNameSelector() {
			@Override
			AgentInfo getAgentInfo() {
				return new AgentInfo(request) {
					@Override
					public boolean isMobileView() {
						return true;
					}
				};
			}

			@Override
			boolean isMobileViewFileExists(HttpServletRequest request, String mobileViewPath) {
				return false;
			}
		};
		target.setApplicationContext(applicationContext);
		Assert.assertEquals("/WEB-INF/views/jsp/test.jsp", target.getViewName(request, "test"));
	}
}
