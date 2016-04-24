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
import net.duckling.dhome.common.auth.sso.IAuthenticationService;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.impl.mock.MockAuthenticationService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class LogOutControllerTest {
	
	private IAuthenticationService aus;
	private MockHttpServletRequest request;
	private LogoutController lc;
	
	@Before
	public void setUp(){
		aus = new MockAuthenticationService();
		request = new MockHttpServletRequest();
		lc = new LogoutController();
		lc.setAuthenticationService(aus);
		request.getSession().setAttribute("currentUser", new SimpleUser());
	}

	@Test
	public void testLogout() {
		ModelAndView mv = lc.logout(request);
		RedirectView rv = (RedirectView)mv.getView();
		Assert.assertEquals("localhost/logout", rv.getUrl());
		Assert.assertNull(request.getSession().getAttribute("currentUser"));
	}
	
	@After
	public void tearDown(){
		aus = null;
		request = null;
		lc = null;
	}

}
