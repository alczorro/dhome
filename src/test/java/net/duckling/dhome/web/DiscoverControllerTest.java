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

import java.util.Map;

import junit.framework.Assert;
import net.duckling.dhome.service.IComposedUserService;
import net.duckling.dhome.service.IEducationService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInterestService;
import net.duckling.dhome.service.IWorkService;
import net.duckling.dhome.service.impl.mock.MockComposedUserService;
import net.duckling.dhome.service.impl.mock.MockInterestService;
import net.duckling.dhome.service.impl.mock.MockWorkService;
import net.duckling.dhome.service.impl.mock.StubInstitutionHomeService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

public class DiscoverControllerTest {
	private DiscoverController dc;
	private IComposedUserService us;
	private MockHttpServletRequest request;
	private IInstitutionHomeService homeService;
	private String json_spring;
	private IWorkService workService;
	private IEducationService eduService;
	private IInterestService interestService;
	
	@Before
	public void setUp(){
		dc = new DiscoverController();
		interestService=new MockInterestService();
		workService=new MockWorkService();
		eduService=new MockEducationService();
		dc.setInterestService(interestService);
		dc.setWorkService(workService);
		dc.setEduService(eduService);
		us = new MockComposedUserService();
		homeService=new StubInstitutionHomeService();
		dc.setComposedUserService(us);
		dc.setHomeService(homeService);
		request = new MockHttpServletRequest();
		
		json_spring = "jsontournamenttemplate";
	}
	
	@Test
	public void testShowAllComposedUsers(){
		ModelAndView mv = dc.showAllComposedUsers(request);
		Map<String, Object> map = mv.getModel();
		Assert.assertEquals("discover", mv.getViewName());
		Assert.assertNull(map.get("discipline"));
		Assert.assertEquals("{\"context\":\"all\"}", map.get("initData").toString());
	}
	
	@Test
	public void testShowAllComposedUsers2(){
		request.setParameter("keyword", "xx");
		ModelAndView mv = dc.showAllComposedUsers(request);
		Map<String, Object> map = mv.getModel();
		Assert.assertEquals("discover", mv.getViewName());
		Assert.assertNull(map.get("discipline"));
		Assert.assertEquals("{\"keyword\":\"xx\",\"context\":\"search\"}", map.get("initData").toString());
	}
	
	@Test
	public void testAll(){
		Model model = new MockModel();
		String json_template = dc.all(request, model, 0, 0);
		Assert.assertEquals(json_spring, json_template);
		Assert.assertEquals("[]", model.asMap().get("users").toString());
	}
	
	@Test
	public void testLatest(){
		Model model = new MockModel();
		String json_template = dc.latest(request, model, 0, 0);
		Assert.assertEquals(json_spring, json_template);
		Assert.assertEquals("[]", model.asMap().get("users").toString());
	}
	
	@Test
	public void testDiscipline(){
		Model model = new MockModel();
		String json_template = dc.discipline(request, model, 0, 0, 0, 0);
		Assert.assertEquals(json_spring, json_template);
		Assert.assertEquals("[]", model.asMap().get("users").toString());
	}
	
	@Test
	public void testSearch(){
		Model model = new MockModel();
		String json_template = dc.search(request, model, "", 0, 0,"all");
		Assert.assertEquals(json_spring, json_template);
		Assert.assertEquals("[]", model.asMap().get("users").toString());
	}
	
	@Test
	public void testFour(){
		Model model = new MockModel();
		String json_template = dc.four(request, model);
		Assert.assertEquals(json_spring, json_template);
		Assert.assertEquals("[{\"uid\":0,\"zhName\":\"test\",\"interest\":null,\"imgURL\":\"\\/system\\/img?imgId=1\",\"salutation\":null,\"institution\":null,\"userPageURL\":\"\\/people\\/\\/test\"}]", model.asMap().get("users").toString());
	}
	
	@After
	public void tearDown(){
		us = null;
		dc = null;
	}
}
