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
import net.duckling.dhome.dao.IInstitutionAwardSearchDAO;
import net.duckling.dhome.dao.IInstitutionBackendAwardAuthorDAO;
import net.duckling.dhome.dao.IInstitutionBackendAwardDAO;
import net.duckling.dhome.dao.IInstitutionTreatiseSearchDAO;
import net.duckling.dhome.domain.institution.InstitutionAttachment;
import net.duckling.dhome.domain.institution.InstitutionAward;
import net.duckling.dhome.domain.institution.InstitutionAwardAuthor;
import net.duckling.dhome.domain.institution.InstitutionAward;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.service.IInstitutionBackendAwardService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstitutionBackendAwardServiceImpl implements
		IInstitutionBackendAwardService {

	private static final Logger LOG=Logger.getLogger(InstitutionBackendAwardServiceImpl.class);
	
	@Autowired
	IInstitutionBackendAwardDAO awardDao;
	
	@Autowired
	IInstitutionBackendAwardAuthorDAO authorDao;
	
	@Autowired
	private IInstitutionAwardSearchDAO awardSearchDAO;
	
	@Override
	public void insert(List<InstitutionAward> awards) {
        awardDao.insert(awards);
	}

	@Override
	public void insert(InstitutionAward award) {
        awardDao.insert(award);
	}

	@Override
	public PageResult<InstitutionAward> getAwards(int insId, int page,SearchInstitutionCondition condition) {
		PageResult<InstitutionAward> result = new PageResult<InstitutionAward>();
		result.setAllSize(awardDao.getAwardCount(insId,condition));
		if(result.getMaxPage() < page){
			page = result.getMaxPage();
		}
		result.setDatas(awardDao.getAwards(insId,page == 0? 0 : (page - 1)*result.getPageSize(), result.getPageSize(),condition));
		
		result.setCurrentPage(page);
		
		return result;	
	}

	@Override
	public InstitutionAward getAward(int awardId) {
		return awardDao.getAward(awardId);
	}

	@Override
	public int getAwardCount(int insId) {
		return awardDao.getAwardCount(insId,new SearchInstitutionCondition());
	}

	@Override
	public void delete(int insId, int[] awardIds) {
		awardDao.delete(insId, awardIds);
	}

	@Override
	public void delete(int insId, int awardId) {
		awardDao.delete(insId, awardId);
	}

	@Override
	public void deleteAll(int insId) {
		awardDao.deleteAll(insId);
	}

	@Override
	public int create(InstitutionAward award, int institutionId,
			int[] authorIds, int[] order, boolean[] communicationAuthors,
			boolean[] authorStudents, boolean[] authorTeacher,int[] clbIds,String[] fileNames) {
		award.setInstitutionId(institutionId);
		award.setId(awardDao.create(award));
		
		int awardId = award.getId();
		authorDao.createRef(awardId, authorIds, order, communicationAuthors, authorStudents, authorTeacher);
		awardDao.createClbRef(awardId, clbIds, fileNames);
		return award.getId();
	}

	@Override
	public void update(InstitutionAward award, int institutionId,
			int[] authorIds, int[] order, boolean[] communicationAuthors,
			boolean[] authorStudents, boolean[] authorTeacher,int[] clbIds,String[] fileNames) {
		
		if(award.getId() == 0){
			LOG.error("update: award.id is null");
			return;
		}
		
		int awardId = award.getId();
		awardDao.update(award);
		authorDao.deleteRef(awardId);
		authorDao.createRef(awardId, authorIds, order, communicationAuthors, authorStudents, authorTeacher);

		awardDao.deleteClbRef(awardId);
		awardDao.createClbRef(awardId, clbIds, fileNames);
	}
	
	@Override
	public void createClbRef(int awardId, int[] clbIds,String[] fileNames) {
		awardDao.createClbRef(awardId, clbIds,fileNames);
	}
	@Override
	public void deleteClbRef(int awardId) {
		awardDao.deleteClbRef(awardId);
	}
	@Override
	public List<InstitutionAttachment> getAttchments(int awardId) {
		return awardDao.getAttachments(awardId);
	}

	@Override
	public void createAuthor(InstitutionAwardAuthor author) {
		if(!authorDao.isExist(author)){
			authorDao.createAuthor(author);
		}
	}

	@Override
	public List<InstitutionAwardAuthor> getAuthorByawardId(int awardId) {
		return authorDao.getByAwardId(awardId);
	}

	@Override
	public List<InstitutionAwardAuthor> searchAuthor(String keyword) {
		return authorDao.search(keyword);
	}
	
	@Override
	public Map<Integer, Integer> getYearsMap(int insId) {
		return awardDao.getYearsMap(insId);
	}

	@Override
	public Map<Integer, List<InstitutionAwardAuthor>> getAuthorsMap(
			List<Integer> awardIds) {
		List<InstitutionAwardAuthor> authors = authorDao.getByAwardIds(awardIds);
		Map<Integer, List<InstitutionAwardAuthor>> map = new HashMap<Integer, List<InstitutionAwardAuthor>>();
		if(CommonUtils.isNull(authors)){
			return map;
		}
		
		for(InstitutionAwardAuthor author : authors){
			int awardId = author.getAwardId();
			if(map.containsKey(awardId)){
				map.get(awardId).add(author);
			}else{
				List<InstitutionAwardAuthor> list = new ArrayList<InstitutionAwardAuthor>();
				list.add(author);
				map.put(awardId, list);
			}
		}
		caculateSubscript(map);
		return map;
	}

	@Override
	public InstitutionAwardAuthor getAuthorById(int awardId, int authorId) {
		return authorDao.getById(awardId, authorId);
	}

	
	/**
	 * 统计下标，单位相同为同一下标
	 * @param map
	 */
	public void caculateSubscript(Map<Integer, List<InstitutionAwardAuthor>> map){
		Set<Entry<Integer, List<InstitutionAwardAuthor>>> entrys = map.entrySet();
		for(Entry<Integer, List<InstitutionAwardAuthor>> entry : entrys){
		    Map<String, Integer> subcscriptMap = new HashMap<String, Integer>();
		    for(InstitutionAwardAuthor author : entry.getValue()){
		    	if(subcscriptMap.containsKey(author.getAuthorCompany())){
		    		author.setSubscriptIndex(subcscriptMap.get(author.getAuthorCompany()));
		    	}else{
		    		int subscript = subcscriptMap.size() + 1;
		    		author.setSubscriptIndex(subscript);
		    		subcscriptMap.put(author.getAuthorCompany(), subscript);
		    	}
		    }
		}
	}

	@Override
	public List<InstitutionAward> getAwardsByUID(int uid) {
		return awardDao.getAwardsByUID(uid);
	}

	@Override
	public PageResult<InstitutionAward> getAwardByUser(int userId, int page,
			SearchInstitutionCondition condition) {
		PageResult<InstitutionAward> result=new PageResult<InstitutionAward>();
		result.setAllSize(awardDao.getAwardCountByUser(userId, condition));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(awardDao.getAwardByUser(userId,(page-1)*result.getPageSize(),result.getPageSize(),condition));
		result.setCurrentPage(page);
		return result;
	}

	@Override
	public int getAwardCountByUser(int userId) {
		return awardDao.getAwardCountByUser(userId, new SearchInstitutionCondition());
	}

	@Override
	public Map<Integer, Integer> getYearByUser(int userId) {
		return awardDao.getYearsMapByUser(userId);
	}

	@Override
	public void deleteAwardUser(int[] awardId) {
		awardDao.deleteAwardUser(awardId);
		
	}

	@Override
	public void insertAwardUser(int awardId, int userId) {
		awardDao.insertAwardUser(awardId, userId);
		
	}

	@Override
	public List<InstitutionAward> getAwardByKeyword(int offset, int size,
			String keyword) {
		return awardSearchDAO.getAwardByKeyword(offset, size, keyword);
	}

	@Override
	public List<InstitutionAward> getAwardByInit(int offset, int size,
			String userName) {
		return awardSearchDAO.getAwardByInit(offset, size, userName);
	}

	@Override
	public void insertAward(String[] awardId, int userId) {
		awardSearchDAO.insertAward(awardId, userId);
	}

	
	
	@Override
	public void deleteUserRef(int uid, int awardId) {
		awardDao.deleteUserRef(uid, awardId);
	}

	@Override
	public void createUserRef(int uid, int awardId, int authorOrder) {
		awardDao.createUserRef(uid, awardId, authorOrder);
	}

	@Override
	public void updateUserRef(int uid, int awardId, int authorOrder) {
		awardDao.updateUserRef(uid, awardId, authorOrder);
	}

	@Override
	public void update(InstitutionAward award,  int authorId,
			int order) {
		awardDao.update(award);
		authorDao.updateRef(authorId, award.getId(),order);
	}

	@Override
	public int create(InstitutionAward award, int institutionId, int authorId,
			int order) {
		return create(award, institutionId, new int[]{authorId}, new int[]{order}, new boolean[]{false}, new boolean[]{false}, new boolean[]{false},new int[]{},new String[]{});	
	}

	@Override
	public Map<Integer, Integer> getGradesMap(int insId) {
		return awardDao.getGradesMap(insId);
	}

	@Override
	public int getAwardCount(int insId, SearchInstitutionCondition condition) {
		return awardDao.getAwardCount(insId, condition);
	}

	@Override
	public List<Integer> getExistAwardIds(int uid) {
		return awardSearchDAO.getExistAwardIds(uid);
	}

}
