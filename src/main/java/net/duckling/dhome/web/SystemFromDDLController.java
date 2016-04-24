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
package net.duckling.dhome.web;

import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.config.AppConfig;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.OauthRequestUrlUtils;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IKeyValueService;
import net.duckling.dhome.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author lvly
 * @since 2012-11-22
 */
@Controller
@RequestMapping("/system/fromDDL")
public class SystemFromDDLController {
	@Autowired
	private IHomeService homeService;
	@Autowired
	private IUserService userService;
	@Autowired
	private AppConfig appConfig;
	@Autowired
	private IKeyValueService keyValueService;
	@RequestMapping
	public ModelAndView toHome(HttpServletRequest request,@RequestParam("email")String email)throws Exception{
		SimpleUser simpleUser=userService.getSimpleUser(email);
		if(simpleUser==null){
			return new ModelAndView(new RedirectView(request.getContextPath()+keyValueService.getValue(IKeyValueService.DEPLOY_SUBFIX)));
		}
		String returnUrl="";
		String domain=homeService.getDomain(simpleUser.getId());
		if(!CommonUtils.isNull(domain)){
			returnUrl="/people/"+domain;
		}
		if(SessionUtils.getUser(request)!=null){
				return new ModelAndView(new RedirectView(returnUrl));
		}else{
			return new ModelAndView(new RedirectView(OauthRequestUrlUtils.getUrl(appConfig)+"&state="+URLEncoder.encode(returnUrl,"utf-8")));
		}
	}
}
