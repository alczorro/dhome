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

import java.util.List;

import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;
import cucumber.demo.core.ShoppingList;

public class ShoppingStepdefs {
    private final ShoppingList shoppingList = new ShoppingList();
    private StringBuilder printedList;

    @Given("^a shopping list:$")
    public void a_shopping_list(List<ShoppingItem> items) throws Throwable {
        for (ShoppingItem item : items) {
            shoppingList.addItem(item.name, item.count);
        }
    }

    @When("^I print that list$")
    public void I_print_that_list() throws Throwable {
        printedList = new StringBuilder();
        shoppingList.print(printedList);
    }

    @Then("^it should look like:$")
    public void it_should_look_like(String expected) throws Throwable {
        assertEquals(expected, printedList.toString());
    }

    // When converting tables to a List of objects it's usually better to
    // use classes that are only used in test (not in production). This
    // reduces coupling between scenarios and domain and gives you more control.
    public static class ShoppingItem {
        private String name;
        private Integer count;
    }
}
