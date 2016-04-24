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

import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.InstitutionPatent;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;

public interface IInstitutionPatentDAO {

	List<InstitutionPatent> getPatent(int id, int i, int pageSize, SearchInstitutionCondition condition);
	List<InstitutionPatent> getPatentsByUID(int uid);

	int getPatentCount(int insId, SearchInstitutionCondition condition);
	List<InstitutionPatent> getPatentByUser(int userId, int i, int pageSize, SearchInstitutionCondition condition);
	int getPatentCountByUser(int userId, SearchInstitutionCondition condition);
	void deletePatent(int[] id);
	void updatePatent(int id,InstitutionPatent patent);
	void insert(List<InstitutionPatent> patents);
	int creat(InstitutionPatent patent);
	InstitutionPatent getPatentById(int id);
	Map<Integer, Integer> getGradesMap(int institutionId);
	Map<Integer, Integer> getGradesMapByUser(int userId);
	void deletePatentUser(int[] id);
	void insertPatentUser(int patentId, int userId);
	
    
    void deleteUserRef(int uid,int patentId);
    
    void createUserRef(int uid,int patentId,int athorOrder);
    void updateUserRef(int uid,int patentId,int authorOrder);
    
    int getPatentCount(int insId, int departId, int year);
    
    List<InstitutionPatent> getPatentsByAuthorId(int authorId,int startYear, int endYear);
}
