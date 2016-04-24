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
package net.duckling.dhome.service.auth;


import junit.framework.Assert;
import junit.framework.TestCase;
import net.duckling.dhome.common.auth.sso.AuthenticationService;
import net.duckling.dhome.common.config.AppConfig;
import net.duckling.dhome.common.umt.UmtClient;
import net.duckling.dhome.service.IKeyValueService;
import net.duckling.dhome.service.impl.test.StubKeyValueService;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import cn.vlabs.commons.principal.UserPrincipal;
import cn.vlabs.duckling.api.umt.rmi.exception.APIRuntimeException;
import cn.vlabs.rest.ServiceException;

public class AuthenticationServiceTest extends TestCase {

	private IMocksControl mc = EasyMock.createControl();
	private UmtClient uc = null;
	AuthenticationService aus = new AuthenticationService();
	IKeyValueService keyValueService=new StubKeyValueService();
	
	@Before
	public void setUp(){
		uc = mc.createMock(UmtClient.class);
		aus.setKeyService(keyValueService);
		aus.setClient(uc);
	}
	
	@Test
	public void testLoginSuccess() {
		UserPrincipal up = new UserPrincipal();
		EasyMock.expect(uc.loginInUMT("yxp", "psw")).andStubReturn(up);
		mc.replay();
		boolean status = aus.login("yxp", "psw");
		Assert.assertTrue(status);
	}
	
	@Test
	public void testLoginWithInvalidEmail(){
		EasyMock.expect(uc.loginInUMT("yxp", "psw")).andStubReturn(null);
		EasyMock.expect(uc.isExistUMTRegister("yxp")).andStubReturn(false);
		mc.replay();
		boolean status = aus.login("yxp", "psw");
		Assert.assertFalse(status);
	}
	
	@Test
	public void testLoginWithInvalidPsw(){
		EasyMock.expect(uc.loginInUMT("yxp", "psw")).andStubReturn(null);
		EasyMock.expect(uc.isExistUMTRegister("yxp")).andStubReturn(true);
		mc.replay();
		boolean status = aus.login("yxp", "psw");
		Assert.assertFalse(status);
	}
	
	@Test
	public void testLoginWithException(){
		APIRuntimeException e = new APIRuntimeException(new ServiceException(0, ""));
		EasyMock.expect(uc.loginInUMT("yxp", "psw")).andThrow(e);
		mc.replay();
		try{
			aus.login("yxp", "psw");
		}catch(APIRuntimeException ex){
			Assert.assertTrue(true);
		}
	}
	
	@Test
	public void testGetLogOutURL(){
		AppConfig ac = mc.createMock(AppConfig.class);
		MockHttpServletRequest request = new MockHttpServletRequest();
		EasyMock.expect(ac.getUmtLogoutURL()).andReturn("localhost/umt/logout");
		EasyMock.expect(ac.getAppName()).andReturn("dhome");
		EasyMock.expect(ac.getBaseURL()).andReturn("localhost/dhome");
		mc.replay();
		aus.setAppConfig(ac);
		String url = aus.getLogOutURL(request);
		Assert.assertNotNull(url);
		System.out.println(url);
	}
	
	@Test 
	public void testGetFindPasswordURL(){
		AppConfig ac = mc.createMock(AppConfig.class);
		EasyMock.expect(ac.getUmtBaseURL()).andReturn("localhost/umt");
		mc.replay();
		aus.setAppConfig(ac);
		String url = aus.getFindPasswordURL();
		Assert.assertNotNull(url);
		System.out.println(url);
	}
	
	@After
	public void tearDown(){
		uc = null;
	}
}
