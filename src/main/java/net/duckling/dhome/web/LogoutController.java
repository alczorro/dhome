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
package net.duckling.dhome.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.duckling.dhome.common.auth.sso.Constants;
import net.duckling.dhome.common.auth.sso.IAuthenticationService;
import net.duckling.dhome.domain.people.SimpleUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
/**
 * 用户登出
 * @author Yangxp
 * @date 2012-08-01
 */
@Controller
@RequestMapping("/system/logout")
public class LogoutController {
	
	@Autowired
	private IAuthenticationService authenticationService;
	
	
	public void setAuthenticationService(IAuthenticationService as){
		authenticationService = as;
	}
	/**
	 * 用户登出：主要生成去UMT登出的链接
	 * @param request
	 * @return
	 */
	@RequestMapping()
	public ModelAndView logout(HttpServletRequest request){
		HttpSession session = request.getSession();
		SimpleUser curUser = (SimpleUser)session.getAttribute(Constants.CURRENT_USER);
		if(null!=curUser){
			session.removeAttribute(Constants.CURRENT_USER);
			session.invalidate();
		}
		String logoutURL = authenticationService.getLogOutURL(request);
		return new ModelAndView(new RedirectView(logoutURL));
	}
}
