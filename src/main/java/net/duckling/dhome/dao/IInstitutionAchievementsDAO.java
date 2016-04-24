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

import net.duckling.dhome.domain.institution.InstitutionAchievements;
import net.duckling.dhome.domain.institution.InstitutionGrants;
import net.duckling.dhome.domain.institution.InstitutionJobApplication;

public interface IInstitutionAchievementsDAO {


	List<InstitutionAchievements> getList(int insId, int offset, int size);
//	List<InstitutionJobApplication> getListByUserId(int insId,int userId, int offset, int size);
	int getListCount(int insId);
//	int getListCountByUserId(int insId,int userId);
	
	void creat(InstitutionAchievements achievements);
	void delete(int applicationId);
	List<InstitutionAchievements> getAllList(int insId);
	InstitutionAchievements getById(int achievementsId);
}
