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
package net.duckling.dhome.service.impl.mock;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.duckling.dhome.common.dsn.DsnClient;
import net.duckling.dhome.service.IDsnSearchService;

public class MockDsnSearchService implements IDsnSearchService {

	@Override
	public JSONArray initQuery(int uid, int offset, int size,
			List<Long> existIds) {
		return generateReturnJSON("init");
	}

	@Override
	public JSONArray query(int uid, String keyword, int offset, int size,
			List<Long> existIds) {
		return generateReturnJSON("query");
	}

	@Override
	public JSONArray getDsnPapers(String paperIds) {
		if("1,2".equals(paperIds)){
			return getJSONArrayForTest();
		}
		return null;
	}

	@Override
	public String getUserNameString(int uid) {
		return ""+uid;
	}
	
	private JSONArray generateReturnJSON(String type){
		JSONArray array = new JSONArray();
		for(int i=0; i<10; i++){
			JSONObject obj = new JSONObject();
			obj.put(DsnClient.AUTHORS, "杨小澎");
			obj.put(DsnClient.DSN_PAPER_ID, i+1);
			obj.put(DsnClient.SOURCE, "test "+type);
			obj.put(DsnClient.TITLE, "Paper "+(i+1));
			obj.put(DsnClient.VOLUMEISSUE, "vol."+(i+1));
			array.add(obj);
		}
		return array;
	}
	
	private JSONArray getJSONArrayForTest(){
		JSONArray result = new JSONArray();
		for(int i=0; i<2; i++){
			JSONObject obj = new JSONObject();
			obj.put(DsnClient.AUTHORS, "yxp");
			obj.put(DsnClient.DSN_PAPER_ID, ""+(i+1));
			obj.put(DsnClient.SOURCE, "yxp");
			obj.put(DsnClient.TITLE, "paper yxp");
			obj.put(DsnClient.VOLUMEISSUE, "vol.paper");
			result.add(obj);
		}
		return result;
	}
}
