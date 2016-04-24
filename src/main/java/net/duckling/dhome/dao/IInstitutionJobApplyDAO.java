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
package net.duckling.dhome.dao;

import java.util.List;
import java.util.Map;

import net.duckling.dhome.domain.institution.InstitutionGrants;
import net.duckling.dhome.domain.institution.InstitutionJobApplication;
import net.duckling.dhome.domain.institution.InstitutionJobApply;

public interface IInstitutionJobApplyDAO {


	List<InstitutionJobApply> getListByApplicationId(int applicationId,int insId, int offset, int size);
	int getListCountByApplicationId(int applicationId,int insId);
	List<InstitutionJobApply> getListByUserId(int insId,int userId, int offset, int size);
	int getListCountByUserId(int insId,int userId);
	
//	void creat(InstitutionJobApplication jobApplication);
	void delete(int insId,int applicationId);
	void deleteById(int applyId);
	void insert(int insId,int userId,int applicationId,int status,InstitutionJobApply jobApply);
	void update(int applyId,int status,InstitutionJobApply jobApply);
	InstitutionJobApply getById(int applyId);
	List<InstitutionJobApply> getIdByUser(int userId, int insId);
}
