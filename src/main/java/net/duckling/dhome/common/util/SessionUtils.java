/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
/**
 * 
 */
package net.duckling.dhome.common.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import net.duckling.dhome.common.auth.sso.Constants;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IInstitutionVmtService;

/**
 * 获取当前用户有关工具类
 * 
 * @author lvly
 * @since 2012-8-14
 */
public final class SessionUtils {
	@Autowired
	private static IInstitutionVmtService institutionVmtService;
	/**
	 * session过期时间
	 * */
	private static final int EXPIRED_TIME=30*60;
	private SessionUtils() {
	}
	/**
	 * 获得session并设置最大活动时间
	 * @param request
	 * @return session
	 * */
	private static HttpSession getSession(HttpServletRequest request){
		HttpSession session=request.getSession(true);
		session.setMaxInactiveInterval(EXPIRED_TIME);
		return session;
	}
	/**
	 * 更新用户首选语言
	 * @param language
	 * */
	public static void updateLanguage(HttpServletRequest request,String language){
		getSession(request).setAttribute(Constants.LANGUAGE,language);
	}
	/**获取用户首选方言
	 * @return */
	public static String getLanguage(HttpServletRequest request){
		return (String)getSession(request).getAttribute(Constants.LANGUAGE);
	}

	/**
	 * 获取当前用户实体类，从session里面取
	 * */
	public static SimpleUser getUser(HttpServletRequest request) {
		return (SimpleUser) getSession(request).getAttribute(Constants.CURRENT_USER);
	}

	/**
	 * 获取当前登录用户的id，从session里面取
	 * */
	public static int getUserId(HttpServletRequest request) {
		SimpleUser user=getUser(request);
		if(user!=null){
			return user.getId();
		}
		return -1;
	}

	/** 更新session里面的用户信息 */
	public static void updateUser(HttpServletRequest request, SimpleUser user) {
		getSession(request).setAttribute(Constants.CURRENT_USER, user);
	}
	/**获得当前登录用户的domain*/
	public static String getDomain(HttpServletRequest request){
		return (String)getSession(request).getAttribute(Constants.CURRENT_USER_DOMAIN);
	}
	
	/****获得当前登录用户的domain
	 * @param domain
	 */
	public static void setDomain(HttpServletRequest request,String domain) {
		getSession(request).setAttribute(Constants.CURRENT_USER_DOMAIN,domain);
		
	}
	/**判断当前是否有登陆用户*/
	public static boolean isLogin(HttpServletRequest request) {
		return getUser(request)!=null;
		
	}
	/**判断目标用户是否是当前用户*/
	public static boolean isSameUser(HttpServletRequest request,SimpleUser destUser){
		return isLogin(request)&&destUser!=null&&destUser.getId()==getUserId(request);
	}
//	/**判断当前用户是否是iap成员*/
//	public static boolean isIapUser(HttpServletRequest request,SimpleUser destUser){
//		if(destUser==null){
//			return false;
//		}
//		return institutionVmtService.isMember(destUser.getEmail());
//	}
	/**
	 * @param request
	 * @return
	 */
	public static boolean isAdminUser(HttpServletRequest request) {
		SimpleUser user=getUser(request);
		if(user==null){
			return false;
		}
		if(user.getIsAdmin()==null){
			return false;
		}
		return user.getIsAdmin();
	}
}
