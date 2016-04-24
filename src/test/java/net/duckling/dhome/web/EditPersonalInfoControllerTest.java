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

import net.duckling.dhome.common.auth.sso.Constants;
import net.duckling.dhome.common.util.UrlUtil;
import net.duckling.dhome.domain.people.DetailedUser;
import net.duckling.dhome.domain.people.Education;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.Work;
import net.duckling.dhome.service.IEducationService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInterestService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.IWorkService;
import net.duckling.dhome.service.impl.mock.MockHomeService;
import net.duckling.dhome.service.impl.mock.MockInterestService;
import net.duckling.dhome.service.impl.mock.MockUserService;
import net.duckling.dhome.service.impl.mock.MockWorkService;
import net.duckling.dhome.testutil.SetFieldUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class EditPersonalInfoControllerTest {
	private MockHttpServletRequest request;
	private IHomeService hs;
	private IUserService us;
	private IWorkService ws;
	private IEducationService es;
	private IInterestService is;
	private EditPersonalInfoController epic;
	
	@Before 
	public void setUp(){
		request = new MockHttpServletRequest();
		hs = new MockHomeService();
		us = new MockUserService();
		ws = new MockWorkService();
		es = new MockEducationService();
		is = new MockInterestService();
		SimpleUser su = new SimpleUser();
		su.setId(108);
		request.getSession().setAttribute(Constants.CURRENT_USER, su);
		epic = new EditPersonalInfoController();
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("homeService", hs);
		param.put("workService", ws);
		param.put("eduService",es);
		param.put("userService", us);
		param.put("interestService", is);
		SetFieldUtils.setValues(epic, param);
	}
	
	@Test
	public void test(){
		ModelAndView mv = epic.display(request);
		RedirectView rv = (RedirectView)mv.getView();
		String expectUrl = UrlUtil.getRootURL(request)+"/people/yangxp/admin/personal/baseinfo";
		Assert.assertEquals(expectUrl,rv.getUrl());
	}
	
	@Test
	public void testSaveBaseInfo(){
		request.setParameter("zhName", "杨小澎");
		request.setParameter("firstClassDiscipline", "1");
		request.setParameter("secondClassDiscipline", "11");
		request.setParameter("introduction", "yangxp introduction");
		ModelAndView mv = epic.saveBaseInfo(request);
		RedirectView rv = (RedirectView)mv.getView();
		SimpleUser resultSu = us.getSimpleUserByUid(108);
		DetailedUser resultDu = us.getDetailedUser(108);
		Assert.assertEquals("/people/yangxp/admin/personal/baseinfo", rv.getUrl());
		Assert.assertEquals("杨小澎",resultSu.getZhName());
		Assert.assertEquals("yangxiaopeng;yxp", resultSu.getPinyin());
		Assert.assertEquals(1, resultDu.getFirstClassDiscipline());
		Assert.assertEquals(11, resultDu.getSecondClassDiscipline());
		Assert.assertEquals("yangxp introduction", resultDu.getIntroduction());
	}
	
	@Test
	public void testSaveWorkWithNew(){
		setRequestParamOfWork();
		ModelAndView mv = epic.saveWork(request);
		RedirectView rv = (RedirectView)mv.getView();
		Work work = ws.getWork(1);
		Assert.assertEquals("/people/yangxp/admin/personal/work", rv.getUrl());
		Assert.assertEquals(0, work.getInstitutionId());
		Assert.assertEquals("CS", work.getDepartment());
		Assert.assertEquals("PhD", work.getPosition());
		Assert.assertEquals("2012-01-01", work.getBeginTime().toString());
		Assert.assertEquals("2012-02-01", work.getEndTime().toString());
	}
	
	@Test
	public void testSaveWorkWithExist(){
		int id = ws.createWork(createWork());
		setRequestParamOfWork();
		request.setParameter("id", ""+id);
		ModelAndView mv = epic.saveWork(request);
		RedirectView rv = (RedirectView)mv.getView();
		Work work = ws.getWork(id);
		Assert.assertEquals("/people/yangxp/admin/personal/work", rv.getUrl());
		Assert.assertEquals(0, work.getInstitutionId());
		Assert.assertEquals("CS", work.getDepartment());
		Assert.assertEquals("PhD", work.getPosition());
		Assert.assertEquals("2012-01-01", work.getBeginTime().toString());
		Assert.assertEquals("2012-02-01", work.getEndTime().toString());
	}
	
	@Test
	public void testDeleteWork(){
		int id = ws.createWork(createWork());
		request.setParameter("id", ""+id);
		ModelAndView mv = epic.deleteWork(request);
		RedirectView rv = (RedirectView)mv.getView();
		Work work = ws.getWork(id);
		Assert.assertEquals("/people/yangxp/admin/personal/work", rv.getUrl());
		Assert.assertNull(work);
	}
	
	@Test
	public void testSaveEducationWithNew(){
		setRequestParamOfEducation();
		ModelAndView mv = epic.saveEducation(request);
		RedirectView rv = (RedirectView)mv.getView();
		Education edu = es.getEducation(1);
		Assert.assertEquals("/people/yangxp/admin/personal/education", rv.getUrl());
		Assert.assertEquals(0, edu.getInsitutionId());
		Assert.assertEquals("CS", edu.getDepartment());
		Assert.assertEquals("PhD", edu.getDegree());
		Assert.assertEquals("2012-01-01", edu.getBeginTime().toString());
		Assert.assertEquals("2012-02-01", edu.getEndTime().toString());
	}
	
	@Test
	public void testSaveEducationWithExist(){
		int id = es.createEducation(createEdu());
		setRequestParamOfEducation();
		request.setParameter("id", ""+id);
		ModelAndView mv = epic.saveEducation(request);
		RedirectView rv = (RedirectView)mv.getView();
		Education edu = es.getEducation(1);
		Assert.assertEquals("/people/yangxp/admin/personal/education", rv.getUrl());
		Assert.assertEquals(0, edu.getInsitutionId());
		Assert.assertEquals("CS", edu.getDepartment());
		Assert.assertEquals("PhD", edu.getDegree());
		Assert.assertEquals("2012-01-01", edu.getBeginTime().toString());
		Assert.assertEquals("2012-02-01", edu.getEndTime().toString());
	}
	
	@Test
	public void testDeleteEducation(){
		int id = es.createEducation(createEdu());
		request.setParameter("id", ""+id);
		ModelAndView mv = epic.deleteEducation(request);
		RedirectView rv = (RedirectView)mv.getView();
		Education edu = es.getEducation(1);
		Assert.assertEquals("/people/yangxp/admin/personal/education", rv.getUrl());
		Assert.assertNull(edu);
	}
	
	@After
	public void tearDown(){
		request = null;
		hs = null;
		us = null;
		ws = null;
		es = null;
	}
	
	private Work createWork(){
		Work work = new Work();
		work.setDepartment("cs");
		work.setPosition("zy");
		work.setInstitutionId(1);
		work.setBeginTime(java.sql.Date.valueOf("2012-02-02"));
		work.setEndTime(java.sql.Date.valueOf("2012-03-01"));
		return work;
	}
	
	private Education createEdu(){
		Education edu = new Education();
		edu.setDepartment("cs");
		edu.setDegree("ms");
		edu.setInsitutionId(1);
		edu.setBeginTime(java.sql.Date.valueOf("2012-02-02"));
		edu.setEndTime(java.sql.Date.valueOf("2012-03-01"));
		return edu;
	}
	
	private void setRequestParamOfWork(){
		request.setParameter("institutionZhName", "北京大学校");
		request.setParameter("department", "CS");
		request.setParameter("position", "PhD");
		request.setParameter("beginTime", "2012-01-01");
		request.setParameter("endTime", "2012-02-01");
	}
	
	private void setRequestParamOfEducation(){
		request.setParameter("institutionZhName", "北京大学校");
		request.setParameter("department", "CS");
		request.setParameter("degree", "PhD");
		request.setParameter("beginTime", "2012-01-01");
		request.setParameter("endTime", "2012-02-01");
	}
}
