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
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IDsnSearchService;
import net.duckling.dhome.service.IInstitutionBackendPaperService;
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
@RequestMapping("/system/searchPapers")
public class SearchPapersController {

	@Autowired
	private IDsnSearchService dsnSearchService;
	@Autowired
	private IPaperService pService;
	@Autowired
	private IInstitutionBackendPaperService paperService;
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
		SimpleUser user = SessionUtils.getUser(request);
		List<Integer> existPaperIds = paperService.getExistPaperIds(user.getId());
		List<Long> existDsnIds = pService.getExistDsnPaperIds(user.getId());
		List<InstitutionPaper> list = null;
		JSONArray array2 = null;
		if(CommonUtils.isNull(keyword)){
			array2 = dsnSearchService.initQuery(user.getId(), offset, size, existDsnIds);
			list = paperService.getPaperByInit(offset, size, CommonUtils.trim(user.getZhName()));
			keyword = user.getZhName();
			List<InstitutionPaper> listAll= paperService.getPaperByInit(0, 9999, CommonUtils.trim(user.getZhName()));
			array2 = filterRepeat(listAll,array2);
		}else{
			array2 = dsnSearchService.query(user.getId(), keyword, offset, size, existDsnIds);
			list = paperService.getPaperByKeyword(offset, size, CommonUtils.trim(keyword));
			List<InstitutionPaper> listAll=paperService.getPaperByKeyword(0, 9999, CommonUtils.trim(keyword));
			array2 = filterRepeat(listAll,array2);
		}
		for (InstitutionPaper institutionPaper : list) {
			institutionPaper.setPublicationName(paperService.getPubsById(institutionPaper.getPublicationId()).getPubName());
		}
		JSONArray array =new JSONArray();
		array.addAll(list);
//		JSONArray array = filter(gson.toJson(list), request);
		array = filter(array,request);
		array = filterExist(array,existPaperIds);
		array2 = filter2(array2,request);
		
		model.addAttribute("result", array);
		model.addAttribute("result2", array2);
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
			InstitutionPaper obj = (InstitutionPaper) array.get(i);
			int paperId = obj.getId();
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
	/**
	 * 过滤掉页面中已经选中的commonPaper
	 * @param array 查询到的commonPaper信息
	 * @param request 参数中包含了existCommonPapers，即为需要过滤的Paper ID
	 * @return 已经过滤后的commonPaper信息
	 */
	private JSONArray filter2(JSONArray array, HttpServletRequest request){
		String existCommonPapers = CommonUtils.trim(request.getParameter("existCommonPapers"));
		if(null == existCommonPapers || "".equals(existCommonPapers)){
			return array;
		}
		JSONArray result = new JSONArray();
		String[] temp = existCommonPapers.split(",");
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
	/**
	 * 过滤掉和iap库中重复的论文
	 * @param array
	 * @param request
	 * @return
	 */
	private JSONArray filterRepeat(List<InstitutionPaper> list,JSONArray array2){
		JSONArray result = new JSONArray();
		int size = array2.size();
		for(int i=0; i<size; i++){
			JSONObject obj = (JSONObject)array2.get(i);
			String title = (String)obj.get(DsnClient.TITLE);
			boolean exist = false;
			for(int j=0;j<list.size();j++){
				InstitutionPaper paper = (InstitutionPaper) list.get(j);
				if(!CommonUtils.isNull(paper) && title.equals(paper.getTitle())){
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
	/**
	 * 过滤掉页面中已经添加的Paper
	 * @param result 查询到的Paper信息
	 * @param existIds 查询到的已经存在的id
	 * @return 已经过滤后的Paper信息
	 */
	private JSONArray filterExist(JSONArray result, List<Integer> existIds) {
		if (null == result || result.size() <= 0 || null == existIds || existIds.isEmpty()) {
			return result;
		}
		int size = result.size();
		JSONArray array = new JSONArray();
		for (int i = 0; i < size; i++) {
			InstitutionPaper obj = (InstitutionPaper) result.get(i);
			int paperId = obj.getId();
			if (!existIds.contains(paperId)) {
				array.add(obj);
			}
		}
		return array;
	}
}
