/*
 * @fileName : UserPermission.java
 * @date : 2013. 5. 27.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.permission.permission;

import com.diaimm.april.commons.ByPhase;
import com.diaimm.april.permission.model.ServiceUser;
import org.springframework.core.Ordered;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author diaimm
 */
public interface ServicePermission extends Ordered {
	/**
	 * 사용자가 권한이 있는지 판단한다.
	 * 
	 *
	 * @param handler
	 * @param serviceUser
	 * @param request
	 *@param response
	 * @param authenticateType
	 * @param byPhase @return
	 */
	abstract boolean authorized(HandlerMethod handler, ServiceUser serviceUser, HttpServletRequest request, HttpServletResponse response, AuthenticationType authenticateType, ByPhase byPhase);

	/**
	 * url은 상대경로로 작성하며, 현재 사용자 정보중 기본으로 세팅해야 하는 값이 있다면 세팅하도록 합니다.
	 * 
	 * @return
	 */
	abstract String setupAuthenticationUrl(ServiceUser serviceUser, HttpServletRequest request);
}
