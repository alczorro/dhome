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
import net.duckling.dhome.common.auth.sso.IAuthenticationService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.impl.mock.MockAuthenticationService;
import net.duckling.dhome.service.impl.mock.MockHomeService;
import net.duckling.dhome.service.impl.mock.MockUserService;
import net.duckling.dhome.testutil.SetFieldUtils;
import net.duckling.dhome.web.LoginController;

import org.springframework.web.servlet.ModelAndView;

import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

public class ForgetPasswordInHomePagedefs {

	private LoginController lc = new LoginController();
	
	private ModelAndView mv = null;
	
	@When("^盖中盖点击\"([^\"]*)\"链接$")
	public void click(String arg1){
		IAuthenticationService aus = new MockAuthenticationService();
		IUserService us = new MockUserService();
		IHomeService hs = new MockHomeService();
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("authenticationService", aus);
		param.put("userService", us);
		param.put("homeService", hs);
		SetFieldUtils.setValues(lc, param);
	    mv = lc.forgetPsw();
	}
	
	@Then("^页面转向\"([^\"]*)\"页面2$")
	public void redirect_to_findpsw(String arg1){
		Assert.assertNotNull(mv);
		Assert.assertEquals("umt/psw.jsp",mv.getViewName());
	}
}
