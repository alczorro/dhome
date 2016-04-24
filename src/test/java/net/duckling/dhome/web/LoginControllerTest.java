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

import junit.framework.Assert;
import net.duckling.dhome.common.auth.sso.Constants;
import net.duckling.dhome.domain.people.SimpleUser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;

public class LoginControllerTest {
	private LoginController lc;
	private MockHttpServletRequest request;
	private Model model;
	
	@Before
	public void setUp(){
		lc = new LoginController();
		request = new MockHttpServletRequest();
		model = new MockModel();
	}
	
	@Test
	public void testIsLogin(){
		SimpleUser su = new SimpleUser();
		su.setId(108);
		request.getSession().setAttribute(Constants.CURRENT_USER, su);
		lc.isLogin(request, model);
		Assert.assertEquals(true, model.asMap().get("isLogin"));
	}
	
	@After
	public void tearDown(){
		lc = null;
		request = null;
	}
}
