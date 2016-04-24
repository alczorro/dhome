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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.common.util.CommonUtils;
import net.duckling.dhome.dao.IInstitutionAcademicDAO;
import net.duckling.dhome.dao.IInstitutionBackendAwardDAO;
import net.duckling.dhome.dao.IInstitutionBackendPaperAuthorDAO;
import net.duckling.dhome.dao.IInstitutionBackendPaperDAO;
import net.duckling.dhome.dao.IInstitutionCopyrightDAO;
import net.duckling.dhome.dao.IInstitutionGrantsDAO;
import net.duckling.dhome.dao.IInstitutionJobApplicationDAO;
import net.duckling.dhome.dao.IInstitutionJobApplyDAO;
import net.duckling.dhome.dao.IInstitutionMemberFromVmtDAO;
import net.duckling.dhome.dao.IInstitutionPatentDAO;
import net.duckling.dhome.dao.IInstitutionTopicDAO;
import net.duckling.dhome.dao.IInstitutionTrainingDAO;
import net.duckling.dhome.domain.institution.InstitutionAcademic;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionAward;
import net.duckling.dhome.domain.institution.InstitutionCopyright;
import net.duckling.dhome.domain.institution.InstitutionGrants;
import net.duckling.dhome.domain.institution.InstitutionJobApplication;
import net.duckling.dhome.domain.institution.InstitutionJobApply;
import net.duckling.dhome.domain.institution.InstitutionMemberFromVmt;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.InstitutionPatent;
import net.duckling.dhome.domain.institution.InstitutionTopic;
import net.duckling.dhome.domain.institution.InstitutionTraining;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.service.IInstitutionGrantsService;
import net.duckling.dhome.service.IInstitutionJobApplyService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstitutionJobApplyServiceImpl implements IInstitutionJobApplyService{
	
	private static final Logger LOG=Logger.getLogger(InstitutionJobApplyServiceImpl.class);
	@Autowired
	private IInstitutionBackendPaperDAO paperDAO;
	@Autowired
	private IInstitutionTopicDAO topicDAO;
	@Autowired
	private IInstitutionPatentDAO patentDAO;
	@Autowired
	private IInstitutionCopyrightDAO copyrightDAO;
	@Autowired
	private IInstitutionBackendAwardDAO awardDAO;
	@Autowired
	private IInstitutionAcademicDAO academicDAO;
	@Autowired
	private IInstitutionTrainingDAO trainingDAO;
	@Autowired
	private IInstitutionBackendPaperAuthorDAO authorDAO;
	@Autowired
	private IInstitutionJobApplicationDAO applicationDAO;
	@Autowired
	private IInstitutionJobApplyDAO applyDAO;
	@Autowired
	private IInstitutionMemberFromVmtDAO vmtDAO;
	
	@Override
	public PageResult<InstitutionJobApplication> getList(int insId,int page) {
		PageResult<InstitutionJobApplication> result=new PageResult<InstitutionJobApplication>();
		result.setAllSize(applicationDAO.getListCount(insId));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(applicationDAO.getList(insId, (page-1)*result.getPageSize(),result.getPageSize()));
		result.setCurrentPage(page);
		return result;
	}
	@Override
	public void creat(InstitutionJobApplication jobApplication) {
		jobApplication.setTitle(jobApplication.getYear()+"年高级专业技术岗位应聘申请表");
		applicationDAO.creat(jobApplication);
	}
	@Override
	public void delete(int insId, int applicationId) {
		applicationDAO.delete(applicationId);
		applyDAO.delete(insId, applicationId);
		
	}
	@Override
	public PageResult<InstitutionJobApply> getListByApplicationId(int insId,
			int applicationId,int page) {
		PageResult<InstitutionJobApply> result=new PageResult<InstitutionJobApply>();
		result.setAllSize(applyDAO.getListCountByApplicationId(insId,applicationId));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(applyDAO.getListByApplicationId(applicationId,insId, (page-1)*result.getPageSize(),result.getPageSize()));
		result.setCurrentPage(page);
		return result;
	}
	@Override
	public List<InstitutionJobApplication> getAllList(int insId) {
		return applicationDAO.getAllList(insId);
	}
	@Override
	public PageResult<InstitutionJobApply> getListByUserId(int insId,
			int userId, int page) {
		PageResult<InstitutionJobApply> result=new PageResult<InstitutionJobApply>();
		result.setAllSize(applyDAO.getListCountByUserId(insId, userId));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(applyDAO.getListByUserId(insId,userId, (page-1)*result.getPageSize(),result.getPageSize()));
		result.setCurrentPage(page);
		return result;
	}
	@Override
	public List<InstitutionJobApply> getIdByUser(int userId, int insId) {
		return applyDAO.getIdByUser(userId, insId);
	}
	
	@Override
	public List<InstitutionAuthor> getAuthorsByUid(int userId) {
		return authorDAO.getAuthorsByUid(userId);
	}
	@Override
	public List<InstitutionPaper> getPapersByAuthorId(
			List<InstitutionAuthor> authors,int startYear,int endYear) {
		List<InstitutionPaper> list=new ArrayList<InstitutionPaper>();
		for (InstitutionAuthor author : authors) {
			list.addAll(paperDAO.getPapersByAuthorId(author.getId(),startYear,endYear));
		}
		return list;
	}
	@Override
	public List<InstitutionPaper> getOtherPapersByAuthorId(
			List<InstitutionAuthor> authors, int startYear, int endYear) {
		List<InstitutionPaper> list=new ArrayList<InstitutionPaper>();
		for (InstitutionAuthor author : authors) {
			list.addAll(paperDAO.getOtherPapersByAuthorId(author.getId(),startYear,endYear));
		}
		return list;
	}
	@Override
	public List<InstitutionTopic> getTopicsByAuthorId(
			List<InstitutionAuthor> authors,int userId, int startYear, int endYear) {
		List<InstitutionTopic> list=new ArrayList<InstitutionTopic>();
		for (InstitutionAuthor author : authors) {
			list.addAll(topicDAO.getTopicsByAuthorId(author.getId(),userId,startYear,endYear));
		}
		return list;
	}
	@Override
	public List<InstitutionPatent> getPatentsByAuthorId(
			List<InstitutionAuthor> authors, int startYear, int endYear) {
		List<InstitutionPatent> list=new ArrayList<InstitutionPatent>();
		for (InstitutionAuthor author : authors) {
			list.addAll(patentDAO.getPatentsByAuthorId(author.getId(),startYear,endYear));
		}
		return list;
	}
	@Override
	public List<InstitutionCopyright> getCopyrightsByAuthorId(
			List<InstitutionAuthor> authors, int startYear, int endYear) {
		List<InstitutionCopyright> list=new ArrayList<InstitutionCopyright>();
		for (InstitutionAuthor author : authors) {
			list.addAll(copyrightDAO.getCopyrightsByAuthorId(author.getId(),startYear,endYear));
		}
		return list;
	}
	@Override
	public List<InstitutionAward> getAwardsByAuthorId(
			List<InstitutionAuthor> authors, int startYear, int endYear) {
		List<InstitutionAward> list=new ArrayList<InstitutionAward>();
		for (InstitutionAuthor author : authors) {
			list.addAll(awardDAO.getAwardsByAuthorId(author.getId(),startYear,endYear));
		}
		return list;
	}
	@Override
	public List<InstitutionAcademic> getAcademicsByAuthorId(int userId,int startYear, int endYear) {
		return academicDAO.getAcademicsByAuthorId(userId,startYear,endYear);
	}
	@Override
	public List<InstitutionTraining> getTrainingsByUserId(int userId,
			Date startTime, Date endTime) {
		return trainingDAO.getTrainingsByUserId(userId, startTime, endTime);
	}
	@Override
	public InstitutionJobApply getById(int applyId) {
		return applyDAO.getById(applyId);
	}
	@Override
	public void deleteById(int applyId) {
		applyDAO.deleteById(applyId);
	}
	@Override
	public void insert(int insId, int userId, int applicationId, int status,
			InstitutionJobApply jobApply) {
		applyDAO.insert(insId, userId, applicationId, status, jobApply);
	}
	@Override
	public void update(int applyId, int status,InstitutionJobApply jobApply) {
		applyDAO.update(applyId, status, jobApply);
	}
	@Override
	public int getAllSciCount(int userid) {
		return paperDAO.getAllSciCount(userid);
	}
	@Override
	public List<InstitutionPaper> getAllFirst(
			List<InstitutionAuthor> authors) {
		List<InstitutionPaper> list=new ArrayList<InstitutionPaper>();
		for (InstitutionAuthor author : authors) {
			list.addAll(paperDAO.getAllFirst(author.getId()));
		}
		return list;
	}
	@Override
	public InstitutionMemberFromVmt getMemberByUid(int insId, int uid) {
		return vmtDAO.getMemberByUid(insId, uid);
	}

}
