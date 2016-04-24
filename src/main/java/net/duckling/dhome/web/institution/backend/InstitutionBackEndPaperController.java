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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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
import net.duckling.dhome.crawler.crawler.CrawlTask;
import net.duckling.dhome.crawler.crawler.ISISite;
import net.duckling.dhome.domain.institution.DatabasePaper;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionMemberFromVmt;
import net.duckling.dhome.domain.institution.InstitutionOption;
import net.duckling.dhome.domain.institution.InstitutionOptionVal;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.InstitutionPaperCiteQueue;
import net.duckling.dhome.domain.institution.InstitutionPublication;
import net.duckling.dhome.domain.institution.SearchInstitutionPaperCondition;
import net.duckling.dhome.domain.object.JsonResult;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendPaperService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionOptionValService;
import net.duckling.dhome.service.IInstitutionPaperCiteQueueService;
import net.duckling.dhome.service.IInstitutionVmtService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.impl.ClbFileService;
import net.duckling.dhome.web.helper.MDoc;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

import cn.cnic.cerc.dlog.util.TimeUtil;



@Controller
@NPermission(role = "admin", authenticated=true)
@RequestMapping("institution/{domain}/backend/paper")
public class InstitutionBackEndPaperController {
	private static final Logger LOG=Logger.getLogger(InstitutionBackEndPaperController.class);
	private static int citeCount = 0;
	private static Thread citeThread;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IHomeService userHomeService;
	@Autowired
	private IInstitutionBackendService backendService;
	
	@Autowired
	private IInstitutionBackendPaperService paperService;
	
	@Autowired
	private IInstitutionHomeService homeService;
	
	@Autowired
    private ClbFileService resourceService;
	
	@Autowired
	private IInstitutionOptionValService optionValService;
	@Autowired
	private IInstitutionVmtService vmtService;
	@Autowired
	private IInstitutionPaperCiteQueueService institutionPaperCiteQueueService;
	
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

	private boolean validatePrefix(String fileName){
		String lower=fileName.toLowerCase();
		return lower.endsWith(".pdf")||lower.endsWith(".doc")||lower.endsWith(".docx");
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
			String fileName = URLDecoder.decode(uplFile.getOriginalFilename(), "UTF-8");
			int clbId=resourceService.createFile(fileName, (int)uplFile.getSize(), uplFile.getInputStream());
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
	
	@RequestMapping("index/{page}")
	public ModelAndView index(
			@PathVariable("domain")String domain,
			@PathVariable("page")int page,
			SearchInstitutionPaperCondition condition,
			HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null||page<0){
			LOG.error("list:page null or home null");
			return null;
		}
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("list:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return null;
		}
		ModelAndView mv=new ModelAndView("institution_backend/paper/relateIndex");
		mv.addObject("domain",domain);
		PageResult<InstitutionAuthor> pageR=paperService.getAuthors(home.getInstitutionId(), page, condition);
		mv.addObject("page",pageR);
		
		mv.addObject("status",condition.getStatus());
		List<InstitutionMemberFromVmt> users=vmtService.getAllUser();
		Map<String,InstitutionMemberFromVmt> userMap= new HashMap<String,InstitutionMemberFromVmt>();
		for (InstitutionMemberFromVmt user : users) {
			userMap.put(user.getCstnetId(), user);
		}
		mv.addObject("userMap",userMap);
		mv.addObject("condition",condition);
//		userService.getSimpleUser(uid)
		return mv;
	}
	@RequestMapping("relate/{page}")
	public ModelAndView relate(
			@PathVariable("domain")String domain,
			@RequestParam("authorId") int authorId,
			@PathVariable("page")int page,
			HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("list:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return null;
		}
		ModelAndView mv=new ModelAndView("institution_backend/paper/relate");
		mv.addObject("domain",domain);
		InstitutionAuthor author=paperService.getAuthorById(authorId);
		mv.addObject("author",author);
		PageResult<InstitutionPaper> pageR=paperService.getPapersByKey(home.getInstitutionId(), page, author.getAuthorName());
		mv.addObject("page",pageR);
		List<Integer> paperId=CommonUtils.extractSthField(pageR.getDatas(),"id");
		if(!CommonUtils.isNull(paperId)){
			Map<Integer,List<InstitutionAuthor>> authorMap=paperService.getListAuthorsMap(paperId);
			mv.addObject("authorMap",authorMap);
		}
		List<Integer> pubId=CommonUtils.extractSthField(pageR.getDatas(), "publicationId");
		
		if(!CommonUtils.isNull(pubId)){
			if(pubId.size()>1||pubId.get(0)!=0){
				mv.addObject("pubMap",CommonUtils.extractSthFieldToMap(paperService.getPubsByIds(pubId), "id"));
			}
		}
//		SimpleUser user = userService.getSimpleUser(author.getCstnetId());
		InstitutionMemberFromVmt user = vmtService.getMemberByCstnetId(home.getInstitutionId(), author.getCstnetId());
		mv.addObject("user",user);
		
		return mv;
	}
	@RequestMapping("relateSubmit")
	@ResponseBody
	public JsonResult relateSubmit(
			@RequestParam("paperId[]")String[] paperId,
			@PathVariable("domain")String domain,
			@RequestParam("uid")String uid,
			@RequestParam("authorId[]")String[] authorId,
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
		if(paperId.length!=0&&uid!=null&&!"".equals(uid)){
			
			SimpleUser user = userService.getSimpleUser(uid);
			if(user!=null){
				paperService.insertPaper(paperId, user.getId());
			}
		}
		if(uid!=null&&!"".equals(uid)){
			paperService.insertAuthorUser(uid, home.getInstitutionId(), authorId, 1);
		}else{
//			paperService.insertAuthorUser(uid, home.getInstitutionId(), authorId, 0);
			return new JsonResult("请添加用户");
		}
		return new JsonResult();
	}
	@RequestMapping("relateCancel")
	@ResponseBody
	public JsonResult relateCancel(
			@RequestParam("paperId[]")String[] paperId,
			@PathVariable("domain")String domain,
			@RequestParam("uid")String uid,
			@RequestParam("authorId[]")String[] authorId,
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
		if(paperId.length!=0&&uid!=null&&!"".equals(uid)){
			
			SimpleUser user = userService.getSimpleUser(uid);
			if(user!=null){
				paperService.deletePaperUser(paperId, user.getId());
			}
		}
		paperService.cancelAuthorUser(home.getInstitutionId(), authorId);
		return new JsonResult();
	}
	@RequestMapping("list/{page}")
	public ModelAndView list(
			@PathVariable("domain")String domain,
			@PathVariable("page")int page,
			SearchInstitutionPaperCondition condition,
			HttpServletRequest request){
		long start= System.currentTimeMillis();
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null||page<0){
			LOG.error("list:page null or home null");
			return null;
		}
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("list:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return new ModelAndView("institution_backend/permission");
		}
		ModelAndView mv=new ModelAndView("institution_backend/paper/list");
		mv.addObject("domain",domain);
		PageResult<InstitutionPaper> pageR=null;
		List<InstitutionPaper> papers=null;
		int flag=0;
		if(condition.getKeyword2()!=null&&!"".equals(condition.getKeyword2())){
			pageR=paperService.getPapersByAuthor(home.getInstitutionId(),page,condition);
			papers=paperService.getPaperByAuthor(home.getInstitutionId(),condition);
			flag=2;
		}
		else if(condition.getKeyword3()!=null&&!"".equals(condition.getKeyword3())){
			pageR=paperService.getPapersByPub(home.getInstitutionId(),page,condition);
			papers=paperService.getPaperByPub(home.getInstitutionId(),condition);
			flag=3;
		}else{
			pageR=paperService.getPapers(home.getInstitutionId(),page,condition);
			papers=paperService.getPapers(home.getInstitutionId(),condition);
			flag=1;
		}
//		int[] papersId = new int[99999];
		List<Integer> papersId=new ArrayList<Integer>();
		for (InstitutionPaper p : papers) {
			papersId.add(p.getId());
		}
		mv.addObject("papersId",papersId);
		mv.addObject("page",pageR);
		mv.addObject("flag",flag);
		
		List<Integer> pubId=CommonUtils.extractSthField(pageR.getDatas(), "publicationId");
		
		if(!CommonUtils.isNull(pubId)){
			if(pubId.size()>1||pubId.get(0)!=0){
				mv.addObject("pubMap",CommonUtils.extractSthFieldToMap(paperService.getPubsByIds(pubId), "id"));
			}
		}
		List<Integer> paperId=CommonUtils.extractSthField(pageR.getDatas(),"id");
		if(!CommonUtils.isNull(paperId)){ 
			Map<Integer,List<InstitutionAuthor>> authorMap=paperService.getListAuthorsMap(paperId);
			mv.addObject("authorMap",authorMap);
		}
		mv.addObject("condition",condition);
		
		Map<Integer,Integer> pubYearMap=paperService.getPublicationYearsMap(home.getInstitutionId());
		mv.addObject("pubYearMap",pubYearMap);
		long end= System.currentTimeMillis();
		if(end-start>500){
			LOG.error("search page is too slowly! cost:"+(end-start)+"ms");   
		}
		
		//testCrawl(home.getInstitutionId());
		Calendar calendar = Calendar.getInstance();
		/**
		 * 获取当前年份
		 */
		int year = calendar.get(Calendar.YEAR);
		mv.addObject("year",year);
		
		List<InstitutionDepartment> depts=backendService.getVmtDepartment(home.getInstitutionId());
		InstitutionDepartment department = new InstitutionDepartment();
		department.setShortName("全所");
		department.setId(-1);
		depts.add(0,department);
		mv.addObject("deptMap",extractDept(depts));
		mv.addObject("dept",depts);
		return mv;
	}
	private void addBaseData(ModelAndView mv,InstitutionHome home){
		//学科方向
		Map<Integer,InstitutionOptionVal> disciplineOrientations= optionValService.getMapByOptionId(InstitutionOption.PAPER_DISCIPLINE_ORIENTATION, home.getInstitutionId());
		mv.addObject("disciplineOrientations",disciplineOrientations);
	}
	
	@RequestMapping("identify/{paperId}")
	@ResponseBody
	public ModelAndView identify(
			@RequestParam("status")int status,
			@PathVariable("domain")String domain,
			@PathVariable("paperId")int paperId,
			HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("submit:home is null["+domain+"]");
		}
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("submit:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
		}
		paperService.updateStatus(paperId,status);
		return new ModelAndView(new RedirectView(request.getContextPath()+"/institution/"+domain+"/backend/paper/list/1"));
	}
	@RequestMapping("update/{paperId}")
	public ModelAndView update(@PathVariable("paperId")int paperId,@PathVariable("domain")String domain,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("update:home is null["+domain+"]");
			return null;
		}
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("update:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return null;
		}
		//不是本机构的论文
		if(!paperService.isMyPaper(home.getInstitutionId(),paperId)){
			LOG.error("update:user["+SessionUtils.getUserId(request)+"] wana read paper["+paperId+"]");
			return null;
		}
		InstitutionPaper paper=paperService.getPaperById(paperId);
		if(paper==null){
			LOG.error("update:paper["+paperId+"] is not found!");
			return null;
		}
		ModelAndView mv=new ModelAndView("institution_backend/paper/modify");
		List<InstitutionDepartment> depts=paperService.getDepartments(home.getInstitutionId());
	    mv.addObject("deptMap",extractDept(depts));
		mv.addObject("op","update");
		mv.addObject("pubsMap",extractPub(paperService.getAllPubs()));
		mv.addObject("disciplines",paperService.getAllDiscipline());
		mv.addObject("domain",domain);
		mv.addObject("paper",paper);
//		mv.addObject("returnPage",returnPage);
		addBaseData(mv,home);
		return mv;
	}
	
	@RequestMapping("create")
	public ModelAndView create(@PathVariable("domain")String domain,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("create:home is null["+domain+"]");
			return null;
		}
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("create:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return null;
		}
		ModelAndView mv=new ModelAndView("institution_backend/paper/modify");
		List<InstitutionDepartment> depts=paperService.getDepartments(home.getInstitutionId());
	    mv.addObject("deptMap",extractDept(depts));
		mv.addObject("op","create");
		mv.addObject("pubs",paperService.getAllPubs());
		mv.addObject("disciplines",paperService.getAllDiscipline());
		mv.addObject("domain",domain);
//		mv.addObject("returnPage",returnPage);
		addBaseData(mv,home);
		return mv;
	}
	@RequestMapping("delete")
	@ResponseBody
	public JsonResult delete(@PathVariable("domain") String domain,@RequestParam("page")String page,@RequestParam("paperId[]")int[] paperId,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("delete:home is null["+domain+"]");
		}
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("delete:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
		}
		LOG.info("delete paper ref["+Arrays.toString(paperId)+"]");
		paperService.delete(home.getInstitutionId(),paperId);
		JsonResult result =  new JsonResult();
		result.setData(page);
		return result;
	}
	
	@RequestMapping("submit")
	@ResponseBody
	public JsonResult submit(
			InstitutionPaper paper,
			@PathVariable("domain")String domain,
			@RequestParam("pid")int pid,
			@RequestParam("uid[]")int[] uid,
			@RequestParam("order[]")int[] order,
			@RequestParam("communicationAuthor[]")boolean[] communicationAuthors,
			@RequestParam("authorStudent[]")boolean[] authorStudents,
			@RequestParam("authorTeacher[]")boolean[] authorTeacher,
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
		paper.setStatus(1);
		paper.setPublicationId(pid);
		if(paper.getCitation().equals("--")){
			paper.setCitation("-1");
		}
		//新建
		if(paper.getId()==0){
			//TODO 这里还需要一个逻辑，即为标记为*号的字段，如果与数据库中的记录匹配，不新建论文，而是简历ref，并更新数据库中数据
			paperService.create(paper,home.getInstitutionId(),uid,order,communicationAuthors,authorStudents,authorTeacher);
		}//更新
		else{
			paperService.update(paper,home.getInstitutionId(),uid,order,communicationAuthors,authorStudents,authorTeacher);
		}
		return new JsonResult();
	}
	@RequestMapping("search/keyword")
	@ResponseBody
	public JSONArray searchKeyword(@PathVariable("domain")String domain,@RequestParam("q")String queryString,HttpServletRequest request){
		List<String> allKeyword=paperService.getAllKeyword(queryString);
		JSONArray array=new JSONArray();
		if(CommonUtils.isNull(allKeyword)){
			return array;
		}
		for(String keyword:allKeyword){
			JSONObject obj=new JSONObject();
			obj.put("name", keyword);
			obj.put("id", keyword);
			array.add(obj);
		}
		return array;
	}
	
	@RequestMapping("search/sponsor")
	@ResponseBody
	public JSONArray searchSponsor(@PathVariable("domain")String domain,@RequestParam("q")String queryString,HttpServletRequest request){
		List<String> allSponsor=paperService.getAllSponsor(queryString);
		JSONArray array=new JSONArray();
		if(CommonUtils.isNull(allSponsor)){
			return array;
		}
		for(String sponsor:allSponsor){
			JSONObject obj=new JSONObject();
			obj.put("name", sponsor);
			obj.put("id", sponsor);
			array.add(obj);
		}
		return array;
	}
	@RequestMapping("search/publication")
	@ResponseBody
	public List<InstitutionPublication> searchPub(@PathVariable("domain")String domain,@RequestParam("q")String key,HttpServletRequest request){
		List<InstitutionPublication> pubs=paperService.getPubsByKey(key);
		return pubs;
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
		paperService.createAuthor(author);
		JsonResult jr=new JsonResult();
		jr.setData(author);
		return jr;
	}
	@RequestMapping("search/author")
	@ResponseBody
	public List<InstitutionAuthor> searchAuthor(@RequestParam("q")String keyword,HttpServletRequest request){
		List<InstitutionAuthor> authors=paperService.searchAuthor(CommonUtils.trim(keyword));
		return authors;
	}
	@RequestMapping("search/user")
	@ResponseBody
	public List<InstitutionMemberFromVmt> searchUser(@RequestParam("q")String keyword,HttpServletRequest request){
		List<InstitutionMemberFromVmt> users=vmtService.searchMember(CommonUtils.trim(keyword));
		return users;
	}
	@RequestMapping("/authors/{paperId}")
	@ResponseBody
	public JsonResult getAuthors(@PathVariable("domain")String domain,@PathVariable("paperId")int paperId,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("update:home is null["+domain+"]");
			return new JsonResult("not found institution");
		}
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("update:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		//不是本机构的论文
		if(!paperService.isMyPaper(home.getInstitutionId(),paperId)){
			LOG.error("update:user["+SessionUtils.getUserId(request)+"] wana read paper["+paperId+"]");
			return new JsonResult("非本机构的论文"); 
		}
		List<InstitutionAuthor> authors=paperService.getAuthorsByPaperId(paperId);
		JsonResult jr=new JsonResult();
		jr.setData(authors);
		return jr;
	}
	
	@RequestMapping("author/{paperId}/{authorId}")
	@ResponseBody
	public JsonResult authorDetail(@PathVariable("paperId")int paperId,@PathVariable("domain")String domain,@PathVariable("authorId")int authorId,HttpServletRequest request){
		InstitutionAuthor author=paperService.getAuthorsById(paperId,authorId);
		if(author==null){
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
		JsonResult jr=new JsonResult();
		jr.setData(jsonObj);
		return jr;
	}
	@RequestMapping("author/{authorId}")
	@ResponseBody
	public JsonResult authorDetails(@PathVariable("domain")String domain,@PathVariable("authorId")int authorId,HttpServletRequest request){
		InstitutionAuthor author=paperService.getAuthorById(authorId);
		if(author==null){
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
		JsonResult jr=new JsonResult();
		jr.setData(jsonObj);
		return jr;
	}
	@RequestMapping("detail/{paperId}")
	@ResponseBody
	public ModelAndView detail(@RequestParam("returnPage") int returnPage,@PathVariable("domain")String domain,@PathVariable("paperId")int paperId,HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution_backend/paper/detail");
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			return null;
		} 
		InstitutionPaper paper = paperService.getPaperById(paperId);
		ArrayList<Integer> id=new ArrayList<Integer>();
		id.add(paperId);
		Map<Integer,List<InstitutionAuthor>> authorMap=paperService.getListAuthorsMap(id);
		List<InstitutionDepartment> depts=paperService.getDepartments(home.getInstitutionId());
	    mv.addObject("deptMap",extractDept(depts));
		mv.addObject("paper",paper);
		mv.addObject("returnPage",returnPage);
		mv.addObject("domain",domain);
		mv.addObject("authorMap",authorMap);
		mv.addObject("pubs",paperService.getAllPubs());
		addBaseData(mv,home);
		return mv;
	}
	
	/**
	 * 显示导入bib文件页面
	 * @return
	 */
	@RequestMapping("import")
	@ResponseBody
	public ModelAndView showImportExceltex(HttpServletRequest request, @PathVariable("domain")String domain) {
		ModelAndView mv = new ModelAndView("institution_backend/paper/addExcelPaper");
		mv.addObject("active",request.getRequestURI().replace(request.getContextPath(), ""));
//		mv.addObject("titleUser",homeService.getSimpleUserByDomain(domain));
		mv.addObject("domain",domain);
		return mv;
	}
	/**
	 * 上传bib文件，非IE浏览器
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(params = "func=uploadExceltex", headers = { "X-File-Name" })
	public String uploadExceltex(Model model,HttpServletRequest request, HttpServletResponse response) throws IOException {
		InputStream ins = request.getInputStream();
//		SimpleUser su = (SimpleUser) request.getSession().getAttribute(Constants.CURRENT_USER);
		ExcelReader er = new ExcelReader();
//		userService.updateSimpleUserLastEditTimeByUid(SessionUtils.getUserId(request));
		List<InstitutionPaper> papers = er.analyzePaper(ins);
//			response.setStatus(HttpStatus.SC_OK);
		//导入到数据库
		
		
		JSONArray array =new JSONArray();
		array.addAll(papers);
		model.addAttribute("result", array);
		return "jsontournamenttemplate";
	}
	/**
	 * 上传bib文件，IE浏览器
	 * @param uplFile
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST, params = "func=uploadExceltex")
	public String uploadExceltex(Model model,@RequestParam("qqfile") MultipartFile uplFile, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		InputStream ins = uplFile.getInputStream();
//		SimpleUser su = (SimpleUser) request.getSession().getAttribute(Constants.CURRENT_USER);
		ExcelReader er = new ExcelReader();
		userService.updateSimpleUserLastEditTimeByUid(SessionUtils.getUserId(request));
		List<InstitutionPaper> papers = er.analyzePaper(ins);
//		response.setStatus(HttpServletResponse.SC_OK);
		JSONArray array =new JSONArray();
		array.addAll(papers);
		model.addAttribute("result", array);
		return "jsontournamenttemplate";
	}
	
	@RequestMapping("cite/{paperId}")
	@ResponseBody
	public JsonResult getPaperCite(@PathVariable("domain")String domain,@PathVariable("paperId")int paperId,HttpServletRequest request){
		InstitutionPaper paper = paperService.getPaperById(paperId);
		if(paper!=null){
			InstitutionPaperCiteQueue model = new InstitutionPaperCiteQueue();
			model.setAppendType(InstitutionPaperCiteQueue.APPEND_TYPE_SINGLE);
			model.setPaperId(paperId);
			model.setLastAccess(new Date());
			SimpleUser user = SessionUtils.getUser(request);
			model.setUid(user.getId());
			institutionPaperCiteQueueService.add(model);
		}
		
		String cite = "";
		JsonResult result = new JsonResult();
		if("".equals(cite)){
			cite = paper.getCitation();
		}
		result.setData(cite);
		return result;
	}
	
	@RequestMapping("cite/batch")
	@ResponseBody
	public ModelAndView batchCite(@PathVariable("domain")String domain,HttpServletRequest request){
		final InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		ModelAndView mv = new ModelAndView("institution_backend/paper/citeBatch");
		int remainingCount = institutionPaperCiteQueueService.getCountByBatch();
		mv.addObject("remainingCount", remainingCount);
		if(remainingCount>0){
			return mv;
		}
		
		final List<InstitutionPaper> papers = paperService.getPapers(home.getInstitutionId(), new SearchInstitutionPaperCondition());
		for(InstitutionPaper paper : papers){
			InstitutionPaperCiteQueue model = new InstitutionPaperCiteQueue();
			model.setAppendType(InstitutionPaperCiteQueue.APPEND_TYPE_BATCH);
			model.setPaperId(paper.getId());
			model.setLastAccess(new Date());
			SimpleUser user = SessionUtils.getUser(request);
			model.setUid(user.getId());
			institutionPaperCiteQueueService.add(model);
		}
		mv.addObject("remainingCount", papers.size());
		return mv;
	}
	
	  /*=========================抓取论文引用频次===================================*/
	private void batchCrawl(List<InstitutionPaper> papers) {
		    citeCount = 0;
			for(InstitutionPaper paper : papers){
				excuteCrawl(paper);
				//count++;
				citeCount ++;
				System.out.print("论文数量    "+ citeCount  +"\n");
			}
	}
	
	private String excuteCrawl(InstitutionPaper paper){
		try {
			CrawlTask crawlTask = new CrawlTask();
			String doi = paper.getDoi();
			String cite = "";
			if(doi != null && !doi.equals("")){
				doi = doi.replace("doi:", "");
				doi = doi.replace("DOI:", "");
				doi = doi.replace("DOI", "");
				
				crawlTask.setQuery(doi);
		        crawlTask.setType(CrawlTask.DO);
		        ISISite isiSite = new ISISite(crawlTask);
				
				cite = isiSite.getCite();
				
				System.out.print(doi +"   DOI    "+ cite+"\n");
			}
			
			if(cite.equals("")){
				crawlTask.setQuery(paper.getTitle());
		        crawlTask.setType(CrawlTask.TS);
		        ISISite isiSite = new ISISite(crawlTask);
				
				cite = isiSite.getCite();
				
				System.out.print(paper.getTitle() +"   TS    "+ cite+"\n");
			}
			
			if(!cite.equals("")){
				paper.setCitation(cite);
				paperService.updatePaper(paper);
			}else{
				cite = "--";
				paper.setCitation("-1");
			}
			paper.setCitationQueryTime(new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));
			paperService.updatePaper(paper);
			return cite;
		}catch(Exception e){
			 e.printStackTrace();
		}
		return "--";
	}
	
	

	@RequestMapping("insert")
	public JsonResult insert(@PathVariable("domain") String domain,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		Map<Integer, DatabasePaper> map = new HashMap<Integer, DatabasePaper>();
		JsonResult jr= new JsonResult();
		List<InstitutionDepartment> depts=paperService.getDepartments(home.getInstitutionId());
		List<DatabasePaper> list= paperService.getPaperTemp();
		for (DatabasePaper dp : list) {
			dp.setDepartId(getIdByName(depts,dp.getDepartment()));
		}
		paperService.insertAuthor(list);
			map = paperService.insertPaper(list);
			paperService.insert1(list);
			paperService.insert2(list);
			paperService.insert3(list);
			paperService.insert4(list);
			List<InstitutionPaper> papers= paperService.getAllPapers();
			paperService.insertPaperRef(list, 1799);
			jr.setData("success");
			return jr;
	}
	public int getIdByName(List<InstitutionDepartment> depts,String name){
		for (InstitutionDepartment dept : depts) {
			if(dept.getShortName()!=null){
				if((dept.getShortName()).equals(name)){
					return dept.getId();
				}
			}
		}
		return 0;
	}
	
	@RequestMapping("batchImport")
	@ResponseBody
	public void report(@PathVariable("domain") String domain,@RequestParam("page")String page,@RequestParam("paperId[]")int[] paperId,HttpServletRequest request, HttpServletResponse resp){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("import:home is null["+domain+"]");
		}
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("import:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
		}
		LOG.info("import paper ref["+Arrays.toString(paperId)+"]");
		List<InstitutionPaper> papers = new ArrayList<InstitutionPaper>();
		List<Integer> paperIds = new ArrayList<Integer>();
		for (int id : paperId) {
			InstitutionPaper paper=paperService.getPaperById(id);
			papers.add(paper);
			paperIds.add(id);
		}
		Map<Integer,List<InstitutionAuthor>> authorMap=paperService.getListAuthorsMap(paperIds);
		List<Integer> pubId=CommonUtils.extractSthField(papers, "publicationId");
		
		Map<Integer, InstitutionPublication> pubMap=null;
		if(!CommonUtils.isNull(pubId)){
			if(pubId.size()>1||pubId.get(0)!=0){
				pubMap=CommonUtils.extractSthFieldToMap(paperService.getPubsByIds(pubId), "id");
			}
		}
		resp.setContentType("application/x-msdownload");
		String fileName="论文导出";
		MDoc.getPapers(fileName,resp,papers,authorMap,pubMap, request);
	}
	
	@RequestMapping("importAll")
	@ResponseBody
	public void importAll(
			@PathVariable("domain")String domain,
			SearchInstitutionPaperCondition condition,
			HttpServletRequest request, 
			HttpServletResponse resp){
		long start= System.currentTimeMillis();
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("import:home is null["+domain+"]");
		}
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("import:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
		}
		List<InstitutionPaper> papers=null;
		if(condition.getKeyword2()!=null&&!"".equals(condition.getKeyword2())){
			papers=paperService.getPaperByAuthor(home.getInstitutionId(),condition);
		}
		else if(condition.getKeyword3()!=null&&!"".equals(condition.getKeyword3())){
			papers=paperService.getPaperByPub(home.getInstitutionId(),condition);
		}else{
			papers=paperService.getPapers(home.getInstitutionId(),condition);
		}
//		int[] papersId = new int[99999];
		List<Integer> paperIds=new ArrayList<Integer>();;
		for (InstitutionPaper p : papers) {
			paperIds.add(p.getId());
		}
		
		
		
		
//		List<InstitutionPaper> papers = new ArrayList<InstitutionPaper>();
//		for (int id : paperIds) {
//			InstitutionPaper paper=paperService.getPaperById(id);
//			papers.add(paper);
//		}
		Map<Integer,List<InstitutionAuthor>> authorMap=paperService.getListAuthorsMap(paperIds);
		List<Integer> pubId=CommonUtils.extractSthField(papers, "publicationId");
		
		Map<Integer, InstitutionPublication> pubMap=null;
		if(!CommonUtils.isNull(pubId)){
			if(pubId.size()>1||pubId.get(0)!=0){
				pubMap=CommonUtils.extractSthFieldToMap(paperService.getPubsByIds(pubId), "id");
			}
		}
		
		resp.setContentType("application/x-msdownload");
		String fileName="论文导出";
		MDoc.getPapers(fileName,resp,papers,authorMap,pubMap, request);
	}
	@RequestMapping("getPaperByTitle")
	@ResponseBody
	public JsonResult getPaperByTitle(@PathVariable("domain")String domain,@RequestParam("title")String title,HttpServletRequest request){
		List<InstitutionPaper> paper = paperService.getPaperByTitle(title.trim());
		
		JsonResult jr=new JsonResult();
		if(paper.size()<1){
			jr.setSuccess(false);
		}else{
			jr.setData(paper);
			jr.setSuccess(true);
		}
		return jr;
	}
	@InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) { 
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
        binder.registerCustomEditor(Date.class, new CustomDateEditor(df,true)); 
    }
}
