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

import java.util.List;
import java.util.Map;

import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.dhome.domain.institution.InstitutionMemberFromVmt;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.PageResult;

public interface IInstitutionBackendService {
	
	PageResult<InstitutionMemberFromVmt> getVmtMember(int institutionId,int page,SearchInstitutionCondition condition);
	
	int getVmtMemberCount(int institutionId);
	int getVmtMemberCount(int institutionId,SearchInstitutionCondition condition);

	boolean isAdmin(int insId,String email);
	boolean isMember(int insId,String email);

	List<InstitutionDepartment> getVmtDepartment(List<Integer> deptId);
	List<InstitutionDepartment> getVmtDepartment(int insId);

	InstitutionMemberFromVmt getVmtMemberByUmtId(int institutionId, String umtId);

	InstitutionDepartment getVmtDepartmentById(int departId);

	void deleteMember(int insId, String[] umtId);
	
	void updateBaseMember(InstitutionMemberFromVmt member);
	void insertMember(InstitutionMemberFromVmt member);
	void updateMember(InstitutionMemberFromVmt member);
	
	Map<String, Integer> getTitlesMap(int insId,int dept); //职称
	Map<String, Integer> getDegreesMap(int insId,int dept); //学历
	Map<String, Integer> getAgesMap(int insId,int dept);  //年龄
	
	void updateDeptShortName(int id,String shortName,int listRank);
	//统计各个成果数量
	int getMemberCount(int ins,int departId,int year);
	int getPaperCount(int ins,int departId,int year);
	int getTreatiseCount(int ins,int departId,int year);
	int getAwardCount(int ins,int departId,int year);
	int getCopyrightCount(int ins,int departId,int year);
	int getPatentCount(int ins,int departId,int year);
	int getTopicCount(int ins,int departId,int year);
	int getAcademicCount(int ins,int departId,int year);
	int getPeriodicalCount(int ins,int departId,int year);
	int getTrainingCount(int ins,int departId,int year);

	List<InstitutionDepartment> getVmtDepartmentOrderPinyin(int insId);

	void batchUpdateDepartment(int insId, List<InstitutionDepartment> depts);
}
