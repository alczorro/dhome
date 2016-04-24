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
package net.duckling.dhome.service;

import java.util.List;
import java.util.Map;

import net.duckling.dhome.domain.institution.InstitutionAcademic;
import net.duckling.dhome.domain.institution.InstitutionAcademicAuthor;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.PageResult;

public interface IInstitutionBackendAcademicService {
	void insert(List<InstitutionAcademic> academics);
	void insert(InstitutionAcademic academic);
	PageResult<InstitutionAcademic> getAcademics(int insId,int page,SearchInstitutionCondition condition);
	List<InstitutionAcademic> getAcademicsByUID(int uid);

	InstitutionAcademic getAcademic(int acmId);
	int getAcademicCount(int insId);
	int getAcademicCount(int insId,SearchInstitutionCondition condition);
	void delete(int insId, int[] acmIds);
	void delete(int insId, int acmId);
	void deleteAll(int insId);
	
	/**
	 * 删除与员工关联
	 * @param id
	 */
	void deleteUserRef(int uid,int acmId);
	
	void createUserRef(int uid,int acmId);
	void update(InstitutionAcademic academic,int authorId,int order);
	int create(InstitutionAcademic academic,int institutionId,String authorId,int order);
	
	
	int create(InstitutionAcademic academic,int institutionId,String[] authorIds);
	void update(InstitutionAcademic academic,int institutionId,String[] authorIds);

//	void createAuthor(InstitutionAcademicAuthor author);
	
	List<InstitutionAcademicAuthor> getAuthorByAcademicId(int acmId);
//	List<InstitutionAcademicAuthor> searchAuthor(String keyword);
	Map<Integer, List<InstitutionAcademicAuthor>> getAuthorsMap(List<Integer> acmIds);
	InstitutionAcademicAuthor getAuthorById(int acmId,String authorId);
	
	 PageResult<InstitutionAcademic> getAcademicByUser(int userId,int page, SearchInstitutionCondition condition);
	 int getAcademicCountByUser(int userId);
//	 Map<Integer, Integer> getGradesMapByUser(int userId);
	 void deleteAcademicUser(int[] academicId);
	 void insertAcademicUser(int academicId, int userId);
		 List<InstitutionAcademic> getAcademicByKeyword(int offset, int size, String keyword);
		 List<InstitutionAcademic> getAcademicByInit(int offset, int size,String userName);
		 void insertAcademic(final String[] academicId,final int userId);
		 List<Integer> getExistAcademicIds(int uid);
		 
	Map<Integer, Integer> getPositionsMap(int insId);	 
		 
	 
}
