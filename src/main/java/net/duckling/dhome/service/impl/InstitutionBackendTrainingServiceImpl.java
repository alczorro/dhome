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
import net.duckling.dhome.dao.IInstitutionMemberFromVmtDAO;
import net.duckling.dhome.dao.IInstitutionTrainingDAO;
import net.duckling.dhome.dao.IInstitutionTrainingSearchDAO;
import net.duckling.dhome.domain.institution.InstitutionAcademicAuthor;
import net.duckling.dhome.domain.institution.InstitutionMemberFromVmt;
import net.duckling.dhome.domain.institution.InstitutionTraining;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.service.IInstitutionBackendTrainingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstitutionBackendTrainingServiceImpl implements
		IInstitutionBackendTrainingService {
	@Autowired
	private IInstitutionTrainingDAO trainingDAO;
	@Autowired
	IInstitutionTrainingSearchDAO trainingSearchDAO;
	@Autowired
	IInstitutionMemberFromVmtDAO vmtDAO;

	@Override
	public PageResult<InstitutionTraining> getTraining(int institutionId, int page, SearchInstitutionCondition condition) {
		PageResult<InstitutionTraining> result=new PageResult<InstitutionTraining>();
		result.setAllSize(trainingDAO.getTrainingCount(institutionId, condition));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(trainingDAO.getTraining(institutionId,(page-1)*result.getPageSize(),result.getPageSize(), condition));
		result.setCurrentPage(page);
		return result;
	}
	@Override
	public void deleteTraining(int[] id){
		trainingDAO.deleteTraining(id);
	}
	@Override
	public int getTrainingCount(int id) {
		return trainingDAO.getTrainingCount(id, new SearchInstitutionCondition());
	}
	@Override
	public void updateTraining(int id, InstitutionTraining training) {
		trainingDAO.updateTraining(id, training);
	}
	@Override
	public int insertTraining(InstitutionTraining training) {
		return trainingDAO.insert(training);
	}
	@Override
	public InstitutionTraining getTrainingById(int id) {
		return trainingDAO.getTrainingById(id); 
	}
	@Override
	public InstitutionTraining getById(int id) {
		return trainingDAO.getById(id); 
	}
	@Override
	public Map<Integer, Integer> getDegreesMap(int institutionId) {
		return trainingDAO.getDegreesMap(institutionId);
	}
	@Override
	public List<InstitutionTraining> search(String keyword) {
		return trainingDAO.search(keyword);
	}
	
	@Override
	public List<InstitutionTraining> getTrainingsByUID(int uid) {
		return trainingDAO.getTrainingsByUID(uid);
	}
	@Override
	public PageResult<InstitutionTraining> getTrainingByUser(int userId,
			int page, SearchInstitutionCondition condition) {
		PageResult<InstitutionTraining> result=new PageResult<InstitutionTraining>();
		result.setAllSize(trainingDAO.getTrainingCountByUser(userId, condition));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(trainingDAO.getTrainingByUser(userId,(page-1)*result.getPageSize(),result.getPageSize(),condition));
		result.setCurrentPage(page);
		return result;
	}
	@Override
	public int getTrainingCountByUser(int userId) {
		return trainingDAO.getTrainingCountByUser(userId, new SearchInstitutionCondition());
	}
	@Override
	public Map<Integer, Integer> getDegreesByUser(int userId) {
		return trainingDAO.getDegreesByUser(userId);
	}
	@Override
	public void deleteTrainingUser(int[] trainingId) {
		trainingDAO.deleteTrainingUser(trainingId);
	}
	@Override
	public void insertTrainingUser(int trainingId, int userId) {
		trainingDAO.insertTrainingUser(trainingId, userId);
	}
	@Override
	public List<InstitutionTraining> getTrainingByKeyword(int offset, int size,
			String keyword) {
		return trainingSearchDAO.getTrainingByKeyword(offset, size, keyword);
	}
	@Override
	public List<InstitutionTraining> getTrainingByInit(int offset, int size,
			String userName) {
		return trainingSearchDAO.getTrainingByInit(offset, size, userName);
	}
	@Override
	public void insertTraining(String[] trainingId, int userId) {
		trainingSearchDAO.insertTraining(trainingId, userId);
	}
	@Override
	public List<Integer> getExistTrainingIds(int uid) {
		return trainingSearchDAO.getExistTrainingIds(uid);
	}
	
	/**
	 * 添加单个员工论著
	 */
	@Override
	public int create(InstitutionTraining training, int institutionId) {
		training.setInstitutionId(institutionId);
		return insertTraining(training);
	}
	

	@Override
	public void update(InstitutionTraining training) {
		trainingDAO.updateTraining(training.getId(),training);
	}

	@Override
	public void deleteUserRef(int uid,int tid) {
		trainingDAO.deleteUserRef(uid, tid);
	}

	@Override
	public void createUserRef(int uid, int tid) {
		trainingDAO.createUserRef(uid, tid);
	}
	@Override
	public int getTrainingCount(int id, SearchInstitutionCondition condition) {
		return trainingDAO.getTrainingCount(id, condition);
	}
	@Override
	public Map<String, InstitutionMemberFromVmt> getAllMember(int ins) {
		Map<String, InstitutionMemberFromVmt> map = new HashMap<String, InstitutionMemberFromVmt>();
		List<InstitutionMemberFromVmt> members = vmtDAO.getAllUser();
		for(InstitutionMemberFromVmt member : members){
			map.put(member.getUmtId(), member);
		}
//		caculateSubscript(map);
		return map;
	}
	@Override
	public List<InstitutionTraining> getStudentByUser(int userId) {
		return trainingDAO.getStudentByUser(userId);
	}
	@Override
	public InstitutionMemberFromVmt getMemberByUser(int ins, String email) {
		return vmtDAO.getMemberByCstnetId(ins, email);
	}
	
}
