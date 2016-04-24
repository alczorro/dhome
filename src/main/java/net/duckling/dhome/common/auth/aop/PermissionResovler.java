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

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.duckling.dhome.common.auth.aop.annotation.NPermission;


public class PermissionResovler {

	private Map<String, MethodPermissionGroup> controllers = new ConcurrentHashMap<String, MethodPermissionGroup>();

	public Method findDenyProcessor(Object handler, String methodName) {
		MethodPermissionGroup helper = getHelper(handler);
		return helper.findDenyHandler(methodName);
	}

	public NPermission findPermission(Object handler, String methodName) {
		MethodPermissionGroup helper = getHelper(handler);
		return helper.findMethodPermission(methodName);
	}

	private MethodPermissionGroup getHelper(Object handler) {
		MethodPermissionGroup helper = controllers.get(handler.getClass().getName());
		if (helper == null) {
			helper = new MethodPermissionGroup(handler.getClass());
			controllers.put(helper.getCurrentClassName(), helper);
		}
		return helper;
	}
}
