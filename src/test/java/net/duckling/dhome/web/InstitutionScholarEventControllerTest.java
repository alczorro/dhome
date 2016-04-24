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
import net.duckling.dhome.common.auth.sso.Constants;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionPeopleService;
import net.duckling.dhome.service.IInstitutionScholarEventService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.impl.mock.MockUserService;
import net.duckling.dhome.service.impl.mock.StubInstitutionHomeService;
import net.duckling.dhome.service.impl.mock.StubInstitutionPeopleService;
import net.duckling.dhome.service.impl.mock.StubInstitutionScholarEventService;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

public class InstitutionScholarEventControllerTest {
	
	private InstitutionScholarEventController isec;
	private IInstitutionScholarEventService ises;
	private IInstitutionHomeService ihs;
	private IInstitutionPeopleService ips;
	private IUserService us;
	private MockHttpServletRequest request;

	@Before
	public void setUp(){
		request = new MockHttpServletRequest();
		ises = new StubInstitutionScholarEventService();
		ihs = new StubInstitutionHomeService();
		ips = new StubInstitutionPeopleService();
		us = new MockUserService();
		isec = new InstitutionScholarEventController();
		isec.setIhs(ihs);
		isec.setIses(ises);
		isec.setPeopleService(ips);
		isec.setUs(us);
		
		SimpleUser su = new SimpleUser();
		su.setId(108);
		request.getSession().setAttribute(Constants.CURRENT_USER, su);
	}
	
	@Test
	public void testDisplay(){
		ModelAndView mv = isec.display(request, "108");
		Assert.assertEquals("institution/instituteActivity", mv.getViewName());
		Assert.assertTrue((Boolean)mv.getModel().get("isMember"));
		InstitutionHome home = (InstitutionHome)mv.getModel().get("institution");
		Assert.assertEquals("test", home.getDomain());
	}
	
	@Test
	public void testEventAll(){
		JSONArray array = isec.eventAll(request, "12", 0, 0);
		Assert.assertEquals("[]", array.toJSONString());
	}
	
	@Test
	public void testEventAll2(){
		JSONArray array = isec.eventAll(request, "testJSON", 0, 0);
		Assert.assertNotNull(array.toJSONString());
	}
	
	@Test
	public void testEventUpcoming(){
		JSONArray array = isec.eventUpcoming(request, "12", 0, 0);
		Assert.assertEquals("[]", array.toJSONString());
	}
	
	@Test
	public void testEventOngoing(){
		JSONArray array = isec.eventOngoing(request, "12", 0, 0);
		Assert.assertEquals("[]", array.toJSONString());
	}
	
	@Test
	public void testEventExpired(){
		JSONArray array = isec.eventExpired(request, "12", 0, 0);
		Assert.assertEquals("[]", array.toJSONString());
	}
	
	@Test
	public void testAddScholarEvent(){
		
	}
	
	@Test
	public void testViewScholarEvent(){
		ModelAndView mv = isec.viewScholarEvent(request, "test", 12);
		Map<String, Object> map = mv.getModel();
		Assert.assertEquals("institution/instituteActivityDetail", mv.getViewName());
		Assert.assertEquals("test", map.get("creatorName"));
		Assert.assertTrue((Boolean)map.get("isMember"));
	}
	
	@Test
	public void testDeleteScholarEvent(){
		JSONObject obj = isec.deleteScholarEvent(request, "test", 12);
		Assert.assertEquals("true", obj.get("status"));
		Assert.assertEquals(12, obj.get("activityId"));
	}
	
	@Test
	public void testEditScholarEvent(){
		
	}
	
	@Test
	public void testSubmitScholarEvent(){
		
	}
	
	@After
	public void tearDown(){
		request = null;
		ises = null;
		ihs = null;
		ips = null;
		us = null;
		isec = null;
	}
}
