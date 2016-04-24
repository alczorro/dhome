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

import java.util.List;

import net.duckling.dhome.dao.IInstitutionScholarEventDAO;
import net.duckling.dhome.domain.institution.ScholarEvent;
import net.duckling.dhome.domain.institution.ScholarEventDetail;

public class MockInstitutionScholarEventDAO implements
		IInstitutionScholarEventDAO {

	@Override
	public int create(ScholarEvent se) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateByID(ScholarEvent se) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public ScholarEvent getScholarEventByID(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ScholarEventDetail> getAllScholarEventOfInstitution(int insId,
			int offset, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ScholarEventDetail> getUpcomingScholarEventOfInstitution(
			int insId, int offset, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ScholarEventDetail> getExpiredScholarEventOfInstitution(
			int insId, int offset, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getActivityCount(int institutionId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<ScholarEventDetail> getOngoingScholarEventOfInstitution(
			int insId, int offset, int size) {
		// TODO Auto-generated method stub
		return null;
	}

}
