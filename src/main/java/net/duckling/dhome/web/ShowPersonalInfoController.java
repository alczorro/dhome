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

import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.object.FavoriteUrl;
import net.duckling.dhome.domain.people.DetailedUser;
import net.duckling.dhome.domain.people.Education;
import net.duckling.dhome.domain.people.Interest;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.Work;
import net.duckling.dhome.service.IEducationService;
import net.duckling.dhome.service.IFavoriteUrlService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInterestService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.IWorkService;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 修改个人资料初始化页面
 * 
 * @author yangxiaopeng
 * */
@Controller
@RequestMapping("/people/{domain}/admin/personal")
public class ShowPersonalInfoController {
	@Autowired
	private IUserService userService;
	@Autowired
	private IWorkService workService;
	@Autowired
	private IEducationService eduService;
	@Autowired
	private IHomeService homeService;
	@Autowired
	private IInterestService interestService;
	@Autowired
	private IFavoriteUrlService favService;
	
	private static final String TITLE_USER = "titleUser";
	private static final String BANNER = "banner";

	/**
	 * 默认跳转到显示个人基本信息
	 * @param request
	 * @return
	 */
	@NPermission(authenticated = true)
	@RequestMapping
	public ModelAndView display(HttpServletRequest request) {
		SimpleUser su = SessionUtils.getUser(request);
		String domain = homeService.getDomain(su.getId());
		String url = "/people/"+domain+"/admin/personal/baseinfo";
		ModelAndView mv = new ModelAndView(new RedirectView(url));
		mv.addObject(TITLE_USER, su.getZhName());
		return mv;
	}
	/**
	 * 显示个人基本信息
	 * @param request
	 * @return
	 */
	@NPermission(authenticated = true)
	@RequestMapping(value = "/baseinfo")
	public ModelAndView baseinfo(HttpServletRequest request) {
		SimpleUser su = userService.getSimpleUserByUid(SessionUtils.getUserId(request));
		ModelAndView mv = new ModelAndView("personalBaseInfo");
		if (su != null) {
			if (!CommonUtils.isNull(su.getStep()) && !"complete".equals(su.getStep()) && su.getStep() != null) {
				return new ModelAndView(su.getStep());
			}

			DetailedUser du = userService.getDetailedUser(su.getId());
			String domain = homeService.getDomain(su.getId());
			JSONObject user = new JSONObject();
			user.put("zhName", su.getZhName());
			user.put("enName", su.getEnName());
			user.put("email", su.getEmail());
			user.put("firstClassDiscipline", du.getFirstClassDiscipline());
			user.put("firstClassDisciplineName", userService.getDisciplineName(du.getFirstClassDiscipline()));
			user.put("secondClassDiscipline", du.getSecondClassDiscipline());
			user.put("secondClassDisciplineName", userService.getDisciplineName(du.getSecondClassDiscipline()));
			user.put("introduction", du.getIntroduction());
			user.put("url", domain);
			user.put("salutation", su.getSalutation());
			mv.addObject("user", user);
			mv.addObject("banner", "1");
			mv.addObject("firstClass", userService.getRootDiscipline());
			mv.addObject("secondClass", userService.getChildDiscipline(du.getFirstClassDiscipline()));
			mv.addObject("titleUser", su.getZhName());
			List<Interest> interests = interestService.getInterest(su.getId());
			mv.addObject("interests", formatInterestKeyword(interests));
			mv.addObject("interestIds", formatInterestId(interests));
		}
		return mv;
	}
	/**
	 * 显示工作经历
	 * @param request
	 * @return
	 */
	@NPermission(authenticated = true)
	@RequestMapping(value = "/work")
	public ModelAndView displayWork(HttpServletRequest request) {
		SimpleUser su = userService.getSimpleUserByUid(SessionUtils.getUserId(request));
		List<Work> works = workService.getWorksByUID(su.getId());
		ModelAndView mv = new ModelAndView("personalWorkInfo");
		mv.addObject("works", works);
		mv.addObject(BANNER, "1");
		mv.addObject(TITLE_USER, su.getZhName());
		return mv;
	}
	/**
	 * 显示教育背景
	 * @param request
	 * @return
	 */
	@NPermission(authenticated = true)
	@RequestMapping(value = "/education")
	public ModelAndView displayEdu(HttpServletRequest request) {
		SimpleUser su = SessionUtils.getUser(request);
		List<Education> edus = eduService.getEducationsByUid(su.getId());
		ModelAndView mv = new ModelAndView("personalEducationInfo");
		mv.addObject("educations", edus);
		mv.addObject(BANNER, "1");
		mv.addObject(TITLE_USER, su.getZhName());
		return mv;
	}
	/**
	 * 显示社交媒体
	 * @param request
	 * @return
	 */
	@NPermission(authenticated = true)
	@RequestMapping(value = "/favorite")
	public ModelAndView displayFavorite(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("personalFavoriteInfo");
		mv.addObject("urls",favService.getFavoritesByUid(SessionUtils.getUserId(request)));
		mv.addObject(TITLE_USER,SessionUtils.getUser(request).getZhName());
		return mv;
	}
	/**
	 * 获取单个社交媒体的实例，用于 编辑
	 * @param request
	 * */
	@NPermission(authenticated = true)
	@RequestMapping(value = "/favorite.json")
	@ResponseBody
	public FavoriteUrl getFavUrlById(@RequestParam("id") int id){
		return favService.getFavoriteUrlById(id);
	}
	/**
	 * 显示图片
	 * @param request
	 * @return
	 */
	@NPermission(authenticated = true)
	@RequestMapping(value = "/photo")
	public ModelAndView displayPhoto(HttpServletRequest request) {
		SimpleUser su = SessionUtils.getUser(request);
		String fileName = "";
		String filePath = "";
		if (su.getImage() != 0) {
			fileName = "/system/img?imgId=" + su.getImage();
		} else {
			fileName = "/resources/images/dhome-head.png";
		}
		filePath = request.getContextPath();
		ModelAndView mv = new ModelAndView();
		mv.setViewName("personalPhotoInfo");
		mv.addObject("fileName", fileName);
		mv.addObject("filePath", filePath);
		mv.addObject(BANNER, "1");
		mv.addObject(TITLE_USER, su.getZhName());
		mv.addObject("isSaved", request.getParameter("isSaved"));
		return mv;
	}
	
	private String formatInterestKeyword(List<Interest> interests){
		StringBuilder sb = new StringBuilder();
		if(null != interests && !interests.isEmpty()){
			for(Interest interest : interests){
				sb.append(interest.getKeyword()+", ");
			}
			sb.replace(sb.lastIndexOf(","), sb.length(), "");
		}
		return sb.toString();
	}
	
	private String formatInterestId(List<Interest> interests){
		StringBuilder sb = new StringBuilder();
		if(null != interests && !interests.isEmpty()){
			for(Interest interest : interests){
				sb.append(interest.getId()+", ");
			}
			sb.replace(sb.lastIndexOf(","), sb.length(), "");
		}
		return sb.toString();
	}
}
