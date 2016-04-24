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
package net.duckling.dhome.common.auth.aop;

import static org.junit.Assert.assertEquals;
import net.duckling.dhome.common.auth.aop.annotation.NPermission;

import org.junit.Before;
import org.junit.Test;

public class PermissionResovlerTest {
	
	private FirstController firstController = new FirstController();
	private SecondController secondController = new SecondController();
	
	private PermissionResovler resovler = null;
	
	@Before
	public void setup(){
		resovler = new PermissionResovler();
	}
	
	@Test
	public void testFindDenyProcessor(){
		assertDeniedHandler("errorHandle",firstController,"sayHello");
		assertDeniedHandler("anotherErrorHandler",firstController,"guest");
		assertDeniedHandler("myhandler",secondController,"first");
		assertDeniedHandler("myhandler",secondController,"second");
	}

	private void assertDeniedHandler(String expectedName,Object target,String methodName) {
		assertEquals(expectedName,resovler.findDenyProcessor(target, methodName).getName());
	}
	
	@Test
	public void testFindPermission(){
		assertMethodPermission(false,firstController,"world");
		assertMethodPermission(false,firstController,"sayHello");
		assertMethodPermission(true,firstController,"guest");
		assertMethodPermission(true,firstController,"iphone");
		assertMethodPermission(true,secondController,"first");
		assertMethodPermission(true,secondController,"second");
	}

	private void assertMethodPermission(boolean expected,Object target,String methodName) {
		NPermission np = resovler.findPermission(target, methodName);
		if(np==null){
			throw new NullPointerException();
		}
		assertEquals(expected,np.authenticated());
	}
	
	


}
