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

import java.util.HashMap;
import java.util.Map;

import net.duckling.dhome.common.util.TranslateUtils;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IAccessLogService;
import net.duckling.dhome.service.IFriendService;
import net.duckling.dhome.service.IHomeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 相关人员
 * @author lvly
 * @since 2012-10-11
 */
@Controller
@RequestMapping("/people/{domain}")
public class FriendController {
	
	private static final int MAX_SIZE=20;
	private static final int INIT_OFFSET=0;
	@Autowired
	private IHomeService homeService;
	@Autowired
	private IFriendService friendService;
	@Autowired
	private IAccessLogService accessLogService;
	@RequestMapping("/friend.json")
	@ResponseBody
	/**
	 * 查找相关人物
	 * */
	public Map<String,Object> getFriend(@PathVariable("domain")String domain){
		SimpleUser user=homeService.getSimpleUserByDomain(domain);
		if(user==null){return null;}
		Map<String,Object> result=new HashMap<String,Object>(); 
		result.put("institutionFriends", TranslateUtils.translateList(friendService.getFriendsByInstitution(user.getId(), INIT_OFFSET, MAX_SIZE)));
		result.put("disciplineFriends", TranslateUtils.translateList(friendService.getFriendsByDiscipline(user.getId(),INIT_OFFSET,MAX_SIZE)));
		result.put("interestFriends", TranslateUtils.translateList(friendService.getFriendsByInterest(user.getId(), INIT_OFFSET, MAX_SIZE)));
		result.put("accessFriends", TranslateUtils.translateList(friendService.getFriendsByAccess(user.getId())));
		result.put("accessCount",accessLogService.getAccessLogCount(user.getId()));
		return result;
	}
	
	
	
}
