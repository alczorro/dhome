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
import net.duckling.dhome.common.config.AppConfig;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.PinyinUtil;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.common.util.UrlUtil;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionVmtService;
import net.duckling.dhome.service.IRegistService;
import net.duckling.dhome.service.IUserService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import cn.vlabs.umt.oauth.AccessToken;
import cn.vlabs.umt.oauth.Oauth;
import cn.vlabs.umt.oauth.UMTOauthConnectException;
import cn.vlabs.umt.oauth.UserInfo;
import cn.vlabs.umt.oauth.common.exception.OAuthProblemException;

/**
 * 登录controller
 * 
 * @author yangxiaopeng
 * @since 2012-8-24
 */
@Controller
@RequestMapping("/system/login")
public class LoginController {
	private static Logger LOGGER=Logger.getLogger(LoginController.class);
	private static final String MESSAGE = "message";

	@Autowired
	private IAuthenticationService authenticationService;
	@Autowired
	private IHomeService homeService;
	@Autowired
	private IRegistService registService;
	@Autowired
	private IUserService userService;
	@Autowired
	IInstitutionVmtService institutionVmtService;
	@Autowired
	private AppConfig appConfig;

	private String getViewUrl(HttpServletRequest request, String domain) {
		String redirectURL = CommonUtils.trim(request.getParameter("pageUrl"));
		String oauthUrl=request.getParameter("state");
		if(!CommonUtils.isNull(oauthUrl)){
			redirectURL=UrlUtil.decode(oauthUrl);
		}
		String view = (StringUtils.isEmpty(redirectURL)) ? "/people/" + domain
				+ "/admin/p/index" : redirectURL;
		String path = UrlUtil.getRootURL(request);
		view = path + view.replaceAll(request.getContextPath(), "");
		return view;
	}

	private ModelAndView redirect2CreateHomePage(String email,String trueName) {
		ModelAndView mv = new ModelAndView("createIndex");
		mv.addObject("emailToRegist", email);
		mv.addObject("zhName", trueName);
		mv.addObject("enName",upperFirstChar(PinyinUtil.getPinyinMingXing(trueName)));
		mv.addObject("rootDisciplines", registService.getRootDiscipline());
		mv.addObject("umtExist", true);
		mv.addObject("defaultDomain", PinyinUtil.getPinyinOnly(trueName)
				.replaceAll(" ", ""));
		return mv;
	}

	private ModelAndView showRegistDenied(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("key", "error.delete.user");
		mv.addObject("redirectURL", UrlUtil.getRootURL(request)
				+ "/system/logout");
		mv.addObject("urlDescription", "error.delete.url.description");
		return mv;
	}

	private String upperFirstChar(String enName) {
		String[] temp = enName.split(" ");
		int size = temp.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			String subStr = temp[i].substring(0, 1).toUpperCase()
					+ temp[i].substring(1, temp[i].length());
			sb.append(subStr + " ");
		}
		return sb.toString().trim();
	}

	/**
	 * 用户点击忘记密码后，跳转到UMT进行密码重置
	 * 
	 * @return
	 */
	@RequestMapping(params = "func=forgetPsw")
	public ModelAndView forgetPsw() {
		String getPswURL = authenticationService.getFindPasswordURL();
		return new ModelAndView(new RedirectView(getPswURL));
	}

	/**
	 * 首页中通过Ajax判断用户是否登录
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/isLogin.json")
	public String isLogin(HttpServletRequest request, Model model) {
		SimpleUser su = (SimpleUser) request.getSession().getAttribute(
				Constants.CURRENT_USER);
		model.addAttribute("isLogin", (null == su) ? false : true);
		return "jsontournamenttemplate";
	}
	private ModelAndView showLoginPage(String email,HttpServletRequest request){
		ModelAndView mv= new ModelAndView("login");
		mv.addObject(MESSAGE, "");
		mv.addObject("email",email);
		mv.addObject("state",request.getParameter("state"));
		return mv;
	}
	
	/**
	 * 用户登录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping
	public ModelAndView login(HttpServletRequest request) {
		SimpleUser user = SessionUtils.getUser(request);
		// 检查用户是否已登录
		if (SessionUtils.getUser(request) != null&&CommonUtils.isNull(request.getParameter("state"))) {
			return new ModelAndView(new RedirectView(request.getContextPath()+ "/people/" + SessionUtils.getDomain(request)));
		}
		Oauth oauth = new Oauth(appConfig.getOauthUmtProp());
		UserInfo userInfo=null;
		AccessToken token=null;
		try {
			if(!CommonUtils.isNull(request.getParameter("code"))){
				token = oauth.getAccessTokenByRequest(request);
				userInfo = token.getUserInfo();
			}
		} catch (UMTOauthConnectException e){
			LOGGER.error(e.getMessage(),e);
//			throw new RuntimeException(e.getMessage(),e);
			return showLoginPage("",request);
		} catch (OAuthProblemException e) {
			LOGGER.error(e.getMessage()+"["+e.getError()+":"+e.getDescription()+"]",e);
			throw new RuntimeException(e.getMessage(),e);
		}
		if(userInfo==null){
			return showLoginPage("",request);
		}
		// 查询用户信息
		SimpleUser su = userService.getSimpleUser(userInfo.getCstnetId());
		// 如果用户未注册，则让用户去注册
		if (null == su || su.getStep()==null) {
			return redirect2CreateHomePage(userInfo.getCstnetId(),userInfo.getTrueName());
		}
		// 如果用户注册未通过，显示提示信息
		if (su.isAuditDeleted()) {
			return showRegistDenied(request);
		}
		String domain = homeService.getDomain(su.getId());
		saveUserInfo2Session(request, su, domain);
		// 查看状态,如果没有走完流程，则跳
		if (su.registInProcess()) {
			return new ModelAndView(su.getStep());
		}
		return new ModelAndView(new RedirectView(getViewUrl(request, domain)));
	}

	private void saveUserInfo2Session(HttpServletRequest request, SimpleUser su,
			String domain) {
		
		//设置用户所属组织ID
		su.setInstitutionId(institutionVmtService.getInstituionId(su.getId()));
		
		HttpSession session = request.getSession();
		session.setAttribute(Constants.CURRENT_USER, su);
		session.setAttribute(Constants.CURRENT_USER_DOMAIN, domain);
	}
}