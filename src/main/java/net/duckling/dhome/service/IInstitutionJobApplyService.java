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
package net.duckling.dhome.service;

import java.util.Date;
import java.util.List;

import net.duckling.dhome.domain.institution.InstitutionAcademic;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionAward;
import net.duckling.dhome.domain.institution.InstitutionCopyright;
import net.duckling.dhome.domain.institution.InstitutionJobApplication;
import net.duckling.dhome.domain.institution.InstitutionJobApply;
import net.duckling.dhome.domain.institution.InstitutionMemberFromVmt;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.InstitutionPatent;
import net.duckling.dhome.domain.institution.InstitutionTopic;
import net.duckling.dhome.domain.institution.InstitutionTraining;
import net.duckling.dhome.domain.object.PageResult;

public interface IInstitutionJobApplyService {
	
	PageResult<InstitutionJobApplication> getList(int insId, int page);
	void creat(InstitutionJobApplication jobApplication);
	void delete(int insId,int applicationId);
	PageResult<InstitutionJobApply> getListByApplicationId(int insId,int applicationId,int page);
	PageResult<InstitutionJobApply> getListByUserId(int insId,int userId,int page);
	List<InstitutionJobApplication> getAllList(int insId);
	InstitutionJobApply getById(int applyId);
	void deleteById(int applyId);
	List<InstitutionJobApply> getIdByUser(int userId,int insId);
	
	void insert(int insId,int userId,int applicationId,int status,InstitutionJobApply jobApply);
	void update(int applyId,int status,InstitutionJobApply jobApply);
	
	List<InstitutionAuthor> getAuthorsByUid(int userId);
	//根据作者查询第一作者论文
	 List<InstitutionPaper> getPapersByAuthorId(List<InstitutionAuthor> authors,int startYear,int endYear);
	 List<InstitutionPaper> getOtherPapersByAuthorId(List<InstitutionAuthor> authors,int startYear,int endYear);
	 //论文数量
	 int getAllSciCount(int userid);
	 List<InstitutionPaper> getAllFirst(List<InstitutionAuthor> authors);
	 //根据作者查询课题
	 List<InstitutionTopic> getTopicsByAuthorId(List<InstitutionAuthor> authors,int userId,int startYear,int endYear);
	//根据作者查询专利和软件著作权
	List<InstitutionPatent> getPatentsByAuthorId(List<InstitutionAuthor> authors,int startYear,int endYear);
	List<InstitutionCopyright> getCopyrightsByAuthorId(List<InstitutionAuthor> authors,int startYear,int endYear);
	//根据作者查询奖励
	List<InstitutionAward> getAwardsByAuthorId(List<InstitutionAuthor> authors,int startYear,int endYear);
	//学术任职
	List<InstitutionAcademic> getAcademicsByAuthorId(int userId,int startYear, int endYear);
	//学生
	List<InstitutionTraining> getTrainingsByUserId(int userId,Date startTime, Date endTime);
	
	InstitutionMemberFromVmt getMemberByUid(int insId, int uid);

}
