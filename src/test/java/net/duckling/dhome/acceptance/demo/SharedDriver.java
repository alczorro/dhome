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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import cucumber.annotation.After;
import cucumber.annotation.Before;
import cucumber.runtime.ScenarioResult;

/**
 * Example of a WebDriver implementation that has an underlying instance that is used for all scenarios and closed
 * when the JVM exits. This saves time. To prevent browser state from leaking between scenarios, cookies are deleted before
 * every scenario.
 * <p/>
 * As a bonus, screenshots are embedded into the report for each scenario. (This only works
 * if you're also using the HTML formatter).
 * <p/>
 * This class can be shared across step definitions via dependency injection.
 */
public class SharedDriver extends EventFiringWebDriver {
	private static WebDriver REAL_DRIVER = getInstance();

	private static ChromeDriver getInstance() {
		URL fileURL = SharedDriver.class.getClassLoader().getSystemResource("chromedriver");
		ChromeDriverService service = new ChromeDriverService.Builder()
				.usingDriverExecutable(new File(fileURL.getFile())).usingAnyFreePort()
				.build();
		return new ChromeDriver(service);
	}

	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				REAL_DRIVER.close();
			}
		});
	}

	public SharedDriver() {
		super(REAL_DRIVER);
	}

	@Before
	public void deleteAllCookies() {
		manage().deleteAllCookies();
	}

	@After
	public void embedScreenshot(ScenarioResult result) {
		try {
			byte[] screenshot = this.getScreenshotAs(OutputType.BYTES);
			result.embed(new ByteArrayInputStream(screenshot), "image/png");
		} catch (WebDriverException somePlatformsDontSupportScreenshots) {
			System.err.println(somePlatformsDontSupportScreenshots.getMessage());
		}
	}
}
