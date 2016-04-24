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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.common.auth.aop.annotation.NAccessDenied;
import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.util.JSONHelper;

import org.apache.commons.httpclient.HttpStatus;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@NPermission(authenticated = true)
//@Controller("/{domain}/abc")
public class FirstController {

	@SuppressWarnings("unused")
	@NAccessDenied({ "sayHello", "world" })
	public void errorHandle(String methodName, HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		obj.put("status", "error");
		obj.put("result", "无权进行此操作！");
		response.setStatus(HttpStatus.SC_FORBIDDEN);
		JSONHelper.writeJSONObject(response, obj);
	}

	@NAccessDenied({ "guest", "iphone" })
	public void anotherErrorHandler(String methodName, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		obj.put("result", methodName);
		JSONHelper.writeJSONObject(response, obj);
	}

	@NPermission(authenticated = false)
	public String sayHello() {
		return "Hello Permission";
	}

	@NPermission(authenticated = false)
	public String world() {
		return "Hello Permission";
	}

	@NPermission(authenticated = true)
	public String iphone() {
		return "iphone";
	}

	public String guest() {
		return "Guest" + privateMethod();
	}

	private String privateMethod() {
		return "private";
	}

	@RequestMapping
	public String getDomain(@PathVariable("domain") String domain) {
		return "get" + domain;
	}
}
