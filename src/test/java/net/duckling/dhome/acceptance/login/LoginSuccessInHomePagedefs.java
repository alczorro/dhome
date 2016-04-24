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
package net.duckling.dhome.acceptance.login;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import net.duckling.dhome.common.auth.sso.Constants;
import net.duckling.dhome.common.auth.sso.IAuthenticationService;
import net.duckling.dhome.common.config.AppConfig;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.impl.mock.MockAuthenticationService;
import net.duckling.dhome.service.impl.mock.MockHomeService;
import net.duckling.dhome.service.impl.mock.MockUserService;
import net.duckling.dhome.testutil.SetFieldUtils;
import net.duckling.dhome.web.LoginController;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.mock.web.MockHttpServletRequest;

import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

public class LoginSuccessInHomePagedefs {
	private IMocksControl control = EasyMock.createControl();
	private String email;
	private IAuthenticationService aus = new MockAuthenticationService();
	private IUserService us = new MockUserService();
	private IHomeService hs = new MockHomeService();
	private MockHttpServletRequest request = new MockHttpServletRequest();
	private LoginController lc = new LoginController();
	
	@Given("^黄盖在dhome的注册邮箱是\"([^\"]*)\"$")
	public void email_in_dhome(String email) {
	    request.setParameter("email", email);
	    this.email = email;
	}

	@Given("^黄盖在dhome的登录密码是\"([^\"]*)\"$")
	public void password_of_email_in_dhome(String password){
	    request.setParameter("password", password);
	}

	@When("^黄盖点击\"([^\"]*)\"按钮$")
	public void click(String button){
		AppConfig config = control.createMock(AppConfig.class);
	    EasyMock.expect(config.getSessionExperied()).andStubReturn(100);
	    control.replay();
	    Map<String,Object> param=new HashMap<String,Object>();
		param.put("authenticationService", aus);
		param.put("userService", us);
		param.put("homeService", hs);
		SetFieldUtils.setValues(lc, param);
		lc.login(request);
	}

	@Then("^页面转向\"([^\"]*)\"页面$")
	public void redirect(String page){
		Assert.assertEquals(us.getSimpleUser(email), request.getSession().getAttribute(Constants.CURRENT_USER));
	}
}
