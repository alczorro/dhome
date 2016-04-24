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
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IEducationService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInterestService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.IWorkService;
import net.duckling.dhome.service.impl.mock.MockHomeService;
import net.duckling.dhome.service.impl.mock.MockInterestService;
import net.duckling.dhome.service.impl.mock.MockUserService;
import net.duckling.dhome.service.impl.mock.MockWorkService;
import net.duckling.dhome.testutil.SetFieldUtils;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class ShowPersonalInfoControllerTest {
	
	private MockHttpServletRequest request;
	private IUserService us;
	private IWorkService ws;
	private IEducationService es;
	private IHomeService hs;
	private IInterestService is;
	private SimpleUser su;
	private ShowPersonalInfoController spic;
	
	@Before
	public void setUp(){
		request = new MockHttpServletRequest();
		us = new MockUserService();
		ws = new MockWorkService();
		es = new MockEducationService();
		hs = new MockHomeService();
		su = new SimpleUser();
		is = new MockInterestService();
		su.setId(108);
		spic = new ShowPersonalInfoController();
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("eduService", es);
		param.put("homeService", hs);
		param.put("interestService",is );
		param.put("userService", us);
		param.put("workService", ws);
		SetFieldUtils.setValues(spic, param);
		request.getSession().setAttribute(Constants.CURRENT_USER, su);
	}
	
	@Test
	public void testDisplay(){
		ModelAndView mv = spic.display(request);
		RedirectView rv = (RedirectView)mv.getView();
		Assert.assertEquals("/people/yangxp/admin/personal/baseinfo", rv.getUrl());
	}
	
	@Test
	public void testBaseInfo(){
		ModelAndView mv = spic.baseinfo(request);
		Map<String, Object> result = mv.getModel();
		JSONObject user = (JSONObject) result.get("user");
		Assert.assertEquals("personalBaseInfo", mv.getViewName());
		Assert.assertEquals("1", result.get("banner"));
		Assert.assertNull(result.get("firstClass"));
		Assert.assertNull(result.get("secondClass"));
		Assert.assertEquals("yangxp", user.get("url"));
		Assert.assertEquals(1, user.get("firstClassDiscipline"));
		Assert.assertEquals(11, user.get("secondClassDiscipline"));
		Assert.assertEquals("第一学科", user.get("firstClassDisciplineName"));
		Assert.assertEquals("第二学科", user.get("secondClassDisciplineName"));
		Assert.assertEquals("introduction", user.get("introduction"));
	}
	
	@Test
	public void testDisplayWork(){
		ModelAndView mv = spic.displayWork(request);
		Map<String, Object> result = mv.getModel();
		Assert.assertEquals("personalWorkInfo", mv.getViewName());
		Assert.assertEquals("1", result.get("banner"));
		Assert.assertNull(result.get("works"));
	}
	
	@Test
	public void testDisplayEdu(){
		
		SessionUtils.updateUser(request,su);
		ModelAndView mv = spic.displayEdu(request);
		Map<String, Object> result = mv.getModel();
		Assert.assertEquals("personalEducationInfo", mv.getViewName());
		Assert.assertEquals("1", result.get("banner"));
		Assert.assertNull(result.get("educations"));
	}
	
	@Test
	public void testDisplayPhoto(){
		ModelAndView mv = spic.displayPhoto(request);
		Map<String, Object> result = mv.getModel();
		Assert.assertEquals("1", result.get("banner"));
		Assert.assertEquals("personalPhotoInfo", mv.getViewName());
		Assert.assertEquals("/resources/images/dhome-head.png", result.get("fileName"));
		Assert.assertEquals(request.getContextPath(), result.get("filePath"));
	}
	
	@After
	public void tearDown(){
		request = null;
		us = null;
		ws = null;
		es = null;
		hs = null;
	}
}
