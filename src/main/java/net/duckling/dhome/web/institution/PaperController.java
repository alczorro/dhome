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

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.auth.sso.Constants;
import net.duckling.dhome.common.dsn.DsnClient;
import net.duckling.dhome.common.dsn.PaperJSONHelper;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.JSONHelper;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.dao.DatabasePaperImpl;
import net.duckling.dhome.dao.IDatabasePaper;
import net.duckling.dhome.domain.institution.DatabasePaper;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.dhome.domain.institution.InstitutionOption;
import net.duckling.dhome.domain.institution.InstitutionOptionVal;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.InstitutionPublication;
import net.duckling.dhome.domain.institution.SearchInstitutionPaperCondition;
import net.duckling.dhome.domain.object.JsonResult;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.domain.people.Paper;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IDsnSearchService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendPaperService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionOptionValService;
import net.duckling.dhome.service.IInstitutionPeopleService;
import net.duckling.dhome.service.IPaperService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.impl.ClbFileService;

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
import com.google.gson.JsonObject;



@Controller
@NPermission(authenticated = true,member = "iap")
@RequestMapping("people/{domain}/admin/paper")
public class PaperController {
	private static final Logger LOG=Logger.getLogger(PaperController.class);
	
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
	private IInstitutionPeopleService peopleService;
	
	@Autowired
	private IInstitutionOptionValService optionValService;
	
	@Autowired
	private IPaperService pService;
	
	@Autowired
	private IDsnSearchService dsnSearchService;
	
	
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
	private void addBaseData(ModelAndView mv,SimpleUser user){
		//学科方向
		Map<Integer,InstitutionOptionVal> disciplineOrientations= optionValService.getMapByOptionId(InstitutionOption.PAPER_DISCIPLINE_ORIENTATION, user.getInstitutionId());
		mv.addObject("disciplineOrientations",disciplineOrientations);
	}
	
	@RequestMapping("list/{page}")
	public ModelAndView list(
			@PathVariable("domain")String domain,
			@PathVariable("page")int page,
			SearchInstitutionPaperCondition condition,
			HttpServletRequest request){
		SimpleUser user = SessionUtils.getUser(request);
		
		long start= System.currentTimeMillis();
		
		ModelAndView mv=new ModelAndView("institution/paper/list");
		mv.addObject("domain",domain);
		PageResult<InstitutionPaper> pageR=paperService.getPapersByUser(user.getId(),page,condition);
		mv.addObject("page",pageR);
		List<InstitutionPaper> papers=paperService.getAllPapers();
		Map<Integer,InstitutionPaper> papersMap= new HashMap<Integer,InstitutionPaper>();
		for (InstitutionPaper paper : papers) {
			papersMap.put(paper.getId(), paper);
		}
		mv.addObject("papersMap",papersMap);
		
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
		List<InstitutionPaper> totalPapers=paperService.getAllPapersByUser(user.getId(), condition);
		List<Integer> totalPubId=CommonUtils.extractSthField(totalPapers, "publicationId");
		Map<Integer,InstitutionPublication> pubMap=new HashMap<Integer,InstitutionPublication>();
		if(!CommonUtils.isNull(pubId)){
			if(pubId.size()>1||pubId.get(0)!=0){
				pubMap=CommonUtils.extractSthFieldToMap(paperService.getPubsByIds(totalPubId), "id");
			}
		}
		
		int totalCitation=0;
		Double totalIf=0.000;
		String ifs=null;
		for (InstitutionPaper paper : totalPapers) {
			if(paper.getCitation()!=null){
				totalCitation+=Integer.parseInt(paper.getCitation().equals("-1")?"0":paper.getCitation());
			}
			if(pubMap!=null&&pubMap.get(paper.getPublicationId())!=null){
				ifs=pubMap.get(paper.getPublicationId()).getIfs();
				BigDecimal bd1 = new BigDecimal(ifs==null?"0":ifs);
		        BigDecimal bd2 = new BigDecimal(Double.toString(totalIf));
		        totalIf = bd1.add(bd2).doubleValue();
			}
		}
		mv.addObject("totalCitation",totalCitation);
		mv.addObject("totalIf",totalIf);
		
		mv.addObject("condition",condition);
		
		mv.addObject("titleUser",user);
		long end= System.currentTimeMillis();
		if(end-start>500){
			LOG.error("search page is too slowly! cost:"+(end-start)+"ms");
		}
		addBaseData(mv,user);
		
		Calendar calendar = Calendar.getInstance();
		/**
		 * 获取当前年份
		 */
		int year = calendar.get(Calendar.YEAR);
		mv.addObject("year",year);
		return mv;
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
	
	@RequestMapping("update/{paperId}")
	public ModelAndView update(@RequestParam("returnPage") int returnPage,@PathVariable("paperId")int paperId,@PathVariable("domain")String domain,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			LOG.error("update:home is null["+domain+"]");
//			return null;
//		}
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			LOG.error("update:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return null;
//		}
//		//不是本机构的论文
//		if(!paperService.isMyPaper(home.getInstitutionId(),paperId)){
//			LOG.error("update:user["+SessionUtils.getUserId(request)+"] wana read paper["+paperId+"]");
//			return null;
//		}
		SimpleUser user = SessionUtils.getUser(request);
		InstitutionPaper paper=paperService.getPaperById(paperId);
		if(paper==null){
			LOG.error("update:paper["+paperId+"] is not found!");
			return null;
		}
		if(paper.getStatus()==1){
			LOG.error("update:paper["+paperId+"] has been certified !");
			return null;
		}
		ModelAndView mv=new ModelAndView("institution/paper/modify");
		List<InstitutionDepartment> depts=paperService.getDepartments(user.getInstitutionId());
	    mv.addObject("deptMap",extractDept(depts));
		mv.addObject("op","update");
		mv.addObject("pubsMap",extractPub(paperService.getAllPubs()));
		mv.addObject("disciplines",paperService.getAllDiscipline());
		mv.addObject("domain",domain);
		mv.addObject("paper",paper);
		mv.addObject("returnPage",returnPage);
		mv.addObject("titleUser",user);
		addBaseData(mv,user);
		return mv;
	}
	
	@RequestMapping("create")
	public ModelAndView create(@PathVariable("domain")String domain,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			LOG.error("create:home is null["+domain+"]");
//			return null;
//		}
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			LOG.error("create:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return null;
//		}
		SimpleUser user = SessionUtils.getUser(request);
		ModelAndView mv=new ModelAndView("institution/paper/modify");
		List<InstitutionDepartment> depts=paperService.getDepartments(user.getInstitutionId());
	    mv.addObject("deptMap",extractDept(depts));
		mv.addObject("op","create");
		mv.addObject("pubs",paperService.getAllPubs());
		mv.addObject("disciplines",paperService.getAllDiscipline());
		mv.addObject("domain",domain);
//		mv.addObject("returnPage",returnPage);
		mv.addObject("titleUser",user);
		addBaseData(mv,user);
		return mv;
	}
	@RequestMapping("delete")
	public ModelAndView delete(@PathVariable("domain") String domain,@RequestParam("page")String page,@RequestParam("paperId[]")int[] paperId,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			LOG.error("delete:home is null["+domain+"]");
//			return new JsonResult("找不到组织机构");
//		}
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			LOG.error("delete:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return new JsonResult("权限不足");
//		}
		LOG.info("delete paper ref["+Arrays.toString(paperId)+"]");
		paperService.deletePaperUser(paperId);
		return new ModelAndView(new RedirectView(request.getContextPath()+"/people/"+domain+"/admin/paper/list/"+page));
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
		SimpleUser user = SessionUtils.getUser(request);
		paper.setCreator(user.getId());
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			LOG.error("submit:home is null["+domain+"]");
//			return new JsonResult("找不到组织机构");
//		}
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			LOG.error("submit:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return new JsonResult("权限不足");
//		}
		paper.setPublicationId(pid);
		if(paper.getCitation().equals("--")){
			paper.setCitation("-1");
		}
		//新建
		if(paper.getId()==0){
			//TODO 这里还需要一个逻辑，即为标记为*号的字段，如果与数据库中的记录匹配，不新建论文，而是简历ref，并更新数据库中数据
			paper.setId(paperService.create(paper,user.getInstitutionId(),uid,order,communicationAuthors,authorStudents,authorTeacher));
			paperService.insertPaperUser(paper.getId(), user.getId());
		}//更新
		else{
			paperService.update(paper,user.getInstitutionId(),uid,order,communicationAuthors,authorStudents,authorTeacher);
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
	@RequestMapping("search/publication")
	@ResponseBody
	public List<InstitutionPublication> searchPub(@PathVariable("domain")String domain,@RequestParam("q")String key,HttpServletRequest request){
		List<InstitutionPublication> pubs=paperService.getPubsByKey(key);
		return pubs;
	}
	@RequestMapping("author/create")
	@ResponseBody
	public JsonResult createAuthor(@PathVariable("domain")String domain,InstitutionAuthor author,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			LOG.error("create.author:home is null["+domain+"]");
//			return new JsonResult("找不到组织机构");
//		}
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
	@RequestMapping("/authors/{paperId}")
	@ResponseBody
	public JsonResult getAuthors(@PathVariable("domain")String domain,@PathVariable("paperId")int paperId,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			LOG.error("update:home is null["+domain+"]");
//			return new JsonResult("not found institution");
//		}
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			LOG.error("update:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return new JsonResult("权限不足");
//		}
//		//不是本机构的论文
//		if(!paperService.isMyPaper(home.getInstitutionId(),paperId)){
//			LOG.error("update:user["+SessionUtils.getUserId(request)+"] wana read paper["+paperId+"]");
//			return new JsonResult("非本机构的论文"); 
//		}
		List<InstitutionAuthor> authors=paperService.getAuthorsByPaperId(paperId);
		JsonResult jr=new JsonResult();
		jr.setData(authors);
		return jr;
	}
//	@RequestMapping("/authors")
//	@ResponseBody
//	public JsonResult getAuthorsByUser(@PathVariable("domain")String domain,HttpServletRequest request){
//		SimpleUser user = SessionUtils.getUser(request);
//		List<InstitutionAuthor> authors=paperService.getAuthorsByUserId(user.getId());
//		JsonResult jr=new JsonResult();
//		jr.setData(authors);
//		return jr;
//	}
	
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
	@RequestMapping("detail/{paperId}")
	@ResponseBody
	public ModelAndView detail(@RequestParam("returnPage") int returnPage,@PathVariable("domain")String domain,@PathVariable("paperId")int paperId,HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution/paper/detail");
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			return null;
//		}
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			return null;
//		} 
		SimpleUser user = SessionUtils.getUser(request);
		InstitutionPaper paper = paperService.getPaperById(paperId);
		ArrayList<Integer> id=new ArrayList<Integer>();
		id.add(paperId);
		Map<Integer,List<InstitutionAuthor>> authorMap=paperService.getListAuthorsMap(id);
		List<InstitutionPaper> papers=paperService.getAllPapers();
		Map<Integer,InstitutionPaper> papersMap= new HashMap<Integer,InstitutionPaper>();
		for (InstitutionPaper p : papers) {
			papersMap.put(p.getId(), p);
		}
		List<InstitutionDepartment> depts=paperService.getDepartments(user.getInstitutionId());
	    mv.addObject("deptMap",extractDept(depts));
		mv.addObject("papersMap",papersMap);
		mv.addObject("paper",paper);
		mv.addObject("returnPage",returnPage);
		mv.addObject("domain",domain);
		mv.addObject("authorMap",authorMap);
		mv.addObject("pubs",paperService.getAllPubs());
		mv.addObject("titleUser",user);
		addBaseData(mv,user);
		return mv;
	}
	@RequestMapping("search/paper")
	public String searchPaper(Model model,@RequestParam("offset") int offset,@RequestParam("size")int size,
			HttpServletRequest request){
		String keyword = CommonUtils.trim(request.getParameter("keyword"));
		SimpleUser user = SessionUtils.getUser(request);
		List<Integer> existPaperIds = paperService.getExistPaperIds(user.getId());
		List<Long> existDsnIds = pService.getExistDsnPaperIds(user.getId());
		List<InstitutionPaper> list = null;
		JSONArray array2 = null;
		if(CommonUtils.isNull(keyword)){
			array2 = dsnSearchService.initQuery(user.getId(), offset, size, existDsnIds);
			list = paperService.getPaperByInit(offset, size, CommonUtils.trim(user.getZhName()));
			keyword = user.getZhName();
			List<InstitutionPaper> listAll= paperService.getPaperByInit(0, 9999, CommonUtils.trim(user.getZhName()));
			array2 = filterRepeat(listAll,array2);
		}else{
			array2 = dsnSearchService.query(user.getId(), keyword, offset, size, existDsnIds);
			list = paperService.getPaperByKeyword(offset, size, CommonUtils.trim(keyword));
			List<InstitutionPaper> listAll=paperService.getPaperByKeyword(0, 9999, CommonUtils.trim(keyword));
			array2 = filterRepeat(listAll,array2);
		}
		for (InstitutionPaper institutionPaper : list) {
			institutionPaper.setPublicationName(paperService.getPubsById(institutionPaper.getPublicationId()).getPubName());
		}
		JSONArray array =new JSONArray();
		array.addAll(list);
//		JSONArray array = filter(gson.toJson(list), request);
		array = filter(array,request);
		array = filterExist(array,existPaperIds);
		array2 = filter2(array2,request);
		
		model.addAttribute("result", array);
		model.addAttribute("result2", array2);
		model.addAttribute("searchKeywords", keyword);
		return "jsontournamenttemplate";
	}
	/**
	 * 过滤掉页面中已经选中的Paper
	 * @param array 查询到的Paper信息
	 * @param request 参数中包含了existPapers，即为需要过滤的Paper ID
	 * @return 已经过滤后的Paper信息
	 */
	private JSONArray filter(JSONArray array, HttpServletRequest request){
		String existPapers = CommonUtils.trim(request.getParameter("existPapers"));
		if(null == existPapers || "".equals(existPapers)){
			return array;
		}
		JSONArray result = new JSONArray();
		String[] temp = existPapers.split(",");
		int size = array.size();
		for(int i=0; i<size; i++){
			InstitutionPaper obj = (InstitutionPaper) array.get(i);
			int paperId = obj.getId();
			boolean exist = false;
			for(String paper: temp){
				if(!CommonUtils.isNull(paper) && paperId == Integer.parseInt(paper)){
					exist = true;
					break;
				}
			}
			if(!exist){
				result.add(obj);
			}
		}
		return result;
	}
	/**
	 * 过滤掉页面中已经选中的commonPaper
	 * @param array 查询到的commonPaper信息
	 * @param request 参数中包含了existCommonPapers，即为需要过滤的Paper ID
	 * @return 已经过滤后的commonPaper信息
	 */
	private JSONArray filter2(JSONArray array, HttpServletRequest request){
		String existCommonPapers = CommonUtils.trim(request.getParameter("existCommonPapers"));
		if(null == existCommonPapers || "".equals(existCommonPapers)){
			return array;
		}
		JSONArray result = new JSONArray();
		String[] temp = existCommonPapers.split(",");
		int size = array.size();
		for(int i=0; i<size; i++){
			JSONObject obj = (JSONObject)array.get(i);
			long paperId = (Long)obj.get(DsnClient.DSN_PAPER_ID);
			boolean exist = false;
			for(String paper: temp){
				if(!CommonUtils.isNull(paper) && paperId == Integer.parseInt(paper)){
					exist = true;
					break;
				}
			}
			if(!exist){
				result.add(obj);
			}
		}
		return result;
	}
	/**
	 * 过滤掉和iap库中重复的论文
	 * @param array
	 * @param request
	 * @return
	 */
	private JSONArray filterRepeat(List<InstitutionPaper> list,JSONArray array2){
		JSONArray result = new JSONArray();
		int size = array2.size();
		for(int i=0; i<size; i++){
			JSONObject obj = (JSONObject)array2.get(i);
			String title = (String)obj.get(DsnClient.TITLE);
			boolean exist = false;
			for(int j=0;j<list.size();j++){
				InstitutionPaper paper = (InstitutionPaper) list.get(j);
				if(!CommonUtils.isNull(paper) && title.equals(paper.getTitle())){
					exist = true;
					break;
				}
			}
			if(!exist){
				result.add(obj);
			}
		}
		return result;
	}
	/**
	 * 过滤掉页面中已经添加的Paper
	 * @param result 查询到的Paper信息
	 * @param existIds 查询到的已经存在的id
	 * @return 已经过滤后的Paper信息
	 */
	private JSONArray filterExist(JSONArray result, List<Integer> existIds) {
		if (null == result || result.size() <= 0 || null == existIds || existIds.isEmpty()) {
			return result;
		}
		int size = result.size();
		JSONArray array = new JSONArray();
		for (int i = 0; i < size; i++) {
			InstitutionPaper obj = (InstitutionPaper) result.get(i);
			int paperId = obj.getId();
			if (!existIds.contains(paperId)) {
				array.add(obj);
			}
		}
		return array;
	}
	@RequestMapping("search")
	@ResponseBody
	public ModelAndView search(@PathVariable("domain")String domain,HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution/paper/searchAdd");
		SimpleUser user = SessionUtils.getUser(request);
//		List<InstitutionAuthor> authors=paperService.getAuthorsByUserId(user.getId());
//		List<InstitutionPaper> list = paperService.getPaperByInit(offset, size, CommonUtils.trim(user.getZhName()));
//		mv.addObject("authors",authors);
		mv.addObject("domain",domain);
		mv.addObject("titleUser",user);
		return mv;
	}
	@RequestMapping("search/submit")
	@ResponseBody
	public ModelAndView save(@PathVariable("domain")String domain,HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution/paper/searchAdd");
		SimpleUser user = SessionUtils.getUser(request);
		String existPapers = CommonUtils.trim(request.getParameter("paperIds"));
		String existCommonPapers = CommonUtils.trim(request.getParameter("commonPaperIds"));
		JSONArray array = dsnSearchService.getDsnPapers(existCommonPapers);
		List<Paper> papers = PaperJSONHelper.jsonArray2PaperList(SessionUtils.getUserId(request), array);
		paperService.movePaper(papers, user.getId());
		if(null != existPapers && !"".equals(existPapers)){
			String[] paperId = existPapers.split(",");
//			String[] authorId = existAuthors.split(",");
//			List<InstitutionPaper> list = paperService.getPaperByInit(offset, size, CommonUtils.trim(user.getZhName()));
//			paperService.insertAuthorUser(user.getId(), user.getInstitutionId(), authorId, -1);
			paperService.insertPaper(paperId, user.getId());
		}
//		if(null == existAuthors || "".equals(existAuthors)){
//			return null;
//		}
		mv.addObject("domain",domain);
		mv.addObject("titleUser",user);
		return mv;
	}
	@RequestMapping("propelling")
	@ResponseBody
	public ModelAndView peopelling(@PathVariable("domain")String domain,HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution/paper/propelling");
		SimpleUser user = SessionUtils.getUser(request);
		List<InstitutionAuthor> authors=paperService.getAuthorsByCstnetId(user.getEmail());
//		List<InstitutionPaper> list = paperService.getPaperByInit(offset, size, CommonUtils.trim(user.getZhName()));
		mv.addObject("authors",authors);
		Gson gson = new Gson();
		mv.addObject("json",gson.toJson(authors));
		mv.addObject("domain",domain);
		mv.addObject("titleUser",user);
		return mv;
	}
	@RequestMapping("propelling/paper")
	public String propellingPaper(Model model,@RequestParam("offset") int offset,@RequestParam("size")int size,
			HttpServletRequest request){
		String keyword = CommonUtils.trim(request.getParameter("keyword"));
		SimpleUser user = SessionUtils.getUser(request);
		List<Integer> existPaperIds = paperService.getExistPaperIds(user.getId());
		List<InstitutionAuthor> authors=paperService.getAuthorsByCstnetId(user.getEmail());
		List<InstitutionPaper> list = null;
		if(CommonUtils.isNull(keyword)){
			if(authors.size()!=0){
				list = paperService.getPaperByKeywordInit(offset, size, authors);
			}
			keyword = "";
			for (InstitutionAuthor author : authors) {
				keyword+=author.getAuthorName()+",";
			}
		}else{
			list = paperService.getPaperByKeyword(offset, size, CommonUtils.trim(keyword));
		}
//		array = filter(array, request);
		JSONArray array =new JSONArray();
		if(list!=null){
			for (InstitutionPaper institutionPaper : list) {
				institutionPaper.setPublicationName(paperService.getPubsById(institutionPaper.getPublicationId()).getPubName());
			}
			array.addAll(list);
		}
		array = filter(array,request);
		array = filterExist(array,existPaperIds);
		model.addAttribute("result", array);
		model.addAttribute("searchKeywords", keyword);
		return "jsontournamenttemplate";
	}
	@RequestMapping("propelling/submit")
	@ResponseBody
	public ModelAndView propellingSave(@PathVariable("domain")String domain,HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution/paper/propelling");
		SimpleUser user = SessionUtils.getUser(request);
		String existPapers = request.getParameter("paperIds");
		String existAuthors = request.getParameter("authorIds");
		if(null != existPapers && !"".equals(existPapers)){
			String[] paperId = existPapers.split(",");
			paperService.insertPaper(paperId, user.getId());
		}
		if(null != existAuthors && !"".equals(existAuthors)){
			String[] authorId = existAuthors.split(",");
			paperService.insertAuthorUser(user.getEmail(), user.getInstitutionId(), authorId, -1);
		}
		List<InstitutionAuthor> authors=paperService.getAuthorsByCstnetId(user.getEmail());
		Gson gson = new Gson();
		mv.addObject("json",gson.toJson(authors));
		mv.addObject("authors",authors);
		mv.addObject("domain",domain);
		mv.addObject("titleUser",user);
		return mv;
	}
	@RequestMapping("propelling/delete")
	@ResponseBody
	public ModelAndView propellingDel(@RequestParam("authorId")int authorId,@PathVariable("domain")String domain,HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution/paper/propelling");
		SimpleUser user = SessionUtils.getUser(request);
		paperService.updateAuthorPropelling(authorId);
		List<InstitutionAuthor> authors=paperService.getAuthorsByCstnetId(user.getEmail());
		mv.addObject("authors",authors);
		Gson gson = new Gson();
		mv.addObject("json",gson.toJson(authors));
		mv.addObject("domain",domain);
		mv.addObject("titleUser",user);
		return mv;
	}
	/**
	 * 迁移论文库
	 * @param request
	 * @return
	 */
	@RequestMapping("moving")
	@ResponseBody
	public JsonResult move(@PathVariable("domain")String domain,HttpServletRequest request){
		SimpleUser user = SessionUtils.getUser(request);
		List<Paper> papers = pService.getPapers(user.getId(), 0, 0);
		paperService.movePaper(papers, user.getId());
		user.setIsMove(1);
		userService.updateSimpleUserStatusByUid(user);
		user.setInstitutionId(1799);
		return new JsonResult();
	}
	
	@RequestMapping("getPaperByTitle")
	@ResponseBody
	public JsonResult getPaperByTitle(@PathVariable("domain")String domain,
			@RequestParam("title")String title,
			HttpServletRequest request){
		List<InstitutionPaper> paper = paperService.getPaperByTitle(title.trim());
		for (InstitutionPaper p : paper) {
			if(p.getCreator()!=0){
				p.setCreatorName(userService.getSimpleUser(p.getCreator()).getZhName());
			}
			if(p.getCreatorName()==null){
				p.setCreatorName("");
			}
		}
		
		JsonResult jr=new JsonResult();
		if(paper.size()<1){
			jr.setSuccess(false);
		}else{
			jr.setData(paper);
			jr.setSuccess(true);
		}
		return jr;
	}
	@RequestMapping("getPaperByDoi")
	@ResponseBody
	public JsonResult getPaperByDoi(@PathVariable("domain")String domain,
			@RequestParam("doi")String doi,
			HttpServletRequest request){
		int id = paperService.getPaperByDoi(doi);
		
		JsonResult jr=new JsonResult();
		if(id>0){
			jr.setSuccess(true);
		}else{
			jr.setSuccess(false);
		}
		return jr;
	}

}
