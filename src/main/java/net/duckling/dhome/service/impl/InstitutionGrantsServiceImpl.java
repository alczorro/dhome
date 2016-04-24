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
import net.duckling.dhome.dao.IInstitutionGrantsDAO;
import net.duckling.dhome.dao.IInstitutionMemberFromVmtDAO;
import net.duckling.dhome.dao.IInstitutionTrainingDAO;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionGrants;
import net.duckling.dhome.domain.institution.InstitutionTraining;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.service.IInstitutionGrantsService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstitutionGrantsServiceImpl implements IInstitutionGrantsService{
	
	private static final Logger LOG=Logger.getLogger(InstitutionGrantsServiceImpl.class);
	@Autowired
	private IInstitutionGrantsDAO grantsDAO;
	@Autowired
	private IInstitutionMemberFromVmtDAO fromVmtDAO;
	@Autowired
	private IInstitutionTrainingDAO trainingDAO;;
	@Override
	public PageResult<InstitutionGrants> getList(Integer userId, Integer degree, Integer status, Integer studentId, int page) {
		PageResult<InstitutionGrants> result=new PageResult<InstitutionGrants>();
		result.setAllSize(grantsDAO.getListCount(userId, degree, status, studentId));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(grantsDAO.getList(userId, degree, status, studentId, (page-1)*result.getPageSize(),result.getPageSize()));
		result.setCurrentPage(page);
		return result;
	}
	
	@Override
	public PageResult<InstitutionGrants> getListLasted(Integer userId, Integer degree, Integer status, int page) {
		PageResult<InstitutionGrants> result=new PageResult<InstitutionGrants>();
		result.setAllSize(grantsDAO.getListCountLasted(userId, degree, status));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(grantsDAO.getListLasted(userId, degree, status, (page-1)*result.getPageSize(),result.getPageSize()));
		result.setCurrentPage(page);
		return result;
	}
	
	@Override
	public List<InstitutionGrants> getListByBatchNo(Integer userId, String batchNo) {
		return grantsDAO.getListByBatchNo(userId, batchNo);
	}

	@Override
	public void delete(int userId, int[] idArr) {
		grantsDAO.delete(userId, idArr);
	}


	@Override
	public InstitutionGrants getById(int id) {
		return grantsDAO.getById(id);
	}

	@Override
	public int create(InstitutionGrants grants) {
		int institutionId = fromVmtDAO.getInstituionId(grants.getUserId());
		
		InstitutionTraining student = trainingDAO.getTrainingById(grants.getStudentId());
		grants.setDegree(student.getDegree());
		grants.setStudentName(student.getStudentName());
		grants.setClassName(student.getClassName());
//		grants.setTutor(student.getUmtId());
		grants.setInstitutionId(institutionId);
		grants.setSumFee(grants.getScholarship1().add(grants.getScholarship2()).add(grants.getAssistantFee()));
		return grantsDAO.create(grants);
	}

	@Override
	public void update(InstitutionGrants grants) {
		grantsDAO.update(grants);
	}
	
	@Override
	public void updateStatus(int uid, int[] ids) {
		grantsDAO.updateStatus(uid, ids);
	}

	@Override
	public int getCount(Integer userId, Integer degree) {
		return grantsDAO.getListCount(userId, degree, null, null);
	}

	@Override
	public Map<Integer, Integer> getDegreesCount(Integer insId, Integer userId) {
		return grantsDAO.getDegreesCount(insId, userId);
	}
	
	@Override
	public Map<String, Integer> getStatusCount(Integer userId){
		return grantsDAO.getStatusCount(userId);
	}

	@Override
	public PageResult<String> getBatchList(Integer userId, Integer degree, int page) {
		PageResult<String> result=new PageResult<String>();
		result.setAllSize(grantsDAO.getBatchCount(userId, degree));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(grantsDAO.getBatchList(userId, degree, (page-1)*result.getPageSize(),result.getPageSize()));
		result.setCurrentPage(page);
		return result;
	}

	@Override
	public PageResult<InstitutionGrants> getListByBatchNo(Integer userId,
			String batchNo, int page) {
		PageResult<InstitutionGrants> result=new PageResult<InstitutionGrants>();
		result.setAllSize(grantsDAO.getBatchGrantsCount(userId, batchNo));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(grantsDAO.getListByBatchNo(userId, batchNo, (page-1)*result.getPageSize(),result.getPageSize()));
		result.setCurrentPage(page);
		return result;
	}

	@Override
	public Map<String,List<InstitutionGrants>> getStudentByUser(int userId) {
		List<InstitutionGrants> grants = grantsDAO.getStudentByUser(userId);
		Map<String,List<InstitutionGrants>> map=new HashMap<String,List<InstitutionGrants>>();
		if(CommonUtils.isNull(grants)){
			return map;
		}
		for (InstitutionGrants grant : grants) {
			if(map.containsKey(grant.getBatchNo())){
				map.get(grant.getBatchNo()).add(grant);
			}else{
				List<InstitutionGrants> list=new ArrayList<InstitutionGrants>();
				list.add(grant);
				map.put(grant.getBatchNo(), list);
			}
		}
		return map;
	}

	@Override
	public int getBatchCount(Integer userId, Integer degree) {
		return grantsDAO.getBatchCount(userId, degree);
	}

	@Override
	public int getListCount(Integer userId, Integer degree, Integer status) {
		return grantsDAO.getListCount(userId, degree, status, null);
	}

}
