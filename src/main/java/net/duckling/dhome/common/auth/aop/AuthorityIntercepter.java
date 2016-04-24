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
package net.duckling.dhome.common.auth.aop;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.auth.sso.Constants;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.common.util.UrlUtil;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionHomeService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AuthorityIntercepter extends HandlerInterceptorAdapter {
	@Autowired
	private IInstitutionHomeService homeService;
	@Autowired
	private IInstitutionBackendService backendService;

	 private static final Logger LOG = Logger.getLogger(PermissionIntercepter.class);

	    private IPermissionChecker checker = null;

	    private IAccessDeniedHandler accessDeniedHandler = null;

	    private String param;

	    private static final String DOMAIN_REGEX = "/people/[A-Z\\-0-9a-z]+/";

	    private PermissionResovler resolver = new PermissionResovler();

	    public void setParam(String param) {
	        this.param = param;
	    }

	    public String getParam() {
	        return this.param;
	    }

	    public void setListener(IAccessDeniedHandler listener) {
	        this.accessDeniedHandler = listener;
	    }

	    public void setPermissionChecker(IPermissionChecker checker) {
	        this.checker = checker;
	    }

	    private String extractDomain(String requestURL) {
	        Pattern p = Pattern.compile(DOMAIN_REGEX);
	        Matcher m = p.matcher(requestURL);
	        while (m.find()) {
	            String result = m.group();
	            result = result.substring(result.indexOf('/', 2) + 1, result.lastIndexOf('/'));
	            return result;
	        }
	        return null;
	    }

	    private boolean isAdminURL(String url) {
	        return (url != null) ? url.contains("/admin/") : false;
	    }

	    private boolean isMySelf(String targetDomain, String currentDomain) {
	        return (targetDomain != null && currentDomain != null) ? (targetDomain.equals(currentDomain)) : false;
	    }

	    private boolean isSystemAdmin(String url) {
	        return (url != null) ? url.contains("/system/admin") : false;
	    }

	    private boolean systemRedirect(HttpServletRequest request, HttpServletResponse response) {
	        SimpleUser su = (SimpleUser) request.getSession(true).getAttribute(Constants.CURRENT_USER);
	        if (null == su) {// 用户未登录，则跳到登录页面
	        	String wanaUrl=UrlUtil.getFullRequestUrl(request);
	        	try {
					wanaUrl=URLEncoder.encode(wanaUrl,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					LOG.error(e.getMessage(),e);
					return false;
				}
	            gotoErrorPage(response, UrlUtil.getRootURL(request) + "/system/login?state"+wanaUrl);
	            return false;
	        } else if (null == su.getIsAdmin() || !su.getIsAdmin()) {// 不是系统管理员，则跳到个人主页
	            String currentDomain = (String) request.getSession().getAttribute(Constants.CURRENT_USER_DOMAIN);
	            gotoErrorPage(response, UrlUtil.getRootURL(request) + "/people/" + currentDomain + "/index.html");
	            return false;
	        }
	        return true;
	    }

	    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
	    	SimpleUser su = (SimpleUser) request.getSession(true).getAttribute(Constants.CURRENT_USER);
	    	InstitutionHome home=homeService.getInstitutionByInstitutionId(su.getInstitutionId());
	    	if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
	    		gotoErrorPage(response, UrlUtil.getRootURL(request) + "/institution/" + home.getDomain() + "/backend/topic/list/1");
				return false;
			}else{
				return true;
			}
	    }

	    private void gotoErrorPage(HttpServletResponse response, String redirectURL) {
	        try {
	            response.sendRedirect(redirectURL);
	        } catch (IOException e) {
	            LOG.error("io exception in handler interceptor.", e);
	        }
	    }

	    private String getMethodNameFromHandler(Object handler) {
	        String methodName;
	        HandlerMethod method = (HandlerMethod) handler;
	        methodName = method.getMethod().getName();
	        return methodName;
	    }

	    private boolean isAccessable(HttpServletRequest request, NPermission permission) {
	        if (checker != null) {
	            return checker.isAccessable(request, permission);
	        }
	        return true;
	    }

	    private boolean executeAccessDeniedProcess(HttpServletRequest request, HttpServletResponse response,
	            Object handler, String methodName, NPermission permission) {
	        Method denyMethod = resolver.findDenyProcessor(handler, methodName);
	        if (denyMethod != null) {
	            Object returnValue = null;
	            try {
	                returnValue = denyMethod.invoke(handler, new Object[] { methodName, request, response });
	            } catch (Exception e) {
	                LOG.error(e.getMessage(), e);
	            }
	            if (isProcessed(returnValue)) {
	                return false;
	            }
	        }else{
	        	String wanaUrl=UrlUtil.getFullRequestUrl(request);
	        	try {
					wanaUrl=URLEncoder.encode(wanaUrl,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					LOG.error(e.getMessage(),e);
					return false;
				}
	            gotoErrorPage(response, UrlUtil.getRootURL(request) + "/system/login?state="+wanaUrl);
	            return false;
	        }
	        accessDeniedHandler.handle(request, response, permission);
	        return false;
	    }

	    private NPermission findPermission(Object handler, String methodName) {
	        NPermission permission = null;
	        if (handler instanceof HandlerMethod) {
	            HandlerMethod hm = (HandlerMethod) handler;
	            permission = resolver.findPermission(hm.getBean(), methodName);
	        } else {
	            permission = resolver.findPermission(handler, methodName);
	        }
	        return permission;
	    }

	    private boolean isProcessed(Object returnValue) {
	        if (returnValue == null) {
	            return true;
	        }
	        return (Boolean) returnValue;
	    }
   
}
