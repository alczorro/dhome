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

import net.duckling.dhome.domain.institution.InstitutionPeriodical;
import net.duckling.dhome.domain.institution.InstitutionPeriodicalAuthor;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.PageResult;

public interface IInstitutionBackendPeriodicalService {
	void insert(List<InstitutionPeriodical> periodicals);
	void insert(InstitutionPeriodical periodical);
	PageResult<InstitutionPeriodical> getPeriodicals(int insId,int page,SearchInstitutionCondition condition);
	List<InstitutionPeriodical> getPeriodicalsByUID(int uid);
	
	InstitutionPeriodical getPeriodical(int perId);
	
	int getPeriodicalCount(int insId);
	int getPeriodicalCount(int insId,SearchInstitutionCondition condition);
	
	void delete(int insId, int[] perIds);
	void delete(int insId, int perId);
	void deleteAll(int insId);
	
	
	/**
	 * 删除与员工关联
	 * @param id
	 */
	void deleteUserRef(int uid,int perId);
	
	void createUserRef(int uid,int perId);
	void update(InstitutionPeriodical periodical,int authorId,int order);
	int create(InstitutionPeriodical periodical,int institutionId,String authorId,int order);
	
	
	int create(InstitutionPeriodical periodical,int institutionId,String[] authorIds);
	void update(InstitutionPeriodical periodical,int institutionId,String[] authorIds);

//	void createAuthor(InstitutionPeriodicalAuthor author);
	
	List<InstitutionPeriodicalAuthor> getAuthorByPerId(int perId);
//	List<InstitutionPeriodicalAuthor> searchAuthor(String keyword);
	Map<Integer, List<InstitutionPeriodicalAuthor>> getAuthorsMap(List<Integer> perIds);
	InstitutionPeriodicalAuthor getAuthorById(int perId,String authorId);
	
	 PageResult<InstitutionPeriodical> getPeriodicalByUser(int userId,int page, SearchInstitutionCondition condition);
	 int getPeriodicalCountByUser(int userId);
//	 Map<Integer, Integer> getGradesMapByUser(int userId);
	 void deletePeriodicalUser(int[] periodicalId);
	 void insertPeriodicalUser(int periodicalId, int userId);
		 List<InstitutionPeriodical> getPeriodicalByKeyword(int offset, int size, String keyword);
		 List<InstitutionPeriodical> getPeriodicalByInit(int offset, int size,String userName);
		 void insertPeriodical(final String[] periodicalId,final int userId);
		 Map<Integer, Integer> getPositionsMap(int insId);	
		 List<Integer> getExistPeriodicalIds(int uid);
}
