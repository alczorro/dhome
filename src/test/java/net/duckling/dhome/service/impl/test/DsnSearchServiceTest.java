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
package net.duckling.dhome.service.impl.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import net.duckling.dhome.common.dsn.DsnClient;
import net.duckling.dhome.dao.ISimpleUserDAO;
import net.duckling.dhome.dao.impl.MockSimpleUserDAO;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.impl.DsnSearchService;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DsnSearchServiceTest {
	private IMocksControl control = EasyMock.createControl();
	private DsnClient client;
	private ISimpleUserDAO suDAO;
	private DsnSearchService dsnService;
	private int uid = 0;
	private String query;
	
	@Before
	public void setUp(){
		client = control.createMock(DsnClient.class);
		suDAO = new MockSimpleUserDAO();
		dsnService = new DsnSearchService();
		dsnService.setSimpleUserDAO(suDAO);
		dsnService.setDsnClient(client);
		uid = createSU(suDAO);
		query = "@(authors) (杨小澎)|(Xiaopeng Yang)|(yangxiaopeng)|(xiaopeng yang)|(yang xiaopeng)";
	}
	
	@Test
	public void testInitQuery(){
		JSONArray array = generateReturnJSON();
		EasyMock.expect(client.getDsnPapers(query, 0, 10, true)).andStubReturn(array);
		control.replay();
		List<Long> existIds = new ArrayList<Long>();
		existIds.add((long)1);
		JSONArray result = dsnService.initQuery(uid, 0, 10, existIds);
		for(int i=0; i<result.size(); i++){
			JSONObject obj1 = (JSONObject)result.get(i);
			JSONObject obj2 = (JSONObject)array.get(i);
			if(!((String)obj1.get(DsnClient.DSN_PAPER_ID)).equals("1")){
				Assert.assertEquals(obj1.get(DsnClient.AUTHORS), obj2.get(DsnClient.AUTHORS));
				Assert.assertEquals(obj1.get(DsnClient.DSN_PAPER_ID), obj2.get(DsnClient.DSN_PAPER_ID));
				Assert.assertEquals(obj1.get(DsnClient.SOURCE), obj2.get(DsnClient.SOURCE));
				Assert.assertEquals(obj1.get(DsnClient.TITLE), obj2.get(DsnClient.TITLE));
				Assert.assertEquals(obj1.get(DsnClient.VOLUMEISSUE), obj2.get(DsnClient.VOLUMEISSUE));
			}
		}
	}
	
	@Test
	public void testQuery(){
		JSONArray array = generateReturnJSON();
		EasyMock.expect(client.getDsnPapers("yang", 0, 10, false)).andStubReturn(array);
		control.replay();
		List<Long> existIds = new ArrayList<Long>();
		existIds.add((long)1);
		JSONArray result = dsnService.query(uid, "yang", 0, 10, existIds);
		for(int i=0; i<result.size(); i++){
			JSONObject obj1 = (JSONObject)result.get(i);
			JSONObject obj2 = (JSONObject)array.get(i);
			if(!((String)obj1.get(DsnClient.DSN_PAPER_ID)).equals("1")){
				Assert.assertEquals(obj1.get(DsnClient.AUTHORS), obj2.get(DsnClient.AUTHORS));
				Assert.assertEquals(obj1.get(DsnClient.DSN_PAPER_ID), obj2.get(DsnClient.DSN_PAPER_ID));
				Assert.assertEquals(obj1.get(DsnClient.SOURCE), obj2.get(DsnClient.SOURCE));
				Assert.assertEquals(obj1.get(DsnClient.TITLE), obj2.get(DsnClient.TITLE));
				Assert.assertEquals(obj1.get(DsnClient.VOLUMEISSUE), obj2.get(DsnClient.VOLUMEISSUE));
			}
		}
	}
	
	@Test
	public void testGetDsnPaper(){
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		obj.put(DsnClient.DSN_PAPER_ID, "12");
		array.add(obj);
		EasyMock.expect(client.getDsnPapersDetail("1")).andStubReturn(array);
		control.replay();
		JSONArray result = dsnService.getDsnPapers("1");
		JSONObject objResult = (JSONObject)result.get(0);
		Assert.assertEquals(obj.get(DsnClient.DSN_PAPER_ID), objResult.get(DsnClient.DSN_PAPER_ID));
	}
	
	@Test
	public void testGetUserNameString(){
		String result = dsnService.getUserNameString(uid);
		Assert.assertEquals("杨小澎,Xiaopeng Yang,yangxiaopeng,xiaopeng yang,yang xiaopeng", result.trim());
	}
	
	@After
	public void tearDown(){
		client = null;
		suDAO = null;
		dsnService = null;
	}
	
	private int createSU(ISimpleUserDAO suDAO){
		SimpleUser su = new SimpleUser();
		su.setEnName("Xiaopeng Yang");
		su.setPinyin("yangxiaopeng");
		su.setZhName("杨小澎");
		return suDAO.registAccount(su);
	}
	
	private JSONArray generateReturnJSON(){
		JSONArray array = new JSONArray();
		for(int i=0; i<10; i++){
			JSONObject obj = new JSONObject();
			obj.put(DsnClient.AUTHORS, "杨小澎");
			obj.put(DsnClient.DSN_PAPER_ID, i+1);
			obj.put(DsnClient.SOURCE, "test");
			obj.put(DsnClient.TITLE, "Paper "+(i+1));
			obj.put(DsnClient.VOLUMEISSUE, "vol."+(i+1));
			array.add(obj);
		}
		return array;
	}
}
