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
import java.util.Map.Entry;
import java.util.Set;

import net.duckling.common.util.CommonUtils;
import net.duckling.dhome.dao.IInstitutionAcademicDAO;
import net.duckling.dhome.dao.IInstitutionAcademicSearchDAO;
import net.duckling.dhome.dao.IInstitutionBackendAcademicAuthorDAO;
import net.duckling.dhome.domain.institution.InstitutionAcademic;
import net.duckling.dhome.domain.institution.InstitutionAcademicAuthor;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.service.IInstitutionBackendAcademicService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstitutionBackendAcademicServiceImpl implements
		IInstitutionBackendAcademicService {
	private static final Logger LOG=Logger.getLogger(InstitutionBackendAcademicServiceImpl.class);

	@Autowired
	IInstitutionAcademicDAO mAcademicDAO;
	
	@Autowired
	IInstitutionBackendAcademicAuthorDAO authorDAO;
	@Autowired
	IInstitutionAcademicSearchDAO acaSearchDAO;

	@Override
	public void insert(List<InstitutionAcademic> academics) {
		mAcademicDAO.insert(academics);
	}

	@Override
	public void insert(InstitutionAcademic academic) {
		// TODO Auto-generated method stub
		mAcademicDAO.insert(academic);

	}

	@Override
	public PageResult<InstitutionAcademic> getAcademics(int insId, int page,SearchInstitutionCondition condition) {
		PageResult<InstitutionAcademic> result = new PageResult<InstitutionAcademic>();
		result.setAllSize(mAcademicDAO.getAcademicCount(insId,condition));
		if(result.getMaxPage() < page){
			page = result.getMaxPage();
		}
		result.setDatas(mAcademicDAO.getAcademics(insId,page == 0? 0 : (page - 1)*result.getPageSize(), result.getPageSize(),condition));
		
		result.setCurrentPage(page);
		return result;	
	}

	@Override
	public int getAcademicCount(int insId) {
		return mAcademicDAO.getAcademicCount(insId,new SearchInstitutionCondition());
	}

	@Override
	public void delete(int insId, int[] acmIds) {
		mAcademicDAO.delete(insId, acmIds);
	}
	
	@Override
	public void delete(int insId, int acmId) {
		mAcademicDAO.delete(insId, acmId);
	}
	
	@Override
	public void deleteAll(int insId) {
		mAcademicDAO.deleteAll(insId);
	}

	@Override
	public int create(InstitutionAcademic academic, int institutionId,
			String[] authorIds) {
		
		
		academic.setInstitutionId(institutionId);
		academic.setId(mAcademicDAO.create(academic));
		authorDAO.createRef(academic.getId(), authorIds);
		return academic.getId();
	}

	@Override
	public void update(InstitutionAcademic academic, int institutionId,
			String[] authorIds) {
		if(academic.getId() == 0){
			LOG.error("update:academic.id is null");
		}
		
		mAcademicDAO.update(academic);
		authorDAO.deleteRef(academic.getId());
		authorDAO.createRef(academic.getId(), authorIds);
		
	}
//
//	@Override
//	public void createAuthor(InstitutionAcademicAuthor author) {
//		authorDAO.createAuthor(author);
//	}

	@Override
	public List<InstitutionAcademicAuthor> getAuthorByAcademicId(int acmId) {
		return authorDAO.getByAcedemicId(acmId);
	}

//	@Override
//	public List<InstitutionAcademicAuthor> searchAuthor(String keyword) {
//		return authorDAO.search(keyword);
//	}

	@Override
	public Map<Integer, List<InstitutionAcademicAuthor>> getAuthorsMap(
			List<Integer> acmIds) {
		Map<Integer,  List<InstitutionAcademicAuthor>> map = new HashMap<Integer, List<InstitutionAcademicAuthor>>();
		List<InstitutionAcademicAuthor> authorIds = authorDAO.getByAcedemicIds(acmIds);
		if(CommonUtils.isNull(authorIds)){
			return map;
		}
		
		for(InstitutionAcademicAuthor author : authorIds){
			int acmId = author.getAcedemicId();
			if(map.containsKey(acmId)){
				map.get(acmId).add(author);
			}else{
				List<InstitutionAcademicAuthor> list = new ArrayList<InstitutionAcademicAuthor>();
				list.add(author);
				map.put(acmId, list);
			}
		}
//		caculateSubscript(map);
		return map;
	}

	@Override
	public InstitutionAcademicAuthor getAuthorById(int acmId, String authorId) {
		return authorDAO.getById(acmId, authorId);
	}
	
	/**
	 * 统计下标，单位相同为同一下标
	 * @param map
	 */
//	public void caculateSubscript(Map<Integer, List<InstitutionAcademicAuthor>> map){
//		Set<Entry<Integer, List<InstitutionAcademicAuthor>>> entrys = map.entrySet();
//		for(Entry<Integer, List<InstitutionAcademicAuthor>> entry : entrys){
//		    Map<String, Integer> subcscriptMap = new HashMap<String, Integer>();
//		    for(InstitutionAcademicAuthor author : entry.getValue()){
//		    	if(subcscriptMap.containsKey(author.getAuthorCompany())){
//		    		author.setSubscriptIndex(subcscriptMap.get(author.getAuthorCompany()));
//		    	}else{
//		    		int subscript = subcscriptMap.size() + 1;
//		    		author.setSubscriptIndex(subscript);
//		    		subcscriptMap.put(author.getAuthorCompany(), subscript);
//		    	}
//		    }
//		}
//	}

	@Override
	public InstitutionAcademic getAcademic(int acmId) {
		return mAcademicDAO.getAcademic(acmId);
	}

	@Override
	public List<InstitutionAcademic> getAcademicsByUID(int uid) {
		return mAcademicDAO.getAcademicsByUID(uid);
	}

	@Override
	public PageResult<InstitutionAcademic> getAcademicByUser(int userId,
			int page, SearchInstitutionCondition condition) {
		PageResult<InstitutionAcademic> result=new PageResult<InstitutionAcademic>();
		result.setAllSize(mAcademicDAO.getAcademicCountByUser(userId, condition));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(mAcademicDAO.getAcademicByUser(userId,(page-1)*result.getPageSize(),result.getPageSize(),condition));
		result.setCurrentPage(page);
		return result;
	}

	@Override
	public int getAcademicCountByUser(int userId) {
		return mAcademicDAO.getAcademicCount(userId, new SearchInstitutionCondition());
	}

	@Override
	public void deleteAcademicUser(int[] academicId) {
		mAcademicDAO.deleteAcademicUser(academicId);
		
	}

	@Override
	public void insertAcademicUser(int academicId, int userId) {
		mAcademicDAO.insertAcademicUser(academicId, userId);
		
	}

	@Override
	public List<InstitutionAcademic> getAcademicByKeyword(int offset, int size,
			String keyword) {
		return acaSearchDAO.getAcademicByKeyword(offset, size, keyword);
	}

	@Override
	public List<InstitutionAcademic> getAcademicByInit(int offset, int size,
			String userName) {
		return acaSearchDAO.getAcademicByInit(offset, size, userName);
	}

	@Override
	public void insertAcademic(String[] academicId, int userId) {
		acaSearchDAO.insertAcademic(academicId, userId);
	}

	@Override
	public List<Integer> getExistAcademicIds(int uid) {
		return acaSearchDAO.getExistAcademicIds(uid);
	}

	
	
	

	/**
	 * 添加单个员工论著
	 */
	@Override
	public int create(InstitutionAcademic academic, int institutionId,
			String authorId, int order) {
		return create(academic, institutionId, new String[]{authorId});
	}
	

//	@Override
//	public void createAuthor(InstitutionAcademicAuthor author) {
//		if(!authorDAO.isExist(author)){
//			authorDAO.createAuthor(author);
//		}
//	}

	@Override
	public void update(InstitutionAcademic academic,
			int authorId, int order) {
		mAcademicDAO.update(academic);
	}

	@Override
	public void deleteUserRef(int uid,int tid) {
		mAcademicDAO.deleteUserRef(uid, tid);
	}

	@Override
	public void createUserRef(int uid, int tid) {
		mAcademicDAO.createUserRef(uid, tid);
	}

	@Override
	public Map<Integer, Integer> getPositionsMap(int insId) {
		return mAcademicDAO.getPositionsMap(insId);
	}

	@Override
	public int getAcademicCount(int insId, SearchInstitutionCondition condition) {
		return mAcademicDAO.getAcademicCount(insId, condition);
	}

}
