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
package net.duckling.dhome.service.impl.mock;

import java.util.ArrayList;
import java.util.List;

import net.duckling.dhome.domain.institution.InstitutionPublicationStatistic;
import net.duckling.dhome.service.IInstitutionPubStatisticService;

public class StubInstitutionPubStatisticService implements
		IInstitutionPubStatisticService {

	@Override
	public int create(InstitutionPublicationStatistic is) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateById(InstitutionPublicationStatistic is) {
		// TODO Auto-generated method stub

	}

	@Override
	public InstitutionPublicationStatistic getStatisticById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InstitutionPublicationStatistic> getStatisticsByInstitutionId(
			int insId) {
		if(insId == 0){
			List<InstitutionPublicationStatistic> result = new ArrayList<InstitutionPublicationStatistic>();
			result.add(build(insId, 2009));
			result.add(build(insId, 2011));
			return result;
		}
		return null;
	}
	
	private InstitutionPublicationStatistic build(int insId, int year){
		InstitutionPublicationStatistic ips = new InstitutionPublicationStatistic();
		ips.setAnnualCitationCount(0);
		ips.setAnnualPaperCount(0);
		ips.setId(1);
		ips.setInstitutionId(insId);
		ips.setTotalCitationCount(0);
		ips.setTotalPaperCount(0);
		ips.setYear(year);
		return ips;
	}

}
