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
package net.duckling.dhome.common.auth.aop;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.duckling.dhome.common.auth.aop.impl.AccessDeniedHandler;
import net.duckling.dhome.common.auth.aop.impl.PermissionChecker;
import net.duckling.dhome.common.auth.sso.Constants;
import net.duckling.dhome.domain.people.SimpleUser;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;

public class PermissionIntercepterTest {

	private PermissionIntercepter incepter = null;
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = null;
	private FirstController firstController = null;
	private SimpleUser user = new SimpleUser();
	
	@Before
	public void setup() {
		incepter = new PermissionIntercepter();
		request  = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		firstController = new FirstController();
		incepter.setListener(new AccessDeniedHandler());
		incepter.setParam("func");
		incepter.setPermissionChecker(new PermissionChecker());
		request.setRequestURI("http://localhost/dhome/people/clive/admin/index.html");
		user.setId(1);
		user.setEmail("test@cnic.cn");
	}
	
	@Test
	public void testPrehandleWithLoginUser() {
		request.addParameter("func", "sayHello");
		request.getSession().setAttribute(Constants.CURRENT_USER, user);
		request.getSession().setAttribute(Constants.CURRENT_USER_DOMAIN, "clive");
		assertEquals(true,incepter.preHandle(request, response, firstController));
	}
	
	@Test
	public void testPrehandleWithGuest() throws UnsupportedEncodingException {
		request.addParameter("func", "sayHello");
		request.getSession().removeAttribute(Constants.CURRENT_USER);
		request.getSession().setAttribute(Constants.CURRENT_USER_DOMAIN, "clive");
		assertEquals(true,incepter.preHandle(request, response, firstController));
		request.removeAllParameters();
		request.addParameter("func", "iphone");
		//assertEquals(false,incepter.preHandle(request, response, firstController));
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testPathVariable() throws NoSuchMethodException, SecurityException{
		request.setRequestURI("http://localhost/dhome/test/abc/index.html");
		Method method = FirstController.class.getMethod("getDomain", String.class);
		HandlerMethod hm = new HandlerMethod(firstController,method);
		incepter.preHandle(request, response, hm);
	}
	
	@Test
	public void testDomain(){
		request.setRequestURI("http://localhost/dhome/people/sina-abc-def/admin/index.html");
		String url = request.getRequestURI();
		String regex = "/people/[A-Z\\-0-9a-z]+/";  
	    Pattern p = Pattern.compile(regex);  
	    Matcher m = p.matcher(url);
	    while(m.find()){
	    	String temp = m.group();
	    	temp = temp.substring(temp.indexOf('/',2)+1,temp.lastIndexOf('/'));
	    	System.out.println(temp);
	    }
	}

}
