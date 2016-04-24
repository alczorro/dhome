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
import net.duckling.dhome.domain.people.Education;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IRegistService;
import net.duckling.dhome.testutil.SetFieldUtils;
import net.duckling.dhome.web.QuickRegistController;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en_au.When;

/**
 * Scenario: 研究单位设置成功 Given 小黄瓜选择的学历是"博士" Given 小黄瓜输入的研究机构名称是"北京大学" Given
 * 小黄瓜输入的院系是"计算机学院" When 小黄瓜点击"保存&下一步"按钮 Then 页面跳转到设置向导第二步
 * */
public class EducationSetStepdefs {
	private MockHttpServletRequest request = new MockHttpServletRequest();
	private QuickRegistController controller = new QuickRegistController();
	private IMocksControl control = EasyMock.createControl();
	private IRegistService service = control.createMock(IRegistService.class);
	private Education edu = new Education();

	private ModelAndView mv = null;

	@Given("^小黄瓜选择的学历是\"([^\"]*)\"$")
	public void inputDegree(String degree) throws Throwable {
		request.setParameter("preOper", "education");
		request.setParameter("degree", degree);
		edu.setDegree(degree);
	}

	@Given("^小黄瓜输入的研究机构名称是\"([^\"]*)\"$")
	public void inputInstitution(String institution) throws Throwable {
		request.setParameter("institution", institution);
	}

	@Given("^小黄瓜输入的院系是\"([^\"]*)\"$")
	public void inputDepartment(String department) throws Throwable {
		request.setParameter("department", department);
		SimpleUser user=new SimpleUser();
		user.setId(99);
		request.getSession().setAttribute("currentUser", user);
		edu.setDepartment(department);
		edu.setUid(user.getId());
	}

	@When("^小黄瓜点击\"([^\"]*)\"按钮$")
	public void clickButton(String arg1) throws Throwable {
		String institution=request.getParameter("institution");
		EasyMock.expect(service.createEducation(edu.getUid(),edu.getDegree(),edu.getDepartment(), institution)).andReturn(10);
		control.replay();
		SetFieldUtils.setValue(controller, "regist", service);
		mv = controller.stepThree(request);
	}

	@Then("^页面跳转到设置向导第三步$")
	public void jump() throws Throwable {
		Assert.assertEquals("configPaper", mv.getViewName());
	}

}
