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
import net.duckling.dhome.dao.IInstitutionAchievementsDAO;
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
import net.duckling.dhome.domain.institution.InstitutionAchievements;
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
import net.duckling.dhome.service.IInstitutionAchievementsService;
import net.duckling.dhome.service.IInstitutionGrantsService;
import net.duckling.dhome.service.IInstitutionJobApplyService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstitutionAchievementsServiceImpl implements IInstitutionAchievementsService{
	
	private static final Logger LOG=Logger.getLogger(InstitutionAchievementsServiceImpl.class);
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
	private IInstitutionAchievementsDAO achievementsDAO;
	@Autowired
	private IInstitutionJobApplyDAO applyDAO;
	@Autowired
	private IInstitutionMemberFromVmtDAO vmtDAO;
	
	@Override
	public PageResult<InstitutionAchievements> getList(int insId,int page) {
		PageResult<InstitutionAchievements> result=new PageResult<InstitutionAchievements>();
		result.setAllSize(achievementsDAO.getListCount(insId));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(achievementsDAO.getList(insId, (page-1)*result.getPageSize(),result.getPageSize()));
		result.setCurrentPage(page);
		return result;
	}
	@Override
	public void creat(InstitutionAchievements achievements) {
		achievements.setTitle(achievements.getYear()+"年大气物理研究所绩效考核表");
		achievementsDAO.creat(achievements);
	}
	@Override
	public void delete(int insId, int applicationId) {
		achievementsDAO.delete(applicationId);
//		applyDAO.delete(insId, applicationId);
		
	}
//	@Override
//	public List<InstitutionAchievements> getAllList(int insId) {
//		return achievementsDAO.getAllList(insId);
//	}
	@Override
	public InstitutionAchievements getById(int achievementsId) {
		return achievementsDAO.getById(achievementsId);
	}
	@Override
	public List<InstitutionAuthor> getAuthorsByUid(int userId) {
		return authorDAO.getAuthorsByUid(userId);
	}
	@Override
	public List<InstitutionPaper> getPapersByAuthor(
			List<InstitutionAuthor> authors, int startYear, int endYear) {
		List<InstitutionPaper> list=new ArrayList<InstitutionPaper>();
		for (InstitutionAuthor author : authors) {
			list.addAll(paperDAO.getPapersByAuthor(author.getId(),startYear,endYear));
		}
		return list;
	}

}
