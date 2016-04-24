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
import net.duckling.dhome.domain.institution.InstitutionAcademic;
import net.duckling.dhome.domain.institution.InstitutionAcademicAuthor;
import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionMemberFromVmt;
import net.duckling.dhome.domain.institution.InstitutionOption;
import net.duckling.dhome.domain.institution.InstitutionOptionVal;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.JsonResult;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendAcademicService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionOptionValService;
import net.duckling.dhome.service.IInstitutionVmtService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.impl.VMTMessageHandlerImpl;
import net.duckling.vmt.api.impl.GroupService;
import net.duckling.vmt.api.impl.OrgService;
import net.duckling.vmt.api.impl.UserService;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@NPermission(role = "admin", authenticated=true)
@RequestMapping("/institution/{domain}/backend/academic")
public class InstitutionBackEndAcademicController {
	private static final Logger LOG=Logger.getLogger(InstitutionBackEndAcademicController.class);
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
	private IInstitutionBackendAcademicService academicService;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private VMTMessageHandlerImpl vmtMessageHandleImpl;
	
	@Autowired
	private IInstitutionOptionValService optionValService;
	
	private void addBaseData(ModelAndView mv,InstitutionHome home){
		//组织名称
		Map<Integer,InstitutionOptionVal> organizationNames= optionValService.getMapByOptionId(InstitutionOption.ACADEMIC_ASSIGNMENT_ORGANIZATION_NAME, home.getInstitutionId());
		mv.addObject("organizationNames",organizationNames);

		//职位
		Map<Integer,InstitutionOptionVal> positions= optionValService.getMapByOptionId(InstitutionOption.ACADEMIC_ASSIGNMENT_POSITION, home.getInstitutionId());
		mv.addObject("positions",positions);
	}
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
	public ModelAndView list(
			@PathVariable("domain")String domain,
			@PathVariable("page")int page,
			SearchInstitutionCondition condition,
			HttpServletRequest request){
		
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null||page<0){
			LOG.error("list:home is null["+domain+"]  or page < 0");
			return null;
		}
		
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("list:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return new ModelAndView("institution_backend/permission");
		}
		
		ModelAndView mv=new ModelAndView("institution_backend/academic/list");
		PageResult<InstitutionAcademic> result=academicService.getAcademics(home.getInstitutionId(), page,condition);
		mv.addObject("page",result);
		
		List<Integer> acmIds=CommonUtils.extractSthField(result.getDatas(),"id");
		if(!CommonUtils.isNull(acmIds)){
			Map<Integer,List<InstitutionAcademicAuthor>> authorMap=academicService.getAuthorsMap(acmIds);
			mv.addObject("authorMap",authorMap);
		}
		
		mv.addObject("condition",condition);
		addBaseData(mv,home);

//		List<SimpleUser> users=userService.getUserByEmails(getEmail(result.getDatas()));
//		mv.addObject("userMap",extract(users));
//		
//		List<Home> homes=userHomeService.getDomainsByUids(getUids(users));
//		mv.addObject("homeMap",extractHome(homes));
//		
//		List<InstitutionDepartment> depts=backEndService.getVmtDepartment(getDeptId(result.getDatas()));
//		mv.addObject("deptMap",extractDept(depts));
		mv.addObject("domain",domain);
		return mv;
	}
	
	@RequestMapping("add")
	public ModelAndView add(@PathVariable("domain")String domain,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("add:home is null["+domain+"]");
			return null;
		}
		
	    ModelAndView mv = new ModelAndView("institution_backend/academic/add");
	    mv.addObject("op", "add");
	    mv.addObject("domain",domain);
	    addDeptMap(mv,home);
	    addBaseData(mv,home);
	    return mv;
	}
	
	@RequestMapping("update/{perId}")
	public ModelAndView update(
			@PathVariable("domain") String domain,
			@PathVariable("perId") int acmId,
			@RequestParam("returnPage") int returnPage,
			HttpServletRequest request){
		InstitutionHome home = homeService.getInstitutionByDomain(domain);
		if(home == null){
			LOG.error("update : home is null["+domain+"]");
		}
		
		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("update:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return null;
		}
		
		InstitutionAcademic academic = academicService.getAcademic(acmId);
		if(academic == null){
			LOG.error("update : Academic["+acmId+"] is not found");
			return null;
		}
		
	    ModelAndView mv = new ModelAndView("institution_backend/academic/add");
	    mv.addObject("op", "update");
	    mv.addObject("academic", academic);
	    mv.addObject("returnPage",returnPage);
	    addDeptMap(mv,home);
	    addBaseData(mv,home);

		return mv;
	}
	
	

	@RequestMapping("detail/{academicId}")
	public ModelAndView detail(
			@PathVariable("domain") String domain,
			@PathVariable("academicId") int academicId,
			@RequestParam("returnPage") int returnPage,
			HttpServletRequest request){
		
		InstitutionHome home = homeService.getInstitutionByDomain(domain);
		if(home == null){
			LOG.error("detail : home is null["+domain+"]");
		}
		
		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("detail["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return null;
		}
		
		InstitutionAcademic academic = academicService.getAcademic(academicId);
		if(academic == null){
			LOG.error("detail : academic["+academicId+"] is not found");
			return null;
		}
		
		 ModelAndView mv = new ModelAndView("institution_backend/academic/detail");
		 mv.addObject("academic", academic);
		 mv.addObject("returnPage",returnPage);
		 addDeptMap(mv,home);
		 addBaseData(mv,home);
		
		 return mv;
	}
	
	/**
	 * 添加作者
	 * @param domain
	 * @param author
	 * @param request
	 * @return
	 */
//	@RequestMapping("author/create")
//	@ResponseBody
//	public JsonResult createAuthor(@PathVariable("domain")String domain,InstitutionAcademicAuthor author,HttpServletRequest request){
//		InstitutionHome home = homeService.getInstitutionByDomain(domain);
//		if(home == null){
//			LOG.error("author.create:home is null ["+domain+"]");
//			return new JsonResult("找不到组织结构");
//		}
//		
//		int uid = SessionUtils.getUserId(request);
//		author.setCreator(uid);
//		
//		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
//			LOG.error("author.create:user["+uid+"] is not admin@["+domain+"]");
//			return new JsonResult("权限不足");
//		}
//		
//		academicService.createAuthor(author);
//		
//		JsonResult result = new JsonResult();
//		result.setData(author);
//		return result;
//	}
//	
	@RequestMapping("author/{perId}/{authorId}")
	@ResponseBody
	public JsonResult authorDetail(@PathVariable("domain") String domain,@PathVariable("perId") int acmId,@PathVariable("authorId") String authorId,HttpServletRequest request){
		InstitutionHome home = homeService.getInstitutionByDomain(domain);
		if(home == null){
			LOG.error("submit :home is null["+domain+"]");
			return new JsonResult("找不到机构主页");
		}
		
		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("submit : user["+SessionUtils.getUserId(request)+"] is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		
		
		InstitutionAcademicAuthor author = academicService.getAuthorById(acmId, authorId);
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
	 * 查询学术任职作者
	 * @param domain
	 * @param perId
	 * @param request
	 * @return
	 */
	@RequestMapping("authors/{acmId}")
	@ResponseBody
	public JsonResult getAuthors(@PathVariable("domain") String domain,
			@PathVariable("acmId") int acmId,HttpServletRequest request){
		InstitutionHome home = homeService.getInstitutionByDomain(domain);
		if(home == null){
			LOG.error("authors :home is null["+domain+"]");
			return new JsonResult("找不到机构主页");
		}
		
		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("authors : user["+SessionUtils.getUserId(request)+"] is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		
		List<InstitutionAcademicAuthor> authors = academicService.getAuthorByAcademicId(acmId);
		JsonResult result = new JsonResult();
		result.setData(authors);
		return result;
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public JsonResult delete(@PathVariable("domain") String domain,@RequestParam("acmId[]")int[] acmId,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("delete:home is null["+domain+"]");
			return new JsonResult("找不到组织机构");
		}
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("delete:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		if(CommonUtils.isNull(acmId)){
			return new JsonResult("参数缺失");
		}
		LOG.info("delete paper ref["+Arrays.toString(acmId)+"]");
		academicService.delete(home.getInstitutionId(),acmId);
		return new JsonResult();
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
			InstitutionAcademic academic,
			@PathVariable("domain")String domain,
			@RequestParam("uid[]") String[] authorIds,
//			@RequestParam("order[]") int[] order,
//			@RequestParam("communicationAuthor[]") boolean[] communicationAuthors,
//			@RequestParam("authorStudent[]") boolean[] authorStudents,
//			@RequestParam("authorTeacher[]") boolean[] authorTeachers,
			HttpServletRequest request){
		
		InstitutionHome home = homeService.getInstitutionByDomain(domain);
		if(home == null){
			LOG.error("submit :home is null["+domain+"]");
			return new JsonResult("找不到机构主页");
		}
		
		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("submit : user["+SessionUtils.getUserId(request)+"] is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		
		int acmId = academic.getId();
		if(acmId == 0){
			academicService.create(academic,home.getInstitutionId(), authorIds);
		}else{
			academicService.update(academic, home.getInstitutionId(), authorIds);
		}
		
		return new JsonResult();
	}
	@RequestMapping("search/user")
	@ResponseBody
	public List<InstitutionMemberFromVmt> searchUser(@RequestParam("q")String keyword,HttpServletRequest request){
		List<InstitutionMemberFromVmt> users=vmtService.searchMember(CommonUtils.trim(keyword));
		return users;
	}
}
