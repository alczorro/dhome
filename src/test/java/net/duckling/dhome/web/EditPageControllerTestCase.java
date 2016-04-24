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

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IEducationService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInterestService;
import net.duckling.dhome.service.IMenuItemService;
import net.duckling.dhome.service.IPageService;
import net.duckling.dhome.service.IPaperService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.IWorkService;
import net.duckling.dhome.service.impl.mock.MockEduService;
import net.duckling.dhome.service.impl.mock.MockHomeService;
import net.duckling.dhome.service.impl.mock.MockInterestService;
import net.duckling.dhome.service.impl.mock.MockMenuItemService;
import net.duckling.dhome.service.impl.mock.MockPageService;
import net.duckling.dhome.service.impl.mock.MockPaperService;
import net.duckling.dhome.service.impl.mock.MockUserService;
import net.duckling.dhome.service.impl.mock.MockWorkService;
import net.duckling.dhome.testutil.SetFieldUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author lvly
 * @since 2012-10-30
 */
public class EditPageControllerTestCase {
	MockHttpServletRequest request = new MockHttpServletRequest();
	EditPageController controller = new EditPageController();
	IPageService pageService = new MockPageService();
	IMenuItemService menuItemService = new MockMenuItemService();
	IHomeService homeService = new MockHomeService();
	IUserService userService = new MockUserService();
	IWorkService workService = new MockWorkService();
	IEducationService eduService = new MockEduService();
	IInterestService interestService=new MockInterestService();
	IPaperService paperService = new MockPaperService();

	@Before
	public void before() throws Exception {
		Map<String,Object> maps=new HashMap<String,Object>();
		maps.put("pageService", pageService);
		maps.put("homeService", homeService);
		maps.put("workService", workService);
		maps.put("eduService", eduService);
		maps.put("menuItemService", menuItemService);
		maps.put("paperService", paperService);
		maps.put("userService", userService);
		SetFieldUtils.setValues(controller, maps);
	}

	@After
	public void after() throws Exception {
		
	}
	
	@Test
	public void test_newPage() {
		request.setRequestURI("/dhome/people/domain/admin/p/newPage");
		request.setContextPath("/dhome");
		request.setParameter("title", "名字要n   车昂");
		SessionUtils.updateUser(request, new SimpleUser());
		ModelAndView mv=controller.newPage("domain", request);
		RedirectView rv = (RedirectView)mv.getView();
		Assert.assertEquals("/people/domain/admin/p/mzyca0?func=edit&pid=0&newPage=true", rv.getUrl());
	}
	@Test
	public void test_changeStatus(){
		request.setRequestURI("/dhome/people/domain/admin/p/changeStatus");
		request.setContextPath("/dhome");
		ModelAndView mv=controller.changeStatus(request);
		Assert.assertEquals("/people/domain/admin/p", ((RedirectView)mv.getView()).getUrl());
	}
	@Test
	public void test_deleteStatus(){
		request.setRequestURI("/dhome/people/domain/admin/p/deletePage");
		request.setContextPath("/dhome");
		ModelAndView mv=controller.deleteStatus( request,"pageName");
		Assert.assertEquals("/people/domain/admin/p", ((RedirectView)mv.getView()).getUrl());
	}
	@Test
	public void test_addPage(){
		request.setRequestURI("/dhome/people/domain/admin/p/addPage");
		request.setContextPath("/dhome");
		request.setParameter("title","testTitle");
		request.setParameter("content","testContent");
		request.setParameter("notRealease","on");
		request.setParameter("itemId","12");
		request.setParameter("pid", "123");
		request.setParameter("pageUrl", "jlskdjflk_jlskdfj");
		SessionUtils.updateUser(request, new SimpleUser());
		ModelAndView mv=controller.submitPage(request);
		Assert.assertEquals("/people/Mockdomain/admin/p/jlskdjflk_jlskdfj", ((RedirectView)mv.getView()).getUrl());
	}
}
