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

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
/**
 * 本来展示index用的，现在功能逐步挪到pagecontroller，
 * 逐渐废弃
 * 
 * */
@Controller
@RequestMapping("/people/{domain}")
public class HomeController {
	
	@Autowired
	private IUserService us;

	/** 跳转到主页面 */
	@RequestMapping
	public ModelAndView home(@PathVariable("domain") String domain, HttpServletRequest request) {
		HttpSession session=request.getSession();
		String fromRegist=CommonUtils.trim(request.getParameter("fromRegist"));
//		String flag=CommonUtils.trim(request.getParameter("isIAP"));
//		if(!CommonUtils.isNull(flag)&&Integer.parseInt(flag)==2){
//			//是否IAP论文库
//			session.removeAttribute("flag");
//			session.setAttribute("flag", 2);
//		}else{
//			session.removeAttribute("flag");
//			session.setAttribute("flag", 1);
//		}
		String param="";
		if(!CommonUtils.isNull(fromRegist)){
			param="?fromRegist="+fromRegist;
//			if(!CommonUtils.isNull(flag)){
//				param="&&flag="+flag;
//			}
		}
//		else{
//			if(!CommonUtils.isNull(flag)){
//				param="?flag="+flag;
//			}
//		}
		ModelAndView mv= new ModelAndView(new RedirectView("/people/"+domain+"/index.html"+param));
		SimpleUser user =SessionUtils.getUser(request);
		if(user!=null&&!CommonUtils.isNull(user.getStep())&&!"complete".equals(user.getStep())&&domain.equals(SessionUtils.getDomain(request))){
			return new ModelAndView(user.getStep());
		}
		return mv;
	}
	/**跳转到内容管理*/
	@RequestMapping(value = "/admin")
	@NPermission(authenticated = true)
	public ModelAndView homeAdmin(@PathVariable("domain") String domain, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView(new RedirectView("/people/"+domain+"/admin/p/index"));
		SimpleUser user = SessionUtils.getUser(request);
		if(user!=null&&!CommonUtils.isNull(user.getStep())&&!"complete".equals(user.getStep())&&user.getStep()!=null){
			return new ModelAndView(user.getStep());
		}
		return mv;
	}
	/**
	 * 提交审核
	 * @param request
	 * @param userId 用户ID
	 * @return 
	 */
	@RequestMapping(value="/submitAudit")
	@ResponseBody
	public boolean submitAudit(HttpServletRequest request,@PathVariable("domain") String domain){
		SimpleUser user=SessionUtils.getUser(request);
		if(user==null){
			return false;
		}
		user.setStatus(SimpleUser.STATUS_AUDIT_ING);
		user.setAuditPropose(null);
		us.updateSimpleUserStatusByUid(user);
		SessionUtils.updateUser(request, user);
		return true;
	}
}
