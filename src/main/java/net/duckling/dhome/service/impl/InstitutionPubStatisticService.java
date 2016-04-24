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

import net.duckling.dhome.dao.IInstitutionStatisticDAO;
import net.duckling.dhome.domain.institution.InstitutionPublicationStatistic;
import net.duckling.dhome.service.IInstitutionPubStatisticService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 机构的论文统计服务
 * @author Yangxp
 *
 */
@Service
public class InstitutionPubStatisticService implements
		IInstitutionPubStatisticService {
	
	@Autowired
	private IInstitutionStatisticDAO isDAO;

	@Override
	public int create(InstitutionPublicationStatistic is) {
		return isDAO.create(is);
	}

	@Override
	public void updateById(InstitutionPublicationStatistic is) {
		isDAO.updateById(is);
	}

	@Override
	public InstitutionPublicationStatistic getStatisticById(int id) {
		return isDAO.getStatisticById(id);
	}

	@Override
	public List<InstitutionPublicationStatistic> getStatisticsByInstitutionId(int insId) {
		return isDAO.getStatisticsByInstitutionId(insId);
	}

}
