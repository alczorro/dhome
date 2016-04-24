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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.config.AppConfig;
import net.duckling.dhome.common.umt.UmtClient;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.UrlUtil;
import net.duckling.dhome.service.IKeyValueService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.vlabs.commons.principal.UserPrincipal;
import cn.vlabs.duckling.api.umt.rmi.exception.APIRuntimeException;
import cn.vlabs.duckling.api.umt.rmi.user.UMTUser;
/**
 * 认证服务，主要用于用户登录，获取UMT用户等.
 * @author Yangxp
 * @date 2012-08-01
 */
@Service
public final class AuthenticationService implements IAuthenticationService {
	
	private static final Logger LOG = Logger.getLogger(AuthenticationService.class);
	
	@Autowired
	private AppConfig config;
	@Autowired
	private IKeyValueService keyService;
	
	private UmtClient client;

	public void setKeyService(IKeyValueService keyService) {
		this.keyService = keyService;
	}
	public void setAppConfig(AppConfig config){
		this.config = config;
	}
	public void setClient(UmtClient client){
		this.client = client;
	}
	
	/**
	 * 初始化
	 */
	@PostConstruct
	public void init(){
		client = new UmtClient(config.getUmtServerURL());
	}
	/**
	 * 销毁
	 */
	@PreDestroy
	public void destroy(){
		client = null;
	}
	@Override
	public boolean login(String email, String password) {
		if(null == email || null == password ||
				"".equals(email) || "".equals(password)){
			return false;
		}
		try{
			UserPrincipal up = client.loginInUMT(email, password);
			return (null == up)?false:true;
		}catch(APIRuntimeException e){
			LOG.error("登录失败，无法连接认证服务器！", e);
			return false;
		}
	}
	@Override
	public UMTUser getUMTUser(String email){
		if(null == email || "".equals(email)){
			return null;
		}
		try{
			return client.getUmtUser(email, "");
		}catch(APIRuntimeException e){
			LOG.error("登录失败，无法连接认证服务器！", e);
			return null;
		}
	}
	@Override
	public String getLogOutURL(HttpServletRequest request) {
		SsoLogoutURL ssoURL = new SsoLogoutURL();
		ssoURL.setUmtLogoutURL(config.getUmtLogoutURL());
		ssoURL.setAppName(config.getAppName());
		ssoURL.setSessionId(request.getSession().getId());
		ssoURL.setRedirectURL(getRedirectURL(request));
		return ssoURL.toString();
	}
	@Override
	public String getFindPasswordURL(){
		return config.getUmtBaseURL()+"/password.jsp";
	}
	
	private String getRedirectURL(HttpServletRequest request){
		return UrlUtil.getRootURL(request)+CommonUtils.killNull(keyService.getValue(IKeyValueService.DEPLOY_SUBFIX));
	}
}
