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
import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionOption;
import net.duckling.dhome.domain.institution.InstitutionOptionVal;
import net.duckling.dhome.domain.institution.InstitutionPeriodical;
import net.duckling.dhome.domain.institution.InstitutionPeriodicalAuthor;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.JsonResult;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendPeriodicalService;
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
@RequestMapping("/institution/{domain}/backend/periodical")
public class InstitutionBackEndPeriodicalController {
	private static final Logger LOG=Logger.getLogger(InstitutionBackEndPeriodicalController.class);
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
	private IInstitutionOptionValService optionValService;
	
	private void addBaseData(ModelAndView mv,InstitutionHome home){
		//期刊名称
		Map<Integer,InstitutionOptionVal> periodicalNames= optionValService.getMapByOptionId(InstitutionOption.PERIODICAL_ASSIGNMENT_PERIODICAL_NAME, home.getInstitutionId());
		mv.addObject("periodicalNames",periodicalNames);

		//职位
		Map<Integer,InstitutionOptionVal> positions= optionValService.getMapByOptionId(InstitutionOption.PERIODICAL_ASSIGNMENT_POSITION, home.getInstitutionId());
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
		
		ModelAndView mv=new ModelAndView("institution_backend/periodical/list");
		PageResult<InstitutionPeriodical> result=periodicalService.getPeriodicals(home.getInstitutionId(), page,condition);
		mv.addObject("page",result);
		
		List<Integer> perIds=CommonUtils.extractSthField(result.getDatas(),"id");
		if(!CommonUtils.isNull(perIds)){
			Map<Integer,List<InstitutionPeriodicalAuthor>> authorMap=periodicalService.getAuthorsMap(perIds);
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
	
	/**
	 * 添加期刊任职页面
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("add")
	public ModelAndView add(@PathVariable("domain")String domain,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("add:home is null["+domain+"]");
			return null;
		}
		
		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("add:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return null;
		}
		
	    ModelAndView mv = new ModelAndView("institution_backend/periodical/add");
	    mv.addObject("op", "add");
	    mv.addObject("domain",domain);
	    
	    addDeptMap(mv,home);
	    addBaseData(mv,home);
	    
	    return mv;
	}
	
	@RequestMapping("update/{perId}")
	public ModelAndView update(
			@PathVariable("domain") String domain,
			@PathVariable("perId") int perId,
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
		
		InstitutionPeriodical per = periodicalService.getPeriodical(perId);
		if(per == null){
			LOG.error("update : periodical["+perId+"] is not found");
			return null;
		}
		
	    ModelAndView mv = new ModelAndView("institution_backend/periodical/add");
	    mv.addObject("op", "update");
	    mv.addObject("per", per);
	    mv.addObject("returnPage",returnPage);
	    
	    addDeptMap(mv,home);
	    addBaseData(mv,home);

		return mv;
	}
	
	
	@RequestMapping("detail/{periodicalId}")
	public ModelAndView detail(
			@PathVariable("domain") String domain,
			@PathVariable("periodicalId") int periodicalId,
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
		
		InstitutionPeriodical periodical = periodicalService.getPeriodical(periodicalId);
		if(periodical == null){
			LOG.error("detail : periodical["+periodicalId+"] is not found");
			return null;
		}
		
		 ModelAndView mv = new ModelAndView("institution_backend/periodical/detail");
		 mv.addObject("periodical", periodical);
		 mv.addObject("returnPage",returnPage);
		 
		 addDeptMap(mv,home);
		 addBaseData(mv,home);
		
		 return mv;
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public JsonResult delete(@PathVariable("domain") String domain,@RequestParam("perId[]")int[] perId,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("delete:home is null["+domain+"]");
			return new JsonResult("找不到组织机构");
		}
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("delete:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		if(CommonUtils.isNull(perId)){
			return new JsonResult("参数缺失");
		}
		LOG.info("delete paper ref["+Arrays.toString(perId)+"]");
		periodicalService.delete(home.getInstitutionId(),perId);
		return new JsonResult();
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
//	public JsonResult createAuthor(@PathVariable("domain")String domain,InstitutionPeriodicalAuthor author,HttpServletRequest request){
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
//		periodicalService.createAuthor(author);
//		
//		JsonResult result = new JsonResult();
//		result.setData(author);
//		return result;
//	}
	
	@RequestMapping("author/{perId}/{authorId}")
	@ResponseBody
	public JsonResult authorDetail(@PathVariable("domain") String domain,@PathVariable("perId") int perId,@PathVariable("authorId") String authorId,HttpServletRequest request){
		InstitutionHome home = homeService.getInstitutionByDomain(domain);
		if(home == null){
			LOG.error("submit :home is null["+domain+"]");
			return new JsonResult("找不到机构主页");
		}
		
		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("submit : user["+SessionUtils.getUserId(request)+"] is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		
		
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
		InstitutionHome home = homeService.getInstitutionByDomain(domain);
		if(home == null){
			LOG.error("authors :home is null["+domain+"]");
			return new JsonResult("找不到机构主页");
		}
		
		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("authors : user["+SessionUtils.getUserId(request)+"] is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		
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
		
		InstitutionHome home = homeService.getInstitutionByDomain(domain);
		if(home == null){
			LOG.error("submit :home is null["+domain+"]");
			return new JsonResult("找不到机构主页");
		}
		
		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("submit : user["+SessionUtils.getUserId(request)+"] is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		
		int perId = per.getId();
		if(perId == 0){
			periodicalService.create(per,home.getInstitutionId(), authorIds);
		}else{
			periodicalService.update(per, home.getInstitutionId(), authorIds);
		}
		
		return new JsonResult();
	}
	
	
	
	

//	@RequestMapping("update/{umtId}")
//	@ResponseBody
//	public boolean update(InstitutionMemberFromVmt vmtMember,@PathVariable("domain")String domain,@PathVariable("umtId")String umtId,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(CommonUtils.isNull(vmtMember.getUmtId())||!vmtMember.getUmtId().equals(umtId)){
//			LOG.error("update.error:umtId not found["+umtId+"]");
//			return false;
//		}
//		if(CommonUtils.isNull(vmtMember.getTrueName())){
//			LOG.error("update.error:trueName can't be null");
//			return false;
//		}
//		if(home==null){
//			LOG.error("update.error:not found institutiton["+domain+"]");
//			return false;
//		}
//		InstitutionVmtInfo vmtInfo=vmtService.getVmtInfoByInstitutionId(home.getInstitutionId());
//		if(vmtInfo==null){
//			LOG.error("update.error:can't found vmtInfo[insId="+home.getInstitutionId()+"]");
//			return false;
//		}
//		if(!backEndService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			LOG.error("update.error:not admin!");
//			return false;
//		}
//		if(CommonUtils.isNull(vmtMember.getUmtId())){
//			LOG.error("update.error：umtId is Null!");
//			return false;
//		}
//		try {
//			VmtUser user=CommonUtils.first(vmtUserService.searchUserByUmtId(vmtInfo.getDn(), new String[]{umtId}));
//			if(user==null){
//				LOG.error("update.error:can't find vmt user!");
//				return false;
//			}
//			user.setTitle(vmtMember.getTitle());
//			user.setName(vmtMember.getTrueName());
//			user.setOffice(vmtMember.getOfficeAddress());
//			user.setOfficePhone(vmtMember.getOfficeTelephone());
//			user.setTelephone(vmtMember.getMobilePhone());
//			user.setTitle(vmtMember.getTitle());
//			user.setSex(vmtMember.getSex());
//			user.setUmtId(vmtMember.getUmtId());
//			vmtUserService.updateByUmtId(vmtInfo.getDn(), user);
//			
//		} catch (ServiceException e) {
//			LOG.error(e.getMessage(),e);
//			return false;
//		}
//		return true;
//	}
//	@RequestMapping("detail/{umtId}.json")
//	@ResponseBody
//	public InstitutionMemberFromVmt detailJson(@PathVariable("domain")String domain,@PathVariable("umtId")String umtId,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			return null;
//		}
//		if(!backEndService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			return null;
//		}
//		return backEndService.getVmtMemberByUmtId(home.getInstitutionId(),umtId);
//	}
//	
//
}
