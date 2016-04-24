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
package net.duckling.dhome.common.auth.aop.impl;

import static junit.framework.Assert.assertEquals;
import net.duckling.dhome.common.auth.aop.FirstController;
import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.auth.sso.Constants;
import net.duckling.dhome.domain.people.SimpleUser;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.mock.web.MockHttpServletRequest;

public class AccessDeniedHandlerTest {
	
	private AccessDeniedHandler handler = null;
	private NPermission permission;
	private MockHttpServletRequest request = new MockHttpServletRequest();
	private SimpleUser u = new SimpleUser();
	
	@Before
	public void setup(){
		this.handler = new AccessDeniedHandler();
		this.permission = AnnotationUtils.findAnnotation(FirstController.class,NPermission.class);
		request.setRequestURI("/dhome/people/clive");
		request.setContextPath("/dhome");
		request.setServerPort(80);
		request.setServerName("localhost");
		u.setEmail("liji@cnic.cn");
		u.setId(1);
		request.getSession().setAttribute(Constants.CURRENT_USER, u);
	}
	
	@Test
	public void testGetLastRequestURL(){
		assertEquals("http://localhost/dhome/people/clive", handler.getLastRequestURL(request));
		request.setServerPort(8080);
		assertEquals("http://localhost:8080/dhome/people/clive", handler.getLastRequestURL(request));
	}

	@Test
	public void testParseRedirectURL() {
		request.setServerPort(8080);
		assertEquals(permission.authenticated(),true);
		assertEquals("http://localhost:8080/dhome/people/clive",handler.parseRedirectURL(request));
		request.getSession().removeAttribute(Constants.CURRENT_USER);
		assertEquals("http://localhost:8080/dhome",handler.parseRedirectURL(request));
	}

}

