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
package net.duckling.dhome.common.auth.sso;

import java.io.IOException;

import javax.servlet.ServletException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class RedirectURLTest {
	
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	
	@Before
	public void setUp(){
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}
	
	@Test
	public void testForward1() throws IOException, ServletException{
		RedirectURL rurl = new RedirectURL(true, "my.home");
		rurl.forward(request, response);
		Assert.assertEquals("my.home", rurl.getUrl());
		Assert.assertTrue(rurl.isRedirect());
		Assert.assertEquals("my.home", response.getRedirectedUrl());
	}
	
	@Test
	public void testForward2() throws IOException, ServletException{
		RedirectURL rurl = new RedirectURL(false, "my.home");
		rurl.forward(request, response);
		Assert.assertEquals("my.home", rurl.getUrl());
		Assert.assertFalse(rurl.isRedirect());
	}
	
	@After
	public void tearDown(){
		
	}
}
