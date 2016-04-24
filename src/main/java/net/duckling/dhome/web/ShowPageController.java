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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.DateUtils;
import net.duckling.dhome.common.util.PaperRender;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.people.CustomPage;
import net.duckling.dhome.domain.people.Education;
import net.duckling.dhome.domain.people.Home;
import net.duckling.dhome.domain.people.MenuItem;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.Theme;
import net.duckling.dhome.domain.people.UserSetting;
import net.duckling.dhome.domain.people.Work;
import net.duckling.dhome.service.IAccessLogService;
import net.duckling.dhome.service.IDsnSearchService;
import net.duckling.dhome.service.IEducationService;
import net.duckling.dhome.service.IFavoriteUrlService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendPaperService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInterestService;
import net.duckling.dhome.service.IMenuItemService;
import net.duckling.dhome.service.IPageService;
import net.duckling.dhome.service.IPaperService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.IUserSettingService;
import net.duckling.dhome.service.IWorkService;
import net.duckling.dhome.web.helper.MessageBoardHelper;
import net.duckling.dhome.web.helper.PageHelper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.cnic.cerc.dlog.client.WebLog;

/**
 * 展示页面的controller
 * @author lvly
 * @since 2012-10-30
 */
@Controller
public class ShowPageController {

	private static final String DOMAIN ="domain";
	private static final String PAGE_NAME = "pageName";
	private static final String HTML_SUFFIX = ".html";
	private static final String PEOPLE = "/people/";
	private static final int FIVE_PAPER_FOR_HOME = 5;
	private static int flag = 1;
	
	@Autowired
	private IInterestService interestService;
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
	private IPaperService paperService;
	@Autowired
	private IInstitutionHomeService institutionHomeService;
	@Autowired
	private IDsnSearchService dsnService;
	@Autowired
	private IAccessLogService logService;
	@Autowired
	private IFavoriteUrlService favService;
	@Autowired
	private IUserSettingService settingService;
	@Autowired
	private MessageBoardHelper boardHelper;
	@Autowired
	private IInstitutionBackendPaperService backendPaperService;
	/** 
	 * 验证URL是否被用
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("/people/{domain}/isUrlUsed.json")
	@ResponseBody
	public boolean isUrlUsed(@PathVariable(DOMAIN) String domain,
			@RequestParam("pageUrl") String keyWord,
			@RequestParam("pid")int pid,
			HttpServletRequest request) {
		return !pageService.isUrlUsed(domain, keyWord, pid);
	}
	/** 
	 * admin页面展示
	 * @param domain
	 * @param pageName
	 * @param request
	 * @return
	 */
	@NPermission(authenticated = true)
	@RequestMapping(value = "/people/{domain}/admin/p/{pageName}")
	public ModelAndView showAdminPage(@PathVariable(DOMAIN) String domain, @PathVariable(PAGE_NAME) String pageName,
			HttpServletRequest request) {
		SimpleUser user = homeService.getSimpleUserByDomain(domain);
		if(!SessionUtils.isSameUser(request,user)&&!PageHelper.adminCanRead(user)&&!SessionUtils.isAdminUser(request)){
			return new ModelAndView("pageAuditing");
		}
		ModelAndView mv = showPage(domain, pageName,request);
		if (mv.getViewName().equals("pageNotFound")) {
			return mv;
		}
		mv.setViewName("adminIndex");
		return addAttribute(mv, request, domain);

	}
	/** 
	 * 游客页面展示
	 * @param domain
	 * @param pageName
	 * @param request
	 * @return
	 */
	@WebLog(method="showPage",params="pageName,domain")
	@RequestMapping("/people/{domain}/{pageName}.html")
	public ModelAndView showPage(@PathVariable(DOMAIN) String domain, @PathVariable(PAGE_NAME) String pageName, 
			HttpServletRequest request) {
		SimpleUser user = homeService.getSimpleUserByDomain(domain);
//		String isIAP = CommonUtils.trim(request.getParameter("flag"));
//		if(isIAP!=null && !"".equals(isIAP)){
//			flag = Integer.parseInt(isIAP);
//		}
		//判断该用户是否做的迁移
		int isMove=userService.getSimpleUser(user.getId()).getIsMove();
		if(isMove==1){
			flag=2;
		}else{
			flag=1;
		}
		boolean isSelf=SessionUtils.isSameUser(request,user);
		if(!isSelf&&!PageHelper.guestCanRead(user)&&!SessionUtils.isAdminUser(request)){
			return new ModelAndView("pageAuditing");
		}
		String url = getURI(request);
		CustomPage page = pageService.getPageByUrl(getGuestPageUrl(url));
		if (page == null) {
			return new ModelAndView("pageNotFound");
		}
		MenuItem item = menuItemService.getMenuItemByUrl(getGuestPageUrl(url));
		if (isGuestURI(request) && item.getStatus() != MenuItem.MENU_ITEM_STATUS_SHOWING) {
			return new ModelAndView("pageNotFound");
		}
		logService.addAccessLog(SessionUtils.getUserId(request), user.getId(), SessionUtils.getDomain(request),getRemoteIp(request));
		ModelAndView mv = createModelAndView(domain);
		mv.addObject("status", generateStatus(item));
		mv.addObject("page", page);
		mv.addObject("flag", flag);
		mv.addObject("active", url);
		mv.addObject("accessCount", logService.getAccessLogCount(user.getId()));
		mv.addObject("fromRegist",CommonUtils.trim(request.getParameter("fromRegist")));
		mv.addObject("lastEditTime",DateUtils.getDateStr(user.getLastEditTime()));
		String commentType=request.getParameter("type");
		boolean needHash=!CommonUtils.isNull(commentType);
		if(isSelf){
			commentType="history";
		}
		mv.addObject("type",commentType);
		mv.addObject("commentId",request.getParameter("commentId"));
		if ("index".equals(pageName)) {
			String status = generateStatus(menuItemService.getMenuItemByUrl(getGuestPageUrl(PEOPLE+domain+"/paper")));
			mv.addObject("paperStatus", status);
			mv.addObject("isIndex", true);
			mv.addObject("urls",favService.getFavoritesByUid(user.getId()));
			
			SimpleUser currUser=SessionUtils.getUser(request);
			UserSetting userSetting=settingService.getSetting(user.getId(), UserSetting.KEY_MSG_BOARD_KEY);
			mv.addObject("msgBoardSetting",userSetting);
			mv.addObject("titleUser",user);
			if("history".equals(commentType)){
				mv.addObject("msgs",boardHelper.removeOthers(user, currUser, userSetting));
			}
		}
		mv.addObject("needHash",needHash);
		mv.addObject("isSelf",isSelf);
		mv.addObject("IAPPaper",backendPaperService.getPapersByUID(user.getId()));
		List<Integer> paperId=CommonUtils.extractSthField(backendPaperService.getPapersByUID(user.getId()),"id");
		if(!CommonUtils.isNull(paperId)){
			Map<Integer,List<InstitutionAuthor>> authorMap=backendPaperService.getListAuthorsMap(paperId);
			mv.addObject("authorMap",authorMap);
		}
		return addAttribute(mv, request, domain);

	}
	private String getRemoteIp(HttpServletRequest request){
		String nginxFowardIp=request.getHeader("x-forwarded-for");
		if (nginxFowardIp == null) {  
			 return request.getRemoteAddr();  
		}  
		return nginxFowardIp;  
	}
	private ModelAndView createModelAndView(String domain){
		SimpleUser user = homeService.getSimpleUserByDomain(domain);
		ModelAndView mv = new ModelAndView();
		mv.addObject("detailUser", userService.getDetailedUser(user.getId()));
		
		/**取得当前home的主题 */
		Home home = homeService.getHomeByDomain(domain);
		int themeid = home.getThemeid();		
		String viewName = getTargetView(themeid);
		mv.setViewName(viewName);
		if(themeid==Theme.THEME_S_SIMPLE||themeid==Theme.THEME_S_FASHION){
			List<CustomPage> pages = pageService.getValidPagesByUid(user.getId());
			JSONArray papers = PaperRender.format(paperService.getPapers(user.getId(), 0, 0), dsnService.getUserNameString(user.getId()));
			mv.addObject("pages", pages);
			mv.addObject("papers", papers);
		}
		
		mv.addObject("interest",interestService.getInterest(user.getId()));
		mv.addObject("tab", "showPage");
		mv.addObject("titleUser", user);
		return mv;
	}
	private String generateStatus(MenuItem item){
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
		return status;
	}
	/**
	 * 判断当前domain的主题，跳转到不同主题的jsp
	 * 
	 * @param domain
	 * @return
	 */
	private String getTargetView(int themeid) {
		
		switch (themeid) {
		case Theme.THEME_S_SIMPLE:
			return "theme_s_simple_index/browseIndex";
		case Theme.THEME_M_TRADITION:
			return "theme_m_tradition_index/browseIndex";
		case Theme.THEME_M_NAMECRARD:
			return "theme_m_namecard_index/browseIndex";
		case Theme.THEME_M_TREND:
			return "theme_m_trend_index/browseIndex";
		case Theme.THEME_M_STEADY:
			return "theme_m_steady_index/browseIndex";
		case Theme.THEME_S_FASHION:
		    return "theme_s_fashion_index/browseIndex";
		case Theme.THEME_M_IAP:
			return "theme_m_iap_index/browseIndex";
		case Theme.THEME_M_IAP_SCIENCE:
			return "theme_m_iap_science_index/browseIndex";
		}

		return "theme_s_simple_index/browseIndex";
	}
	/**
	 * 处理前台获得工作经历的ajax请求
	 * 
	 * @param request
	 * @param pageUrl
	 * @return
	 */
	@RequestMapping("/people/{domain}/admin/p/getEdus.json")
	@ResponseBody
	public List<Education> getEdus(HttpServletRequest request) {
		return eduService.getEducationsByUid(SessionUtils.getUserId(request));

	}
	/**
	 * 处理前台获得工作经历的ajax请求
	 * 
	 * @param request
	 * @param pageUrl
	 * @return
	 */
	@RequestMapping("/people/{domain}/admin/p/getWorks.json")
	@ResponseBody
	public List<Work> getWorks(HttpServletRequest request) {
		return workService.getWorksByUID(SessionUtils.getUserId(request));

	}
	@RequestMapping("/people/{domain}/admin/p/getStep.json")
	@ResponseBody
	public JSONObject getStep(HttpServletRequest request, @RequestParam("module") String module){
		int uid = SessionUtils.getUserId(request);
		int step = userService.getStep(uid, module);
		if(step<=0){
			userService.create(uid, module, 1);
			step = 1;
		}
		JSONObject obj = new JSONObject();
		obj.put("step", step);
		return obj;
	}

	@RequestMapping("/people/{domain}/admin/p/updateStep.json")
	@ResponseBody
	public JSONObject updateStep(HttpServletRequest request, @RequestParam("module") String module,
			@RequestParam("step") Integer step){
		int uid = SessionUtils.getUserId(request);
		userService.updateStep(uid, module, (step<=0)?1:step);
		JSONObject obj = new JSONObject();
		obj.put("status", "success");
		return obj;
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
		mv.addObject("fivePapers",PaperRender.format(paperService.getPapers(user.getId(), 0, FIVE_PAPER_FOR_HOME), dsnService.getUserNameString(user.getId())));
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
		} else if (!temp.contains(HTML_SUFFIX)) {
			temp += HTML_SUFFIX;
		}

		return temp;
	}

	private String getURI(HttpServletRequest request) {
		return request.getRequestURI().replace(request.getContextPath(), "");
	}

	private boolean isGuestURI(HttpServletRequest request) {
		return !getURI(request).contains("/admin/p/");
	}

}
