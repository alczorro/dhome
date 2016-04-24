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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.dsn.PaperJSONHelper;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.PaperRender;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.common.util.UrlUtil;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.people.MenuItem;
import net.duckling.dhome.domain.people.Paper;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IDsnSearchService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendPaperService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IMenuItemService;
import net.duckling.dhome.service.IPaperService;
import net.duckling.dhome.service.IUserService;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
/**
 * 管理论文（论文列表页面）
 * @author Yangxp
 * @date 2012-08-01
 */
@Controller
@NPermission(authenticated = true)
@RequestMapping("/people/{domain}/admin/commonPaper")
public class CommonPaperController {

	private static final String PAPER_ID = "paperId";
	@Autowired
	private IPaperService paperService;
	@Autowired
	private IHomeService homeService;
	@Autowired
	private IMenuItemService menuItemService;
	@Autowired
	private IDsnSearchService dsnService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IInstitutionBackendPaperService backendPaperService;
	@Autowired
	private IInstitutionBackendService backEndService;
	/**
	 * 编辑论文页面显示
	 * @param request
	 * @return
	 */
	@RequestMapping
	public ModelAndView display(HttpServletRequest request) {
		SimpleUser curUser = SessionUtils.getUser(request);
		List<Paper> papers = paperService.getPapers(curUser.getId(), 0, 0);
		String domain = homeService.getDomain(curUser.getId());
		String url=UrlUtil.getPaperURL(domain);
		MenuItem item=menuItemService.getMenuItemByUrl(url);
		String status="";
		if (item != null) {
			switch (item.getStatus()) {
			case MenuItem.MENU_ITEM_STATUS_HIDING:
				status = "hide";
				break;
			case MenuItem.MENU_ITEM_STATUS_SHOWING:
				status = "show";
				break;
			case MenuItem.MENU_ITEM_STATUS_REMOVED:
				status = "delete";
				break;
			default:
				break;
			}
		}
		ModelAndView mv = new ModelAndView("institution/paper/commonList");
		String flag="";
		if(curUser.getInstitutionId()==null||curUser.getInstitutionId()==0){
			flag="noIAP";
		}else{
			if(backEndService.isMember(curUser.getInstitutionId(),curUser.getEmail())){
				flag="IAP";
				//没有迁移的用户点击学术成果
//				return new ModelAndView(new RedirectView("/people/" + domain + "/admin/paper/list/1"));
				
			}else{
				flag="noIAP";
			}
		}
		//判断该用户是否做的迁移
		int isMove=userService.getSimpleUser(curUser.getId()).getIsMove();
		mv.addObject("isMove", isMove);
		mv.addObject("flag", flag);
		mv.addObject("domain", domain);
		mv.addObject("name", curUser.getZhName());
		mv.addObject("titleUser",homeService.getSimpleUserByDomain(domain));
		mv.addObject("papers", PaperRender.format(papers, dsnService.getUserNameString(curUser.getId())));
		mv.addObject("IAPPaper",backendPaperService.getPapersByUID(curUser.getId()));
		mv.addObject("banner","1");
		mv.addObject("status",status);
		mv.addObject("active",request.getRequestURI().replace(request.getContextPath(), ""));
		List<Integer> paperId=CommonUtils.extractSthField(backendPaperService.getPapersByUID(curUser.getId()),"id");
		if(!CommonUtils.isNull(paperId)){
			Map<Integer,List<InstitutionAuthor>> authorMap=backendPaperService.getListAuthorsMap(paperId);
			mv.addObject("authorMap",authorMap);
		}
		return mv;
	}
	/**
	 * 显示学术成果页面
	 * @param request
	 * @param domain
	 * @return
	 */
	@RequestMapping(params="func=achievement")
	public ModelAndView paperNew(HttpServletRequest request){
		SimpleUser curUser = SessionUtils.getUser(request);
		List<Paper> papers = paperService.getPapers(curUser.getId(), 0, 0);
		String domain = homeService.getDomain(curUser.getId());
		String url=UrlUtil.getPaperURL(domain);
		MenuItem item=menuItemService.getMenuItemByUrl(url);
		String status="";
		if (item != null) {
			switch (item.getStatus()) {
			case MenuItem.MENU_ITEM_STATUS_HIDING:
				status = "hide";
				break;
			case MenuItem.MENU_ITEM_STATUS_SHOWING:
				status = "show";
				break;
			case MenuItem.MENU_ITEM_STATUS_REMOVED:
				status = "delete";
				break;
			default:
				break;
			}
		}
		ModelAndView mv = new ModelAndView("institution/paper/commonList");
		String flag="";
		if(curUser.getInstitutionId()==null||curUser.getInstitutionId()==0){
			flag="noIAP";
		}else{
			if(backEndService.isMember(curUser.getInstitutionId(),curUser.getEmail())){
				flag="IAP";
				//迁移的用户点击学术成果
				if(userService.getSimpleUser(curUser.getId()).getIsMove()==1){
					return new ModelAndView(new RedirectView("/people/" + domain + "/admin/paper/list/1"));
				}
				
			}else{
				flag="noIAP";
			}
		}
		//判断该用户是否做的迁移
		int isMove=userService.getSimpleUser(curUser.getId()).getIsMove();
		mv.addObject("isMove", isMove);
		mv.addObject("flag", flag);
		mv.addObject("domain", domain);
		mv.addObject("name", curUser.getZhName());
		mv.addObject("titleUser",homeService.getSimpleUserByDomain(domain));
		mv.addObject("papers", PaperRender.format(papers, dsnService.getUserNameString(curUser.getId())));
		mv.addObject("IAPPaper",backendPaperService.getPapersByUID(curUser.getId()));
		mv.addObject("banner","1");
		mv.addObject("status",status);
		mv.addObject("active",request.getRequestURI().replace(request.getContextPath(), ""));
		List<Integer> paperId=CommonUtils.extractSthField(backendPaperService.getPapersByUID(curUser.getId()),"id");
		if(!CommonUtils.isNull(paperId)){
			Map<Integer,List<InstitutionAuthor>> authorMap=backendPaperService.getListAuthorsMap(paperId);
			mv.addObject("authorMap",authorMap);
		}
		return mv;
	}
	/**
	 * 获取用户的所有论文信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/getPapers.json")
	@ResponseBody
	public  List<Paper> getPaperJson(HttpServletRequest request, HttpServletResponse response) {
		return  paperService.getPapers(SessionUtils.getUserId(request), 0, 0);
	}
	/**
	 * 删除论文
	 * @param paperId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/deletePaper.json")
	public String deletePaper(HttpServletRequest request,@RequestParam(PAPER_ID) int paperId, Model model) {
		JSONObject obj = new JSONObject();
		addStatusAndPaperId(obj, paperId, paperService.deletePaper(paperId));
		userService.updateSimpleUserLastEditTimeByUid(SessionUtils.getUserId(request));
		model.addAttribute("result", obj);
		return "jsontournamenttemplate";
	}
	/**
	 * 论文排序
	 * @param request
	 * @param paperId
	 * @param sequence
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/reorder.json")
	public String reorder(HttpServletRequest request, @RequestParam(PAPER_ID) int paperId,
			@RequestParam("sequence") int sequence, Model model) {
		SimpleUser su = SessionUtils.getUser(request);
		JSONObject obj = new JSONObject();
		addStatusAndPaperId(obj, paperId, paperService.updateSequence(su.getId(), paperId, sequence));
		obj.put("sequence", sequence);
		model.addAttribute("result", obj);
		userService.updateSimpleUserLastEditTimeByUid(su.getId());
		return "jsontournamenttemplate";
	}
	/** 
	 * 更改发布状态
	 * @param request
	 * @return
	 */
	@RequestMapping("/changeStatus")
	public ModelAndView changeStatus(HttpServletRequest request) {
		String url = request.getRequestURI().replace(request.getContextPath(), "");
		url = url.substring(0, url.lastIndexOf('/'));
		MenuItem item = menuItemService.getMenuItemByUrl(getGuestPageUrl(url));
		if (MenuItem.MENU_ITEM_STATUS_HIDING == item.getStatus()) {
			item.setStatus(MenuItem.MENU_ITEM_STATUS_SHOWING);
		} else if (MenuItem.MENU_ITEM_STATUS_SHOWING == item.getStatus()) {
			item.setStatus(MenuItem.MENU_ITEM_STATUS_HIDING);
		}
		menuItemService.updateMenuItem(item);
		return new ModelAndView(new RedirectView(url));
	}
	/**
	 * 上传论文后返回论文ID和状态
	 * @param paperId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/afterUpload.json")
	public String afterUpload(@RequestParam(PAPER_ID) int paperId, Model model) {
		JSONObject obj = new JSONObject();
		addStatusAndPaperId(obj, paperId, true);
		model.addAttribute("result", obj);
		return "jsontournamenttemplate";
	}

	private void addStatusAndPaperId(JSONObject obj, int paperId, boolean status) {
		obj.put("status", status);
		obj.put(PAPER_ID, paperId);
	}
	/**
	 * 跳转到游客浏览页面
	 * 
	 * @param str
	 *            /dhome/people/ceshijun/admin/p/test
	 * @return /dhome/people/ceshijun/test.html
	 * */
	public String getGuestPageUrl(String str) {
		String temp = str.replace("/admin", "");
		if(temp.endsWith("paper")){
			temp+=".dhome";
		}else if (!temp.contains(".html")) {
			temp += ".html";
		}
		
		return temp;
	}
}
