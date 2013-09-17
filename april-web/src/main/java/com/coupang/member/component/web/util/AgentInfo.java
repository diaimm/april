/*
 * @fileName : RequestHelper.java
 * @date : 2013. 7. 2.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.component.web.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mobile.device.LiteDeviceResolver;

/**
 * @author diaimm
 * 
 */
public class AgentInfo {
	private final HttpServletRequest request;
	private final Device device;
	private final String agent;
	private boolean isforceAppToWeb = false;
	private boolean isApp = false;
	private String appReturnUrl;
	private String appUuid;

	public AgentInfo(HttpServletRequest request) {
		this.request = request;
		this.agent = request.getHeader("User-Agent");

		DeviceResolver deviceResolver = new LiteDeviceResolver();
		this.device = deviceResolver.resolveDevice(request);
	}

	/**
	 * 화면을 표시 유형 - mobile or web or App
	 * @return
	 */
	public boolean isMobileView() {
		if (this.isforceAppToWeb) {
			return false;
		}

		return this.isMobile();
	}

	/**
	 * 모바일 Agent 인지 아닌지
	 * @return
	 */
	public boolean isMobile() {
		if (device == null) {
			return false;
		}

		return device.isMobile() || this.isIPad();
	}

	public String getAgent() {
		return agent;
	}

	public boolean isIPad() {
		String userAgent = request.getHeader("user-agent");
		if (StringUtils.isNotBlank(userAgent) && userAgent.contains("iPad")) {
			return true;
		}

		return false;
	}

	/**
	 * @return the isApp
	 */
	public boolean isApp() {
		return isApp;
	}

	/**
	 * @param isApp the isApp to set
	 */
	public void setApp(boolean isApp) {
		this.isApp = isApp;
	}

	/**
	 * @return the isforceAppToWeb
	 */
	public boolean isIsforceAppToWeb() {
		return isforceAppToWeb;
	}

	/**
	 * @param isforceAppToWeb the isforceAppToWeb to set
	 */
	public void setIsforceAppToWeb(boolean isforceAppToWeb) {
		this.isforceAppToWeb = isforceAppToWeb;
	}

	/**
	 * @return the appReturnUrl
	 */
	public String getAppReturnUrl() {
		return appReturnUrl;
	}

	/**
	 * @param appReturnUrl the appReturnUrl to set
	 */
	public void setAppReturnUrl(String appReturnUrl) {
		this.appReturnUrl = appReturnUrl;
	}

	/**
	 * @return the appUuid
	 */
	public String getAppUuid() {
		return appUuid;
	}

	/**
	 * @param appUuid the appUuid to set
	 */
	public void setAppUuid(String appUuid) {
		this.appUuid = appUuid;
	}

}
