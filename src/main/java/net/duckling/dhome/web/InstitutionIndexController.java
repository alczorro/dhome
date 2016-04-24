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

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.clb.FileNameSafeUtil;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.ImageUtils;
import net.duckling.dhome.common.util.PaperRender;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.Member;
import net.duckling.dhome.domain.people.Paper;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionPaperService;
import net.duckling.dhome.service.IInstitutionPeopleService;
import net.duckling.dhome.service.IInstitutionScholarEventService;
import net.duckling.dhome.service.impl.ClbFileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 机构主页首页展示
 * 
 * @author lvly
 * @since 2012-9-18
 */
@Controller
@RequestMapping("/institution/{domain}")
public class InstitutionIndexController {
	private static final int DEFAULT_SIZE = 5;
	private static final int DEFAULT_OFFSET = 0;
	private static final String ALL = "-1";
	@Autowired
	private ClbFileService imgService;
	@Autowired
	private IInstitutionHomeService homeService;
	@Autowired
	private IInstitutionPaperService paperService;
	@Autowired
	private IInstitutionPeopleService peopleService;
	@Autowired
	private IInstitutionScholarEventService ises;

	@RequestMapping(value = "/index.html")
	/**展示机构主页*/
	public ModelAndView index(@PathVariable("domain") String domain, HttpServletRequest request) {
		InstitutionHome home = homeService.getInstitutionByDomain(domain);
		List<Paper> papers = paperService.getPaperSortByCiteTime(home.getInstitutionId(), DEFAULT_OFFSET, DEFAULT_SIZE);
		List<Member> users = peopleService.getPeoplesByInstitutionId(home.getInstitutionId(), DEFAULT_OFFSET,
				DEFAULT_SIZE);
		int memberSize = peopleService.getMembersSize(home.getInstitutionId());
		int activityCount=ises.getCount(home.getInstitutionId());
		ModelAndView mv = new ModelAndView("institution/instituteIndex");
		mv.addObject("home", home);
		mv.addObject("papers", PaperRender.format(papers, null));
		mv.addObject("users", users);
		mv.addObject("memberSize", memberSize);
		mv.addObject("paperSize", home.getPaperCount());
		mv.addObject("gIndex", home.getGindex());
		mv.addObject("hIndex", home.getHindex());
		mv.addObject("activityCount",activityCount);
		mv.addObject("isMember", peopleService.isMember(SessionUtils.getUserId(request), home.getInstitutionId()));
		return mv;
	}

	/** 更改机构主页基本信息,准备 */
	@RequestMapping(value = "/edit/index.html")
	public ModelAndView edit(@PathVariable("domain") String domain) {
		InstitutionHome home = homeService.getInstitutionByDomain(domain);
		ModelAndView mv = new ModelAndView("institution/instituteConfig");
		mv.addObject("home", home);
		return mv;
	}

	/** 提交更改数据 */
	@RequestMapping(value = "/submit/index.html")
	public ModelAndView submit(@RequestParam("isDefault") boolean isDefault, @PathVariable("domain") String domain,
			HttpServletRequest request) {
		ModelAndView mv = new ModelAndView(new RedirectView("/institution/" + domain + "/index.html"));

		InstitutionHome home = homeService.getInstitutionByDomain(domain);
		if (!isDefault) {
			int id = storeImage(request);
			home.setLogoId(id);
		}
		home.setIntroduction(CommonUtils.trim(request.getParameter("introduction")));
		home.setLastEditor(SessionUtils.getUserId(request));
		home.setLastEditTime(new Date());
		homeService.updateInstitutionHome(home);
		return mv;
	}

	private int storeImage(HttpServletRequest request) {
		String fileName = CommonUtils.trim(request.getParameter("fileName"));
		fileName=FileNameSafeUtil.makeSafe(fileName);
		String path = request.getSession().getServletContext().getRealPath("");
		path += ImageUtils.PATH;
		path += fileName;
		File file = new File(("true".equals(request.getParameter("isCut"))) ? ImageUtils.getCutPath(path) : path);
		file = ImageUtils.scale(file.getAbsolutePath(), 400, 300);
		int id = imgService.createFile(file);
		ImageUtils.delete(file);
		return id;
	}
}
