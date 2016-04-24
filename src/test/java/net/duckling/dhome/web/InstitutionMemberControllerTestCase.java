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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import junit.framework.Assert;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionPaperService;
import net.duckling.dhome.service.IInstitutionPeopleService;
import net.duckling.dhome.service.impl.mock.StubInstitutionHomeService;
import net.duckling.dhome.service.impl.mock.StubInstitutionPaperService;
import net.duckling.dhome.service.impl.mock.StubInstitutionPeopleService;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lvly
 * @since 2012-9-21
 */
public class InstitutionMemberControllerTestCase {
	HttpServletRequest request = new MockHttpServletRequest();
	InstitutionMemberController controller = new InstitutionMemberController();
	IInstitutionHomeService homeService = new StubInstitutionHomeService();
	IInstitutionPaperService paperService = new StubInstitutionPaperService();
	IInstitutionPeopleService peopleService = new StubInstitutionPeopleService();

	@Before
	public void before() throws Exception {
		controller.setHomeService(homeService);
		controller.setPeopleService(peopleService);

	}

	@Test
	public void test() {
		ModelAndView mv = controller.index(request, "test");

		// --------------------
		Map<String, Object> map = mv.getModel();
		InstitutionHome expectHome = new InstitutionHome();
		expectHome.setId(12);
		expectHome.setName("测试用机构");
		expectHome.setDomain("test");
		InstitutionHome actualHome = (InstitutionHome) map.get("home");
		Assert.assertEquals(expectHome.getId(), actualHome.getId());
		Assert.assertEquals(expectHome.getName(), actualHome.getName());
		Assert.assertEquals(expectHome.getDomain(), actualHome.getDomain());

		Assert.assertEquals("institution/instituteMember", mv.getViewName());
	}
}
