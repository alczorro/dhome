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

import java.util.List;

import net.duckling.dhome.domain.people.DetailedUser;
import net.duckling.dhome.domain.people.Discipline;
import net.duckling.dhome.domain.people.Home;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IRegistService;

/**
 * @author lvly
 * @since 2012-8-23
 */
public class MockRegistService implements IRegistService {

	@Override
	public int createSimpleUser(SimpleUser user, String password, boolean umtExist) {
		return 1;
	}

	@Override
	public int createDetailedUser(DetailedUser user) {
		return 0;
	}

	@Override
	public boolean isEmailUsed(String email) {
		return false;
	}

	@Override
	public boolean hasCreateHomePage(String email) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDomainUsed(String domain) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int createEducation(int uid, String degree, String department, String institution) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int createWork(int id, String position, String department, String institution) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateSimpleUser(SimpleUser user) {
		// TODO Auto-generated method stub

	}

	@Override
	public int createHome(Home home) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Discipline> getRootDiscipline() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Discipline> getChildDiscipline(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
