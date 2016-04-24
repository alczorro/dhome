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

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import net.duckling.dhome.domain.institution.ScholarEvent;
import net.duckling.dhome.domain.institution.ScholarEventDetail;
import net.duckling.dhome.service.IInstitutionScholarEventService;

/**
 * @author lvly
 * @since 2012-9-21
 */
public class StubInstitutionScholarEventService implements IInstitutionScholarEventService{

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
		if(12 == id){
			ScholarEvent se = new ScholarEvent();
			se.setCreator(12);
			se.setId(0);
			return se;
		}
		return null;
	}

	@Override
	public List<ScholarEventDetail> getAllScholarEventOfInstitution(int insId, int offset, int size) {
		if(insId == 12){
			return new ArrayList<ScholarEventDetail>();
		}else if(insId == 11){
			List<ScholarEventDetail> result = new ArrayList<ScholarEventDetail>();
			ScholarEventDetail sed = new ScholarEventDetail();
			sed.setCreator(12);
			sed.setCreatorName("test");
			sed.setCreateTime(Date.valueOf("2012-09-09"));
			sed.setEndTime(Date.valueOf("2012-09-09"));
			sed.setStartTime(Date.valueOf("2012-09-09"));
			result.add(sed);
			return result;
		}
		return null;
	}

	@Override
	public List<ScholarEventDetail> getUpcomingScholarEventOfInstitution(int insId, int offset, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ScholarEventDetail> getExpiredScholarEventOfInstitution(int insId, int offset, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCount(int institutionId) {
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
