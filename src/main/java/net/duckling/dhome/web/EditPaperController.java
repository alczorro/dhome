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
package net.duckling.dhome.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.common.auth.sso.Constants;
import net.duckling.dhome.common.clb.FileNameSafeUtil;
import net.duckling.dhome.common.dsn.PaperJSONHelper;
import net.duckling.dhome.common.exception.BibResolveFailedException;
import net.duckling.dhome.common.util.BibReader;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.JSONHelper;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.people.Paper;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IDsnSearchService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IPaperService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.impl.ClbFileService;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
/**
 * 编辑论文
 * @author Yangxp
 *
 */
@Controller
@RequestMapping("/people/{domain}/admin/paper/edit")
public class EditPaperController {

	private static final Logger LOG = Logger.getLogger(EditPaperController.class);
	private static final String PAPERID = "paperId";
	private static final String DOMAIN="domain";
	private static final String STATUS="status";

	@Autowired
	private IPaperService paperService;
	@Autowired
	private IDsnSearchService dsnSearchService;
	@Autowired
	private ClbFileService fileService;
	@Autowired
	private IHomeService homeService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IInstitutionBackendService backEndService;
	
	private void addBaseData(ModelAndView mv,SimpleUser curUser){
		String flag="";
		if(curUser.getInstitutionId()==null||curUser.getInstitutionId()==0){
			flag="noIAP";
		}else{
			if(backEndService.isMember(curUser.getInstitutionId(),curUser.getEmail())){
				flag="IAP";
			}else{
				flag="noIAP";
			}
		}
		mv.addObject("flag", flag);
	}
	
	/**
	 * 显示添加论文/编辑论文的页面
	 * @param request
	 * @return
	 */
	@RequestMapping
	public ModelAndView display(HttpServletRequest request, @PathVariable(DOMAIN)String domain) {
		return showPaperSearch(request, domain);
	}
	/**
	 * 显示添加论文/编辑论文的页面
	 * @param request
	 * @param domain
	 * @return
	 */
	@RequestMapping(params="func=paperNew")
	public ModelAndView paperNew(HttpServletRequest request, @PathVariable(DOMAIN)String domain){
		ModelAndView mv = new ModelAndView("institution/paper/addNewPaper");
		String paperid = request.getParameter(CommonUtils.trim(PAPERID));
		String needFocus = request.getParameter(CommonUtils.trim("needFocus"));
		if (null != paperid && !"".equals(paperid)) {
			Paper paper = paperService.getPaper(Integer.parseInt(paperid));
			mv.addObject("paper", PaperJSONHelper.paper2JSONObject(paper));	
			mv.addObject("needFocus", Boolean.valueOf(needFocus));
		}
		mv.addObject("active",request.getRequestURI().replace(request.getContextPath(), ""));
		mv.addObject("titleUser",homeService.getSimpleUserByDomain(domain));
		SimpleUser user = SessionUtils.getUser(request);
		addBaseData(mv,user);
		return mv;
	}
	/**
	 * 保存添加/编辑论文的结果
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "func=save")
	public ModelAndView save(HttpServletRequest request) {
		Paper paper = buildPaperFromRequest(request);
		if (paper.getId() <= 0) {
			paperService.create(paper);
		} else {
			paperService.updateById(paper);
		}
		userService.updateSimpleUserLastEditTimeByUid(SessionUtils.getUserId(request));
		return redirect2PaperHome(request);
	}
	/**
	 * 显示搜索论文页面
	 * @return
	 */
	@RequestMapping(params = "func=paperSearch")
	public ModelAndView showPaperSearch(HttpServletRequest request, @PathVariable(DOMAIN)String domain) {
		ModelAndView mv = new ModelAndView("institution/paper/addSearchPaper");
		mv.addObject("active",request.getRequestURI().replace(request.getContextPath(), ""));
		SimpleUser user = SessionUtils.getUser(request);
		mv.addObject("titleUser",homeService.getSimpleUserByDomain(domain));
		
		addBaseData(mv,user);
		return mv;
	}
	/**
	 * 保存搜索论文结果
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "func=saveSearchPaper")
	public ModelAndView saveSearchPaper(HttpServletRequest request) {
		String paids = CommonUtils.trim(request.getParameter("paperIds"));
		JSONArray array = dsnSearchService.getDsnPapers(paids);
		List<Paper> papers = PaperJSONHelper.jsonArray2PaperList(SessionUtils.getUserId(request), array);
		paperService.batchCreate(papers);
		userService.updateSimpleUserLastEditTimeByUid(SessionUtils.getUserId(request));
		return redirect2PaperHome(request);
	}
	/**
	 * 显示导入bib文件页面
	 * @return
	 */
	@RequestMapping(params = "func=importBibtex")
	public ModelAndView showImportBibtex(HttpServletRequest request, @PathVariable(DOMAIN)String domain) {
		ModelAndView mv = new ModelAndView("institution/paper/addBibtexPaper");
		mv.addObject("active",request.getRequestURI().replace(request.getContextPath(), ""));
		mv.addObject("titleUser",homeService.getSimpleUserByDomain(domain));
		SimpleUser user = SessionUtils.getUser(request);
		addBaseData(mv,user);
		return mv;
	}
	/**
	 * 上传bib文件，非IE浏览器
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(params = "func=uploadBibtex", headers = { "X-File-Name" })
	public void uploadBibtex(HttpServletRequest request, HttpServletResponse response) throws IOException {
		InputStream ins = request.getInputStream();
		SimpleUser su = (SimpleUser) request.getSession().getAttribute(Constants.CURRENT_USER);
		BibReader br = new BibReader(ins, su.getId());
		try{
			userService.updateSimpleUserLastEditTimeByUid(SessionUtils.getUserId(request));
			List<Paper> papers = br.analyze(request.getContentLength());
			response.setStatus(HttpStatus.SC_OK);
			writeJSON2Response(response, papers);
		}catch(BibResolveFailedException e){
			response.setStatus(HttpStatus.SC_OK);
			JSONObject obj = new JSONObject();
			obj.put(STATUS, false);
			obj.put("errMessage", e.toString());
			JSONHelper.writeJSONObject(response, obj);
		}
	}
	/**
	 * 上传bib文件，IE浏览器
	 * @param uplFile
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST, params = "func=uploadBibtex")
	public void uploadBibtex(@RequestParam("qqfile") MultipartFile uplFile, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		InputStream ins = uplFile.getInputStream();
		SimpleUser su = (SimpleUser) request.getSession().getAttribute(Constants.CURRENT_USER);
		BibReader br = new BibReader(ins, su.getId());
		try{
			userService.updateSimpleUserLastEditTimeByUid(SessionUtils.getUserId(request));
			List<Paper> papers = br.analyze((int)uplFile.getSize());
			response.setStatus(HttpServletResponse.SC_OK);
			writeJSON2Response(response, papers);
		}catch(BibResolveFailedException e){
			response.setStatus(HttpStatus.SC_OK);
			JSONObject obj = new JSONObject();
			obj.put(STATUS, false);
			obj.put("errMessage", e.toString());
			JSONHelper.writeJSONObject(response, obj);
		}
	}
	/**
	 * 保存bib文件选中结果
	 * @param request
	 * @return
	 */
	
	@RequestMapping(params = "func=saveBibPaper")
	public ModelAndView saveBibPaper(HttpServletRequest request) {
		String json =CommonUtils.trim( request.getParameter("papers"));
		if (null != json && !"".equals(json)) {
			JSONArray array = parseJSONArray(json);
			userService.updateSimpleUserLastEditTimeByUid(SessionUtils.getUserId(request));
			List<Paper> papers = PaperJSONHelper.jsonArray2PaperList(-1, array);
			paperService.batchCreate(papers);
		}
		return redirect2PaperHome(request);
	}
	private boolean validatePrefix(String fileName){
		String lower=fileName.toLowerCase();
		return lower.endsWith(".pdf")||lower.endsWith(".doc")||lower.endsWith(".docx");
	}
	/**
	 * 上传论文全文，适合非IE浏览器
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(method= RequestMethod.POST ,params = "func=uploadFulltext", headers = {"X-File-Name"})
	@ResponseBody
	public  String uploadFulltext(HttpServletRequest request,@RequestHeader("X-File-Name") String fileName) throws IOException{
		String paperId = CommonUtils.trim(request.getParameter(PAPERID));
		InputStream ins = request.getInputStream();
//		String fileName = getFileNameFromHeader(request);
		if(!validatePrefix(fileName)){
			JSONObject obj = new JSONObject();
			obj.put(STATUS, false);
			obj.put("desc", "File Type Error");
			return obj.toJSONString();
		}
		String fileName1=FileNameSafeUtil.makeSafe(fileName);
		int clbId = fileService.createFile(URLDecoder.decode(fileName1, "UTF-8"),request.getContentLength(), ins);
		updateLocalUrl(clbId, paperId);
		userService.updateSimpleUserLastEditTimeByUid(SessionUtils.getUserId(request));
		JSONObject obj = new JSONObject();
		obj.put(STATUS, clbId>0);
		obj.put("clbId", clbId);
		obj.put(PAPERID, paperId);
		return obj.toJSONString();
	}
	/**
	 * 上传论文全文，适合IE浏览器
	 * @param mulFile
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST, params = "func=uploadFulltext")
	@ResponseBody
	public  String uploadFulltextForIE(@RequestParam("qqfile") MultipartFile mulFile, HttpServletRequest request) throws IOException{
		String paperId = CommonUtils.trim(request.getParameter(PAPERID));
		InputStream ins = mulFile.getInputStream();
		String fileName = mulFile.getOriginalFilename();
		if(!validatePrefix(fileName)){
			JSONObject obj = new JSONObject();
			obj.put(STATUS, false);
			obj.put("desc", "File Type Error");
			return obj.toJSONString();
		}
		String fileName1=FileNameSafeUtil.makeSafe(fileName);
		int clbId = fileService.createFile(URLDecoder.decode(fileName1, "UTF-8"), ins);
		updateLocalUrl(clbId, paperId);
		userService.updateSimpleUserLastEditTimeByUid(SessionUtils.getUserId(request));
		JSONObject obj = new JSONObject();
		obj.put(STATUS, (clbId>0));
		obj.put("clbId", clbId);
		obj.put(PAPERID, paperId);
		return obj.toJSONString();
	}
	
	private void updateLocalUrl(int clbId, String paperId){
		if(clbId<=0 || null == paperId || "".equals(paperId)){
			return;
		}
		String url = "/system/download/"+clbId;
		Paper paper = paperService.getPaper(Integer.valueOf(paperId));
		paper.setLocalFulltextURL(url);
		paper.setClbId(clbId);
		paperService.updateById(paper);
	}

	private Paper buildPaperFromRequest(HttpServletRequest request) {
		SimpleUser su = (SimpleUser) request.getSession().getAttribute(Constants.CURRENT_USER);
		String idStr =CommonUtils.trim( request.getParameter(PAPERID));
		int id = (null == idStr || "".equals(idStr)) ? 0 : Integer.parseInt(idStr);
		String title = CommonUtils.trim(request.getParameter("title"));
		String authors = CommonUtils.trim(request.getParameter("authors"));
		String source = CommonUtils.trim(request.getParameter("source"));
		String volumeIssue = CommonUtils.trim(request.getParameter("volumeIssue"));
		String publishedTime = CommonUtils.trim(request.getParameter("publishedTime"));
		String paperURL =CommonUtils.trim( request.getParameter("paperURL"));
		String clbIdStr =CommonUtils.trim( request.getParameter("clbId"));
		String summary = CommonUtils.trim(request.getParameter("summary"));
		String pages = CommonUtils.trim(request.getParameter("pages"));
		summary = (null!=summary && !"".equals(summary))?summary.trim():"";
		int clbId = (null == clbIdStr || "".equals(clbIdStr))?0:Integer.parseInt(clbIdStr);
		String localFulltextURL = (clbId>0)?"/system/download/"+clbId:null;
		Paper paper = Paper.build(su.getId(), title, authors, source, volumeIssue, publishedTime, 0 + "", summary, "", "",
				localFulltextURL, paperURL, clbId, 0, 0, pages);
		paper.setId(id);
		return paper;
	}

	private ModelAndView redirect2PaperHome(HttpServletRequest request) {
		String domain = (String) request.getSession().getAttribute(Constants.CURRENT_USER_DOMAIN);
		return new ModelAndView(new RedirectView("/people/" + domain + "/admin/commonPaper"));
	}

	private void writeJSON2Response(HttpServletResponse response, List<Paper> papers) {
		JSONObject obj = new JSONObject();
		obj.put(STATUS, true);
		if (null == papers) {
			obj.put("result", new JSONObject());
		} else {
			JSONArray array = new JSONArray();
			for (Paper paper : papers) {
				array.add(PaperJSONHelper.paper2JSONObject(paper));
			}
			obj.put("result", array);
		}
		JSONHelper.writeJSONObject(response, obj);
	}

	private JSONArray parseJSONArray(String json) {
		JSONParser parser = new JSONParser();
		JSONArray array = new JSONArray();
		try {
			array = (JSONArray) parser.parse(json);
		} catch (ParseException e) {
			LOG.error("解析JSON错误！", e);
		}
		return array;
	}
	
	private String getFileNameFromHeader(HttpServletRequest request) {
		String filename = request.getHeader("X-File-Name");
		try {
			filename = URLDecoder.decode(filename, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOG.warn("Your system doesn't support utf-8 character encode. so sucks.");
		}
		return filename;
	}
	
}
