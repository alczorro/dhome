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

import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.Member;
import net.duckling.dhome.domain.institution.ScholarEventDetail;
import net.duckling.dhome.domain.people.Paper;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionPaperService;
import net.duckling.dhome.service.IInstitutionPeopleService;
import net.duckling.dhome.service.IInstitutionScholarEventService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lvly
 * @since 2013-4-3
 */
@Controller
@RequestMapping("/institution/{domain}")
public class RssController {
	@Autowired
	private IInstitutionHomeService homeService;
	@Autowired
	private IInstitutionPeopleService peopleService;
	@Autowired
	private IInstitutionHomeService ihs;
	@Autowired 
	private IInstitutionPaperService ips;
	@Autowired
	private IInstitutionScholarEventService ises;
	
	@RequestMapping(value="/rss_members.xml",method=RequestMethod.GET)
	public ModelAndView getUserRss(@PathVariable("domain") String domain, HttpServletRequest request) {
		InstitutionHome home= homeService.getInstitutionByDomain(domain);
		List<Member> peoples=peopleService.getPeoplesByInstitutionId(homeService.getInstitutionIdByDomain(domain), 0, Integer.MAX_VALUE);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("peopleRssViewer");
		mv.addObject("peopleRssInfo",peoples);
		mv.addObject("institution",home);
		return mv;
	}
	@RequestMapping(value="/rss_publications.xml",method=RequestMethod.GET)
	public ModelAndView getPaperRss(@PathVariable("domain") String domain, HttpServletRequest request) {
		InstitutionHome home= homeService.getInstitutionByDomain(domain);
		int insId = ihs.getInstitutionIdByDomain(domain);
		List<Paper> papers = ips.getPaperSortByCiteTime(insId, 0, Integer.MAX_VALUE);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("paperRssViewer");
		mv.addObject("paperRssInfo",papers);
		mv.addObject("institution",home);
		return mv;
	}
	@RequestMapping(value="/rss_scholarevent.xml",method=RequestMethod.GET)
	public ModelAndView getScholarRss(@PathVariable("domain") String domain, HttpServletRequest request) {
		InstitutionHome home= homeService.getInstitutionByDomain(domain);
		InstitutionHome ih = ihs.getInstitutionByDomain(domain);
		List<ScholarEventDetail> list = ises.getAllScholarEventOfInstitution(ih.getInstitutionId(), 0, Integer.MAX_VALUE);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("scholarRssViewer");
		mv.addObject("scholarRssInfo",list);
		mv.addObject("institution",home);
		return mv;
	}

}
