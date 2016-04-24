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
package net.duckling.dhome.common.auth.aop.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import net.duckling.dhome.common.auth.aop.IPermissionChecker;
import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionHomeService;

public class PermissionChecker implements IPermissionChecker {
	@Autowired
	private IInstitutionHomeService homeService;
	@Autowired
	private IInstitutionBackendService backendService;
	public static String ROLE_ADMIN="admin";
	public static String ROLE_USER="user";
	public static String MEMBER_TOURIST="tourist";
	public static String MEMBER_IAP="iap";
	
	public boolean isAccessable(HttpServletRequest request, NPermission requirePermission) {
		String role = ROLE_USER;
		String member = MEMBER_TOURIST;
		if (requirePermission.role() != null) {
			role = requirePermission.role();
		}
		if (requirePermission.member() != null) {
			member = requirePermission.member();
		}
		
		if (requirePermission.authenticated()) {
			HttpSession session = request.getSession();
			SimpleUser user=(SimpleUser) session.getAttribute("currentUser");
			if(user==null){
				return false;
			}
		}
		
		if (ROLE_ADMIN.equals(role)) {
			return checkAdmin(request, requirePermission);
		}
		if (MEMBER_IAP.equals(member)) {
			return checkIsIAP(request, requirePermission);
		}
		
		return true;
	}
	
	private boolean checkAdmin(HttpServletRequest request,
			NPermission requirePermission) {
		HttpSession session = request.getSession();
		SimpleUser user = (SimpleUser) session.getAttribute("currentUser");
		if(user.getInstitutionId()==null){
			return false;
		}else{
			InstitutionHome home=homeService.getInstitutionByInstitutionId(user.getInstitutionId());
			return backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail());
		}
	}
	private boolean checkIsIAP(HttpServletRequest request,
			NPermission requirePermission) {
		HttpSession session = request.getSession();
		SimpleUser user = (SimpleUser) session.getAttribute("currentUser");
		if(user.getInstitutionId()==null){
			return false;
		}else{
			InstitutionHome home=homeService.getInstitutionByInstitutionId(user.getInstitutionId());
			return backendService.isMember(home.getInstitutionId(),SessionUtils.getUser(request).getEmail());
		}
	}
}
