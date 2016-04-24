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

import net.duckling.dhome.dao.IEducationDAO;
import net.duckling.dhome.domain.people.Education;

/**
 * @author lvly
 * @since 2012-9-21
 */
public class StubEducationDAO implements IEducationDAO{
	@Override
	public int getCitedEducationCount(int institutionId, int uid) {
		if(institutionId==112&&uid==12){
			return 0;
		}
		return 1;
	}
	
	@Override
	public int createEducation(Education edu) {
		return 12;
	}
	@Override
	public List<Education> getEdusByUID(int uid) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Education getEducation(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean updateEducationById(Education edu) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void deleteEducation(int id) {
		// TODO Auto-generated method stub
		
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



