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

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import net.duckling.dhome.common.auth.sso.Constants;
import net.duckling.dhome.domain.people.Paper;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IDsnSearchService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IMenuItemService;
import net.duckling.dhome.service.IPaperService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.impl.mock.MockDsnSearchService;
import net.duckling.dhome.service.impl.mock.MockHomeService;
import net.duckling.dhome.service.impl.mock.MockMenuItemService;
import net.duckling.dhome.service.impl.mock.MockPaperService;
import net.duckling.dhome.service.impl.mock.MockUserService;
import net.duckling.dhome.testutil.SetFieldUtils;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class AdminPaperControllerTest {
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private IHomeService hs;
	private IDsnSearchService dss;
	private IPaperService ps;
	private IMenuItemService mis;
	private AdminPaperController apc;
	private IUserService userService;
	
	@Before
	public void setUp(){
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		hs = new MockHomeService();
		dss = new MockDsnSearchService();
		ps = new MockPaperService();
		mis = new MockMenuItemService();
		userService=new MockUserService();
		SimpleUser su = new SimpleUser();
		su.setId(108);
		su.setZhName("yxp");
		request.getSession().setAttribute(Constants.CURRENT_USER, su);
		apc = new AdminPaperController();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("dsnService", dss);
		map.put("homeService",hs);
		map.put("menuItemService",mis);
		map.put("paperService", ps);
		map.put("userService", userService);
		SetFieldUtils.setValues(apc,map);
	}
	
	@Test
	public void testDisplay(){
		ModelAndView mv = apc.display(request);
		Map<String, Object> result = mv.getModel();
		Assert.assertEquals("adminPaper", mv.getViewName());
		Assert.assertEquals("yangxp", result.get("domain"));
		Assert.assertEquals("yxp", result.get("name"));
		Assert.assertNotNull(result.get("titleUser"));
		Assert.assertEquals("[]", result.get("papers").toString());
		Assert.assertEquals("1", result.get("banner"));
		Assert.assertEquals("show", result.get("status"));
	}
	
	@Test
	public void testGetPaperJSON(){
		Assert.assertNull(apc.getPaperJson(request, response));
	}
	
	@Test
	public void testDeletePaper(){
		int id = ps.create(createPaper());
		Model model = new MockModel();
		apc.deletePaper(request,id, model);
		JSONObject result = (JSONObject)model.asMap().get("result");
		Assert.assertNull(ps.getPaper(id));
		Assert.assertEquals(true, result.get("status"));
		Assert.assertEquals(id, result.get("paperId"));
	}
	
	@Test
	public void testReorder(){
		int id = ps.create(createPaper());
		Model model = new MockModel();
		apc.reorder(request, id, 1, model);
		JSONObject result = (JSONObject)model.asMap().get("result");
		Paper paper = ps.getPaper(id);
		Assert.assertEquals(true, result.get("status"));
		Assert.assertEquals(id, result.get("paperId"));
		Assert.assertEquals(1, result.get("sequence"));
		Assert.assertEquals(1, paper.getSequence());
	}
	
	@Test
	public void testChangeStatus(){
		request.setContextPath("/dhome");
		request.setRequestURI("http://localhost/dhome");
		String url = request.getRequestURI().replace(request.getContextPath(), "");
		url = url.substring(0, url.lastIndexOf("/"));
		ModelAndView mv = apc.changeStatus(request);
		RedirectView rv = (RedirectView)mv.getView();
		Assert.assertEquals(url, rv.getUrl());
	}
	
	@Test
	public void testAfterUpload(){
		Model model = new MockModel();
		apc.afterUpload(1, model);
		JSONObject result = (JSONObject)model.asMap().get("result");
		Assert.assertEquals(true, result.get("status"));
		Assert.assertEquals(1, result.get("paperId"));
	}
	
	@After
	public void tearDown(){
		request = null;
		hs = null;
		dss = null;
		ps = null;
		mis = null;
	}
	
	private Paper createPaper(){
		return Paper.build(108, "paper", "", "", "", "", "", "", "", "", "", "", 0, 0, 0,"");
	}
}
