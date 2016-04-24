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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.dhome.domain.institution.Institution;
import net.duckling.dhome.domain.people.Work;
import net.duckling.dhome.service.IWorkService;

/**
 * @author lvly
 * @since 2012-8-22
 */
public class MockWorkService implements IWorkService{

	Map<Integer, Work> works = new HashMap<Integer, Work>();
	
	@Override
	public List<Work> getWorksByUID(int uid) {
		if(uid==108){
			return null;
		}
		return null;
	}

	@Override
	public Work getWork(int id) {
		return works.get(id);
	}

	@Override
	public Institution getInstitutionByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int createInstitution(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean updateWorkById(Work work) {
		int id = work.getId();
		Work temp = works.get(id);
		temp.setBeginTime(work.getBeginTime());
		temp.setDepartment(work.getDepartment());
		temp.setEndTime(work.getEndTime());
		temp.setFirstClassDiscipline(work.getFirstClassDiscipline());
		temp.setInstitutionId(work.getInstitutionId());
		temp.setPosition(work.getPosition());
		temp.setSecondClassDiscipline(work.getSecondClassDiscipline());
		temp.setUserName(work.getUserName());
		temp.setUid(work.getUid());
		works.put(id, temp);
		return true;
	}

	@Override
	public int createWork(Work work) {
		int id = works.size()+1;
		work.setId(id);
		works.put(id, work);
		return id;
	}

	@Override
	public void delteWork(int id) {
		works.remove(new Integer(id));
	}

	@Override
	public List<Institution> getDsnInstitutionsByPrefixName(String prefix) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int searchOfficalInstitution(String insName) {
		// TODO Auto-generated method stub
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
