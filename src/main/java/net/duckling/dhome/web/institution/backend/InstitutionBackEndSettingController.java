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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import net.duckling.common.util.CommonUtils;
import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.util.PinyinUtil;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionOption;
import net.duckling.dhome.domain.institution.InstitutionOptionVal;
import net.duckling.dhome.domain.institution.InstitutionSetting;
import net.duckling.dhome.domain.object.JsonResult;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendAwardService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionOptionValService;
import net.duckling.dhome.service.IUserService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 后台设置
 * @author Brett
 *
 */
@Controller
@NPermission(role = "admin", authenticated=true)
@RequestMapping("/institution/{domain}/backend/setting")
public class InstitutionBackEndSettingController {
	private static final Logger LOG=Logger.getLogger(InstitutionBackEndSettingController.class);
	@Autowired
	private IHomeService userHomeService;
	@Autowired
	private IInstitutionHomeService homeService;
	@Autowired
	private IInstitutionBackendService backendService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IInstitutionBackendAwardService awardSerice;
	@Autowired
	private IInstitutionOptionValService optionValService;
	@Autowired
	private IInstitutionBackendService backEndService;
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ModelAndView list(
			@PathVariable("domain")String domain,
			@PathVariable("id")int id,
			HttpServletRequest request){
		
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("list:home is null["+domain+"]  or page < 0");
			return null;
		}
		
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("list:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return new ModelAndView("institution_backend/permission");
		}
		
		ModelAndView mv=new ModelAndView("institution_backend/setting/list");
		
		List<InstitutionSetting> settingList = InstitutionSetting.getList();
		mv.addObject("settingList",settingList);
		
		List<InstitutionOption> optionList = InstitutionOption.getList(id);
		
		for(InstitutionOption item : optionList){
			List<InstitutionOptionVal> valList = optionValService.getListByOptionId(item.getId(), home.getInstitutionId());
			item.setValList(valList);
		}
		
		mv.addObject("optionList",optionList);
		mv.addObject("domain",domain);
		mv.addObject("id",id);
		return mv;
	}
	
	
	@RequestMapping("deptlist")
	public ModelAndView deptlist(
			@PathVariable("domain")String domain,
			HttpServletRequest request){
		
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("list:home is null["+domain+"]  or page < 0");
			return null;
		}
		
		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("list:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return null;
		}
		
		ModelAndView mv=new ModelAndView("institution_backend/setting/deptList");
		
		//部门
		List<InstitutionDepartment> depts=backEndService.getVmtDepartment(home.getInstitutionId());
		mv.addObject("dept",depts);
		return mv;
	}
	
	
	@RequestMapping("sort/initial")
	@ResponseBody
	public JsonResult sortInitial(@PathVariable("domain")String domain,
			HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("add:home is null["+domain+"]");
			return null;
		}
		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("add:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return null;
		}
		
		List<InstitutionDepartment> depts=backEndService.getVmtDepartmentOrderPinyin(home.getInstitutionId());
		Map<Character, List<InstitutionDepartment>> temp = new TreeMap<Character, List<InstitutionDepartment>>();
		for(InstitutionDepartment dept : depts){
			Character ch = PinyinUtil.getPinyin(dept.getShortName()).toLowerCase().charAt(0);
			if(temp.containsKey(ch)){
				temp.get(ch).add(dept);
			}else{
				List<InstitutionDepartment> list = new ArrayList<InstitutionDepartment>();
				list.add(dept);
				temp.put(ch, list);
			}
		}
		
		depts.clear();
		for(Map.Entry<Character, List<InstitutionDepartment>> entry : temp.entrySet()){
			depts.addAll(entry.getValue());
		}

		backEndService.batchUpdateDepartment(home.getInstitutionId(), depts);
		
		JsonResult result = new JsonResult();
	    return result;
	}
	
	
	@RequestMapping("edit")
	@ResponseBody
	public JsonResult edit(@PathVariable("domain")String domain,
			@RequestParam("id") int id,
			HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("add:home is null["+domain+"]");
			return null;
		}
		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("add:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return null;
		}
		
		JsonResult result = new JsonResult();
		InstitutionOptionVal val = null;
		if(id>0){
			val = optionValService.getById(id);
		}else{
			 val = new InstitutionOptionVal();
		}
		result.setData(val);
		
	    return result;
	}
	
	@RequestMapping(value="/dept/edit",method = RequestMethod.POST)
	@ResponseBody
	public JsonResult editDept(
			InstitutionOptionVal optionVal,
			@PathVariable("domain")String domain,
			@RequestParam("shortName") String shortName,
			@RequestParam("listRank") int listRank,
			@RequestParam("id") int id,
			HttpServletRequest request){
		
		InstitutionHome home = homeService.getInstitutionByDomain(domain);
		if(home == null){
			LOG.error("submit :home is null["+domain+"]");
			return new JsonResult("找不到机构主页");
		}
		
		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("submit : user["+SessionUtils.getUserId(request)+"] is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		JsonResult result = new JsonResult();
		backendService.updateDeptShortName(id, shortName,listRank);
		return result;
	}


	/**
	 * 保存
	 * @param optionVal
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping("submit")
	@ResponseBody
	public JsonResult submit(
			InstitutionOptionVal optionVal,
			@PathVariable("domain")String domain,
			HttpServletRequest request){
		
		InstitutionHome home = homeService.getInstitutionByDomain(domain);
		if(home == null){
			LOG.error("submit :home is null["+domain+"]");
			return new JsonResult("找不到机构主页");
		}
		
		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("submit : user["+SessionUtils.getUserId(request)+"] is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		optionVal.setInstitutionId(home.getInstitutionId());
		JsonResult result = new JsonResult();
		if(optionVal.getId() > 0){
			InstitutionOptionVal original = optionValService.getById(optionVal.getId());
			original.setVal(optionVal.getVal());
			original.setRank(optionVal.getRank());
			optionValService.edit(original);
		}else{
			optionValService.add(optionVal);
		}
		return result;
	}
	
	/**
	 * 删除
	 * @param id
	 * @param domain
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/delete",method = RequestMethod.GET)
	@ResponseBody
	public JsonResult delete(
			@RequestParam("id") int id,
			@PathVariable("domain")String domain,
			HttpServletRequest request){
		
		InstitutionHome home = homeService.getInstitutionByDomain(domain);
		if(home == null){
			LOG.error("submit :home is null["+domain+"]");
			return new JsonResult("找不到机构主页");
		}
		
		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
			LOG.error("submit : user["+SessionUtils.getUserId(request)+"] is not admin@["+domain+"]");
			return new JsonResult("权限不足");
		}
		JsonResult result = new JsonResult();
		InstitutionOptionVal original = optionValService.getById(id);
		result.setData(original);
		optionValService.delete(id, home.getInstitutionId());
		return result;
	}

}
