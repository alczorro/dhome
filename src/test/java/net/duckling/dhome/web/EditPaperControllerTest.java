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
import net.duckling.dhome.domain.people.Paper;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IDsnSearchService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IPaperService;
import net.duckling.dhome.service.impl.mock.MockDsnSearchService;
import net.duckling.dhome.service.impl.mock.MockHomeService;
import net.duckling.dhome.service.impl.mock.MockPaperService;
import net.duckling.dhome.service.impl.mock.MockUserService;
import net.duckling.dhome.testutil.SetFieldUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class EditPaperControllerTest {
	private MockHttpServletRequest request;
	private IPaperService ps;
	private IDsnSearchService dss;
	private EditPaperController epc;
	private IHomeService homeService;
	
	@Before
	public void setUp(){
		request = new MockHttpServletRequest();
		ps = new MockPaperService();
		dss = new MockDsnSearchService();
		homeService = new MockHomeService();
		epc = new EditPaperController();
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("dsnSearchService",dss);
		param.put("paperService", ps);
		param.put("dsnSearchService", dss);
		param.put("homeService", homeService);
		param.put("userService", new MockUserService());
		SetFieldUtils.setValues(epc, param);
		SimpleUser su = new SimpleUser();
		su.setId(108);
		request.getSession().setAttribute(Constants.CURRENT_USER, su);
		request.getSession().setAttribute(Constants.CURRENT_USER_DOMAIN, "yangxp");
	}
	
	@Test
	public void testDisplay(){
		int id = ps.create(createPaper());
		request.setParameter("paperId", ""+id);
		ModelAndView mv = epc.display(request,"");
		Assert.assertEquals("addSearchPaper", mv.getViewName());
	}
	
	@Test
	public void testSavePaperWithNew(){
		setRequestParamForPaper();
		ModelAndView mv = epc.save(request);
		RedirectView rv = (RedirectView)mv.getView();
		Paper paper = ps.getPaper(1);
		Assert.assertEquals("/people/yangxp/admin/paper", rv.getUrl());
		Assert.assertEquals("paper title", paper.getTitle());
		Assert.assertEquals("paper authors", paper.getAuthors());
		Assert.assertEquals(108, paper.getUid());
	}
	
	@Test
	public void testSavePaperWithExist(){
		int id = ps.create(createPaper());
		setRequestParamForPaper();
		request.setParameter("paperId", ""+id);
		ModelAndView mv = epc.save(request);
		RedirectView rv = (RedirectView)mv.getView();
		Paper paper = ps.getPaper(id);
		Assert.assertEquals("/people/yangxp/admin/paper", rv.getUrl());
		Assert.assertEquals("paper title", paper.getTitle());
		Assert.assertEquals("paper authors", paper.getAuthors());
		Assert.assertEquals(108, paper.getUid());
	}
	
	@Test
	public void testShowPaperSearch(){
		Assert.assertEquals("addSearchPaper", epc.showPaperSearch(request,"").getViewName()) ;
	}
	
	@Test
	public void testSaveSearchPaper(){
		request.setParameter("paperIds", "1,2");
		ModelAndView mv = epc.saveSearchPaper(request);
		RedirectView rv = (RedirectView)mv.getView();
		Assert.assertEquals("/people/yangxp/admin/paper", rv.getUrl());
		for(int i=1; i<=2; i++){
			Paper paper = ps.getPaper(i);
			Assert.assertEquals("yxp", paper.getAuthors());
			Assert.assertEquals(i, paper.getDsnPaperId());
			Assert.assertEquals("yxp", paper.getSource());
			Assert.assertEquals("paper yxp", paper.getTitle());
			Assert.assertEquals("vol.paper", paper.getVolumeIssue());
		}
	}
	
	@Test
	public void testShowImportBibtex(){
		Assert.assertEquals("addBibtexPaper", epc.showImportBibtex(request,"").getViewName());
	}
	
	@Test
	public void testUploadBibtexInIE(){
		//TODO:
	}
	
	@Test
	public void testUploadBibtexInNoIE(){
		//TODO:
	}
	
	@Test
	public void testSaveBibtextPaper(){
		JSONArray array = new JSONArray();
		array.add(createPaperJSON());
		request.setParameter("papers", array.toJSONString());
		ModelAndView mv = epc.saveBibPaper(request);
		Paper paper = ps.getPaper(1);
		RedirectView rv = (RedirectView)mv.getView();
		Assert.assertEquals("/people/yangxp/admin/paper", rv.getUrl());
		Assert.assertEquals(108, paper.getUid());
		Assert.assertEquals("yxp", paper.getAuthors());
		Assert.assertEquals("title", paper.getTitle());
	}
	
	@Test
	public void testUploadFulltextInIE(){
		//TODO:
	}
	
	@Test
	public void testUploadFulltextInNoIE(){
		//TODO:
	}
	
	@After
	public void tearDown(){
		request = null;
		ps = null;
		dss = null;
		epc = null;
	}
	
	private Paper createPaper(){
		return Paper.build(108, "title", "yxp", "yxp", "vol.1", 
				"2012", "0", "", "", "", "", "", 0, 0, 0,"");
	}
	
	private JSONObject createPaperJSON(){
		JSONObject obj = new JSONObject();
		obj.put("uid", "108");
		obj.put("title", "title");
		obj.put("authors", "yxp");
		obj.put("source", "yxp");
		obj.put("volumeIssue", "vol.1");
		return obj;
	}
	
	private void setRequestParamForPaper(){
		request.setParameter("title", "paper title");
		request.setParameter("authors", "paper authors");
		request.setParameter("volumeIssue", "vol.1");
		request.setParameter("source", "yxp");
		request.setParameter("paperURL", "http://mypaper.cn");
		request.setParameter("publishedTime", "2012");
		request.setParameter("localFulltextURL", "");
		request.setParameter("clbId", "0");
	}
}
