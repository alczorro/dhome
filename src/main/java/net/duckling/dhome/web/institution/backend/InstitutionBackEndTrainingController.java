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


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
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
import net.duckling.dhome.domain.institution.InstitutionMemberFromVmt;
import net.duckling.dhome.domain.institution.InstitutionOption;
import net.duckling.dhome.domain.institution.InstitutionOptionVal;
import net.duckling.dhome.domain.institution.InstitutionTraining;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.JsonResult;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionBackendTrainingService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionOptionValService;

import org.apache.log4j.Logger;
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
@RequestMapping("/institution/{domain}/backend/training")

public class InstitutionBackEndTrainingController {
	private static final Logger LOG=Logger.getLogger(InstitutionBackEndTrainingController.class);
	
	@Autowired
	private IInstitutionBackendTrainingService backEndTrainingService;
	@Autowired
	private IInstitutionHomeService homeService;
	@Autowired
	private IInstitutionBackendService backendService;
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
	
	@RequestMapping("list/{page}")
	public ModelAndView list(@PathVariable("domain")String domain,
			@PathVariable("page")int page,
			SearchInstitutionCondition condition,
			HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution_backend/training/list");
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("list:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return new ModelAndView("institution_backend/permission");
		}
		
		if(home==null||page<0){
			return null;
		}
		PageResult<InstitutionTraining> result=backEndTrainingService.getTraining(home.getInstitutionId(), page, condition);
		Map<Integer,Integer> pubDegreeMap=backEndTrainingService.getDegreesMap(home.getInstitutionId());
		mv.addObject("degreeMap",pubDegreeMap);
		mv.addObject("condition",condition);
		mv.addObject("page",result);
		mv.addObject("domain",domain);
		addBaseData(mv,home);
		
//		List<Integer> trainingId=CommonUtils.extractSthField(result.getDatas(),"id");
//		Map<Integer,List<InstitutionAuthor>> authorMap=backEndTrainingService.getListAuthorsMap(trainingId);
//		mv.addObject("authorMap",authorMap);
		return mv;
	}
	private void addBaseData(ModelAndView mv,InstitutionHome home){
		//学位
		Map<Integer,InstitutionOptionVal> degrees= optionValService.getMapByOptionId(InstitutionOption.TRAINING_DEGREE, home.getInstitutionId());
		mv.addObject("degrees",degrees);
		//导师
		Map<String, InstitutionMemberFromVmt> memberMap=backEndTrainingService.getAllMember(home.getInstitutionId());
		mv.addObject("memberMap",memberMap);
	}
	
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
		InstitutionTraining training = backEndTrainingService.getTrainingById(id);
		if(training==null){
			LOG.error("update:paper["+id+"] is not found!");
			return null;
		}
		ModelAndView mv=new ModelAndView("institution_backend/training/modify");
		mv.addObject("op","update");
		mv.addObject("training",training);
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
		backEndTrainingService.deleteTraining(id);
//		backEndTrainingService.deleteByTrainingId(id);
		
//		InstitutionTraining training = listJson(domain, id, request);
//			LOG.info("delete user["+Arrays.toString(umtId)+"] in ["+vmtInfo.getDn()+"]");
//		} catch (ServiceException e) {
//			LOG.error(e.getMessage(),e);
//		}
		return new ModelAndView(new RedirectView(request.getContextPath()+"/institution/"+domain+"/backend/training/list/"+page));
	}
	@RequestMapping("update/{id}")
	@ResponseBody
	public ModelAndView update(InstitutionTraining training,@PathVariable("domain")String domain,@PathVariable("id")int id,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("create:home is null["+domain+"]");
		}
		
//		if(CommonUtils.isNull(training.getId())||!training.getId().equals(id)){
//			LOG.error("update.error:id not found["+id+"]");
//			return false;
//		}
//		if(CommonUtils.isNull(vmtMember.getTrueName())){
//			LOG.error("update.error:trueName can't be null");
//			return false;
//		}
		
		backEndTrainingService.updateTraining(id, training);
		return new ModelAndView(new RedirectView(request.getContextPath()+"/institution/"+domain+"/backend/training/modify"));
	}
	@RequestMapping("create")
	public ModelAndView create(@PathVariable("domain")String domain,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("create:home is null["+domain+"]");
			return null;
		}
		
		ModelAndView mv=new ModelAndView("institution_backend/training/modify");
		mv.addObject("trainingCount",backEndTrainingService.getTrainingCount(home.getInstitutionId()));
		mv.addObject("op","create");
		mv.addObject("domain",domain);
		addBaseData(mv,home);
		addDeptMap(mv,home);
		return mv;
	}
	@RequestMapping("submit")
	public JsonResult submit(InstitutionTraining training,@PathVariable("domain")String domain,
			@RequestParam("enrollmentYear")int enrollmentYear,
			@RequestParam("enrollmentMonth")int enrollmentMonth,
			@RequestParam("graduationYear")int graduationYear,
			@RequestParam("graduationMonth")int graduationMonth,
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
		String enrollment="";
		String graduation="";
		if(enrollmentMonth<10){
			enrollment = Integer.toString(enrollmentYear)+"-0"+Integer.toString(enrollmentMonth);
		}else{
			enrollment = Integer.toString(enrollmentYear)+"-"+Integer.toString(enrollmentMonth);
		}
		if(graduationMonth<10){
			graduation = Integer.toString(graduationYear)+"-0"+Integer.toString(graduationMonth);
		}else{
			graduation = Integer.toString(graduationYear)+"-"+Integer.toString(graduationMonth);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM"); 
		
	    try {
			Date enrollmentDate = (Date) sdf.parse(enrollment);
			Date graduationDate = (Date) sdf.parse(graduation);
			training.setEnrollmentDate(enrollmentDate);
			training.setGraduationDate(graduationDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		training.setInstitutionId(home.getInstitutionId());
		
		if(training.getId()==0){
			backEndTrainingService.insertTraining(training);
//			backEndTrainingService.createRef(training.getId(), uid, authorType);
		}else{
			backEndTrainingService.updateTraining(training.getId(), training);
//			backEndTrainingService.deleteRef(training.getId());
//			backEndTrainingService.createRef(training.getId(), uid, authorType);
		}
		return new JsonResult();
	}
	@RequestMapping("detail/{trainingId}")
	@ResponseBody
	public ModelAndView detail(@RequestParam("returnPage") int returnPage,@PathVariable("domain")String domain,@PathVariable("trainingId")int copyrightId,HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution_backend/training/detail");
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			return null;
		} 
		InstitutionTraining training = backEndTrainingService.getTrainingById(copyrightId);
		mv.addObject("training",training);
		mv.addObject("returnPage",returnPage);
		mv.addObject("domain",domain);
		addBaseData(mv,home);
		addDeptMap(mv,home);
		return mv;
	}
}
