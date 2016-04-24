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

import net.duckling.dhome.domain.people.Dictionary;
import net.duckling.dhome.service.IInterestService;
import net.duckling.dhome.service.IRegistService;
import net.duckling.dhome.service.IUserService;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 主要负责index页面，加载数据,无关于登陆用户
 * @author lvly
 * @since 2012-8-29
 */
@Controller
@RequestMapping("/system/index")
public class IndexController {
	@Autowired
	private IUserService userService;
	@Autowired
	private IInterestService interestService;
	@Autowired
	private IRegistService registService;
	
	/**
	 * 获取用户总数量
	 * @return
	 */
	@RequestMapping("/homeCount")
	@ResponseBody
	public  int getHomeCount(){
		return userService.getUserCount();
	}
	@RequestMapping("/isUmtUser")
	@ResponseBody
	public  boolean isUmtUser(@RequestParam("email") String email){
		return userService.isUmtUser(email);
	}
	@RequestMapping("/isDhomeUser")
	@ResponseBody
	public boolean isDhomeUser(@RequestParam("email") String email){
		return !registService.isEmailUsed(email);
	}
	@RequestMapping("/help.html")
	public String toHelp(){
		return "dhomeHelp";
	}
	
	
	@RequestMapping("/interest")
	@ResponseBody
	public JSONArray getInterest(@RequestParam("keyword") String keyword){
		JSONArray array = new JSONArray();
		List<Dictionary> interests = interestService.searchInterest(keyword);
		if(null != interests && !interests.isEmpty()){
			for(Dictionary interest : interests){
				JSONObject obj = new JSONObject();
				obj.put("id", interest.getId());
				obj.put("name", interest.getKeyWord());
				array.add(obj);
			}
		}
		return array;
	}
}
