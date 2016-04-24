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

import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.dhome.domain.institution.InstitutionTreatise;
import net.duckling.dhome.domain.institution.InstitutionTreatise;
import net.duckling.dhome.domain.institution.InstitutionTreatiseAuthor;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.PageResult;

public interface IInstitutionBackendTreatiseService {
	void insert(List<InstitutionTreatise> treatises);
	void insert(InstitutionTreatise treatise);
	PageResult<InstitutionTreatise> getTreatises(int insId,int page,SearchInstitutionCondition condition);
	List<InstitutionTreatise> getTreatisesByUID(int uid);
	InstitutionTreatise getTreatise(int perId);
	int getTreatiseCount(int insId);
	int getTreatiseCount(int insId,SearchInstitutionCondition condition);
	void delete(int insId, int[] treatiseIds);
	void delete(int insId, int treatiseId);
	void deleteAll(int insId);
	
	/**
	 * 删除与员工关联
	 * @param id
	 */
	void deleteUserRef(int uid,int tid);
	
	void createUserRef(int uid,int tid,int authorOrder);
	void updateUserRef(int uid,int tid,int authorOrder);
	void update(InstitutionTreatise treatise,int authorId,int order);
	int create(InstitutionTreatise treatise,int institutionId,int authorId,int order);
	
	int create(InstitutionTreatise treatise,int institutionId,int[] authorIds,int[] order, boolean[] communicationAuthors, boolean[] authorStudents, boolean[] authorTeacher);
	void update(InstitutionTreatise treatise,int institutionId,int[] authorIds,int[] order, boolean[] communicationAuthors, boolean[] authorStudents, boolean[] authorTeacher);
    

	void createAuthor(InstitutionTreatiseAuthor author);
	
	List<InstitutionTreatiseAuthor> getAuthorByTreatiseId(int treatiseId);
	List<InstitutionTreatiseAuthor> searchAuthor(String keyword);
	Map<Integer, List<InstitutionTreatiseAuthor>> getAuthorsMap(List<Integer> treatiseIds);
	InstitutionTreatiseAuthor getAuthorById(int treatiseId,int authorId);
	
	 PageResult<InstitutionTreatise> getTreatiseByUser(int userId,int page, SearchInstitutionCondition condition);
	 int getTreatiseCountByUser(int userId);
//	 Map<Integer, Integer> getGradesMapByUser(int userId);
	 void deleteTreatiseUser(int[] treatiseId);
	 void insertTreatiseUser(int treatiseId, int userId);
		 List<InstitutionTreatise> getTreatiseByKeyword(int offset, int size, String keyword);
		 List<InstitutionTreatise> getTreatiseByInit(int offset, int size,String userName);
		 void insertTreatise(String[] treatiseId, int userId);
		 List<Integer> getExistTreatiseIds(int uid);
		 
	 Map<Integer, Integer> getPublishersMap(int insId);
	 List<InstitutionDepartment> getDepartments(int insId);
	 //批量导入
	 List<InstitutionTreatise> insertTreatises(List<InstitutionTreatise> treatises);
	 int create(InstitutionTreatise treatise);
	 int insertAuthor(String name,String company);
	 void insertRef(InstitutionTreatise treatise);
}
