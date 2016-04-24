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

import net.duckling.dhome.domain.institution.InstitutionAcademic;
import net.duckling.dhome.domain.institution.InstitutionAcademic;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;

/**
 * 学术任职数据库接口
 * @author liyanzhao
 *
 */
public interface IInstitutionAcademicDAO {
	
	void insert(List<InstitutionAcademic> academics);
	void insert(InstitutionAcademic academic);
	List<InstitutionAcademic> getAcademics(int insId,int offset,int pageSize,SearchInstitutionCondition condition);
	List<InstitutionAcademic> getAcademicsByUID(int uid);

	InstitutionAcademic getAcademic(int acmId);
	int getAcademicCount(int insId,SearchInstitutionCondition condition);
	
	void delete(int insId, int[] acmId);
	void delete(int insId,int acmId);
    void deleteAll(int insId);
	
	int create(InstitutionAcademic academic);
	void update(InstitutionAcademic academic);
	
    void deleteUserRef(int uid,int acmId);
    void createUserRef(int uid,int acmId);
	
	List<InstitutionAcademic> getAcademicByUser(int userId, int i, int pageSize, SearchInstitutionCondition condition);
	int getAcademicCountByUser(int userId, SearchInstitutionCondition condition);
//	Map<Integer, Integer> getFundsFromsMapByUser(int userId);
	void deleteAcademicUser(int[] id);
	void insertAcademicUser(int academicId, int userId);
	
	Map<Integer, Integer> getPositionsMap(int insId);
	
	int getAcademicCount(int insId, int departId, int year);
	
	//根据作者获取学术任职
	List<InstitutionAcademic> getAcademicsByAuthorId(int userId,int startYear, int endYear);
}
