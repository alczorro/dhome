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
package net.duckling.dhome.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.dhome.domain.people.Education;
import net.duckling.dhome.service.IEducationService;

public class MockEducationService implements IEducationService {
	
	Map<Integer, Education> edus = new HashMap<Integer, Education>();

	@Override
	public List<Education> getEducationsByUid(int uid) {
		if(uid==108){
			return null;
		}
		return null;
	}

	@Override
	public Education getEducation(int id) {
		return edus.get(id);
	}

	@Override
	public int createEducation(Education edu) {
		int id = edus.size()+1;
		edu.setId(id);
		edus.put(id, edu);
		return id;
	}

	@Override
	public boolean updateEducationById(Education edu) {
		int id = edu.getId();
		Education temp = edus.get(id);
		temp.setBeginTime(edu.getBeginTime());
		temp.setDegree(edu.getDegree());
		temp.setDepartment(edu.getDepartment());
		temp.setEndTime(edu.getEndTime());
		temp.setInsitutionId(edu.getInsitutionId());
		temp.setUid(edu.getUid());
		temp.setUserName(edu.getUserName());
		edus.put(id, temp);
		return true;
	}

	@Override
	public void deleteEducation(int id) {
		edus.remove(new Integer(id));
	}

	@Override
	public List<Education> getEducationWithZeroInstitutionId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Education> getEducationWithNonZeroInstitutionId() {
		// TODO Auto-generated method stub
		return null;
	}

}
