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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.DefaultValuePageUtils;
import net.duckling.dhome.common.util.UrlUtil;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IMailService;
import net.duckling.dhome.service.IUserService;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
/**
 * 系统管理页面
 * @author Yangxp
 *
 */
@Controller
@RequestMapping("/system/admin")
public class SystemAdminUserController {
	
	private static final String SPRING_JSON = "jsontournamenttemplate";
	private static final String STATUS = "status";
	
	@Autowired
	private IUserService us;
	@Autowired
	private IHomeService hs;
	@Autowired
	private IMailService ms;
	
	/**
	 * 显示系统管理页面
	 * @param request
	 * @return
	 */
	@RequestMapping
	public ModelAndView display(HttpServletRequest request){
		return new ModelAndView("systemAdmin");
	}
	/**
	 * 根据关键词、审核状态查询用户主页信息
	 * @param request
	 * @param model
	 * @param offset 偏移量
	 * @param size 数量
	 * @return
	 */
	@RequestMapping(value="/getUsers.json")
	public String all(HttpServletRequest request, Model model,
			@RequestParam("offset") int offset, @RequestParam("size") int size){
		String status = CommonUtils.trim(request.getParameter(STATUS));
		String keyword = CommonUtils.trim(request.getParameter("keyword"));
		List<SimpleUser> users = null;
		Map<Integer, String> domains = null;
		int offsetTemp = offset;
		JSONArray array = new JSONArray();
		do{
			users = us.getAllUsers(status, keyword, offsetTemp, size);
			domains = hs.getDomainByUID(extractUID(users));
			offsetTemp += size;
			array.addAll(getJSONUsers(request, users, domains));
		}while(!users.isEmpty() && array.size()<size);//此处循环是因为，数据库存在很多垃圾数据，导致用户没有域名
		model.addAttribute("users", array.toString());
		model.addAttribute("actualOffset", offsetTemp-offset);
		return SPRING_JSON;
	}
	/**
	 * 审核用户主页
	 * @param request
	 * @param model
	 * @param id 用户ID
	 * @param status 变更后的审核状态
	 * @param reason 变更理由
	 * @return
	 */
	@RequestMapping(value="/check.json")
	public String check(HttpServletRequest request, Model model, @RequestParam("id") int id,
			@RequestParam(STATUS) String status){
		SimpleUser su = us.getSimpleUserByUid(id);
		String reason = CommonUtils.trim(request.getParameter("reason"));
		reason = (null == reason || "".equals(reason))?"无":reason;
		su.setAuditPropose(reason);
		su.setStatus(status);
		us.updateSimpleUserStatusByUid(su);
		if(SimpleUser.STATUS_AUDIT_OK.equals(status)){
			ms.sendCheckHomePageMail(DefaultValuePageUtils.CHECK_HOME_PAGE_SUCCESS_TEMP, su, hs.getDomain(id));
		}else{
			ms.sendCheckHomePageMail(DefaultValuePageUtils.CHECK_HOME_PAGE_FAIL_TEMP, su, hs.getDomain(id));
		}
		model.addAttribute("id", id);
		model.addAttribute(STATUS, status);
		model.addAttribute("name", su.getZhName());
		model.addAttribute("reason", reason);
		return SPRING_JSON;
	}
	
	private List<Integer> extractUID(List<SimpleUser> users){
		List<Integer> uids = new ArrayList<Integer>();
		if(null != users){
			for(SimpleUser su : users){
				uids.add(su.getId());
			}
		}
		return uids;
	}
	
	private JSONArray getJSONUsers(HttpServletRequest request, List<SimpleUser> users,
			Map<Integer, String> domains){
		JSONArray array = new JSONArray();
		if(null != users){
			for(SimpleUser su : users){
				int uid = su.getId();
				String domain = domains.get(Integer.valueOf(uid));
				JSONObject obj = new JSONObject();
				obj.put("id", uid);
				String domainStr = (null == domain)?"无域名":domain;
				obj.put("domain", domainStr);
				obj.put("email", su.getEmail());
				String url = (null == domain)?"":getHomePageURL(request, domain);
				obj.put("url", url);
				obj.put("title", su.getZhName());
				obj.put(STATUS, su.getStatus());
				obj.put("reason", (null == su.getAuditPropose())?"无":su.getAuditPropose());
				array.add(obj);
			}
		}
		return array;
	}
	
	private String getHomePageURL(HttpServletRequest request,String domain){
		return UrlUtil.getRootURL(request)+"/people/"+domain+"/index.html";
	}
	
}
