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
package net.duckling.dhome.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.common.util.CommonUtils;
import net.duckling.dhome.dao.IInstitutionBackendTopicAuthorDAO;
import net.duckling.dhome.dao.IInstitutionTopicDAO;
import net.duckling.dhome.dao.IInstitutionTopicSearchDAO;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionTopic;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.service.IInstitutionBackendTopicService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstitutionBackendTopicServiceImpl implements
		IInstitutionBackendTopicService {
	@Autowired
	private IInstitutionTopicDAO topicDAO;
	@Autowired
	private IInstitutionBackendTopicAuthorDAO tAuthorDAO;
	@Autowired
	IInstitutionTopicSearchDAO topicSearchDAO;

	@Override
	public PageResult<InstitutionTopic> getTopic(int institutionId, int page, SearchInstitutionCondition condition) {
		PageResult<InstitutionTopic> result=new PageResult<InstitutionTopic>();
		result.setAllSize(topicDAO.getTopicCount(institutionId, condition));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(topicDAO.getTopic(institutionId,(page-1)*result.getPageSize(),result.getPageSize(), condition));
		result.setCurrentPage(page);
		return result;
	}
	@Override
	public void deleteTopic(int[] id){
		topicDAO.deleteTopic(id);
	}
	@Override
	public int getTopicCount(int id) {
		return topicDAO.getTopicCount(id, new SearchInstitutionCondition());
	}
	@Override
	public void updateTopic(int id, InstitutionTopic topic) {
		topicDAO.updateTopic(id, topic);
	}
	@Override
	public void insertTopic(InstitutionTopic topic) {
		topicDAO.insert(topic);
	}
	@Override
	public InstitutionTopic getTopicById(int id) {
		return topicDAO.getTopicById(id); 
	}
	@Override
	public List<InstitutionAuthor> getAuthorsByTopicId(int topicId) {
		return tAuthorDAO.getAuthorListByTopicId(topicId);
	}
	@Override
	public Map<Integer, List<InstitutionAuthor>> getListAuthorsMap(
			List<Integer> topicIds) {
		List<InstitutionAuthor> authors=tAuthorDAO.getAuthorListByTopicIds(topicIds);
		Map<Integer,List<InstitutionAuthor>> map=new HashMap<Integer,List<InstitutionAuthor>>();
		if(CommonUtils.isNull(authors)){
			return map;
		}
		for(InstitutionAuthor author:authors){
			addToMap(map,author.getTopicId(),author);
		}
		return map;
	}
	//有则追加，无则新建List
		private void addToMap(Map<Integer,List<InstitutionAuthor>> map,int topicId,InstitutionAuthor author){
			if(map.containsKey(topicId)){
				map.get(topicId).add(author);
			}else{
				List<InstitutionAuthor> list=new ArrayList<InstitutionAuthor>();
				list.add(author);
				map.put(topicId, list);
			}
		}
		@Override
		public void deleteByTopicId(int[] topicIds) {
			
			tAuthorDAO.deleteAuthorsByTopicIds(topicIds);
		}
//		@Override
//		public void createAuthor(InstitutionAuthor author) {
//			tAuthorDAO.createAuthor(author);
//		}
		@Override
		public void updateRef(int topicId, int[] uid, String[] authorType) {
			tAuthorDAO.createRef(topicId, uid, authorType);
			
		}
		@Override
		public void createRef(int topicId, int[] uid, String[] authorType) {
			tAuthorDAO.createRef(topicId, uid, authorType);
			
		}
		@Override
		public void deleteRef(int topicId) {
			tAuthorDAO.deleteRef(topicId);
			
		}
		@Override
		public Map<Integer, Integer> getFundsFromsMap(int institutionId) {
			return topicDAO.getFundsFromsMap(institutionId);
		}
		@Override
		public int insert(InstitutionTopic topic, int[] uid, String[] authorType) {
			topic.setId(topicDAO.creat(topic));
			
			int topicId = topic.getId();
			tAuthorDAO.createRef(topicId, uid, authorType);
			return topic.getId();
		}

		
		@Override
		public List<InstitutionTopic> getTopicsByUID(int uid) {
			return topicDAO.getTopicsByUID(uid);
		}

		@Override
		public PageResult<InstitutionTopic> getTopicByUser(int userId,
				int page, SearchInstitutionCondition condition) {
				PageResult<InstitutionTopic> result=new PageResult<InstitutionTopic>();
				result.setAllSize(topicDAO.getTopicCountByUser(userId, condition));
				if(result.getMaxPage()<page){
					page=result.getMaxPage();
				}
				if(page==0){
					return result;
				}
				result.setDatas(topicDAO.getTopicByUser(userId,(page-1)*result.getPageSize(),result.getPageSize(),condition));
				result.setCurrentPage(page);
				return result;
		}
		@Override
		public int getTopicCountByUser(int userId) {
			return topicDAO.getTopicCountByUser(userId, new SearchInstitutionCondition());
		}
		@Override
		public Map<Integer, Integer> getGradesMapByUser(int userId) {
			return topicDAO.getFundsFromsMapByUser(userId);
		}
		@Override
		public void deleteTopicUser(int[] topicId) {
			topicDAO.deleteTopicUser(topicId);
		}
		@Override
		public void insertTopicUser(int topicId, int userId,String role) {
			topicDAO.insertTopicUser(topicId, userId,role);
			
		}
		@Override
		public List<InstitutionTopic> getTopicByKeyword(int offset, int size,
				String keyword) {
			return topicSearchDAO.getTopicByKeyword(offset, size, keyword);
		}
		@Override
		public List<InstitutionTopic> getTopicByInit(int offset, int size,
				String userName) {
			return topicSearchDAO.getTopicByInit(offset, size, userName);
		}
		@Override
		public void insertTopic(String[] topicId, int userId) {
			topicSearchDAO.insertTopic(topicId, userId);
		}
		@Override
		public List<Integer> getExistTopicIds(int uid) {
			return topicSearchDAO.getExistTopicIds(uid);
		}

		
		/**
		 * 添加单个员工论著
		 */
		@Override
		public int create(InstitutionTopic topic, int institutionId,int[] uids,String[] authorTypes) {
			topic.setInstitution_id(institutionId);
			return insert(topic, uids,authorTypes);
		}
		

		@Override
		public void createAuthor(InstitutionAuthor author) {
			if(!tAuthorDAO.isExist(author)){
				tAuthorDAO.createAuthor(author);
			}
		}

		@Override
		public void update(InstitutionTopic topic,int[] uids,String[] authorTypes) {
			topicDAO.updateTopic(topic.getId(),topic);
			tAuthorDAO.deleteRef(topic.getId());
			tAuthorDAO.createRef(topic.getId(), uids, authorTypes);
		}

		@Override
		public void deleteUserRef(int uid,int tid) {
			topicDAO.deleteUserRef(uid, tid);
		}

		@Override
		public void createUserRef(int uid, int tid,String role) {
			topicDAO.createUserRef(uid, tid,role);
		}
		
		@Override
		public void updateUserRef(int uid, int topicId, String role) {
			topicDAO.updateUserRef(uid, topicId, role);
		}
		@Override
		public void updateRole(String role, int userId, int topicId) {
			topicDAO.updateRole(role, userId, topicId);
		}
		@Override
		public InstitutionTopic getTopicRoleById(int id, int userId) {
			return topicDAO.getTopicRoleById(id, userId);
		}
		@Override
		public InstitutionAuthor getAuthorsById(int topicId, int authorId) {
			return tAuthorDAO.getById(topicId, authorId);
		}
		@Override
		public int getTopicCount(int id, SearchInstitutionCondition condition) {
			return topicDAO.getTopicCount(id, condition);
		}
		
}
