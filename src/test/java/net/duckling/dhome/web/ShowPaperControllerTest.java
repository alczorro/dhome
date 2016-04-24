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

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import net.duckling.dhome.common.auth.sso.Constants;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IDsnSearchService;
import net.duckling.dhome.service.IEducationService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInterestService;
import net.duckling.dhome.service.IPaperService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.IWorkService;
import net.duckling.dhome.service.impl.mock.MockDsnSearchService;
import net.duckling.dhome.service.impl.mock.MockHomeService;
import net.duckling.dhome.service.impl.mock.MockInterestService;
import net.duckling.dhome.service.impl.mock.MockPaperService;
import net.duckling.dhome.service.impl.mock.MockUserService;
import net.duckling.dhome.service.impl.mock.MockWorkService;
import net.duckling.dhome.service.impl.mock.StubInstitutionHomeService;
import net.duckling.dhome.testutil.SetFieldUtils;

import org.json.simple.JSONArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

public class ShowPaperControllerTest {
	
	private IPaperService ps;
	private IHomeService hs;
	private IDsnSearchService dsnService;
	private IUserService userService;
	private IInstitutionHomeService insService;
	private IWorkService workService;
	private IEducationService eduService;
	private IInterestService interestService;
	private ShowPaperController spc;
	private MockHttpServletRequest request;
	
	@Before
	public void setUp(){
		ps = new MockPaperService();
		hs = new MockHomeService();
		dsnService = new MockDsnSearchService();
		userService = new MockUserService();
		insService = new StubInstitutionHomeService();
		interestService = new MockInterestService();
		workService = new MockWorkService();
		eduService = new MockEducationService();
		spc = new ShowPaperController();
		request = new MockHttpServletRequest();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("paperService", ps);
		map.put("homeService", hs);
		map.put("dsnService", dsnService);
		map.put("workService", workService);
		map.put("eduService", eduService);
		map.put("institutionHomeService", insService);
		map.put("interestService", interestService);
		map.put("userService", userService);
		SetFieldUtils.setValues(spc, map);
	}
	
	@Test
	public void testDisplay(){
		ModelAndView mv = spc.display(request, "yxp1");
		Object obj = mv.getModel().get("papers");
		Assert.assertEquals((new JSONArray()).toJSONString(),obj.toString());
		Assert.assertEquals("theme_m_namecard_index/browsePaper", mv.getViewName());
	}
	
	@Test
	public void testDisplay2(){
		request.getSession().setAttribute(Constants.CURRENT_USER_DOMAIN, "yangxp");
		request.getSession().setAttribute(Constants.CURRENT_USER, new SimpleUser());
		ModelAndView mv = spc.display(request, "yxp");
		Assert.assertEquals("error", mv.getViewName());
		Assert.assertEquals("您查看的页面未找到！", mv.getModel().get("message").toString());
		Assert.assertEquals("people/yangxp/admin/p/index", mv.getModel().get("redirectURL").toString());
		Assert.assertEquals("个人首页", mv.getModel().get("urlDescription").toString());
	}
	
	@After
	public void tearDown(){
		ps = null;
		hs = null;
		spc = null;
	}
}
