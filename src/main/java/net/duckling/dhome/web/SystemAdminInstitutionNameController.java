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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.PinyinUtil;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.common.util.UrlUtil;
import net.duckling.dhome.domain.institution.Institution;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.object.InstitutionName;
import net.duckling.dhome.domain.people.Education;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.Work;
import net.duckling.dhome.service.IEducationService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionPeopleService;
import net.duckling.dhome.service.IWorkService;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
/**
 * 管理机构名称，主要用于管理员创建新机构，将用户填写的机构名和官方机构相关联
 * 需要管理员权限才能访问，具体权限控制在过滤器中
 * @author Yangxp
 * @since 2012-10-15
 */
@Controller
@RequestMapping("/system/admin/name")
public class SystemAdminInstitutionNameController {
	
	private static final Logger LOG = Logger.getLogger(SystemAdminInstitutionNameController.class);
	
	@Autowired
	private IWorkService workService;
	@Autowired
	private IEducationService eduService;
	@Autowired
	private IInstitutionHomeService homeService;
	@Autowired
	private IInstitutionPeopleService peopleService;
	
	/**
	 * 显示管理页面
	 * @param request
	 * @return
	 */
	@RequestMapping
	public ModelAndView display(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("institution/systemAdminName");
		List<Work> zeroWorks = workService.getWorkWithZeroInstitutionId();
		List<Work> nonZeroWorks = workService.getWorkWithNonZeroInstitutionId();
		List<Education> zeroEdus = eduService.getEducationWithZeroInstitutionId();
		List<Education> nonZeroEdus = eduService.getEducationWithNonZeroInstitutionId();
		List<InstitutionName> zeroName = mergeWorkAndEducation(zeroWorks, zeroEdus);
		List<InstitutionName> nonZeroName = mergeWorkAndEducation(nonZeroWorks, nonZeroEdus);
		mv.addObject("zeroNames", getJSONInstitutionName(zeroName));
		mv.addObject("nonZeroNames", getJSONInstitutionName(nonZeroName));
		return mv;
	}
	
	/**
	 * 搜索机构
	 * @param request
	 * @param insName 搜索关键词
	 * @return
	 */
	@RequestMapping(params = "func=search")
	@ResponseBody
	public JSONArray search(HttpServletRequest request, @RequestParam("insName")String insName){
		List<Institution> inss = homeService.searchForInstitutionBySimilarName(insName);
		return getJSONInstitution(inss, request);
	}
	
	/**
	 * 将用户输入的机构名称与官方机构名称关联起来
	 * @param request
	 * @param sourceId 工作经历或者教育背景记录的ID
	 * @param sourceType 类型为work或education
	 * @param sourceInsId 原来关联的机构ID
	 * @param targetInsId 将要关联的机构ID
	 * @return
	 */
	@RequestMapping(params = "func=referTo")
	@ResponseBody
	public JSONObject referTo(HttpServletRequest request, @RequestParam("sourceId") String sourceId,
			@RequestParam("sourceType")String sourceType, @RequestParam("sourceInsId")Integer sourceInsId,
			@RequestParam("targetInsId")Integer targetInsId){
		JSONObject obj = new JSONObject();
		obj.put("sourceId", sourceId);
		obj.put("sourceType", sourceType);
		obj.put("targetInsId", targetInsId);
		String[] sourceIds = sourceId.split(",");
		String[] sourceTypes = sourceType.split(",");
		if(sourceIds.length == sourceTypes.length && targetInsId > 0){
			for(int i=0; i<sourceIds.length; i++){
				makeReference(sourceIds[i], sourceTypes[i], sourceInsId, targetInsId);
			}
			obj.put("status", "success");
		}else{
			obj.put("status", "fail");
		}
		return obj;
	}
	
	/**
	 * 显示新建机构页面
	 * @param requst
	 * @param insName
	 * @return
	 */
	@RequestMapping(params = "func=new")
	public ModelAndView createNewInstitution(HttpServletRequest requst, @RequestParam("insName")String insName){
		ModelAndView mv = new ModelAndView("institution/newInstitutionHome");
		mv.addObject("institutionName", insName);
		mv.addObject("institutionDomain", PinyinUtil.getShortPinyin(insName));
		return mv;
	}
	/**
	 * 检查机构域名是否可用
	 * @param request
	 * @param domain
	 * @return
	 */
	@RequestMapping(params = "func=checkDomain")
	@ResponseBody
	public boolean checkDomain(HttpServletRequest request, @RequestParam("domain")String domain){
		return !homeService.isValidHome(domain);
	}
	/**
	 * 检查机构名是否重名
	 * @param request
	 * @param name
	 * @return 重名则返回false,不重名返回true
	 */
	@RequestMapping(params = "func=checkName")
	@ResponseBody
	public boolean checkName(HttpServletRequest request, @RequestParam("name")String name){
		boolean result = true;
		String tempName = name.trim();
		int insId = workService.searchOfficalInstitution(tempName);
		if(insId>0){
			InstitutionHome home = homeService.getInstitutionByInstitutionId(insId);
			result = (null != home)?false:true;
		}
		return result;
	}
	/**
	 * 创建新机构
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "func=submitNew")
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public ModelAndView submitNew(HttpServletRequest request){
		String name = CommonUtils.trim(request.getParameter("institutionName"));
		String introduction = CommonUtils.trim(request.getParameter("institutionIntroduction"));
		String domain = CommonUtils.trim(request.getParameter("institutionDomain"));
		SimpleUser su = SessionUtils.getUser(request);
		Institution ins = workService.getInstitutionByName(name);
		int insId = (null != ins)? ins.getId():workService.createInstitution(name);
		createAndUpdateInstitutionHome(insId, su.getId(), introduction, domain);
		homeService.createAliasInstitutionName(name, insId, true);
		return new ModelAndView(new RedirectView(UrlUtil.getRootURL(request)+"/system/admin/name"));
	}
	/**
	 * 合并工作经历和教育背景中的机构信息
	 * @param works
	 * @param edus
	 * @return
	 */
	private List<InstitutionName> mergeWorkAndEducation(List<Work> works, List<Education> edus){
		List<InstitutionName> result = new ArrayList<InstitutionName>();
		if(null != works && !works.isEmpty()){
			for(Work work : works){
				int insId = work.getInstitutionId();
				result.add(InstitutionName.build(work.getId(),
						InstitutionName.WORK, work.getAliasInstitutionName(), 
						insId, homeService.getInstitutionById(insId).getZhName()));
			}
		}
		if(null != edus && !edus.isEmpty()){
			for(Education edu : edus){
				int insId = edu.getInsitutionId();
				result.add(InstitutionName.build(edu.getId(),
						InstitutionName.EDUCATION, edu.getAliasInstitutionName(), insId,
						homeService.getInstitutionById(insId).getZhName()));
			}
		}
		return result;
	}
	
	private JSONArray getJSONInstitutionName(List<InstitutionName> names){
		JSONArray array = new JSONArray();
		if(null != names && !names.isEmpty()){
			Map<String, Integer> existMap = new HashMap<String, Integer>();
			for(InstitutionName name : names){
				addToArray(array, name, existMap);
			}
		}
		return array;
	}
	
	/**
	 * 添加到JSONArray并去重
	 * @param array 结果数组
	 * @param name 待添加的InstitutionName对象
	 * @param existMap 已添加的Map【key=机构名_机构ID,value=array中的索引】
	 */
	private void addToArray(JSONArray array, InstitutionName name, Map<String, Integer> existMap){
		String insName = name.getInstitutionName();
		if(null != insName){
			insName = insName.toLowerCase();
		}
		int insId = name.getInstitutionId();
		String key = insName+"_"+insId;
		if(existMap.containsKey(key)){
			Integer index = existMap.get(key);
			JSONObject obj = (JSONObject)array.get(index);
			obj.put("id", obj.get("id")+","+name.getSourceId());
			obj.put("type", obj.get("type")+","+name.getSourceType());
		}else{
			JSONObject obj = new JSONObject();
			obj.put("id", name.getSourceId());
			obj.put("name", name.getInstitutionName());
			obj.put("officalName", name.getOfficalInstitutionName());
			obj.put("insId", name.getInstitutionId());
			obj.put("type", name.getSourceType());
			array.add(obj);
			existMap.put(key, array.size()-1);
		}
	}
	
	private JSONArray getJSONInstitution(List<Institution> inss, HttpServletRequest request){
		JSONArray array = new JSONArray();
		if(null != inss && !inss.isEmpty()){
			for(Institution ins : inss){
				JSONObject obj = new JSONObject();
				obj.put("id", ins.getId());
				obj.put("name", ins.getZhName());
				InstitutionHome home = homeService.getInstitutionByInstitutionId(ins.getId());
				String url = (null!=home)?UrlUtil.getRootURL(request)+"/institution/"+home.getDomain()+"/index.html":"";
				obj.put("url", url);
				array.add(obj);
			}
		}
		return array;
	}

	/**
	 * 创建机构主页并更新机构简介和域名
	 * @param insId 机构ID
	 * @param uid 创建者ID
	 * @param introduction 机构简介
	 * @param domain 机构域名
	 */
	private void createAndUpdateInstitutionHome(int insId, int uid, String introduction, String domain){
		homeService.createInstitutionHomeForAdmin(uid,homeService.getInstitutionById(insId));
		InstitutionHome home = homeService.getInstitutionByInstitutionId(insId);
		home.setIntroduction(introduction);
		home.setDomain(domain);
		homeService.updateInstitutionHome(home);
	}
	/**
	 * 将原纪录类型(sourceType)为工作经历(work)或教育背景(education)的记录(记录ID为sourceId)，
	 * 关联到新的官方机构(targetId)上，并自动取消与原来关联的机构(sourceInsId)的任何关系
	 * @param sourceId 工作经历或教育背景的ID
	 * @param sourceType 类型为：work(工作经历)/education(教育背景)
	 * @param sourceInsId 原先关联的官方机构ID
	 * @param targetId 将要关联的官方机构ID
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED)
	private void makeReference(String sourceId, String sourceType, int sourceInsId, int targetId){
		int id = Integer.valueOf(sourceId);
		if(InstitutionName.WORK.equals(sourceType)){
			Work work = workService.getWork(id);
			work.setInstitutionId(targetId);
			peopleService.deleteMember(work.getUid(), sourceInsId);
			peopleService.addMember(work.getUid(), targetId);
			workService.updateWorkById(work);
			homeService.createAliasInstitutionName(work.getAliasInstitutionName(), targetId, false);
		}else if(InstitutionName.EDUCATION.equals(sourceType)){
			Education edu = eduService.getEducation(id);
			edu.setInsitutionId(targetId);
			peopleService.deleteMember(edu.getUid(), sourceInsId);
			peopleService.addMember(edu.getUid(), targetId);
			eduService.updateEducationById(edu);
			homeService.createAliasInstitutionName(edu.getAliasInstitutionName(), targetId, false);
		}else{
			LOG.info("Unknown institution type while admin reference! sourceId="+sourceId+" and sourceType="+sourceType);
		}
		
	}
}
