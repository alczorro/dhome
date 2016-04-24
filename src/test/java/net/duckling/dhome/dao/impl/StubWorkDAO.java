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

import java.util.List;

import net.duckling.dhome.dao.IWorkDAO;
import net.duckling.dhome.domain.people.Work;

/**
 * @author lvly
 * @since 2012-9-21
 */
public class StubWorkDAO implements IWorkDAO{

	@Override
	public int createWork(Work work) {
		return 0;
	}

	@Override
	public List<Work> getWorksByUID(int uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Work getWork(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateWorkById(Work work) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void deleteWork(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getCitedWorkCount(int institutionId, int uid) {
		if(institutionId==112&&uid==12){
			return 1;
		}
		return 0;
	}

	@Override
	public List<Work> getWorkWithZeroInstitutionId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Work> getWorkWithNonZeroInstitutionId() {
		// TODO Auto-generated method stub
		return null;
	}

}
