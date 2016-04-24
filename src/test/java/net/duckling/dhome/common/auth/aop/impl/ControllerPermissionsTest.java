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
package net.duckling.dhome.common.auth.aop.impl;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import net.duckling.dhome.common.auth.aop.FirstController;
import net.duckling.dhome.common.auth.aop.MethodPermissionGroup;
import net.duckling.dhome.common.auth.aop.SecondController;
import net.duckling.dhome.common.auth.aop.annotation.NPermission;

import org.junit.Before;
import org.junit.Test;

public class ControllerPermissionsTest {

	private MethodPermissionGroup container1 = null;
	private MethodPermissionGroup container2 = null;

	@Before
	public void setup() {
		container1 = new MethodPermissionGroup(FirstController.class);
		container2 = new MethodPermissionGroup(SecondController.class);
	}

	@Test
	public void testGetmethodPermission() throws NoSuchMethodException {
		assertMethodPermission(FirstController.class, "guest", true);
		assertMethodPermission(FirstController.class, "sayHello", false);
		assertMethodPermission(FirstController.class, "world", false);
		assertMethodPermission(FirstController.class, "iphone", true);
	}
	
	@Test(expected = NoSuchMethodException.class)
	public void testPrivateMethodPermission() throws NoSuchMethodException{
		assertMethodPermission(FirstController.class, "privateMethod", true);
	}
	
	private void assertMethodPermission(Class clazz, String methodName, boolean expected) throws NoSuchMethodException {
		Method method = clazz.getMethod(methodName);
		NPermission np = container1.findMethodPermission(method.getName());
		assertEquals(expected, np.authenticated());
	}
	
	@Test
	public void testFindDenyProcessor(){
		assertDeniedMethod(container1,"anotherErrorHandler","guest");
		assertDeniedMethod(container1,"errorHandle","sayHello");
		assertDeniedMethod(container2,"myhandler","first");
		assertDeniedMethod(container2,"myhandler","second");
	}
	
	private void assertDeniedMethod(MethodPermissionGroup container,String expectedName,String targetMethod){
		Method m = container.findDenyHandler(targetMethod);
		assertEquals(expectedName,m.getName());
	}

}
