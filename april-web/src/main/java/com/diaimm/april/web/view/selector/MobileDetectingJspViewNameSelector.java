/*
 * @fileName : MobileDetectingViewNameSelector.java
 * @date : 2013. 7. 2.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.web.view.selector;

import com.diaimm.april.web.spring.interceptor.AgentDetectInterceptor.AgentInfoHolder;
import com.diaimm.april.web.util.AgentInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author diaimm
 */
public class MobileDetectingJspViewNameSelector implements ViewNameSelector, ApplicationContextAware {
	public static final String IS_MOBILE_SELECT_ATTRIBUTE_KEY = MobileDetectingJspViewNameSelector.class.getName() + "_isMobileSelected";
	private static final String DEFAULT_POSTFIX = ".jsp";
	private static Map<String, Boolean> mobileViewFileExistsCache = new ConcurrentHashMap<String, Boolean>();
	private String mobilePrefix = "/WEB-INF/views_mobile/jsp/";
	private String webPrefix = "/WEB-INF/views/jsp/";
	private String postFix = DEFAULT_POSTFIX;
	private ApplicationContext applicationContext;

	public static Map<String, Boolean> getMobileViewFileExistsCache() {
		return mobileViewFileExistsCache;
	}

	/**
	 * @see ViewNameSelector#getViewName(javax.servlet.http.HttpServletRequest, java.lang.String)
	 */
	@Override
	public String getViewName(HttpServletRequest request, String viewName) {
		if (StringUtils.isNotBlank(viewName) && viewName.startsWith("/")) {
			viewName = viewName.substring(1);
		}

		String webViewPath = getWebPrefix() + viewName + getPostFix();
		String mobileViewPath = getMobilePrefix() + viewName + getPostFix();

		if (isMobileView(request, mobileViewPath)) {
			return mobileViewPath;
		}
		return webViewPath;
	}

	public boolean isMobileView(HttpServletRequest request, String mobileViewPath) {
		return getAgentInfo().isMobileView() && isMobileViewFileExists(request, mobileViewPath);
	}

	AgentInfo getAgentInfo() {
		AgentInfoHolder agentInfoHolder = applicationContext.getBean(AgentInfoHolder.class);
		return agentInfoHolder.get();
	}

	boolean isMobileViewFileExists(HttpServletRequest request, String mobileViewPath) {
		if (!getMobileViewFileExistsCache().containsKey(mobileViewPath)) {
			synchronized (getMobileViewFileExistsCache()) {
				if (!getMobileViewFileExistsCache().containsKey(mobileViewPath)) {
					String realPath = request.getSession().getServletContext().getRealPath(mobileViewPath);
					File file = new File(realPath);
					getMobileViewFileExistsCache().put(mobileViewPath, file.exists() && file.isFile());
				}
			}
		}

		boolean ret = getMobileViewFileExistsCache().get(mobileViewPath);
		request.setAttribute(IS_MOBILE_SELECT_ATTRIBUTE_KEY, ret);
		return ret;
	}

	/**
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public String getMobilePrefix() {
		return mobilePrefix;
	}

	/**
	 * @param mobilePrefix the mobilePrefix to set
	 */
	public void setMobilePrefix(String mobilePrefix) {
		this.mobilePrefix = mobilePrefix;
	}

	public String getWebPrefix() {
		return webPrefix;
	}

	/**
	 * @param webPrefix the webPrefix to set
	 */
	public void setWebPrefix(String webPrefix) {
		this.webPrefix = webPrefix;
	}

	public String getPostFix() {
		return postFix;
	}

	/**
	 * @param postFix the postFix to set
	 */
	public void setPostFix(String postFix) {
		this.postFix = postFix;
	}
}
