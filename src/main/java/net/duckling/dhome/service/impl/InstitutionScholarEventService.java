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
package net.duckling.dhome.service.impl;

import java.util.List;

import net.duckling.dhome.dao.IInstitutionScholarEventDAO;
import net.duckling.dhome.domain.institution.ScholarEvent;
import net.duckling.dhome.domain.institution.ScholarEventDetail;
import net.duckling.dhome.service.IInstitutionScholarEventService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 机构学术活动服务
 * @author Yangxp
 * @since 2012-09-25
 */
@Service
public class InstitutionScholarEventService implements
		IInstitutionScholarEventService {

	@Autowired
	private IInstitutionScholarEventDAO eventDAO;
	
	public void setEventDAO(IInstitutionScholarEventDAO eventDAO) {
		this.eventDAO = eventDAO;
	}

	@Override
	public int create(ScholarEvent se) {
		return eventDAO.create(se);
	}

	@Override
	public void updateByID(ScholarEvent se) {
		eventDAO.updateByID(se);
	}

	@Override
	public void remove(int id) {
		eventDAO.remove(id);
	}

	@Override
	public ScholarEvent getScholarEventByID(int id) {
		return eventDAO.getScholarEventByID(id);
	}

	@Override
	public List<ScholarEventDetail> getAllScholarEventOfInstitution(int insId, int offset, int size) {
		return eventDAO.getAllScholarEventOfInstitution(insId, offset, size);
	}

	@Override
	public List<ScholarEventDetail> getUpcomingScholarEventOfInstitution(int insId,
			int offset, int size) {
		return eventDAO.getUpcomingScholarEventOfInstitution(insId, offset, size);
	}

	@Override
	public List<ScholarEventDetail> getOngoingScholarEventOfInstitution(
			int insId, int offset, int size) {
		return eventDAO.getOngoingScholarEventOfInstitution(insId, offset, size);
	}

	@Override
	public List<ScholarEventDetail> getExpiredScholarEventOfInstitution(int insId,
			int offset, int size) {
		return eventDAO.getExpiredScholarEventOfInstitution(insId, offset, size);
	}
	@Override
	public int getCount(int institutionId) {
		return eventDAO.getActivityCount(institutionId);
	}

}
