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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;
/**
 * 生成UMT登出链接地址
 * @author Yangxp
 * @date 2012-08-01
 */
public class SsoLogoutURL {
	private static final Logger LOG = Logger.getLogger(SsoLogoutURL.class);
	
	private String umtLogoutURL;
	private String appName;
	private String redirectURL;
	private String sessionId;
	
	public String getUmtLogoutURL() {
		return umtLogoutURL;
	}

	public void setUmtLogoutURL(String umtLogoutURL) {
		this.umtLogoutURL = umtLogoutURL;
	}

	public String getRedirectURL() {
		return redirectURL;
	}

	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * 构造登出URL
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(getUmtLogoutURL());
			sb.append("?WebServerURL="+URLEncoder.encode(getRedirectURL(), "UTF-8"));
			sb.append("&appname=" + getAppName());
			sb.append("&sid=" + getSessionId());
		} catch (UnsupportedEncodingException e) {
			LOG.error(e.getMessage(),e);
		}
		return sb.toString();
	}
}
