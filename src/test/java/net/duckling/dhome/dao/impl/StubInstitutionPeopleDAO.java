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
package net.duckling.dhome.dao.impl;

import java.util.ArrayList;
import java.util.List;

import net.duckling.dhome.dao.IInstitutionPeopleDAO;
import net.duckling.dhome.domain.people.SimpleUser;

/**
 * @author lvly
 * @since 2012-9-21
 */
public class StubInstitutionPeopleDAO implements IInstitutionPeopleDAO {

	@Override
	public int createInstitutionPeople(int userId, int institutionId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isExists(int userId, int institutionId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SimpleUser> getPeoplesByInstitituionId(int institutionId, int offset, int size) {
		List<SimpleUser> list=new ArrayList<SimpleUser>();
		SimpleUser user=new SimpleUser();
		user.setId(1);
		user.setZhName("name1");
		list.add(user);
		SimpleUser user1=new SimpleUser();
		user1.setId(2);
		user1.setZhName("name2");
		list.add(user1);
		return list;
	}

	@Override
	public List<Integer> getInstitutionHasPerson() {
		List<Integer> result = new ArrayList<Integer>();
		result.add(0);
		return result;
	}

	@Override
	public int getMembersSize(int institutionId) {
		if(institutionId==12){
			return 12;
		}
		return 0;
	}

	@Override
	public void deleteMember(int institutionId, int uid) {

	}

	@Override
	public boolean isMember(int uid, int institutionId) {
		return uid>0&&institutionId==112;
	}

}
