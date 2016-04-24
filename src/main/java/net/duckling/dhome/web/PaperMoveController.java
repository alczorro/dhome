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
package net.duckling.dhome.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.PaperRender;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.common.util.UrlUtil;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.object.JsonResult;
import net.duckling.dhome.domain.people.MenuItem;
import net.duckling.dhome.domain.people.Paper;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IDsnSearchService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendPaperService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionHomeService;
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
@RequestMapping("/people/{domain}/admin/paperMove")
public class PaperMoveController {

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
	@Autowired
	private IInstitutionHomeService insHomeService;
	/**
	 * 迁移论文库
	 * @param request
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public JsonResult create(@PathVariable("domain")String domain,HttpServletRequest request){
		int uid=SessionUtils.getUser(request).getId();
		List<Paper> papers = paperService.getPapers(uid, 0, 0);
		backendPaperService.movePaper(papers, uid);
		
		
		return new JsonResult();
	}
}
