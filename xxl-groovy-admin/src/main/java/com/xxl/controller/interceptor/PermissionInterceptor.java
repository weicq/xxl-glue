package com.xxl.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.xxl.controller.annotation.PermessionLimit;
import com.xxl.core.util.CookieUtil;

/**
 * 权限拦截, 简易版
 * @author xuxueli 2015-12-12 18:09:04
 */
public class PermissionInterceptor extends HandlerInterceptorAdapter {
	
	public static final String LOGIN_IDENTITY_KEY = "LOGIN_IDENTITY";
	public static final String LOGIN_IDENTITY_VAL = "sdf!121sdf$78sd!8";
	
	public static boolean login(HttpServletResponse response){
		CookieUtil.set(response, LOGIN_IDENTITY_KEY, LOGIN_IDENTITY_VAL);
		return true;
	}
	public static void logout(HttpServletRequest request, HttpServletResponse response){
		CookieUtil.remove(request, response, LOGIN_IDENTITY_KEY);
	}
	public static boolean ifLogin(HttpServletRequest request){
		String indentityInfo = CookieUtil.getValue(request, LOGIN_IDENTITY_KEY);
		if (indentityInfo==null || !LOGIN_IDENTITY_VAL.equals(indentityInfo.trim())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		if (!(handler instanceof HandlerMethod)) {
			return super.preHandle(request, response, handler);
		}
		
		HandlerMethod method = (HandlerMethod)handler;
		PermessionLimit permission = method.getMethodAnnotation(PermessionLimit.class);
		if (permission == null || (permission.login() && !ifLogin(request))) {
			response.sendRedirect(request.getContextPath() + "/login");
		}
		
		return super.preHandle(request, response, handler);
	}
	
}
