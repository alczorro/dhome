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
/**
 * 
 */
package net.duckling.dhome.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import junit.framework.Assert;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionPaperService;
import net.duckling.dhome.service.IInstitutionPeopleService;
import net.duckling.dhome.service.IInstitutionScholarEventService;
import net.duckling.dhome.service.impl.mock.StubInstitutionHomeService;
import net.duckling.dhome.service.impl.mock.StubInstitutionPaperService;
import net.duckling.dhome.service.impl.mock.StubInstitutionPeopleService;
import net.duckling.dhome.service.impl.mock.StubInstitutionScholarEventService;
import net.duckling.dhome.testutil.SetFieldUtils;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lvly
 * @since 2012-9-21
 */
public class InstitutionIndexControllerTestCase {
	HttpServletRequest request=new MockHttpServletRequest();
	InstitutionIndexController controller=new InstitutionIndexController();
	IInstitutionHomeService homeService=new StubInstitutionHomeService();
	IInstitutionPaperService paperService=new StubInstitutionPaperService();
	IInstitutionPeopleService peopleService=new StubInstitutionPeopleService();
	IInstitutionScholarEventService eventService=new StubInstitutionScholarEventService();
	@Before
	public void before() throws Exception {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("homeService", homeService);
		param.put("paperService", paperService);
		param.put("peopleService", peopleService);
		param.put("ises",eventService);
		SetFieldUtils.setValues(controller, param);
	}
	@Test
	public void test_index(){
		ModelAndView mv=controller.index("test", request);
		//--------------------
		Map<String,Object> map=mv.getModel();
		InstitutionHome expectHome=new InstitutionHome();
		expectHome.setId(12);
		expectHome.setName("测试用机构");
		expectHome.setDomain("test");
		InstitutionHome actualHome=(InstitutionHome)map.get("home");
		Assert.assertEquals(expectHome.getId(), actualHome.getId());
		Assert.assertEquals(expectHome.getName(), actualHome.getName());
		Assert.assertEquals(expectHome.getDomain(), actualHome.getDomain());
		
		Assert.assertEquals("institution/instituteIndex", mv.getViewName());
		
	}
	@Test
	public void test_edit(){
		ModelAndView mv=controller.edit("test");
		
		Map<String,Object> map=mv.getModel();
		InstitutionHome expectHome=new InstitutionHome();
		expectHome.setId(12);
		expectHome.setName("测试用机构");
		expectHome.setDomain("test");
		InstitutionHome actualHome=(InstitutionHome)map.get("home");
		Assert.assertEquals(expectHome.getId(), actualHome.getId());
		Assert.assertEquals(expectHome.getName(), actualHome.getName());
		Assert.assertEquals(expectHome.getDomain(), actualHome.getDomain());
		Assert.assertEquals("institution/instituteConfig", mv.getViewName());
	}
}
