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
package net.duckling.dhome.service.impl.mock;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.vlabs.duckling.api.umt.rmi.user.UMTUser;

import net.duckling.dhome.common.auth.sso.IAuthenticationService;

public class MockAuthenticationService implements IAuthenticationService{

	private Map<String, String> userMap;
	private String logoutURL;
	private String pswURL;
	public MockAuthenticationService(){
		userMap = new HashMap<String, String>();
		userMap.put("xiaoming@163.com", "123456");
		logoutURL = "localhost/logout";
		pswURL = "umt/psw.jsp";
	}
	
	@Override
	public boolean login(String email, String password) {
		if(null == email || null == password){
			return false;
		}
		if(userMap.containsKey(email)){
			return userMap.get(email).equals(password)?true:false;
		}
		return false;
	}

	@Override
	public String getLogOutURL(HttpServletRequest request) {
		return logoutURL;
	}

	@Override
	public String getFindPasswordURL() {
		return pswURL;
	}

	@Override
	public UMTUser getUMTUser(String email) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
