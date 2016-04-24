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
package net.duckling.dhome.service.impl.mock;

import java.util.ArrayList;
import java.util.List;

import net.duckling.dhome.domain.people.Friend;
import net.duckling.dhome.service.IFriendService;

/**
 * @author lvly
 * @since 2012-10-15
 */
public class StubFriendService implements IFriendService{
	@Override
	public List<Friend> getFriendsByAccess(int userId) {
		if(userId==0){
			List<Friend> friends=new ArrayList<Friend>();
			Friend f1=new Friend();
			f1.setDomain("test1");
			Friend f2=new Friend();
			f2.setDomain("test2");
			friends.add(f1);
			friends.add(f2);
			return friends;
		}
		return null;
	}
	@Override
	public List<Friend> getFriendsByInstitution(int userId, int offset, int size) {
		
		if(userId==0){
			List<Friend> friends=new ArrayList<Friend>();
			Friend f1=new Friend();
			f1.setDomain("test1");
			Friend f2=new Friend();
			f2.setDomain("test2");
			friends.add(f1);
			friends.add(f2);
			return friends;
		}
		return null;
	}

	@Override
	public List<Friend> getFriendsByDiscipline(int userId, int offset, int size) {
		if(userId==0){
			List<Friend> friends=new ArrayList<Friend>();
			Friend f1=new Friend();
			f1.setDomain("test2");
			Friend f2=new Friend();
			f2.setDomain("test3");
			friends.add(f1);
			friends.add(f2);
			return friends;
		}
		return null;
	}

	@Override
	public List<Friend> getFriendsByInterest(int userId, int offset, int size) {
		if(userId==0){
			List<Friend> friends=new ArrayList<Friend>();
			Friend f1=new Friend();
			f1.setDomain("test3");
			Friend f2=new Friend();
			f2.setDomain("test4");
			friends.add(f1);
			friends.add(f2);
			return friends;
		}
		return null;
	}

}
