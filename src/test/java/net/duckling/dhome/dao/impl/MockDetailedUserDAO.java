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
package net.duckling.dhome.dao.impl;

import java.util.HashMap;
import java.util.Map;

import net.duckling.dhome.dao.IDetailedUserDAO;
import net.duckling.dhome.domain.people.DetailedUser;

public class MockDetailedUserDAO implements IDetailedUserDAO{

	private Map<Integer, DetailedUser> users = new HashMap<Integer, DetailedUser>();
	
	@Override
	public int createDetailedUser(DetailedUser dUser) {
		int size = users.size();
		if(null != dUser){
			dUser.setId(size+1);
			users.put(dUser.getUid(), dUser);
		}
		return size;
	}

	@Override
	public DetailedUser getUser(int uid) {
		for(Map.Entry<Integer, DetailedUser> entry : users.entrySet()){
			DetailedUser du = entry.getValue();
			if(du.getUid() == uid){
				return du;
			}
		}
		return null;
	}

	@Override
	public boolean updateDetailedUserByUid(DetailedUser du) {
		DetailedUser temp = getUser(du.getUid());
		if(null != temp){
			temp.setBirthday(du.getBirthday());
			temp.setBlogUrl(du.getBlogUrl());
			temp.setFirstClassDiscipline(du.getFirstClassDiscipline());
			temp.setGender(du.getGender());
			temp.setIntroduction(du.getIntroduction());
			temp.setSecondClassDiscipline(du.getSecondClassDiscipline());
			temp.setWeiboUrl(du.getWeiboUrl());
			users.put(du.getUid(), temp);
			return true;
		}
		return false;
	}

}
