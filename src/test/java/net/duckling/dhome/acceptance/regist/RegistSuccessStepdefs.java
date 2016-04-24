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

import junit.framework.Assert;
import net.duckling.dhome.service.IRegistService;
import net.duckling.dhome.testutil.SetFieldUtils;
import net.duckling.dhome.web.QuickRegistController;

import org.easymock.EasyMock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;


/**
 *   Scenario: 首页快速注册成功
      Given 小明的输入真实姓名是"小明"
      And 小明的输入邮箱地址是"zhaojuan@cnic.cn"
      When 小明点击"开始创建主页"按钮
      Then 小明跳到了"个人研究主页创建"页面
 * 
 *
 */
public class RegistSuccessStepdefs {
	
	private QuickRegistController controller = new QuickRegistController();
	private MockHttpServletRequest request = new MockHttpServletRequest();
	
	private IRegistService service=EasyMock.createControl().createMock(IRegistService.class);
	private String pageName;
	
	@Given("^小明的输入真实姓名是\"([^\"]*)\"$")
	public void inputZhName(String arg1) throws Throwable {
	    request.setParameter("zhName", arg1);
	}

	@Given("^小明的输入邮箱地址是\"([^\"]*)\"$")
	public void inputEmail(String arg1) throws Throwable {
	    request.setParameter("emailToRegist", arg1);
	}

	@When("^小明点击\"([^\"]*)\"按钮$")
	public void clickButton(String arg1) throws Throwable {
		SetFieldUtils.setValue(controller, "registService", service);
		ModelAndView mv = controller.stepOne(request);
		pageName = (String)mv.getModel().get("pageName");
	}

	@Then("^小明跳到了\"([^\"]*)\"页面$")
	public void jump(String expected) throws Throwable {
		Assert.assertEquals(expected,pageName);
	}

}
