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
import net.duckling.dhome.dao.IInstitutionBackendTreatiseAuthorDAO;
import net.duckling.dhome.dao.IInstitutionBackendTreatiseDAO;
import net.duckling.dhome.dao.IInstitutionDepartDAO;
import net.duckling.dhome.dao.IInstitutionTreatiseSearchDAO;
import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.dhome.domain.institution.InstitutionTreatise;
import net.duckling.dhome.domain.institution.InstitutionTreatiseAuthor;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.service.IInstitutionBackendTreatiseService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstitutionBackendTreatiseServiceImpl implements
		IInstitutionBackendTreatiseService {

	private static final Logger LOG=Logger.getLogger(InstitutionBackendTreatiseServiceImpl.class);
	
	@Autowired
	IInstitutionBackendTreatiseDAO treatiseDao;
	
	@Autowired
	IInstitutionBackendTreatiseAuthorDAO authorDao;
	
	@Autowired
	private IInstitutionTreatiseSearchDAO treatiseSearchDAO;
	
	@Autowired
	private IInstitutionDepartDAO deptDAO;
	
	@Override
	public void insert(List<InstitutionTreatise> treatises) {
        treatiseDao.insert(treatises);
	}

	@Override
	public void insert(InstitutionTreatise treatise) {
        treatiseDao.insert(treatise);
	}

	@Override
	public PageResult<InstitutionTreatise> getTreatises(int insId, int page,SearchInstitutionCondition condition) {
		PageResult<InstitutionTreatise> result = new PageResult<InstitutionTreatise>();
//		result.setPageSize(20);
		result.setAllSize(treatiseDao.getTreatiseCount(insId,condition));
		if(result.getMaxPage() < page){
			page = result.getMaxPage();
		}
		result.setDatas(treatiseDao.getTreatises(insId,page == 0? 0 : (page - 1)*result.getPageSize(), result.getPageSize(),condition));
		
		result.setCurrentPage(page);
		return result;	
	}

	@Override
	public InstitutionTreatise getTreatise(int treatiseId) {
		return treatiseDao.getTreatise(treatiseId);
	}

	@Override
	public int getTreatiseCount(int insId) {
		return treatiseDao.getTreatiseCount(insId,new SearchInstitutionCondition());
	}

	@Override
	public void delete(int insId, int[] treatiseIds) {
		treatiseDao.delete(insId, treatiseIds);
	}

	@Override
	public void delete(int insId, int treatiseId) {
		treatiseDao.delete(insId, treatiseId);
	}

	@Override
	public void deleteAll(int insId) {
		treatiseDao.deleteAll(insId);
	}

	@Override
	public int create(InstitutionTreatise treatise, int institutionId,
			int[] authorIds, int[] order, boolean[] communicationAuthors,
			boolean[] authorStudents, boolean[] authorTeacher) {
		treatise.setInstitutionId(institutionId);
		treatise.setId(treatiseDao.create(treatise));
		authorDao.createRef(treatise.getId(), authorIds, order, communicationAuthors, authorStudents, authorTeacher);
		return treatise.getId();
	}

	@Override
	public void update(InstitutionTreatise treatise, int institutionId,
			int[] authorIds, int[] order, boolean[] communicationAuthors,
			boolean[] authorStudents, boolean[] authorTeacher) {
		
		if(treatise.getId() == 0){
			LOG.error("update: treatise.id is null");
			return;
		}
		
		treatiseDao.update(treatise);
		authorDao.deleteRef(treatise.getId());
		authorDao.createRef(treatise.getId(), authorIds, order, communicationAuthors, authorStudents, authorTeacher);

	}

	@Override
	public List<InstitutionTreatiseAuthor> getAuthorByTreatiseId(int treatiseId) {
		return authorDao.getByTreatiseId(treatiseId);
	}

	@Override
	public List<InstitutionTreatiseAuthor> searchAuthor(String keyword) {
		return authorDao.search(keyword);
	}

	@Override
	public Map<Integer, List<InstitutionTreatiseAuthor>> getAuthorsMap(
			List<Integer> treatiseIds) {
		List<InstitutionTreatiseAuthor> authors = authorDao.getByTreatiseIds(treatiseIds);
		Map<Integer, List<InstitutionTreatiseAuthor>> map = new HashMap<Integer, List<InstitutionTreatiseAuthor>>();
		if(CommonUtils.isNull(authors)){
			return map;
		}
		
		for(InstitutionTreatiseAuthor author : authors){
			int treatiseId = author.getTreatiseId();
			if(map.containsKey(treatiseId)){
				map.get(treatiseId).add(author);
			}else{
				List<InstitutionTreatiseAuthor> list = new ArrayList<InstitutionTreatiseAuthor>();
				list.add(author);
				map.put(treatiseId, list);
			}
		}
		caculateSubscript(map);
		return map;
	}

	@Override
	public InstitutionTreatiseAuthor getAuthorById(int treatiseId, int authorId) {
		return authorDao.getById(treatiseId, authorId);
	}

	
	/**
	 * 统计下标，单位相同为同一下标
	 * @param map
	 */
	public void caculateSubscript(Map<Integer, List<InstitutionTreatiseAuthor>> map){
		Set<Entry<Integer, List<InstitutionTreatiseAuthor>>> entrys = map.entrySet();
		for(Entry<Integer, List<InstitutionTreatiseAuthor>> entry : entrys){
		    Map<String, Integer> subcscriptMap = new HashMap<String, Integer>();
		    for(InstitutionTreatiseAuthor author : entry.getValue()){
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
	public List<InstitutionTreatise> getTreatisesByUID(int uid) {
		return treatiseDao.getTreatisesByUID(uid);
	}

	@Override
	public PageResult<InstitutionTreatise> getTreatiseByUser(int userId,
			int page, SearchInstitutionCondition condition) {
		PageResult<InstitutionTreatise> result=new PageResult<InstitutionTreatise>();
		result.setAllSize(treatiseDao.getTreatiseCountByUser(userId, condition));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(treatiseDao.getTreatiseByUser(userId,(page-1)*result.getPageSize(),result.getPageSize(),condition));
		result.setCurrentPage(page);
		return result;
	}

	@Override
	public int getTreatiseCountByUser(int userId) {
		return treatiseDao.getTreatiseCountByUser(userId, new SearchInstitutionCondition());
	}

	@Override
	public void deleteTreatiseUser(int[] treatiseId) {
		treatiseDao.deleteTreatiseUser(treatiseId);
	}


	@Override
	public List<InstitutionTreatise> getTreatiseByKeyword(int offset, int size,
			String keyword) {
		return treatiseSearchDAO.getTreatiseByKeyword(offset, size, keyword);
	}

	@Override
	public List<InstitutionTreatise> getTreatiseByInit(int offset, int size,
			String userName) {
		return treatiseSearchDAO.getTreatiseByInit(offset, size, userName);
	}

	@Override
	public List<Integer> getExistTreatiseIds(int uid) {
		return treatiseSearchDAO.getExistTreatiseIds(uid);
	}

	/**
	 * 添加单个员工论著
	 */
	@Override
	public int create(InstitutionTreatise treatise, int institutionId,
			int authorId, int order) {
		return create(treatise, institutionId, new int[]{authorId}, new int[]{order}, new boolean[]{false}, new boolean[]{false}, new boolean[]{false});
	}
	

	@Override
	public void createAuthor(InstitutionTreatiseAuthor author) {
		if(!authorDao.isExist(author)){
			authorDao.createAuthor(author);
		}
	}

	@Override
	public void update(InstitutionTreatise treatise,
			int authorId, int order) {
		treatiseDao.update(treatise);
		authorDao.updateRef(authorId, treatise.getId(),order);
	}

	@Override
	public void deleteUserRef(int uid,int tid) {
		treatiseDao.deleteUserRef(uid, tid);
	}

	@Override
	public void createUserRef(int uid, int tid, int athorOrder) {
		treatiseDao.createUserRef(uid, tid, athorOrder);
	}

	@Override
	public void updateUserRef(int uid, int tid, int authorOrder) {
		treatiseDao.updateUserRef(uid, tid, authorOrder);
	}
	
	public void insertTreatise(String[] treatiseId, int userId) {
		treatiseSearchDAO.insertTreatise(treatiseId, userId);
	}

	@Override
	public Map<Integer, Integer> getPublishersMap(int insId) {
		return treatiseDao.getPublishersMap(insId);
	}

	@Override
	public void insertTreatiseUser(int treatiseId, int userId) {
		treatiseDao.insertTreatiseUser(treatiseId, userId);
	}

	@Override
	public int getTreatiseCount(int insId, SearchInstitutionCondition condition) {
		return treatiseDao.getTreatiseCount(insId, condition);
	}

	@Override
	public List<InstitutionDepartment> getDepartments(int insId) {
		return deptDAO.getDepartsByInsId(insId);
	}

	@Override
	public List<InstitutionTreatise> insertTreatises(
			List<InstitutionTreatise> treatises) {
		return treatiseDao.insertTreatises(treatises);
	}

	@Override
	public int create(InstitutionTreatise treatise) {
		return treatiseDao.create(treatise);
	}

	@Override
	public int insertAuthor(String name, String company) {
		return authorDao.insertAuthor(name, company);
	}

	@Override
	public void insertRef(InstitutionTreatise treatise) {
		if(treatise.getAuthor()!=null&&!"".equals(treatise.getAuthor())){
			int authorId=authorDao.insertAuthor(treatise.getAuthor(), treatise.getAuthorCompany());
			authorDao.insertRef(treatise.getId(), authorId, treatise.getAuthorOrder());
		}
		if(treatise.getAuthor2()!=null&&!"".equals(treatise.getAuthor2())){
			int authorId=authorDao.insertAuthor(treatise.getAuthor2(), treatise.getAuthor2Company());
			authorDao.insertRef(treatise.getId(), authorId, treatise.getAuthor2Order());
		}
		if(treatise.getAuthor3()!=null&&!"".equals(treatise.getAuthor3())){
			int authorId=authorDao.insertAuthor(treatise.getAuthor3(), treatise.getAuthor3Company());
			authorDao.insertRef(treatise.getId(), authorId, treatise.getAuthor3Order());
		}
	}

}
