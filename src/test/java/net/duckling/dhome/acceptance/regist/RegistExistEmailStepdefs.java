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
package net.duckling.dhome.acceptance.regist;

import java.io.UnsupportedEncodingException;

import junit.framework.Assert;
import net.duckling.dhome.service.IRegistService;
import net.duckling.dhome.testutil.SetFieldUtils;
import net.duckling.dhome.web.QuickRegistController;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import cucumber.annotation.en.But;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;


/**
 *    Scenario: 注册邮箱地址已存在
 *    Given 小红的输入邮箱地址是"liming@163.com"
      But  小红的输入邮箱地址已经被注册
      When 小红点击"开始创建主页"按钮
      Then 小红看到提示信息"邮件地址已存在，请用其它邮件地址注册；如果您要登录，请进入登陆页面"
 * 
 * @author clive
 */

public class RegistExistEmailStepdefs {
	private IMocksControl control = EasyMock.createControl();
	private QuickRegistController controller = new QuickRegistController();
	private MockHttpServletRequest request = new MockHttpServletRequest();
	private MockHttpServletResponse response = new MockHttpServletResponse();
	private IRegistService service=control.createMock(IRegistService.class);
	
	
	@Given("^小红的输入邮箱地址是\"([^\"]*)\"$")
	public void inputEmail(String email){
		request.setParameter("email", email);
	}
	
	@But("^小红的输入邮箱地址已经被注册$")
	public void butEmailUsed(){
		EasyMock.expect(service.isEmailUsed(request.getParameter("email"))).andReturn(true);
		control.replay();
				
	}
	
	@When("^小红点击\"([^\"]*)\"按钮$")
	public void clickButton(String msg){
		SetFieldUtils.setValue(controller, "registService", service);
		controller.isEmailUsed(request);
	}
	
	@Then("^小红看到提示信息\"([^\"]*)\"$")
	public void seeAlert(String expected) throws UnsupportedEncodingException{
		String str=response.getContentAsString();
		Assert.assertEquals("false",str);
		System.out.println(expected);
	}

}
