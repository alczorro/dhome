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

import net.duckling.dhome.domain.institution.InstitutionCopyright;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;

public interface IInstitutionCopyrightDAO {

	List<InstitutionCopyright> getCopyright(int id, int i, int pageSize, SearchInstitutionCondition condition);
	List<InstitutionCopyright> getCopyrightsByUID(int uid);

	int getCopyrightCount(int insId, SearchInstitutionCondition condition);
	List<InstitutionCopyright> getCopyrightByUser(int userId, int i, int pageSize, SearchInstitutionCondition condition);
	int getCopyrightCountByUser(int userId, SearchInstitutionCondition condition);
	void deleteCopyright(int[] id);
	void updateCopyright(int id,InstitutionCopyright copyright);
	void insert(List<InstitutionCopyright> copyrights);
	int creat(InstitutionCopyright copyright);
	InstitutionCopyright getCopyrightById(int id);
	Map<Integer, Integer> getGradesMap(int institutionId);
	Map<Integer, Integer> getGradesMapByUser(int userId);
	void deleteCopyrightUser(int[] id);
	void insertCopyrightUser(int copyrightId, int userId);
	
    void deleteUserRef(int uid,int copyrightId);
    
    void createUserRef(int uid,int copyrightId,int athorOrder);
    void updateUserRef(int uid,int copyrightId,int authorOrder);
    
    int getCopyrightCount(int insId, int departId, int year);
    
    List<InstitutionCopyright> getCopyrightsByAuthorId(int authorId,int startYear, int endYear);
}
