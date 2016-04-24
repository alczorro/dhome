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
import net.duckling.dhome.domain.institution.InstitutionPublicationStatistic;
import net.duckling.dhome.domain.institution.InstitutionVmtInfo;
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
import net.duckling.vmt.api.domain.VmtUser;
import net.duckling.vmt.api.impl.UserService;

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
import cn.vlabs.rest.ServiceException;



@Controller
@NPermission(member = "iap", authenticated=true)
@RequestMapping("institution/{domain}/backend/readOnly")
public class BackEndPaperController {
	private static final String SPLIT = ",";
	private static final String EMPTY = "";
	private static final String END_EMPTY_IPS = ",0],";
	/**前台显示走势图时需要至少9个点     **/
	private static final int IPS_NUMBER = 9;
	private static final Logger LOG=Logger.getLogger(BackEndPaperController.class);
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
	private UserService vmtUserService;
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
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			LOG.error("list:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return new ModelAndView("institution_backend/permission");
//		}
		ModelAndView mv=new ModelAndView("institution_backend/paper/readlist");
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
		
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			LOG.error("create.author:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return new JsonResult("权限不足");
//		}
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
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			LOG.error("update:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return new JsonResult("权限不足");
//		}
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
		ModelAndView mv=new ModelAndView("institution_backend/paper/readdetail");
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			return null;
//		} 
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
	
	
	
	@RequestMapping("batchImport")
	@ResponseBody
	public void report(@PathVariable("domain") String domain,@RequestParam("page")String page,@RequestParam("paperId[]")int[] paperId,HttpServletRequest request, HttpServletResponse resp){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("import:home is null["+domain+"]");
		}
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			LOG.error("import:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//		}
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
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			LOG.error("import:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//		}
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
	
	@RequestMapping("index")
	public ModelAndView index(@PathVariable("domain")String domain,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
//		
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			LOG.error("list:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return new ModelAndView("institution_backend/permission");
//		}
		
		ModelAndView mv=new ModelAndView("institution_backend/pandect/readindex");
		
		//学历统计
		Map<String, Integer> degreesMap = backendService.getDegreesMap(home.getInstitutionId(),-1);
		
		//部门
		List<InstitutionDepartment> depts=backendService.getVmtDepartment(home.getInstitutionId());
		InstitutionDepartment department = new InstitutionDepartment();
		department.setShortName("全所");
		department.setId(-1);
		depts.add(0,department);
		
		mv.addObject("dept",depts);
		mv.addObject("educations",formatPieData(formatDegree(degreesMap),true));
		
		Map<String, Map<String, Integer>> paperDeptMap = paperService.getPaperStatisticsForDept();
//		mv.addObject("stats", getJSONStatisticForDept(paperDeptMap));
		int paperCount=backendService.getPaperCount(home.getInstitutionId(), -1, -1);
		
		List<ArrayList<Object>> cites = new ArrayList<ArrayList<Object>>();
		List<ArrayList<Object>> pubCounts = new ArrayList<ArrayList<Object>>();
		addPaperStatistic(paperCount,cites,pubCounts,paperDeptMap);
		mv.addObject("deptCites", cites);
		mv.addObject("deptPubCounts", pubCounts);
		
		List<ArrayList<Object>> yearCites = new ArrayList<ArrayList<Object>>();
		List<ArrayList<Object>> yearPubCounts = new ArrayList<ArrayList<Object>>();
		addPaperStatistic(paperCount,yearCites,yearPubCounts,paperService.getPaperStatisticsForYear());
		mv.addObject("yearCites", yearCites);
		mv.addObject("yearPubCounts", yearPubCounts);
		
		
		mv.addObject("dPaperCount", paperCount);
		
		mv.addObject("citedNum",paperService.getPapersCite(home.getInstitutionId()));
//		PageResult<InstitutionMemberFromVmt> result=backendService.getVmtMember(home.getInstitutionId(), page);
//		mv.addObject("page",result);
//		List<SimpleUser> users=userService.getUserByEmails(getEmail(result.getDatas()));
//		mv.addObject("userMap",extract(users));
//		
//		List<Home> homes=userHomeService.getDomainsByUids(getUids(users));
//		mv.addObject("homeMap",extractHome(homes));
//		
//		List<InstitutionDepartment> depts=backendService.getVmtDepartment(getDeptId(result.getDatas()));
//		mv.addObject("deptMap",extractDept(depts));
//		mv.addObject("domain",domain);
		return mv;
	}
	
	public List<ArrayList<Object>> formatPieData(Map<String, Integer> map,boolean isSplit){
		List<ArrayList<Object>> list = new ArrayList<ArrayList<Object>>();
		for(Map.Entry<String, Integer> entry : map.entrySet()){
			ArrayList<Object> temp = new ArrayList<Object>();
			if(isSplit){
				temp.add("'"+entry.getKey()+"'");
			}else{
				temp.add(entry.getKey());
			}
			temp.add(entry.getValue());
			list.add(temp);
		}
		
		return list;
	}
	
	
	/**
	 * 已部门为标准统计论文
	 * @param stats
	 * @return
	 */
	private void addPaperStatistic(int total,List<ArrayList<Object>> cites,List<ArrayList<Object>> pubCounts,Map<String, Map<String, Integer>> data){
		
		int sum=0;
		for(Map.Entry<String, Map<String, Integer>> entry : data.entrySet()){
			
			String key = entry.getKey();
//			if(deptMap != null && !"未知".equals(key)){
//				key = deptMap.get(Integer.valueOf(key)).getShortName();
//			}
			
			ArrayList<Object> cite = new ArrayList<Object>();
			cite.add("'"+key+"'");
			cite.add(entry.getValue().get("cite"));
			cites.add(cite);
			
			ArrayList<Object> count = new ArrayList<Object>();
			count.add("'"+key+"'");
			count.add(entry.getValue().get("count"));
			pubCounts.add(count);
			sum+=entry.getValue().get("count");
		}
		
		if(total-sum>0){
			ArrayList<Object> count = new ArrayList<Object>();
			count.add("'其他'");
			count.add(total-sum);
			pubCounts.add(count);
		}
	}
	/**
	 * 总览
	 * @return 学术成果数量
	 */
	@RequestMapping("count/{departmentId}/{year}")
	@ResponseBody
	private JsonResult getCount(@PathVariable("domain") String domain,@PathVariable("departmentId") int deptId,@PathVariable("year") int year){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		
		JsonResult result = new JsonResult();
		Map<String , Integer> map = new HashMap<String, Integer>();
		int memberCount=backendService.getMemberCount(home.getInstitutionId(), deptId, year);
		map.put("member", memberCount);
		int paperCount=backendService.getPaperCount(home.getInstitutionId(), deptId, year);
		map.put("paper", paperCount);
		
		int treatiseCount=backendService.getTreatiseCount(home.getInstitutionId(), deptId, year);
		map.put("treatise", treatiseCount);
		int awardCount=backendService.getAwardCount(home.getInstitutionId(), deptId, year);
		map.put("award", awardCount);
		
		int copyrightCount=backendService.getCopyrightCount(home.getInstitutionId(), deptId, year);
		map.put("copyright", copyrightCount);
		int patentCount=backendService.getPatentCount(home.getInstitutionId(), deptId, year);
		map.put("patent", patentCount);
		
		int topicCount=backendService.getTopicCount(home.getInstitutionId(), deptId, year);
		map.put("topic", topicCount);
		int academicCount=backendService.getAcademicCount(home.getInstitutionId(), deptId, year);
		map.put("academic", academicCount);
		
		int periodicalCount=backendService.getPeriodicalCount(home.getInstitutionId(), deptId, year);
		map.put("periodical", periodicalCount);
		int trainingCount=backendService.getTrainingCount(home.getInstitutionId(), deptId, year);
		map.put("training", trainingCount);
		
		result.setData(map);
		return result;
	}
	/**
	 * 成员分布
	 * @return
	 */
	@RequestMapping("member/{op}/{deptId}")
	@ResponseBody
	private JsonResult memberDistribute(@PathVariable("domain") String domain,@PathVariable("deptId") int deptId,@PathVariable("op") String op){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		
		int insId = home.getInstitutionId();
		JsonResult result = new JsonResult();
		Map<String , Integer> map = new LinkedHashMap<String, Integer>();
		if(op.equals("degree")){
			map = formatDegree(backendService.getDegreesMap(insId, deptId));
		}
		
		if(op.equals("age")){
			map = formatAge(backendService.getAgesMap(insId, deptId));
		}
		
		if(op.equals("title")){
			Map<String , Integer> temp = backendService.getTitlesMap(insId, deptId);
			for(Map.Entry<String, Integer> entry : temp.entrySet()){
				if(!entry.getKey().equals("其他")){
					map.put(entry.getKey()+"("+entry.getValue()+"人)", entry.getValue());
				}
			}
			map.put("其他"+"("+temp.get("其他")+"人)", temp.get("其他"));
		}
		
		result.setData(formatPieData(map,false));
		return result;
	}
	
	public Map<String,Integer> formatDegree(Map<String, Integer> data){
		String doctor = "博士";
		String master = "硕士";
		String college = "大学本科";
		String other = "其他";
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		map.put(doctor, 0);
		map.put(master, 0);
		map.put(college, 0);
		map.put(other, 0);
		for(Map.Entry<String, Integer> entry : data.entrySet()){
			String key = entry.getKey();
			if(key.contains(doctor)){
				key = doctor;
			}else if(key.contains(master)){
				key = master;
			}else if(key.contains("学士")){
				key = college;
			}else{
				key = other;
			}
			map.put(key, map.get(key) + entry.getValue());
		}
		
		Map<String, Integer> temp = new LinkedHashMap<String, Integer>();
		for(Map.Entry<String, Integer> entry : map.entrySet()){
			temp.put(entry.getKey()+"("+entry.getValue()+"人)", entry.getValue());
		}
		return temp;
	}
	
	public Map<String, Integer> formatAge(Map<String, Integer> data){
		Map<String, Integer> temp = new LinkedHashMap<String, Integer>();
		for(Map.Entry<String, Integer> entry : data.entrySet()){
			String key = entry.getKey();
			if(key.equals("其他")){
				temp.put("其他", entry.getValue());
			}else{
				int age = Integer.valueOf(key);
				if(age < 20){
					key = "20岁以下";
				}else if(age >= 20 && age < 25){
					key ="20 ~ 24岁";
				}else if(age >= 25 && age < 30){
					key ="25 ~ 29岁";
				}else if(age >= 30 && age < 35){
				    key ="30 ~ 34岁";
			    }else if(age >= 35 && age < 40){
				    key ="35 ~ 39岁";
			    }else if(age >= 40 && age < 45){
			    	key ="40 ~ 44岁";
			    }else if(age >= 45 && age < 50){
			    	key ="45 ~ 49岁";
			    }else if(age >= 50 && age < 55){
			    	key ="50 ~ 54岁";
			    }else if(age >= 55 && age < 60){
			    	key ="55 ~ 59岁";
			    }else{
			    	key ="60岁以上";
				}
			
				if(temp.containsKey(key)){
					temp.put(key, temp.get(key) + entry.getValue());
				}else{
					temp.put(key, entry.getValue());
				}
			}
		}
		
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		for(Map.Entry<String, Integer> entry : temp.entrySet()){
			if(!entry.getKey().equals("其他")){
				map.put(entry.getKey()+"("+entry.getValue()+"人)", entry.getValue());
			}
		}
		map.put("其他"+"("+temp.get("其他")+"人)", temp.get("其他"));
		return map;
	}
	
	
	/**
	 * 此处默认stats中的数据都是按年份排好序的
	 * @param stats
	 * @return
	 */
	private JSONObject getJSONStatistic(List<InstitutionPublicationStatistic> stats){
		JSONObject result = new JSONObject();
		StringBuilder cite = new StringBuilder();//每年的引用次数
		StringBuilder num = new StringBuilder();//每年的论文数
		StringBuilder year = new StringBuilder();//年份
		StringBuilder totalCite = new StringBuilder();//截止到该年的累积引用次数
		StringBuilder totalNum = new StringBuilder();//截止到该年的累积论文数
		int curYear = -1;
		if(null != stats && !stats.isEmpty()){
			for(InstitutionPublicationStatistic statsRecord : stats){
				getData(statsRecord, cite, num, year, totalCite, totalNum, curYear);
				curYear = statsRecord.getYear();
			}
			int systemYear = Calendar.getInstance().get(Calendar.YEAR);
			if(curYear != systemYear){
				insertEmptyYear(cite, num, year, totalCite, totalNum, curYear, systemYear+1);
			}
			paddingRecords(cite, num, year, totalCite, totalNum);
			cite.replace(cite.lastIndexOf(SPLIT), cite.length(), EMPTY);
			num.replace(num.lastIndexOf(SPLIT), num.length(), EMPTY);
			year.replace(year.lastIndexOf(SPLIT), year.length(), EMPTY);
			totalCite.replace(totalCite.lastIndexOf(SPLIT), totalCite.length(), EMPTY);
			totalNum.replace(totalNum.lastIndexOf(SPLIT), totalNum.length(), EMPTY);
		}
		result.put("cite", cite.toString());
		result.put("num", num.toString());
		result.put("year", year.toString());
		result.put("totalCite", totalCite.toString());
		result.put("totalNum", totalNum.toString());
		result.put("isEmpty", isNoPaper(stats));
		return result;
	}
	
	
	
	private boolean isNoPaper(List<InstitutionPublicationStatistic> stats){
		if(null == stats || stats.isEmpty()){
			return true;
		}
		InstitutionPublicationStatistic ipsEntity = stats.get(stats.size()-1);
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		if(currentYear == ipsEntity.getYear()){
			return ipsEntity.getTotalPaperCount() == 0;
		}else{
			LOG.info("");
			return false;
		}
	}
	
	
	/**
	 * 将论文统计信息解析出来，并存入对应的StringBuilder对象中
	 * @param ips 论文统计信息
	 * @param cite 引用次数
	 * @param num 论文数量
	 * @param year 年份
	 * @param totalCite 累积引用次数
	 * @param totalNum 累积论文数量
	 * @param curYear 当前年
	 */
	private void getData(InstitutionPublicationStatistic ips, StringBuilder cite,
			StringBuilder num, StringBuilder year, StringBuilder totalCite,
			StringBuilder totalNum, int curYear){
		int ipsYear = ips.getYear();
		if(curYear != -1 && (ipsYear - curYear > 1)){
			insertEmptyYear(cite, num, year, totalCite, totalNum, curYear, ipsYear);
		}
		year.append(ipsYear+SPLIT);
		cite.append("["+ipsYear+SPLIT+ips.getAnnualCitationCount()+"]"+SPLIT);
		num.append("["+ipsYear+SPLIT+ips.getAnnualPaperCount()+"]"+SPLIT);
		totalCite.append("["+ipsYear+SPLIT+ips.getTotalCitationCount()+"]"+SPLIT);
		totalNum.append("["+ipsYear+SPLIT+ips.getTotalPaperCount()+"]"+SPLIT);
	}
	
	/**
	 * 对于论文时间跳跃度比较大的年份之间插入冗余年份信息。 如某机构有两篇论文分别发表于2009、2012年，<br/>
	 * 则插入2010和2011年的冗余信息。
	 * @param cite 引用次数
	 * @param num 论文数量
	 * @param year 年份
	 * @param totalCite 累积引用次数
	 * @param totalNum 累积论文数量
	 * @param curYear 起始年
	 * @param ipsYear 结束年
	 */
	private void insertEmptyYear(StringBuilder cite, StringBuilder num, StringBuilder year, StringBuilder totalCite,
			StringBuilder totalNum, int curYear, int ipsYear){
		String temp = totalCite.substring(0, totalCite.lastIndexOf(SPLIT));
		int start = temp.lastIndexOf(SPLIT)>=0?temp.lastIndexOf(SPLIT)+1:0;
		int end = temp.lastIndexOf(']')>=0?temp.lastIndexOf(']'):temp.length();
		String curTotalCite = temp.substring(start, end);
		temp = totalNum.substring(0, totalNum.lastIndexOf(SPLIT));
		start = temp.lastIndexOf(SPLIT)>=0?temp.lastIndexOf(SPLIT)+1:0;
		end = temp.lastIndexOf(']')>=0?temp.lastIndexOf(']'):temp.length();
		String curTotalNum = temp.substring(start, end);
		for(int i=curYear+1; i<ipsYear; i++){
			year.append(i+SPLIT);
			cite.append("["+i+",0]"+SPLIT);
			num.append("["+i+",0]"+SPLIT);
			totalCite.append("["+i+SPLIT+curTotalCite+"]"+SPLIT);
			totalNum.append("["+i+SPLIT+curTotalNum+"]"+SPLIT);
		}
	}
	
	
	/**
	 * 填充空的统计数据，已满足至少返回9条论文统计信息供前台显示
	 * @param cite
	 * @param num
	 * @param year
	 * @param totalCite
	 * @param totalNum
	 */
	private void paddingRecords(StringBuilder cite, StringBuilder num, StringBuilder year, StringBuilder totalCite,
			StringBuilder totalNum){
		String[] years = year.toString().split(SPLIT);
		int yearNum = years.length;
		if(yearNum < IPS_NUMBER){
			int firstYear = Integer.valueOf(years[0]);
			int paddingLen = IPS_NUMBER - yearNum;
			for(int i =1; i<=paddingLen; i++){
				int tempYear = (firstYear-i);
				year.insert(0, tempYear+",");
				cite.insert(0, "["+tempYear+END_EMPTY_IPS);
				num.insert(0, "["+tempYear+END_EMPTY_IPS);
				totalCite.insert(0, "["+tempYear+END_EMPTY_IPS);
				totalNum.insert(0, "["+tempYear+END_EMPTY_IPS);
			}
		}
	}
	
	
}
