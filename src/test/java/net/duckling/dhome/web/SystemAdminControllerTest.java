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
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IMailService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.impl.mock.MockHomeService;
import net.duckling.dhome.service.impl.mock.MockMailService;
import net.duckling.dhome.service.impl.mock.MockUserService;
import net.duckling.dhome.testutil.SetFieldUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

public class SystemAdminControllerTest {
	
	private IUserService us = null;
	private IHomeService hs = null;
	private IMailService ms = null;
	private MockHttpServletRequest request = null;
	private SystemAdminUserController sac = null;
	private Model model = null;
	
	@Before
	public void setUp(){
		us = new MockUserService();
		hs = new MockHomeService();
		ms = new MockMailService();
		model = new MockModel();
		request = new MockHttpServletRequest();
		sac = new SystemAdminUserController();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("hs", hs);
		map.put("ms", ms);
		map.put("us", us);
		SetFieldUtils.setValues(sac, map);
	}
	
	@Test
	public void testDisplay(){
		ModelAndView mv = sac.display(request);
		Assert.assertEquals("systemAdmin", mv.getViewName());
	}
	
	@Test
	public void testAll(){
		request.setParameter("status", "");
		request.setParameter("keyword", "");
		sac.all(request, model, 0, 10);
		String users = (String)model.asMap().get("users");
		Integer actualLen = (Integer)model.asMap().get("actualOffset");
		Assert.assertNotNull(users);
		Assert.assertEquals(new Integer(20), actualLen);
	}
	
	@Test
	public void testCheck(){
		request.setParameter("reason", "you are a fool");
		sac.check(request, model, 108, "fail");
		Integer uid = (Integer)model.asMap().get("id");
		String status = (String)model.asMap().get("status");
		String name = (String)model.asMap().get("name");
		Assert.assertEquals(new Integer(108), uid);
		Assert.assertEquals("fail", status);
		Assert.assertNull(name);
	}
	
	@After
	public void tearDown(){
		us = null;
		hs = null;
		ms = null;
		request = null;
		sac = null;
	}
}
