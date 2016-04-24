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

import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionTopic;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.PageResult;

public interface IInstitutionBackendTopicService {
	PageResult<InstitutionTopic> getTopic(int institutionId,int page, SearchInstitutionCondition condition);
	List<InstitutionTopic> getTopicsByUID(int uid);

	void deleteTopic(int[] id);
	 int getTopicCount(int id);
	 int getTopicCount(int id,SearchInstitutionCondition condition);
	 void updateTopic(int id, InstitutionTopic topic);
	 void insertTopic(InstitutionTopic topic);
	 InstitutionTopic getTopicById(int id);
	 List<InstitutionAuthor> getAuthorsByTopicId(int topicId);
	 InstitutionAuthor getAuthorsById(int topicId,int authorId);
	 Map<Integer, List<InstitutionAuthor>> getListAuthorsMap(
				List<Integer> topicIds);
	 void deleteByTopicId(int[] topicIds);
	 void createAuthor(InstitutionAuthor author);
	 void updateRef(int topicId, int[] uid, String[] authorType);
	 void createRef(int topicId, int[] uid, String[] authorType);
	 int insert(InstitutionTopic topic,int[] uid, String[] authorType);
	 void deleteRef(int topicId);
	 Map<Integer, Integer> getFundsFromsMap(int institutionId);
	 PageResult<InstitutionTopic> getTopicByUser(int userId,int page, SearchInstitutionCondition condition);
	 int getTopicCountByUser(int userId);
	 Map<Integer, Integer> getGradesMapByUser(int userId);
	 void deleteTopicUser(int[] topicId);
	 void insertTopicUser(int topicId, int userId,String role);
		 List<InstitutionTopic> getTopicByKeyword(int offset, int size, String keyword);
		 List<InstitutionTopic> getTopicByInit(int offset, int size,String userName);
		 void insertTopic(final String[] topicId,final int userId);
		 List<Integer> getExistTopicIds(int uid);
		 
		 
			/**
			 * 删除与员工关联
			 * @param id
			 */
			void deleteUserRef(int uid,int topicId);
			
			void createUserRef(int uid,int topicId,String role);
			void updateUserRef(int uid,int topicId,String role);
			
			void update(InstitutionTopic topic,int[] uids,String[] authorTypes);
			int create(InstitutionTopic topic,int institutionId,int[] uids,String[] authorTypes);
			void updateRole(String role,int userId,int topicId);
			InstitutionTopic getTopicRoleById(int id,int userId);
}
