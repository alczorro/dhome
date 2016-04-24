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

import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.PinyinUtil;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.people.CustomPage;
import net.duckling.dhome.domain.people.Education;
import net.duckling.dhome.domain.people.Home;
import net.duckling.dhome.domain.people.MenuItem;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.Work;
import net.duckling.dhome.service.IEducationService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IMenuItemService;
import net.duckling.dhome.service.IPageService;
import net.duckling.dhome.service.IPaperService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.IWorkService;
import net.duckling.dhome.web.helper.PageCharHelper;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 编辑页面的controller
 * @author lvly
 * @since 2012-10-30
 */
@Controller
public class EditPageController {

	private static final String DOMAIN ="domain";
	private static final String PAGE_NAME = "pageName";
	private static final String HTML_SUFFIX = ".html";
	private static final String PEOPLE = "/people/";
	private static final int FIVE_PAPER_FOR_HOME = 5;
	
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
	private IPaperService paperService;
	@Autowired
	private IInstitutionHomeService institutionHomeService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IInstitutionBackendService backEndService;
	@Autowired
	private IInstitutionHomeService insHomeService;
	
	/** 
	 * 编辑页面 
	 * @param request
	 * @param domain
	 * @param pid
	 * @return
	 */
	@NPermission(authenticated = true)
	@RequestMapping(value = "/people/{domain}/admin/p/{pageName}", params = { "func=edit" })
	public ModelAndView editPage(HttpServletRequest request, @PathVariable(DOMAIN) String domain,
			@RequestParam("pid") int pid) {
		SimpleUser user = SessionUtils.getUser(request);
		InstitutionHome home=insHomeService.getInstitutionByDomain(domain);
//		if(home==null){
//			return null;
//		}
		String flag="error";
		if(user.getInstitutionId()!=null){
			if(!backEndService.isMember(user.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
				flag="success";
			}
		}
		
		CustomPage page = pageService.getPage(pid);
		MenuItem item = menuItemService.getMenuItemByUrl(page.getUrl());
		ModelAndView mv = new ModelAndView("adminIndex");
		mv.addObject("tab", "editPage");
		mv.addObject("page", page);
		mv.addObject("item", item);
		mv.addObject("newPage",!CommonUtils.isNull(request.getParameter("newPage")));
		mv.addObject("titleUser", homeService.getSimpleUserByDomain(domain));
		mv.addObject("active", getURI(request));
		mv.addObject("flag", flag);
		return mv;
	}
	@NPermission(authenticated = true)
	@RequestMapping("/people/{domain}/admin/p/getPinYin")
	@ResponseBody
	public String getPinYin(@PathVariable(DOMAIN) String domain, HttpServletRequest request){
		String title=CommonUtils.killNull(request.getParameter("title"));
		String pid=CommonUtils.killNull(request.getParameter("pid"));
		String pageName=PinyinUtil.getShortPinyin(title);
		if(pageService.isUrlUsed(domain, pageName, -1)){
			return pageName+pid;
		}
		else{
			return pageName;
		}
	}
	
	/** 准备新页面 */
	@NPermission(authenticated = true)
	@RequestMapping("/people/{domain}/admin/p/newPage")
	public ModelAndView newPage(@PathVariable(DOMAIN) String domain, HttpServletRequest request) {
		String title = CommonUtils.killNull(request.getParameter("title"));
		//现在只有空白页面只有这玩意
		String defaultMenuItemName=CommonUtils.killNull(request.getParameter("defaultMenuItemName"));
		String defaultPageName=CommonUtils.killNull(request.getParameter("defaultPageName"));
		MenuItem item = new MenuItem();
		item.setStatus(CommonUtils.isNull(defaultMenuItemName)?MenuItem.MENU_ITEM_STATUS_SHOWING:MenuItem.MENU_ITEM_STATUS_HIDING);
		item.setTitle(CommonUtils.isNull(defaultMenuItemName)?title:defaultMenuItemName);
		item.setUid(SessionUtils.getUserId(request));
		item.setId(menuItemService.addMenuItem(item));
		String defaultUrl = PageCharHelper.getEnOnly(CommonUtils.isNull(defaultPageName)?PinyinUtil.getShortPinyin(title):defaultPageName);
		if (pageService.isUrlUsed(domain, defaultUrl, -1)||CommonUtils.isNull(defaultUrl)) {
			defaultUrl += item.getId();
		}
		String pageUrl = PEOPLE + homeService.getDomain(item.getUid()) + "/" + defaultUrl + HTML_SUFFIX;
		item.setUrl(pageUrl);
		menuItemService.updateMenuItem(item);
		CustomPage page = new CustomPage();
		page.setUid(SessionUtils.getUserId(request));
		page.setTitle(title);
		page.setUrl(item.getUrl());
		page.setKeyWord(defaultUrl);
		page.setId(pageService.createPage(page));
		return new ModelAndView(new RedirectView(PEOPLE+domain+"/admin/p/"+defaultUrl+"?func=edit&pid="+page.getId()+"&newPage=true"));
	}
	
	/** 更改发布状态 */
	@NPermission(authenticated = true)
	@RequestMapping("/people/{domain}/admin/p/{pageName}/changeStatus")
	public ModelAndView changeStatus(HttpServletRequest request) {
		String url = getURI(request);
		url = url.substring(0, url.lastIndexOf('/'));
		MenuItem item = menuItemService.getMenuItemByUrl(getGuestPageUrl(url));
		if (MenuItem.MENU_ITEM_STATUS_HIDING == item.getStatus()) {
			item.setStatus(MenuItem.MENU_ITEM_STATUS_SHOWING);
		} else if (MenuItem.MENU_ITEM_STATUS_SHOWING == item.getStatus()) {
			item.setStatus(MenuItem.MENU_ITEM_STATUS_HIDING);
		}
		menuItemService.updateMenuItem(item);
		return new ModelAndView(new RedirectView(url));
	}
	/** 删除页面！ */
	@NPermission(authenticated = true)
	@RequestMapping("/people/{domain}/admin/p/{pageName}/deletePage")
	public ModelAndView deleteStatus(HttpServletRequest request, @PathVariable(PAGE_NAME) String pageName) {
		String url = getURI(request);
		url = url.substring(0, url.lastIndexOf('/'));
		MenuItem item = menuItemService.getMenuItemByUrl(getGuestPageUrl(url));
		item.setStatus(MenuItem.MENU_ITEM_STATUS_REMOVED);
		userService.updateSimpleUserLastEditTimeByUid(SessionUtils.getUserId(request));
		menuItemService.updateMenuItem(item);
		return new ModelAndView(new RedirectView(url.replace(pageName, "index")));
	}

	/** 新页面创建好了！ 提交！ */
	@NPermission(authenticated = true)
	@RequestMapping("/people/{domain}/admin/p/addPage")
	public ModelAndView submitPage(HttpServletRequest request) {
		String title = CommonUtils.trim(request.getParameter("title"));
		String content = CommonUtils.trim(request.getParameter("content"));
		if(!CommonUtils.isNull(content)){
			content=PageCharHelper.deleteEnter(content);
		}
		String notRealease = CommonUtils.trim(request.getParameter("notRealease"));
		String itemId = CommonUtils.trim(request.getParameter("itemId"));
		String pid = CommonUtils.trim(request.getParameter("pid"));
		int uid = SessionUtils.getUserId(request);
		userService.updateSimpleUserLastEditTimeByUid(uid);
		MenuItem menuItem = menuItemService.getMenuItemById(Integer.valueOf(itemId));
		menuItem.setTitle(title);
		menuItem.setUid(uid);
		if(!menuItem.getUrl().endsWith("index.html")){
			menuItem.setUrl(PEOPLE + homeService.getDomain(menuItem.getUid()) + "/" + CommonUtils.trim(request.getParameter("pageUrl"))
					+ HTML_SUFFIX);
		}
		if ("on".equals(notRealease)) {
			menuItem.setStatus(MenuItem.MENU_ITEM_STATUS_HIDING);
		} else {
			menuItem.setStatus(MenuItem.MENU_ITEM_STATUS_SHOWING);
		}
		menuItemService.updateMenuItem(menuItem);

		CustomPage page = pageService.getPage(Integer.valueOf(pid));
		page.setUid(uid);
		page.setTitle(title);
		page.setContent(content);
		page.setUrl(menuItem.getUrl());
		page.setKeyWord(CommonUtils.trim(request.getParameter("pageUrl")));
		pageService.updatePage(page);

		return new ModelAndView(new RedirectView(getAdminPageUrl(menuItem.getUrl())));
	}
	@NPermission(authenticated = true)
	@RequestMapping("/people/{domain}/admin/p/{pageName}/cancelPage")
	public ModelAndView cancelPage(HttpServletRequest request,@RequestParam("isNew") boolean isNew,@PathVariable(DOMAIN) String domain,@PathVariable(PAGE_NAME) String pageName) {
		String url = getURI(request);
		url = url.substring(0, url.lastIndexOf('/'));
		if(isNew){
			return deleteStatus(request, pageName);
		}
		return new ModelAndView(new RedirectView(url));
	}
	protected ModelAndView addAttribute(ModelAndView mv, HttpServletRequest request, String domain) {
		SimpleUser user = homeService.getSimpleUserByDomain(domain);
		mv.addObject(DOMAIN, domain);
		List<Work> works = workService.getWorksByUID(user.getId());
		mv.addObject("works", works);
		List<Education> edus = eduService.getEducationsByUid(user.getId());
		mv.addObject("edus", edus);
		String homeDomain="";
		if(!CommonUtils.isNull(works)){
			Work work=CommonUtils.first(works);
			homeDomain=getInstitutionHomeDomain(work.getInstitutionId());
			mv.addObject("nearestInstitution",CommonUtils.first(works));
		}else if(!CommonUtils.isNull(edus)){
			Education edu=CommonUtils.first(edus);
			mv.addObject("nearestInstitution",CommonUtils.first(edus));
			homeDomain=getInstitutionHomeDomain(edu.getInsitutionId());
		}
		mv.addObject("homeDomain",homeDomain);
		mv.addObject("name", user.getZhName());
		mv.addObject("titleUser", user);
		mv.addObject("fivePapers", paperService.getPapers(user.getId(), 0, FIVE_PAPER_FOR_HOME));
		mv.addObject("totalPaperCount", paperService.getPaperCount(user.getId()));
		return mv;
	}
	private String  getInstitutionHomeDomain(int institutionId){
		//此处添加institutionId>0的条件是因为逻辑更改后，查询insId=0的机构主页会返回某个莫名其妙的机构，而不是返回空
		if(institutionId>0){
			InstitutionHome home=institutionHomeService.getInstitutionByInstitutionId(institutionId);
			if(home!=null){
				return home.getDomain();
			}
		}
		return null;
	}

	/**
	 * 跳转到主人更改页面
	 * 
	 * @param str
	 *            /dhome/people/ceshijun/test.html
	 * @return /dhome/people/ceshijun/admin/p/test
	 * */
	private String getAdminPageUrl(String str) {
		int index = str.lastIndexOf('/');
		return str.substring(0, index) + "/admin/p" + str.substring(index).replace(HTML_SUFFIX, "");
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
		temp=temp.contains(";")?temp.split(";")[0]:temp;
		if (temp.endsWith("paper")) {
			temp += ".dhome";
		} else if(temp.endsWith("msgboard")){
			temp += ".dhome";
		}else if (!temp.contains(HTML_SUFFIX)) {
			temp += HTML_SUFFIX;
		}

		return temp;
	}

	private String getURI(HttpServletRequest request) {
		return request.getRequestURI().replace(request.getContextPath(), "");
	}
}
