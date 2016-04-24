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

import net.duckling.dhome.common.util.CommonUtils;

import org.apache.log4j.Logger;

import cn.vlabs.commons.principal.UserPrincipal;
import cn.vlabs.duckling.api.umt.rmi.exception.UserExistException;
import cn.vlabs.duckling.api.umt.rmi.user.UMTUser;
import cn.vlabs.duckling.api.umt.rmi.user.UserService;
import cn.vlabs.umt.oauth.AccessToken;
import cn.vlabs.umt.oauth.client.OAuthClient;
import cn.vlabs.umt.oauth.client.request.OAuthClientRequest;
import cn.vlabs.umt.oauth.client.response.OAuthJSONAccessTokenResponse;
import cn.vlabs.umt.oauth.common.exception.OAuthProblemException;
import cn.vlabs.umt.oauth.common.exception.OAuthSystemException;
/**
 * UMT客户端
 * @author Zhaojuan
 * @date 2012-08-01
 */
public class UmtClient {

	private static final Logger LOG = Logger.getLogger(UmtClient.class);

	private String umtServiceURL;
	private UserService umtUserService;
	/**构造方法
	 * @param url umt地址，从appConfig里面读取
	 * */
	public UmtClient(String url) {
		umtServiceURL = url;
		umtUserService = new UserService(umtServiceURL);
	}
	
	public void setUmtUserService(UserService us){
		umtUserService = us;
	}
	/**
	 * 获取UMT用户信息
	 * @param uid
	 * @param displayName
	 * @return
	 */
	public UMTUser getUmtUser(String uid, String displayName) {
		UMTUser umt = umtUserService.getUMTUser(uid);
		if (umt == null) {
			LOG.error("This user is not found.");
			return null;
		}
		return umt;
	}
	
	/**
	 * 创建umt账号
	 * @param uid user id
	 * @param displayName zhName or enName
	 * @param password password
	 * @return is create success?
	 * */
	public boolean createAccount(String uid, String displayName, String password) {
		if (!umtUserService.isExist(uid)) {
			UMTUser user = new UMTUser(uid, displayName, uid, password);
			try {
				umtUserService.createUser(user);
				return true;
			} catch (UserExistException e) {
				LOG.error("This user is exist, please change your user id.", e);
				return false;
			}
		}
		return false;
	}	
	/**
	 * 验证UMT用户账户和密码信息
	 * @param uid
	 * @param password
	 * @return
	 */
	public boolean isCorrectUserInfo(String uid, String password) {
		UMTUser umtUser = umtUserService.getUMTUser(uid);
		if (umtUser == null || password == null) {
			return false;
		}
		return password.equals(umtUser.getPassword());
	}
	/**
	 * validate the email is used in umt system
	 * @param email
	 * @return is email exist
	 * */
	public boolean isExistUMTRegister(String email) {
		if(CommonUtils.isNull(email)){
			return true;
		}
		return umtUserService.isExist(email.toLowerCase());
	}
	
	/**
	 * do login in umt system
	 * @param email email
	 * @param password password
	 * @return 
	 * */
	public UserPrincipal loginInUMT(String email, String password) {
		return  umtUserService.login(email, password);
	}
	
}
