/*
 * @fileName : UserPermissionInterceptor.java
 * @date : 2013. 5. 27.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.permission.spring;

import com.diaimm.april.commons.ByPhase;
import com.diaimm.april.permission.ServiceUserConstantsAware;
import com.diaimm.april.permission.model.ServiceUser;
import com.diaimm.april.permission.permission.AuthenticationType;
import com.diaimm.april.permission.permission.AuthenticationTypeFactory;
import com.diaimm.april.permission.permission.ServicePermission;
import com.diaimm.april.permission.permission.ServicePermissionFactory;
import com.diaimm.april.permission.permission.annotation.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author diaimm
 * 
 */
public class ServiceUserPermissionInterceptor extends WebContentInterceptor implements ServiceUserConstantsAware {
	@Autowired
	private ByPhase byPhase;
	private AuthenticationTypeFactory authenticationTypeFactory;
	private ServicePermissionFactory servicePermissionFactory;

	public ServiceUserPermissionInterceptor(AuthenticationTypeFactory authenticationTypeFactory, ServicePermissionFactory servicePermissionFactory) {
		this.authenticationTypeFactory = authenticationTypeFactory;
		this.servicePermissionFactory = servicePermissionFactory;
	}

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

		for (ServicePermission servicePermission : getSortedPermissions(requiredPermissionInfo)) {
			/*
			 * 로그인 회원 정보와 비교하여 접근 권한 처리 
			 */
			if (checkByPermissionChecker(request, response, handler, requiredPermissionInfo, serviceUser, servicePermission) == false) {
				return false;
			}
		}

		return true;
	}

	private List<ServicePermission> getSortedPermissions(RequiredPermissionInfo requiredPermissionInfo) {
		List<ServicePermission> requiredPermissions = new ArrayList<ServicePermission>(requiredPermissionInfo.requiredPermissions);
		Collections.sort(requiredPermissions, new Comparator<ServicePermission>() {
			@Override
			public int compare(ServicePermission o1, ServicePermission o2) {
				return o1.getOrder() - o2.getOrder();
			}
		});

		return requiredPermissions;
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
	boolean checkByPermissionChecker(HttpServletRequest request, HttpServletResponse response, Object handler,
		RequiredPermissionInfo requiredPermissionInfo, ServiceUser serviceUser, ServicePermission servicePermission) {
		return servicePermission.authorized((HandlerMethod)handler, serviceUser, request, response, requiredPermissionInfo.authenticateType, byPhase);
	}

	/**
	 * @param handlerMethod
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
	 * @param requiredPermissionInfo
	 * @param permission
	 */
	private void overridePermission(RequiredPermissionInfo requiredPermissionInfo, Permission permission) {
		if (permission != null) {
			requiredPermissionInfo.setAuthenticateType(authenticationTypeFactory.create(permission));
			requiredPermissionInfo.addRequiredPermissions(servicePermissionFactory.create(permission));
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
		 * @param authenticateType
		 *            the authenticateType to set
		 */
		void setAuthenticateType(AuthenticationType authenticateType) {
			this.authenticateType = authenticateType;
		}

		/**
		 * @param requiredPermissions
		 *            the requiredPermissions to set
		 */
		void addRequiredPermissions(ServicePermission... requiredPermissions) {
			this.requiredPermissions.addAll(Arrays.asList(requiredPermissions));
		}
	}
}
