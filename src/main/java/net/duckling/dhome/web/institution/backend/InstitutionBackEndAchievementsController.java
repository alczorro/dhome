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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import net.duckling.dhome.domain.institution.InstitutionAchievements;
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
import net.duckling.dhome.domain.people.Home;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.Work;
import net.duckling.dhome.service.IEducationService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionAchievementsService;
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
@RequestMapping("institution/{domain}/backend/achievements")
public class InstitutionBackEndAchievementsController {
	private static final Logger LOG=Logger.getLogger(InstitutionBackEndAchievementsController.class);
	
	@Autowired
	private IUserService userService;
	@Autowired
	private IInstitutionVmtService vmtService;
	@Autowired
	private IInstitutionBackendService backEndService;
	@Autowired
	private IHomeService userHomeService;
	@Autowired
	private IInstitutionBackendService backendService;
	@Autowired
	private IInstitutionGrantsService grantsService;
	@Autowired
	private IInstitutionAchievementsService achievementsService;
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
		ModelAndView mv=new ModelAndView("institution_backend/member/achievementsList");
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null||page<0){
			return null;
		}
		PageResult<InstitutionAchievements> result= achievementsService.getList(home.getInstitutionId(), page);
		
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
		ModelAndView mv=new ModelAndView("institution_backend/member/addAchievements");
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		
//		mv.addObject("page", result);
		mv.addObject("domain", domain);
		String vmtMemberUrl = config.getUmtServerMemberURL();
		InstitutionVmtInfo vmtInfo = vmtService.getVmtInfoByInstitutionId(home.getInstitutionId());
        vmtMemberUrl +="?dn="+vmtInfo.getDn();
        mv.addObject("memberUrl",vmtMemberUrl);
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
		
//		mv.addObject("page", result);
		mv.addObject("domain", domain);
		String vmtMemberUrl = config.getUmtServerMemberURL();
		InstitutionVmtInfo vmtInfo = vmtService.getVmtInfoByInstitutionId(home.getInstitutionId());
        vmtMemberUrl +="?dn="+vmtInfo.getDn();
        mv.addObject("memberUrl",vmtMemberUrl);
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
		achievementsService.delete(home.getInstitutionId(), id);
		return new ModelAndView(new RedirectView(request.getContextPath()+"/institution/"+domain+"/backend/achievements/list/"+1));
	}
	
	@RequestMapping("details/{id}/{page}")
	public ModelAndView details(@PathVariable("domain")String domain,
			@PathVariable("page")int page,
			SearchInstitutionCondition condition,
			@PathVariable("id")int achievementsId,
			HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution_backend/member/achievementsBatchList");
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
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
		
        mv.addObject("condition",condition);
        mv.addObject("achievementsId",achievementsId);
        mv.addObject("keywordDept",extractDept(depts).get(condition.getMemberDpetId()));
        mv.addObject("keywordTitle",(condition.getTitle()==null)?"全部":(condition.getTitle()));
        
        String vmtMemberUrl = config.getUmtServerMemberURL();
		InstitutionVmtInfo vmtInfo = vmtService.getVmtInfoByInstitutionId(home.getInstitutionId());
        vmtMemberUrl +="?dn="+vmtInfo.getDn();
        mv.addObject("memberUrl",vmtMemberUrl);
		return mv;
	}
	
	@RequestMapping("save")
	@ResponseBody
	public JsonResult save(
			InstitutionAchievements achievements,
			@PathVariable("domain")String domain,
			HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		achievements.setInstitutionId(home.getInstitutionId());
		//新建
		achievementsService.creat(achievements);
		
		return new JsonResult();
	}
	
	@RequestMapping("report")
	public void report(
			@RequestParam("achievementsId")int achievementsId,
			@RequestParam("umtId")String umtId,
			@RequestParam("uid")int uid,
			@PathVariable("domain")String domain,
			HttpServletRequest request, HttpServletResponse resp) throws IOException{
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return;
		}
		InstitutionAchievements achievements = achievementsService.getById(achievementsId);
//		Map<Integer,InstitutionPublication>pubsMap=extractPub(paperService.getAllPubs());
//			SimpleUser user = userService.getSimpleUser(uid);
			InstitutionMemberFromVmt member = vmtService.getVmtMemberByUmtId(home.getInstitutionId(), umtId);
			List<InstitutionDepartment> depts=backEndService.getVmtDepartment(home.getInstitutionId());
			
			//获取论文
				Date startTime=achievements.getStartTime();
				Date endTime=achievements.getEndTime();
				Calendar cl = Calendar.getInstance(); 
				cl.setTime(startTime);
				int startYear=cl.get(Calendar.YEAR);
				cl.setTime(endTime);
				int endYear=cl.get(Calendar.YEAR);
				List<InstitutionAuthor> authors=achievementsService.getAuthorsByUid(uid);
				
				List<InstitutionPaper> papers=achievementsService.getPapersByAuthor(authors, startYear, endYear);
				List<Integer> pubId=CommonUtils.extractSthField(papers, "publicationId");
				
				Map<Integer, InstitutionPublication> pubMap=null;
				if(!CommonUtils.isNull(pubId)){
					if(pubId.size()>1||pubId.get(0)!=0){
						pubMap=CommonUtils.extractSthFieldToMap(paperService.getPubsByIds(pubId), "id");
					}
				}
				List<Integer> paperId=CommonUtils.extractSthField(papers,"id");
				Map<Integer,List<InstitutionAuthor>> authorMap=null;
				if(!CommonUtils.isNull(paperId)){ 
					authorMap=paperService.getListAuthorsMap(paperId);
				}
					

			
			
//			List<SimpleUser> users = userService.getAllUser();
//			InstitutionJobApplication appli=getByApplicationId(jobApply.getApplicationId(),home.getInstitutionId());
			
			resp.setContentType("application/x-msdownload");
			String fileName=member.getTrueName()+"-大气物理所绩效考核表";
			MDoc.getAchievements(fileName, resp, papers, authorMap, pubMap, member, extractDept(depts), authors,request);
		
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
//	InstitutionJobApplication getByApplicationId(int id,int insId){
//		List<InstitutionJobApplication> apps=applyService.getAllList(insId);
//		for (InstitutionJobApplication app : apps) {
//			if(app.getId()==id){
//				return app;
//			}
//		}
//		return null;
//	}
	
	@InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) { 
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
        binder.registerCustomEditor(Date.class, new CustomDateEditor(df,false)); 
    }
}
