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

import net.duckling.dhome.domain.institution.InstitutionAttachment;
import net.duckling.dhome.domain.institution.InstitutionAward;
import net.duckling.dhome.domain.institution.InstitutionAwardAuthor;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.PageResult;

public interface IInstitutionBackendAwardService {
	void insert(List<InstitutionAward> awards);
	void insert(InstitutionAward award);
	PageResult<InstitutionAward> getAwards(int insId,int page,SearchInstitutionCondition condition);
	List<InstitutionAward> getAwardsByUID(int uid);
	InstitutionAward getAward(int perId);
	int getAwardCount(int insId);
	int getAwardCount(int insId,SearchInstitutionCondition condition);
	void delete(int insId, int[] awardIds);
	void delete(int insId, int awardId);
	void deleteAll(int insId);
	
	/**
	 * 删除与员工关联
	 * @param id
	 */
	void deleteUserRef(int uid,int awardId);
	
	void createUserRef(int uid,int awardId,int authorOrder);
	void updateUserRef(int uid,int awardId,int authorOrder);
	void update(InstitutionAward award,int authorId,int order);
	int create(InstitutionAward award,int institutionId,int authorId,int order);
	
	int create(InstitutionAward award,int institutionId,int[] authorIds,int[] order,
			boolean[] communicationAuthors, boolean[] authorStudents, boolean[] authorTeacher,int[] clbIds,String[] fileNames);
	void update(InstitutionAward award,int institutionId,int[] authorIds,int[] order, 
			boolean[] communicationAuthors, boolean[] authorStudents, boolean[] authorTeacher,int[] clbIds,String[] fileNames);

	void createAuthor(InstitutionAwardAuthor author);
	
	List<InstitutionAwardAuthor> getAuthorByawardId(int awardId);
	List<InstitutionAwardAuthor> searchAuthor(String keyword);
	Map<Integer, List<InstitutionAwardAuthor>> getAuthorsMap(List<Integer> awardIds);
	InstitutionAwardAuthor getAuthorById(int awardId,int authorId);
	
	void createClbRef(int awardId,int[] clbIds,String[] fileNames);
	void deleteClbRef(int awardId);
	List<InstitutionAttachment> getAttchments(int awardId);
	
	Map<Integer, Integer> getYearsMap(int insId);
	Map<Integer, Integer> getGradesMap(int insId);
	
	PageResult<InstitutionAward> getAwardByUser(int userId,int page, SearchInstitutionCondition condition);
	 int getAwardCountByUser(int userId);
	 Map<Integer, Integer> getYearByUser(int userId);
	 void deleteAwardUser(int[] awardId);
	 void insertAwardUser(int awardId, int userId);
		 List<InstitutionAward> getAwardByKeyword(int offset, int size, String keyword);
		 List<InstitutionAward> getAwardByInit(int offset, int size,String userName);
		 void insertAward(final String[] awardId,final int userId);
		 List<Integer> getExistAwardIds(int uid);
}
