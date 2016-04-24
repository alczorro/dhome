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
import net.duckling.dhome.dao.IInstitutionBackendCopyrightAuthorDAO;
import net.duckling.dhome.dao.IInstitutionBackendCopyrightFileDAO;
import net.duckling.dhome.dao.IInstitutionCopyrightDAO;
import net.duckling.dhome.dao.IInstitutionCopyrightSearchDAO;
import net.duckling.dhome.domain.institution.InstitutionAttachment;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionCopyright;
import net.duckling.dhome.domain.institution.InstitutionCopyright;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.service.IInstitutionBackendCopyrightService;

@Service
public class InstitutionBackendCopyrightServiceImpl implements
		IInstitutionBackendCopyrightService {
	
	@Autowired
	private IInstitutionCopyrightDAO copyrightDAO;
	@Autowired
	private IInstitutionBackendCopyrightAuthorDAO authorCopyrightDAO;
	@Autowired
	private IInstitutionBackendCopyrightFileDAO fileCopyrightDAO;
	@Autowired
	private IInstitutionCopyrightSearchDAO searchDAO;

	@Override
	public PageResult<InstitutionCopyright> getCopyright(int institutionId,
			int page, SearchInstitutionCondition condition) {
		PageResult<InstitutionCopyright> result=new PageResult<InstitutionCopyright>();
		result.setAllSize(copyrightDAO.getCopyrightCount(institutionId,condition));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(copyrightDAO.getCopyright(institutionId,(page-1)*result.getPageSize(),result.getPageSize(),condition));
		result.setCurrentPage(page);
		return result;
	}

	@Override
	public int getCopyrightCount(int insId) {
		return copyrightDAO.getCopyrightCount(insId,new SearchInstitutionCondition());
	}

	@Override
	public void updateCopyright(int id, InstitutionCopyright copyright) {
		copyrightDAO.updateCopyright(id, copyright);
		
	}

//	@Override
//	public void insertCopyright(InstitutionCopyright copyright) {
//		copyright.setId(copyrightDAO.creat(copyright));
//		
//	}

	@Override
	public InstitutionCopyright getCopyrightById(int id) {
		return copyrightDAO.getCopyrightById(id);
	}

	@Override
	public List<InstitutionAuthor> getAuthorsByCopyrightId(int copyrightId) {
		return authorCopyrightDAO.getAuthorListByCopyrightId(copyrightId);
	}

	@Override
	public Map<Integer, List<InstitutionAuthor>> getListAuthorsMap(
			List<Integer> copyrightIds) {
		List<InstitutionAuthor> authors=authorCopyrightDAO.getAuthorListByCopyrightIds(copyrightIds);
		Map<Integer,List<InstitutionAuthor>> map=new HashMap<Integer,List<InstitutionAuthor>>();
		if(CommonUtils.isNull(authors)){
			return map;
		}
		for(InstitutionAuthor author:authors){
			addToMap(map,author.getCopyrightId(),author);
		}
		return map;
	}
	//有则追加，无则新建List
			private void addToMap(Map<Integer,List<InstitutionAuthor>> map,int copyrightId,InstitutionAuthor author){
				if(map.containsKey(copyrightId)){
					map.get(copyrightId).add(author);
				}else{
					List<InstitutionAuthor> list=new ArrayList<InstitutionAuthor>();
					list.add(author);
					map.put(copyrightId, list);
				}
			}

	@Override
	public void deleteByCopyrightId(int[] copyrightIds) {
		copyrightDAO.deleteCopyright(copyrightIds);
		
	}
//
//	@Override
//	public void createAuthor(InstitutionAuthor author) {
//		authorCopyrightDAO.createAuthor(author);
//		
//	}

//	@Override
//	public void updateRef(int copyrightId, int[] uid, String[] authorType) {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public void createRef(int copyrightId, int[] uid, int[] order) {
		authorCopyrightDAO.createRef(copyrightId, uid, order);
		
	}

	@Override
	public void deleteRef(int copyrightId) {
		authorCopyrightDAO.deleteRef(copyrightId);
		
	}

	@Override
	public List<InstitutionAttachment> getAttachment(int objType, int objId) {
		return fileCopyrightDAO.getAttachment(objType, objId);
	}

	@Override
	public void insertAttachments(int objId, int[] clbIds,
			String[] fileNames) {
		fileCopyrightDAO.insertAttachments(objId, clbIds, fileNames);
		
	}
	
	@Override
	public int insert(InstitutionCopyright copyright,
			int[] uid, int[] order, int[] clbIds, String[] fileNames) {
		copyright.setId(copyrightDAO.creat(copyright));
		
		int copyrightId = copyright.getId();
		authorCopyrightDAO.createRef(copyrightId, uid, order);
		fileCopyrightDAO.insertAttachments(copyrightId, clbIds, fileNames);
		return copyright.getId();
	}


	@Override
	public void deleteAttachments(int objId) {
		fileCopyrightDAO.deleteAttachments(objId);
		
	}

	@Override
	public Map<Integer, Integer> getGradesMap(int institutionId) {
		return copyrightDAO.getGradesMap(institutionId);
	}

	@Override
	public PageResult<InstitutionCopyright> getCopyrightByUser(int userId,
			int page, SearchInstitutionCondition condition) {
		PageResult<InstitutionCopyright> result=new PageResult<InstitutionCopyright>();
		result.setAllSize(copyrightDAO.getCopyrightCountByUser(userId,condition));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(copyrightDAO.getCopyrightByUser(userId,(page-1)*result.getPageSize(),result.getPageSize(),condition));
		result.setCurrentPage(page);
		return result;
	}

	@Override
	public int getCopyrightCountByUser(int userId) {
		return copyrightDAO.getCopyrightCountByUser(userId,new SearchInstitutionCondition());
	}

	@Override
	public Map<Integer, Integer> getGradesMapByUser(int userId) {
		return copyrightDAO.getGradesMapByUser(userId);
	}

	@Override
	public void deleteCopyrightUser(int[] copyrightId) {
		copyrightDAO.deleteCopyrightUser(copyrightId);
		
	}

	@Override
	public void insertCopyrightUser(int copyrightId, int userId) {
		copyrightDAO.insertCopyrightUser(copyrightId, userId);
	}


	@Override
	public List<InstitutionCopyright> getCopyrightsByUID(int uid) {
		return copyrightDAO.getCopyrightsByUID(uid);
	}


	@Override
	public List<InstitutionCopyright> getCopyrightByKeyword(int offset,
			int size, String keyword) {
		return searchDAO.getCopyrightByKeyword(offset, size, keyword);
	}

	@Override
	public List<InstitutionCopyright> getCopyrightByInit(int offset, int size,
			String userName) {
		return searchDAO.getCopyrightByInit(offset, size, userName);
	}

	@Override
	public void insertCopyright(String[] copyrightId, int userId) {
		searchDAO.insertCopyright(copyrightId, userId);
	}

	@Override
	public List<Integer> getExistCopyrightIds(int uid) {
		return searchDAO.getExistCopyrightIds(uid);
	}

	
	/**
	 * 添加单个员工软件著作权
	 */
	@Override
	public int create(InstitutionCopyright copyright, int institutionId,
			int authorId, int order) {
		copyright.setInstitutionId(institutionId);
		return insert(copyright, new int[]{authorId}, new int[]{order},new int[]{},new String[]{});
	}
	

	@Override
	public void createAuthor(InstitutionAuthor author) {
		if(!authorCopyrightDAO.isExist(author)){
			authorCopyrightDAO.createAuthor(author);
		}
	}

	@Override
	public void update(InstitutionCopyright copyright,
			int authorId, int order) {
		copyrightDAO.updateCopyright(copyright.getId(),copyright);
		authorCopyrightDAO.updateRef(authorId, copyright.getId(),order);
	}

	@Override
	public void deleteUserRef(int uid,int copyrightId) {
		copyrightDAO.deleteUserRef(uid, copyrightId);
	}

	@Override
	public void createUserRef(int uid, int copyrightId, int athorOrder) {
		copyrightDAO.createUserRef(uid, copyrightId, athorOrder);
	}

	@Override
	public void updateUserRef(int uid, int copyrightId, int authorOrder) {
		copyrightDAO.updateUserRef(uid, copyrightId, authorOrder);
	}

	@Override
	public int getCopyrightCount(int insId, SearchInstitutionCondition condition) {
		return copyrightDAO.getCopyrightCount(insId, condition);
	}

	@Override
	public InstitutionAuthor getById(int copyrightId, int authorId) {
		return authorCopyrightDAO.getById(copyrightId, authorId);
	}
	
}
