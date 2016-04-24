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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.auth.sso.IAuthenticationService;
import net.duckling.dhome.common.umt.UmtClient;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.people.Home;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.UrlMapping;
import net.duckling.dhome.service.IAccessLogService;
import net.duckling.dhome.service.IDomainService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IMessageBoardService;
import net.duckling.dhome.service.IRegistService;
import net.duckling.dhome.service.IUrlMappingService;
import net.duckling.dhome.service.IUserService;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 
 * 更新个人域名页面
 * @author lvly
 * @since 2012-8-28
 */
@Controller
@RequestMapping("/people/{domain}/admin")
public class SettingController {
	
	private static final String CHANGED = "changed";
	private static final String DOMAIN = "domain";
	private static final String CHANGED_URL="changedUrl";
	private static final String CHANGED_EMAIL="changedEmail";
	private static final String URL="url";
	
	@Autowired
	private IRegistService registService;
	@Autowired
	private IDomainService domainService;
	@Autowired
	private IUrlMappingService urlService;
	@Autowired
	private IHomeService homeService;
	@Autowired
	private IMessageBoardService msgBoardService;
	@Autowired
	private IAccessLogService accessLogService;
	@Autowired
	private IAuthenticationService authenticationService;
	@Autowired
	private IUserService userService;
//	@Autowired
//	private AuthorizationCodeServiceImpl authorizationService;
	
	
	/**显示更改domain页面*/
	@NPermission(authenticated = true)
	@RequestMapping(value="/domain")
	public ModelAndView showDomainEdit(HttpServletRequest request){
		ModelAndView mv=new ModelAndView("domainEdit");
		mv.addObject(CHANGED,CommonUtils.trim(request.getParameter(CHANGED)));
		mv.addObject(CHANGED_URL,CommonUtils.trim(request.getParameter(CHANGED_URL)));
		mv.addObject("urlMapping",urlService.getUrlMappingByUserId(SessionUtils.getUserId(request)));
		return mv;
	}
	/**显示更改语言页面*/
	@NPermission(authenticated = true)
	@RequestMapping(value="/language")
	public ModelAndView showLanguageEdit(HttpServletRequest request,@PathVariable(DOMAIN) String domain){
		ModelAndView mv= new ModelAndView("languageEdit");
		mv.addObject("language",homeService.getHomeByDomain(domain).getLanguage());
		mv.addObject(CHANGED,CommonUtils.trim(request.getParameter(CHANGED)));
		return mv;
	}
	/**显示更改登录邮箱页面*/
	@NPermission(authenticated = true)
	@RequestMapping(value="/loginEmail")
	public ModelAndView showLoginEmailEdit(HttpServletRequest request,@PathVariable(DOMAIN) String domain){
		SimpleUser user = SessionUtils.getUser(request);
		ModelAndView mv= new ModelAndView("loginEmailEdit");
		mv.addObject("user",user);
		mv.addObject(CHANGED_EMAIL,CommonUtils.trim(request.getParameter(CHANGED_EMAIL)));
		mv.addObject("message", request.getParameter("msg"));
		return mv;
	}
	/**
	 * 判定新邮箱是否被注册
	 * ajax
	 * */
	@NPermission(authenticated = true)
	@RequestMapping(value="/email/isEmailUsed")
	@ResponseBody
	public  boolean isEmailUsed(@RequestParam("email") String email,HttpServletRequest request){
		if(userService.isUmtUser(email)&&!registService.isEmailUsed(email)){
			return true;
		}else{
			return false;
		}
//		return !registService.isEmailUsed(email);
	}
	/**
	 * 判定登录密码是否正确
	 * ajax
	 * */
	@NPermission(authenticated = true)
	@RequestMapping(value="/email/isOldPassword")
	@ResponseBody
	public  boolean isOldPassword(@RequestParam("oldpassword") String oldPw,@RequestParam("email") String email,HttpServletRequest request){
//		if(SessionUtils.getDomain(request).equals(domain)){
//			return true;
//		}
		return authenticationService.login(email, oldPw);
	}
	/**
	 * 判定新邮箱密码是否正确
	 * ajax
	 * */
	@NPermission(authenticated = true)
	@RequestMapping(value="/email/isNewPassword")
	@ResponseBody
	public  boolean isNewPassword(@RequestParam("newpassword") String newPw,@RequestParam("email") String email,HttpServletRequest request){
//		if(SessionUtils.getDomain(request).equals(domain)){
//			return true;
//		}
//		JSONObject obj = new JSONObject();
//		try {
//			AccessToken token = authorizationService.umtPasswordAccessToken(email, newPw);
////			obj.put("accessToken", token.getAccessToken());
////			obj.put("refreshToken", token.getRefreshToken());
////			obj.put("expiresIn", token.getExpiresIn());
////			obj.put("DisplayName", token.getUserInfo().getTrueName());
////			String cstnetId = token.getUserInfo().getCstnetId();
////			obj.put("uid", cstnetId);
//			
//		} catch (OAuthProblemException e) {
//			String s = e.getDescription();
//			if("用户名或密码校验错误".equals(s)){
////				obj.put("message", "用户名或密码错误");
//				return false;
//			}else{
////				obj.put("message", e.getDescription());
//				return true;
//			}
//			
//		} 
		return authenticationService.login(email, newPw);
	}
	/**
	 * 判定domain是否被使用过
	 * ajax
	 * */
	@NPermission(authenticated = true)
	@RequestMapping(value="/domain/isDomainUsed")
	@ResponseBody
	public  boolean isDomainUsed(@RequestParam(DOMAIN) String domain,HttpServletRequest request){
		if(SessionUtils.getDomain(request).equals(domain)){
			return true;
		}
		return !registService.isDomainUsed(domain);
	}
	/**
	 * 判定urlMapping是否被使用过
	 * ajax
	 * */
	@NPermission(authenticated = true)
	@RequestMapping(value="/url/isUrlUsed")
	@ResponseBody
	public  boolean isUrlUsed(HttpServletRequest request,@RequestParam(URL) String url){
		return !urlService.isUrlUsed(url,SessionUtils.getUserId(request));
	}
	/**更改个人域名
	 * */
	@NPermission(authenticated = true)
	@RequestMapping("/domain/submit")
	public ModelAndView submitDomain(HttpServletRequest request,@RequestParam(DOMAIN) String domain,@RequestParam("oldDomain") String oldDomain){
		int uid=SessionUtils.getUserId(request);
		domainService.updateDomain(oldDomain, domain, uid);
		SessionUtils.setDomain(request, domain);
		//更新冗余数据
		msgBoardService.updateUserInfo(uid, 0, null,domain);
		accessLogService.updateAccessLog(uid, domain);
		
		return new ModelAndView(new RedirectView(return2Request(request).replace(oldDomain, domain)+"?changed=true"));
	}
	@NPermission(authenticated = true)
	@RequestMapping("/url/submit")
	public ModelAndView submitUrl(HttpServletRequest request,@PathVariable(DOMAIN) String domain,@RequestParam(URL) String url){
		String checkStatus="on".equals(CommonUtils.trim(request.getParameter("status")))?UrlMapping.STATUS_VALID:UrlMapping.STATUS_INVALID;
		String urlId=request.getParameter("urlId");
		if(CommonUtils.isNull(urlId)){
			urlService.addUrlMapping(url,domain,SessionUtils.getUserId(request),checkStatus);
		}else{
			urlService.updateUrlMapping(url,domain,SessionUtils.getUserId(request),checkStatus,Integer.valueOf(urlId));
		}
		String rUrl=return2Request(request);
		rUrl=rUrl.replace(URL, DOMAIN);
		return new ModelAndView(new RedirectView(rUrl+"?changedUrl=true"));
	}
	@NPermission(authenticated = true)
	@RequestMapping("/loginEmail/submit")
	public ModelAndView submitEmail(HttpServletRequest request,@PathVariable(DOMAIN) String domain,@RequestParam("newEmail") String newEmail){
//		int uid=SessionUtils.getUserId(request);
		String oldPw = CommonUtils.trim(request.getParameter("oldpassword"));
		String newPw = CommonUtils.trim(request.getParameter("newpassword"));
		SimpleUser user = SessionUtils.getUser(request);
		if(authenticationService.login(user.getEmail(), oldPw)&&isEmailUsed(newEmail,request)&&authenticationService.login(newEmail, newPw)){
			user.setEmail(newEmail);
//			userService.updateSimpleUserEmailByUid(uid, newEmail);
			
			userService.updateSimpleUserByUid(user);
			String rUrl=return2Request(request);
			return new ModelAndView(new RedirectView(rUrl+"?changedEmail=true"));
		}else{
			String rUrl=return2Request(request);
			String msg;
			try {
				msg = URLEncoder.encode("邮箱更改失败，请检查输入内容是否正确！", "utf-8");
				return new ModelAndView(new RedirectView(rUrl+"?msg="+msg));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}
	/**更改语言*/
	@NPermission(authenticated = true)
	@RequestMapping("/language/submit")
	public ModelAndView submitLanguage(HttpServletRequest request,@RequestParam("language") String language,@PathVariable(DOMAIN) String domain){
		ModelAndView mv=new ModelAndView(new RedirectView(return2Request(request)+"?changed=true"));
		Home home=homeService.getHomeByDomain(domain);
		home.setLanguage(language);
		homeService.updateHome(home);
		return mv;
	}
	/**跳转到原来发请求的页面*/
	private String return2Request(HttpServletRequest request){
		String url=request.getRequestURI().replace(request.getContextPath(), "");
		return url.substring(0,url.lastIndexOf('/'));
	}
}
