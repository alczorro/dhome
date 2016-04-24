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
import net.duckling.dhome.dao.IInstitutionBackendPeriodicalAuthorDAO;
import net.duckling.dhome.dao.IInstitutionPeriodicalDAO;
import net.duckling.dhome.dao.IInstitutionPeriodicalSearchDAO;
import net.duckling.dhome.domain.institution.InstitutionPeriodical;
import net.duckling.dhome.domain.institution.InstitutionPeriodicalAuthor;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.service.IInstitutionBackendPeriodicalService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstitutionBackendPeriodicalServiceImpl implements
		IInstitutionBackendPeriodicalService {
	private static final Logger LOG=Logger.getLogger(InstitutionBackendPeriodicalServiceImpl.class);

	@Autowired
	IInstitutionPeriodicalDAO mPeriodicalDAO;
	
	@Autowired
	IInstitutionBackendPeriodicalAuthorDAO authorDao;

	@Autowired
	IInstitutionPeriodicalSearchDAO perSearchDAO;
	
	@Override
	public void insert(List<InstitutionPeriodical> periodicals) {
		mPeriodicalDAO.insert(periodicals);
	}

	@Override
	public void insert(InstitutionPeriodical periodical) {
		// TODO Auto-generated method stub
		mPeriodicalDAO.insert(periodical);
	}

	@Override
	public PageResult<InstitutionPeriodical> getPeriodicals(int insId, int page,SearchInstitutionCondition condition) {
		PageResult<InstitutionPeriodical> result = new PageResult<InstitutionPeriodical>();
		result.setAllSize(mPeriodicalDAO.getPeriodicalCount(insId,condition));
		if(result.getMaxPage() < page){
			page = result.getMaxPage();
		}
		result.setDatas(mPeriodicalDAO.getPeriodicals(insId, page == 0? 0 : (page - 1)*result.getPageSize(), result.getPageSize(),condition));
		
		result.setCurrentPage(page);
		return result;	
	}

	@Override
	public int getPeriodicalCount(int insId) {
		return mPeriodicalDAO.getPeriodicalCount(insId,new SearchInstitutionCondition());
	}

	@Override
	public void delete(int insId, int[] perIds) {
		mPeriodicalDAO.delete(insId, perIds);
	}
	
	@Override
	public void delete(int insId, int perId) {
		mPeriodicalDAO.delete(insId, perId);
	}
	
	@Override
	public void deleteAll(int insId) {
		mPeriodicalDAO.deleteAll(insId);
	}

	/**
	 * 添加期刊任职
	 */
	@Override
	public int create(InstitutionPeriodical periodical, int institutionId,
			String[] authorIds) {
		
		periodical.setInstitutionId(institutionId);
		periodical.setId(mPeriodicalDAO.create(periodical));
		authorDao.createRef(periodical.getId(), authorIds);
		return periodical.getId();
	}

//	@Override
//	public void createAuthor(InstitutionPeriodicalAuthor author) {
//		authorDao.createAuthor(author);
//	}

	@Override
	public List<InstitutionPeriodicalAuthor> getAuthorByPerId(int perId) {
		return authorDao.getByPerId(perId);
	}

//	@Override
//	public List<InstitutionPeriodicalAuthor> searchAuthor(String keyword) {
//		return authorDao.search(keyword);
//	}

	@Override
	public Map<Integer, List<InstitutionPeriodicalAuthor>> getAuthorsMap(
			List<Integer> perIds) {
		List<InstitutionPeriodicalAuthor> authors = authorDao.getByPerIds(perIds);
		Map<Integer, List<InstitutionPeriodicalAuthor>> map = new HashMap<Integer, List<InstitutionPeriodicalAuthor>>();
		if(CommonUtils.isNull(authors)){
			return map;
		}
		
		for(InstitutionPeriodicalAuthor author : authors){
			int perId = author.getPerId();
			if(map.containsKey(perId)){
				map.get(perId).add(author);
			}else{
				List<InstitutionPeriodicalAuthor> list = new ArrayList<InstitutionPeriodicalAuthor>();
				list.add(author);
				map.put(perId, list);
			}
		}
//		caculateSubscript(map);
		return map;
	}
	
	/**
	 * 统计下标，单位相同为同一下标
	 * @param map
	 */
//	public void caculateSubscript(Map<Integer, List<InstitutionPeriodicalAuthor>> map){
//		Set<Entry<Integer, List<InstitutionPeriodicalAuthor>>> entrys = map.entrySet();
//		for(Entry<Integer, List<InstitutionPeriodicalAuthor>> entry : entrys){
//		    Map<String, Integer> subcscriptMap = new HashMap<String, Integer>();
//		    for(InstitutionPeriodicalAuthor author : entry.getValue()){
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
	public InstitutionPeriodicalAuthor getAuthorById(int perId, String authorId) {
		return authorDao.getById(perId, authorId);
	}

	/**
	 * 修改期刊任职
	 */
	@Override
	public void update(InstitutionPeriodical periodical, int institutionId,
			String[] authorIds) {
		
		if(periodical.getId() == 0){
			LOG.error("update:periodical.id is null");
		}
		
		mPeriodicalDAO.update(periodical);
		authorDao.deleteRef(periodical.getId());
		authorDao.createRef(periodical.getId(), authorIds);
		
	}

	@Override
	public InstitutionPeriodical getPeriodical(int perId) {
		return mPeriodicalDAO.getPeriodical(perId);
	}

	@Override
	public List<InstitutionPeriodical> getPeriodicalsByUID(int uid) {
		return mPeriodicalDAO.getPeriodicalsByUID(uid);
	}

	@Override
	public PageResult<InstitutionPeriodical> getPeriodicalByUser(int userId,
			int page, SearchInstitutionCondition condition) {
		PageResult<InstitutionPeriodical> result=new PageResult<InstitutionPeriodical>();
		result.setAllSize(mPeriodicalDAO.getPeriodicalCountByUser(userId, condition));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(mPeriodicalDAO.getPeriodicalByUser(userId,(page-1)*result.getPageSize(),result.getPageSize(),condition));
		result.setCurrentPage(page);
		return result;
	}

	@Override
	public int getPeriodicalCountByUser(int userId) {
		return mPeriodicalDAO.getPeriodicalCount(userId, new SearchInstitutionCondition());
	}

	@Override
	public void deletePeriodicalUser(int[] periodicalId) {
		mPeriodicalDAO.deletePeriodicalUser(periodicalId);
	}

	@Override
	public void insertPeriodicalUser(int periodicalId, int userId) {
		mPeriodicalDAO.insertPeriodicalUser(periodicalId, userId);
	}

	@Override
	public List<InstitutionPeriodical> getPeriodicalByKeyword(int offset,
			int size, String keyword) {
		return perSearchDAO.getPeriodicalByKeyword(offset, size, keyword);
	}

	@Override
	public List<InstitutionPeriodical> getPeriodicalByInit(int offset,
			int size, String userName) {
		return perSearchDAO.getPeriodicalByInit(offset, size, userName);
	}

	@Override
	public void insertPeriodical(String[] periodicalId, int userId) {
		perSearchDAO.insertPeriodical(periodicalId, userId);
	}

	@Override
	public List<Integer> getExistPeriodicalIds(int uid) {
		return perSearchDAO.getExistPeriodicalIds(uid);
	}

	
	/**
	 * 添加单个员工论著
	 */
	@Override
	public int create(InstitutionPeriodical periodical, int institutionId,
			String authorId, int order) {
		return create(periodical, institutionId, new String[]{authorId});
	}
	

//	@Override
//	public void createAuthor(InstitutionPeriodicalAuthor author) {
//		if(!authorDao.isExist(author)){
//			authorDao.createAuthor(author);
//		}
//	}

	@Override
	public void update(InstitutionPeriodical periodical,
			int authorId, int order) {
		mPeriodicalDAO.update(periodical);
	}

	@Override
	public void deleteUserRef(int uid,int perId) {
		mPeriodicalDAO.deleteUserRef(uid, perId);
	}

	@Override
	public void createUserRef(int uid, int perId) {
		mPeriodicalDAO.createUserRef(uid, perId);
	}

	@Override
	public Map<Integer, Integer> getPositionsMap(int insId) {
		return mPeriodicalDAO.getPositionsMap(insId);
	}

	@Override
	public int getPeriodicalCount(int insId,
			SearchInstitutionCondition condition) {
		return mPeriodicalDAO.getPeriodicalCount(insId, condition);
	}

}
