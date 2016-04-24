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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionOption;
import net.duckling.dhome.domain.institution.InstitutionOptionVal;
import net.duckling.dhome.domain.institution.InstitutionPeriodical;
import net.duckling.dhome.domain.institution.InstitutionPeriodical;
import net.duckling.dhome.domain.institution.InstitutionPeriodicalAuthor;
import net.duckling.dhome.domain.institution.InstitutionPeriodical;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.JsonResult;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendPeriodicalService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionOptionValService;
import net.duckling.dhome.service.IInstitutionPeopleService;
import net.duckling.dhome.service.IInstitutionVmtService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.impl.VMTMessageHandlerImpl;
import net.duckling.vmt.api.impl.GroupService;
import net.duckling.vmt.api.impl.OrgService;
import net.duckling.vmt.api.impl.UserService;

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
@RequestMapping("/people/{domain}/admin/periodical")
public class PeriodicalController {
	private static final Logger LOG=Logger.getLogger(PeriodicalController.class);
	@Autowired
	private IHomeService userHomeService;
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private UserService vmtUserService;
	
	@Autowired
	private IInstitutionHomeService homeService;
	@Autowired
	private IInstitutionVmtService vmtService;
	
	@Autowired
	private IInstitutionBackendService backendService;
	
	@Autowired
	private IUserService userService;
	@Autowired
	private IInstitutionBackendPeriodicalService periodicalService;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private VMTMessageHandlerImpl vmtMessageHandleImpl;
	
	@Autowired
	private IInstitutionPeopleService peopleService;
	
	@Autowired
	private IInstitutionOptionValService optionValService;
	
	private void addBaseData(ModelAndView mv,SimpleUser user){
		//期刊名称
		Map<Integer,InstitutionOptionVal> periodicalNames= optionValService.getMapByOptionId(InstitutionOption.PERIODICAL_ASSIGNMENT_PERIODICAL_NAME, user.getInstitutionId());
		mv.addObject("periodicalNames",periodicalNames);

		//职位
		Map<Integer,InstitutionOptionVal> positions= optionValService.getMapByOptionId(InstitutionOption.PERIODICAL_ASSIGNMENT_POSITION, user.getInstitutionId());
		mv.addObject("positions",positions);
	}
	
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
	public ModelAndView list(
			@PathVariable("domain")String domain,
			@PathVariable("page")int page,
			SearchInstitutionCondition condition,
			HttpServletRequest request){
		
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null||page<0){
//			LOG.error("list:home is null["+domain+"]  or page < 0");
//			return null;
//		}
//		
//		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
//			LOG.error("list:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return null;
//		}
		SimpleUser user = SessionUtils.getUser(request);
		ModelAndView mv=new ModelAndView("institution/periodical/list");
		PageResult<InstitutionPeriodical> result=periodicalService.getPeriodicalByUser(user.getId(), page,condition);
		mv.addObject("page",result);
		
		List<Integer> perIds=CommonUtils.extractSthField(result.getDatas(),"id");
		if(!CommonUtils.isNull(perIds)){
			Map<Integer,List<InstitutionPeriodicalAuthor>> authorMap=periodicalService.getAuthorsMap(perIds);
			mv.addObject("authorMap",authorMap);
		}
		
		 addBaseData(mv,user);
		
		mv.addObject("condition",condition);

//		List<SimpleUser> users=userService.getUserByEmails(getEmail(result.getDatas()));
//		mv.addObject("userMap",extract(users));
//		
//		List<Home> homes=userHomeService.getDomainsByUids(getUids(users));
//		mv.addObject("homeMap",extractHome(homes));
//		
//		List<InstitutionDepartment> depts=backEndService.getVmtDepartment(getDeptId(result.getDatas()));
//		mv.addObject("deptMap",extractDept(depts));
		mv.addObject("domain",domain);
		mv.addObject("titleUser",user);
		return mv;
	}
	
	/**
	 * 添加期刊任职页面
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("add")
	public ModelAndView add(@PathVariable("domain")String domain,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			LOG.error("add:home is null["+domain+"]");
//			return null;
//		}
//		
//		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
//			LOG.error("add:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return null;
//		}
		SimpleUser user = SessionUtils.getUser(request);
	    ModelAndView mv = new ModelAndView("institution/periodical/add");
	    mv.addObject("op", "add");
	    mv.addObject("domain",domain);
	    mv.addObject("titleUser",user);
	    
	    addDeptMap(mv,user);
	    addBaseData(mv,user);
	    return mv;
	}
	
	@RequestMapping("update/{perId}")
	public ModelAndView update(
			@PathVariable("domain") String domain,
			@PathVariable("perId") int perId,
			@RequestParam("returnPage") int returnPage,
			HttpServletRequest request){
//		InstitutionHome home = homeService.getInstitutionByDomain(domain);
//		if(home == null){
//			LOG.error("update : home is null["+domain+"]");
//		}
//		
//		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
//			LOG.error("update:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return null;
//		}
		
		InstitutionPeriodical per = periodicalService.getPeriodical(perId);
		if(per == null){
			LOG.error("update : periodical["+perId+"] is not found");
			return null;
		}
		SimpleUser user = SessionUtils.getUser(request);
	    ModelAndView mv = new ModelAndView("institution/periodical/add");
	    mv.addObject("op", "update");
	    mv.addObject("per", per);
	    mv.addObject("returnPage",returnPage);
	    mv.addObject("titleUser",user);
	    
	    addDeptMap(mv,user);
	    addBaseData(mv,user);
		return mv;
	}
	
	
	@RequestMapping("detail/{periodicalId}")
	public ModelAndView detail(
			@PathVariable("domain") String domain,
			@PathVariable("periodicalId") int periodicalId,
			@RequestParam("returnPage") int returnPage,
			HttpServletRequest request){
		
//		InstitutionHome home = homeService.getInstitutionByDomain(domain);
//		if(home == null){
//			LOG.error("detail : home is null["+domain+"]");
//		}
//		
//		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
//			LOG.error("detail["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return null;
//		}
		
		InstitutionPeriodical periodical = periodicalService.getPeriodical(periodicalId);
		if(periodical == null){
			LOG.error("detail : periodical["+periodicalId+"] is not found");
			return null;
		}
		SimpleUser user = SessionUtils.getUser(request);
		 ModelAndView mv = new ModelAndView("institution/periodical/detail");
		 mv.addObject("periodical", periodical);
		 mv.addObject("returnPage",returnPage);
		 mv.addObject("titleUser",user);
		 
		 addDeptMap(mv,user);
		 addBaseData(mv,user);
		 return mv;
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public ModelAndView delete(@PathVariable("domain") String domain,@RequestParam("page")String page,@RequestParam("perId[]")int[] perId,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			LOG.error("delete:home is null["+domain+"]");
//			return new JsonResult("找不到组织机构");
//		}
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			LOG.error("delete:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return new JsonResult("权限不足");
//		}
//		if(CommonUtils.isNull(perId)){
//			return new JsonResult("参数缺失");
//		}
		LOG.info("delete periodical ref["+Arrays.toString(perId)+"]");
		periodicalService.deletePeriodicalUser(perId);
		return new ModelAndView(new RedirectView(request.getContextPath()+"/people/"+domain+"/admin/periodical/list/"+page));
	}
	
	@RequestMapping("author/{perId}/{authorId}")
	@ResponseBody
	public JsonResult authorDetail(@PathVariable("domain") String domain,@PathVariable("perId") int perId,@PathVariable("authorId") String authorId,HttpServletRequest request){
//		InstitutionHome home = homeService.getInstitutionByDomain(domain);
//		if(home == null){
//			LOG.error("submit :home is null["+domain+"]");
//			return new JsonResult("找不到机构主页");
//		}
//		
//		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
//			LOG.error("submit : user["+SessionUtils.getUserId(request)+"] is not admin@["+domain+"]");
//			return new JsonResult("权限不足");
//		}
		
		
		InstitutionPeriodicalAuthor author = periodicalService.getAuthorById(perId, authorId);
		if(author == null){
			return new JsonResult("未查询到作者信息");
		}
		List<String> emails=new ArrayList<String>();
		emails.add(author.getCstnetId());
		SimpleUser u=CommonUtils.first(userService.getUserByEmails(emails));
		JSONObject jsonObj=new JSONObject();
		if(u!=null){
			jsonObj.put("home", userHomeService.getDomain(u.getId()));
		}else{
			jsonObj.put("home", false);
		}
		jsonObj.put("author", author);
		
		JsonResult result = new JsonResult();
		result.setData(jsonObj);
		return result;
	}
	
	/**
	 * 查询期刊任职作者
	 * @param domain
	 * @param perId
	 * @param request
	 * @return
	 */
	@RequestMapping("authors/{perId}")
	@ResponseBody
	public JsonResult getAuthors(@PathVariable("domain") String domain,
			@PathVariable("perId") int perId,HttpServletRequest request){
//		InstitutionHome home = homeService.getInstitutionByDomain(domain);
//		if(home == null){
//			LOG.error("authors :home is null["+domain+"]");
//			return new JsonResult("找不到机构主页");
//		}
//		
//		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
//			LOG.error("authors : user["+SessionUtils.getUserId(request)+"] is not admin@["+domain+"]");
//			return new JsonResult("权限不足");
//		}
		
		List<InstitutionPeriodicalAuthor> authors = periodicalService.getAuthorByPerId(perId);
		JsonResult result = new JsonResult();
		result.setData(authors);
		return result;
	}

	/**
	 * 添加/更改期刊任职提交
	 * @param per
	 * @param domain
	 * @param authorIds
	 * @param order
	 * @param communicationAuthors
	 * @param authorStudents
	 * @param authorTeachers
	 * @param request
	 * @return
	 */
	@RequestMapping("submit")
	@ResponseBody
	public JsonResult submit(
			InstitutionPeriodical per,
			@PathVariable("domain")String domain,
			@RequestParam("uid[]") String[] authorIds,
//			@RequestParam("order[]") int[] order,
//			@RequestParam("communicationAuthor[]") boolean[] communicationAuthors,
//			@RequestParam("authorStudent[]") boolean[] authorStudents,
//			@RequestParam("authorTeacher[]") boolean[] authorTeachers,
			HttpServletRequest request){
		
//		InstitutionHome home = homeService.getInstitutionByDomain(domain);
//		if(home == null){
//			LOG.error("submit :home is null["+domain+"]");
//			return new JsonResult("找不到机构主页");
//		}
//		
//		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
//			LOG.error("submit : user["+SessionUtils.getUserId(request)+"] is not admin@["+domain+"]");
//			return new JsonResult("权限不足");
//		}
		SimpleUser user = SessionUtils.getUser(request);
		int perId = per.getId();
		if(perId == 0){
			per.setId(periodicalService.create(per,user.getInstitutionId(), authorIds));
			periodicalService.insertPeriodicalUser(per.getId(), user.getId());
		}else{
			periodicalService.update(per,user.getInstitutionId(), authorIds);
		}
		
		return new JsonResult();
	}
	
	
	@RequestMapping("search/periodical")
	public String searchPeriodical(Model model,@RequestParam("offset") int offset,@RequestParam("size")int size,
			HttpServletRequest request){
		String keyword = CommonUtils.trim(request.getParameter("keyword"));
		SimpleUser user = SessionUtils.getUser(request);
		List<Integer> existPeriodicalIds=periodicalService.getExistPeriodicalIds(user.getId());
		List<InstitutionPeriodical> list = null;
		if(CommonUtils.isNull(keyword)){
			list = periodicalService.getPeriodicalByInit(offset, size, CommonUtils.trim(user.getZhName()));
			keyword = user.getZhName();
		}else{
			list = periodicalService.getPeriodicalByKeyword(offset, size, CommonUtils.trim(keyword));
		}
//		array = filter(array, request);
		JSONArray array =new JSONArray();
		array.addAll(list);
		array = filter(array,request);
		array = filterExist(array,existPeriodicalIds);
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
		String existPeriodicals = CommonUtils.trim(request.getParameter("existPeriodicals"));
		if(null == existPeriodicals || "".equals(existPeriodicals)){
			return array;
		}
		JSONArray result = new JSONArray();
		String[] temp = existPeriodicals.split(",");
		int size = array.size();
		for(int i=0; i<size; i++){
			InstitutionPeriodical obj = (InstitutionPeriodical) array.get(i);
			int periodicalId = obj.getId();
			boolean exist = false;
			for(String periodical: temp){
				if(!CommonUtils.isNull(periodical) && periodicalId == Integer.parseInt(periodical)){
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
			InstitutionPeriodical obj = (InstitutionPeriodical) result.get(i);
			int periodicalId = obj.getId();
			if (!existIds.contains(periodicalId)) {
				array.add(obj);
			}
		}
		return array;
	}
	@RequestMapping("search")
	@ResponseBody
	public ModelAndView detail(@PathVariable("domain")String domain,HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution/periodical/searchAdd");
		SimpleUser user = SessionUtils.getUser(request);
//		List<InstitutionPeriodical> list = periodicalService.getPeriodicalByInit(offset, size, CommonUtils.trim(user.getZhName()));
		mv.addObject("domain",domain);
		mv.addObject("titleUser",user);
		addBaseData(mv,user);
		return mv;
	}
	@RequestMapping("search/submit")
	@ResponseBody
	public ModelAndView save(@PathVariable("domain")String domain,HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution/periodical/searchAdd");
		SimpleUser user = SessionUtils.getUser(request);
		String existPeriodicals = request.getParameter("periodicalIds");
		if(null == existPeriodicals || "".equals(existPeriodicals)){
			return null;
		}
		String[] periodicalId = existPeriodicals.split(",");
//		List<InstitutionPeriodical> list = periodicalService.getPeriodicalByInit(offset, size, CommonUtils.trim(user.getZhName()));
		periodicalService.insertPeriodical(periodicalId, user.getId());
		mv.addObject("domain",domain);
		addBaseData(mv,user);
		return mv;
	}
	
}
