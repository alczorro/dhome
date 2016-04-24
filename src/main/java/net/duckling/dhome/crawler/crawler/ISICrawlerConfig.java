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


import java.io.File;

import net.duckling.dhome.crawler.parser.Parser;






public class ISICrawlerConfig {
	
	public static void initConfig(Parser parser) throws Exception {
		//if (_instance == null) {
			//String prefix = System.getProperty("paperSearch_webappHome");
			//if(prefix==null||prefix.equals("")){
				//prefix = "D:\\Programs\\tomcat5.5.23\\webapps\\PaperSearch\\";
				String prefix = ISICrawlerConfig.class.getResource("/").getPath();
				File file = new File(new File(prefix).getParent(),CONFIG_FILE);
			//}
			parser.setDocument(file.getAbsolutePath());
		//} 
			
		//return _instance;
	}

	private ISICrawlerConfig(String configFile,Parser parser) {

		try {
			parser.setDocument(configFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static ISICrawlerConfig _instance;
	private final static String CONFIG_FILE = "/conf/isi.xml";


}
