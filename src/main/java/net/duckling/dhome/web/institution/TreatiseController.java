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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.dhome.domain.institution.InstitutionOption;
import net.duckling.dhome.domain.institution.InstitutionOptionVal;
import net.duckling.dhome.domain.institution.InstitutionTreatise;
import net.duckling.dhome.domain.institution.InstitutionTreatiseAuthor;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.JsonResult;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionBackendTreatiseService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionOptionValService;
import net.duckling.dhome.service.IInstitutionPeopleService;
import net.duckling.dhome.service.IInstitutionVmtService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.impl.VMTMessageHandlerImpl;
import net.duckling.vmt.api.impl.GroupService;
import net.duckling.vmt.api.impl.OrgService;
import net.duckling.vmt.api.impl.UserService;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
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

@Controller
@NPermission(authenticated = true,member = "iap")
@RequestMapping("/people/{domain}/admin/treatise")
public class TreatiseController {
	private static final Logger LOG=Logger.getLogger(TreatiseController.class);
	@Autowired
	private IHomeService userHomeService;
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private UserService vmtUserService;
	
	@Autowired
	private IInstitutionHomeService homeService;
	@Autowired
	private IInstitutionVmtService vmtService;
	
	@Autowired
	private IInstitutionBackendService backendService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IInstitutionBackendTreatiseService treatiseService;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private VMTMessageHandlerImpl vmtMessageHandleImpl;
	
	@Autowired
	private IInstitutionPeopleService peopleService;
	
	@Autowired
	private IInstitutionOptionValService optionValService;
	
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
	public ModelAndView list(
			@PathVariable("domain")String domain,
			@PathVariable("page")int page,
			SearchInstitutionCondition condition,
			HttpServletRequest request){
		
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null||page<0){
//			LOG.error("list:home is null["+domain+"]  or page < 0");
//			return null;
//		}
		
//		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
//			LOG.error("list:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return null;
//		}
		SimpleUser user = SessionUtils.getUser(request);
		ModelAndView mv=new ModelAndView("institution/treatise/list");
		PageResult<InstitutionTreatise> result=treatiseService.getTreatiseByUser(user.getId(), page, condition);
		mv.addObject("page",result);
		
		List<Integer> treatiseIds=CommonUtils.extractSthField(result.getDatas(),"id");
		if(!CommonUtils.isNull(treatiseIds)){
			Map<Integer, List<InstitutionTreatiseAuthor>> authorMap=treatiseService.getAuthorsMap(treatiseIds);
			mv.addObject("authorMap",authorMap);
		}
		mv.addObject("condition",condition);

//		List<SimpleUser> users=userService.getUserByEmails(getEmail(result.getDatas()));
//		mv.addObject("userMap",extract(users));
//		
//		List<Home> homes=userHomeService.getDomainsByUids(getUids(users));
//		mv.addObject("homeMap",extractHome(homes));
//		
//		List<InstitutionDepartment> depts=backEndService.getVmtDepartment(getDeptId(result.getDatas()));
//		mv.addObject("deptMap",extractDept(depts));
		mv.addObject("domain",domain);
		mv.addObject("titleUser",user);
		addBaseData(mv,user);
		return mv;
	}
	private void addBaseData(ModelAndView mv,SimpleUser user){
		//出版社
		Map<Integer,InstitutionOptionVal> publishers= optionValService.getMapByOptionId(InstitutionOption.TREATISE_PUBLISHER, user.getInstitutionId());
		mv.addObject("publishers",publishers);
	}
	
	
	/**
	 * 添加论著
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("add")
	public ModelAndView add(@PathVariable("domain")String domain,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			LOG.error("add:home is null["+domain+"]");
//			return null;
//		}
//		
//		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
//			LOG.error("add:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return null;
//		}
		SimpleUser user = SessionUtils.getUser(request);
	    ModelAndView mv = new ModelAndView("institution/treatise/add");
	    List<InstitutionDepartment> depts=treatiseService.getDepartments(user.getInstitutionId());
	    mv.addObject("deptMap",extractDept(depts));
	    mv.addObject("op", "add");
	    mv.addObject("domain",domain);
	    mv.addObject("titleUser",user);
	    addBaseData(mv,user);
	    return mv;
	}
	
	@RequestMapping("update/{treatiseId}")
	public ModelAndView update(
			@PathVariable("domain") String domain,
			@PathVariable("treatiseId") int treatiseId,
			@RequestParam("returnPage") int returnPage,
			HttpServletRequest request){
//		InstitutionHome home = homeService.getInstitutionByDomain(domain);
//		if(home == null){
//			LOG.error("update : home is null["+domain+"]");
//		}
//		
//		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
//			LOG.error("update:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return null;
//		}
		SimpleUser user = SessionUtils.getUser(request);
		InstitutionTreatise treatise = treatiseService.getTreatise(treatiseId);
		if(treatise == null){
			LOG.error("update : treatise["+treatiseId+"] is not found");
			return null;
		}
		
	    ModelAndView mv = new ModelAndView("institution/treatise/add");
	    List<InstitutionDepartment> depts=treatiseService.getDepartments(user.getInstitutionId());
	    mv.addObject("deptMap",extractDept(depts));
	    mv.addObject("op", "update");
	    mv.addObject("treatise", treatise);
	    mv.addObject("returnPage",returnPage);
	    mv.addObject("titleUser",user);
	    addBaseData(mv,user);
		return mv;
	}
	
	
	@RequestMapping("detail/{treatiseId}")
	public ModelAndView detail(
			@PathVariable("domain") String domain,
			@PathVariable("treatiseId") int treatiseId,
			@RequestParam("returnPage") int returnPage,
			HttpServletRequest request){
		
//		InstitutionHome home = homeService.getInstitutionByDomain(domain);
//		if(home == null){
//			LOG.error("detail : home is null["+domain+"]");
//		}
//		
//		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
//			LOG.error("detail["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return null;
//		}
		SimpleUser user = SessionUtils.getUser(request);
		InstitutionTreatise treatise = treatiseService.getTreatise(treatiseId);
		if(treatise == null){
			LOG.error("detail : treatise["+treatiseId+"] is not found");
			return null;
		}
		
		 ModelAndView mv = new ModelAndView("institution/treatise/detail");
		  List<InstitutionDepartment> depts=treatiseService.getDepartments(user.getInstitutionId());
		    mv.addObject("deptMap",extractDept(depts));
		 mv.addObject("treatise", treatise);
		 mv.addObject("returnPage",returnPage);
		 mv.addObject("titleUser",user);
		 addBaseData(mv,user);
		 return mv;
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public ModelAndView delete(@PathVariable("domain") String domain,@RequestParam("page")String page,@RequestParam("treatiseId[]")int[] treatiseId,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			LOG.error("delete:home is null["+domain+"]");
//			return new JsonResult("找不到组织机构");
//		}
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			LOG.error("delete:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return new JsonResult("权限不足");
//		}
//		if(CommonUtils.isNull(treatiseId)){
//			return new JsonResult("参数缺失");
//		}
		LOG.info("delete treatise ref["+Arrays.toString(treatiseId)+"]");
		treatiseService.deleteTreatiseUser(treatiseId);
		return new ModelAndView(new RedirectView(request.getContextPath()+"/people/"+domain+"/admin/treatise/list/"+page));
	}
	
	/**
	 * 添加作者
	 * @param domain
	 * @param author
	 * @param request
	 * @return
	 */
	@RequestMapping("author/create")
	@ResponseBody
	public JsonResult createAuthor(@PathVariable("domain")String domain,InstitutionTreatiseAuthor author,HttpServletRequest request){
//		InstitutionHome home = homeService.getInstitutionByDomain(domain);
//		if(home == null){
//			LOG.error("author.create:home is null ["+domain+"]");
//			return new JsonResult("找不到组织结构");
//		}
		
		int uid = SessionUtils.getUserId(request);
		author.setCreator(uid);
		
//		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
//			LOG.error("author.create:user["+uid+"] is not admin@["+domain+"]");
//			return new JsonResult("权限不足");
//		}
		
		treatiseService.createAuthor(author);
		
		JsonResult result = new JsonResult();
		result.setData(author);
		return result;
	}
	
	@RequestMapping("author/{treatiseId}/{authorId}")
	@ResponseBody
	public JsonResult authorDetail(@PathVariable("domain") String domain,@PathVariable("treatiseId") int treatiseId,@PathVariable("authorId") int authorId,HttpServletRequest request){
//		InstitutionHome home = homeService.getInstitutionByDomain(domain);
//		if(home == null){
//			LOG.error("submit :home is null["+domain+"]");
//			return new JsonResult("找不到机构主页");
//		}
//		
//		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
//			LOG.error("submit : user["+SessionUtils.getUserId(request)+"] is not admin@["+domain+"]");
//			return new JsonResult("权限不足");
//		}
		
		
		InstitutionTreatiseAuthor author = treatiseService.getAuthorById(treatiseId, authorId);
		if(author == null){
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
		
		JsonResult result = new JsonResult();
		result.setData(jsonObj);
		return result;
	}
	
	/**
	 * 查询论著作者
	 * @param domain
	 * @param treatiseId
	 * @param request
	 * @return
	 */
	@RequestMapping("authors/{treatiseId}")
	@ResponseBody
	public JsonResult getAuthors(@PathVariable("domain") String domain,
			@PathVariable("treatiseId") int treatiseId,HttpServletRequest request){
//		InstitutionHome home = homeService.getInstitutionByDomain(domain);
//		if(home == null){
//			LOG.error("authors :home is null["+domain+"]");
//			return new JsonResult("找不到机构主页");
//		}
//		
//		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
//			LOG.error("authors : user["+SessionUtils.getUserId(request)+"] is not admin@["+domain+"]");
//			return new JsonResult("权限不足");
//		}
		
		List<InstitutionTreatiseAuthor> authors = treatiseService.getAuthorByTreatiseId(treatiseId);
		JsonResult result = new JsonResult();
		result.setData(authors);
		return result;
	}

	/**
	 * 添加/更改论著提交
	 * @param treatise
	 * @param domain
	 * @param authorIds
	 * @param order
	 * @param communicationAuthors
	 * @param authorStudents
	 * @param authorTeachers
	 * @param request
	 * @return
	 */
	@RequestMapping("submit")
	@ResponseBody
	public JsonResult submit(
			InstitutionTreatise treatise,
			@PathVariable("domain")String domain,
			@RequestParam("uid[]") int[] authorIds,
			@RequestParam("order[]") int[] order,
			@RequestParam("communicationAuthor[]") boolean[] communicationAuthors,
			@RequestParam("authorStudent[]") boolean[] authorStudents,
			@RequestParam("authorTeacher[]") boolean[] authorTeachers,
			HttpServletRequest request){
		
//		InstitutionHome home = homeService.getInstitutionByDomain(domain);
//		if(home == null){
//			LOG.error("submit :home is null["+domain+"]");
//			return new JsonResult("找不到机构主页");
//		}
//		
//		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
//			LOG.error("submit : user["+SessionUtils.getUserId(request)+"] is not admin@["+domain+"]");
//			return new JsonResult("权限不足");
//		}
		SimpleUser user = SessionUtils.getUser(request);
		int treatiseId = treatise.getId();
		if(treatiseId == 0){
			treatise.setId(treatiseService.create(treatise,user.getInstitutionId(), authorIds, order, communicationAuthors, authorStudents, authorTeachers));
			treatiseService.insertTreatiseUser(treatise.getId(), user.getId());
		}else{
			treatiseService.update(treatise, user.getInstitutionId(), authorIds, order, communicationAuthors, authorStudents, authorTeachers);
		}
		
		return new JsonResult();
	}
	@RequestMapping("search/treatise")
	public String searchTreatise(Model model,@RequestParam("offset") int offset,@RequestParam("size")int size,
			HttpServletRequest request){
		String keyword = CommonUtils.trim(request.getParameter("keyword"));
		SimpleUser user = SessionUtils.getUser(request);
		List<Integer> existTreatiseIds = treatiseService.getExistTreatiseIds(user.getId());
		List<InstitutionTreatise> list = null;
		if(CommonUtils.isNull(keyword)){
			list = treatiseService.getTreatiseByInit(offset, size, CommonUtils.trim(user.getZhName()));
			keyword = user.getZhName();
		}else{
			list = treatiseService.getTreatiseByKeyword(offset, size, CommonUtils.trim(keyword));
		}
//		array = filter(array, request);
		JSONArray array =new JSONArray();
		array.addAll(list);
		array = filter(array,request);
		array = filterExist(array,existTreatiseIds);
		model.addAttribute("result", array);
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
		String existTreatises = CommonUtils.trim(request.getParameter("existTreatises"));
		if(null == existTreatises || "".equals(existTreatises)){
			return array;
		}
		JSONArray result = new JSONArray();
		String[] temp = existTreatises.split(",");
		int size = array.size();
		for(int i=0; i<size; i++){
			InstitutionTreatise obj = (InstitutionTreatise) array.get(i);
			int treatiseId = obj.getId();
			boolean exist = false;
			for(String treatise: temp){
				if(!CommonUtils.isNull(treatise) && treatiseId == Integer.parseInt(treatise)){
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
			InstitutionTreatise obj = (InstitutionTreatise) result.get(i);
			int treatiseId = obj.getId();
			if (!existIds.contains(treatiseId)) {
				array.add(obj);
			}
		}
		return array;
	}
	@RequestMapping("search")
	@ResponseBody
	public ModelAndView detail(@PathVariable("domain")String domain,HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution/treatise/searchAdd");
		SimpleUser user = SessionUtils.getUser(request);
//		List<InstitutionTreatise> list = treatiseService.getTreatiseByInit(offset, size, CommonUtils.trim(user.getZhName()));
		mv.addObject("domain",domain);
		mv.addObject("titleUser",user);
		addBaseData(mv,user);
		return mv;
	}
	@RequestMapping("search/submit")
	@ResponseBody
	public ModelAndView save(@PathVariable("domain")String domain,HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution/treatise/searchAdd");
		SimpleUser user = SessionUtils.getUser(request);
		String existTreatises = request.getParameter("treatiseIds");
		if(null == existTreatises || "".equals(existTreatises)){
			return null;
		}
		String[] treatiseId = existTreatises.split(",");
//		List<InstitutionTreatise> list = treatiseService.getTreatiseByInit(offset, size, CommonUtils.trim(user.getZhName()));
		treatiseService.insertTreatise(treatiseId, user.getId());
		mv.addObject("domain",domain);
		addBaseData(mv,user);
		return mv;
	}
	
	
	
	

//	@RequestMapping("update/{umtId}")
//	@ResponseBody
//	public boolean update(InstitutionMemberFromVmt vmtMember,@PathVariable("domain")String domain,@PathVariable("umtId")String umtId,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(CommonUtils.isNull(vmtMember.getUmtId())||!vmtMember.getUmtId().equals(umtId)){
//			LOG.error("update.error:umtId not found["+umtId+"]");
//			return false;
//		}
//		if(CommonUtils.isNull(vmtMember.getTrueName())){
//			LOG.error("update.error:trueName can't be null");
//			return false;
//		}
//		if(home==null){
//			LOG.error("update.error:not found institutiton["+domain+"]");
//			return false;
//		}
//		InstitutionVmtInfo vmtInfo=vmtService.getVmtInfoByInstitutionId(home.getInstitutionId());
//		if(vmtInfo==null){
//			LOG.error("update.error:can't found vmtInfo[insId="+home.getInstitutionId()+"]");
//			return false;
//		}
//		if(!backEndService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			LOG.error("update.error:not admin!");
//			return false;
//		}
//		if(CommonUtils.isNull(vmtMember.getUmtId())){
//			LOG.error("update.error：umtId is Null!");
//			return false;
//		}
//		try {
//			VmtUser user=CommonUtils.first(vmtUserService.searchUserByUmtId(vmtInfo.getDn(), new String[]{umtId}));
//			if(user==null){
//				LOG.error("update.error:can't find vmt user!");
//				return false;
//			}
//			user.setTitle(vmtMember.getTitle());
//			user.setName(vmtMember.getTrueName());
//			user.setOffice(vmtMember.getOfficeAddress());
//			user.setOfficePhone(vmtMember.getOfficeTelephone());
//			user.setTelephone(vmtMember.getMobilePhone());
//			user.setTitle(vmtMember.getTitle());
//			user.setSex(vmtMember.getSex());
//			user.setUmtId(vmtMember.getUmtId());
//			vmtUserService.updateByUmtId(vmtInfo.getDn(), user);
//			
//		} catch (ServiceException e) {
//			LOG.error(e.getMessage(),e);
//			return false;
//		}
//		return true;
//	}
//	@RequestMapping("detail/{umtId}.json")
//	@ResponseBody
//	public InstitutionMemberFromVmt detailJson(@PathVariable("domain")String domain,@PathVariable("umtId")String umtId,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			return null;
//		}
//		if(!backEndService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			return null;
//		}
//		return backEndService.getVmtMemberByUmtId(home.getInstitutionId(),umtId);
//	}
//	
//
}
