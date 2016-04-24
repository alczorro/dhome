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

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionVmtInfo;
import net.duckling.dhome.domain.object.JsonResult;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionVmtService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.impl.VMTMessageHandlerImpl;
import net.duckling.vmt.api.domain.TreeNode;
import net.duckling.vmt.api.impl.GroupService;
import net.duckling.vmt.api.impl.OrgService;
import net.duckling.vmt.api.impl.UserService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import cn.vlabs.rest.ServiceException;

@Controller
@RequestMapping("/institution/{domain}/backend")
public class InstitutionBackEndIndexController {
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
	private IUserService userService;
	@Autowired
	private IInstitutionBackendService backEndService;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private VMTMessageHandlerImpl vmtMessageHandleImpl;
	
	private static final Logger LOGGER=Logger.getLogger(InstitutionBackEndIndexController.class);
	
	@RequestMapping
	public RedirectView index(HttpServletRequest request,@PathVariable("domain")String domain){
		return new RedirectView(request.getContextPath()+"institution/"+domain+"/backend/member/list/1");
	}
	@RequestMapping("syncFromVmt")
	@ResponseBody
	public JsonResult index(@PathVariable("domain")String domain){
		JsonResult jr= new JsonResult();
		
		InstitutionHome insHome=homeService.getInstitutionByDomain(domain);
		InstitutionVmtInfo vmtInfo=vmtService.getVmtInfoByInstitutionId(insHome.getInstitutionId());
		if(vmtInfo==null){
			String errorMsg = "why can't find vmt ref info that domain["+domain+"],id["+insHome.getId()+"]";
			LOGGER.error(errorMsg);
			jr.setData(errorMsg);
			return jr;
		}
		
		try {
			if("group".equals(vmtInfo.getGroupOrOrg())){
				TreeNode treeNode=groupService.getMember(vmtInfo.getDn());
				vmtService.syncGroup(treeNode, vmtInfo);
				
			}else if("org".equals(vmtInfo.getGroupOrOrg())){
				String vmtDn = vmtInfo.getDn();
				TreeNode treeNode=orgService.getTree(vmtDn);
				vmtService.syncOrg(treeNode, vmtInfo);
			}
		} catch (ServiceException e) {
			LOGGER.error(e.getMessage(),e);
			jr.setData(e.getMessage());
			return jr;
		}
		jr.setData("success");
		return jr;
	}

}
