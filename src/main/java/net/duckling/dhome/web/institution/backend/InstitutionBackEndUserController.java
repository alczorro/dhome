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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.common.util.CommonUtils;
import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.auth.sso.Constants;
import net.duckling.dhome.common.config.AppConfig;
import net.duckling.dhome.common.exception.BibResolveFailedException;
import net.duckling.dhome.common.util.BibReader;
import net.duckling.dhome.common.util.DateUtils;
import net.duckling.dhome.common.util.ExcelReader;
import net.duckling.dhome.common.util.JSONHelper;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.institution.InstitutionAcademic;
import net.duckling.dhome.domain.institution.InstitutionAcademicAuthor;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionAward;
import net.duckling.dhome.domain.institution.InstitutionAwardAuthor;
import net.duckling.dhome.domain.institution.InstitutionCopyright;
import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionMemberFromVmt;
import net.duckling.dhome.domain.institution.InstitutionOption;
import net.duckling.dhome.domain.institution.InstitutionOptionVal;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.InstitutionPatent;
import net.duckling.dhome.domain.institution.InstitutionPeriodical;
import net.duckling.dhome.domain.institution.InstitutionPeriodicalAuthor;
import net.duckling.dhome.domain.institution.InstitutionPublication;
import net.duckling.dhome.domain.institution.InstitutionTopic;
import net.duckling.dhome.domain.institution.InstitutionTraining;
import net.duckling.dhome.domain.institution.InstitutionTreatise;
import net.duckling.dhome.domain.institution.InstitutionTreatiseAuthor;
import net.duckling.dhome.domain.institution.InstitutionVmtInfo;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.JsonResult;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.domain.people.Education;
import net.duckling.dhome.domain.people.Home;
import net.duckling.dhome.domain.people.Paper;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.Work;
import net.duckling.dhome.service.IEducationService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendAcademicService;
import net.duckling.dhome.service.IInstitutionBackendAwardService;
import net.duckling.dhome.service.IInstitutionBackendCopyrightService;
import net.duckling.dhome.service.IInstitutionBackendPaperService;
import net.duckling.dhome.service.IInstitutionBackendPatentService;
import net.duckling.dhome.service.IInstitutionBackendPeriodicalService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionBackendTopicService;
import net.duckling.dhome.service.IInstitutionBackendTrainingService;
import net.duckling.dhome.service.IInstitutionBackendTreatiseService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionOptionValService;
import net.duckling.dhome.service.IInstitutionVmtService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.IWorkService;
import net.duckling.dhome.service.impl.ClbFileService;
import net.duckling.dhome.service.impl.VMTMessageHandlerImpl;
import net.duckling.vmt.api.domain.VmtUser;
import net.duckling.vmt.api.impl.GroupService;
import net.duckling.vmt.api.impl.OrgService;
import net.duckling.vmt.api.impl.UserService;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.google.gson.Gson;

import cn.vlabs.rest.ServiceException;

@Controller
@NPermission(role = "admin", authenticated=true)
@RequestMapping("/institution/{domain}/backend/member")
public class InstitutionBackEndUserController {
	private static final Logger LOG=Logger.getLogger(InstitutionBackEndUserController.class);
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
	private IUserService userService;
	@Autowired
	private IInstitutionBackendService backEndService;
	
	@Autowired
	private IEducationService educationService;
	
	@Autowired
	private IWorkService workService;
	
	@Autowired 
	private  IInstitutionBackendTreatiseService treatiseService;
	
	@Autowired
	private IInstitutionBackendAwardService awardService;
	
	@Autowired
	private IInstitutionBackendAcademicService academicService;
	
	@Autowired
	private IInstitutionBackendPeriodicalService periodicalService;
	
	@Autowired
	private IInstitutionBackendCopyrightService copyrightService;
	
	@Autowired
	private IInstitutionBackendPatentService patentService; 
	
	@Autowired
	private IInstitutionBackendTopicService topicService;
	
	@Autowired
	private IInstitutionBackendTrainingService trainingService; 
	
	@Autowired
	private IInstitutionBackendPaperService paperService;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
    private ClbFileService resourceService;
	
	@Autowired
	private AppConfig config;

	@Autowired
	private IInstitutionOptionValService optionValService;
	
	private void addBaseData(ModelAndView mv,InstitutionHome home){
		//部门
		List<InstitutionDepartment> depts=paperService.getDepartments(home.getInstitutionId());
	    mv.addObject("deptMap",extractDept(depts));
		
		//学科方向
		Map<Integer,InstitutionOptionVal> disciplineOrientations= optionValService.getMapByOptionId(InstitutionOption.PAPER_DISCIPLINE_ORIENTATION, home.getInstitutionId());
		mv.addObject("disciplineOrientations",disciplineOrientations);
		//论著出版社
		Map<Integer,InstitutionOptionVal> publishers= optionValService.getMapByOptionId(InstitutionOption.TREATISE_PUBLISHER, home.getInstitutionId());
		mv.addObject("publishers",publishers);
		//奖励名称
		Map<Integer,InstitutionOptionVal> awardNames= optionValService.getMapByOptionId(InstitutionOption.AWARD_NAME, home.getInstitutionId());
		mv.addObject("awardNames",awardNames);
		
		//类别
		Map<Integer,InstitutionOptionVal> awardTypes= optionValService.getMapByOptionId(InstitutionOption.AWARD_TYPE, home.getInstitutionId());
		mv.addObject("awardTypes",awardTypes);

		//等级
		Map<Integer,InstitutionOptionVal> awardGrades= optionValService.getMapByOptionId(InstitutionOption.AWARD_GRADE, home.getInstitutionId());
		mv.addObject("awardGrades",awardGrades);
	}
	private Map<Integer,InstitutionPublication> extractPub(List<InstitutionPublication> pubs){
		if(CommonUtils.isNull(pubs)){
			return Collections.emptyMap();
		}
		Map<Integer,InstitutionPublication> map=new HashMap<Integer,InstitutionPublication>();
		for(InstitutionPublication pub:pubs){
			map.put(pub.getId(),pub);
		}
		return map;
	}
	
	@RequestMapping(value="/upload",method = RequestMethod.POST,headers = { "X-File-Name" })
	@ResponseBody
	public JsonResult uploadXls(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestHeader("X-File-Name") String fileName) throws IOException{
		if(!validatePrefix(fileName)){
			return new JsonResult("请上传docx,doc,pdf格式文件");
		}
		if(request.getContentLength()<=0){
			return new JsonResult("请勿上传空文件");
		}
		int clbId=resourceService.createFile(URLDecoder.decode(fileName, "UTF-8"), request.getContentLength(), request.getInputStream());
		JsonResult jr=new JsonResult();
		jr.setData(clbId);
		return jr;
	}
	@RequestMapping(value="/upload",method = RequestMethod.POST)
	@ResponseBody
	public void uploadXls(
			@RequestParam("qqfile") MultipartFile uplFile,
			HttpServletResponse response) throws IOException {
		JsonResult jr=new JsonResult();
		if(!validatePrefix(uplFile.getOriginalFilename())){
			jr.setDesc("请上传docx,doc,pdf格式文件");
		}else if(uplFile.getSize()<=0){
			jr.setDesc("请上传docx,doc,pdf格式文件");
		}else{
			int clbId=resourceService.createFile(URLDecoder.decode(uplFile.getOriginalFilename(), "UTF-8"), (int)uplFile.getSize(), uplFile.getInputStream());
			jr.setSuccess(true);
			jr.setData(clbId);
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
	
	@Autowired
	private VMTMessageHandlerImpl vmtMessageHandleImpl;
	private Map<Integer,InstitutionDepartment> extractDept(List<InstitutionDepartment> depts){
		if(CommonUtils.isNull(depts)){
			return Collections.emptyMap();
		}
		Map<Integer,InstitutionDepartment> map=new HashMap<Integer,InstitutionDepartment>();
		for(InstitutionDepartment dept:depts){
			map.put(dept.getId(),dept);
		}
		return map;
	}
	private int getDeptIdByName(List<InstitutionDepartment> depts,InstitutionMemberFromVmt member){
		for (InstitutionDepartment dept : depts) {
			if(dept.getName().equals(member.getDepartment())){
				return dept.getId();
			}
		}
		return 0;
		
	}
	private List<Integer> getDeptId(List<InstitutionMemberFromVmt> member){
		List<Integer> deptIds=new ArrayList<Integer>();
		if(CommonUtils.isNull(member)){
			return deptIds;
		}
		for(InstitutionMemberFromVmt vmt:member){
			deptIds.add(vmt.getDepartId());
		}
		return deptIds;
	}
	private Map<Integer,Home> extractHome(List<Home> homes){
		if(CommonUtils.isNull(homes)){
			return Collections.emptyMap();
		}
		Map<Integer,Home> map=new HashMap<Integer,Home>();
		for(Home home:homes){
			map.put(home.getUid(),home);
		}
		return map;
	}
	private Map<String,SimpleUser> extract(List<SimpleUser> users){
		if(CommonUtils.isNull(users)){
			return Collections.emptyMap();
		}
		Map<String,SimpleUser> map=new HashMap<String,SimpleUser>();
		for(SimpleUser user:users){
			map.put(user.getEmail(),user);
		}
		return map;
	}
	private List<String> getEmail(List<InstitutionMemberFromVmt> vmts){
		List<String> email=new ArrayList<String>();
		if(CommonUtils.isNull(vmts)){
			return email;
		}
		for(InstitutionMemberFromVmt vmt:vmts){
			email.add(vmt.getCstnetId());
		}
		return email;
	}
	private List<Integer> getUids(List<SimpleUser> users){
		if(CommonUtils.isNull(users)){
			return Collections.emptyList();
		}
		List<Integer> uids=new ArrayList<Integer>();
		for(SimpleUser user:users){
			uids.add(user.getId());
		}
		return uids;
	}
	@RequestMapping("list/{page}")
	public ModelAndView list(@PathVariable("domain")String domain,@PathVariable("page")int page,SearchInstitutionCondition condition,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null||page<0){
			return null;
		}
		
		if(!backEndService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("list:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return new ModelAndView("institution_backend/permission");
		}
		
		ModelAndView mv=new ModelAndView("institution_backend/member/list");
		PageResult<InstitutionMemberFromVmt> result=backEndService.getVmtMember(home.getInstitutionId(), page,condition);
		
		mv.addObject("page",result);
		List<SimpleUser> users=userService.getUserByEmails(getEmail(result.getDatas()));
		mv.addObject("userMap",extract(users));
		
		List<Home> homes=userHomeService.getDomainsByUids(getUids(users));
		mv.addObject("homeMap",extractHome(homes));
		
		List<InstitutionDepartment> depts=backEndService.getVmtDepartment(home.getInstitutionId());
		InstitutionDepartment department = new InstitutionDepartment();
		department.setShortName("全所");
		department.setId(-1);
		depts.add(0,department);
		mv.addObject("deptMap",extractDept(depts));
		mv.addObject("dept",depts);
		mv.addObject("domain",domain);
		
		Map<String, Integer> titles = backEndService.getTitlesMap(home.getInstitutionId(),-1);
		LinkedHashMap<String, Integer> titleMap = new LinkedHashMap<String, Integer>();
		titleMap.put("全部", 0);
		titleMap.putAll(titles);
		mv.addObject("titles",titleMap);
		
		String vmtMemberUrl = config.getUmtServerMemberURL();
		InstitutionVmtInfo vmtInfo = vmtService.getVmtInfoByInstitutionId(home.getInstitutionId());
        vmtMemberUrl +="?dn="+vmtInfo.getDn();
        mv.addObject("memberUrl",vmtMemberUrl);
        
        mv.addObject("condition",condition);
        mv.addObject("keywordDept",extractDept(depts).get(condition.getMemberDpetId()));
        mv.addObject("keywordTitle",(condition.getTitle()==null)?"全部":(condition.getTitle()));
		return mv;
	}
	/**
	 * 显示导入员工excel文件页面
	 * @return
	 */
	@RequestMapping("import")
	@ResponseBody
	public ModelAndView showImportExceltex(HttpServletRequest request, @PathVariable("domain")String domain) {
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		ModelAndView mv = new ModelAndView("institution_backend/member/addExcelMember");
//		mv.addObject("active",request.getRequestURI().replace(request.getContextPath(), ""));
//		mv.addObject("titleUser",homeService.getSimpleUserByDomain(domain));
		mv.addObject("domain",domain);
		String vmtMemberUrl = config.getUmtServerMemberURL();
		InstitutionVmtInfo vmtInfo = vmtService.getVmtInfoByInstitutionId(home.getInstitutionId());
        vmtMemberUrl +="?dn="+vmtInfo.getDn();
        mv.addObject("memberUrl",vmtMemberUrl);
        
        List<InstitutionDepartment> depts=backEndService.getVmtDepartment(home.getInstitutionId());
		mv.addObject("depts",extractDept(depts));
		return mv;
	}
	/**
	 * 批量导入员工
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "func=uploadExceltex",method = RequestMethod.POST, headers = { "X-File-Name" })
	public void uploadExceltex(@PathVariable("domain")String domain,
			@RequestHeader("X-File-Name") String fileName,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("add:home is null["+domain+"]");
		}
		
		if(!backEndService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("add:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
		}
		InputStream ins = request.getInputStream();
		ExcelReader er = new ExcelReader();
		List<InstitutionMemberFromVmt> member = er.analyzeMember(ins);
		List<InstitutionDepartment> depts=backEndService.getVmtDepartment(home.getInstitutionId());
		List<InstitutionMemberFromVmt> allMember = vmtService.getAllUser();
		//存放没有邮箱的用户
		List<InstitutionMemberFromVmt> noEmailMember=new ArrayList<InstitutionMemberFromVmt>();
		//存放excel有 数据库没有的员工
		List<InstitutionMemberFromVmt> notAddMember=new ArrayList<InstitutionMemberFromVmt>();
		//存放用户的sn
		List<String> sns=new ArrayList<String>();
		List<String> totalSn=new ArrayList<String>();
		for (InstitutionMemberFromVmt m : allMember) {
			totalSn.add(m.getSn());
		}
		for (InstitutionMemberFromVmt m : member) {
			sns.add(m.getSn());
			m.setInstitutionId(home.getInstitutionId());
			m.setDepartId(getDeptIdByName(depts, m));
			if(m.getSn()!=null&&!"".equals(m.getSn())){
				if(m.getCstnetId()==null || "".equals(m.getCstnetId())){
					noEmailMember.add(m);
				}
			}
			
			if(!totalSn.contains(m.getSn())&&m.getCstnetId()!=null && !"".equals(m.getCstnetId())){
				notAddMember.add(m);
			}
		}
		//导入到数据库
		vmtService.updateMember(member);
		vmtService.insertMembers(noEmailMember);
		//数据库中有，excel中没有的员工
		List<InstitutionMemberFromVmt> notWorkingMember=getNotWorkingMember(allMember,sns);
		for (InstitutionMemberFromVmt m : notWorkingMember) {
			m.setStatus("hides");
		}
		for (InstitutionMemberFromVmt m : notAddMember) {
			m.setStatus("adds");
		}
		vmtService.updateMemberStatus(notWorkingMember);
		
		JsonResult jr=new JsonResult();
//		if(!validatePrefix(uplFile.getOriginalFilename())){
//			jr.setDesc("请上传docx,doc,pdf格式文件");
//		}else if(uplFile.getSize()<=0){
//			jr.setDesc("请上传docx,doc,pdf格式文件");
//		}else{
//			int clbId=resourceService.createFile(uplFile.getOriginalFilename(), (int)uplFile.getSize(), uplFile.getInputStream());
			jr.setSuccess(true);
			Gson gson=new Gson();
			notWorkingMember.addAll(notAddMember);
			jr.setData(gson.toJson(notWorkingMember));
//			jr.setData(clbId);
//		}
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

	private List<InstitutionMemberFromVmt> getNotWorkingMember(List<InstitutionMemberFromVmt> allMember,List<String> sns){
		List<InstitutionMemberFromVmt> notWorkingMember=new ArrayList<InstitutionMemberFromVmt>();
		for (InstitutionMemberFromVmt member : allMember) {
			if(!sns.contains(member.getSn())){
				if(member.getJobStatus()==null||!member.getJobStatus().equals("不在职"))
					notWorkingMember.add(member);
			}
		}
		return notWorkingMember;
	}
	private boolean validatePrefix(String fileName){
		String lower=fileName.toLowerCase();
		return lower.endsWith(".pdf")||lower.endsWith(".doc")||lower.endsWith(".docx");
	}
	/**
	 * 上传bib文件，IE浏览器
	 * @param uplFile
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST, params = "func=uploadExceltex")
	public void uploadBibtex(@RequestParam("qqfile") MultipartFile uplFile, HttpServletRequest request,
			@PathVariable("domain")String domain,
			HttpServletResponse response) throws IOException {
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("add:home is null["+domain+"]");
		}
		
		if(!backEndService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("add:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
		}
		InputStream ins = uplFile.getInputStream();
		ExcelReader er = new ExcelReader();
		List<InstitutionMemberFromVmt> member = er.analyzeMember(ins);
		List<InstitutionDepartment> depts=backEndService.getVmtDepartment(home.getInstitutionId());
		List<InstitutionMemberFromVmt> allMember = vmtService.getAllUser();
		//存放没有邮箱的用户
				List<InstitutionMemberFromVmt> noEmailMember=new ArrayList<InstitutionMemberFromVmt>();
				//存放excel有 数据库没有的员工
				List<InstitutionMemberFromVmt> notAddMember=new ArrayList<InstitutionMemberFromVmt>();
				//存放用户的sn
				List<String> sns=new ArrayList<String>();
				List<String> totalSn=new ArrayList<String>();
				for (InstitutionMemberFromVmt m : allMember) {
					totalSn.add(m.getSn());
				}
				for (InstitutionMemberFromVmt m : member) {
					sns.add(m.getSn());
					m.setInstitutionId(home.getInstitutionId());
					m.setDepartId(getDeptIdByName(depts, m));
					if(m.getSn()!=null&&!"".equals(m.getSn())){
						if(m.getCstnetId()==null || "".equals(m.getCstnetId())){
							noEmailMember.add(m);
						}
					}
					
					if(!totalSn.contains(m)&&m.getCstnetId()!=null && !"".equals(m.getCstnetId())){
						notAddMember.add(m);
					}
				}
				//导入到数据库
				vmtService.updateMember(member);
				vmtService.insertMembers(noEmailMember);
				//数据库中有，excel中没有的员工
				List<InstitutionMemberFromVmt> notWorkingMember=getNotWorkingMember(allMember,sns);
				for (InstitutionMemberFromVmt m : notWorkingMember) {
					m.setStatus("hides");
				}
				for (InstitutionMemberFromVmt m : notAddMember) {
					m.setStatus("adds");
				}
				vmtService.updateMemberStatus(notWorkingMember);
				
				JsonResult jr=new JsonResult();
//				if(!validatePrefix(uplFile.getOriginalFilename())){
//					jr.setDesc("请上传docx,doc,pdf格式文件");
//				}else if(uplFile.getSize()<=0){
//					jr.setDesc("请上传docx,doc,pdf格式文件");
//				}else{
//					int clbId=resourceService.createFile(uplFile.getOriginalFilename(), (int)uplFile.getSize(), uplFile.getInputStream());
					jr.setSuccess(true);
					Gson gson=new Gson();
					notWorkingMember.addAll(notAddMember);
					jr.setData(gson.toJson(notWorkingMember));
			
//			jr.setData(clbId);
//		}
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
	
	/**
	 * 添加员工页面
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
		
		if(!backEndService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("add:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return null;
		}
		
	    ModelAndView mv = new ModelAndView("institution_backend/member/add");
	    mv.addObject("op", "add");
	    mv.addObject("domain",domain);
	    
		List<InstitutionDepartment> depts=backEndService.getVmtDepartment(home.getInstitutionId());
		Map<Integer, InstitutionDepartment> deptMap = extractDept(depts);
		mv.addObject("depts",depts);
		mv.addObject("deptId",deptMap.keySet());
	    return mv;
	}
	
	@RequestMapping("submit")
	@ResponseBody
	public JsonResult submit(
			InstitutionMemberFromVmt member,
			@PathVariable("domain")String domain,
			HttpServletRequest request){
		
		InstitutionHome home = homeService.getInstitutionByDomain(domain);
		if(home == null){
			LOG.error("submit :home is null["+domain+"]");
			return new JsonResult("找不到机构主页");
		}
		
		if(!backEndService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("submit : user["+SessionUtils.getUserId(request)+"] is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		member.setInstitutionId(home.getInstitutionId());
		backEndService.insertMember(member);
		return new JsonResult();
	}
	
	@RequestMapping("delete")
	public ModelAndView delete(@PathVariable("domain")String domain,@RequestParam("umtId[]")String[] umtId,@RequestParam("page")String page,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		if(!backEndService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			return null;
		}
		InstitutionVmtInfo vmtInfo=vmtService.getVmtInfoByInstitutionId(home.getInstitutionId());
		if(vmtInfo==null){
			return null;
		}
		try {
			List<VmtUser> users=vmtUserService.searchUserByUmtId(vmtInfo.getDn(),umtId);
			if(CommonUtils.isNull(users)){
				backEndService.deleteMember(home.getInstitutionId(), umtId);
//				LOG.error("why not found user["+Arrays.toString(umtId)+"],in "+vmtInfo.getDn());
			}else{
				vmtUserService.removeUser(extractDN(users));
				backEndService.deleteMember(home.getInstitutionId(), umtId);
			}
			LOG.info("delete user["+Arrays.toString(umtId)+"] in ["+vmtInfo.getDn()+"]");
		} catch (ServiceException e) {
			LOG.error(e.getMessage(),e);
		}
		return new ModelAndView(new RedirectView(request.getContextPath()+"/institution/"+domain+"/backend/member/list/"+page));
	}
	
	private String[] extractDN(List<VmtUser> users){
		if(CommonUtils.isNull(users)){
			return new String[]{};
		}
		String[] dns=new String[users.size()];
		int index=0;
		for(VmtUser u:users){
			dns[index++]=u.getDn();
		}
		return dns;
	}
	@RequestMapping("update/{umtId}")
	@ResponseBody
	public boolean update(InstitutionMemberFromVmt vmtMember,@PathVariable("domain")String domain,@PathVariable("umtId")String umtId,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(CommonUtils.isNull(vmtMember.getUmtId())||!vmtMember.getUmtId().equals(umtId)){
			LOG.error("update.error:umtId not found["+umtId+"]");
			return false;
		}
		if(CommonUtils.isNull(vmtMember.getTrueName())){
			LOG.error("update.error:trueName can't be null");
			return false;
		}
		if(home==null){
			LOG.error("update.error:not found institutiton["+domain+"]");
			return false;
		}
		InstitutionVmtInfo vmtInfo=vmtService.getVmtInfoByInstitutionId(home.getInstitutionId());
		if(vmtInfo==null){
			LOG.error("update.error:can't found vmtInfo[insId="+home.getInstitutionId()+"]");
			return false;
		}
		if(!backEndService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("update.error:not admin!");
			return false;
		}
		if(CommonUtils.isNull(vmtMember.getUmtId())){
			LOG.error("update.error：umtId is Null!");
			return false;
		}
		try {
			VmtUser user=CommonUtils.first(vmtUserService.searchUserByUmtId(vmtInfo.getDn(), new String[]{umtId}));
			if(user==null){
				LOG.error("update.error:can't find vmt user!");
				return false;
			}
			user.setTitle(vmtMember.getTechnicalTitle());
			user.setName(vmtMember.getTrueName());
			user.setOffice(vmtMember.getOfficeAddress());
			user.setOfficePhone(vmtMember.getOfficeTelephone());
			user.setTelephone(vmtMember.getMobilePhone());
			user.setSex(vmtMember.getSex());
			user.setUmtId(vmtMember.getUmtId());
			vmtUserService.updateByUmtId(vmtInfo.getDn(), user);
			
			vmtMember.setUmtId(umtId);
			vmtMember.setInstitutionId(home.getInstitutionId());
			backEndService.updateBaseMember(vmtMember);
		} catch (ServiceException e) {
			LOG.error(e.getMessage(),e);
			return false;
		}
		return true;
	}
	@RequestMapping("detail/{umtId}.json")
	@ResponseBody
	public InstitutionMemberFromVmt detailJson(@PathVariable("domain")String domain,@PathVariable("umtId")String umtId,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		if(!backEndService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			return null;
		}
		
		return backEndService.getVmtMemberByUmtId(home.getInstitutionId(),umtId);
	}
	
	@RequestMapping("detail/{umtId}")
	public ModelAndView detail(@PathVariable("domain")String domain,@PathVariable("umtId")String umtId,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		
		ModelAndView mv=new ModelAndView("institution_backend/member/detail");
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		if(vmtMember.getDepartId()>0){
			mv.addObject("dept",backEndService.getVmtDepartmentById(vmtMember.getDepartId()));
		}
		mv.addObject("member",vmtMember);
		
		InstitutionAuthor author = new InstitutionAuthor();
		author.setAuthorName(vmtMember.getTrueName());
		author.setAuthorEmail(vmtMember.getCstnetId());
	//	author.setAuthorCompany(vmtMember.getDepartId());
		paperService.createAuthor(author );
		mv.addObject("author",author);
		
		//教育经历
//		List<Education> educations = educationService.getEducationsByUid(vmtMember.getUid());
//		mv.addObject("educations",educations);
		
		List<InstitutionPublication> pubs = paperService.getAllPubs();
		mv.addObject("pubsMap",extractPub(pubs));
		mv.addObject("disciplines",paperService.getAllDiscipline());
		
		mv.addObject("domain",domain);
		addBaseData(mv,home);
		return mv;
	}
	
	
	/**
	 * 查询教育经历
	 * @param domain
	 * @param awardId
	 * @param request
	 * @return
	 */
	@RequestMapping("educations/{umtId}")
	@ResponseBody
	public JsonResult getEducations(@PathVariable("domain") String domain,
			@PathVariable("umtId") String umtId,HttpServletRequest request){
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		List<Education> educations = educationService.getEducationsByUid(vmtMember.getUid());
		JsonResult result = new JsonResult();
		result.setData(educations);
		return result;
	}
	
	/**
	 * 修改或保存教育经历
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("modify/education/{umtId}")
	@ResponseBody
	public JsonResult modifyEducation(@PathVariable("domain") String domain,@PathVariable("umtId") String umtId,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home == null){
			return null;
		}
		
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		
		Education edu = getEducationFromRequest(request);
		edu.setInsitutionId(home.getInstitutionId());
		edu.setUid(vmtMember.getUid());
		if(edu.getId()>0){
			educationService.updateEducationById(edu);
		}else{
			edu.setId(educationService.createEducation(edu));
		}
		JsonResult result = new JsonResult();
		result.setData(edu);
		return result;
	}
	
	/**
	 * 修改或保存教育经历
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("delete/education/{id}")
	@ResponseBody
	public JsonResult deleteEducation(@PathVariable("domain") String domain,@PathVariable("id") int id,HttpServletRequest request){
		educationService.deleteEducation(id);
		JsonResult result = new JsonResult();
		return result;
	}
	
	
	private Education getEducationFromRequest(HttpServletRequest request){
		int id = 0;
		String idStr = net.duckling.dhome.common.util.CommonUtils.trim(request.getParameter("id"));
		if(null != idStr && !"".equals(idStr)){
			id = Integer.valueOf(idStr);
		}
		String degree = net.duckling.dhome.common.util.CommonUtils.trim(request.getParameter("degree"));
		String degree2 = net.duckling.dhome.common.util.CommonUtils.trim(request.getParameter("degree2"));
		String department = net.duckling.dhome.common.util.CommonUtils.trim(request.getParameter("department"));
		String insZhName = net.duckling.dhome.common.util.CommonUtils.trim(request.getParameter("institutionZhName").trim());
		String beginTime = net.duckling.dhome.common.util.CommonUtils.trim(request.getParameter("beginTime"));
		String endTimeStr = net.duckling.dhome.common.util.CommonUtils.trim(request.getParameter("endTime"));
		String tutor = net.duckling.dhome.common.util.CommonUtils.trim(request.getParameter("tutor"));
		String graduationProject = net.duckling.dhome.common.util.CommonUtils.trim(request.getParameter("graduationProject"));
		int graduationProjectCid = 0;
		String cidStr = net.duckling.dhome.common.util.CommonUtils.trim((request.getParameter("graduationProjectCid")));
		if(null != cidStr && !"".equals(cidStr)){
			graduationProjectCid = Integer.valueOf(cidStr);
		}
		java.util.Date endTime = (CommonUtils.isNull(endTimeStr))?DateUtils.getMaxDate():Date.valueOf(endTimeStr);
		
		Education edu = new Education();
		edu.setId(id);
		edu.setDegree(degree);
		edu.setDegree2(degree2);
		edu.setDepartment(department);
		edu.setBeginTime(Date.valueOf(beginTime));
		edu.setEndTime(endTime);
		edu.setAliasInstitutionName(insZhName);
		edu.setTutor(tutor);
		edu.setGraduationProject(graduationProject);
		edu.setGraduationProjectCid(graduationProjectCid);
		return edu;
	}
	
	/**
	 * 查询工作经历
	 * @param domain
	 * @param umtId
	 * @param request
	 * @return
	 */
	@RequestMapping("works/{umtId}")
	@ResponseBody
	public JsonResult getWorks(@PathVariable("domain") String domain,
			@PathVariable("umtId") String umtId,HttpServletRequest request){
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		List<Work> works = workService.getWorksByUID(vmtMember.getUid());
		JsonResult result = new JsonResult();
		result.setData(works);
		return result;
	}
	
	/**
	 * 修改或保存工作经历
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("modify/work/{umtId}")
	@ResponseBody
	public JsonResult modifyWork(@PathVariable("domain") String domain,@PathVariable("umtId") String umtId,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home == null){
			return null;
		}
		
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		
		Work work = getWorkFromRequest(request);
		work.setInstitutionId(home.getInstitutionId());
		work.setUid(vmtMember.getUid());
		if(work.getId()>0){
			workService.updateWorkById(work);
		}else{
			work.setId(workService.createWork(work));
		}
		JsonResult result = new JsonResult();
		result.setData(work);
		return result;
	}
	
	/**
	 * 修改或保存工作经历
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("delete/work/{id}")
	@ResponseBody
	public JsonResult deleteWork(@PathVariable("domain") String domain,@PathVariable("id") int id,HttpServletRequest request){
		workService.delteWork(id);
		JsonResult result = new JsonResult();
		return result;
	}
	
	
	private Work getWorkFromRequest(HttpServletRequest request){
		int id = 0;
		String idStr = net.duckling.dhome.common.util.CommonUtils.trim(request.getParameter("id"));
		if(null != idStr && !"".equals(idStr)){
			id = Integer.valueOf(idStr);
		}
		String position = net.duckling.dhome.common.util.CommonUtils.trim(request.getParameter("position"));
		String department = net.duckling.dhome.common.util.CommonUtils.trim(request.getParameter("department"));
		String insZhName = net.duckling.dhome.common.util.CommonUtils.trim(request.getParameter("institutionZhName").trim());
		String beginTime = net.duckling.dhome.common.util.CommonUtils.trim(request.getParameter("beginTime"));
		String endTimeStr = net.duckling.dhome.common.util.CommonUtils.trim(request.getParameter("endTime"));
		
		java.util.Date endTime = (CommonUtils.isNull(endTimeStr))?DateUtils.getMaxDate():Date.valueOf(endTimeStr);
		
		Work work = new Work();
		work.setId(id);
		work.setDepartment(department);
		work.setBeginTime(Date.valueOf(beginTime));
		work.setEndTime(endTime);
		work.setAliasInstitutionName(insZhName);
		work.setPosition(position);
		return work;
	}
	
	
	/**
	 * 查询论著
	 * @param domain
	 * @param umtId
	 * @param request
	 * @return
	 */
	@RequestMapping("treatises/{umtId}")
	@ResponseBody
	public JsonResult getTreatises(@PathVariable("domain") String domain,
			@PathVariable("umtId") String umtId,HttpServletRequest request){
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		List<InstitutionTreatise> treatises = treatiseService.getTreatisesByUID(vmtMember.getUid());
		JsonResult result = new JsonResult();
		result.setData(treatises);
		return result;
	}
	
	/**
	 * 修改或保存论著
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("modify/treatise/{umtId}")
	@ResponseBody
	public JsonResult modifyTreatise(
			InstitutionTreatise treatise,
			@PathVariable("domain") String domain,
			@PathVariable("umtId") String umtId,
			HttpServletRequest request){
		
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home == null){
			return null;
		}
		
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		JsonResult result = new JsonResult();
		
		InstitutionTreatiseAuthor author = new InstitutionTreatiseAuthor();
		author.setAuthorName(vmtMember.getTrueName());
		author.setAuthorEmail(vmtMember.getCstnetId());
	//	author.setAuthorCompany(vmtMember.getDepartId());
		treatiseService.createAuthor(author );
		
		if(treatise.getId() > 0){
			List<InstitutionTreatiseAuthor> authors = treatiseService.getAuthorByTreatiseId(treatise.getId());
			for(InstitutionTreatiseAuthor au : authors){
				if(au.getOrder() == treatise.getAuthorOrder() && au.getId() != author.getId()){
					result.setSuccess(false);
					result.setDesc("作者排序重复");
					return result;
				}
			}
			
			treatiseService.update(treatise, author.getId(), treatise.getAuthorOrder());
			treatiseService.updateUserRef(vmtMember.getUid(), treatise.getId(), treatise.getAuthorOrder());
		}else{
			int id = treatiseService.create(treatise, home.getInstitutionId(), author.getId(), treatise.getAuthorOrder());
			treatise.setId(id);
			treatiseService.createUserRef(vmtMember.getUid(), id, treatise.getAuthorOrder());
		}
		
		result.setData(treatise);
		return result;
	}
	
	/**
	 * 删除论著
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("delete/treatise/{umtId}/{treatiseId}")
	@ResponseBody
	public JsonResult deleteTreatise(@PathVariable("domain") String domain,
			@PathVariable("treatiseId") int treatiseId,
			@PathVariable("umtId") String umtId,
			HttpServletRequest request){
		
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		
		treatiseService.deleteUserRef(vmtMember.getUid(),treatiseId);
		JsonResult result = new JsonResult();
		return result;
	}

	/**
	 * 查询奖励
	 * @param domain
	 * @param umtId
	 * @param request
	 * @return
	 */
	@RequestMapping("awards/{umtId}")
	@ResponseBody
	public JsonResult getAwards(@PathVariable("domain") String domain,
			@PathVariable("umtId") String umtId,HttpServletRequest request){
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		List<InstitutionAward> awards = awardService.getAwardsByUID(vmtMember.getUid());
		JsonResult result = new JsonResult();
		result.setData(awards);
		return result;
	}
	
	
	/**
	 * 修改或保存论著
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("modify/award/{umtId}")
	@ResponseBody
	public JsonResult modifyAward(
			InstitutionAward award,
			@PathVariable("domain") String domain,
			@PathVariable("umtId") String umtId,
			@RequestParam("clbId[]")  int[] clbIds,
			@RequestParam("fileName[]") String[] fileNames,
			HttpServletRequest request){
		
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home == null){
			return null;
		}
		
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		JsonResult result = new JsonResult();
		
		InstitutionAwardAuthor author = new InstitutionAwardAuthor();
		author.setAuthorName(vmtMember.getTrueName());
		author.setAuthorEmail(vmtMember.getCstnetId());
	//	author.setAuthorCompany(vmtMember.getDepartId());
		awardService.createAuthor(author );
        		
		if(award.getId() > 0){
			List<InstitutionAwardAuthor> authors = awardService.getAuthorByawardId(award.getId());
			for(InstitutionAwardAuthor au : authors){
				if(au.getOrder() == award.getAuthorOrder() && au.getId() != author.getId()){
					result.setSuccess(false);
					result.setDesc("作者排序重复");
					return result;
				}
			}
			
			awardService.update(award, author.getId(), award.getAuthorOrder());
			awardService.updateUserRef(vmtMember.getUid(), award.getId(), award.getAuthorOrder());
			
			awardService.deleteClbRef(award.getId());
			awardService.createClbRef(award.getId(), clbIds, fileNames);
		}else{
			int id = awardService.create(award, home.getInstitutionId(), author.getId(), award.getAuthorOrder());
			award.setId(id);
			awardService.createUserRef(vmtMember.getUid(), id, award.getAuthorOrder());
			awardService.createClbRef(award.getId(), clbIds, fileNames);
		}
		
		result.setData(award);
		return result;
	}
	
	/**
	 * 删除论著
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("delete/award/{umtId}/{awardId}")
	@ResponseBody
	public JsonResult deleteAward(@PathVariable("domain") String domain,
			@PathVariable("awardId") int awardId,
			@PathVariable("umtId") String umtId,
			HttpServletRequest request){
		
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		
		awardService.deleteUserRef(vmtMember.getUid(),awardId);
		JsonResult result = new JsonResult();
		return result;
	}
	
	
	/**
	 * 查询学术任职
	 * @param domain
	 * @param umtId
	 * @param request
	 * @return
	 */
	@RequestMapping("academics/{umtId}")
	@ResponseBody
	public JsonResult getAcademics(@PathVariable("domain") String domain,
			@PathVariable("umtId") String umtId,HttpServletRequest request){
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		List<InstitutionAcademic> academics = academicService.getAcademicsByUID(vmtMember.getUid());
		JsonResult result = new JsonResult();
		result.setData(academics);
		return result;
	}
	
	
	/**
	 * 修改或保存学术任职
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("modify/academic/{umtId}")
	@ResponseBody
	public JsonResult modifyAcademic(
			InstitutionAcademic academic,
			@PathVariable("domain") String domain,
			@PathVariable("umtId") String umtId,
			HttpServletRequest request){
		
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home == null){
			return null;
		}
		
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		JsonResult result = new JsonResult();
		
		InstitutionAcademicAuthor author = new InstitutionAcademicAuthor();
		author.setTrueName(vmtMember.getTrueName());
		author.setCstnetId(vmtMember.getCstnetId());
	//	author.setAuthorCompany(vmtMember.getDepartId());
//		academicService.createAuthor(author );
        		
		if(academic.getId() > 0){
			academicService.update(academic, author.getId(), 1);
		}else{
			int id = academicService.create(academic, home.getInstitutionId(), author.getUmtId(), 1);
			academic.setId(id);
			academicService.createUserRef(vmtMember.getUid(), id);
		}
		
		result.setData(academic);
		return result;
	}
	
	/**
	 * 删除学术任职
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("delete/academic/{umtId}/{academicId}")
	@ResponseBody
	public JsonResult deleteAcademic(@PathVariable("domain") String domain,
			@PathVariable("academicId") int academicId,
			@PathVariable("umtId") String umtId,
			HttpServletRequest request){
		
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		
		academicService.deleteUserRef(vmtMember.getUid(),academicId);
		JsonResult result = new JsonResult();
		return result;
	}
	
	/**
	 * 查询期刊任职
	 * @param domain
	 * @param umtId
	 * @param request
	 * @return
	 */
	@RequestMapping("periodicals/{umtId}")
	@ResponseBody
	public JsonResult getPeriodicals(@PathVariable("domain") String domain,
			@PathVariable("umtId") String umtId,HttpServletRequest request){
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		List<InstitutionPeriodical> periodicals = periodicalService.getPeriodicalsByUID(vmtMember.getUid());
		JsonResult result = new JsonResult();
		result.setData(periodicals);
		return result;
	}
	
	
	/**
	 * 修改或保存期刊任职
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("modify/periodical/{umtId}")
	@ResponseBody
	public JsonResult modifyPeriodical(
			InstitutionPeriodical periodical,
			@PathVariable("domain") String domain,
			@PathVariable("umtId") String umtId,
			HttpServletRequest request){
		
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home == null){
			return null;
		}
		
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		JsonResult result = new JsonResult();
		
		InstitutionPeriodicalAuthor author = new InstitutionPeriodicalAuthor();
		author.setTrueName(vmtMember.getTrueName());
		author.setCstnetId(vmtMember.getCstnetId());
	//	author.setAuthorCompany(vmtMember.getDepartId());
//		periodicalService.createAuthor(author );
        		
		if(periodical.getId() > 0){
			periodicalService.update(periodical, author.getId(), 1);
		}else{
			int id = periodicalService.create(periodical, home.getInstitutionId(), author.getUmtId(), 1);
			periodical.setId(id);
			periodicalService.createUserRef(vmtMember.getUid(), id);
		}
		
		result.setData(periodical);
		return result;
	}
	
	/**
	 * 删除期刊任职
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("delete/periodical/{umtId}/{periodicalId}")
	@ResponseBody
	public JsonResult deletePeriodical(@PathVariable("domain") String domain,
			@PathVariable("periodicalId") int periodicalId,
			@PathVariable("umtId") String umtId,
			HttpServletRequest request){
		
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		
		periodicalService.deleteUserRef(vmtMember.getUid(),periodicalId);
		JsonResult result = new JsonResult();
		return result;
	}
	
	
	/**
	 * 查询软件著作权
	 * @param domain
	 * @param umtId
	 * @param request
	 * @return
	 */
	@RequestMapping("copyrights/{umtId}")
	@ResponseBody
	public JsonResult getCopyrights(@PathVariable("domain") String domain,
			@PathVariable("umtId") String umtId,HttpServletRequest request){
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		List<InstitutionCopyright> copyrights = copyrightService.getCopyrightsByUID(vmtMember.getUid());
		JsonResult result = new JsonResult();
		result.setData(copyrights);
		return result;
	}
	
	/**
	 * 修改或保存软件著作权
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("modify/copyright/{umtId}")
	@ResponseBody
	public JsonResult modifyCopyright(
			InstitutionCopyright copyright,
			@PathVariable("domain") String domain,
			@PathVariable("umtId") String umtId,
			HttpServletRequest request){
		
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home == null){
			return null;
		}
		
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		JsonResult result = new JsonResult();
		
		InstitutionAuthor author = new InstitutionAuthor();
		author.setAuthorName(vmtMember.getTrueName());
		author.setAuthorEmail(vmtMember.getCstnetId());
	//	author.setAuthorCompany(vmtMember.getDepartId());
		copyrightService.createAuthor(author );
        		
		if(copyright.getId() > 0){
			List<InstitutionAuthor> authors = copyrightService.getAuthorsByCopyrightId(copyright.getId());
			for(InstitutionAuthor au : authors){
				if(au.getOrder() == copyright.getPersonalOrder() && au.getId() != author.getId()){
					result.setSuccess(false);
					result.setDesc("作者排序重复");
					return result;
				}
			}
			copyrightService.update(copyright, author.getId(), copyright.getPersonalOrder());
			copyrightService.updateUserRef(vmtMember.getUid(), copyright.getId(), copyright.getPersonalOrder());
		}else{
			int id = copyrightService.create(copyright, home.getInstitutionId(), author.getId(), copyright.getPersonalOrder());
			copyright.setId(id);
			copyrightService.createUserRef(vmtMember.getUid(), id, copyright.getPersonalOrder());
		}
		
		result.setData(copyright);
		return result;
	}
	
	/**
	 * 删除软件著作权
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("delete/copyright/{umtId}/{copyrightId}")
	@ResponseBody
	public JsonResult deleteCopyright(@PathVariable("domain") String domain,
			@PathVariable("copyrightId") int copyrightId,
			@PathVariable("umtId") String umtId,
			HttpServletRequest request){
		
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		
		copyrightService.deleteUserRef(vmtMember.getUid(),copyrightId);
		JsonResult result = new JsonResult();
		return result;
	}
	
	/**
	 * 查询专利
	 * @param domain
	 * @param umtId
	 * @param request
	 * @return
	 */
	@RequestMapping("patents/{umtId}")
	@ResponseBody
	public JsonResult getPatents(@PathVariable("domain") String domain,
			@PathVariable("umtId") String umtId,HttpServletRequest request){
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		List<InstitutionPatent> Patents = patentService.getPatentsByUID(vmtMember.getUid());
		JsonResult result = new JsonResult();
		result.setData(Patents);
		return result;
	}
	
	
	/**
	 * 修改或保存专利
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("modify/patent/{umtId}")
	@ResponseBody
	public JsonResult modifyPatent(
			InstitutionPatent patent,
			@PathVariable("domain") String domain,
			@PathVariable("umtId") String umtId,
			HttpServletRequest request){
		
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home == null){
			return null;
		}
		
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		JsonResult result = new JsonResult();
		
		InstitutionAuthor author = new InstitutionAuthor();
		author.setAuthorName(vmtMember.getTrueName());
		author.setAuthorEmail(vmtMember.getCstnetId());
	//	author.setAuthorCompany(vmtMember.getDepartId());
		patentService.createAuthor(author );
		
		if(patent.getId() > 0){
			List<InstitutionAuthor> authors = patentService.getAuthorsByPatentId(patent.getId());
			for(InstitutionAuthor au : authors){
				if(au.getOrder() == patent.getPersonalOrder() && au.getId() != author.getId()){
					result.setSuccess(false);
					result.setDesc("作者排序重复");
					return result;
				}
			}
			
			patentService.update(patent, author.getId(), patent.getPersonalOrder());
			patentService.updateUserRef(vmtMember.getUid(), patent.getId(), patent.getPersonalOrder());
		}else{
			int id = patentService.create(patent, home.getInstitutionId(), author.getId(), patent.getPersonalOrder());
			patent.setId(id);
			patentService.createUserRef(vmtMember.getUid(), id, patent.getPersonalOrder());
		}
		
		result.setData(patent);
		return result;
	}
	
	/**
	 * 删除专利
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("delete/patent/{umtId}/{patentId}")
	@ResponseBody
	public JsonResult deletePatent(@PathVariable("domain") String domain,
			@PathVariable("patentId") int patentId,
			@PathVariable("umtId") String umtId,
			HttpServletRequest request){
		
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		
		patentService.deleteUserRef(vmtMember.getUid(),patentId);
		JsonResult result = new JsonResult();
		return result;
	}
	
	
	/**
	 * 查询课题
	 * @param domain
	 * @param umtId
	 * @param request
	 * @return
	 */
	@RequestMapping("topics/{umtId}")
	@ResponseBody
	public JsonResult getTopics(@PathVariable("domain") String domain,
			@PathVariable("umtId") String umtId,HttpServletRequest request){
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		List<InstitutionTopic> topics = topicService.getTopicsByUID(vmtMember.getUid());
		JsonResult result = new JsonResult();
		result.setData(topics);
		return result;
	}
	
	
	/**
	 * 修改或保存课题
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("modify/topic/{umtId}")
	@ResponseBody
	public JsonResult modifyTopic(
			InstitutionTopic topic,
			@PathVariable("domain") String domain,
			@PathVariable("umtId") String umtId,
			@RequestParam("uid[]")int[] uids,
			@RequestParam("authorType[]")String[] authorTypes,
			HttpServletRequest request){
		
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home == null){
			return null;
		}
		
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		JsonResult result = new JsonResult();
		
//		InstitutionAuthor author = new InstitutionAuthor();
//		author.setAuthorName(vmtMember.getTrueName());
//		author.setAuthorEmail(vmtMember.getCstnetId());
//	//	author.setAuthorCompany(vmtMember.getDepartId());
//		topicService.createAuthor(author );
		
		if(topic.getId() > 0){
			topicService.update(topic,uids,authorTypes);
			topicService.updateUserRef(vmtMember.getUid(), topic.getId(),topic.getRole());
		}else{
			int id = topicService.create(topic, home.getInstitutionId(),uids,authorTypes);
			topic.setId(id);
			topicService.createUserRef(vmtMember.getUid(), id,topic.getRole());
		}
		
		result.setData(topic);
		return result;
	}
	
	/**
	 * 删除课题
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("delete/topic/{umtId}/{topicId}")
	@ResponseBody
	public JsonResult deleteTopic(@PathVariable("domain") String domain,
			@PathVariable("topicId") int topicId,
			@PathVariable("umtId") String umtId,
			HttpServletRequest request){
		
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		
		topicService.deleteUserRef(vmtMember.getUid(),topicId);
		JsonResult result = new JsonResult();
		return result;
	}
	
	/**
	 * 查询个人培养
	 * @param domain
	 * @param umtId
	 * @param request
	 * @return
	 */
	@RequestMapping("trainings/{umtId}")
	@ResponseBody
	public JsonResult getTrainings(@PathVariable("domain") String domain,
			@PathVariable("umtId") String umtId,HttpServletRequest request){
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		List<InstitutionTraining> trainings = trainingService.getTrainingsByUID(vmtMember.getUid());
		JsonResult result = new JsonResult();
		result.setData(trainings);
		return result;
	}
	
	
	/**
	 * 修改或保存人才培养
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("modify/training/{umtId}")
	@ResponseBody
	public JsonResult modifyTraining(
			InstitutionTraining training,
			@PathVariable("domain") String domain,
			@PathVariable("umtId") String umtId,
			HttpServletRequest request){
		
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home == null){
			return null;
		}
		
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		JsonResult result = new JsonResult();
		
		String beginTimeStr = net.duckling.dhome.common.util.CommonUtils.trim(request.getParameter("beginTime"));
		String endTimeStr = net.duckling.dhome.common.util.CommonUtils.trim(request.getParameter("endTime"));
		java.util.Date endTime = (CommonUtils.isNull(endTimeStr))?DateUtils.getMaxDate():Date.valueOf(endTimeStr);
		java.util.Date beginTime = (CommonUtils.isNull(beginTimeStr))?DateUtils.getMaxDate():Date.valueOf(beginTimeStr);
		training.setGraduationDate(endTime);
		training.setEnrollmentDate(beginTime);
		
		if(training.getId() > 0){
			trainingService.update(training);
		}else{
			int id = trainingService.create(training, home.getInstitutionId());
			training.setId(id);
			trainingService.createUserRef(vmtMember.getUid(), id);
		}
		
		result.setData(training);
		return result;
	}
	
	/**
	 * 删除人才培养
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("delete/training/{umtId}/{trainingId}")
	@ResponseBody
	public JsonResult deleteTraining(@PathVariable("domain") String domain,
			@PathVariable("trainingId") int trainingId,
			@PathVariable("umtId") String umtId,
			HttpServletRequest request){
		
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		
		trainingService.deleteUserRef(vmtMember.getUid(),trainingId);
		JsonResult result = new JsonResult();
		return result;
	}
	
	/**
	 * 查询论文
	 * @param domain
	 * @param umtId
	 * @param request
	 * @return
	 */
	@RequestMapping("papers/{umtId}")
	@ResponseBody
	public JsonResult getPapers(@PathVariable("domain") String domain,
			@PathVariable("umtId") String umtId,HttpServletRequest request){
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		List<InstitutionPaper> papers = paperService.getPapersByUID(vmtMember.getUid());
	    List<Integer> pubId=net.duckling.dhome.common.util.CommonUtils.extractSthField(papers, "publicationId");
		
	    Map<Integer,InstitutionPublication> pubMap = new HashMap<Integer, InstitutionPublication>();
		if(!CommonUtils.isNull(pubId)){
			if(pubId.size()>1||pubId.get(0)!=0){
				pubMap = net.duckling.dhome.common.util.CommonUtils.extractSthFieldToMap(paperService.getPubsByIds(pubId), "id");
			}
		}
		
		Map<Integer,List<InstitutionAuthor>> authorMap = new HashMap<Integer, List<InstitutionAuthor>>();
		List<Integer> paperId=net.duckling.dhome.common.util.CommonUtils.extractSthField(papers,"id");
		if(!CommonUtils.isNull(paperId)){
			authorMap=paperService.getListAuthorsMap(paperId);
		}
		
		for(InstitutionPaper paper : papers){
			int pId = paper.getPublicationId();
			if(pubMap.containsKey(pId)){
				paper.setPublication(pubMap.get(pId));
			}
				
			int id = paper.getId();
			if(authorMap.containsKey(id)){
				paper.setAuthors(authorMap.get(id));
			}
		}
		
		JsonResult result = new JsonResult();
		result.setData(papers);
		return result;
	}
	
	@RequestMapping("modify/paper/{umtId}")
	@ResponseBody
	public JsonResult modifPaper(
			InstitutionPaper paper,
			@PathVariable("domain")String domain,
			@RequestParam("pid")int pid,
			@PathVariable("umtId")String umtId,
			@RequestParam("uid[]")int[] uid,
			@RequestParam("order[]")int[] order,
			@RequestParam("communicationAuthor[]")boolean[] communicationAuthors,
			@RequestParam("authorStudent[]")boolean[] authorStudents,
			@RequestParam("authorTeacher[]")boolean[] authorTeacher,
			@RequestParam("pubName")String pubName,
			HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("submit:home is null["+domain+"]");
			return new JsonResult("找不到组织机构");
		}
		if(!backEndService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("submit:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		paper.setPublicationId(pid);
		//新建
		if(paper.getId()==0){
			//TODO 这里还需要一个逻辑，即为标记为*号的字段，如果与数据库中的记录匹配，不新建论文，而是简历ref，并更新数据库中数据
			int id = paperService.create(paper,home.getInstitutionId(),uid,order,communicationAuthors,authorStudents,authorTeacher);
			paper.setId(id);
			paperService.createUserRef(vmtMember.getUid(), id);
		}//更新
		else{
			paperService.update(paper,home.getInstitutionId(),uid,order,communicationAuthors,authorStudents,authorTeacher);
		}
		
		InstitutionPublication publication = new InstitutionPublication();
		publication.setId(paper.getPublicationId());
		publication.setPubName(pubName);
		paper.setPublication(publication );
		
		List<InstitutionAuthor> authors = paperService.getAuthorsByPaperId(paper.getId());
		caculateSubscript(authors);
		paper.setAuthors(authors);
		
		JsonResult rs = new JsonResult();
		rs.setData(paper);
		return rs;
	}
	
	private void caculateSubscript(List<InstitutionAuthor> authors){
		Map<String,Integer> subscriptMap=new HashMap<String,Integer>();
		for(InstitutionAuthor author:authors){
			if(subscriptMap.containsKey(author.getAuthorCompany())){
				author.setSubscriptIndex(subscriptMap.get(author.getAuthorCompany()));
			}else{
				int index=subscriptMap.size()+1;
				author.setSubscriptIndex(index);
				subscriptMap.put(author.getAuthorCompany(),index);
			}
		}
	}
	
	/**
	 * 删除人才培养
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("delete/paper/{umtId}/{paperId}")
	@ResponseBody
	public JsonResult deletePaper(@PathVariable("domain") String domain,
			@PathVariable("paperId") int paperId,
			@PathVariable("umtId") String umtId,
			HttpServletRequest request){
		
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		
		paperService.deleteUserRef(vmtMember.getUid(),paperId);
		JsonResult result = new JsonResult();
		return result;
	}
	
	/**
	 * 获取论文全部刊物
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("paper/pubs")
	@ResponseBody
	public JsonResult getPaperPubs(@PathVariable("domain") String domain,HttpServletRequest request){
		List<InstitutionPublication> pubs = paperService.getAllPubs();
		JsonResult rs = new JsonResult();
		rs.setData(pubs);
		return rs;
	}
	/**
	 * 根据获取论文刊物
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("paper/pub/{pid}")
	@ResponseBody
	public JsonResult getPaperPubsByPid(@PathVariable("domain") String domain,@PathVariable("pid")int pid,HttpServletRequest request){
		InstitutionPublication pub = paperService.getPubsById(pid);
		JsonResult rs = new JsonResult();
		rs.setData(pub);
		return rs;
	}
}
