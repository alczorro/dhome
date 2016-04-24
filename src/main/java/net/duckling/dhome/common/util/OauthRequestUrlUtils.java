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
/**
 * 
 */
package net.duckling.dhome.common.util;

import net.duckling.dhome.common.config.AppConfig;

/**
 * @author lvly
 * @since 2013-3-29
 */
public class OauthRequestUrlUtils {
	
	public static String getUrl(AppConfig appConfig){
		StringBuffer sb=new StringBuffer();
		sb.append(appConfig.getOauthUmtAuthorizeURL())
		  .append("?").append("response_type=code")
		  .append("&redirect_uri=").append(CommonUtils.trim(appConfig.getOauthUmtRedirectURL()))
		  .append("&client_id=").append(CommonUtils.trim(appConfig.getOauthUmtClientId()))
		  .append("&theme=").append(CommonUtils.trim(appConfig.getOauthUmtTheme()));
		return sb.toString();
	}

}
