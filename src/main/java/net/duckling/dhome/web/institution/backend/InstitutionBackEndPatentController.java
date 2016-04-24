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
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.ExcelReader;
import net.duckling.dhome.common.util.JSONHelper;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.institution.InstitutionAttachment;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.dhome.domain.institution.InstitutionOption;
import net.duckling.dhome.domain.institution.InstitutionOptionVal;
import net.duckling.dhome.domain.institution.InstitutionPatent;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionTreatise;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.JsonResult;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.service.IInstitutionBackendPatentService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionOptionValService;
import net.duckling.dhome.service.impl.ClbFileService;

import org.apache.log4j.Logger;
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
import org.springframework.web.servlet.view.RedirectView;


@Controller
@NPermission(role = "admin", authenticated=true)
@RequestMapping("/institution/{domain}/backend/patent")

public class InstitutionBackEndPatentController {

	private static final Logger LOG=Logger.getLogger(InstitutionBackEndPatentController.class);
	
	@Autowired
	private IInstitutionBackendPatentService patentService;
	@Autowired
	private IInstitutionHomeService homeService;
	@Autowired
	private IInstitutionBackendService backendService;
	@Autowired
    private ClbFileService resourceService;
	@Autowired
	private IInstitutionOptionValService optionValService;
	
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

//	private boolean validatePrefix(String fileName){
//		String lower=fileName.toLowerCase();
//		return lower.endsWith(".pdf")||lower.endsWith(".doc")||lower.endsWith(".docx");
//	}
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
	
	@RequestMapping("list/{page}")
	public ModelAndView list(@PathVariable("domain")String domain,
			@PathVariable("page")int page,
			SearchInstitutionCondition condition,
			HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("list:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return new ModelAndView("institution_backend/permission");
		}
		
		ModelAndView mv=new ModelAndView("institution_backend/patent/list");
		if(home==null||page<0){
			return null;
		}
		PageResult<InstitutionPatent> result=patentService.getPatent(home.getInstitutionId(), page,condition);
		Map<Integer,Integer> pubGradeMap=patentService.getGradesMap(home.getInstitutionId());
		mv.addObject("gradeMap",pubGradeMap);
		mv.addObject("condition",condition);
		mv.addObject("page",result);
		mv.addObject("domain",domain);
		addBaseData(mv,home);
		
//		List<Integer> topicId=CommonUtils.extractSthField(result.getDatas(),"id");
//		Map<Integer,List<InstitutionAuthor>> authorMap=backEndTopicService.getListAuthorsMap(topicId);
//		mv.addObject("authorMap",authorMap);
		return mv;
	}
	
	private void addBaseData(ModelAndView mv,InstitutionHome home){
		//类别
		Map<Integer,InstitutionOptionVal> types= optionValService.getMapByOptionId(InstitutionOption.PATENT_TYPE, home.getInstitutionId());
		mv.addObject("types",types);
		
		//等级
		Map<Integer,InstitutionOptionVal> grades= optionValService.getMapByOptionId(InstitutionOption.PATENT_GRADE, home.getInstitutionId());
		mv.addObject("grades",grades);
		
		//专利状态
		Map<Integer,InstitutionOptionVal> statusMap= optionValService.getMapByOptionId(InstitutionOption.PATENT_STATUS, home.getInstitutionId());
		mv.addObject("statusMap",statusMap);
	}
	@RequestMapping("delete")
	public ModelAndView delete(@PathVariable("domain")String domain,@RequestParam("id[]")int[] id,@RequestParam("page")String page,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		patentService.deleteByPatentId(id);
//		patentService.deleteByTopicId(id);
		return new ModelAndView(new RedirectView(request.getContextPath()+"/institution/"+domain+"/backend/patent/list/"+page));
	}
	@RequestMapping("submit")
	public JsonResult submit(InstitutionPatent patent,@PathVariable("domain")String domain,
			@RequestParam("uid[]")int[] uid,
			@RequestParam("order[]")int[] order,
			@RequestParam("clbId[]")  int[] clbIds,
			@RequestParam("fileName[]") String[] fileNames,
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
		patent.setInstitutionId(home.getInstitutionId());
		if(patent.getId()==0){
			patentService.insert(patent, uid, order, clbIds, fileNames);
//			patentService.insertPatent(patent);
//			patentService.createRef(patent.getId(), uid, order);
//			patentService.insertAttachments(patent.getId(), clbIds, fileNames);
		}else{
			patentService.updatePatent(patent.getId(), patent);
			patentService.deleteRef(patent.getId());
			patentService.createRef(patent.getId(), uid, order);
			patentService.deleteAttachments(patent.getId());
			patentService.insertAttachments(patent.getId(), clbIds, fileNames);
		}
		return new JsonResult();
	}
	
	@RequestMapping("edit/{id}")
	@ResponseBody
	public ModelAndView edit(@RequestParam("returnPage") int returnPage,@PathVariable("domain")String domain,@PathVariable("id")int id,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			return null;
		} 
		InstitutionPatent patent = patentService.getPatentById(id);
		int objType=2;
		List<InstitutionAttachment> files = patentService.getAttachment(objType, id);
		if(patent==null){
			LOG.error("update:paper["+id+"] is not found!");
			return null;
		}
		ModelAndView mv=new ModelAndView("institution_backend/patent/modify");
		mv.addObject("op","update");
		mv.addObject("patent",patent);
		mv.addObject("files",files);
		mv.addObject("returnPage",returnPage);
		addBaseData(mv,home);
		addDeptMap(mv,home);
		return mv;
	}
	@RequestMapping("update/{id}")
	@ResponseBody
	public ModelAndView update(InstitutionPatent patent,@PathVariable("domain")String domain,@PathVariable("id")int id,HttpServletRequest request){
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
		
		patentService.updatePatent(id, patent);
		return new ModelAndView(new RedirectView(request.getContextPath()+"/institution/"+domain+"/backend/patent/modify"));
	}
	@RequestMapping("create")
	public ModelAndView create(@PathVariable("domain")String domain,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("create:home is null["+domain+"]");
			return null;
		}
		
		ModelAndView mv=new ModelAndView("institution_backend/patent/modify");
		mv.addObject("patentCount",patentService.getPatentCount(home.getInstitutionId()));
		mv.addObject("op","create");
		mv.addObject("domain",domain);
		addBaseData(mv,home);
		addDeptMap(mv,home);
		return mv;
	}
	
	@RequestMapping("/authors/{patentId}")
	@ResponseBody
	public JsonResult getAuthors(@PathVariable("domain")String domain,@PathVariable("patentId")int patentId,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("update:home is null["+domain+"]");
			return new JsonResult("not found institution");
		}
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("update:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}

		List<InstitutionAuthor> authors=patentService.getAuthorsByPatentId(patentId);
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
		patentService.createAuthor(author);
		JsonResult jr=new JsonResult();
		jr.setData(author);
		return jr;
	}
	/**
	 * 查询附件
	 * @param domain
	 * @param patentId
	 * @param request
	 * @return
	 */
	@RequestMapping("attachments/{patentId}")
	@ResponseBody
	public JsonResult getAttchments(@PathVariable("domain") String domain,
			@PathVariable("patentId") int patentId,HttpServletRequest request){
		InstitutionHome home = homeService.getInstitutionByDomain(domain);
		if(home == null){
			LOG.error("authors :home is null["+domain+"]");
			return new JsonResult("找不到机构主页");
		}
		
		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("authors : user["+SessionUtils.getUserId(request)+"] is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		int objType=2;
		List<InstitutionAttachment> attachments = patentService.getAttachment(objType, patentId);
		JsonResult result = new JsonResult();
		result.setData(attachments);
		return result;
	}
	@RequestMapping("detail/{patentId}")
	@ResponseBody
	public ModelAndView detail(@RequestParam("returnPage") int returnPage,@PathVariable("domain")String domain,@PathVariable("patentId")int patentId,HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution_backend/patent/detail");
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			return null;
		} 
		InstitutionPatent patent = patentService.getPatentById(patentId);
		mv.addObject("patent",patent);
		mv.addObject("returnPage",returnPage);
		mv.addObject("domain",domain);
		addBaseData(mv,home);
		addDeptMap(mv,home);
		return mv;
	}
	/**
	 * 显示导入论著excel文件页面
	 * @return
	 */
//	@RequestMapping("import")
//	@ResponseBody
//	public ModelAndView showImportExceltex(HttpServletRequest request, @PathVariable("domain")String domain) {
//		ModelAndView mv = new ModelAndView("institution_backend/treatise/addExcelPatent");
////		mv.addObject("active",request.getRequestURI().replace(request.getContextPath(), ""));
////		mv.addObject("titleUser",homeService.getSimpleUserByDomain(domain));
//		mv.addObject("domain",domain);
//		return mv;
//	}
//	/**
//	 * 批量导入论著
//	 * @param domain
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(params = "func=uploadExceltex", headers = { "X-File-Name" })
//	public void uploadExceltex(@PathVariable("domain")String domain,HttpServletRequest request, HttpServletResponse response) throws IOException {
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			LOG.error("add:home is null["+domain+"]");
//		}
//		
//		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
//			LOG.error("add:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//		}
//		InputStream ins = request.getInputStream();
//		ExcelReader er = new ExcelReader();
//		List<InstitutionTreatise> treatises = er.analyzeTreatise(ins);
//		List<InstitutionDepartment> depts=treatiseSerice.getDepartments(home.getInstitutionId());
//		//导入到数据库
//		
//		for (InstitutionTreatise treatise : treatises) {
//			treatise.setInstitutionId(home.getInstitutionId());
//			treatise.setDepartId(getDeptIdByName(depts,treatise));
//			treatise.setPublisher(getPubIdByName(publishers,treatise));
//		}
//		treatiseSerice.insertTreatises(treatises);
//		for (InstitutionTreatise treatise : treatises) {
//			treatiseSerice.insertRef(treatise);
//		}
//		
//		JsonResult jr=new JsonResult();
//			jr.setSuccess(true);
//		response.setContentType("text/html");
//		response.setStatus(HttpServletResponse.SC_OK);
//		response.setCharacterEncoding("UTF-8");
//		//IE兼容，不这么整，会下载json
//		try{
//			JSONHelper.writeJSONObject(response, jr.toJSON());
//		}catch(Exception e){
//			LOG.error(e.getMessage(),e);
//		}
//		
//	}
}
