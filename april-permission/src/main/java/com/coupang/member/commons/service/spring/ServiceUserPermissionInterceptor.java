/*
 * @fileName : UserPermissionInterceptor.java
 * @date : 2013. 5. 27.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.commons.service.spring;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import com.coupang.commons.ByPhase;
import com.coupang.member.commons.service.ServiceUserConstantsAware;
import com.coupang.member.commons.service.model.ServiceUser;
import com.coupang.member.commons.service.permission.AuthenticationType;
import com.coupang.member.commons.service.permission.ServicePermission;
import com.coupang.member.commons.service.permission.annotation.Permission;

/**
 * @author diaimm
 * 
 */
public class ServiceUserPermissionInterceptor extends WebContentInterceptor implements ServiceUserConstantsAware {
	@Autowired
	private ByPhase byPhase;

	/**
	 * @see org.springframework.web.servlet.mvc.WebContentInterceptor#preHandle(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
		if (!HandlerMethod.class.isAssignableFrom(handler.getClass())) {
			return true;
		}

		RequiredPermissionInfo requiredPermissionInfo = getRequiredPermissions((HandlerMethod)handler);
		return checkPermissions(request, response, handler, requiredPermissionInfo);
	}

	/**
	 * 접근 권한 체크 로직
	 * @param request
	 * @param response
	 * @param handler
	 * @param requiredPermissionInfo
	 */
	boolean checkPermissions(HttpServletRequest request, HttpServletResponse response, Object handler, RequiredPermissionInfo requiredPermissionInfo) {
		ServiceUser serviceUser = (ServiceUser)request.getAttribute(ServiceUserInterceptor.USER_MODEL_ATTRIBUTE_KEY);
		for (ServicePermission servicePermission : ServicePermission.sort(requiredPermissionInfo.requiredPermissions)) {
			/*
			 * 로그인 회원 정보와 비교하여 접근 권한 처리 
			 */
			if (checkByPermissionChecker(request, response, handler, requiredPermissionInfo, serviceUser, servicePermission) == false) {
				return false;
			}
		}

		return true;
	}

	/**
	 * @param request
	 * @param response
	 * @param handler
	 * @param requiredPermissionInfo
	 * @param serviceUser
	 * @param servicePermission
	 * @return
	 */
	boolean checkByPermissionChecker(HttpServletRequest request, HttpServletResponse response, Object handler, RequiredPermissionInfo requiredPermissionInfo, ServiceUser serviceUser,
		ServicePermission servicePermission) {
		return servicePermission.authorized((HandlerMethod)handler, serviceUser, request, response, requiredPermissionInfo.authenticateType, byPhase);
	}

	/**
	 * @param method
	 * @return
	 */
	RequiredPermissionInfo getRequiredPermissions(HandlerMethod handlerMethod) {
		// authenticateType : class -> method -> parameter로 가면서 override 된다.
		// requiredPermissions : class -> method -> parameter로 가면서 취합된다.
		RequiredPermissionInfo ret = new RequiredPermissionInfo();
		overridePermission(ret, handlerMethod.getBeanType().getAnnotation(Permission.class));
		overridePermission(ret, handlerMethod.getMethodAnnotation(Permission.class));

		MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
		if (methodParameters != null) {
			for (MethodParameter methodParameter : methodParameters) {
				if (ServiceUser.class.isAssignableFrom(methodParameter.getParameterType())) {
					overridePermission(ret, methodParameter.getParameterAnnotation(Permission.class));
				}
			}
		}
		return ret;
	}

	/**
	 * @param ret
	 * @param classPermission
	 */
	private void overridePermission(RequiredPermissionInfo requiredPermissionInfo, Permission permission) {
		if (permission != null) {
			requiredPermissionInfo.setAuthenticateType(permission.authType());
			requiredPermissionInfo.addRequiredPermissions(permission.required().getFullPermissionCheckers());
		}
	}

	/**
	 * test 가능 range로..
	 * 
	 * @author diaimm
	 * 
	 */
	static class RequiredPermissionInfo {
		private Set<ServicePermission> requiredPermissions = new HashSet<ServicePermission>();
		private AuthenticationType authenticateType;

		/**
		 * @return the requiredPermissions
		 */
		Set<ServicePermission> getRequiredPermissions() {
			return requiredPermissions;
		}

		/**
		 * @return the authenticateType
		 */
		AuthenticationType getAuthenticateType() {
			return authenticateType;
		}

		/**
		 * @param requiredPermissions
		 *            the requiredPermissions to set
		 */
		void addRequiredPermissions(Collection<ServicePermission> requiredPermissions) {
			this.requiredPermissions.addAll(requiredPermissions);
		}

		/**
		 * @param authenticateType
		 *            the authenticateType to set
		 */
		void setAuthenticateType(AuthenticationType authenticateType) {
			this.authenticateType = authenticateType;
		}
	}
}
