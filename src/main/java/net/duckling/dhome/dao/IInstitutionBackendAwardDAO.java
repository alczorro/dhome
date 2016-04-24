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

import net.duckling.dhome.domain.institution.InstitutionAttachment;
import net.duckling.dhome.domain.institution.InstitutionAward;
import net.duckling.dhome.domain.institution.InstitutionAward;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;

/**
 * 论著数据库接口
 * @author liyanzhao
 *
 */
public interface IInstitutionBackendAwardDAO {
	
	void insert(List<InstitutionAward> awards);
	void insert(InstitutionAward award);
	List<InstitutionAward> getAwards(int insId,int offset,int pageSize,SearchInstitutionCondition condition);
	List<InstitutionAward> getAwardsByUID(int uid);

	InstitutionAward getAward(int awardId);
	int getAwardCount(int insId,SearchInstitutionCondition condition);
	
	void delete(int insId, int[] awardIds);
	void delete(int insId,int awardId);
    void deleteAll(int insId);
	
    void deleteUserRef(int uid,int awardId);
    
    void createUserRef(int uid,int awardId,int athorOrder);
    void updateUserRef(int uid,int awardId,int authorOrder);
    
	int create(InstitutionAward award);
	void update(InstitutionAward award);
	
	void createClbRef(int awardId,int[] clbIds,String[] fileNames);
	List<InstitutionAttachment> getAttachments(int oid);
	
	void deleteClbRef(int awardId);
	
	Map<Integer, Integer> getYearsMap(int institutionId);
	
	List<InstitutionAward> getAwardByUser(int userId, int i, int pageSize, SearchInstitutionCondition condition);
	int getAwardCountByUser(int userId, SearchInstitutionCondition condition);
	Map<Integer, Integer> getYearsMapByUser(int userId);
	Map<Integer, Integer> getGradesMap(int insId);
	void deleteAwardUser(int[] id);
	void insertAwardUser(int awardId, int userId);
	
	int getAwardCount(int insId, int departId, int year);
	
	List<InstitutionAward> getAwardsByAuthorId(int authorId,int startYear, int endYear);
}
