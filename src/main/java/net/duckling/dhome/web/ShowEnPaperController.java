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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.duckling.dhome.common.auth.sso.Constants;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.PaperRender;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.people.Education;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.Work;
import net.duckling.dhome.service.IDsnSearchService;
import net.duckling.dhome.service.IEducationService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendPaperService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInterestService;
import net.duckling.dhome.service.IPaperService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.IWorkService;
import net.duckling.dhome.web.helper.ThemeHelper;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
/**
 * 游客状态下显示论文列表
 * @author Yangxp
 * @date 2012-08-30
 */
@Controller
@RequestMapping("/people/{domain}/en_paper.dhome")
public class ShowEnPaperController {

	@Autowired
	private IPaperService paperService;
	@Autowired
	private IHomeService homeService;
	@Autowired
	private IDsnSearchService dsnService;
	@Autowired
	private IWorkService workService;
	@Autowired
	private IEducationService eduService;
	@Autowired
	private IInstitutionHomeService institutionHomeService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IInterestService interestService;
	@Autowired
	private ThemeHelper helper;
	@Autowired
	private IInstitutionBackendPaperService backendPaperService;
	
	/**
	 * 显示论文
	 * @param request
	 * @param domain
	 * @return
	 */
	@RequestMapping
	public ModelAndView display(HttpServletRequest request, @PathVariable("domain") String domain) {
		SimpleUser targetUser = homeService.getSimpleUserByDomain(domain);
		HttpSession session = request.getSession();
		if(null != targetUser){
			JSONArray papers = PaperRender.format(paperService.getEnPapers(targetUser.getId(), 0, 0), dsnService.getUserNameString(targetUser.getId()));
			ModelAndView mv = new ModelAndView();
			String viewName = helper.getTargetView(domain,"browsePaper");
			mv.setViewName(viewName);
			mv.addObject("isSelf",SessionUtils.isSameUser(request, targetUser));
			mv.addObject("papers", papers);
			mv.addObject("IAPPaper",backendPaperService.getEnPapersByUID(targetUser.getId()));
			List<Integer> paperId=CommonUtils.extractSthField(backendPaperService.getPapersByUID(targetUser.getId()),"id");
			if(!CommonUtils.isNull(paperId)){
				Map<Integer,List<InstitutionAuthor>> authorMap=backendPaperService.getListAuthorsMap(paperId);
				mv.addObject("authorMap",authorMap);
			}
			mv.addObject("titleUser",targetUser);
			mv.addObject("name", targetUser.getZhName());
			mv.addObject("active",request.getRequestURI().replace(request.getContextPath(), ""));
			addCommonLeftDataForSteadyTheme(mv,viewName, domain);
			return mv;
		}else{
			SimpleUser curUser = (SimpleUser)session.getAttribute(Constants.CURRENT_USER);
			String mydomain = (String)session.getAttribute(Constants.CURRENT_USER_DOMAIN);
			ModelAndView mv = new ModelAndView("error");
			mv.addObject("message", "您查看的页面未找到！");
			String redirectURL = (null != curUser && null!=mydomain)?"people/"+mydomain+"/admin/p/index":"/login";
			String urlDescription = (null != curUser && null!=mydomain)?"个人首页":"登录页面";
			mv.addObject("redirectURL", redirectURL);
			mv.addObject("urlDescription", urlDescription);
			return mv;
		}
	}
	/**
	 * 判断当前domain的主题，跳转到不同主题的jsp
	 * @param domain
	 * @return
	 */
	
	
	private void addCommonLeftDataForSteadyTheme(ModelAndView mv, String viewName, String domain){
//		if(null == viewName || !"theme_m_steady_index/browsePaper".equals(viewName)){
//			return;
//		}
		SimpleUser user = homeService.getSimpleUserByDomain(domain);
		mv.addObject("domain", domain);
		List<Work> works = workService.getWorksByUID(user.getId());
		List<Education> edus = eduService.getEducationsByUid(user.getId());
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
		mv.addObject("interest",interestService.getInterest(user.getId()));
		mv.addObject("detailUser", userService.getDetailedUser(user.getId()));
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
}
