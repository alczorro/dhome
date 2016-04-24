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
/**
 * 
 */
package net.duckling.dhome.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.Member;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionPeopleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 机构主页首页展示
 * 
 * @author lvly
 * @since 2012-9-18
 */
@Controller
@RequestMapping("/institution/{domain}")
public class InstitutionMemberController {
	@Autowired
	private IInstitutionHomeService homeService;
	@Autowired
	private IInstitutionPeopleService peopleService;

	@RequestMapping(value = "/members.html")
	public ModelAndView index(HttpServletRequest request,@PathVariable("domain") String domain) {
		ModelAndView mv = new ModelAndView("institution/instituteMember");
		InstitutionHome home= homeService.getInstitutionByDomain(domain);
		mv.addObject("home",home);
		mv.addObject("isMember", peopleService.isMember(SessionUtils.getUserId(request), home.getInstitutionId()));

		return mv;
	}

	@RequestMapping(value = "getMembers.json")
	@ResponseBody
	public List<Member> getMembers(@PathVariable("domain") String domain, @RequestParam("offset") int offset,
			@RequestParam("size") int size) {
		return peopleService.getPeoplesByInstitutionId(homeService.getInstitutionIdByDomain(domain), offset, size);
	}

	public void setHomeService(IInstitutionHomeService homeService) {
		this.homeService = homeService;
	}

	public void setPeopleService(IInstitutionPeopleService peopleService) {
		this.peopleService = peopleService;
	}
	
}
