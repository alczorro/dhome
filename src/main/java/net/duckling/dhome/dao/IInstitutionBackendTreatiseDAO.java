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
import net.duckling.dhome.domain.institution.InstitutionPeriodical;
import net.duckling.dhome.domain.institution.InstitutionTreatise;
import net.duckling.dhome.domain.institution.InstitutionTreatise;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;

/**
 * 论著数据库接口
 * @author liyanzhao
 *
 */
public interface IInstitutionBackendTreatiseDAO {
	
	void insert(List<InstitutionTreatise> treatises);
	void insert(InstitutionTreatise treatise);
	List<InstitutionTreatise> getTreatises(int insId,int offset,int pageSize,SearchInstitutionCondition condition);
	List<InstitutionTreatise> getTreatisesByUID(int uid);
	InstitutionTreatise getTreatise(int treatiseId);
	int getTreatiseCount(int insId,SearchInstitutionCondition condition);
	
	void delete(int insId, int[] treatiseId);
	void delete(int insId,int treatiseId);
    void deleteAll(int insId);
    
    void deleteUserRef(int uid,int tid);
    
    void createUserRef(int uid,int tid,int athorOrder);
    void updateUserRef(int uid,int tid,int authorOrder);
    
    Map<Integer,Integer> getPublishersMap(int insId);
	
	int create(InstitutionTreatise treatise);
	void update(InstitutionTreatise treatise);
	List<InstitutionTreatise> getTreatiseByUser(int userId, int i, int pageSize, SearchInstitutionCondition condition);
	int getTreatiseCountByUser(int userId, SearchInstitutionCondition condition);
//	Map<Integer, Integer> getFundsFromsMapByUser(int userId);
	void deleteTreatiseUser(int[] id);
	void insertTreatiseUser(int treatiseId, int userId);
	void updateTreatises(InstitutionTreatise treatise);
	
	List<InstitutionTreatise> insertTreatises(List<InstitutionTreatise> treatises);
	int getTreatiseId(String name);
	int getTreatiseCount(int ins, int departId, int year);
}
