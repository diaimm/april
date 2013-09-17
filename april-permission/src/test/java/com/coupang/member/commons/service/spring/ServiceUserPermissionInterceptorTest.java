/*
 * @fileName : UserPermissionInterceptorTest.java
 * @date : 2013. 5. 27.
 * @author : diaimm. Santorini team, Agile, Coupang.
 * @desc : 
 */
package com.coupang.member.commons.service.spring;

/**
 * @author diaimm
 * 
 */
public class ServiceUserPermissionInterceptorTest {
	// @Test
	// public void preHandleTest() throws ServletException, SecurityException, NoSuchMethodException {
	// ServicePermissionInterceptor target = new ServicePermissionInterceptor() {
	// /**
	// * @see com.coupang.common.serviceuser.spring.ServicePermissionInterceptor#getRequiredPermissions(org.springframework.web.method.HandlerMethod)
	// */
	// @Override
	// RequiredPermissionInfo getRequiredPermissions(HandlerMethod handlerMethod) {
	// return null;
	// }
	//
	// /**
	// * @see com.coupang.common.serviceuser.spring.ServicePermissionInterceptor#checkPermissions(javax.servlet.http.HttpServletRequest,
	// * javax.servlet.http.HttpServletResponse, java.lang.Object,
	// * com.coupang.common.serviceuser.spring.ServicePermissionInterceptor.RequiredPermissionInfo)
	// */
	// @Override
	// boolean checkPermissions(HttpServletRequest request, HttpServletResponse response, Object handler,
	// RequiredPermissionInfo requiredPermissionInfo) {
	// return false;
	// }
	// };
	//
	// HttpServletRequest request = new MockHttpServletRequest();
	// HttpServletResponse response = new MockHttpServletResponse();
	// Assert.assertTrue(target.preHandle(request, response, new Object()));
	//
	// Method method = SampleClass.class.getDeclaredMethod("userPermission0MemberModel", new Class<?>[] { String.class, String.class });
	// Assert.assertFalse(target.preHandle(request, response, new HandlerMethod(SampleClass.class, method)));
	// }
	//
	// @Test
	// public void checkPermissionsTest() throws SecurityException, NoSuchMethodException {
	// ServicePermissionInterceptor target = new ServicePermissionInterceptor() {
	// /**
	// * @see com.coupang.common.serviceuser.spring.ServicePermissionInterceptor#checkByPermissionChecker(javax.servlet.http.HttpServletRequest,
	// * javax.servlet.http.HttpServletResponse, java.lang.Object,
	// * com.coupang.common.serviceuser.spring.ServicePermissionInterceptor.RequiredPermissionInfo,
	// * com.coupang.common.serviceuser.model.ServiceUser, com.coupang.common.serviceuser.permission.ServicePermission)
	// */
	// @Override
	// boolean checkByPermissionChecker(HttpServletRequest request, HttpServletResponse response, Object handler,
	// RequiredPermissionInfo requiredPermissionInfo, ServiceUser serviceUser, ServicePermission servicePermission) {
	// return true;
	// }
	// };
	//
	// HttpServletRequest request = new MockHttpServletRequest();
	// HttpServletResponse response = new MockHttpServletResponse();
	//
	// Method method = SampleClass.class.getDeclaredMethod("userPermission0MemberModel", new Class<?>[] { String.class, String.class });
	// RequiredPermissionInfo requiredPermissionInfo = new RequiredPermissionInfo();
	// requiredPermissionInfo.setAuthenticateType(AuthenticationType.DEFAULT);
	// List<ServicePermission> requiredPermissions = new ArrayList<ServicePermission>();
	// requiredPermissions.add(ServicePermission.MEMBER);
	// requiredPermissionInfo.addRequiredPermissions(requiredPermissions);
	// Assert.assertTrue(target.checkPermissions(request, response, new HandlerMethod(SampleClass.class, method), requiredPermissionInfo));
	//
	// target = new ServicePermissionInterceptor() {
	// /**
	// * @see com.coupang.common.serviceuser.spring.ServicePermissionInterceptor#checkByPermissionChecker(javax.servlet.http.HttpServletRequest,
	// * javax.servlet.http.HttpServletResponse, java.lang.Object,
	// * com.coupang.common.serviceuser.spring.ServicePermissionInterceptor.RequiredPermissionInfo,
	// * com.coupang.common.serviceuser.model.ServiceUser, com.coupang.common.serviceuser.permission.ServicePermission)
	// */
	// @Override
	// boolean checkByPermissionChecker(HttpServletRequest request, HttpServletResponse response, Object handler,
	// RequiredPermissionInfo requiredPermissionInfo, ServiceUser serviceUser, ServicePermission servicePermission) {
	// return false;
	// }
	// };
	// Assert.assertFalse(target.checkPermissions(request, response, new HandlerMethod(SampleClass.class, method), requiredPermissionInfo));
	// }
	//
	// @Test
	// public void getRequiredPermissionsParameterAnnotationOnlyTest() throws SecurityException, NoSuchMethodException {
	// ServicePermissionInterceptor target = new ServicePermissionInterceptor();
	// Method method = SampleClass.class.getDeclaredMethod("userPermission0MemberModel", new Class<?>[] { String.class, String.class });
	// RequiredPermissionInfo requiredPermissions = target.getRequiredPermissions(new HandlerMethod(SampleClass.class, method));
	// Assert.assertEquals(0, requiredPermissions.getRequiredPermissions().size());
	//
	// method = SampleClass.class.getDeclaredMethod("userPermission0MemberModel2", new Class<?>[] { String.class, String.class });
	// requiredPermissions = target.getRequiredPermissions(new HandlerMethod(SampleClass.class, method));
	// Assert.assertEquals(0, requiredPermissions.getRequiredPermissions().size());
	//
	// method = SampleClass.class.getDeclaredMethod("userPermission1Annotated", new Class<?>[] { String.class, Member.class });
	// requiredPermissions = target.getRequiredPermissions(new HandlerMethod(SampleClass.class, method));
	// Assert.assertEquals(0, requiredPermissions.getRequiredPermissions().size());
	//
	// method = SampleClass.class.getDeclaredMethod("userPermission2Annotated", new Class<?>[] { Member.class, Member.class });
	// requiredPermissions = target.getRequiredPermissions(new HandlerMethod(SampleClass.class, method));
	// Assert.assertEquals(0, requiredPermissions.getRequiredPermissions().size());
	// }
	//
	// @Test
	// public void getRequiredPermissionsMethodAnnotationTest() throws SecurityException, NoSuchMethodException {
	// ServicePermissionInterceptor target = new ServicePermissionInterceptor();
	// Method method = SampleClass2.class.getDeclaredMethod("userPermission0MemberModel", new Class<?>[] { String.class, String.class });
	// RequiredPermissionInfo requiredPermissions = target.getRequiredPermissions(new HandlerMethod(SampleClass.class, method));
	// Assert.assertEquals(0, requiredPermissions.getRequiredPermissions().size());
	//
	// method = SampleClass2.class.getDeclaredMethod("userPermission0MemberModel2", new Class<?>[] { String.class, String.class });
	// requiredPermissions = target.getRequiredPermissions(new HandlerMethod(SampleClass.class, method));
	// Assert.assertEquals(1, requiredPermissions.getRequiredPermissions().size());
	//
	// method = SampleClass2.class.getDeclaredMethod("userPermission1Annotated", new Class<?>[] { String.class, Member.class });
	// requiredPermissions = target.getRequiredPermissions(new HandlerMethod(SampleClass.class, method));
	// Assert.assertEquals(0, requiredPermissions.getRequiredPermissions().size());
	//
	// method = SampleClass2.class.getDeclaredMethod("userPermission2Annotated", new Class<?>[] { Member.class, Member.class });
	// requiredPermissions = target.getRequiredPermissions(new HandlerMethod(SampleClass.class, method));
	// Assert.assertEquals(0, requiredPermissions.getRequiredPermissions().size());
	// }
	//
	// @Test
	// public void getRequiredPermissionsClassAnnotationTest() throws SecurityException, NoSuchMethodException {
	// ServicePermissionInterceptor target = new ServicePermissionInterceptor();
	// Method method = SampleClass3.class.getDeclaredMethod("userPermission0MemberModel", new Class<?>[] { String.class, String.class });
	// RequiredPermissionInfo requiredPermissions = target.getRequiredPermissions(new HandlerMethod(SampleClass.class, method));
	// Assert.assertEquals(0, requiredPermissions.getRequiredPermissions().size());
	//
	// method = SampleClass3.class.getDeclaredMethod("userPermission0MemberModel2", new Class<?>[] { String.class, String.class });
	// requiredPermissions = target.getRequiredPermissions(new HandlerMethod(SampleClass.class, method));
	// Assert.assertEquals(2, requiredPermissions.getRequiredPermissions().size());
	//
	// method = SampleClass3.class.getDeclaredMethod("userPermission1Annotated", new Class<?>[] { String.class, Member.class });
	// requiredPermissions = target.getRequiredPermissions(new HandlerMethod(SampleClass.class, method));
	// Assert.assertEquals(0, requiredPermissions.getRequiredPermissions().size());
	//
	// method = SampleClass3.class.getDeclaredMethod("userPermission2Annotated", new Class<?>[] { Member.class, Member.class });
	// requiredPermissions = target.getRequiredPermissions(new HandlerMethod(SampleClass.class, method));
	// Assert.assertEquals(0, requiredPermissions.getRequiredPermissions().size());
	// }
	//
	// private static class SampleClass {
	// @SuppressWarnings("unused")
	// public void userPermission0MemberModel(String param1, String param2) {
	//
	// }
	//
	// @SuppressWarnings("unused")
	// public void userPermission0MemberModel2(String param1, @Permission(required = ServicePermission.MEMBER) String string) {
	//
	// }
	//
	// @SuppressWarnings("unused")
	// public void userPermission1Annotated(String param1, @Permission(required = ServicePermission.MEMBER) Member member) {
	//
	// }
	//
	// @SuppressWarnings("unused")
	// public void userPermission2Annotated(@Permission(required = ServicePermission.REALNAME) Member member,
	// @Permission(required = ServicePermission.MEMBER) Member member1) {
	//
	// }
	// }
	//
	// private static class SampleClass2 {
	// @SuppressWarnings("unused")
	// public void userPermission0MemberModel(String param1, String param2) {
	//
	// }
	//
	// @Permission(required = ServicePermission.SUBSCRIBE)
	// public void userPermission0MemberModel2(String param1, @Permission(required = ServicePermission.MEMBER) String string) {
	//
	// }
	//
	// @SuppressWarnings("unused")
	// public void userPermission1Annotated(String param1, @Permission(required = ServicePermission.MEMBER) Member member) {
	//
	// }
	//
	// @SuppressWarnings("unused")
	// public void userPermission2Annotated(@Permission(required = ServicePermission.REALNAME) Member member,
	// @Permission(required = ServicePermission.MEMBER) Member member1) {
	//
	// }
	// }
	//
	// @Permission(required = ServicePermission.SUBSCRIBE)
	// private static class SampleClass3 {
	// @SuppressWarnings("unused")
	// public void userPermission0MemberModel(String param1, String param2) {
	//
	// }
	//
	// @Permission(required = ServicePermission.MEMBER)
	// public void userPermission0MemberModel2(String param1, @Permission(required = ServicePermission.REALNAME) String string) {
	//
	// }
	//
	// @SuppressWarnings("unused")
	// public void userPermission1Annotated(String param1, @Permission(required = ServicePermission.MEMBER) Member member) {
	//
	// }
	//
	// @SuppressWarnings("unused")
	// public void userPermission2Annotated(@Permission(required = ServicePermission.REALNAME) Member member,
	// @Permission(required = ServicePermission.MEMBER) Member member1) {
	//
	// }
	// }
}
