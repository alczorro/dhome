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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.ExcelReader;
import net.duckling.dhome.common.util.JSONHelper;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.dhome.domain.institution.InstitutionMemberFromVmt;
import net.duckling.dhome.domain.institution.InstitutionOption;
import net.duckling.dhome.domain.institution.InstitutionOptionVal;
import net.duckling.dhome.domain.institution.InstitutionTreatise;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionTreatiseAuthor;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.JsonResult;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionBackendTreatiseService;
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
@RequestMapping("/institution/{domain}/backend/treatise")
public class InstitutionBackEndTreatiseController {
	private static final Logger LOG=Logger.getLogger(InstitutionBackEndTreatiseController.class);
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
	private IInstitutionBackendTreatiseService treatiseSerice;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private VMTMessageHandlerImpl vmtMessageHandleImpl;
	
	@Autowired
	private IInstitutionOptionValService optionValService;
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
	private int getDeptIdByName(List<InstitutionDepartment> depts,InstitutionTreatise t){
		for (InstitutionDepartment dept : depts) {
			if(dept.getShortName().equals(t.getDepartShortName())){
				return dept.getId();
			}
		}
		return 0;
		
	}
	private int getPubIdByName(Map<Integer,InstitutionOptionVal> publishers,InstitutionTreatise t){
		 for (Entry<Integer, InstitutionOptionVal> entry : publishers.entrySet()) {
			if(entry.getValue().getVal().equals(t.getPublisherName())){
				return entry.getKey();
			}
		}
		 return 0;
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
		
		ModelAndView mv=new ModelAndView("institution_backend/treatise/list");
		PageResult<InstitutionTreatise> result=treatiseSerice.getTreatises(home.getInstitutionId(), page,condition);
		mv.addObject("page",result);
		
		List<Integer> treatiseIds=CommonUtils.extractSthField(result.getDatas(),"id");
		if(!CommonUtils.isNull(treatiseIds)){
			Map<Integer, List<InstitutionTreatiseAuthor>> authorMap=treatiseSerice.getAuthorsMap(treatiseIds);
			mv.addObject("authorMap",authorMap);
		}
		
		Map<Integer, Integer> publishersMap = treatiseSerice.getPublishersMap(home.getInstitutionId());
		mv.addObject("publishersMap",publishersMap);

		mv.addObject("condition", condition);
//		List<SimpleUser> users=userService.getUserByEmails(getEmail(result.getDatas()));
//		mv.addObject("userMap",extract(users));
//		
//		List<Home> homes=userHomeService.getDomainsByUids(getUids(users));
//		mv.addObject("homeMap",extractHome(homes));
//		
//		List<InstitutionDepartment> depts=backEndService.getVmtDepartment(getDeptId(result.getDatas()));
//		mv.addObject("deptMap",extractDept(depts));
		mv.addObject("domain",domain);
		addBaseData(mv,home);
		return mv;
	}
	
	private void addBaseData(ModelAndView mv,InstitutionHome home){
		//出版社
		Map<Integer,InstitutionOptionVal> publishers= optionValService.getMapByOptionId(InstitutionOption.TREATISE_PUBLISHER, home.getInstitutionId());
		mv.addObject("publishers",publishers);
	}
	
	/**
	 * 添加论著
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
		List<InstitutionDepartment> depts=treatiseSerice.getDepartments(home.getInstitutionId());
	    ModelAndView mv = new ModelAndView("institution_backend/treatise/add");
	    mv.addObject("op", "add");
	    mv.addObject("domain",domain);
	    mv.addObject("deptMap",extractDept(depts));
	    addBaseData(mv,home);
	    return mv;
	}
	
	@RequestMapping("update/{treatiseId}")
	public ModelAndView update(
			@PathVariable("domain") String domain,
			@PathVariable("treatiseId") int treatiseId,
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
		
		InstitutionTreatise treatise = treatiseSerice.getTreatise(treatiseId);
		if(treatise == null){
			LOG.error("update : treatise["+treatiseId+"] is not found");
			return null;
		}
		
	    ModelAndView mv = new ModelAndView("institution_backend/treatise/add");
	    List<InstitutionDepartment> depts=treatiseSerice.getDepartments(home.getInstitutionId());
	    mv.addObject("deptMap",extractDept(depts));
	    mv.addObject("op", "update");
	    mv.addObject("treatise", treatise);
	    mv.addObject("returnPage",returnPage);
	    addBaseData(mv,home);
		return mv;
	}
	
	
	@RequestMapping("detail/{treatiseId}")
	public ModelAndView detail(
			@PathVariable("domain") String domain,
			@PathVariable("treatiseId") int treatiseId,
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
		
		InstitutionTreatise treatise = treatiseSerice.getTreatise(treatiseId);
		if(treatise == null){
			LOG.error("detail : treatise["+treatiseId+"] is not found");
			return null;
		}
		
		 ModelAndView mv = new ModelAndView("institution_backend/treatise/detail");
		 List<InstitutionDepartment> depts=treatiseSerice.getDepartments(home.getInstitutionId());
		 mv.addObject("deptMap",extractDept(depts));
		 mv.addObject("treatise", treatise);
		 mv.addObject("returnPage",returnPage);
		 addBaseData(mv,home);
		 return mv;
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public JsonResult delete(@PathVariable("domain") String domain,@RequestParam("treatiseId[]")int[] treatiseId,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("delete:home is null["+domain+"]");
			return new JsonResult("找不到组织机构");
		}
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("delete:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		if(CommonUtils.isNull(treatiseId)){
			return new JsonResult("参数缺失");
		}
		LOG.info("delete treatise ref["+Arrays.toString(treatiseId)+"]");
		treatiseSerice.delete(home.getInstitutionId(),treatiseId);
		return new JsonResult();
	}
	
	/**
	 * 添加作者
	 * @param domain
	 * @param author
	 * @param request
	 * @return
	 */
	@RequestMapping("author/create")
	@ResponseBody
	public JsonResult createAuthor(@PathVariable("domain")String domain,InstitutionTreatiseAuthor author,HttpServletRequest request){
		InstitutionHome home = homeService.getInstitutionByDomain(domain);
		if(home == null){
			LOG.error("author.create:home is null ["+domain+"]");
			return new JsonResult("找不到组织结构");
		}
		
		int uid = SessionUtils.getUserId(request);
		author.setCreator(uid);
		
		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("author.create:user["+uid+"] is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		
		treatiseSerice.createAuthor(author);
		
		JsonResult result = new JsonResult();
		result.setData(author);
		return result;
	}
	
	@RequestMapping("author/{treatiseId}/{authorId}")
	@ResponseBody
	public JsonResult authorDetail(@PathVariable("domain") String domain,@PathVariable("treatiseId") int treatiseId,@PathVariable("authorId") int authorId,HttpServletRequest request){
		InstitutionHome home = homeService.getInstitutionByDomain(domain);
		if(home == null){
			LOG.error("submit :home is null["+domain+"]");
			return new JsonResult("找不到机构主页");
		}
		
		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("submit : user["+SessionUtils.getUserId(request)+"] is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		
		
		InstitutionTreatiseAuthor author = treatiseSerice.getAuthorById(treatiseId, authorId);
		if(author == null){
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
		
		JsonResult result = new JsonResult();
		result.setData(jsonObj);
		return result;
	}
	
	/**
	 * 查询论著作者
	 * @param domain
	 * @param treatiseId
	 * @param request
	 * @return
	 */
	@RequestMapping("authors/{treatiseId}")
	@ResponseBody
	public JsonResult getAuthors(@PathVariable("domain") String domain,
			@PathVariable("treatiseId") int treatiseId,HttpServletRequest request){
		InstitutionHome home = homeService.getInstitutionByDomain(domain);
		if(home == null){
			LOG.error("authors :home is null["+domain+"]");
			return new JsonResult("找不到机构主页");
		}
		
		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("authors : user["+SessionUtils.getUserId(request)+"] is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		
		List<InstitutionTreatiseAuthor> authors = treatiseSerice.getAuthorByTreatiseId(treatiseId);
		JsonResult result = new JsonResult();
		result.setData(authors);
		return result;
	}

	/**
	 * 添加/更改论著提交
	 * @param treatise
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
			InstitutionTreatise treatise,
			@PathVariable("domain")String domain,
			@RequestParam("uid[]") int[] authorIds,
			@RequestParam("order[]") int[] order,
			@RequestParam("communicationAuthor[]") boolean[] communicationAuthors,
			@RequestParam("authorStudent[]") boolean[] authorStudents,
			@RequestParam("authorTeacher[]") boolean[] authorTeachers,
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
		
		int treatiseId = treatise.getId();
		if(treatiseId == 0){
			treatiseSerice.create(treatise,home.getInstitutionId(), authorIds, order, communicationAuthors, authorStudents, authorTeachers);
		}else{
			treatiseSerice.update(treatise, home.getInstitutionId(), authorIds, order, communicationAuthors, authorStudents, authorTeachers);
		}
		
		return new JsonResult();
	}
	/**
	 * 显示导入论著excel文件页面
	 * @return
	 */
	@RequestMapping("import")
	@ResponseBody
	public ModelAndView showImportExceltex(HttpServletRequest request, @PathVariable("domain")String domain) {
		ModelAndView mv = new ModelAndView("institution_backend/treatise/addExcelTreatise");
//		mv.addObject("active",request.getRequestURI().replace(request.getContextPath(), ""));
//		mv.addObject("titleUser",homeService.getSimpleUserByDomain(domain));
		mv.addObject("domain",domain);
		return mv;
	}
	/**
	 * 批量导入论著
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "func=uploadExceltex", headers = { "X-File-Name" })
	public void uploadExceltex(@PathVariable("domain")String domain,HttpServletRequest request, HttpServletResponse response) throws IOException {
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("add:home is null["+domain+"]");
		}
		
		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("add:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
		}
		InputStream ins = request.getInputStream();
		ExcelReader er = new ExcelReader();
		List<InstitutionTreatise> treatises = er.analyzeTreatise(ins);
		List<InstitutionDepartment> depts=treatiseSerice.getDepartments(home.getInstitutionId());
		Map<Integer,InstitutionOptionVal> publishers= optionValService.getMapByOptionId(InstitutionOption.TREATISE_PUBLISHER, home.getInstitutionId());
		//导入到数据库
		
		for (InstitutionTreatise treatise : treatises) {
			treatise.setInstitutionId(home.getInstitutionId());
			treatise.setDepartId(getDeptIdByName(depts,treatise));
			treatise.setPublisher(getPubIdByName(publishers,treatise));
		}
		treatiseSerice.insertTreatises(treatises);
		for (InstitutionTreatise treatise : treatises) {
			treatiseSerice.insertRef(treatise);
		}
		
		JsonResult jr=new JsonResult();
			jr.setSuccess(true);
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setCharacterEncoding("UTF-8");
		//IE兼容，不这么整，会下载json
		try{
			JSONHelper.writeJSONObject(response, jr.toJSON());
		}catch(Exception e){
			LOG.error(e.getMessage(),e);
		}
		
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
