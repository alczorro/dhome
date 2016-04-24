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
package net.duckling.dhome.crawler.crawler;






import net.duckling.dhome.crawler.http.HttpConnection;
import net.duckling.dhome.crawler.http.HttpResponse;
import net.duckling.dhome.crawler.io.WriteStreamToParser;
import net.duckling.dhome.crawler.metadata.Metadata;
import net.duckling.dhome.crawler.parser.ISICiteParser;
import net.duckling.dhome.crawler.parser.ISICrawlerConfigParser;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.NameValuePair;

public class ISISite{
	private HttpConnection connection ;

	private String hostUrl;
	private String advancedUrl;
	private String START_URL = "http://apps.webofknowledge.com"; //new host name
	private NameValuePair[] params;
	private String cookiesStr = "";
	CrawlTask task;
	public ISISite(CrawlTask task) throws Exception{
		connection	= new HttpConnection();
		this.task = task;
		
	}
	
	private void initSearchCountParams(String SID) throws Exception {
		ISICrawlerConfigParser configParser = new ISICrawlerConfigParser();
		ISICrawlerConfig.initConfig(configParser);
		Rules rules = configParser.getRules("crawl_mainPage", task.getQuery(),task.getType(), task.getStartYear(), task.getEndYear(),SID);
		hostUrl = rules.get("host_url").toString();
		advancedUrl = rules.get("advanced_url").toString();	   
		params = (NameValuePair[])rules.get("request_param");
		rules= configParser.getRules("craw_listPage", task.getQuery(),task.getType(), task.getStartYear(), task.getEndYear(),SID);
	}

    public String login(){
    	String SID = "";
    	try {
    		System.out.println("Getting ISI SID:"+START_URL);
			HttpResponse response = connection.executeGetWithoutContent(
					START_URL, null,cookiesStr,"", true);
			
			System.out.println("coockies:"+response.getCookies());
			Cookie[]  cookies = response.getCookies();
			if (cookies != null) {
				
				for(int i=0;i<cookies.length;i++){
					if(cookies[i].getName().equals("SID")||cookies[i].getName().equals("sid")){					
						SID = cookies[i].getValue();
						cookiesStr += "SID=\""+SID+"\"; "; 
					}
					else if(cookies[i].getName().equals("CUSTOMER")||cookies[i].getName().equals("E_GROUP_NAME")
                    		){
                    	cookiesStr +=cookies[i].getName()+"=\""+cookies[i].getValue()+"\"; ";
                    }
					else if (cookies[i].getName().equals("JSESSIONID")){
						cookiesStr+=cookies[i].getName()+"=\""+cookies[i].getValue()+"\"";
					}
						
					}
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SID;
    }
    
	public String getCite() {	
		String SID = login();
		
			try {
				initSearchCountParams(SID);
				
				HttpResponse response = connection.executePostWithoutContent(
						advancedUrl, params,cookiesStr);
				
				ISICiteParser parser = new ISICiteParser();
				WriteStreamToParser write = new WriteStreamToParser(parser,hostUrl);
				String location = response.getHeaders().get(Metadata.LOCATION);
				connection.executeGet(location, null, cookiesStr,"",true,write);		
				String citeCount = parser.parse();
				System.out.print(location+" \n ************************抓取的引用频次  " + citeCount +" \n");
				return citeCount;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		return "";
	}
}
