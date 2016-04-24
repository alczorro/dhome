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

import java.util.List;

import junit.framework.Assert;
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
import cucumber.table.DataTable;

/***
 * 个人主页设置向导-研究单位设置-工作人员
 * 
 * 小明是工作人员，在这步设置单位和职称信息。
 * Scenario: 研究单位设置成功 
 * Given 小明输入的研究机构名称是"中国科学院计算机网络信息中心"
 * Given 职称列表 |助研| |副研究员| |研究员| |讲师| |副教授| |教授| 
 * And 小明输入的部门是"协同中心" 
 * And小明选择了职称是"助研" 
 * When 小明点击"保存&下一步"按钮 Then 页面跳转到设置向导第二步
 * 
 * @author lvly
 * @since 2012-08-30
 * */
public class InstitutionStepdefs {
	private IMocksControl control = EasyMock.createControl();
	private QuickRegistController controller = new QuickRegistController();
	private MockHttpServletRequest request = new MockHttpServletRequest();
	private IRegistService service=control.createMock(IRegistService.class);
	
	private List<String> positionList;
	@Given("^小明输入的研究机构名称是\"([^\"]*)\"$")
	public void inputInstitution(String arg1) throws Throwable {
		request.setParameter("institution", arg1);
		request.setParameter("preOper", "work");
		SimpleUser user=new SimpleUser();
		user.setId(-1);
		request.getSession().setAttribute("currentUser", user);
	}

	@Given("^职称列表$")
	public void inputPosition(DataTable arg1) throws Throwable {
		positionList=arg1.flatten();
	}

	@Given("^小明输入的部门是\"([^\"]*)\"$")
	public void inputDepartment(String arg1) throws Throwable {
		request.setParameter("department", arg1);
	}

	@Given("^小明选择了职称是\"([^\"]*)\"$")
	public void input(String arg1) throws Throwable {
		Assert.assertEquals(true, positionList.contains(arg1));
		request.setParameter("position", arg1);
	}

	@Then("^页面跳转到设置向导第二步$")
	public void jmp() throws Throwable {
		EasyMock.expect(service.createWork(
				((SimpleUser)request.getSession().getAttribute("currentUser")).getId(),
				request.getParameter("position"),
				request.getParameter("department"),
				request.getParameter("institution"))).andReturn(10);
		control.replay();
		SetFieldUtils.setValue(controller, "registService", service);
		ModelAndView mv=controller.stepThree(request);
		Assert.assertEquals("configPaper",mv.getViewName());
	}

}
