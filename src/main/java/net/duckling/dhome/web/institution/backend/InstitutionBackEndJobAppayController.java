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

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
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
import net.duckling.dhome.common.config.AppConfig;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.JSONHelper;
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
import net.duckling.dhome.domain.institution.InstitutionVmtInfo;
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
import net.duckling.dhome.service.IInstitutionVmtService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.IWorkService;
import net.duckling.dhome.web.helper.MDoc;
import net.duckling.dhome.web.helper.PdfTemplateHelper;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


/**
 * 后台岗位评定
 * @author Brett
 *
 */
@Controller
@NPermission(role = "admin", authenticated=true)
@RequestMapping("institution/{domain}/backend/job")
public class InstitutionBackEndJobAppayController {
	private static final Logger LOG=Logger.getLogger(InstitutionBackEndJobAppayController.class);
	
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
	@Autowired
	private AppConfig config;
	@Autowired
	private IInstitutionVmtService vmtService;
	
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
	private boolean validatePrefix(String fileName){
		String lower=fileName.toLowerCase();
		return lower.endsWith(".ftl");
	}
	@RequestMapping(value="/upload",method = RequestMethod.POST,headers = { "X-File-Name" })
	@ResponseBody
	public JsonResult uploadXls(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestHeader("X-File-Name") String fileName) throws IOException{
		if(!validatePrefix(fileName)){
			return new JsonResult("请上传ftl格式文件");
		}
		if(request.getContentLength()<=0){
			return new JsonResult("请勿上传空文件");
		}
		InputStream is = request.getInputStream();
//		String prefix = InstitutionBackEndJobAppayController.class.getResource("/").getPath();
		String path = request.getSession().getServletContext().getRealPath("/WEB-INF/conf/wordTempl.ftl");
		 //输出文档路径及名称  
//		File outFile = new File(new File(prefix).getParent(),"/conf/wordTempl.ftl");
		File outFile = new File(path);
//        Writer out = null;  
        FileOutputStream fos=null;
        try {  
            fos = new FileOutputStream(outFile);  
//            OutputStreamWriter oWriter = new OutputStreamWriter(fos,"UTF-8");  
//             out = new BufferedWriter(oWriter); 
             byte[] buffer = new byte[512];  // 缓冲区  
             int bytesToRead = -1;  
             // 通过循环将读入的Word文件的内容输出到浏览器中  
             while((bytesToRead = is.read(buffer)) != -1) {  
                 fos.write(buffer, 0, bytesToRead);  
             }  
        } catch (FileNotFoundException e1) {  
            e1.printStackTrace();  
        }finally {  
            if(is != null)
				try {
					is.close();
					if(fos != null) fos.close();  
//		            if(file != null) file.delete(); // 删除临时文件  
				} catch (IOException e) {
					e.printStackTrace();
				}  
        }    
//		int clbId=resourceService.createFile(URLDecoder.decode(fileName, "UTF-8"), request.getContentLength(), request.getInputStream());
		JsonResult jr=new JsonResult();
//		jr.setData(clbId);
		return jr;
	}
	@RequestMapping(value="/upload",method = RequestMethod.POST)
	@ResponseBody
	public void uploadXls(
			@RequestParam("qqfile") MultipartFile uplFile,HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		JsonResult jr=new JsonResult();
		if(!validatePrefix(uplFile.getOriginalFilename())){
			jr.setDesc("请上传docx,doc,pdf格式文件");
		}else if(uplFile.getSize()<=0){
			jr.setDesc("请上传docx,doc,pdf格式文件");
		}else{
			String fileName = URLDecoder.decode(uplFile.getOriginalFilename(), "UTF-8");
			InputStream is = request.getInputStream();
			String prefix = MDoc.class.getResource("/").getPath();
			File file = new File(new File(prefix).getParent(),"/conf");
			 //输出文档路径及名称  
	        File outFile = new File(file,"wordTempl.ftl");  
	        Writer out = null;  
	        FileOutputStream fos=null;
	        try {  
	            fos = new FileOutputStream(outFile);  
//	            OutputStreamWriter oWriter = new OutputStreamWriter(fos,"UTF-8");  
//	             out = new BufferedWriter(oWriter); 
	             byte[] buffer = new byte[512];  // 缓冲区  
	             int bytesToRead = -1;  
	             // 通过循环将读入的Word文件的内容输出到浏览器中  
	             while((bytesToRead = is.read(buffer)) != -1) {  
	                 fos.write(buffer, 0, bytesToRead);  
	             }  
	        } catch (FileNotFoundException e1) {  
	            e1.printStackTrace();  
	        }finally {  
	            if(is != null)
					try {
						is.close();
						if(fos != null) fos.close();  
//			            if(file != null) file.delete(); // 删除临时文件  
					} catch (IOException e) {
						e.printStackTrace();
					}  
	        }    
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
		ModelAndView mv=new ModelAndView("institution_backend/member/jobApplicationList");
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null||page<0){
			return null;
		}
		PageResult<InstitutionJobApplication> result= applyService.getList(home.getInstitutionId(), page);
		
		mv.addObject("page", result);
		mv.addObject("domain", domain);
		String vmtMemberUrl = config.getUmtServerMemberURL();
		InstitutionVmtInfo vmtInfo = vmtService.getVmtInfoByInstitutionId(home.getInstitutionId());
        vmtMemberUrl +="?dn="+vmtInfo.getDn();
        mv.addObject("memberUrl",vmtMemberUrl);
		
		return mv;
	}
	
	@RequestMapping("creat")
	public ModelAndView creat(@PathVariable("domain")String domain,
			HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution_backend/member/addJobApplication");
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		String vmtMemberUrl = config.getUmtServerMemberURL();
		InstitutionVmtInfo vmtInfo = vmtService.getVmtInfoByInstitutionId(home.getInstitutionId());
        vmtMemberUrl +="?dn="+vmtInfo.getDn();
        mv.addObject("memberUrl",vmtMemberUrl);
//		mv.addObject("page", result);
		mv.addObject("domain", domain);
		
		return mv;
	}
	@RequestMapping("addTempl")
	public ModelAndView add(@PathVariable("domain")String domain,
			HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution_backend/member/addTemplate");
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		String vmtMemberUrl = config.getUmtServerMemberURL();
		InstitutionVmtInfo vmtInfo = vmtService.getVmtInfoByInstitutionId(home.getInstitutionId());
        vmtMemberUrl +="?dn="+vmtInfo.getDn();
        mv.addObject("memberUrl",vmtMemberUrl);
//		mv.addObject("page", result);
		mv.addObject("domain", domain);
		
		return mv;
	}
	
	@RequestMapping("delete/{id}")
	public ModelAndView delete(
			@PathVariable("domain")String domain,
			@PathVariable("id")int id,
			HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		applyService.delete(home.getInstitutionId(), id);
		return new ModelAndView(new RedirectView(request.getContextPath()+"/institution/"+domain+"/backend/job/list/"+1));
	}
	
	@RequestMapping("details/{page}/{id}")
	public ModelAndView details(@PathVariable("domain")String domain,
			@PathVariable("page")int page,
			@PathVariable("id")int applicationId,
			HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution_backend/member/jobApplyList");
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		PageResult<InstitutionJobApply> result= applyService.getListByApplicationId(home.getInstitutionId(), applicationId, page);
		List<InstitutionJobApplication> applications=applyService.getAllList(home.getInstitutionId());
		mv.addObject("applicationMap", extractApplication(applications));
		mv.addObject("page", result);
		mv.addObject("domain", domain);
		List<SimpleUser> users = userService.getAllUser();
		mv.addObject("userMap", extractUser(users));
		String vmtMemberUrl = config.getUmtServerMemberURL();
		InstitutionVmtInfo vmtInfo = vmtService.getVmtInfoByInstitutionId(home.getInstitutionId());
        vmtMemberUrl +="?dn="+vmtInfo.getDn();
        mv.addObject("memberUrl",vmtMemberUrl);
		return mv;
	}
	
	@RequestMapping("save")
	@ResponseBody
	public JsonResult save(
			InstitutionJobApplication jobApplication,
			@PathVariable("domain")String domain,
			HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		jobApplication.setInstitutionId(home.getInstitutionId());
		//新建
		applyService.creat(jobApplication);
		
		return new JsonResult();
	}
	
	@RequestMapping("report")
	public void report(
			@RequestParam("applyId")int applyId,
			@PathVariable("domain")String domain,
			HttpServletRequest request, HttpServletResponse resp) throws IOException{
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return;
		}
		InstitutionJobApply jobApply = applyService.getById(applyId);
		List<InstitutionJobApplication> applications=applyService.getAllList(home.getInstitutionId());
//		mv.addObject("applicationMap", extractApplication(applications));
		Map<Integer,InstitutionPublication>pubsMap=extractPub(paperService.getAllPubs());
		DetailedUser detailUser = userService.getDetailedUser(jobApply.getUserId());
		SimpleUser user = userService.getSimpleUser(jobApply.getUserId());
		InstitutionMemberFromVmt member = applyService.getMemberByUid(home.getInstitutionId(), user.getId());
		
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
		Map<Integer,InstitutionOptionVal> awardTypes= optionValService.getMapByOptionId(InstitutionOption.AWARD_TYPE, home.getInstitutionId());

		//等级
		Map<Integer,InstitutionOptionVal> awardGrades= optionValService.getMapByOptionId(InstitutionOption.AWARD_GRADE, home.getInstitutionId());
		//学术任职
		Map<Integer,InstitutionOptionVal> organizationNames= optionValService.getMapByOptionId(InstitutionOption.ACADEMIC_ASSIGNMENT_ORGANIZATION_NAME, home.getInstitutionId());
		Map<Integer,InstitutionOptionVal> positions= optionValService.getMapByOptionId(InstitutionOption.ACADEMIC_ASSIGNMENT_POSITION, home.getInstitutionId());
		//课题
		Map<Integer,InstitutionOptionVal> types= optionValService.getMapByOptionId(InstitutionOption.TOPIC_TYPE, home.getInstitutionId());
		//学生
		Map<Integer,InstitutionOptionVal> degrees= optionValService.getMapByOptionId(InstitutionOption.TRAINING_DEGREE, home.getInstitutionId());
	

		
		
		List<SimpleUser> users = userService.getAllUser();
		InstitutionJobApplication appli=getByApplicationId(jobApply.getApplicationId(),home.getInstitutionId());
		
		resp.setContentType("application/x-msdownload");
		String fileName=extractUser(users).get(jobApply.getUserId()).getZhName()+"-"+appli.getTitle();
		MDoc.getJobApplys(fileName,resp, member,user,jobApply,works,edus,firstPapers,otherPapers,
				authorMap,otherAuthorMap,extractAuthor(authors),extractPub(paperService.getAllPubs()),topics,
				patents,copyrights,awards,academics,trainings,awardTypes,awardGrades,organizationNames,positions,types,degrees,
				papersCount,allSciCount,allFirstPapers,allFirstSciCount,otherSciCount,allIf,firstIf,otherIf,allFirstIf,request);
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
