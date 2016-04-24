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
package net.duckling.dhome.web;

import java.util.Map;

import junit.framework.Assert;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionPaperService;
import net.duckling.dhome.service.IInstitutionPeopleService;
import net.duckling.dhome.service.IInstitutionPubStatisticService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.impl.mock.MockUserService;
import net.duckling.dhome.service.impl.mock.StubInstitutionHomeService;
import net.duckling.dhome.service.impl.mock.StubInstitutionPaperService;
import net.duckling.dhome.service.impl.mock.StubInstitutionPeopleService;
import net.duckling.dhome.service.impl.mock.StubInstitutionPubStatisticService;

import org.json.simple.JSONArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

public class InstitutionPaperControllerTest {
	
	private InstitutionPaperController ipc = null;
	private IInstitutionPaperService ipapers = null;
	private IInstitutionHomeService ihs = null;
	private IUserService us = null;
	private IInstitutionPubStatisticService ipss = null;
	private IInstitutionPeopleService ipeoples = null;
	private MockHttpServletRequest request = null;
	
	@Before
	public void setUp(){
		ipc = new InstitutionPaperController();
		ipapers = new StubInstitutionPaperService();
		ihs = new StubInstitutionHomeService();
		us = new MockUserService();
		ipss = new StubInstitutionPubStatisticService();
		ipeoples = new StubInstitutionPeopleService();
		request = new MockHttpServletRequest();
		ipc.setIhs(ihs);
		ipc.setIps(ipapers);
		ipc.setIpss(ipss);
		ipc.setPeopleService(ipeoples);
		ipc.setUs(us);
	}
	
	@Test
	public void testDisplay(){
		ModelAndView mv = ipc.display(request, "test");
		Map<String,Object> map = mv.getModel();
		Assert.assertEquals("institution/institutePaper", mv.getViewName());
		Assert.assertEquals(0, map.get("amount"));
		Assert.assertEquals(0, map.get("gindex"));
		Assert.assertEquals(0, map.get("hindex"));
		Assert.assertEquals(0, map.get("citedNum"));
		Assert.assertFalse((Boolean)map.get("isMember"));
	}
	
	@Test
	public void testPaperCite(){
		JSONArray array = ipc.paperCite(request,"test", 0, 0);
		Assert.assertEquals("[{\"summary\":null,\"id\":0,\"authors\":\"test\",\"downloadURL\":\"\",\"title\":\"test Paper\"" +
				",\"source\":null,\"publishedTime\":null,\"pages\":null,\"volumeIssue\":null,\"paperURL\":\"\"}]", array.toJSONString());
	}
	
	@Test
	public void testPaperYear(){
		JSONArray array = ipc.paperYear(request,"test", "2012",  0, 0);
		Assert.assertEquals("[]", array.toJSONString());
	}
	
	@Test
	public void testPaperAuthor(){
		JSONArray array = ipc.paperAuthor(request,"test", 12, 0, 0);
		Assert.assertEquals("[]", array.toJSONString());
	}
	
	@After
	public void tearDown(){
		ipc = null;
		ipapers = null;
		ihs = null;
		us = null;
		ipss = null;
		ipeoples = null;
		request = null;
	}
}
