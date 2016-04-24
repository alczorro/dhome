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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.dhome.domain.people.Home;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IHomeService;

public class MockHomeService implements IHomeService {

	@Override
	public String getDomain(int uid) {
		if(uid == 108){
			return "yangxp";
		}
		return "Mockdomain";
	}

	@Override
	public SimpleUser getSimpleUserByDomain(String domain) {
		if("yxp".equals(domain)){
			return null;
		}
		return new SimpleUser();
	}

	@Override
	public Home getHomeByDomain(String domain) {
		return new Home();
	}

	@Override
	public boolean updateHome(Home home) {
		
		return false;
	}

	@Override
	public Map<Integer, String> getDomainByUID(List<Integer> uids) {
		Map<Integer, String> result = new HashMap<Integer, String>();
		if(null!=uids){
			for(int uid : uids){
				switch(uid){
					case 0 : result.put(0, "xiaoming");break;
					case 108: result.put(108, "yang");break;
					default : break;
				}
			}
		}
		return result;
	}

	@Override
	public List<Home> getDomainsByUids(List<Integer> uids) {
		// TODO Auto-generated method stub
		return null;
	}


}
