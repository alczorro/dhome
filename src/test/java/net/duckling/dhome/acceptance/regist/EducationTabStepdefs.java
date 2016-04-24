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
import net.duckling.dhome.web.QuickRegistController;

import org.springframework.mock.web.MockHttpServletRequest;

import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en_au.When;

public class EducationTabStepdefs {
	private MockHttpServletRequest request = new MockHttpServletRequest();
	private QuickRegistController controller = new QuickRegistController();
	private String mv=null;

		@Given("^小二是在读学生$")
		public void clickImStudent() throws Throwable {
			
		}
		
		@When("^小二点击\"([^\"]*)\"按钮$")
		public void clickImStudent(String button) throws Throwable {
			mv=controller.stepThreeEdu();
		}
		
		@Then("^页面切换至学生的研究单位区域$")
		public void 页面切换至学生的研究单位区域() throws Throwable {
			Assert.assertEquals("configEducation", mv);
		}


}
