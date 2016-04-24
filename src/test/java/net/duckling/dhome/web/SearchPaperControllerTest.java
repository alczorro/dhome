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
import net.duckling.dhome.service.IPaperService;
import net.duckling.dhome.service.impl.mock.MockDsnSearchService;
import net.duckling.dhome.service.impl.mock.MockPaperService;
import net.duckling.dhome.testutil.SetFieldUtils;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.json.simple.JSONArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;

public class SearchPaperControllerTest {
	private IMocksControl mc = EasyMock.createControl();
	private MockHttpServletRequest request;
	private SearchPaperController spc;
	private IPaperService ps;
	private IDsnSearchService dss;
	private Model model;
	
	@Before
	public void setUp(){
		request = new MockHttpServletRequest();
		spc = new SearchPaperController();
		ps = new MockPaperService();
		dss = new MockDsnSearchService();
		model = new MockModel();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("dsnSearchService", dss);
		map.put("paperService",ps);
		SetFieldUtils.setValues(spc, map);
		SimpleUser su = new SimpleUser();
		su.setId(108);
		request.getSession().setAttribute(Constants.CURRENT_USER, su);
	}
	
	@Test
	public void testInitSearch(){
		JSONArray array = dss.initQuery(108, 0, 10, null);
		spc.search(request, model, 0, 10);
		Map<String, Object> result = model.asMap();
		Assert.assertEquals(array.toJSONString(), result.get("result").toString());
		Assert.assertEquals(result.get("searchKeywords").toString(), dss.getUserNameString(108));
	}
	
	@Test
	public void testSearch(){
		request.setParameter("keyword", "yxp");
		JSONArray array = dss.query(108, "yxp", 0, 10, null);
		spc.search(request, model, 0, 10);
		Map<String, Object> result = model.asMap();
		Assert.assertEquals(array.toJSONString(), result.get("result").toString());
		Assert.assertEquals(result.get("searchKeywords").toString(), "yxp");
	}
	
	@After
	public void tearDown(){
		request = null;
		spc = null;
		ps = null;
		dss = null;
	}
}
