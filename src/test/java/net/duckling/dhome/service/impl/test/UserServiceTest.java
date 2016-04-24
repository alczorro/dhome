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
package net.duckling.dhome.service.impl.test;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.duckling.dhome.dao.IDetailedUserDAO;
import net.duckling.dhome.dao.IDisciplineDAO;
import net.duckling.dhome.dao.ISimpleUserDAO;
import net.duckling.dhome.dao.impl.MockDetailedUserDAO;
import net.duckling.dhome.dao.impl.MockDisciplineDAO;
import net.duckling.dhome.dao.impl.MockSimpleUserDAO;
import net.duckling.dhome.domain.people.DetailedUser;
import net.duckling.dhome.domain.people.Discipline;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.impl.UserService;
import net.duckling.dhome.service.impl.mock.MockCacheService;
import net.duckling.falcon.api.cache.ICacheService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserServiceTest extends TestCase{
	
	private ISimpleUserDAO suDAO;
	private IDetailedUserDAO duDAO;
	private IDisciplineDAO diDAO;
	private UserService us = null;
	private ICacheService cs = null;
	private String EMAIL = "abc@abc.com";
	private String ZHNAME = "ABC";
	private String ENNAME = "abc";
	private String BIRTHDAY = "1988-01-01";
	private String BLOGURL = "http://myblog.cn";
	private int FIRST_CLASS = 1;
	private int SECOND_CLASS = 2;
	private String GENDER = "male";
	private String INTRODUCTION = "introduction";
	private String WEIBOURL = "http://weibo.cn/mine";
	
	private int uid = 0;
	
	@Before
	public void setUp(){
		suDAO = new MockSimpleUserDAO();
		duDAO = new MockDetailedUserDAO();
		diDAO = new MockDisciplineDAO();
		cs = new MockCacheService();
		us = new UserService();
		us.setSimpleUserDAO(suDAO);
		us.setDetailedUserDAO(duDAO);
		us.setDisciplineDAO(diDAO);
		us.setCacheService(cs);
		createSU();
		createDU();
	}
	
	private void createSU(){
		SimpleUser su = new SimpleUser();
		su.setEmail(EMAIL);
		su.setZhName(ZHNAME);
		su.setEnName(ENNAME);
		uid = suDAO.registAccount(su);
	}
	
	private void createDU(){
		DetailedUser du = new DetailedUser();
		du.setUid(uid);
		du.setBirthday(BIRTHDAY);
		du.setBlogUrl(BLOGURL);
		du.setFirstClassDiscipline(FIRST_CLASS);
		du.setGender(GENDER);
		du.setIntroduction(INTRODUCTION);
		du.setSecondClassDiscipline(SECOND_CLASS);
		du.setWeiboUrl(WEIBOURL);
		duDAO.createDetailedUser(du);
	}
	
	@Test
	public void testGetSimpleUser(){
		SimpleUser su = us.getSimpleUser(EMAIL);
		Assert.assertEquals(EMAIL, su.getEmail());
		Assert.assertEquals(ZHNAME, su.getZhName());
		Assert.assertEquals(ENNAME, su.getEnName());
	}
	
	@Test
	public void testGetDetailedUser(){
		DetailedUser du = us.getDetailedUser(uid);
		Assert.assertNotNull(du);
		Assert.assertEquals(BIRTHDAY, du.getBirthday());
		Assert.assertEquals(BLOGURL, du.getBlogUrl());
		Assert.assertEquals(FIRST_CLASS, du.getFirstClassDiscipline());
		Assert.assertEquals(GENDER, du.getGender());
		Assert.assertEquals(INTRODUCTION, du.getIntroduction());
		Assert.assertEquals(SECOND_CLASS, du.getSecondClassDiscipline());
		Assert.assertEquals(WEIBOURL, du.getWeiboUrl());
	}
	
	@Test
	public void testUpdateSimpleUserByUid(){
		SimpleUser su = us.getSimpleUserByUid(uid);
		su.setSalutation("砖家");
		Assert.assertTrue(us.updateSimpleUserByUid(su));
		SimpleUser su2 = us.getSimpleUserByUid(uid);
		Assert.assertEquals("砖家", su2.getSalutation());
	}
	
	@Test
	public void testUpdateDetailedUserByUid(){
		DetailedUser du = us.getDetailedUser(uid);
		du.setIntroduction("innn");
		Assert.assertTrue(us.updateDetailedUserByUid(du));
		DetailedUser du2 = us.getDetailedUser(uid);
		Assert.assertEquals("innn", du2.getIntroduction());
	}
	
	@Test
	public void testGetSimpleUserByUid(){
		SimpleUser su = us.getSimpleUserByUid(uid);
		Assert.assertEquals(EMAIL, su.getEmail());
		Assert.assertEquals(ZHNAME, su.getZhName());
		Assert.assertEquals(ENNAME, su.getEnName());
	}
	
	@Test 
	public void testGetRootDiscipline(){
		List<Discipline> list = us.getRootDiscipline();
		Assert.assertNotNull(list);
	}
	
	@Test
	public void testGetChildDiscipline(){
		List<Discipline> list = us.getChildDiscipline(uid);
		Assert.assertNotNull(list);
	}
	
	@Test
	public void testGetDisciplineName(){
		Assert.assertEquals("name", us.getDisciplineName(uid));
	}
	
	@After
	public void tearDown(){
		suDAO = null;
		us = null;
	}
}
