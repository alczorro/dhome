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
package net.duckling.dhome.acceptance;

import static org.junit.Assert.assertEquals;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;
import cucumber.demo.core.Hello;

public class RegistStepdefs {
    private String email;
    private String password;
    private String result;
    private Hello hello;

    @Given("^我的Email地址是\"([^\"]*)\"$")
    public void My_email_input(String email) {
    	hello = new Hello(email);
    	this.email = email;
    }
    
    @Given("^我的密码是\"(.*)\"")
    public void My_password_input(String password) {
    	this.password = password;
    }

    @When("^当我点击\"创建页面\"按钮$")
    public void I_ask_it_to_say_hi() {
    	result = hello.regist(email, password);
    }

    @Then("^我应该看到\"([^\"]*)\"$")
    public void it_should_answer_with(String expectedHi) {
        assertEquals(expectedHi, result);
    }
    
    @Then("^我跳转到\"([^\"]*)\"$")
    public void redirect_to(String expectedHi) {
        assertEquals(expectedHi, "新建页面");
    }
}
