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
package net.duckling.dhome.common.auth.sso;

import javax.servlet.http.HttpServletRequest;

import cn.vlabs.duckling.api.umt.rmi.user.UMTUser;

/**
 * 认证服务接口
 * @author Yangxp
 *
 */
public interface IAuthenticationService {
	/**
	 * 用户登录UMT
	 * @param email 用户名
	 * @param password 密码
	 * @return true or false
	 */
	boolean login(String email, String password);
	/**
	 * 获取UMT用户
	 * @param email
	 * @return
	 */
	UMTUser getUMTUser(String email);
	/**
	 * 生成用户注销的URL
	 * @param request 
	 * @return 
	 */
	String getLogOutURL(HttpServletRequest request);
	/**
	 * 生成找回密码页面的URL
	 * @return
	 */
	String getFindPasswordURL();
	
}
