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
package net.duckling.dhome.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.duckling.dhome.common.util.PaperRender;
import net.duckling.dhome.domain.people.Paper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PaperRenderTest {
	
	private int uid = 108;
	private long dsnPaperId = 0;
	private String userStr= "A1";
	private List<Paper> paperList = null;
	private JSONArray paperArray = null;
	
	@Before
	public void setUp(){
		paperList = createPaperList();
		paperArray = createPaperJSONArray();
	}
	
	@Test
	public void testFormatList(){
		JSONArray result = PaperRender.format(paperList, userStr);
		Assert.assertTrue(assertEquals(paperArray, result));
	}
	
	@Test
	public void testFormatJSONArray(){
		JSONArray result = PaperRender.format(paperArray, userStr);
		Assert.assertTrue(assertEquals(paperArray, result));
	}
	
	@After
	public void tearDown(){
		if(null != paperList){
			paperList.clear();
			paperList = null;
		}
		if(null != paperArray){
			paperArray.clear();
			paperArray = null;
		}
	}
	
	private List<Paper> createPaperList(){
		List<Paper> list = new ArrayList<Paper>();
		list.add(Paper.build(uid, "A", "A1", "CNIC", "", "2012",
				"1", "", "", "", "", "", 
				0, 0, dsnPaperId,""));
		list.add(Paper.build(uid, "B", "A1", "CNIC", "", "2012",
				"2", "", "", "", "", "", 
				0, 0, dsnPaperId,""));
		list.add(Paper.build(uid, "C", "A1", "CNIC", "", "2012",
				"3", "", "", "", "", "", 
				0, 0, dsnPaperId,""));
		return list;
	}
	
	private JSONArray createPaperJSONArray(){
		JSONArray array = new JSONArray();
		array.add(getJSONObject(Paper.build(uid, "A", "A1", "CNIC", "", "2012",
				"1", "", "", "", "", "", 
				0, 0, dsnPaperId,"")));
		array.add(getJSONObject(Paper.build(uid, "B", "A1", "CNIC", "", "2012",
				"2", "", "", "", "", "", 
				0, 0, dsnPaperId,"")));
		array.add(getJSONObject(Paper.build(uid, "C", "A1", "CNIC", "", "2012",
				"3", "", "", "", "", "", 
				0, 0, dsnPaperId,"")));
		return array;
	}
	
	private JSONObject getJSONObject(Paper paper){
		JSONObject obj = new JSONObject();
		obj.put("authors", paper.getAuthors());
		obj.put("title", paper.getTitle());
		return obj;
	}
	
	private boolean assertEquals(JSONArray expected, JSONArray real){
		if(expected.isEmpty() || real.isEmpty() || expected.size() != real.size()){
			return false;
		}
		int size = expected.size();
		for(int i=0; i<size; i++){
			JSONObject objExp = (JSONObject)expected.get(i);
			JSONObject objRea = (JSONObject)real.get(i);
			if(!objExp.get("title").equals(objRea.get("title"))){
				return false;
			}else if(!objExp.get("authors").equals(objRea.get("authors"))){
				return false;
			}
		}
		return true;
	}
}
