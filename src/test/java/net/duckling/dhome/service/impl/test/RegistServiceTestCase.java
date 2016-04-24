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

import java.util.HashMap;
import java.util.Map;

import net.duckling.dhome.common.config.AppConfig;
import net.duckling.dhome.common.umt.UmtClient;
import net.duckling.dhome.dao.IDetailedUserDAO;
import net.duckling.dhome.dao.IDisciplineDAO;
import net.duckling.dhome.dao.IEducationDAO;
import net.duckling.dhome.dao.IHomeDAO;
import net.duckling.dhome.dao.IInstitutionDAO;
import net.duckling.dhome.dao.ISimpleUserDAO;
import net.duckling.dhome.dao.IWorkDAO;
import net.duckling.dhome.dao.impl.StubEducationDAO;
import net.duckling.dhome.dao.impl.StubInstitutionDAO;
import net.duckling.dhome.dao.impl.StubWorkDAO;
import net.duckling.dhome.domain.people.DetailedUser;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.impl.RegistService;
import net.duckling.dhome.service.impl.mock.StubInstitutionHomeService;
import net.duckling.dhome.testutil.SetFieldUtils;
import net.duckling.falcon.api.cache.ICacheService;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**测试RegistService的每个公共方法
 * @author lvly
 * @since 2012-08-13
 * */
public class RegistServiceTestCase {
	RegistService service=new RegistService();
	IMocksControl control = EasyMock.createControl();
	UmtClient umt=control.createMock(UmtClient.class);
	IDetailedUserDAO dDAO=control.createMock(IDetailedUserDAO.class);
	ISimpleUserDAO sDAO=control.createMock(ISimpleUserDAO.class);
	IHomeDAO hDAO=control.createMock(IHomeDAO.class);
	ICacheService cs = control.createMock(ICacheService.class);
	@Before
	public void init(){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("simpleUserDAO", sDAO);
		map.put("detailedUserDAO", dDAO);
		map.put("educationDAO", new StubEducationDAO());
		map.put("workDAO", new StubWorkDAO());
		map.put("institutionDAO", new StubInstitutionDAO());
		//map.put("config", value);
		map.put("homeDAO", hDAO);
		//map.put("disciplineDAO", value);
		map.put("cacheService", cs);
		map.put("institutionHomeService", new StubInstitutionHomeService());
		map.put("umt", umt);
		SetFieldUtils.setValues(service, map);
	}
	@Test
	public void testCreateSimpleUser(){
		SimpleUser user=new SimpleUser();
		user.setEmail("test@test.com");
		user.setEnName("LouisCloud");
		user.setZhName("吕龙云");
		user.setSalutation("xx");
		EasyMock.expect(umt.createAccount(user.getEmail(), user.getZhName(),"123" )).andReturn(true);
		EasyMock.expect(sDAO.registAccount(user)).andReturn(12);
		EasyMock.expect(sDAO.getUser(12)).andReturn(user);
		EasyMock.expectLastCall();
		cs.set("dhome.uid-user.simpleuser.12", user);
		control.replay();
		service.createSimpleUser(user, "123",false);
	}
	@Test 
	public void testCreateDetailUser(){
		DetailedUser user=new DetailedUser();
		user.setFirstClassDiscipline(1);
		user.setSecondClassDiscipline(2);
		user.setBirthday("2012-02-12");
		EasyMock.expect(dDAO.createDetailedUser(user)).andReturn(12);
		EasyMock.expectLastCall();
		cs.set("dhome.uid-user.detaileduser.0", user);
		control.replay();
		Assert.assertTrue(service.createDetailedUser(user)>0);
	}
	@Test
	public void testCreateEducation(){
		int uid=12;
		String degree="degree";
		String department="department";
		String institution="12";//数字的时候测试
		control.replay();
		Assert.assertTrue(service.createEducation(uid, degree, department, institution)>0);
		institution="中科院";
		Assert.assertTrue(service.createEducation(uid, degree, department, institution)>0);
	}
	@Test
	public void testCreateWork(){
		int uid=12;
		String position="职称";
		String department="department";
		String institution="12";//数字的时候测试
		control.replay();
		Assert.assertTrue(service.createWork(uid, position, department, institution)==0);
		institution="中科院";
		Assert.assertTrue(service.createWork(uid, position, department, institution)==0);
	}
	@Test
	public void testIsEmailUsed(){
		String email="test@test.com";

		EasyMock.expect(umt.isExistUMTRegister(email)).andReturn(false);
		EasyMock.expect(sDAO.isEmailUsed(email)).andReturn(true);
		control.replay();
		Assert.assertTrue(service.isEmailUsed(email));
	}
	@Test
	public void testIsDomainUsed(){
		String domain="hahah";
		EasyMock.expect(hDAO.isDomainUsed(domain)).andReturn(false);
		Assert.assertFalse(service.isDomainUsed(domain));
	}
}
