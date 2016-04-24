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

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.domain.institution.InstitutionHomeDiscover;
import net.duckling.dhome.service.IInstitutionHomeService;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 机构主页发现
 * @author lvly
 * @since 2012-9-25
 */
@Controller
@RequestMapping("system/discover/institute")
public class InstitutionDiscoverController {
	@Autowired
	private IInstitutionHomeService homeService;
	private static final String KEY_LASTEST = "lastest";
	private static final String KEY_PAPER_COUNT = "paperCount";
	private static final String KEY_MEMBER_COUNT = "memberCount";

	@RequestMapping
	public String display(HttpServletRequest request) {
		return "/institution/instituteDiscover";
	}

	@RequestMapping(value = "/getInstitute.json")
	@ResponseBody
	/**获得主页，要么根据最晚，要么论文数量，要么研究队伍数量排序
	 * @param keyword 查询的关键字
	 * @param offset 偏移量
	 * @param size 数量
	 * */
	public JSONObject getInstitutes(@RequestParam("keyWord") String keyWord, @RequestParam("offset") int offset,
			@RequestParam("size") int size) {
		JSONObject obj = new JSONObject();
		obj.put("keyWord", keyWord);
		List<InstitutionHomeDiscover> list= null;
		if( KEY_LASTEST.equals(keyWord)) {
			list = homeService.getInstitutionsByLastest(offset, size);
		}else if(KEY_PAPER_COUNT.equals(keyWord)){
			list=homeService.getInstitutionsByPaperCount(offset, size);
		}else if(KEY_MEMBER_COUNT.equals(keyWord)){
			list=homeService.getInstitutionsByMemberCount(offset, size);
		}
		obj.put("data", list);
		obj.put("size", list==null?0:list.size());
		return obj;
	}

}
