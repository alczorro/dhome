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
package net.duckling.dhome.web.institution.backend;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionOption;
import net.duckling.dhome.domain.institution.InstitutionOptionVal;
import net.duckling.dhome.domain.institution.InstitutionTopic;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.JsonResult;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionBackendTopicService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionOptionValService;
import net.duckling.dhome.service.IUserService;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@NPermission(role = "admin", authenticated=true)
@RequestMapping("/institution/{domain}/backend/topic")

public class InstitutionBackEndTopicController {
	private static final Logger LOG=Logger.getLogger(InstitutionBackEndTopicController.class);
	
	@Autowired
	private IInstitutionBackendTopicService backEndTopicService;
	@Autowired
	private IInstitutionHomeService homeService;
	@Autowired
	private IInstitutionBackendService backendService;
	@Autowired
	private IInstitutionOptionValService optionValService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IHomeService userHomeService;
	
	public void addDeptMap(ModelAndView mv,InstitutionHome home){
		List<InstitutionDepartment> depts=backendService.getVmtDepartment(home.getInstitutionId());
	    mv.addObject("deptMap",extractDept(depts));
	}
	private Map<Integer,InstitutionDepartment> extractDept(List<InstitutionDepartment> depts){
		if(CommonUtils.isNull(depts)){
			return Collections.emptyMap();
		}
		Map<Integer,InstitutionDepartment> map=new LinkedHashMap<Integer,InstitutionDepartment>();
		for(InstitutionDepartment dept:depts){
			map.put(dept.getId(),dept);
		}
		return map;
	}
	
	@RequestMapping("list/{page}")
	public ModelAndView list(@PathVariable("domain")String domain,
			@PathVariable("page")int page,
			SearchInstitutionCondition condition,
			HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution_backend/topic/list");
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("list:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return new ModelAndView("institution_backend/permission");
		}
		
		if(home==null||page<0){
			return null;
		}
		PageResult<InstitutionTopic> result=backEndTopicService.getTopic(home.getInstitutionId(), page, condition);
		Map<Integer,Integer> pubFundsFromMap=backEndTopicService.getFundsFromsMap(home.getInstitutionId());
		
		mv.addObject("fundsFromMap",pubFundsFromMap);
		mv.addObject("condition",condition);
		mv.addObject("page",result);
		mv.addObject("domain",domain);
		
		List<Integer> topicId=CommonUtils.extractSthField(result.getDatas(),"id");
		if(!CommonUtils.isNull(topicId)){
			Map<Integer,List<InstitutionAuthor>> authorMap=backEndTopicService.getListAuthorsMap(topicId);
			mv.addObject("authorMap",authorMap);
		}
		addBaseData(mv,home);
		return mv;
	}
	private void addBaseData(ModelAndView mv,InstitutionHome home){
		//类别
		Map<Integer,InstitutionOptionVal> types= optionValService.getMapByOptionId(InstitutionOption.TOPIC_TYPE, home.getInstitutionId());
		mv.addObject("types",types);

		//资金来源
		Map<Integer,InstitutionOptionVal> fundsFroms= optionValService.getMapByOptionId(InstitutionOption.TOPIC_FUNDS_FROM, home.getInstitutionId());
		mv.addObject("fundsFroms",fundsFroms);
	}
	
//	@RequestMapping("list/{id}.json")
//	@ResponseBody
//	public ModelAndView listJson(@PathVariable("domain")String domain,@PathVariable("id")int id,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			return null;
//		}
////		if(!backEndService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
////			return null;
////		}
//		InstitutionTopic topic = backEndTopicService.getTopicById(home.getInstitutionId(), id);
//		if(topic==null){
//			LOG.error("update:paper["+id+"] is not found!");
//			return null;
//		}
//		ModelAndView mv=new ModelAndView("institution_backend/topic/modify");
//		mv.addObject("op","update");
//		mv.addObject("topic",topic);
//		return mv;
//	}
	@RequestMapping("edit/{id}")
	@ResponseBody
	public ModelAndView edit(@RequestParam("returnPage") int returnPage,@PathVariable("domain")String domain,@PathVariable("id")int id,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
//		if(!backEndService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			return null;
//		}
		InstitutionTopic topic = backEndTopicService.getTopicById(id);
		if(topic==null){
			LOG.error("update:paper["+id+"] is not found!");
			return null;
		}
		ModelAndView mv=new ModelAndView("institution_backend/topic/modify");
		mv.addObject("op","update");
		mv.addObject("topic",topic);
		mv.addObject("returnPage",returnPage);
		addBaseData(mv,home);
		addDeptMap(mv,home);
		return mv;
	}
	@RequestMapping("delete")
	public ModelAndView delete(@PathVariable("domain")String domain,@RequestParam("id[]")int[] id,@RequestParam("page")String page,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		backEndTopicService.deleteTopic(id);
//		backEndTopicService.deleteByTopicId(id);
		
//		InstitutionTopic topic = listJson(domain, id, request);
//			LOG.info("delete user["+Arrays.toString(umtId)+"] in ["+vmtInfo.getDn()+"]");
//		} catch (ServiceException e) {
//			LOG.error(e.getMessage(),e);
//		}
		return new ModelAndView(new RedirectView(request.getContextPath()+"/institution/"+domain+"/backend/topic/list/"+page));
	}
	@RequestMapping("update/{id}")
	@ResponseBody
	public ModelAndView update(InstitutionTopic topic,@PathVariable("domain")String domain,@PathVariable("id")int id,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("create:home is null["+domain+"]");
		}
		
//		if(CommonUtils.isNull(topic.getId())||!topic.getId().equals(id)){
//			LOG.error("update.error:id not found["+id+"]");
//			return false;
//		}
//		if(CommonUtils.isNull(vmtMember.getTrueName())){
//			LOG.error("update.error:trueName can't be null");
//			return false;
//		}
		
		backEndTopicService.updateTopic(id, topic);
		return new ModelAndView(new RedirectView(request.getContextPath()+"/institution/"+domain+"/backend/topic/modify"));
	}
	@RequestMapping("create")
	public ModelAndView create(@PathVariable("domain")String domain,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("create:home is null["+domain+"]");
			return null;
		}
		
		ModelAndView mv=new ModelAndView("institution_backend/topic/modify");
		mv.addObject("topicCount",backEndTopicService.getTopicCount(home.getInstitutionId()));
		mv.addObject("op","create");
		mv.addObject("domain",domain);
		addBaseData(mv,home);
		addDeptMap(mv,home);
		return mv;
	}
	@RequestMapping("submit")
	public JsonResult submit(InstitutionTopic topic,@PathVariable("domain")String domain,
			@RequestParam("uid[]")int[] uid,
			@RequestParam("authorType[]")String[] authorType,
			HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("submit:home is null["+domain+"]");
			return new JsonResult("找不到组织机构");
		}
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("submit:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		topic.setInstitution_id(home.getInstitutionId());
		if(topic.getId()==0){
//			backEndTopicService.insertTopic(topic);
//			backEndTopicService.createRef(topic.getId(), uid, authorType);
			backEndTopicService.insert(topic, uid, authorType);
		}else{
			backEndTopicService.updateTopic(topic.getId(), topic);
			backEndTopicService.deleteRef(topic.getId());
			backEndTopicService.createRef(topic.getId(), uid, authorType);
		}
		return new JsonResult();
	}
	@RequestMapping("/authors/{topicId}")
	@ResponseBody
	public JsonResult getAuthors(@PathVariable("domain")String domain,@PathVariable("topicId")int topicId,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("update:home is null["+domain+"]");
			return new JsonResult("not found institution");
		}
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("update:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}

		List<InstitutionAuthor> authors=backEndTopicService.getAuthorsByTopicId(topicId);
		JsonResult jr=new JsonResult();
		jr.setData(authors);
		return jr;
	}
	@RequestMapping("author/create")
	@ResponseBody
	public JsonResult createAuthor(@PathVariable("domain")String domain,InstitutionAuthor author,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("create.author:home is null["+domain+"]");
			return new JsonResult("找不到组织机构");
		}
		int uid=SessionUtils.getUser(request).getId();
		author.setCreator(uid);
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("create.author:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		backEndTopicService.createAuthor(author);
		JsonResult jr=new JsonResult();
		jr.setData(author);
		return jr;
	}
	@RequestMapping("detail/{topicId}")
	@ResponseBody
	public ModelAndView detail(@RequestParam("returnPage") int returnPage,@PathVariable("domain")String domain,@PathVariable("topicId")int copyrightId,HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution_backend/topic/detail");
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			return null;
		} 
		InstitutionTopic topic = backEndTopicService.getTopicById(copyrightId);
		Map<Integer, InstitutionOptionVal> fundsFromMap = optionValService.getMapByOptionId(InstitutionOption.TOPIC_FUNDS_FROM, home.getInstitutionId());
		mv.addObject("fundsFromMap",fundsFromMap);
		mv.addObject("topic",topic);
		mv.addObject("returnPage",returnPage);
		mv.addObject("domain",domain);
		addBaseData(mv,home);
		addDeptMap(mv,home);
		return mv;
	}
	@RequestMapping("author/{topicId}/{authorId}")
	@ResponseBody
	public JsonResult authorDetail(@PathVariable("topicId")int topicId,@PathVariable("domain")String domain,@PathVariable("authorId")int authorId,HttpServletRequest request){
		InstitutionAuthor author=backEndTopicService.getAuthorsById(topicId,authorId);
		if(author==null){
			return new JsonResult("未查询到作者信息");
		}
		List<String> emails=new ArrayList<String>();
		emails.add(author.getAuthorEmail());
		SimpleUser u=CommonUtils.first(userService.getUserByEmails(emails));
		JSONObject jsonObj=new JSONObject();
		if(u!=null){
			jsonObj.put("home", userHomeService.getDomain(u.getId()));
		}else{
			jsonObj.put("home", false);
		}
		jsonObj.put("author", author);
		JsonResult jr=new JsonResult();
		jr.setData(jsonObj);
		return jr;
	}
}
