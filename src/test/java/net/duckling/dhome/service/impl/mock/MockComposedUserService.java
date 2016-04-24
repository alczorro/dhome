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

import java.util.ArrayList;
import java.util.List;

import net.duckling.dhome.domain.people.ComposedUser;
import net.duckling.dhome.domain.people.DisciplineStructrue;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IComposedUserService;

public class MockComposedUserService implements IComposedUserService {
	@Override
	public List<ComposedUser> getComposedUsersByInterest(String keyword, int offset, int size) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getSearchComposedUsersCount(String keyword) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public List<ComposedUser> getAllComposedUsers(int offset, int size) {
		return null;
	}

	@Override
	public List<ComposedUser> getLatestComposedUsers(int offset, int size) {
		if(offset == 0 && size ==4){
			List<ComposedUser> users = new ArrayList<ComposedUser>();
			ComposedUser cu = new ComposedUser();
			SimpleUser su = new SimpleUser();
			su.setZhName("test");
			su.setImage(1);
			cu.setSimpleUser(su);
			cu.setUrl("/test");
			users.add(cu);
			return users;
		}
		return null;
	}

	@Override
	public DisciplineStructrue getDiscipline() {
		return null;
	}

	@Override
	public List<ComposedUser> getComposedUsersByDiscipline(int first,
			int second, int offset, int size) {
		return null;
	}

	@Override
	public List<ComposedUser> searchComposedUsers(String keyword, int offset,
			int size) {
		return null;
	}

}
