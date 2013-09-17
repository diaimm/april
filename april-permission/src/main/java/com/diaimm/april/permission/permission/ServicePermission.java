/*
 * @fileName : UserPermission.java
 * @date : 2013. 5. 27.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.diaimm.april.permission.permission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.diaimm.april.permission.model.ServiceUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;

import com.coupang.commons.ByPhase;
import com.coupang.commons.web.util.URLHelpers;
import com.diaimm.april.permission.model.ServiceUser;

/**
 * @author diaimm
 */
public enum ServicePermission {
	// 구독
	SUBSCRIBE(0) {
		@Override
		public boolean authorized(ServiceUser serviceUser) {
			/*
			 * 로그인 하지 않은 회원은  SUBSCRIBE 으로 명명
			 * 검사할 것이 없다. 
			 */
			return true;
		}

		@Override
		void setupAuthenticationUrlMapping(Map<AuthenticationType, String> mapping, ServiceUser serviceUser) {
			// do nothing
		}
	},
	// 정회원
	MEMBER(1, SUBSCRIBE) {
		@Override
		public boolean authorized(ServiceUser serviceUser) {
			/*
			 * 로그인을 성공했다면 정회원이다. 
			 */
			if (serviceUser.isLogin()) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		void setupAuthenticationUrlMapping(Map<AuthenticationType, String> mapping, ServiceUser serviceUser) {
			/*
			 * 로그인 화면으로 보낸다.  
			 */
			mapping.put(AuthenticationType.DEFAULT, "/login.pang");
			mapping.put(AuthenticationType.MOBILE, "/login.pang");
			// TODO 현재는 팝업으로 인증이 필요한 서비스가 없기 때문에 팝업을 닫고 부모 페이지를 리다이렉트 시킨다.
			mapping.put(AuthenticationType.PC_POPUP, "/loginForPopUp.pang");
		}
	},
	// 본인인증
	REALNAME(2, MEMBER) {
		@Override
		public boolean authorized(ServiceUser serviceUser) {
			/*
			 * 본인 인증을 받아서  verifiedYN = Y 이라는 것은 ci, di 정보도 있다는 의미이다. 
			 */
			if (serviceUser.isLogin() && StringUtils.equals(serviceUser.getRealNameVerified(), "Y")) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		void setupAuthenticationUrlMapping(Map<AuthenticationType, String> mapping, ServiceUser serviceUser) {
			if (serviceUser.isLogin() == false) {
				/*
				 * 로그인을 안한 상태면 로그인 화면으로 보냄
				 */
				MEMBER.setupAuthenticationUrlMapping(mapping, serviceUser);
			} else {
				/*
				 * 본인인증을 안한 상태면 본인 인증 화면으로 보냄 
				 */
				mapping.put(AuthenticationType.DEFAULT, "/userModify.pang");
				mapping.put(AuthenticationType.MOBILE, "/userModify.pang");
				// TODO 현재는 팝업으로 인증이 필요한 서비스가 없기 때문에 팝업을 닫고 부모 페이지를 리다이렉트 시킨다.
				mapping.put(AuthenticationType.PC_POPUP, "/userModifyForPopUp.pang");
			}
		}
	};

	private final ServicePermission[] required;
	private final int order;

	ServicePermission(int order, ServicePermission... required) {
		this.required = required;
		this.order = order;
		initializeFullPermissions();
	}

	/**
	 * 
	 */
	private void initializeFullPermissions() {
		List<ServicePermission> fullPermissions = getRequiredPermissionsCascade();
		Collections.reverse(fullPermissions);
		FullPermissionHolder.cache.put(this, fullPermissions);
	}

	/**
	 * @return
	 */
	private List<ServicePermission> getRequiredPermissionsCascade() {
		List<ServicePermission> ret = new ArrayList<ServicePermission>();

		ServicePermission[] currentPermissions = new ServicePermission[] {this};
		for (ServicePermission permission : currentPermissions) {
			ret.add(permission);

			if (permission.required != null) {
				for (ServicePermission requiredPermission : permission.required) {
					ret.addAll(requiredPermission.getRequiredPermissionsCascade());
				}
			}
		}
		return ret;
	}

	public List<ServicePermission> getFullPermissionCheckers() {
		return FullPermissionHolder.cache.get(this);
	}

	public static List<ServicePermission> sort(Set<ServicePermission> permissions) {
		List<ServicePermission> toList = new ArrayList<ServicePermission>(permissions);
		Collections.sort(toList, new Comparator<ServicePermission>() {
			@Override
			public int compare(ServicePermission o1, ServicePermission o2) {
				return o1.order - o2.order;
			}
		});

		return toList;
	}

	private static final class FullPermissionHolder {
		private static Map<ServicePermission, List<ServicePermission>> cache = new HashMap<ServicePermission, List<ServicePermission>>();
	}

	public boolean authorized(HandlerMethod handler, ServiceUser serviceUser, HttpServletRequest request, HttpServletResponse response, AuthenticationType authenticationType, ByPhase byPhase) {
		if (authorized(serviceUser) == false) {
			// 인증 실패 시 리턴 시킨다. 
			onAuthorizationFail(handler, serviceUser, request, response, authenticationType, byPhase);
			return false;
		}

		return true;
	}

	/**
	 * @param handler
	 * @param serviceUser
	 * @param request
	 * @param response
	 * @param authenticateType
	 */
	void onAuthorizationFail(HandlerMethod handler, ServiceUser serviceUser, HttpServletRequest request, HttpServletResponse response, AuthenticationType authenticationType, ByPhase byPhase) {
		response.setStatus(302);
		response.setHeader("Location", getFullAuthenticationUrl(handler, serviceUser, request, authenticationType, byPhase));
	}

	/**
	 * 접근 실패 하여 리턴 할 URL 생성
	 * @param handler
	 * @param serviceUser
	 * @param request
	 * @param authenticationType
	 * @return
	 */
	String getFullAuthenticationUrl(HandlerMethod handler, ServiceUser serviceUser, HttpServletRequest request, AuthenticationType authenticationType, ByPhase byPhase) {
		StringBuffer fullUrl = new StringBuffer(URLHelpers.LOGIN_URL_BASE.getValue(byPhase, request));
		Map<AuthenticationType, String> mapping = new HashMap<AuthenticationType, String>();

		// AuthenticationType 에 따른 이동 페이지를 Map 에 세팅
		setupAuthenticationUrlMapping(mapping, serviceUser);

		// AuthenticationType 에 따른 이동 페이지 선택, 상대 경로가 내려온다. 
		String authenticationUrl = mapping.get(authenticationType);
		if (authenticationUrl.charAt(0) != '/') {
			fullUrl.append("/");
		}

		fullUrl.append(authenticationUrl);

		if (authenticationUrl.contains("?")) {
			fullUrl.append("&");
		} else {
			fullUrl.append("?");
		}

		// 권한 확득 후 다시 리턴할 현재 페이지 정보 저장
		fullUrl.append("rtnUrl=").append(URLHelpers.CURRENT_URL_BASE.getValue(byPhase, request));

		return fullUrl.toString();
	}

	/**
	 * 사용자가 권한이 있는지 판단한다.
	 * 
	 * @param serviceUser
	 * @return
	 */
	abstract boolean authorized(ServiceUser serviceUser);

	/**
	 * url은 상대경로로 작성하며, 현재 사용자 정보중 기본으로 세팅해야 하는 값이 있다면 세팅하도록 합니다.
	 * 
	 * @return
	 */
	abstract void setupAuthenticationUrlMapping(Map<AuthenticationType, String> mapping, ServiceUser serviceUser);
}
