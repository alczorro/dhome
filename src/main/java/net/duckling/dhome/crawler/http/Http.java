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
package net.duckling.dhome.crawler.http;





import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class Http {
	public  final int MAX_THREAD = 10;
	private static MultiThreadedHttpConnectionManager connectionManager =
        new MultiThreadedHttpConnectionManager();
	private static HttpClient client;
	public static synchronized HttpClient getClient() {	
//		if(client == null){
//			client = new HttpClient(connectionManager);
//		}
	    return new HttpClient(connectionManager);
	}
	

	public  void setConf(){		
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		client.getParams().setParameter(HttpMethodParams.SINGLE_COOKIE_HEADER,
				true);	
		System.setProperty("apache.commons.httpclient.cookiespec", "compatibility");
		HttpConnectionManagerParams params = connectionManager.getParams();
		params.setMaxTotalConnections(MAX_THREAD);
	}
		
	
	
	
}
