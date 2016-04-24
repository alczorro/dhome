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
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.JSONHelper;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.institution.InstitutionAttachment;
import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionAward;
import net.duckling.dhome.domain.institution.InstitutionAwardAuthor;
import net.duckling.dhome.domain.institution.InstitutionOption;
import net.duckling.dhome.domain.institution.InstitutionOptionVal;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.JsonResult;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendAwardService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionOptionValService;
import net.duckling.dhome.service.IInstitutionVmtService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.impl.ClbFileService;
import net.duckling.dhome.service.impl.VMTMessageHandlerImpl;
import net.duckling.vmt.api.impl.GroupService;
import net.duckling.vmt.api.impl.OrgService;
import net.duckling.vmt.api.impl.UserService;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.vlabs.rest.server.annotation.Init;

@Controller
@NPermission(role = "admin", authenticated=true)
@RequestMapping("/institution/{domain}/backend/award")
public class InstitutionBackEndAwardController {
	private static final Logger LOG=Logger.getLogger(InstitutionBackEndAwardController.class);
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
	private IInstitutionBackendAwardService awardSerice;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private VMTMessageHandlerImpl vmtMessageHandleImpl;
	@Autowired
    private ClbFileService resourceService;
	
	@Autowired
	private IInstitutionOptionValService optionValService;

	private boolean validatePrefix(String fileName){
		String lower=fileName.toLowerCase();
		return lower.endsWith(".pdf")||lower.endsWith(".doc")||lower.endsWith(".docx");
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
		
		ModelAndView mv=new ModelAndView("institution_backend/award/list");
		PageResult<InstitutionAward> result=awardSerice.getAwards(home.getInstitutionId(), page,condition);
		mv.addObject("page",result);
		
		List<Integer> awardIds=CommonUtils.extractSthField(result.getDatas(),"id");
		if(!CommonUtils.isNull(awardIds)){
			Map<Integer, List<InstitutionAwardAuthor>> authorMap=awardSerice.getAuthorsMap(awardIds);
			mv.addObject("authorMap",authorMap);
		}
		
	

		Map<Integer,Integer> gradeMap=awardSerice.getGradesMap(home.getInstitutionId());
		mv.addObject("gradeMap",gradeMap);
		
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
		
		addBaseData(mv, home);
		addDeptMap(mv, home);
		return mv;
	}
	
	private void addBaseData(ModelAndView mv,InstitutionHome home){
		Map<Integer,InstitutionOptionVal> awardNames= optionValService.getMapByOptionId(InstitutionOption.AWARD_NAME, home.getInstitutionId());
		mv.addObject("awardNames",awardNames);
		
		//类别
		Map<Integer,InstitutionOptionVal> types= optionValService.getMapByOptionId(InstitutionOption.AWARD_TYPE, home.getInstitutionId());
		mv.addObject("types",types);

		//等级
		Map<Integer,InstitutionOptionVal> grades= optionValService.getMapByOptionId(InstitutionOption.AWARD_GRADE, home.getInstitutionId());
		mv.addObject("grades",grades);
	}
	
	/**
	 * 添加奖励
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
		
	    ModelAndView mv = new ModelAndView("institution_backend/award/add");
	    mv.addObject("op", "add");
	    mv.addObject("domain",domain);
	    
		addBaseData(mv, home);
		addDeptMap(mv, home);
	    return mv;
	}
	
	
	@RequestMapping("update/{awardId}")
	public ModelAndView update(
			@PathVariable("domain") String domain,
			@PathVariable("awardId") int awardId,
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
		
		InstitutionAward award = awardSerice.getAward(awardId);
		if(award == null){
			LOG.error("update : award["+awardId+"] is not found");
			return null;
		}
		
	    ModelAndView mv = new ModelAndView("institution_backend/award/add");
	    mv.addObject("op", "update");
	    mv.addObject("award", award);
	    mv.addObject("returnPage",returnPage);

		addBaseData(mv, home);
		addDeptMap(mv, home);
		return mv;
	}
	
	@RequestMapping("detail/{awardId}")
	public ModelAndView detail(
			@PathVariable("domain") String domain,
			@PathVariable("awardId") int awardId,
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
		
		InstitutionAward award = awardSerice.getAward(awardId);
		if(award == null){
			LOG.error("detail : award["+awardId+"] is not found");
			return null;
		}
		
		 ModelAndView mv = new ModelAndView("institution_backend/award/detail");
		 mv.addObject("award", award);
		 mv.addObject("returnPage",returnPage);
		
		 addBaseData(mv, home);
		 addDeptMap(mv, home);
		 return mv;
	}
	
	@RequestMapping(value="/upload",method = RequestMethod.POST,headers = { "X-File-Name" })
	@ResponseBody
	public JsonResult uploadXls(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestHeader("X-File-Name") String fileName) throws IOException{
//		if(!validatePrefix(fileName)){
//			return new JsonResult("请上传docx,doc,pdf格式文件");
//		}
		if(request.getContentLength()<=0){
			return new JsonResult("请勿上传空文件");
		}
		int clbId=resourceService.createFile(URLDecoder.decode(fileName, "UTF-8"), request.getContentLength(), request.getInputStream());
		JsonResult jr=new JsonResult();
		//jr.setData(clbId);
		InstitutionAttachment attachment = new InstitutionAttachment();
		attachment.setCreateTime(new Timestamp(System.currentTimeMillis()));
		attachment.setFileName(fileName);
		attachment.setClbId(clbId);
		jr.setData(attachment);
		return jr;
	}
	
	@RequestMapping(value="/upload",method = RequestMethod.POST)
	@ResponseBody
	public void uploadXls(
			@RequestParam("qqfile") MultipartFile uplFile,
			HttpServletResponse response) throws IOException {
		JsonResult jr=new JsonResult();
//		if(!validatePrefix(uplFile.getOriginalFilename())){
//			jr.setDesc("请上传docx,doc,pdf格式文件");
//		}else 
		if(uplFile.getSize()<=0){
			jr.setDesc("请勿上传空文件");
		}else{
			int clbId=resourceService.createFile(URLDecoder.decode(uplFile.getOriginalFilename(), "UTF-8"), (int)uplFile.getSize(), uplFile.getInputStream());
			jr.setSuccess(true);
			//jr.setData(clbId);
			InstitutionAttachment attachment = new InstitutionAttachment();
			attachment.setCreateTime(new Timestamp(System.currentTimeMillis()));
			attachment.setFileName(uplFile.getOriginalFilename());
			attachment.setClbId(clbId);
			jr.setData(attachment);
		}
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
//	
//	@RequestMapping(value="upload",method = RequestMethod.POST)
//	@ResponseBody
//	public void uploadXls(
//			@RequestParam("qqfile") MultipartFile uplFile,
//			HttpServletResponse response) throws IOException {
//		JsonResult jr=new JsonResult();
////		if(!validatePrefix(uplFile.getOriginalFilename())){
////			jr.setDesc("请上传docx,doc,pdf格式文件");
////		}else 
//		if(uplFile.getSize()<=0){
//			jr.setDesc("文件错误");
//		}else{
//			int clbId=resourceService.createFile(uplFile.getOriginalFilename(), (int)uplFile.getSize(), uplFile.getInputStream());
//			jr.setSuccess(true);
//			
//			InstitutionAttachment attachment = new InstitutionAttachment();
//			attachment.setCreateTime(new Timestamp(System.currentTimeMillis()));
//			attachment.setFileName(uplFile.getOriginalFilename());
//			attachment.setClbId(clbId);
//			jr.setData(attachment);
//		}
//		response.setContentType("text/html");
//		response.setStatus(HttpServletResponse.SC_OK);
//		response.setCharacterEncoding("UTF-8");
//		//IE兼容，不这么整，会下载json
//		try{
//			JSONHelper.writeJSONObject(response, jr.toJSON());
//		}catch(Exception e){
//			LOG.error(e.getMessage(),e);
//		}
//	}
	
	@RequestMapping("delete")
	@ResponseBody
	public JsonResult delete(@PathVariable("domain") String domain,@RequestParam("awardId[]")int[] awardId,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("delete:home is null["+domain+"]");
			return new JsonResult("找不到组织机构");
		}
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("delete:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		if(CommonUtils.isNull(awardId)){
			return new JsonResult("参数缺失");
		}
		LOG.info("delete award ref["+Arrays.toString(awardId)+"]");
		awardSerice.delete(home.getInstitutionId(),awardId);
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
	public JsonResult createAuthor(@PathVariable("domain")String domain,InstitutionAwardAuthor author,HttpServletRequest request){
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
		
		awardSerice.createAuthor(author);
		
		JsonResult result = new JsonResult();
		result.setData(author);
		return result;
	}
	
	@RequestMapping("author/{awardId}/{authorId}")
	@ResponseBody
	public JsonResult authorDetail(@PathVariable("domain") String domain,@PathVariable("awardId") int awardId,@PathVariable("authorId") int authorId,HttpServletRequest request){
		InstitutionHome home = homeService.getInstitutionByDomain(domain);
		if(home == null){
			LOG.error("submit :home is null["+domain+"]");
			return new JsonResult("找不到机构主页");
		}
		
		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("submit : user["+SessionUtils.getUserId(request)+"] is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		
		
		InstitutionAwardAuthor author = awardSerice.getAuthorById(awardId, authorId);
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
	 * 查询奖励作者
	 * @param domain
	 * @param awardId
	 * @param request
	 * @return
	 */
	@RequestMapping("authors/{awardId}")
	@ResponseBody
	public JsonResult getAuthors(@PathVariable("domain") String domain,
			@PathVariable("awardId") int awardId,HttpServletRequest request){
		InstitutionHome home = homeService.getInstitutionByDomain(domain);
		if(home == null){
			LOG.error("authors :home is null["+domain+"]");
			return new JsonResult("找不到机构主页");
		}
		
		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("authors : user["+SessionUtils.getUserId(request)+"] is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		
		List<InstitutionAwardAuthor> authors = awardSerice.getAuthorByawardId(awardId);
		JsonResult result = new JsonResult();
		result.setData(authors);
		return result;
	}
	
	
	/**
	 * 查询附件
	 * @param domain
	 * @param awardId
	 * @param request
	 * @return
	 */
	@RequestMapping("attchments/{awardId}")
	@ResponseBody
	public JsonResult getAttchments(@PathVariable("domain") String domain,
			@PathVariable("awardId") int awardId,HttpServletRequest request){
		InstitutionHome home = homeService.getInstitutionByDomain(domain);
		if(home == null){
			LOG.error("authors :home is null["+domain+"]");
			return new JsonResult("找不到机构主页");
		}
		
		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("authors : user["+SessionUtils.getUserId(request)+"] is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		
		List<InstitutionAttachment> attachments = awardSerice.getAttchments(awardId);
		JsonResult result = new JsonResult();
		result.setData(attachments);
		return result;
	}

	/**
	 * 添加/更改奖励提交
	 * @param award
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
			InstitutionAward award,
			@PathVariable("domain")String domain,
			@RequestParam("uid[]") int[] authorIds,
			@RequestParam("order[]") int[] order,
			@RequestParam("communicationAuthor[]") boolean[] communicationAuthors,
			@RequestParam("authorStudent[]") boolean[] authorStudents,
			@RequestParam("authorTeacher[]") boolean[] authorTeachers,
			@RequestParam("clbId[]")  int[] clbIds,
			@RequestParam("fileName[]") String[] fileNames,
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
		
		int awardId = award.getId();
		if(awardId == 0){
			awardSerice.create(award,home.getInstitutionId(), authorIds, order, communicationAuthors, authorStudents, authorTeachers,clbIds,fileNames);
		}else{
			awardSerice.update(award,home.getInstitutionId(),authorIds, order, communicationAuthors, authorStudents, authorTeachers,clbIds,fileNames);
		}
		
		return new JsonResult();
	}

}
