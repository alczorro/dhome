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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.duckling.common.util.CommonUtils;
import net.duckling.dhome.dao.IInstitutionBackendPatentAuthorDAO;
import net.duckling.dhome.dao.IInstitutionBackendPatentFileDAO;
import net.duckling.dhome.dao.IInstitutionPatentDAO;
import net.duckling.dhome.dao.IInstitutionPatentSearchDAO;
import net.duckling.dhome.domain.institution.InstitutionAttachment;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionPatent;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.service.IInstitutionBackendPatentService;

@Service
public class InstitutionBackendPatentServiceImpl implements
		IInstitutionBackendPatentService {
	
	@Autowired
	private IInstitutionPatentDAO patentDAO;
	@Autowired
	private IInstitutionBackendPatentAuthorDAO authorPatentDAO;
	@Autowired
	private IInstitutionBackendPatentFileDAO filePatentDAO;
	@Autowired
	private IInstitutionPatentSearchDAO searchDAO;

	@Override
	public PageResult<InstitutionPatent> getPatent(int institutionId,
			int page, SearchInstitutionCondition condition) {
		PageResult<InstitutionPatent> result=new PageResult<InstitutionPatent>();
		result.setAllSize(patentDAO.getPatentCount(institutionId,condition));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(patentDAO.getPatent(institutionId,(page-1)*result.getPageSize(),result.getPageSize(),condition));
		result.setCurrentPage(page);
		return result;
	}

	@Override
	public int getPatentCount(int insId) {
		return patentDAO.getPatentCount(insId,new SearchInstitutionCondition());
	}

	@Override
	public void updatePatent(int id, InstitutionPatent patent) {
		patentDAO.updatePatent(id, patent);
		
	}

//	@Override
//	public void insertPatent(InstitutionPatent patent) {
//		patent.setId(patentDAO.creat(patent));
//		
//	}

	@Override
	public InstitutionPatent getPatentById(int id) {
		return patentDAO.getPatentById(id);
	}

	@Override
	public List<InstitutionAuthor> getAuthorsByPatentId(int patentId) {
		return authorPatentDAO.getAuthorListByPatentId(patentId);
	}

	@Override
	public Map<Integer, List<InstitutionAuthor>> getListAuthorsMap(
			List<Integer> patentIds) {
		List<InstitutionAuthor> authors=authorPatentDAO.getAuthorListByPatentIds(patentIds);
		Map<Integer,List<InstitutionAuthor>> map=new HashMap<Integer,List<InstitutionAuthor>>();
		if(CommonUtils.isNull(authors)){
			return map;
		}
		for(InstitutionAuthor author:authors){
			addToMap(map,author.getPatentId(),author);
		}
		return map;
	}
	//有则追加，无则新建List
			private void addToMap(Map<Integer,List<InstitutionAuthor>> map,int patentId,InstitutionAuthor author){
				if(map.containsKey(patentId)){
					map.get(patentId).add(author);
				}else{
					List<InstitutionAuthor> list=new ArrayList<InstitutionAuthor>();
					list.add(author);
					map.put(patentId, list);
				}
			}

	@Override
	public void deleteByPatentId(int[] patentIds) {
		patentDAO.deletePatent(patentIds);
		
	}


//	@Override
//	public void updateRef(int patentId, int[] uid, String[] authorType) {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public void createRef(int patentId, int[] uid, int[] order) {
		authorPatentDAO.createRef(patentId, uid, order);
		
	}

	@Override
	public void deleteRef(int patentId) {
		authorPatentDAO.deleteRef(patentId);
		
	}

	@Override
	public List<InstitutionAttachment> getAttachment(int objType, int objId) {
		return filePatentDAO.getAttachment(objType, objId);
	}

	@Override
	public void insertAttachments(int objId, int[] clbIds,
			String[] fileNames) {
		filePatentDAO.insertAttachments(objId, clbIds, fileNames);
		
	}
	
	@Override
	public int insert(InstitutionPatent patent,
			int[] uid, int[] order, int[] clbIds, String[] fileNames) {
		patent.setId(patentDAO.creat(patent));
		
		int patentId = patent.getId();
		authorPatentDAO.createRef(patentId, uid, order);
		filePatentDAO.insertAttachments(patentId, clbIds, fileNames);
		return patent.getId();
	}


	@Override
	public void deleteAttachments(int objId) {
		filePatentDAO.deleteAttachments(objId);
		
	}

	@Override
	public Map<Integer, Integer> getGradesMap(int institutionId) {
		return patentDAO.getGradesMap(institutionId);
	}

	@Override
	public PageResult<InstitutionPatent> getPatentByUser(int userId,
			int page, SearchInstitutionCondition condition) {
		PageResult<InstitutionPatent> result=new PageResult<InstitutionPatent>();
		result.setAllSize(patentDAO.getPatentCountByUser(userId,condition));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(patentDAO.getPatentByUser(userId,(page-1)*result.getPageSize(),result.getPageSize(),condition));
		result.setCurrentPage(page);
		return result;
	}

	@Override
	public int getPatentCountByUser(int userId) {
		return patentDAO.getPatentCountByUser(userId,new SearchInstitutionCondition());
	}

	@Override
	public Map<Integer, Integer> getGradesMapByUser(int userId) {
		return patentDAO.getGradesMapByUser(userId);
	}

	@Override
	public void deletePatentUser(int[] patentId) {
		patentDAO.deletePatentUser(patentId);
		
	}

	@Override
	public void insertPatentUser(int patentId, int userId) {
		patentDAO.insertPatentUser(patentId, userId);
	}


	@Override
	public List<InstitutionPatent> getPatentsByUID(int uid) {
		return patentDAO.getPatentsByUID(uid);
	}


	@Override
	public List<InstitutionPatent> getPatentByKeyword(int offset,
			int size, String keyword) {
		return searchDAO.getPatentByKeyword(offset, size, keyword);
	}

	@Override
	public List<InstitutionPatent> getPatentByInit(int offset, int size,
			String userName) {
		return searchDAO.getPatentByInit(offset, size, userName);
	}

	@Override
	public void insertPatent(String[] patentId, int userId) {
		searchDAO.insertPatent(patentId, userId);
	}

	
	/**
	 * 添加单个员工专利
	 */
	@Override
	public int create(InstitutionPatent patent, int institutionId,
			int authorId, int order) {
		patent.setInstitutionId(institutionId);
		return insert(patent, new int[]{authorId}, new int[]{order},new int[]{},new String[]{});
	}
	

	@Override
	public void createAuthor(InstitutionAuthor author) {
		if(!authorPatentDAO.isExist(author)){
			authorPatentDAO.createAuthor(author);
		}
	}

	@Override
	public void update(InstitutionPatent patent,
			int authorId, int order) {
		patentDAO.updatePatent(patent.getId(),patent);
		authorPatentDAO.updateRef(authorId, patent.getId(),order);
	}

	@Override
	public void deleteUserRef(int uid,int patentId) {
		patentDAO.deleteUserRef(uid, patentId);
	}

	@Override
	public void createUserRef(int uid, int patentId, int athorOrder) {
		patentDAO.createUserRef(uid, patentId, athorOrder);
	}

	@Override
	public void updateUserRef(int uid, int patentId, int authorOrder) {
		patentDAO.updateUserRef(uid, patentId, authorOrder);
	}

	@Override
	public int getPatentCount(int insId, SearchInstitutionCondition condition) {
		return patentDAO.getPatentCount(insId, condition);
	}

	@Override
	public List<Integer> getExistPatentIds(int uid) {
		return searchDAO.getExistPatentIds(uid);
	}
}
