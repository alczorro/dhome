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
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionPublication;
import net.duckling.dhome.domain.object.JsonResult;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionPublicationService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@NPermission(role = "admin", authenticated=true)
@RequestMapping("institution/{domain}/backend/publication")
public class InstitutionBackEndPublicationController {
	
	private static final Logger LOG=Logger.getLogger(InstitutionBackEndPublicationController.class);
	
	@Autowired
	private IInstitutionBackendService backendService;
	
	@Autowired
	private IInstitutionHomeService homeService;
	
	@Autowired
	private IInstitutionPublicationService pubService;
	
	@RequestMapping("create")
	@ResponseBody
	public JsonResult create(InstitutionPublication pub,@PathVariable("domain")String domain,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			LOG.error("create:home is null["+domain+"]");
			return new JsonResult("home is null["+domain+"]");
		}
		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("create:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return new JsonResult("not admin!");
		}
		
		pub.setInstitutionId(home.getInstitutionId());
		pubService.create(pub);
		JsonResult jr= new JsonResult();
		jr.setData(pub);
		return jr;
	}

}
