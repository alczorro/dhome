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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.domain.institution.InstitutionGrants;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionMemberFromVmt;
import net.duckling.dhome.domain.institution.InstitutionOption;
import net.duckling.dhome.domain.institution.InstitutionOptionVal;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionBackendTrainingService;
import net.duckling.dhome.service.IInstitutionGrantsService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionOptionValService;
import net.duckling.dhome.service.IUserService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


/**
 * 后台奖助学金
 * @author Brett
 *
 */
@Controller
@NPermission(role = "admin", authenticated=true)
@RequestMapping("institution/{domain}/backend/grants")
public class InstitutionBackEndGrantsController {
	private static final Logger LOG=Logger.getLogger(InstitutionBackEndGrantsController.class);
	
	@Autowired
	private IUserService userService;
	@Autowired
	private IHomeService userHomeService;
	@Autowired
	private IInstitutionBackendService backendService;
	@Autowired
	private IInstitutionGrantsService grantsService;
	@Autowired
	private IInstitutionHomeService homeService;
	@Autowired
	private IInstitutionOptionValService optionValService;
	@Autowired
	private IInstitutionBackendTrainingService backEndTrainingService;
	
	@RequestMapping("list/{page}")
	public ModelAndView list(@PathVariable("domain")String domain,
			@PathVariable("page")int page,
			SearchInstitutionCondition condition,
			HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution_backend/training/grantsList");
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null||page<0){
			return null;
		}
		Integer degree = condition.getDegree() == 0 ? null : condition.getDegree();
		PageResult<InstitutionGrants> result= grantsService.getList(null, degree, null, null, page);
		
		Map<Integer,InstitutionOptionVal> degreeMap= optionValService.getMapByOptionId(InstitutionOption.TRAINING_DEGREE, home.getInstitutionId());
		//导师
		Map<String, InstitutionMemberFromVmt> memberMap=backEndTrainingService.getAllMember(home.getInstitutionId());
		mv.addObject("memberMap",memberMap);
		
		Map<Integer, Integer> degreesCountMap = grantsService.getDegreesCount(home.getInstitutionId(), null);
		mv.addObject("degreeMap", degreeMap);
		mv.addObject("degreesCountMap", degreesCountMap);
		mv.addObject("condition", condition);
		mv.addObject("page", result);
		mv.addObject("domain", domain);
		
		return mv;
	}
}
