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

import net.duckling.dhome.common.dsn.DsnClient;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IDsnSearchService;
import net.duckling.dhome.service.IPaperService;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * 论文搜索
 * @author Yangxp
 * @date 2012-08-01
 */
@Controller
@RequestMapping("/system/searchPaper")
public class SearchPaperController {

	@Autowired
	private IDsnSearchService dsnSearchService;
	@Autowired
	private IPaperService paperService;
	/**
	 * 搜索论文。若包含keyword，则按照关键词去DSN搜索；若不包含
	 * 则使用用户中文名，英文名，拼音进行搜索
	 * @param request 请求
	 * @param model 模型
	 * @param offset 本次查询的偏移量
	 * @param size 本次查询获取的结果集大小
	 * @return
	 */
	@RequestMapping(value="/paper.json")
	public String search(HttpServletRequest request, Model model,
			@RequestParam("offset") int offset, @RequestParam("size") int size){
		String keyword = CommonUtils.trim(request.getParameter("keyword"));
		SimpleUser su = SessionUtils.getUser(request);
		List<Long> existDsnIds = paperService.getExistDsnPaperIds(su.getId());
		JSONArray array = null;
		if(CommonUtils.isNull(keyword)){
			array = dsnSearchService.initQuery(su.getId(), offset, size, existDsnIds);
			keyword = dsnSearchService.getUserNameString(su.getId());
		}else{
			array = dsnSearchService.query(su.getId(), keyword, offset, size, existDsnIds);
		}
		array = filter(array, request);
		model.addAttribute("result", array);
		model.addAttribute("searchKeywords", keyword);
		return "jsontournamenttemplate";
	}
	
	/**
	 * 过滤掉页面中已经选中的Paper
	 * @param array 查询到的Paper信息
	 * @param request 参数中包含了existPapers，即为需要过滤的Paper ID
	 * @return 已经过滤后的Paper信息
	 */
	private JSONArray filter(JSONArray array, HttpServletRequest request){
		String existPapers = CommonUtils.trim(request.getParameter("existPapers"));
		if(null == existPapers || "".equals(existPapers)){
			return array;
		}
		JSONArray result = new JSONArray();
		String[] temp = existPapers.split(",");
		int size = array.size();
		for(int i=0; i<size; i++){
			JSONObject obj = (JSONObject)array.get(i);
			long paperId = (Long)obj.get(DsnClient.DSN_PAPER_ID);
			boolean exist = false;
			for(String paper: temp){
				if(!CommonUtils.isNull(paper) && paperId == Integer.parseInt(paper)){
					exist = true;
					break;
				}
			}
			if(!exist){
				result.add(obj);
			}
		}
		return result;
	}
}
