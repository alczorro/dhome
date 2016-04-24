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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.auth.sso.Constants;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.people.CustomPage;
import net.duckling.dhome.domain.people.Education;
import net.duckling.dhome.domain.people.Home;
import net.duckling.dhome.domain.people.MenuItem;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.Theme;
import net.duckling.dhome.domain.people.Work;
import net.duckling.dhome.service.IEducationService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IMenuItemService;
import net.duckling.dhome.service.IPageService;
import net.duckling.dhome.service.IThemeService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.IWorkService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 更换主题的controller
 * @author zhaojuan
 *
 */
@Controller

public class CustomThemeController {
	
	@Autowired
	private IPageService pageService;
	@Autowired
	private IHomeService homeService;
	@Autowired
	private IWorkService workService;
	@Autowired
	private IEducationService eduService;
	@Autowired
	private IMenuItemService menuItemService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IThemeService themeService;
	@Autowired
	private IInstitutionHomeService institutionHomeService;
	@Autowired
	private IInstitutionBackendService backEndService;
	
	@RequestMapping("/people/{domain}/admin/custom/theme")
	public ModelAndView show(@PathVariable("domain") String domain,
			HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("customTheme");
		String currentUserDomain = (String) request.getSession().getAttribute(Constants.CURRENT_USER_DOMAIN);
		Home home = homeService.getHomeByDomain(currentUserDomain);
		int choosedThemeId = home.getThemeid();
		if(choosedThemeId==0){
			choosedThemeId = Theme.THEME_M_NAMECRARD;
		}
		List<Theme> themes = themeService.getAllThemes();	
		mv.addObject("myThemeId", choosedThemeId);
		mv.addObject("themes", themes);
		SimpleUser user =  (SimpleUser) request.getSession().getAttribute(Constants.CURRENT_USER);
		//是否IAP用户
		String flag="";
		if(user.getInstitutionId()==null||user.getInstitutionId()==0){
			flag="noIAP";
		}else{
			if(backEndService.isMember(user.getInstitutionId(),user.getEmail())){
				flag="IAP";
			}else{
				flag="noIAP";
			}
		}
		mv.addObject("flag", flag);
		mv.addObject("titleUser", user);
		return mv;
		
	}
	/**
	 * 更新当前主题
	 * @param themeId
	 * @param request
	 * @return
	 */
	@RequestMapping("/system/custom/theme/changeTheme")
	@ResponseBody
	public  String changeTheme(@RequestParam Integer themeId,
			HttpServletRequest request) {
		String currentUserDomain = (String) request.getSession().getAttribute(Constants.CURRENT_USER_DOMAIN);
		Home home = homeService.getHomeByDomain(currentUserDomain);
		home.setThemeid(themeId);
		boolean result = homeService.updateHome(home);
		if(result){
			return "success";
		}
		return "failed";
		
	}
	
	@RequestMapping("/people/{domain}/admin/custom/theme/{themeId}")
	public ModelAndView showPage(@PathVariable("domain") String domain, @PathVariable("themeId") int themeId,
			HttpServletRequest request) {
		String url = getURI(request);
		CustomPage page = pageService.getPageByUrl(getGuestPageUrl(url));
		MenuItem item = menuItemService.getMenuItemByUrl(getGuestPageUrl(url));
		String status = "";
		if (item != null) {
			switch (item.getStatus()) {
			case MenuItem.MENU_ITEM_STATUS_HIDING:
				status = "hide";
				break;
			case MenuItem.MENU_ITEM_STATUS_SHOWING:
				status = "show";
				break;
			case MenuItem.MENU_ITEM_STATUS_REMOVED:
				status = "delete";
				break;
			default:
				break;
			}
		}
		SimpleUser user = homeService.getSimpleUserByDomain(domain);
		ModelAndView mv = new ModelAndView();
		mv.addObject("detailUser", userService.getDetailedUser(user.getId()));
		mv.addObject("status", status);
		String viewName = getTargetView(domain);
		mv.setViewName(viewName);
		mv.addObject("tab", "showPage");
		mv.addObject("page", page);
		mv.addObject("active", url);
		mv.addObject("isIndex", true);
		return addAttribute(mv, request, domain);

	}
	protected ModelAndView addAttribute(ModelAndView mv, HttpServletRequest request, String domain) {
		SimpleUser user = homeService.getSimpleUserByDomain(domain);
		mv.addObject("domain", domain);
		List<Work> works=workService.getWorksByUID(user.getId());
		mv.addObject("works", works);
		List<Education> edus= eduService.getEducationsByUid(user.getId());
		mv.addObject("edus",edus);
		String homeDomain="";
		if(!CommonUtils.isNull(works)){
			Work work=CommonUtils.first(works);
			homeDomain=getInstitutionHomeUrl(work.getInstitutionId());
			mv.addObject("nearestInstitution",CommonUtils.first(works));
		}else{
			Education edu=CommonUtils.first(edus);
			mv.addObject("nearestInstitution",CommonUtils.first(edus));
			homeDomain=getInstitutionHomeUrl(edu.getInsitutionId());
		}
		mv.addObject("homeDomain",homeDomain);
		mv.addObject("name", user.getZhName());
		mv.addObject("currentUser", user);
		return mv;
	}
	private String  getInstitutionHomeUrl(int institutionId){
		InstitutionHome home=institutionHomeService.getInstitutionByInstitutionId(institutionId);
		if(home!=null){
			return home.getDomain();
		}
		return null;
	}
	/**
	 * 跳转到游客浏览页面
	 * 
	 * @param str
	 *            /dhome/people/ceshijun/admin/p/test
	 * @return /dhome/people/ceshijun/test.html
	 * */
	private String getGuestPageUrl(String str) {
		String temp = str.replace("/admin/p", "");
		if (temp.endsWith("paper")) {
			temp += ".dhome";
		} else if (!temp.contains(".html")) {
			temp += ".html";
		}

		return temp;
	}
	/**
	 * 判断当前domain的主题，跳转到不同主题的jsp
	 * @param domain
	 * @return
	 */
	private String getTargetView(String domain) {
		Home home = homeService.getHomeByDomain(domain);
		int themeid = home.getThemeid();
		switch(themeid){
		case Theme.THEME_S_SIMPLE:return "theme_s_simple_index/browseIndex";
		case Theme.THEME_M_TRADITION:return "theme_m_tradition_index/browseIndex";
		case Theme.THEME_M_NAMECRARD:return "theme_m_namecard_index/browseIndex";
		
		}
		
		return "theme_s_simple_index/browseIndex";
	}
	private String  getURI(HttpServletRequest request){
		return request.getRequestURI().replace(request.getContextPath(), "");
	}
	public void setPageService(IPageService pageService) {
		this.pageService = pageService;
	}
	public void setHomeService(IHomeService homeService) {
		this.homeService = homeService;
	}
	public void setWorkService(IWorkService workService) {
		this.workService = workService;
	}
	public void setEduService(IEducationService eduService) {
		this.eduService = eduService;
	}
	public void setMenuItemService(IMenuItemService menuItemService) {
		this.menuItemService = menuItemService;
	}
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	public void setThemeService(IThemeService themeService) {
		this.themeService = themeService;
	}

}
