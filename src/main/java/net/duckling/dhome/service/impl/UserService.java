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
package net.duckling.dhome.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.duckling.common.util.CommonUtils;
import net.duckling.dhome.common.config.AppConfig;
import net.duckling.dhome.common.umt.UmtClient;
import net.duckling.dhome.common.util.MemCacheKeyGenerator;
import net.duckling.dhome.dao.IDetailedUserDAO;
import net.duckling.dhome.dao.IDisciplineDAO;
import net.duckling.dhome.dao.ISimpleUserDAO;
import net.duckling.dhome.dao.IUserGuideDAO;
import net.duckling.dhome.domain.people.DetailedUser;
import net.duckling.dhome.domain.people.Discipline;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.UserGuide;
import net.duckling.dhome.service.IUserService;
import net.duckling.falcon.api.cache.ICacheService;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.vlabs.umt.oauth.AccessToken;
import cn.vlabs.umt.oauth.UserInfo;
import cn.vlabs.umt.oauth.client.HttpClient;
import cn.vlabs.umt.oauth.client.HttpsConnectionClient;
import cn.vlabs.umt.oauth.client.OAuthClient;
import cn.vlabs.umt.oauth.client.URLConnectionClient;
import cn.vlabs.umt.oauth.client.request.OAuthClientRequest;
import cn.vlabs.umt.oauth.client.response.OAuthJSONAccessTokenResponse;
import cn.vlabs.umt.oauth.common.OAuth;
import cn.vlabs.umt.oauth.common.exception.OAuthProblemException;
import cn.vlabs.umt.oauth.common.exception.OAuthSystemException;
/**
 * 用户服务
 * @author Yangxp
 *
 */
@Service
public class UserService implements IUserService{
	private static final String OAUTH_CLIENT_ID = "client_id";
	private static final String OAUTH_CLIENT_SECRET = "client_secret";
	private static final String OAUTH_ACCESS_TOKEN_URL = "access_token_URL";
	private static final String REDIRCET_URI = "redirect_uri";
	/**
	 * umt token校验方法
	 */
	private static final String OAUTH_GRANT_PASSWORD = "password";
	private static final String OAUTH_GRANT_TYPE = "grant_type";
	
	@Autowired
	private ISimpleUserDAO simpleUserDAO;
	@Autowired
	private IDetailedUserDAO detailedUserDAO;
	@Autowired
	private IDisciplineDAO disciplineDAO;
	@Autowired
	private IUserGuideDAO userGuideDAO;
	@Autowired
	private AppConfig config;
	@Autowired
	private ICacheService cacheService;
	
	private UmtClient umt;
	/**
	 * 初始化
	 */
	@PostConstruct
	public void init(){
		umt=new UmtClient(config.getUmtServerURL());
	}
	/**
	 * 销毁
	 */
	@PreDestroy
	public void destroy(){
		umt=null;
	}
	
	public void setSimpleUserDAO(ISimpleUserDAO sudao){
		simpleUserDAO = sudao;
	}
	public void setDetailedUserDAO(IDetailedUserDAO duDAO){
		detailedUserDAO = duDAO;
	}
	public void setDisciplineDAO(IDisciplineDAO diDAO){
		disciplineDAO = diDAO;
	}
	public void setUserGuideDAO(IUserGuideDAO ugDAO){
		userGuideDAO = ugDAO;
	}
	public void setCacheService(ICacheService cs){
		cacheService = cs;
	}
	
	@Override
	public List<SimpleUser> getSimpleUserByDiscipline(int first, int second, int offset, int size) {
		return simpleUserDAO.getSimpleUserByDiscipline(first, second, offset, size);
	}

	@Override
	public SimpleUser getSimpleUser(String email) {
		return simpleUserDAO.getUser(email);
	}
	@Override
	public SimpleUser getSimpleUser(int uid) {
		return simpleUserDAO.getUser(uid);
	}
	@Override
	public DetailedUser getDetailedUser(int uid) {
		String key=MemCacheKeyGenerator.getDetailUserKey(uid);
		Object du = cacheService.get(key);
		if(null == du){
			DetailedUser duDB = detailedUserDAO.getUser(uid);
			cacheService.set(key, duDB);
			return duDB;
		}else{
			return (DetailedUser)du;
		}
	}

	@Override
	public boolean updateSimpleUserByUid(SimpleUser su) {
		su.setAuditPropose(null);
		su.setStatus(null);
		return updateDBAndCache(su);
	}
	private boolean updateDBAndCache(SimpleUser su){
		int result = simpleUserDAO.updateAccount(su);
		if(result > 0){
			cacheService.set(MemCacheKeyGenerator.getSimpleUserKey(su.getId()), su);
		}
		return result>0;
	}
	@Override
	public boolean updateSimpleUserStatusByUid(SimpleUser su){
		return updateDBAndCache(su);
	}

	@Override
	public boolean updateDetailedUserByUid(DetailedUser du) {
		boolean result = detailedUserDAO.updateDetailedUserByUid(du);
		if(result){
			cacheService.set(MemCacheKeyGenerator.getDetailUserKey(du.getUid()), du);
		}
		return result;
	}
	
	@Override
	public SimpleUser getSimpleUserByUid(int uid) {
		if(uid<=0){
			return null;
		}
		String key=MemCacheKeyGenerator.getSimpleUserKey(uid);
		Object obj = cacheService.get(key);
		if(null == obj){
			SimpleUser su = simpleUserDAO.getUser(uid);
			cacheService.set(key, su);
			return su;
		}else{
			return (SimpleUser)obj;
		}
	}
	@Override
	public List<SimpleUser> getSimpleUsersByUid(List<Integer> uids) {
		return simpleUserDAO.getUsers(uids);
	}
	@Override
	public List<Discipline> getRootDiscipline() {
		return disciplineDAO.getRoot();
	}
	@Override
	public List<Discipline> getChildDiscipline(int id) {
		return disciplineDAO.getChild(id);
	}
	@Override
	public String getDisciplineName(int id) {
		return disciplineDAO.getName(id);
	}
	@Override
	public int getUserCount() {
		return simpleUserDAO.getCount();
	}
	@Override
	public boolean isUmtUser(String email) {
		return umt.isExistUMTRegister(email);
		
	}
	@Override
	public List<SimpleUser> getAllUsers(String status, String keyword, int offset, int size) {
		return simpleUserDAO.getAllUsers(status, keyword, offset, size);
	}
	@Override
	public SimpleUser getSimpleUserByImgId(int imgId) {
		return simpleUserDAO.getSimpleUserByImgId(imgId);
	}
	@Override
	public List<SimpleUser> getSimpleUserByInterest(String keyword, int offset, int size) {
		return simpleUserDAO.getSimpleUserByInterest(keyword, offset, size);
	}
	@Override
	public int create(int uid, String module, int step) {
		return userGuideDAO.create(UserGuide.build(uid, module, step));
	}
	@Override
	public int getStep(int uid, String module) {
		return userGuideDAO.getStep(uid, module);
	}
	@Override
	public void updateStep(int uid, String module, int step) {
		userGuideDAO.updateStep(uid, module, step);
	}
	@Override
	public void updateSimpleUserLastEditTimeByUid(int uid) {
		if(uid<0){
			return;
		}
		if(simpleUserDAO.updateSimpleUserLastEditTimeByUid(uid)>0){
			String key=MemCacheKeyGenerator.getSimpleUserKey(uid);
			SimpleUser su=(SimpleUser)cacheService.get(key);
			if(su!=null){
				su.setLastEditTime(new Date());
				cacheService.set(key,su);
			}
		}
	}
	@Override
	public List<SimpleUser> getUserByEmails(List<String> email) {
		if(CommonUtils.isNull(email)){
			return Collections.emptyList();
		}
		return simpleUserDAO.getUsersByEmails(email);
	}
	@Override
	public List<SimpleUser> getAllUser() {
		return simpleUserDAO.getAllUser();
	}
//	@Override
//	public List<SimpleUser> searchUser(String key) {
//		if(CommonUtils.isNull(key)){
//			return null;
//		}
//		return simpleUserDAO.searchUser(key);
//	}
//	@Override
//	public void updateSimpleUserEmailByUid(int uid,String newEamil) {
//		simpleUserDAO.updateSimpleUserEmailByUid(uid, newEamil);
//	}
	@Override
	public boolean isCorrectUserInfo(String uid, String password) {
		return umt.isCorrectUserInfo(uid, password);
	}
	/**
	 * 验证UMT用户账户和密码信息是否正确
	 * @param uid
	 * @param password
	 * @return
	 */
//	@Override
//	public AccessToken umtPasswordAccessToken(String userName,String password) throws OAuthProblemException{
//		try {
//			OAuthClientRequest request = OAuthClientRequest
//					.tokenLocation(
//							properties.getProperty(OAUTH_ACCESS_TOKEN_URL))
//					.setClientId(properties.getProperty(OAUTH_CLIENT_ID))
//					.setClientSecret(
//							properties.getProperty(OAUTH_CLIENT_SECRET))
//					.setParameter(OAUTH_GRANT_TYPE, OAUTH_GRANT_PASSWORD)
//					.setParameter(OAuth.OAUTH_USERNAME, userName)
//					.setParameter(OAuth.OAUTH_PASSWORD, password)
//					.setParameter(OAuth.OAUTH_REDIRECT_URI, properties.getProperty(REDIRCET_URI))
//					.buildBodyMessage();
//			OAuthClient oAuthClient = new OAuthClient(getHttpClient());
//			OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient
//					.accessToken(request, OAuthJSONAccessTokenResponse.class);
//			String error = oAuthResponse.getParam("error");
//			if (error != null && error.length() > 0) {
//				OAuthProblemException ex = OAuthProblemException.error(error,
//						oAuthResponse.getParam("error_description"));
//				throw ex;
//			}
//			AccessToken token = new AccessToken();
//			token.setAccessToken(oAuthResponse.getAccessToken());
//			token.setRefreshToken(oAuthResponse.getRefreshToken());
//			token.setExpiresIn(oAuthResponse.getExpiresIn());
//			token.setScope(oAuthResponse.getScope());
//			token.setUserInfo(getUserInfo(oAuthResponse.getParam("userInfo")));
//			return token;
//		} catch (OAuthSystemException e) {
//			throw OAuthProblemException.error("systemError", e.getMessage());
//		}
//	}
//	private HttpClient getHttpClient() {
//		if (properties.getProperty(OAUTH_ACCESS_TOKEN_URL).toLowerCase()
//				.startsWith("https")) {
//			return new HttpsConnectionClient();
//		} else {
//			return new URLConnectionClient();
//		}
//	}
//
//	private UserInfo getUserInfo(String param) {
//		if (param == null || param.length() == 0) {
//			return null;
//		}
//		JSONObject obj;
//		try {
//			UserInfo user = new UserInfo();
//			obj = new JSONObject(param);
//			user.setType(getFromJSON(obj, "type"));
//			user.setTrueName(getFromJSON(obj, "truename"));
//			user.setCstnetId(getFromJSON(obj, "cstnetId"));
//			user.setUmtId(getFromJSON(obj, "umtId"));
//			user.setPasswordType(getFromJSON(obj, "passwordType"));
//			user.setCstnetIdStatus(getFromJSON(obj, "cstnetIdStatus"));
//			user.setSecurityEmail(getFromJSON(obj, "securityEmail"));
//			try {
//				JSONArray emails = obj.getJSONArray("secondaryEmails");
//				if (emails != null && emails.length() > 0) {
//					String[] r = new String[emails.length()];
//					for (int i = 0; i < emails.length(); i++) {
//						r[i] = emails.getString(i);
//					}
//					user.setSecondaryEmails(r);
//				}
//			} catch (JSONException e) {
//			}
//			return user;
//		} catch (JSONException e) {
//			return null;
//		}
//	}
//	private String getFromJSON(JSONObject obj, String key) {
//		try {
//			return obj.getString(key);
//		} catch (JSONException e) {
//			return null;
//		}
//	}
}
