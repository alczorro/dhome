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
import net.duckling.dhome.service.IAccessLogService;
import net.duckling.dhome.service.IDsnSearchService;
import net.duckling.dhome.service.IEducationService;
import net.duckling.dhome.service.IFavoriteUrlService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInterestService;
import net.duckling.dhome.service.IMenuItemService;
import net.duckling.dhome.service.IPageService;
import net.duckling.dhome.service.IPaperService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.IWorkService;
import net.duckling.dhome.service.impl.mock.MockDsnSearchService;
import net.duckling.dhome.service.impl.mock.MockEduService;
import net.duckling.dhome.service.impl.mock.MockHomeService;
import net.duckling.dhome.service.impl.mock.MockInterestService;
import net.duckling.dhome.service.impl.mock.MockMenuItemService;
import net.duckling.dhome.service.impl.mock.MockPageService;
import net.duckling.dhome.service.impl.mock.MockPaperService;
import net.duckling.dhome.service.impl.mock.MockUserService;
import net.duckling.dhome.service.impl.mock.MockWorkService;
import net.duckling.dhome.service.impl.mock.StubAccessLogService;
import net.duckling.dhome.service.impl.mock.StubFavoriteUrlService;
import net.duckling.dhome.testutil.SetFieldUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lvly
 * @since 2012-10-30
 */
public class ShowPageControllerTestCase {
	MockHttpServletRequest request = new MockHttpServletRequest();
	ShowPageController controller = new ShowPageController();
	IPageService pageService = new MockPageService();
	IMenuItemService menuItemService = new MockMenuItemService();
	IHomeService homeService = new MockHomeService();
	IUserService userService = new MockUserService();
	IWorkService workService = new MockWorkService();
	IEducationService eduService = new MockEduService();
	IInterestService interestService=new MockInterestService();
	IPaperService paperService = new MockPaperService();
	IDsnSearchService dsnService = new MockDsnSearchService();
	IAccessLogService logService=new StubAccessLogService();
	IFavoriteUrlService favService=new StubFavoriteUrlService();

	@Before
	public void before() throws Exception {
		Map<String,Object> maps=new HashMap<String,Object>();
		maps.put("pageService", pageService);
		maps.put("homeService", homeService);
		maps.put("userService", userService);
		maps.put("workService", workService);
		maps.put("eduService", eduService);
		maps.put("interestService", interestService);
		maps.put("menuItemService", menuItemService);
		maps.put("paperService", paperService);
		maps.put("dsnService", dsnService);
		maps.put("logService", logService);
		maps.put("favService", favService);
		SetFieldUtils.setValues(controller, maps);
	}

	@After
	public void after() throws Exception {
		
	}
	@Test
	public void test_isUrlUsed() {
		// ------------ set param--------------
		request.setParameter("pid", "12");
		request.setParameter("pageUrl", "pageName");
		// ------------get result---------
		boolean result=controller.isUrlUsed("domain", "pageName", 12, request);
		Assert.assertEquals(false, result);
	}


	@Test
	public void test_showAdminPage() {
		// ------------set param--------------
		request.setRequestURI("/dhome/people/domain/admin/p/pageName");
		request.setContextPath("/dhome");
		// --------------get Result------------
		ModelAndView mv = controller.showAdminPage("domain", "pageName", request);
		Map map = mv.getModel();
		Assert.assertEquals("show", map.get("status"));
		Assert.assertEquals("/people/domain/admin/p/pageName", map.get("active"));

	}

	@Test
	public void test_showPage() {
		// ------------set param--------------
		request.setRequestURI("/dhome/people/domain/index.html");
		request.setContextPath("/dhome");
		// --------------get Result------------
		ModelAndView mv = controller.showPage("domain", "index", request);
		Map map = mv.getModel();
		Assert.assertEquals("show", map.get("status"));
		Assert.assertEquals("/people/domain/index.html", map.get("active"));
		Assert.assertTrue( (boolean)map.get("isIndex"));
	}
}
