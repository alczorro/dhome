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
package net.duckling.dhome.crawler.parser;



import java.util.LinkedList;
import java.util.List;

import net.duckling.dhome.crawler.crawler.Rules;
import nu.xom.Element;
import nu.xom.Nodes;

import org.apache.commons.httpclient.NameValuePair;


public class ISICrawlerConfigParser extends XmlParser{
	public Rules getRules(String name, String query,String queryType,String startYear,String endYear,String SID) {
		Rules rules = new Rules();
		Nodes nodes = getNodes(doc, "//ns:isi_config/ns:" + name+"/ns:param");
		for (int i = 0; i < nodes.size(); i++) {
			Element params = (Element) nodes.get(i);	
			String paramsName = params.getAttributeValue("name").trim();
			Object value = params.getAttributeValue("value").trim();
			rules.set(paramsName, value);
		}
		if(name.equals("crawl_mainPage")){
			String paramsName = "request_param";
			Object value = wrapParams(query,queryType,
					startYear,endYear,SID);
			rules.set(paramsName, value);
		}
//		if(name.equals("sources")){
//			rules.set("sources", wrapSources());
//		}
		
		return rules;
	}
	
	public NameValuePair[] wrapParams(String query,String queryType, String startYear,
			String endYear,String SID) {
		Nodes nodes = getNodes(doc, "//ns:isi_config/ns:crawl_mainPage/ns:request_param");
		List<NameValuePair> list = new LinkedList<NameValuePair>();
		int i = 0;
		for (; i < nodes.size(); i++) {
			Element params = (Element) nodes.get(i);
			NameValuePair np = new NameValuePair();
			String name = params.getAttributeValue("name");
			String value = params.getAttributeValue("value");
			if (name.equals("value(input1)")) {
				value = query;
			}
			
			if(name.equals("value(select1)")){
				value = queryType;
			}
			
			if (name.equals("startYear") && startYear != null && !startYear.equals("")) {
				value = startYear;
			}
			if (name.equals("endYear") && endYear != null  && !endYear.equals("")) {
				value = endYear;
			}
			if (name.equals("SID")) {
				value = SID;
			}
			
			if(name.equals("sa_params")){
				value = "UA||"+SID+"|http://apps.webofknowledge.com|'";
			}
			
			if(name != null)
				name = name.trim();
			np.setName(name);
			
			if(value != null)
				value = value.trim();
			np.setValue(value);

			list.add(np);
		}
		return list.toArray(new NameValuePair[i]);
	}
	@Override
	public Object parse() {
		// TODO Auto-generated method stub
		return null;
	}

}
