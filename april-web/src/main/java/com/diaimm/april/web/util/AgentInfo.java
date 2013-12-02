/*
 * @fileName : RequestHelper.java
 * @date : 2013. 7. 2.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.web.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mobile.device.LiteDeviceResolver;

import javax.servlet.http.HttpServletRequest;

/**
 * @author diaimm
 * 
 */
public class AgentInfo {
	private final HttpServletRequest request;
	private final Device device;
	private final String agent;
	private boolean isforceAppToWeb = false;

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
}
