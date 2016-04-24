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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.institution.InstitutionAcademic;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionAward;
import net.duckling.dhome.domain.institution.InstitutionCopyright;
import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.dhome.domain.institution.InstitutionGrants;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionJobApplication;
import net.duckling.dhome.domain.institution.InstitutionJobApply;
import net.duckling.dhome.domain.institution.InstitutionMemberFromVmt;
import net.duckling.dhome.domain.institution.InstitutionOption;
import net.duckling.dhome.domain.institution.InstitutionOptionVal;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.InstitutionPatent;
import net.duckling.dhome.domain.institution.InstitutionPublication;
import net.duckling.dhome.domain.institution.InstitutionTopic;
import net.duckling.dhome.domain.institution.InstitutionTraining;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.JsonResult;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.domain.people.DetailedUser;
import net.duckling.dhome.domain.people.Education;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.Work;
import net.duckling.dhome.service.IEducationService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendPaperService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionBackendTrainingService;
import net.duckling.dhome.service.IInstitutionGrantsService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionJobApplyService;
import net.duckling.dhome.service.IInstitutionOptionValService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.IWorkService;
import net.duckling.dhome.web.helper.MDoc;

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
import org.springframework.web.servlet.view.RedirectView;


/**
 * 后台岗位评定
 * @author Brett
 *
 */
@Controller
@NPermission(authenticated = true,member = "iap")
@RequestMapping("people/{domain}/admin/job")
public class JobAppayController {
	private static final Logger LOG=Logger.getLogger(JobAppayController.class);
	
	@Autowired
	private IUserService userService;
	@Autowired
	private IHomeService userHomeService;
	@Autowired
	private IInstitutionBackendService backendService;
	@Autowired
	private IInstitutionGrantsService grantsService;
	@Autowired
	private IInstitutionJobApplyService applyService;
	@Autowired
	private IInstitutionHomeService homeService;
	@Autowired
	private IInstitutionOptionValService optionValService;
	@Autowired
	private IInstitutionBackendTrainingService backEndTrainingService;
	@Autowired
	private IWorkService workService;
	@Autowired
	private IEducationService eduService;
	@Autowired
	private IInstitutionBackendPaperService paperService;
	
	private void addBaseData(ModelAndView mv,SimpleUser user){
//		//部门
//		List<InstitutionDepartment> depts=paperService.getDepartments(home.getInstitutionId());
//	    mv.addObject("deptMap",extractDept(depts));
		
		//学科方向
		Map<Integer,InstitutionOptionVal> disciplineOrientations= optionValService.getMapByOptionId(InstitutionOption.PAPER_DISCIPLINE_ORIENTATION, user.getInstitutionId());
		mv.addObject("disciplineOrientations",disciplineOrientations);
		//论著出版社
		Map<Integer,InstitutionOptionVal> publishers= optionValService.getMapByOptionId(InstitutionOption.TREATISE_PUBLISHER, user.getInstitutionId());
		mv.addObject("publishers",publishers);
		//奖励名称
		Map<Integer,InstitutionOptionVal> awardNames= optionValService.getMapByOptionId(InstitutionOption.AWARD_NAME, user.getInstitutionId());
		mv.addObject("awardNames",awardNames);
		
		//类别
		Map<Integer,InstitutionOptionVal> awardTypes= optionValService.getMapByOptionId(InstitutionOption.AWARD_TYPE, user.getInstitutionId());
		mv.addObject("awardTypes",awardTypes);

		//等级
		Map<Integer,InstitutionOptionVal> awardGrades= optionValService.getMapByOptionId(InstitutionOption.AWARD_GRADE, user.getInstitutionId());
		mv.addObject("awardGrades",awardGrades);
		//学术任职
		Map<Integer,InstitutionOptionVal> organizationNames= optionValService.getMapByOptionId(InstitutionOption.ACADEMIC_ASSIGNMENT_ORGANIZATION_NAME, user.getInstitutionId());
		mv.addObject("organizationNames",organizationNames);
		Map<Integer,InstitutionOptionVal> positions= optionValService.getMapByOptionId(InstitutionOption.ACADEMIC_ASSIGNMENT_POSITION, user.getInstitutionId());
		mv.addObject("positions",positions);
		//课题
		Map<Integer,InstitutionOptionVal> types= optionValService.getMapByOptionId(InstitutionOption.TOPIC_TYPE, user.getInstitutionId());
		mv.addObject("types",types);
		//学生
		Map<Integer,InstitutionOptionVal> degrees= optionValService.getMapByOptionId(InstitutionOption.TRAINING_DEGREE, user.getInstitutionId());
		mv.addObject("degrees",degrees);
	}
	
	private Map<Integer,InstitutionJobApplication> extractApplication(List<InstitutionJobApplication> applications){
		if(CommonUtils.isNull(applications)){
			return Collections.emptyMap();
		}
		Map<Integer,InstitutionJobApplication> map=new HashMap<Integer,InstitutionJobApplication>();
		for(InstitutionJobApplication app:applications){
			map.put(app.getId(),app);
		}
		return map;
	}
	private Map<Integer,InstitutionAuthor> extractAuthor(List<InstitutionAuthor> authors){
		if(CommonUtils.isNull(authors)){
			return Collections.emptyMap();
		}
		Map<Integer,InstitutionAuthor> map=new HashMap<Integer,InstitutionAuthor>();
		for(InstitutionAuthor author:authors){
			map.put(author.getId(),author);
		}
		return map;
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
	private Map<Integer,SimpleUser> extractUser(List<SimpleUser> users){
		if(CommonUtils.isNull(users)){
			return Collections.emptyMap();
		}
		Map<Integer,SimpleUser> map=new HashMap<Integer,SimpleUser>();
		for(SimpleUser user:users){
			map.put(user.getId(),user);
		}
		return map;
	}
	@RequestMapping("list/{page}")
	public ModelAndView list(@PathVariable("domain")String domain,
			@PathVariable("page")int page,
			SearchInstitutionCondition condition,
			HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution/jobapply/applicationList");
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null||page<0){
//			return null;
//		}
		SimpleUser user = SessionUtils.getUser(request);
		PageResult<InstitutionJobApplication> result= applyService.getList(user.getInstitutionId(), page);
		List<InstitutionJobApply> jobApplys=applyService.getIdByUser(user.getId(), user.getInstitutionId());
		Map<Integer,InstitutionJobApply> jobMap=new HashMap<Integer,InstitutionJobApply>();
		for (InstitutionJobApply job : jobApplys) {
			jobMap.put(job.getApplicationId(), job);
		}
		mv.addObject("jobMap", jobMap);
		mv.addObject("page", result);
		mv.addObject("domain", domain);
		mv.addObject("user", user);
		return mv;
	}
	
	@RequestMapping("myList/{page}")
	public ModelAndView myList(@PathVariable("domain")String domain,
			@PathVariable("page")int page,
			SearchInstitutionCondition condition,
			HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution/jobapply/applyList");
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null||page<0){
//			return null;
//		}
		SimpleUser user = SessionUtils.getUser(request);
		PageResult<InstitutionJobApply> result= applyService.getListByUserId(user.getInstitutionId(), user.getId(), page);
		List<InstitutionJobApplication> applications=applyService.getAllList(user.getInstitutionId());
		mv.addObject("applicationMap", extractApplication(applications));
		mv.addObject("user", user);
		mv.addObject("page", result);
		mv.addObject("domain", domain);
		
		return mv;
	}
	
//	@RequestMapping("creat")
//	public ModelAndView creat(@PathVariable("domain")String domain,
//			HttpServletRequest request){
//		ModelAndView mv=new ModelAndView("institution_backend/member/addJobApplication");
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			return null;
//		}
//		
////		mv.addObject("page", result);
//		mv.addObject("domain", domain);
//		
//		return mv;
//	}
	
	@RequestMapping("delete/{id}")
	public ModelAndView delete(
			@PathVariable("domain")String domain,
			@PathVariable("id")int id,
			HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			return null;
//		}
		applyService.deleteById(id);
		return new ModelAndView(new RedirectView(request.getContextPath()+"/people/"+domain+"/admin/job/myList/"+1));
	}
	
	@RequestMapping("details/{id}")
	public ModelAndView details(@PathVariable("domain")String domain,
			@PathVariable("id")int applicationId,
			HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution/jobapply/jobApply");
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			return null;
//		}
		SimpleUser user = SessionUtils.getUser(request);
		Map<Integer,InstitutionPublication>pubsMap=extractPub(paperService.getAllPubs());
//		PageResult<InstitutionJobApply> result= applyService.getListByApplicationId(home.getInstitutionId(), applicationId, page);
		List<InstitutionJobApplication> applications=applyService.getAllList(user.getInstitutionId());
		mv.addObject("applicationMap", extractApplication(applications));
		DetailedUser detailUser = userService.getDetailedUser(user.getId());
		mv.addObject("detailUser", detailUser);
		InstitutionMemberFromVmt member = applyService.getMemberByUid(user.getInstitutionId(), user.getId());
		mv.addObject("member", member);
		List<Work> works = workService.getWorksByUID(user.getId());
		mv.addObject("works", works);
		List<Education> edus = eduService.getEducationsByUid(user.getId());
		mv.addObject("educations", edus);
		//获取论文
		Date startTime=extractApplication(applications).get(applicationId).getStartTime();
		Date endTime=extractApplication(applications).get(applicationId).getEndTime();
		Calendar cl = Calendar.getInstance(); 
		cl.setTime(startTime);
		int startYear=cl.get(Calendar.YEAR);
		cl.setTime(endTime);
		int endYear=cl.get(Calendar.YEAR);
		List<InstitutionAuthor> authors=applyService.getAuthorsByUid(user.getId());
		mv.addObject("authors", extractAuthor(authors));
		List<InstitutionPaper> firstPapers=applyService.getPapersByAuthorId(authors, startYear, endYear);
		mv.addObject("firstPapers", firstPapers);
		//sci论文数量
		int firstSciCount=0;
		float firstIf=0;
		for (InstitutionPaper p : firstPapers) {
			if(p.getPublicationId()!=0){
				if(pubsMap.get(p.getPublicationId())!=null&&pubsMap.get(p.getPublicationId())!=null){
					if(pubsMap.get(p.getPublicationId()).getIfs()!=null){
						firstIf+=Float.parseFloat(pubsMap.get(p.getPublicationId()).getIfs());
					}
				if(pubsMap.get(p.getPublicationId()).getPublicationType()!=null&&pubsMap.get(p.getPublicationId()).getPublicationType().equals("SCI")){
					firstSciCount++;
				}
				}
			}
		}
		mv.addObject("firstSciCount", firstSciCount);
		mv.addObject("firstIf", firstIf);
		List<InstitutionPaper> otherPapers=applyService.getOtherPapersByAuthorId(authors, startYear, endYear);
		mv.addObject("otherPapers", otherPapers);
		//sci论文数量
		int otherSciCount=0;
		float otherIf=0;
		for (InstitutionPaper p : otherPapers) {
			if(p.getPublicationId()!=0){
				if(pubsMap.get(p.getPublicationId())!=null&&pubsMap.get(p.getPublicationId())!=null){
					if(pubsMap.get(p.getPublicationId()).getIfs()!=null){
						otherIf+=Float.parseFloat(pubsMap.get(p.getPublicationId()).getIfs());
					}
				if(pubsMap.get(p.getPublicationId()).getPublicationType()!=null&&pubsMap.get(p.getPublicationId()).getPublicationType().equals("SCI")){
					otherSciCount++;
				}
				}
			}
		}
		mv.addObject("otherSciCount", otherSciCount);
		mv.addObject("ifs", otherIf+firstIf);
		//作者
		List<Integer> paperId=CommonUtils.extractSthField(firstPapers,"id");
		if(!CommonUtils.isNull(paperId)){
			Map<Integer,List<InstitutionAuthor>> authorMap=paperService.getListAuthorsMap(paperId);
			mv.addObject("authorMap",authorMap);
		}
		List<Integer> otherPaperId=CommonUtils.extractSthField(otherPapers,"id");
		if(!CommonUtils.isNull(otherPaperId)){
			Map<Integer,List<InstitutionAuthor>> otherAuthorMap=paperService.getListAuthorsMap(otherPaperId);
			mv.addObject("otherAuthorMap",otherAuthorMap);
		}
		
		int papersCount = paperService.getPapersCountByUser(user.getId());
		mv.addObject("papersCount",papersCount);
		List<InstitutionPaper> allPapers=paperService.getPapersByUID(user.getId());
		float allIf=0;
		for (InstitutionPaper p : allPapers) {
			if(pubsMap.get(p.getPublicationId())!=null&&pubsMap.get(p.getPublicationId()).getIfs()!=null){
				allIf+=Float.parseFloat(pubsMap.get(p.getPublicationId()).getIfs());
			}
		}
		mv.addObject("allIf",allIf);
		int allSciCount=applyService.getAllSciCount(user.getId());
		mv.addObject("allSciCount",allSciCount);
		List<InstitutionPaper> allFirstPapers=applyService.getAllFirst(authors);
		mv.addObject("allFirstCount",allFirstPapers.size());
		int allFirstSciCount=0;
		float allFirstIf=0;
		for (InstitutionPaper p : allFirstPapers) {
			if(p.getPublicationId()!=0){
				if(pubsMap.get(p.getPublicationId())!=null&&pubsMap.get(p.getPublicationId())!=null){
					if(pubsMap.get(p.getPublicationId()).getIfs()!=null){
						allFirstIf+=Float.parseFloat(pubsMap.get(p.getPublicationId()).getIfs());
					}
					if(pubsMap.get(p.getPublicationId()).getPublicationType()!=null&&pubsMap.get(p.getPublicationId()).getPublicationType().equals("SCI")){
						allFirstSciCount++;
					}
				}
			}
		}
		mv.addObject("allFirstIf",allFirstIf);
		mv.addObject("allFirstSciCount",allFirstSciCount);
		//获取课题
		List<InstitutionTopic> topics=applyService.getTopicsByAuthorId(authors,user.getId(), startYear, endYear);
		mv.addObject("topics", topics);
		//获取专利和软件著作权
		List<InstitutionPatent> patents=applyService.getPatentsByAuthorId(authors, startYear, endYear);
		mv.addObject("patents", patents);
		List<InstitutionCopyright> copyrights=applyService.getCopyrightsByAuthorId(authors,startYear, endYear);
		mv.addObject("copyrights", copyrights);
		//获取奖励
		List<InstitutionAward> awards=applyService.getAwardsByAuthorId(authors,startYear, endYear);
		mv.addObject("awards", awards);
		//获取学术任职
		List<InstitutionAcademic> academics=applyService.getAcademicsByAuthorId(user.getId(),startYear, endYear);
		mv.addObject("academics", academics);
		//获取学生
		List<InstitutionTraining> trainings=applyService.getTrainingsByUserId(user.getId(), startTime, endTime);
		mv.addObject("trainings", trainings);
		//期刊
		mv.addObject("pubsMap",extractPub(paperService.getAllPubs()));
		mv.addObject("user", user);
		mv.addObject("applicationId", applicationId);
		mv.addObject("domain", domain);
		addBaseData(mv,user);
		return mv;
	}
	@RequestMapping("update/{id}")
	public ModelAndView update(@PathVariable("id")int applyId,@PathVariable("domain")String domain,HttpServletRequest request){
		SimpleUser user = SessionUtils.getUser(request);
		Map<Integer,InstitutionPublication> pubsMap=extractPub(paperService.getAllPubs());
		InstitutionJobApply jobApply=applyService.getById(applyId);
		
		ModelAndView mv=new ModelAndView("institution/jobapply/jobApply");
		mv.addObject("jobApply",jobApply);
//		mv.addObject("returnPage",returnPage);
		

		
		int applicationId=jobApply.getApplicationId();
		List<InstitutionJobApplication> applications=applyService.getAllList(user.getInstitutionId());
		mv.addObject("applicationMap", extractApplication(applications));
		DetailedUser detailUser = userService.getDetailedUser(user.getId());
		mv.addObject("detailUser", detailUser);
		InstitutionMemberFromVmt member = applyService.getMemberByUid(user.getInstitutionId(), user.getId());
		mv.addObject("member", member);
		List<Work> works = workService.getWorksByUID(user.getId());
		mv.addObject("works", works);
		List<Education> edus = eduService.getEducationsByUid(user.getId());
		mv.addObject("educations", edus);
		//获取论文
		Date startTime=extractApplication(applications).get(applicationId).getStartTime();
		Date endTime=extractApplication(applications).get(applicationId).getEndTime();
		Calendar cl = Calendar.getInstance(); 
		cl.setTime(startTime);
		int startYear=cl.get(Calendar.YEAR);
		cl.setTime(endTime);
		int endYear=cl.get(Calendar.YEAR);
		List<InstitutionAuthor> authors=applyService.getAuthorsByUid(user.getId());
		mv.addObject("authors", extractAuthor(authors));
		List<InstitutionPaper> firstPapers=applyService.getPapersByAuthorId(authors, startYear, endYear);
		mv.addObject("firstPapers", firstPapers);
		//sci论文数量
		int firstSciCount=0;
		float firstIf=0;
		for (InstitutionPaper p : firstPapers) {
			if(p.getPublicationId()!=0){
				if(pubsMap.get(p.getPublicationId())!=null&&pubsMap.get(p.getPublicationId()).getPublicationType()!=null){
					firstIf+=Float.parseFloat(pubsMap.get(p.getPublicationId()).getIfs());
				if(pubsMap.get(p.getPublicationId()).getPublicationType().equals("SCI")){
					firstSciCount++;
				}
				}
			}
		}
		mv.addObject("firstSciCount", firstSciCount);
		mv.addObject("firstIf", firstIf);
		List<InstitutionPaper> otherPapers=applyService.getOtherPapersByAuthorId(authors, startYear, endYear);
		mv.addObject("otherPapers", otherPapers);
		//sci论文数量
		int otherSciCount=0;
		float otherIf=0;
		for (InstitutionPaper p : otherPapers) {
			if(p.getPublicationId()!=0){
				if(pubsMap.get(p.getPublicationId())!=null&&pubsMap.get(p.getPublicationId()).getPublicationType()!=null){
					otherIf+=Float.parseFloat(pubsMap.get(p.getPublicationId()).getIfs());
				if(pubsMap.get(p.getPublicationId()).getPublicationType().equals("SCI")){
					otherSciCount++;
				}
				}
			}
		}
		mv.addObject("otherSciCount", otherSciCount);
		mv.addObject("ifs", otherIf+firstIf);
		int papersCount = paperService.getPapersCountByUser(user.getId());
		List<InstitutionPaper> allPapers=paperService.getPapersByUID(user.getId());
		float allIf=0;
		for (InstitutionPaper p : allPapers) {
			if(pubsMap.get(p.getPublicationId())!=null&&pubsMap.get(p.getPublicationId()).getPublicationType()!=null){
				allIf+=Float.parseFloat(pubsMap.get(p.getPublicationId()).getIfs());
			}
		}
		mv.addObject("allIf",allIf);
		mv.addObject("papersCount",papersCount);
		int allSciCount=applyService.getAllSciCount(user.getId());
		mv.addObject("allSciCount",allSciCount);
		List<InstitutionPaper> allFirstPapers=applyService.getAllFirst(authors);
		mv.addObject("allFirstCount",allFirstPapers.size());
		int allFirstSciCount=0;
		float allFirstIf=0;
		for (InstitutionPaper p : allFirstPapers) {
			if(p.getPublicationId()!=0){
				if(pubsMap.get(p.getPublicationId())!=null&&pubsMap.get(p.getPublicationId()).getPublicationType()!=null){
					allFirstIf+=Float.parseFloat(pubsMap.get(p.getPublicationId()).getIfs());
					if(pubsMap.get(p.getPublicationId()).getPublicationType().equals("SCI")){
						allFirstSciCount++;
					}
				}
			}
		}
		mv.addObject("allFirstIf",allFirstIf);
		mv.addObject("allFirstSciCount",allFirstSciCount);
		//作者
		List<Integer> paperId=CommonUtils.extractSthField(firstPapers,"id");
		if(!CommonUtils.isNull(paperId)){
			Map<Integer,List<InstitutionAuthor>> authorMap=paperService.getListAuthorsMap(paperId);
			mv.addObject("authorMap",authorMap);
		}
		List<Integer> otherPaperId=CommonUtils.extractSthField(otherPapers,"id");
		if(!CommonUtils.isNull(otherPaperId)){
			Map<Integer,List<InstitutionAuthor>> otherAuthorMap=paperService.getListAuthorsMap(otherPaperId);
			mv.addObject("otherAuthorMap",otherAuthorMap);
		}
		//获取课题
		List<InstitutionTopic> topics=applyService.getTopicsByAuthorId(authors,user.getId(), startYear, endYear);
		mv.addObject("topics", topics);
		//获取专利和软件著作权
		List<InstitutionPatent> patents=applyService.getPatentsByAuthorId(authors, startYear, endYear);
		mv.addObject("patents", patents);
		List<InstitutionCopyright> copyrights=applyService.getCopyrightsByAuthorId(authors,startYear, endYear);
		mv.addObject("copyrights", copyrights);
		//获取奖励
		List<InstitutionAward> awards=applyService.getAwardsByAuthorId(authors,startYear, endYear);
		mv.addObject("awards", awards);
		//获取学术任职
		List<InstitutionAcademic> academics=applyService.getAcademicsByAuthorId(user.getId(),startYear, endYear);
		mv.addObject("academics", academics);
		//获取学生
		List<InstitutionTraining> trainings=applyService.getTrainingsByUserId(user.getId(), startTime, endTime);
		mv.addObject("trainings", trainings);
		//期刊
		mv.addObject("pubsMap",extractPub(paperService.getAllPubs()));
		mv.addObject("user", user);
		mv.addObject("applicationId", applicationId);
		mv.addObject("domain", domain);
		addBaseData(mv,user);
		return mv;
	}
	
	@RequestMapping("save/{status}")
	@ResponseBody
	public JsonResult save(
			@PathVariable("status")int status,
			InstitutionJobApply jobApply,
			@PathVariable("domain")String domain,
			HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			return null;
//		}
		SimpleUser user = SessionUtils.getUser(request);
		//新建
		if(jobApply.getId()==0){
			applyService.insert(user.getInstitutionId(), user.getId(), jobApply.getApplicationId(), status, jobApply);
		}else{
			applyService.update(jobApply.getId(), status, jobApply);
		}
		
		return new JsonResult();
	}
	
	@RequestMapping("report")
	public void report(
			@RequestParam("applyId")int applyId,
			@PathVariable("domain")String domain,
			HttpServletRequest request, HttpServletResponse resp) throws IOException{
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			return;
//		}
		SimpleUser user = SessionUtils.getUser(request);
		
		InstitutionJobApply jobApply = applyService.getById(applyId);
		List<InstitutionJobApplication> applications=applyService.getAllList(user.getInstitutionId());
//		mv.addObject("applicationMap", extractApplication(applications));
		Map<Integer,InstitutionPublication>pubsMap=extractPub(paperService.getAllPubs());
		DetailedUser detailUser = userService.getDetailedUser(jobApply.getUserId());
		InstitutionMemberFromVmt member = applyService.getMemberByUid(user.getInstitutionId(), user.getId());
//		SimpleUser user = userService.getSimpleUser(jobApply.getUserId());
		
		List<Work> works = workService.getWorksByUID(user.getId());
		
		List<Education> edus = eduService.getEducationsByUid(user.getId());
		
		//获取论文
		Date startTime=extractApplication(applications).get(jobApply.getApplicationId()).getStartTime();
		Date endTime=extractApplication(applications).get(jobApply.getApplicationId()).getEndTime();
		Calendar cl = Calendar.getInstance(); 
		cl.setTime(startTime);
		int startYear=cl.get(Calendar.YEAR);
		cl.setTime(endTime);
		int endYear=cl.get(Calendar.YEAR);
		List<InstitutionAuthor> authors=applyService.getAuthorsByUid(user.getId());
		
		List<InstitutionPaper> firstPapers=applyService.getPapersByAuthorId(authors, startYear, endYear);
		
		List<InstitutionPaper> otherPapers=applyService.getOtherPapersByAuthorId(authors, startYear, endYear);
		
		//作者
		List<Integer> paperId=CommonUtils.extractSthField(firstPapers,"id");
		Map<Integer,List<InstitutionAuthor>> authorMap=null;
		if(!CommonUtils.isNull(paperId)){
			authorMap=paperService.getListAuthorsMap(paperId);
			
		}
		List<Integer> otherPaperId=CommonUtils.extractSthField(otherPapers,"id");
		Map<Integer,List<InstitutionAuthor>> otherAuthorMap=null;
		if(!CommonUtils.isNull(otherPaperId)){
			otherAuthorMap=paperService.getListAuthorsMap(otherPaperId);
			
		}
		//论文数量
		int papersCount = paperService.getPapersCountByUser(user.getId());
		List<InstitutionPaper> allPapers=paperService.getPapersByUID(user.getId());
//		float allIf=0;
//		for (InstitutionPaper p : allPapers) {
//			if(pubsMap.get(p.getPublicationId())!=null&&pubsMap.get(p.getPublicationId()).getPublicationType()!=null){
//				allIf+=Float.parseFloat(pubsMap.get(p.getPublicationId()).getIfs());
//			}
//		}
		Double allIf=0.000;
		String ifs=null;
		for (InstitutionPaper paper : allPapers) {
			if(pubsMap.get(paper.getPublicationId())!=null){
				ifs=pubsMap.get(paper.getPublicationId()).getIfs();
				BigDecimal bd1 = new BigDecimal(ifs==null?"0":ifs);
		        BigDecimal bd2 = new BigDecimal(Double.toString(allIf));
		        allIf = bd1.add(bd2).doubleValue();
			}
		}
		int allSciCount=applyService.getAllSciCount(user.getId());
		List<InstitutionPaper> allFirstPapers=applyService.getAllFirst(authors);
		int allFirstSciCount=0;
		Double allFirstIf=0.000;
		for (InstitutionPaper paper : allFirstPapers) {
			if(pubsMap.get(paper.getPublicationId())!=null){
				ifs=pubsMap.get(paper.getPublicationId()).getIfs();
				BigDecimal bd1 = new BigDecimal(ifs==null?"0":ifs);
		        BigDecimal bd2 = new BigDecimal(Double.toString(allFirstIf));
		        allFirstIf = bd1.add(bd2).doubleValue();
		        if(pubsMap.get(paper.getPublicationId()).getPublicationType()!=null&&pubsMap.get(paper.getPublicationId()).getPublicationType().equals("SCI")){
					allFirstSciCount++;
				}
			}
		}
//		for (InstitutionPaper p : allFirstPapers) {
//			if(p.getPublicationId()!=0){
//				if(pubsMap.get(p.getPublicationId())!=null&&pubsMap.get(p.getPublicationId()).getPublicationType()!=null){
//					allFirstIf+=Float.parseFloat(pubsMap.get(p.getPublicationId()).getIfs());
//					if(pubsMap.get(p.getPublicationId()).getPublicationType().equals("SCI")){
//						allFirstSciCount++;
//					}
//				}
//			}
//		}
		int otherSciCount=0;
		Double otherIf=0.000;
		for (InstitutionPaper paper : otherPapers) {
			if(pubsMap.get(paper.getPublicationId())!=null){
				ifs=pubsMap.get(paper.getPublicationId()).getIfs();
				BigDecimal bd1 = new BigDecimal(ifs==null?"0":ifs);
		        BigDecimal bd2 = new BigDecimal(Double.toString(otherIf));
		        otherIf = bd1.add(bd2).doubleValue();
		        if(pubsMap.get(paper.getPublicationId()).getPublicationType()!=null&&pubsMap.get(paper.getPublicationId()).getPublicationType().equals("SCI")){
					otherSciCount++;
				}
			}
		}
//		for (InstitutionPaper p : otherPapers) {
//			if(p.getPublicationId()!=0){
//				if(pubsMap.get(p.getPublicationId())!=null&&pubsMap.get(p.getPublicationId()).getPublicationType()!=null){
//					otherIf+=Float.parseFloat(pubsMap.get(p.getPublicationId()).getIfs());
//				if(pubsMap.get(p.getPublicationId()).getPublicationType().equals("SCI")){
//					otherSciCount++;
//				}
//				}
//			}
//		}
		int firstSciCount=0;
		Double firstIf=0.000;
		for (InstitutionPaper paper : firstPapers) {
			if(pubsMap.get(paper.getPublicationId())!=null){
				ifs=pubsMap.get(paper.getPublicationId()).getIfs();
				BigDecimal bd1 = new BigDecimal(ifs==null?"0":ifs);
		        BigDecimal bd2 = new BigDecimal(Double.toString(firstIf));
		        firstIf = bd1.add(bd2).doubleValue();
		        if(pubsMap.get(paper.getPublicationId()).getPublicationType()!=null&&pubsMap.get(paper.getPublicationId()).getPublicationType().equals("SCI")){
		        	firstSciCount++;
				}
			}
		}
//		for (InstitutionPaper p : firstPapers) {
//			if(p.getPublicationId()!=0){
//				if(pubsMap.get(p.getPublicationId())!=null&&pubsMap.get(p.getPublicationId()).getPublicationType()!=null){
//					firstIf+=Float.parseFloat(pubsMap.get(p.getPublicationId()).getIfs());
//				if(pubsMap.get(p.getPublicationId()).getPublicationType().equals("SCI")){
//					firstSciCount++;
//				}
//				}
//			}
//		}
		//获取课题
		List<InstitutionTopic> topics=applyService.getTopicsByAuthorId(authors,user.getId(), startYear, endYear);
		
		//获取专利和软件著作权
		List<InstitutionPatent> patents=applyService.getPatentsByAuthorId(authors, startYear, endYear);
		
		List<InstitutionCopyright> copyrights=applyService.getCopyrightsByAuthorId(authors,startYear, endYear);
		
		//获取奖励
		List<InstitutionAward> awards=applyService.getAwardsByAuthorId(authors,startYear, endYear);
		
		//获取学术任职
		List<InstitutionAcademic> academics=applyService.getAcademicsByAuthorId(user.getId(),startYear, endYear);
		
		//获取学生
		List<InstitutionTraining> trainings=applyService.getTrainingsByUserId(user.getId(), startTime, endTime);
		
		//期刊
//		mv.addObject("pubsMap",extractPub(paperService.getAllPubs()));
//		mv.addObject("user", user);
//		mv.addObject("applicationId", applicationId);
//		//学科方向
//		Map<Integer,InstitutionOptionVal> disciplineOrientations= optionValService.getMapByOptionId(InstitutionOption.PAPER_DISCIPLINE_ORIENTATION, user.getInstitutionId());
//		//论著出版社
//		Map<Integer,InstitutionOptionVal> publishers= optionValService.getMapByOptionId(InstitutionOption.TREATISE_PUBLISHER, user.getInstitutionId());
//		//奖励名称
//		Map<Integer,InstitutionOptionVal> awardNames= optionValService.getMapByOptionId(InstitutionOption.AWARD_NAME, user.getInstitutionId());
		
		//类别
		Map<Integer,InstitutionOptionVal> awardTypes= optionValService.getMapByOptionId(InstitutionOption.AWARD_TYPE, user.getInstitutionId());

		//等级
		Map<Integer,InstitutionOptionVal> awardGrades= optionValService.getMapByOptionId(InstitutionOption.AWARD_GRADE, user.getInstitutionId());
		//学术任职
		Map<Integer,InstitutionOptionVal> organizationNames= optionValService.getMapByOptionId(InstitutionOption.ACADEMIC_ASSIGNMENT_ORGANIZATION_NAME, user.getInstitutionId());
		Map<Integer,InstitutionOptionVal> positions= optionValService.getMapByOptionId(InstitutionOption.ACADEMIC_ASSIGNMENT_POSITION, user.getInstitutionId());
		//课题
		Map<Integer,InstitutionOptionVal> types= optionValService.getMapByOptionId(InstitutionOption.TOPIC_TYPE, user.getInstitutionId());
		//学生
		Map<Integer,InstitutionOptionVal> degrees= optionValService.getMapByOptionId(InstitutionOption.TRAINING_DEGREE, user.getInstitutionId());
	
		
//		 Map<String, Object> dataMap = new HashMap<String, Object>();  
//         dataMap.put("name", user.getZhName());
//         if(member.getSex().equals("male")){
//	    	 dataMap.put("sex", "男"); 
//			}
//			if(member.getSex().equals("female")){
//				dataMap.put("sex", "女"); 
//			}
//		cl = Calendar.getInstance();
//		cl.setTime(member.getBirth());
//		dataMap.put("year", cl.get(Calendar.YEAR));
//		Map<Integer,String> positionMap=new HashMap<Integer,String>();
//		positionMap.put(1, "研究员");
//		positionMap.put(2, "副研究员");
//		positionMap.put(3, "正研级高工");
//		positionMap.put(4, "高级工程师");
//		positionMap.put(5, "编审");
//		positionMap.put(6, "副编审");
//		positionMap.put(7, "高级实验师");
//		dataMap.put("position", positionMap.get(jobApply.getJobId()));
//			
//		//教育工作经历
//         List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();//题目  
////         List<Map<String, Object>> list11 = new ArrayList<Map<String, Object>>();//答案  
//         
//         String str="";
////		 Calendar cl = Calendar.getInstance();
//         for (int i = 0; i < works.size(); i++) { 
//        	 str+=works.get(i).getAliasInstitutionName()+"   "+works.get(i).getDepartment();
//				if(works.get(i).getDepartment()!=null&&!"".equals(works.get(i).getDepartment())){
//					str+=","+works.get(i).getPosition()+"|";
////					Calendar cl = Calendar.getInstance();
//					if(works.get(i).getBeginTime()!=null){
//						cl.setTime(works.get(i).getBeginTime());
//						str+=cl.get(Calendar.YEAR)+"."+cl.get(Calendar.MONTH)+"."+cl.get(Calendar.DAY_OF_MONTH);
//					}
//					if(works.get(i).getEndTime()!=null){
//						cl.setTime(works.get(i).getEndTime());
//						if(cl.get(Calendar.YEAR)== 3000){
//							str+="-"+"至今"+"\r\n";
//						}else{
//							str+="-"+cl.get(Calendar.YEAR)+"."+cl.get(Calendar.MONTH)+"."+cl.get(Calendar.DAY_OF_MONTH)+"\r\n";
//						}
//					}
//				}
//   
//             Map<String, Object> map = new HashMap<String, Object>();  
//             map.put("work", str); 
//             str="";
//            
//             list1.add(map);  
//   
//         }  
//         for (int i = 0; i < edus.size(); i++) { 
//        	 str+=edus.get(i).getAliasInstitutionName()+"   "+edus.get(i).getDepartment()+","+edus.get(i).getDegree()+"|";
////			Calendar cl = Calendar.getInstance(); 
//			cl.setTime(edus.get(i).getBeginTime());
//			str+=cl.get(Calendar.YEAR)+"."+cl.get(Calendar.MONTH)+"."+cl.get(Calendar.DAY_OF_MONTH)+"-";
//			
//			cl.setTime(edus.get(i).getEndTime());
//			if(cl.get(Calendar.YEAR)== 3000){
//				str+="至今"+"\r\n";
//			}else{
//				str+=cl.get(Calendar.YEAR)+"."+cl.get(Calendar.MONTH)+"."+cl.get(Calendar.DAY_OF_MONTH)+"\r\n";
//			}
//   
//             Map<String, Object> map = new HashMap<String, Object>();  
//             map.put("work", str);  
//            
//             list1.add(map);  
//             str="";
//         }  
//         dataMap.put("table1", list1); 
////         dataMap.put("table11", list11);
//       //工作业绩
//         dataMap.put("performance",jobApply.getJobPerformance());
//      // 论文  
//         List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();  
////         List<Map<String, Object>> list12 = new ArrayList<Map<String, Object>>();
//         String firstPaper="";
//		 String otherPaper="";
//			int j=1;
//			Map<Integer,InstitutionPublication> pubMap = extractPub(paperService.getAllPubs());
//			for (int i=1;i<=firstPapers.size();i++) {
//				if(pubMap.get(firstPapers.get(i-1).getPublicationId()).getPublicationType().equals("SCI")){
//					firstPaper+="("+j+") ";
//					j++;
//					for (InstitutionAuthor au : authorMap.get(firstPapers.get(i-1).getId())) {
//						firstPaper+=au.getAuthorName()+",";
//					}
//					firstPaper+=firstPapers.get(i-1).getPublicationYear()+":"+firstPapers.get(i-1).getTitle()+"."+
//					pubMap.get(firstPapers.get(i-1).getPublicationId()).getPubName()+","+firstPapers.get(i-1).getVolumeNumber()+"("+
//					firstPapers.get(i-1).getSeries()+"),"+firstPapers.get(i-1).getPublicationPage();
//					if(firstPapers.get(i-1).getDoi()!=null){
//						firstPaper+=",doi:"+firstPapers.get(i-1).getDoi();
//					}
//					firstPaper+=". 【SCI】";
//					for (InstitutionAuthor au : authorMap.get(firstPapers.get(i-1).getId())) {
//						if(extractAuthor(authors).get(au.getId())!=null){
//							if(au.isCommunicationAuthor()==true){
//								firstPaper+="【通讯作者】";
//							}
//							if(au.getOrder()==2){
//								if((au.isAuthorStudent()==true)||(au.isAuthorTeacher()==true)){
//									firstPaper+="【学生在读】";
//								}
//							}
//						}
//						
//					}
//					firstPaper+="\r\n";
//				}
//				 Map<String, Object> map = new HashMap<String, Object>();  
//	             map.put("firstPaper", firstPaper);  
//	            
//	             list2.add(map);  
//			}
//		 dataMap.put("count2",String.valueOf(j-1+otherSciCount));
//		 dataMap.put("count4",String.valueOf(j-1));
//           
//         for (int i=1;i<=firstPapers.size();i++) {
//			if(!pubMap.get(firstPapers.get(i-1).getPublicationId()).getPublicationType().equals("SCI")){
//				firstPaper+="("+j+") ";
//				j++;
//				for (InstitutionAuthor au : authorMap.get(firstPapers.get(i-1).getId())) {
//					firstPaper+=au.getAuthorName()+",";
//				}
//				firstPaper+=firstPapers.get(i-1).getPublicationYear()+":"+firstPapers.get(i-1).getTitle()+"."+
//				pubMap.get(firstPapers.get(i-1).getPublicationId()).getPubName()+","+firstPapers.get(i-1).getVolumeNumber()+"("+
//				firstPapers.get(i-1).getSeries()+"),"+firstPapers.get(i-1).getPublicationPage();
//				if(firstPapers.get(i-1).getDoi()!=null){
//					firstPaper+=",doi:"+firstPapers.get(i-1).getDoi();
//				}
//				firstPaper+=". ";
//				for (InstitutionAuthor au : authorMap.get(firstPapers.get(i-1).getId())) {
//					if(extractAuthor(authors).get(au.getId())!=null){
//						if(au.isCommunicationAuthor()==true){
//							firstPaper+="【通讯作者】";
//						}
//						if(au.getOrder()==2){
//							if((au.isAuthorStudent()==true)||(au.isAuthorTeacher()==true)){
//								firstPaper+="【学生在读】";
//							}
//						}
//					}
//					
//				}
//				firstPaper+="\r\n";
//			}
//			 Map<String, Object> map = new HashMap<String, Object>();  
//             map.put("firstPaper", firstPaper); 
//             list2.add(map);
//		}
//         dataMap.put("table2", list2); 
//         List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
//         int k=1;
//			for (int i=1;i<=otherPapers.size();i++) {
//				if(pubMap.get(otherPapers.get(i-1).getPublicationId()).getPublicationType().equals("SCI")){
//					otherPaper+="("+k+") ";
//					k++;
//					for (InstitutionAuthor au : otherAuthorMap.get(otherPapers.get(i-1).getId())) {
//						otherPaper+=au.getAuthorName()+",";
//					}
//					otherPaper+=otherPapers.get(i-1).getPublicationYear()+":"+otherPapers.get(i-1).getTitle()+"."+
//					pubMap.get(otherPapers.get(i-1).getPublicationId()).getPubName()+","+otherPapers.get(i-1).getVolumeNumber()+"("+
//					otherPapers.get(i-1).getSeries()+"),"+otherPapers.get(i-1).getPublicationPage();
//					if(otherPapers.get(i-1).getDoi()!=null){
//						otherPaper+=",doi:"+otherPapers.get(i-1).getDoi();
//					}
//					otherPaper+=". 【SCI】";
//					for (InstitutionAuthor au : otherAuthorMap.get(otherPapers.get(i-1).getId())) {
//						if(extractAuthor(authors).get(au.getId())!=null){
//							if(au.isCommunicationAuthor()==true){
//								otherPaper+="【通讯作者】";
//							}
//							if(au.getOrder()==2){
//								if((au.isAuthorStudent()==true)||(au.isAuthorTeacher()==true)){
//									otherPaper+="【学生在读】";
//								}
//							}
//						}
//						
//					}
//					otherPaper+="\r\n";
//				}
//				 Map<String, Object> map = new HashMap<String, Object>();  
//	             map.put("otherPaper", otherPaper); 
//	             list3.add(map);
//			}
//			for (int i=1;i<=otherPapers.size();i++) {
//				if(!pubMap.get(otherPapers.get(i-1).getPublicationId()).getPublicationType().equals("SCI")){
//					otherPaper+="("+k+") ";
//					k++;
//					for (InstitutionAuthor au : otherAuthorMap.get(otherPapers.get(i-1).getId())) {
//						otherPaper+=au.getAuthorName()+",";
//					}
//					otherPaper+=otherPapers.get(i-1).getPublicationYear()+":"+otherPapers.get(i-1).getTitle()+"."+
//					pubMap.get(otherPapers.get(i-1).getPublicationId()).getPubName()+","+otherPapers.get(i-1).getVolumeNumber()+"("+
//					otherPapers.get(i-1).getSeries()+"),"+otherPapers.get(i-1).getPublicationPage();
//					if(otherPapers.get(i-1).getDoi()!=null){
//						otherPaper+=",doi:"+otherPapers.get(i-1).getDoi();
//					}
//					otherPaper+=". ";
//					for (InstitutionAuthor au : otherAuthorMap.get(otherPapers.get(i-1).getId())) {
//						if(extractAuthor(authors).get(au.getId())!=null){
//							if(au.isCommunicationAuthor()==true){
//								otherPaper+="【通讯作者】";
//							}
//							if(au.getOrder()==2){
//								if((au.isAuthorStudent()==true)||(au.isAuthorTeacher()==true)){
//									otherPaper+="【学生在读】";
//								}
//							}
//						}
//						
//					}
//					otherPaper+="\r\n";
//				}
//				 Map<String, Object> map = new HashMap<String, Object>();  
//	             map.put("otherPaper", otherPaper); 
//	             list3.add(map);
//			}
//		 dataMap.put("table3", list3);
//		//论文数量
//		 dataMap.put("allcount1",papersCount);
//		 dataMap.put("allcount2",allSciCount);
//		 dataMap.put("allcount3",allFirstPapers.size());
//		 dataMap.put("allcount4",allFirstSciCount);
//		
//		 dataMap.put("count1",firstPapers.size()+otherPapers.size());
//		
//		 dataMap.put("count3",firstPapers.size());
//		
//		 dataMap.put("allIf",allIf);
//		 dataMap.put("allFirstIf",allFirstIf);
//		 BigDecimal bd1 = new BigDecimal(Double.toString(otherIf));
//	     BigDecimal bd2 = new BigDecimal(Double.toString(firstIf));
//		 dataMap.put("ifs",bd1.add(bd2).doubleValue());
//		 dataMap.put("firstIf",firstIf);
//		//课题
//		 List<Map<String, Object>> list4 = new ArrayList<Map<String, Object>>();
//		for(int i = 1; i <= topics.size(); i++){
//			InstitutionTopic topic = topics.get(i-1);
//			 Map<String, Object> map = new HashMap<String, Object>();  
//             map.put("topic_name", topic.getName());
//             map.put("topic_type", types.get(topic.getType()).getVal());
//             map.put("topic_cost", topic.getProject_cost());
//             if(topic.getRole().equals("admin")){
//            	map.put("topic_role", "负责人");
// 			}
// 			if(topic.getRole().equals("member")){
// 				map.put("topic_role", "参与人");
// 			}
// 			map.put("topic_time", topic.getStart_year()+"."+topic.getStart_month() + " - " + 
//					topic.getEnd_year()+"."+topic.getEnd_month());
// 			list4.add(map);
//		}
//		dataMap.put("table4", list4);
//		//专利与成果
//		 List<Map<String, Object>> list5 = new ArrayList<Map<String, Object>>();
//		for(int i = 1; i <= patents.size(); i++){
//			InstitutionPatent patent = patents.get(i-1);
//			Map<String, Object> map = new HashMap<String, Object>();  
//            map.put("patent_name", patent.getName());
//			list5.add(map);
//		}
//		for(int i = 1; i <= copyrights.size(); i++){
//			InstitutionCopyright copyright = copyrights.get(i-1);
//			Map<String, Object> map = new HashMap<String, Object>();  
//            map.put("patent_name", copyright.getName());
//			list5.add(map);
//		}
//		dataMap.put("table5", list5);
//		//奖励
//		 List<Map<String, Object>> list6 = new ArrayList<Map<String, Object>>();
//		for(int i = 1; i <= awards.size(); i++){
//			InstitutionAward award = awards.get(i-1);
//			Map<String, Object> map = new HashMap<String, Object>();  
//            map.put("award_year", award.getYear());
//            map.put("award_type", awardTypes.get(award.getType()).getVal());
//            map.put("award_name", award.getName());
//            map.put("award_grade", awardGrades.get(award.getGrade()).getVal());
//            map.put("award_order", award.getAuthorOrder());
//            list6.add(map);
//		}
//		dataMap.put("table6", list6);
//		//学术任职
//		 List<Map<String, Object>> list7 = new ArrayList<Map<String, Object>>();
//		for(int i = 1; i <= academics.size(); i++){
//			InstitutionAcademic aca = academics.get(i-1);
//			Map<String, Object> map = new HashMap<String, Object>();  
//            map.put("academic_name", organizationNames.get(aca.getName()).getVal());
//            map.put("academic_position", positions.get(aca.getPosition()).getVal());
//            map.put("academic_time", aca.getStartYear()+"."+aca.getStartMonth() + " - " + 
//					aca.getEndYear()+"."+aca.getEndMonth());
//			list7.add(map);
//		}
//		dataMap.put("table7", list7);
//		
//		int count1=0;
//		int count2=0;
//		String docNames="";
//		String masterNames="";
//		for (InstitutionTraining training : trainings) {
//			if(degrees.get(training.getDegree()).getVal().equals("博士")){
//				count1++;
//				docNames+=training.getStudentName()+"   ";
//			}
//			if(degrees.get(training.getDegree()).getVal().equals("硕士")){
//				count2++;
//				masterNames+=training.getStudentName()+"   ";
//			}
//		}
//		dataMap.put("dcount",count1==0?"0":String.valueOf(count1));
//		dataMap.put("dnames",docNames);
//		dataMap.put("mcount",count2==0?"0":String.valueOf(count2));
//		dataMap.put("mnames",masterNames);
//		
//		dataMap.put("remark",jobApply.getRemark());
//		 
//         MDoc doc= new MDoc();
//         File file = null;  
//         InputStream fin = null;  
//         ServletOutputStream out = null; 
//         try{
//        	 file=doc.createDoc(dataMap, "aaa.doc");
//             fin = new FileInputStream(file);  
//             
//             resp.setCharacterEncoding("utf-8");  
//             resp.setContentType("application/msword");  
//             // 设置浏览器以下载的方式处理该文件默认名为resume.doc  
//             resp.addHeader("Content-Disposition", "attachment;filename=resume.doc");  
//               
//             out = resp.getOutputStream();  
//             byte[] buffer = new byte[512];  // 缓冲区  
//             int bytesToRead = -1;  
//             // 通过循环将读入的Word文件的内容输出到浏览器中  
//             while((bytesToRead = fin.read(buffer)) != -1) {  
//                 out.write(buffer, 0, bytesToRead);  
//             }  
//         } finally {  
//             if(fin != null) fin.close();  
//             if(out != null) out.close();  
//             if(file != null) file.delete(); // 删除临时文件  
//         } 
		List<SimpleUser> users = userService.getAllUser();
		InstitutionJobApplication appli=getByApplicationId(jobApply.getApplicationId(),user.getInstitutionId());
		String fileName=extractUser(users).get(jobApply.getUserId()).getZhName()+"-"+appli.getTitle();
         MDoc.getJobApplys(fileName,resp, member,user,jobApply,works,edus,firstPapers,otherPapers,
				authorMap,otherAuthorMap,extractAuthor(authors),extractPub(paperService.getAllPubs()),topics,
				patents,copyrights,awards,academics,trainings,awardTypes,awardGrades,organizationNames,positions,types,degrees,
				papersCount,allSciCount,allFirstPapers,allFirstSciCount,otherSciCount,allIf,firstIf,otherIf,allFirstIf,request);
         
//		List<SimpleUser> users = userService.getAllUser();
//		InstitutionJobApplication appli=getByApplicationId(jobApply.getApplicationId(),user.getInstitutionId());
//		
//		resp.setContentType("application/x-msdownload");
//		String fileName=extractUser(users).get(jobApply.getUserId()).getZhName()+"-"+appli.getTitle();
//	    resp.setHeader("Content-disposition","attachment; filename="+
//	    		new String( fileName.getBytes("utf-8"), "ISO8859-1" ) + ".pdf");
//	    
////		List<InstitutionGrants> grantsList = grantsService.getListByBatchNo(user.getId(), batchNo);
//		ServletOutputStream sos = resp.getOutputStream();
//		ByteArrayOutputStream baos = PdfTemplateHelper.getJobApplys(member,user,jobApply,works,edus,firstPapers,otherPapers,
//				authorMap,otherAuthorMap,extractAuthor(authors),extractPub(paperService.getAllPubs()),topics,
//				patents,copyrights,awards,academics,trainings,awardTypes,awardGrades,organizationNames,positions,types,degrees,
//				papersCount,allSciCount,allFirstPapers,allFirstSciCount,otherSciCount,allIf,firstIf,otherIf,allFirstIf);
//	    baos.writeTo(sos);
//	    
//	    sos.flush();  
//	    sos.close();
	}
	InstitutionJobApplication getByApplicationId(int id,int insId){
		List<InstitutionJobApplication> apps=applyService.getAllList(insId);
		for (InstitutionJobApplication app : apps) {
			if(app.getId()==id){
				return app;
			}
		}
		return null;
	}
	
	@InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) { 
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
        binder.registerCustomEditor(Date.class, new CustomDateEditor(df,false)); 
    }
}
