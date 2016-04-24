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
package net.duckling.dhome.web.institution;


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
import net.duckling.dhome.domain.institution.InstitutionOption;
import net.duckling.dhome.domain.institution.InstitutionOptionVal;
import net.duckling.dhome.domain.institution.InstitutionTopic;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionTopic;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.JsonResult;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendPaperService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionBackendTopicService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionOptionValService;
import net.duckling.dhome.service.IInstitutionPeopleService;
import net.duckling.dhome.service.IUserService;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@NPermission(authenticated = true,member = "iap")
@RequestMapping("/people/{domain}/admin/topic")

public class TopicController {
	private static final Logger LOG=Logger.getLogger(TopicController.class);
	
	@Autowired
	private IInstitutionBackendTopicService backEndTopicService;
	@Autowired
	private IInstitutionHomeService homeService;
	@Autowired
	private IInstitutionBackendService backendService;
	@Autowired
	private IInstitutionBackendPaperService paperService;
	@Autowired
	private IInstitutionPeopleService peopleService;
	@Autowired
	private IInstitutionOptionValService optionValService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IHomeService userHomeService;
	
	public void addDeptMap(ModelAndView mv,SimpleUser user){
		List<InstitutionDepartment> depts=backendService.getVmtDepartment(user.getInstitutionId());
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
		ModelAndView mv=new ModelAndView("institution/topic/list");
		SimpleUser user = SessionUtils.getUser(request);
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null||page<0){
//			return null;
//		}
		PageResult<InstitutionTopic> result=backEndTopicService.getTopicByUser(user.getId(), page, condition);
		Map<Integer,Integer> pubFundsFromMap=backEndTopicService.getGradesMapByUser(user.getId());
		mv.addObject("fundsFromMap",pubFundsFromMap);
		mv.addObject("condition",condition);
		mv.addObject("page",result);
		mv.addObject("domain",domain);
		mv.addObject("titleUser",user);
		
		List<Integer> topicId=CommonUtils.extractSthField(result.getDatas(),"id");
//		for (Integer integer : topicId) {
//			System.out.println(integer+"=======");
//		}
		Map<Integer,List<InstitutionAuthor>> authorMap=topicId==null ? new HashMap<Integer,List<InstitutionAuthor>>() :
				backEndTopicService.getListAuthorsMap(topicId);
		mv.addObject("authorMap",authorMap);
		addBaseData(mv,user);
//		for (int key : authorMap.keySet()) {
//			   System.out.println("key= "+ key + " and value= " + authorMap.get(key));
//			  }
		return mv;
	}
	private void addBaseData(ModelAndView mv,SimpleUser user){
		//类别
		Map<Integer,InstitutionOptionVal> types= optionValService.getMapByOptionId(InstitutionOption.TOPIC_TYPE, user.getInstitutionId());
		mv.addObject("types",types);

		//资金来源
		Map<Integer,InstitutionOptionVal> fundsFroms= optionValService.getMapByOptionId(InstitutionOption.TOPIC_FUNDS_FROM, user.getInstitutionId());
		mv.addObject("fundsFroms",fundsFroms);
	}
	
	@RequestMapping("edit/{id}")
	@ResponseBody
	public ModelAndView edit(@RequestParam("returnPage") int returnPage,@PathVariable("domain")String domain,@PathVariable("id")int id,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			return null;
//		}
//		if(!backEndService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			return null;
//		}
		SimpleUser user = SessionUtils.getUser(request);
		InstitutionTopic topic = backEndTopicService.getTopicRoleById(id, user.getId());
		if(topic==null){
			LOG.error("update:paper["+id+"] is not found!");
			return null;
		}
		
//		Map<Integer, InstitutionOptionVal> typeMap = optionValService.getMapByOptionId(InstitutionOption.TOPIC_TYPE, user.getInstitutionId());
//		Map<Integer, InstitutionOptionVal> fundsFromMap = optionValService.getMapByOptionId(InstitutionOption.TOPIC_FUNDS_FROM, user.getInstitutionId());
		ModelAndView mv=new ModelAndView("institution/topic/modify");
//		mv.addObject("typeMap",typeMap);
//		mv.addObject("fundsFromNameMap",fundsFromMap);
		mv.addObject("op","update");
		mv.addObject("topic",topic);
		mv.addObject("returnPage",returnPage);
		mv.addObject("titleUser",user);
		addBaseData(mv,user);
		addDeptMap(mv,user);
		return mv;
	}
	@RequestMapping("delete")
	public ModelAndView delete(@PathVariable("domain")String domain,@RequestParam("id[]")int[] id,@RequestParam("page")String page,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			return null;
//		}
//		backEndTopicService.deleteTopic(id);
		backEndTopicService.deleteTopicUser(id);
		
		return new ModelAndView(new RedirectView(request.getContextPath()+"/people/"+domain+"/admin/topic/list/"+page));
	}
	@RequestMapping("update/{id}")
	@ResponseBody
	public ModelAndView update(InstitutionTopic topic,@PathVariable("domain")String domain,@PathVariable("id")int id,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			LOG.error("create:home is null["+domain+"]");
//		}
		
//		if(CommonUtils.isNull(topic.getId())||!topic.getId().equals(id)){
//			LOG.error("update.error:id not found["+id+"]");
//			return false;
//		}
//		if(CommonUtils.isNull(vmtMember.getTrueName())){
//			LOG.error("update.error:trueName can't be null");
//			return false;
//		}
		
		backEndTopicService.updateTopic(id, topic);
		return new ModelAndView(new RedirectView(request.getContextPath()+"/people/"+domain+"/admin/topic/modify"));
	}
	@RequestMapping("create")
	public ModelAndView create(@PathVariable("domain")String domain,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			LOG.error("create:home is null["+domain+"]");
//			return null;
//		}
		SimpleUser user = SessionUtils.getUser(request);
		ModelAndView mv=new ModelAndView("institution/topic/modify");
		mv.addObject("topicCount",backEndTopicService.getTopicCountByUser(user.getId()));
		mv.addObject("op","create");
		mv.addObject("domain",domain);
		mv.addObject("titleUser",user);
		addBaseData(mv,user);
		addDeptMap(mv,user);
		return mv;
	}
	@RequestMapping("submit")
	public JsonResult submit(InstitutionTopic topic,@PathVariable("domain")String domain,
			@RequestParam("role")String role,
			@RequestParam("uid[]")int[] uid,
			@RequestParam("authorType[]")String[] authorType,
			HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			LOG.error("submit:home is null["+domain+"]");
//			return new JsonResult("找不到组织机构");
//		}
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			LOG.error("submit:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return new JsonResult("权限不足");
//		}
//		topic.setInstitution_id(home.getInstitutionId());
		SimpleUser user = SessionUtils.getUser(request);
		topic.setInstitution_id(user.getInstitutionId());
		if(topic.getId()==0){
//			backEndTopicService.insertTopic(topic);
//			backEndTopicService.createRef(topic.getId(), uid, authorType);
			topic.setId(backEndTopicService.insert(topic, uid, authorType));
			backEndTopicService.insertTopicUser(topic.getId(), user.getId(),role);
		}else{
			backEndTopicService.updateTopic(topic.getId(), topic);
			backEndTopicService.updateRole(role, user.getId(), topic.getId());
			backEndTopicService.deleteRef(topic.getId());
			backEndTopicService.createRef(topic.getId(), uid, authorType);
		}
		return new JsonResult();
	}
	@RequestMapping("/authors/{topicId}")
	@ResponseBody
	public JsonResult getAuthors(@PathVariable("domain")String domain,@PathVariable("topicId")int topicId,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			LOG.error("update:home is null["+domain+"]");
//			return new JsonResult("not found institution");
//		}
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			LOG.error("update:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return new JsonResult("权限不足");
//		}

		List<InstitutionAuthor> authors=backEndTopicService.getAuthorsByTopicId(topicId);
		JsonResult jr=new JsonResult();
		jr.setData(authors);
		return jr;
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
	
	@RequestMapping("author/create")
	@ResponseBody
	public JsonResult createAuthor(@PathVariable("domain")String domain,InstitutionAuthor author,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			LOG.error("create.author:home is null["+domain+"]");
//			return new JsonResult("找不到组织机构");
//		}
		int uid=SessionUtils.getUser(request).getId();
		author.setCreator(uid);
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			LOG.error("create.author:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return new JsonResult("权限不足");
//		}
		backEndTopicService.createAuthor(author);
		JsonResult jr=new JsonResult();
		jr.setData(author);
		return jr;
	}
	@RequestMapping("detail/{topicId}")
	@ResponseBody
	public ModelAndView detail(@RequestParam("returnPage") int returnPage,@PathVariable("domain")String domain,@PathVariable("topicId")int topicId,HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution/topic/detail");
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			return null;
//		}
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			return null;
//		} 
		
		SimpleUser user = SessionUtils.getUser(request);
		Map<Integer, InstitutionOptionVal> fundsFromMap = optionValService.getMapByOptionId(InstitutionOption.TOPIC_FUNDS_FROM, user.getInstitutionId());
		InstitutionTopic topic = backEndTopicService.getTopicById(topicId);
		mv.addObject("fundsFromMap",fundsFromMap);
		mv.addObject("topic",topic);
		mv.addObject("returnPage",returnPage);
		mv.addObject("domain",domain);
		mv.addObject("titleUser",user);
		addBaseData(mv,user);
		addDeptMap(mv,user);
		return mv;
	}
	
	@RequestMapping("search/author")
	@ResponseBody
	public List<InstitutionAuthor> searchAuthor(@RequestParam("q")String keyword,HttpServletRequest request){
		List<InstitutionAuthor> authors=paperService.searchAuthor(CommonUtils.trim(keyword));
		return authors;
		
	}
	@RequestMapping("search/topic")
	public String searchTopic(Model model,@RequestParam("offset") int offset,@RequestParam("size")int size,
			HttpServletRequest request){
//		ModelAndView mv=new ModelAndView("institution/topic/add");
//		List<InstitutionTopic> topics=backEndTopicService.getTopicByKeyword(offset, size, CommonUtils.trim(keyword));
//		JsonResult result = new JsonResult();
//		result.setData(topics);
//		return result;
		String keyword = CommonUtils.trim(request.getParameter("keyword"));
		SimpleUser user = SessionUtils.getUser(request);
		List<Integer> existTopicIds=backEndTopicService.getExistTopicIds(user.getId());
		List<InstitutionTopic> list = null;
		if(CommonUtils.isNull(keyword)){
			list = backEndTopicService.getTopicByInit(offset, size, CommonUtils.trim(user.getZhName()));
			keyword = user.getZhName();
		}else{
			list = backEndTopicService.getTopicByKeyword(offset, size, CommonUtils.trim(keyword));
		}
//		array = filter(array, request);
		JSONArray array =new JSONArray();
		array.addAll(list);
		array = filter(array,request);
		array = filterExist(array,existTopicIds);
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
		String existTopics = CommonUtils.trim(request.getParameter("existTopics"));
		if(null == existTopics || "".equals(existTopics)){
			return array;
		}
		JSONArray result = new JSONArray();
		String[] temp = existTopics.split(",");
		int size = array.size();
		for(int i=0; i<size; i++){
			InstitutionTopic obj = (InstitutionTopic) array.get(i);
			int topicId = obj.getId();
			boolean exist = false;
			for(String topic: temp){
				if(!CommonUtils.isNull(topic) && topicId == Integer.parseInt(topic)){
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
			InstitutionTopic obj = (InstitutionTopic) result.get(i);
			int topicId = obj.getId();
			if (!existIds.contains(topicId)) {
				array.add(obj);
			}
		}
		return array;
	}
	@RequestMapping("search")
	@ResponseBody
	public ModelAndView detail(@PathVariable("domain")String domain,HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution/topic/searchAdd");
		SimpleUser user = SessionUtils.getUser(request);
//		List<InstitutionTopic> list = backEndTopicService.getTopicByInit(offset, size, CommonUtils.trim(user.getZhName()));
		mv.addObject("domain",domain);
		addBaseData(mv,user);
		return mv;
	}
	@RequestMapping("search/submit")
	@ResponseBody
	public ModelAndView save(@PathVariable("domain")String domain,HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution/topic/searchAdd");
		SimpleUser user = SessionUtils.getUser(request);
		String existPapers = request.getParameter("topicIds");
		if(null == existPapers || "".equals(existPapers)){
			return null;
		}
		String[] topicId = existPapers.split(",");
//		List<InstitutionTopic> list = backEndTopicService.getTopicByInit(offset, size, CommonUtils.trim(user.getZhName()));
		backEndTopicService.insertTopic(topicId, user.getId());
		mv.addObject("domain",domain);
		addBaseData(mv,user);
		return mv;
	}
}
