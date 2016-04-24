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
import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.auth.aop.impl.PermissionChecker;
import net.duckling.dhome.common.auth.sso.Constants;
import net.duckling.dhome.domain.people.SimpleUser;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.mock.web.MockHttpServletRequest;

public class PermissionCheckerTest {
	
	private NPermission permission;
	private MockHttpServletRequest request = new MockHttpServletRequest();
	private SimpleUser u = new SimpleUser();
	private PermissionChecker permissionChecker;
	
	@Before
	public void setup(){
		this.permission = AnnotationUtils.findAnnotation(FirstController.class,NPermission.class);
		this.permissionChecker = new PermissionChecker();
		request.getSession().setAttribute(Constants.CURRENT_USER, u);
	}	
	
	@Test
	public void testIsAccessable(){
		boolean flag = permissionChecker.isAccessable(request, permission);
		assertEquals(true,flag);
		request.getSession().removeAttribute(Constants.CURRENT_USER);
		flag = permissionChecker.isAccessable(request, permission);
		assertEquals(false,flag);
	}

}
