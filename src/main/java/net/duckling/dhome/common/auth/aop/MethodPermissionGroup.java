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

import net.duckling.dhome.common.auth.aop.annotation.NAccessDenied;
import net.duckling.dhome.common.auth.aop.annotation.NPermission;

import org.springframework.core.annotation.AnnotationUtils;

public class MethodPermissionGroup {

	private Class parentClazz;

	private NPermission clazzPermission;

	private Map<String, Method> deniedHandlerMap;

	private Map<String, NPermission> methodPermissionMap;

	public MethodPermissionGroup(Class clazz) {
		methodPermissionMap = new ConcurrentHashMap<String, NPermission>();
		deniedHandlerMap = new ConcurrentHashMap<String, Method>();
		parentClazz = clazz;
		init();
	}

	public Method findDenyHandler(String methodName) {
		if (methodName != null) {
			return deniedHandlerMap.get(methodName);
		}
		return null;
	}

	public NPermission findMethodPermission(String methodName) {
		if (methodName != null) {
			NPermission methodPermission = methodPermissionMap.get(methodName);
			if (methodPermission != null) {
				return methodPermission;
			}
		}
		return clazzPermission;
	}

	public String getCurrentClassName() {
		return parentClazz.getName();
	}

	private void init() {
		Method[] methods = parentClazz.getMethods();
		loadClazzPermission();
		loadMethodPermission(methods);
		loadAccessDeniedHandlers(methods);
	}

	private void loadClazzPermission() {
		clazzPermission = AnnotationUtils.findAnnotation(parentClazz, NPermission.class);
	}

	private void loadMethodPermission(Method[] methods) {
		for (Method m : methods) {
			NPermission methodPermission = AnnotationUtils.findAnnotation(m, NPermission.class);
			if (methodPermission != null) {
				methodPermissionMap.put(m.getName(), methodPermission);
			}
		}
	}

	private void loadAccessDeniedHandlers(Method[] methods) {
		Method defaultHandler = findDefaultHandler(methods);
		if (defaultHandler != null) {
			handleWithStarCase(methods, defaultHandler);
		}
	}

	private Method findDefaultHandler(Method[] methods) {
		Method defaultHandler = null;
		for (Method currentMethod : methods) {
			NAccessDenied anno = AnnotationUtils.findAnnotation(currentMethod, NAccessDenied.class);
			if (anno != null) {
				for (String targetName : anno.value()) {
					if ("*".equals(targetName)) {
						defaultHandler = currentMethod;
						break;
					} else {
						deniedHandlerMap.put(targetName, currentMethod);
					}
				}
			}
		}
		return defaultHandler;
	}

	private void handleWithStarCase(Method[] methods, Method defaultHandler) {
		for (Method currentMethod : methods) {
			if (AnnotationUtils.findAnnotation(currentMethod, NAccessDenied.class) == null) {
				deniedHandlerMap.put(currentMethod.getName(), defaultHandler);
			}
		}
	}
}
