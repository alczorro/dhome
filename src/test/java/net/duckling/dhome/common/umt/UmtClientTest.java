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
package net.duckling.dhome.common.umt;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.vlabs.commons.principal.UserPrincipal;
import cn.vlabs.duckling.api.umt.rmi.user.UMTUser;
import cn.vlabs.duckling.api.umt.rmi.user.UserService;

public class UmtClientTest {
	
	private IMocksControl mc = null;
	private UmtClient uc = null;
	private String umtServerURL = "127.0.0.1";
	private String uid = "108";
	private UserService us = null;
	
	@Before
	public void setUp(){
		mc = EasyMock.createControl();
		us = mc.createMock(UserService.class);
		uc = new UmtClient(umtServerURL);
		uc.setUmtUserService(us);
	}
	
	@Test
	public void testIsCorrectUserInfo(){
		UMTUser user = new UMTUser();
		user.setPassword("1");
		EasyMock.expect(us.getUMTUser(uid)).andStubReturn(user);
		mc.replay();
		Assert.assertTrue(uc.isCorrectUserInfo(uid, "1"));
	}
	
	@Test
	public void testIsCorrectUserInfoWithNull(){
		EasyMock.expect(us.getUMTUser(uid)).andStubReturn(null);
		mc.replay();
		Assert.assertFalse(uc.isCorrectUserInfo(uid, ""));
	}
	
	@Test
	public void testIsExistUMTRegister(){
		EasyMock.expect(us.isExist(uid)).andStubReturn(false);
		mc.replay();
		Assert.assertFalse(uc.isExistUMTRegister(uid));
	}
	
	@Test
	public void testLoginInUMT(){
		UserPrincipal up = new UserPrincipal();
		EasyMock.expect(us.login("xx@xx.xx", "123")).andStubReturn(up);
		mc.replay();
		UserPrincipal upReal = uc.loginInUMT("xx@xx.xx", "123");
		Assert.assertNotNull(upReal);
		Assert.assertEquals(up, upReal);
	}
	
	
	@After
	public void tearDown(){
		mc = null;
		us = null;
		uc = null;
	}
}
