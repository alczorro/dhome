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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.util.CharUtils;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.domain.institution.InstitutionHomeDiscover;
import net.duckling.dhome.domain.people.ComposedUser;
import net.duckling.dhome.domain.people.Education;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.Work;
import net.duckling.dhome.service.IComposedUserService;
import net.duckling.dhome.service.IEducationService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInterestService;
import net.duckling.dhome.service.IWorkService;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 发现主页的controller
 * 
 * @author zhaojuan
 * 
 */
@Controller
@RequestMapping("/system/discover")
public class DiscoverController {

	public void setInterestService(IInterestService interestService) {
		this.interestService = interestService;
	}

	public void setHomeService(IInstitutionHomeService homeService) {
		this.homeService = homeService;
	}

	public void setWorkService(IWorkService workService) {
		this.workService = workService;
	}

	public void setEduService(IEducationService eduService) {
		this.eduService = eduService;
	}

	private static final int INIT_SIZE_FOR_INDEX = 4;
	private static final String OFFSET = "offset";
	private static final String SIZE = "size";
	private static final String SPRING_JSON = "jsontournamenttemplate";

	private static final String USERS = "users";
	private static final String INSTITUTES = "institutes";
	private static final String INTEREST = "interest";
	private static final String KEYWORD = "keyword";
	private static final String ALL = "all";

	@Autowired
	private IComposedUserService composedUserService;
	@Autowired
	private IInstitutionHomeService homeService;
	@Autowired
	private IWorkService workService;
	@Autowired
	private IEducationService eduService;
	@Autowired
	private IInterestService interestService;

	public void setComposedUserService(IComposedUserService composedUserService) {
		this.composedUserService = composedUserService;
	}

	/**
	 * 显示所有用户
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping
	public ModelAndView showAllComposedUsers(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("discover");
		mv.addObject("discipline", composedUserService.getDiscipline());
		mv.addObject(INTEREST, interestService.getInterestAll());
		JSONObject obj = initData(request);
		mv.addObject("initData", obj);
		return mv;
	}

	/**
	 * 准备初始化数据，根据请求中是否包含keyword判断是否是查询
	 * 
	 * @param request
	 * @return
	 */
	private JSONObject initData(HttpServletRequest request) {
		JSONObject obj = new JSONObject();
		String keyword = CommonUtils.trim(request.getParameter(KEYWORD));
		String type = CommonUtils.trim(request.getParameter("type"));
		keyword = (null!=keyword)?keyword.trim():"";
		if (INTEREST.equals(type)) {
			obj.put("context", INTEREST);
			obj.put(KEYWORD, keyword);
		} else if (null == keyword || "".equals(keyword)) {
			obj.put("context", "all");
		} else {
			obj.put("context", "search");
			obj.put(KEYWORD, keyword);
		}
		return obj;
	}

	/**
	 * 获取所有用户列表
	 * 
	 * @param request
	 * @param model
	 * @param offset
	 *            偏移量
	 * @param size
	 *            用户数量
	 * @return
	 */
	@RequestMapping(value = "/all.json")
	public String all(HttpServletRequest request, Model model, @RequestParam(OFFSET) int offset,
			@RequestParam(SIZE) int size) {
		List<ComposedUser> comUsers = composedUserService.getAllComposedUsers(offset, size);
		JSONArray users = getJSON(comUsers, request);
		model.addAttribute(USERS, users);
		return SPRING_JSON;
	}

	@RequestMapping(value = "/interest.json")
	public String byInterest(HttpServletRequest request, Model model, @RequestParam(KEYWORD) String keyword,
			@RequestParam("offset") int offset, @RequestParam("size") int size) throws UnsupportedEncodingException {
		String decodeKeyword=URLDecoder.decode(keyword, "utf8");
		List<ComposedUser> comUsers = composedUserService.getComposedUsersByInterest(decodeKeyword, offset, size);
		JSONArray users = getJSON(comUsers, request);
		model.addAttribute(USERS, users);
		return SPRING_JSON;
	}

	/**
	 * 获取最新创建的用户列表
	 * 
	 * @param request
	 * @param model
	 * @param offset
	 * @param size
	 * @return
	 */
	@RequestMapping(value = "/latest.json")
	public String latest(HttpServletRequest request, Model model, @RequestParam(OFFSET) int offset,
			@RequestParam(SIZE) int size) {
		List<ComposedUser> comUsers = composedUserService.getLatestComposedUsers(offset, size);
		JSONArray users = getJSON(comUsers, request);
		model.addAttribute(USERS, users);
		return SPRING_JSON;
	}

	/**
	 * 根据学科显示对应的用户列表
	 * 
	 * @param request
	 * @param model
	 * @param first
	 *            第一学科
	 * @param second
	 *            第二学科
	 * @param offset
	 *            偏移量
	 * @param size
	 *            用户数量
	 * @return
	 */
	@RequestMapping(value = "/discipline.json")
	public String discipline(HttpServletRequest request, Model model, @RequestParam("first") int first,
			@RequestParam("second") int second, @RequestParam(OFFSET) int offset, @RequestParam(SIZE) int size) {
		List<ComposedUser> comUsers = composedUserService.getComposedUsersByDiscipline(first, second, offset, size);
		JSONArray users = getJSON(comUsers, request);
		model.addAttribute(USERS, users);
		return SPRING_JSON;
	}

	/** 跳转到搜索结果页面 */
	@RequestMapping(value = "search")
	public ModelAndView searchView(HttpServletRequest request, @RequestParam(KEYWORD) String keyword) {
		ModelAndView mv = new ModelAndView("search");
		mv.addObject(KEYWORD, CharUtils.replaceDangerouse(keyword));
		return mv;
	}

	/**
	 * 根据关键词查询指定用户
	 * 
	 * @param request
	 * @param model
	 * @param keyword
	 *            查询关键词
	 * @param offset
	 *            偏移量
	 * @param size
	 *            用户数量
	 * @return
	 */
	

	@RequestMapping(value = "/search.json")
	public String search(HttpServletRequest request, Model model, @RequestParam(KEYWORD) String keyword,
			@RequestParam(OFFSET) int offset, @RequestParam(SIZE) int size, @RequestParam("type") String type) {
		List<ComposedUser> comUsers = null;
		List<InstitutionHomeDiscover> institutes = null;
		int comUsersCount = 0;
		int institutesCount = 0;
		String tempKeyword = CharUtils.replaceDangerouse(keyword);
		if (ALL.equals(type)) {
			comUsersCount = composedUserService.getSearchComposedUsersCount(tempKeyword);
			institutesCount = homeService.getInstitutionsByKeywordCount(tempKeyword);
			comUsers = composedUserService.searchComposedUsers(tempKeyword, offset, size);
			institutes = homeService.getInstitutionsByKeyword(tempKeyword, offset, size);
		} else if (USERS.equals(type)) {
			comUsersCount = composedUserService.getSearchComposedUsersCount(tempKeyword);
			comUsers = composedUserService.searchComposedUsers(tempKeyword, offset, size);
		} else if (INSTITUTES.equals(type)) {
			institutesCount = homeService.getInstitutionsByKeywordCount(tempKeyword);
			institutes = homeService.getInstitutionsByKeyword(tempKeyword, offset, size);
		}

		JSONArray users = getJSON(comUsers, request);
		model.addAttribute(USERS, users);
		model.addAttribute("type", type);
		model.addAttribute(INSTITUTES, institutes);
		model.addAttribute("usersCount", comUsersCount);
		model.addAttribute("institutesCount", institutesCount);
		return SPRING_JSON;
	}

	private Object getNearestInstitution(int userId) {
		List<Work> works = workService.getWorksByUID(userId);

		if (!CommonUtils.isNull(works)) {
			return CommonUtils.first(works);
		} else {
			List<Education> edus = eduService.getEducationsByUid(userId);
			return CommonUtils.first(edus);
		}
	}

	/**
	 * 获取四个最新的用户
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/four.json")
	public String four(HttpServletRequest request, Model model) {
		List<ComposedUser> comUsers = composedUserService.getLatestComposedUsers(0, INIT_SIZE_FOR_INDEX);
		JSONArray users = getJSON(comUsers, request);
		model.addAttribute(USERS, users);
		return SPRING_JSON;
	}

	private JSONArray getJSON(List<ComposedUser> users, HttpServletRequest request) {
		JSONArray result = new JSONArray();
		if (null != users && !users.isEmpty()) {
			for (ComposedUser user : users) {
				JSONObject obj = new JSONObject();
				obj.put("imgURL", getImageURL(user.getSimpleUser(), request));
				obj.put("userPageURL", getUserPageURL(user, request));
				obj.put("zhName", user.getSimpleUser().getZhName());
				obj.put(INTEREST, user.getInterest());
				obj.put("institution", getNearestInstitution(user.getSimpleUser().getId()));
				obj.put("salutation", user.getSimpleUser().getSalutation());
				obj.put("uid", user.getSimpleUser().getId()); /**for img fadein **/
				result.add(obj);
			}
		}
		return result;
	}

	private String getImageURL(SimpleUser su, HttpServletRequest request) {
		int image = su.getImage();
		String path = (image == 0) ? "/resources/images/dhome-head.png?" : "/system/img?imgId=";
		return request.getContextPath() + path + image;
	}

	private String getUserPageURL(ComposedUser user, HttpServletRequest request) {
		return request.getContextPath() + "/people/" + user.getUrl();
	}
}
