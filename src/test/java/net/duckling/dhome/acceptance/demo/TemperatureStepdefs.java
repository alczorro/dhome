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
package net.duckling.dhome.acceptance.demo;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

public class TemperatureStepdefs {
    private final WebDriver webDriver;

    public TemperatureStepdefs(SharedDriver webDriver) {
        this.webDriver = webDriver;
    }

    @When("^I enter (.+) Celcius$")
    public void i_enter_Celcius(double celcius) {
        webDriver.findElement(By.id("celcius")).sendKeys(String.valueOf(celcius));
    }

    @Then("^I should see (.+) Fahrenheit$")
    public void i_should_see_Fahrenheit(double fahrenheit) {
        assertEquals(String.valueOf(fahrenheit), webDriver.findElement(By.id("fahrenheit")).getAttribute("value"));
    }
}
