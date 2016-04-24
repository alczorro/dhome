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
package net.duckling.dhome.common.util;

import static org.junit.Assert.assertEquals;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class UrlUtilTest {

	private MockHttpServletRequest request = new MockHttpServletRequest();
	
	@Before
	public void setUp(){
		request.setContextPath("/dhome");
		request.setServerName("localhost");
		request.setScheme("http");
		request.setServerPort(80);
	}

	@Test
	public void testGetAbsoluteURL() {
		request.setRequestURI("http://localhost/dhome");
		testWithPort(80,request,"http://localhost/dhome");
		testWithPort(443,request,"http://localhost/dhome");
		testWithPort(8080,request,"http://localhost:8080/dhome");
	}
	@Test
	public void test_canUse(){
		//测试模糊匹配
		String  str="xxxxdhome";
		Assert.assertFalse((UrlUtil.canUse(str)));
		str="dhomexxxx";
		Assert.assertFalse((UrlUtil.canUse(str)));
		str="xxxxdhomexxxx";
		Assert.assertFalse((UrlUtil.canUse(str)));
		//测试前缀匹配
		str="prexxxx";
		Assert.assertFalse((UrlUtil.canUse(str)));
		str="xxxxpre";
		Assert.assertTrue((UrlUtil.canUse(str)));
		str="xxxxprexxxx";
		Assert.assertTrue((UrlUtil.canUse(str)));
		//测试后置匹配
		str="subxxxx";
		Assert.assertTrue((UrlUtil.canUse(str)));
		str="xxxxsub";
		Assert.assertFalse((UrlUtil.canUse(str)));
		str="xxxxsubxxxx";
		Assert.assertTrue((UrlUtil.canUse(str)));
		//精确匹配
		str="p";
		Assert.assertFalse(UrlUtil.canUse(str));
		str="xxxpxxx";
		Assert.assertTrue(UrlUtil.canUse(str));
		str="xxxxp";
		Assert.assertTrue(UrlUtil.canUse(str));
		str="pxxxxxx";
		Assert.assertTrue(UrlUtil.canUse(str));
				
	}
	@Test
	public void testGetURLFromRequestURI(){
		String expectedURL = "http://localhost/dhome/people";
		request.setRequestURI(expectedURL);
		assertEquals(expectedURL,UrlUtil.getURLFromRequestURI(request));
		request.setRequestURI("/dhome/people");
		assertEquals(expectedURL,UrlUtil.getURLFromRequestURI(request));
		request.setRequestURI("dhome/people");
		assertEquals(expectedURL,UrlUtil.getURLFromRequestURI(request));
	}
	
	private void testWithPort(int port,MockHttpServletRequest r,String expected){
		request.setServerPort(port);
		assertEquals(expected,UrlUtil.getRootURL(r));
	}

}
