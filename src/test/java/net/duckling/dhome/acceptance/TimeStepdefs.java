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

import java.util.Calendar;

import com.mdimension.jchronic.Chronic;
import com.mdimension.jchronic.Options;
import com.mdimension.jchronic.utils.Span;

import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.api.Transform;
import cucumber.api.Transformer;

public class TimeStepdefs {
    private static Options options = new Options();
    private Calendar laundryDate;

    @Given("^today is \"([^\"]*)\"$")
    public void today_is(Calendar today) throws Throwable {
        options.setNow(today);
    }

    @Given("^I did laundry (.*)")
    public void I_did_laundry_time_ago(@Transform(ChronicConverter.class) Calendar laundryDate) throws Throwable {
        this.laundryDate = laundryDate;
    }

    @Then("^my laundry day must have been \"([^\"]*)\"$")
    public void my_laundry_day_must_have_been(Calendar day) throws Throwable {
        assertEquals(day, laundryDate);
    }

    public static class ChronicConverter extends Transformer<Calendar> {
        @Override
        public Calendar transform(String value) {
            Span span = Chronic.parse(value, options);
            return span.getEndCalendar();
        }
    }
}
