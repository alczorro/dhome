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
package net.duckling.dhome.common.auth.aop.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.duckling.dhome.common.auth.aop.IAccessDeniedHandler;
import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.auth.sso.Constants;
import net.duckling.dhome.common.util.UrlUtil;
import net.duckling.dhome.domain.people.SimpleUser;

import org.apache.log4j.Logger;

public class AccessDeniedHandler implements IAccessDeniedHandler {

	private static final Logger LOG = Logger.getLogger(AccessDeniedHandler.class);
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, NPermission requirePermission) {
		sendRedirectRequest(response, parseRedirectURL(request));
	}

	protected String parseRedirectURL(HttpServletRequest request) {
		String redirectURL = getLastRequestURL(request);
		if(isGuestUser(request)){
			redirectURL = getHomeURL(request,redirectURL);
		}
		return redirectURL;
	}

	private void sendRedirectRequest(HttpServletResponse response, String redirectURL) {
		try {
			response.sendRedirect(redirectURL);
		} catch (IOException e) {
			LOG.error("Redirect Error in DHomeDenyLinster.onDeny"+e.getMessage(),e);
		}
	}
	
	private String getHomeURL(HttpServletRequest request,String lastRequestURL){
		LOG.error("User Guest has no access - forbidden");
		request.getSession().setAttribute("redirectURL", lastRequestURL);
		return UrlUtil.getRootURL(request);
	}
	
	
	private boolean isGuestUser(HttpServletRequest request){
		HttpSession session = request.getSession();
		return (SimpleUser)session.getAttribute(Constants.CURRENT_USER) == null;
	}
	
	protected String getLastRequestURL(HttpServletRequest request) {
		String url = UrlUtil.getURLFromRequestURI(request);
		if (url.endsWith("/")){
			url = url.substring(0, url.length()-1);
		}
		if (request.getQueryString() != null){	
			url = url + "?" + request.getQueryString();
		}
		return url;
	}
	
}
