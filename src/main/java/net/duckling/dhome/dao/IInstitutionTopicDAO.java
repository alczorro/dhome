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

import net.duckling.dhome.domain.institution.InstitutionTopic;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;

public interface IInstitutionTopicDAO {
	List<InstitutionTopic> getTopic(int id, int i, int pageSize, SearchInstitutionCondition condition);
	List<InstitutionTopic> getTopicsByUID(int uid);
	int getTopicCount(int insId, SearchInstitutionCondition condition);
	void deleteTopic(int[] id);
	void updateTopic(int id,InstitutionTopic topic);
	void insert(List<InstitutionTopic> topic);
	void insert(InstitutionTopic topic);
	int creat(InstitutionTopic topic);
	InstitutionTopic getTopicById(int id);
	Map<Integer, Integer> getFundsFromsMap(int institutionId);
	List<InstitutionTopic> getTopicByUser(int userId, int i, int pageSize, SearchInstitutionCondition condition);
	int getTopicCountByUser(int userId, SearchInstitutionCondition condition);
	Map<Integer, Integer> getFundsFromsMapByUser(int userId);
	void deleteTopicUser(int[] id);
	void insertTopicUser(int topicId, int userId,String role);
	
    void deleteUserRef(int uid,int topicId);
    void createUserRef(int uid,int topicId,String role);
    void updateUserRef(int uid,int topicId,String role);
    void updateRole(String role,int userId,int topicId);
    InstitutionTopic getTopicRoleById(int id,int userId);
    
    int getTopicCount(int insId, int departId, int year);
    
    List<InstitutionTopic> getTopicsByAuthorId(int authorId,int userId,int startYear, int endYear);
}
