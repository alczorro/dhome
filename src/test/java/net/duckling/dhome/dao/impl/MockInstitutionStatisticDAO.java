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

import java.util.ArrayList;
import java.util.List;

import net.duckling.dhome.dao.IInstitutionStatisticDAO;
import net.duckling.dhome.domain.institution.InstitutionPublicationStatistic;

public class MockInstitutionStatisticDAO implements IInstitutionStatisticDAO {

	private List<InstitutionPublicationStatistic> temp;

	public MockInstitutionStatisticDAO(){
		temp = new ArrayList<InstitutionPublicationStatistic>();
	}
	
	@Override
	public int create(InstitutionPublicationStatistic is) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] batchCreate(List<InstitutionPublicationStatistic> statList) {
		if(null != temp && null != statList){
			for(InstitutionPublicationStatistic ips : statList){
				temp.add(ips);
			}
		}
		return null;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

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
			return temp;
		}
		return null;
	}

}
