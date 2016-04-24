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

import net.duckling.dhome.domain.people.Education;
import net.duckling.dhome.service.IEducationService;

/**
 * @author lvly
 * @since 2012-8-22
 */
public class MockEduService implements IEducationService{

	@Override
	public List<Education> getEducationsByUid(int uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Education getEducation(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int createEducation(Education edu) {
		// TODO Auto-generated method stub
		return 0;
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
