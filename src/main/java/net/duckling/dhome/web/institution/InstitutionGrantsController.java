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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.institution.InstitutionGrants;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionMemberFromVmt;
import net.duckling.dhome.domain.institution.InstitutionOption;
import net.duckling.dhome.domain.institution.InstitutionOptionVal;
import net.duckling.dhome.domain.institution.InstitutionTraining;
import net.duckling.dhome.domain.object.JsonResult;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendTrainingService;
import net.duckling.dhome.service.IInstitutionGrantsService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionOptionValService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.web.helper.PdfTemplateHelper;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;


/**
 * 奖助学金
 * @author Brett
 *
 */
@Controller
@NPermission(authenticated = true,member = "iap")
@RequestMapping("people/{domain}/admin/grants")
public class InstitutionGrantsController {
	private static final Logger LOG=Logger.getLogger(InstitutionGrantsController.class);
	
	@Autowired
	private IUserService userService;
	@Autowired
	private IHomeService userHomeService;
	@Autowired
	private IInstitutionGrantsService grantsService;
	@Autowired
	private IInstitutionBackendTrainingService trainingService;
	@Autowired
	private IInstitutionHomeService homeService;
	@Autowired
	private IInstitutionOptionValService optionValService;
	@Autowired
	private IInstitutionBackendTrainingService backEndTrainingService;
	
	private void addBaseData(ModelAndView mv,SimpleUser user){
		//学位
		Map<Integer,InstitutionOptionVal> degrees= optionValService.getMapByOptionId(InstitutionOption.TRAINING_DEGREE, user.getInstitutionId());
		mv.addObject("degrees",degrees);
		//导师
		Map<String, InstitutionMemberFromVmt> memberMap=backEndTrainingService.getAllMember(user.getInstitutionId());
		mv.addObject("memberMap",memberMap);
		InstitutionMemberFromVmt member = backEndTrainingService.getMemberByUser(user.getInstitutionId(), user.getEmail());
		mv.addObject("member",member);
	}
	
	@RequestMapping("list/{page}")
	public ModelAndView list(@PathVariable("domain")String domain,
			@PathVariable("page")int page,
			HttpServletRequest request){
		Integer status = 0;
		ModelAndView mv = null;
		PageResult<InstitutionGrants> result = null;
		SimpleUser user = SessionUtils.getUser(request);
		if("handed".equals(request.getParameter("status"))){
			 status = 1;
			 result= grantsService.getListLasted(user.getId(), null, status, page);
			 mv=new ModelAndView("institution/training/grantsHandedList");
		}else{
			 result= grantsService.getList(user.getId(), null, status, null, page);
			 mv=new ModelAndView("institution/training/grantsList");
		}
		
		Map<Integer,InstitutionOptionVal> degreeMap= optionValService.getMapByOptionId(InstitutionOption.TRAINING_DEGREE, user.getInstitutionId());
		mv.addObject("degreeMap",degreeMap);
		mv.addObject("handedCount",grantsService.getBatchCount(user.getId(), null));
		mv.addObject("onHandCount",grantsService.getListCount(user.getId(), null, 0));
		mv.addObject("page",result);
		mv.addObject("titleUser",user);
		return mv;
	}
	
	@RequestMapping("batch/{page}")
	public ModelAndView batch(@PathVariable("domain")String domain,
			@PathVariable("page")int page,
			HttpServletRequest request){
		SimpleUser user = SessionUtils.getUser(request);
		PageResult<String> result = grantsService.getBatchList(user.getId(), null, page);
		ModelAndView mv=new ModelAndView("institution/training/grantsBatchList");
		Map<String,List<InstitutionGrants>> studentMap=grantsService.getStudentByUser(user.getId());
		mv.addObject("studentMap",studentMap);
		mv.addObject("page",result);
		mv.addObject("titleUser",user);
		
		mv.addObject("handedCount",grantsService.getBatchCount(user.getId(), null));
		mv.addObject("onHandCount",grantsService.getListCount(user.getId(), null, 0));
		return mv;
	}
	
	@RequestMapping("handed/{batchNo}/{pape}")
	public ModelAndView grantsForBatch(@PathVariable("domain")String domain,
			@PathVariable("batchNo")String batchNo,
			@PathVariable("pape")int pape,
			HttpServletRequest request){
		SimpleUser user = SessionUtils.getUser(request);
		PageResult<InstitutionGrants> list = grantsService.getListByBatchNo(user.getId(), batchNo,pape);
		ModelAndView mv=new ModelAndView("institution/training/grantsHandedList");
		mv.addObject("page",list);
		mv.addObject("batchNo",batchNo);
		mv.addObject("titleUser",user);
		mv.addObject("handedCount",grantsService.getBatchCount(user.getId(), null));
		mv.addObject("onHandCount",grantsService.getListCount(user.getId(), null, 0));
		
		Map<Integer,InstitutionOptionVal> degreeMap= optionValService.getMapByOptionId(InstitutionOption.TRAINING_DEGREE, user.getInstitutionId());
		mv.addObject("degreeMap",degreeMap);
		
		return mv;
	}
	
	@RequestMapping("listHistory/{page}")
	public ModelAndView listHistory(@PathVariable("domain")String domain,
			@RequestParam Integer studentId,
			@PathVariable("page")int page,
			HttpServletRequest request){
		SimpleUser user = SessionUtils.getUser(request);
		InstitutionTraining student = trainingService.getById(studentId);
		PageResult<InstitutionGrants> result = grantsService.getList(user.getId(), null, 1, studentId, page);
		
		ModelAndView mv=new ModelAndView("institution/training/grantsHistory");
		
		mv.addObject("page",result);
		mv.addObject("student",student);
		mv.addObject("titleUser",user);
		mv.addObject("onHandCount",grantsService.getListCount(user.getId(), null, 0));
		addBaseData(mv,user);
		return mv;
	}
	
	@RequestMapping("create")
	public ModelAndView create(HttpServletRequest request){
		SimpleUser user = SessionUtils.getUser(request);
		
		ModelAndView mv=new ModelAndView("institution/training/grantsEdit");
		List<InstitutionTraining> trainings = trainingService.getStudentByUser(user.getId());
		mv.addObject("trainings",trainings);
		mv.addObject("op","create");
		mv.addObject("grants", new InstitutionGrants());
		mv.addObject("titleUser",user);
		return mv;
	}
	
	@RequestMapping("delete/{id}")
	@ResponseBody
	public JsonResult delete(
			InstitutionGrants grants,
			@PathVariable("domain")String domain,
			@PathVariable("id")int id,
			HttpServletRequest request){
		
		SimpleUser user = SessionUtils.getUser(request);
		grants.setUserId(user.getId());
		
		grantsService.delete(user.getId(), new int[]{id});
		return new JsonResult();
	}
	
	@RequestMapping("save")
	@ResponseBody
	public JsonResult save(
			InstitutionGrants grants,
			@PathVariable("domain")String domain,
			HttpServletRequest request){
		SimpleUser user = SessionUtils.getUser(request);
		grants.setUserId(user.getId());
		grants.setTutor(user.getZhName());
		//新建
		if(grants.getId()==0){
			grantsService.create(grants);
		}//更新
		else{
			grantsService.update(grants);
		}
		return new JsonResult();
	}
	
	@RequestMapping("updateStatus")
	@ResponseBody
	public JsonResult updateStatus(
			@RequestParam("grantsId[]")int[] ids,
			HttpServletRequest request, HttpServletResponse resp) throws IOException{
		SimpleUser user = SessionUtils.getUser(request);

		grantsService.updateStatus(user.getId(), ids);
	
		return new JsonResult();
	}
	
	@RequestMapping("report")
	public void report(
			@RequestParam("batchNo")String batchNo,
			HttpServletRequest request, HttpServletResponse resp) throws IOException{
		SimpleUser user = SessionUtils.getUser(request);
		resp.setContentType("application/x-msdownload");
	    resp.setHeader("Content-disposition","attachment; filename=" + batchNo + ".pdf");
	    
		List<InstitutionGrants> grantsList = grantsService.getListByBatchNo(user.getId(), batchNo);
		ServletOutputStream sos = resp.getOutputStream();
		ByteArrayOutputStream baos = PdfTemplateHelper.getGrants(grantsList);
	    baos.writeTo(sos);
	    
	    sos.flush();  
	    sos.close();
	}
	
	@RequestMapping("search/student")
	@ResponseBody
	public List<InstitutionTraining> searchStudent(@RequestParam("q")String keyword,HttpServletRequest request){
		List<InstitutionTraining> training=trainingService.search(CommonUtils.trim(keyword));
		return training;
	}
	
	@InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) { 
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
        binder.registerCustomEditor(Date.class, new CustomDateEditor(df,false)); 
    }
}
