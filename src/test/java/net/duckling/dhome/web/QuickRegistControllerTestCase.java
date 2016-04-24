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
import net.duckling.dhome.common.auth.sso.IAuthenticationService;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IEmailSendLogService;
import net.duckling.dhome.service.IRegistService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.impl.mock.MockAuthenticationService;
import net.duckling.dhome.service.impl.mock.MockRegistService;
import net.duckling.dhome.service.impl.mock.MockUserService;
import net.duckling.dhome.service.impl.mock.StubEmailSendLogService;
import net.duckling.dhome.testutil.SetFieldUtils;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * 针对注册模块单元测试
 * @author lvly
 * @since 2012-8-23
 */
public class QuickRegistControllerTestCase {
	MockHttpServletRequest request = new MockHttpServletRequest();
	MockHttpServletResponse response = new MockHttpServletResponse();
	QuickRegistController controller = new QuickRegistController();
	
	IRegistService registService=new MockRegistService();
	IAuthenticationService authenticationService=new MockAuthenticationService();
	IUserService userService = new MockUserService();
	IEmailSendLogService emailSendLogService=new StubEmailSendLogService();
	@Before
	public void before(){
		SetFieldUtils.setValue(controller, "registService", registService);
		SetFieldUtils.setValue(controller, "authenticationService", authenticationService);
		SetFieldUtils.setValue(controller, "userService", userService);
	}
	@Test
	public void test_isEmailUsed(){
		request.setParameter("email","email@test.com");
		boolean result=controller.isEmailUsed(request);
		//如果true，可用
		Assert.assertTrue(result);
	}
	@Test
	public void isDomainUsed(){
		request.setParameter("domain","domainTest");
		boolean result=controller.isDomainUsed(request);
		Assert.assertTrue(result);
	}
	@Test
	public void test_stepOne(){
		request.setParameter("domain","domainTest");
		request.setParameter("zhName","测试");
		request.setParameter("emailToRegist","email@email.com");
		ModelAndView mv=controller.stepOne(request);
		Map map=mv.getModel();
		Assert.assertEquals("email@email.com", map.get("emailToRegist"));
		Assert.assertEquals("测试", map.get("zhName"));
		Assert.assertEquals("个人研究主页创建",map.get("pageName"));
		Assert.assertEquals("Shi Ce", map.get("enName"));
		Assert.assertEquals("ceshi", map.get("defaultDomain"));
	}
	@Test
	public void test_stepTwo(){
		ModelAndView mv=controller.stepTwo(request);
		Assert.assertEquals("两次密码输入不一致",mv.getModel().get("passWordMessage"));
		request.setParameter("password", "password$123");
		request.setParameter("repeatPassword", "password$123");
		request.setParameter("domain", "domainTest");
		request.setParameter("firstClassDiscipline","12");
		request.setParameter("secondClassDiscipline","34");
		request.setParameter("emailToRegist", "wokao@123.com");
		mv=controller.stepTwo(request);
		Assert.assertEquals("configCareer", mv.getViewName());
		Assert.assertEquals(1, mv.getModel().get("uid"));
		Assert.assertEquals("账户创建成功", mv.getModel().get("message"));
		Assert.assertEquals("个人研究主页创建", mv.getModel().get("pageName"));
		Assert.assertEquals("domainTest", mv.getModel().get("domain"));
		
	}
	@Test
	public void test_stepThree(){
		request.setParameter("preOper","education");
		request.getSession().setAttribute("currentUser", new SimpleUser());
		ModelAndView mv=controller.stepThree(request);
		Assert.assertEquals("configPaper", mv.getViewName());
	}
}
