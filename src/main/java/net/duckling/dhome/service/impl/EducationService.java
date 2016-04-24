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

import java.util.List;

import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.dao.IEducationDAO;
import net.duckling.dhome.dao.IInstitutionDAO;
import net.duckling.dhome.dao.ISimpleUserDAO;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.people.Education;
import net.duckling.dhome.service.IEducationService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionPeopleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2012-8-14
 */
@Service("educationService")
public class EducationService implements IEducationService{
	@Autowired
	private IEducationDAO eduDAO;
	@Autowired
	private IInstitutionDAO institutionDAO;
	@Autowired
	private ISimpleUserDAO	simpleDAO;
	@Autowired
	private IInstitutionHomeService institutionHomeService;
	@Autowired
	private IInstitutionPeopleService institutionPeopleService;
	@Override
	public List<Education> getEducationsByUid(int uid) {
		List<Education> edus= eduDAO.getEdusByUID(uid);
		if(CommonUtils.isNull(edus)){
			return null;
		}
		for(Education edu:edus){
			edu.setUserName(simpleDAO.getUser(edu.getUid()).getZhName());
			InstitutionHome home=institutionHomeService.getInstitutionByInstitutionId(edu.getInsitutionId());
			//此处添加institutionId>0的条件是因为逻辑更改后，查询insId=0的机构主页会返回某个莫名其妙的机构，而不是返回空
			if(edu.getInsitutionId()>0 && home!=null){
				edu.setDomain(home.getDomain());
				edu.setInstitutionName(institutionDAO.getInstitution(edu.getInsitutionId()).getZhName());
			}
		}
		return edus;
	}
	
	@Override
	public Education getEducation(int id){
		Education edu = eduDAO.getEducation(id);
		if(null!=edu && edu.getInsitutionId()>0){
			edu.setInstitutionName(institutionDAO.getInstitution(edu.getInsitutionId()).getZhName());
		}
		return edu;
	}

	@Override
	public int createEducation(Education edu) {
		int insId = edu.getInsitutionId();
		//此处加入insId>0是因为修改工作经历添加的逻辑后，导致work的insId可能为0，
		if(insId>0){
			//建立机构主页，没有，则不创建在，institution_people里面加一条
			institutionHomeService.createInstitutionHome(edu.getUid(), institutionDAO.getInstitution(insId));
			institutionHomeService.createAliasInstitutionName(edu.getAliasInstitutionName(), insId, true);
		}
		return eduDAO.createEducation(edu);
	}

	@Override
	public boolean updateEducationById(Education edu) {
		Education oldEdu=getEducation(edu.getId());
		int insId = edu.getInsitutionId();
		int oldInsId = oldEdu.getInsitutionId();
		//此处加入insId>0是因为修改工作经历添加的逻辑后，导致work的insId可能为0，
		if(oldInsId!=insId){
			if(insId>0){
				institutionHomeService.createInstitutionHome(edu.getUid(), institutionDAO.getInstitution(insId));
				//如果新的机构名称不是别名，自然也不可能是官方名，则创建官方名称
				if(institutionHomeService.checkTypeOfInstitutionName(edu.getAliasInstitutionName(), insId, false)){
					institutionHomeService.createAliasInstitutionName(edu.getAliasInstitutionName(), insId, true);
				}
			}
			if(oldInsId>0){//取消用户与旧机构的关联关系
				institutionPeopleService.deleteMember(edu.getUid(), oldInsId);
			}
		}
		return eduDAO.updateEducationById(edu);
	}

	@Override
	public void deleteEducation(int id) {
		Education edu=getEducation(id);
		institutionPeopleService.deleteMember(edu.getUid(), edu.getInsitutionId());
		eduDAO.deleteEducation(id);
	}

	@Override
	public List<Education> getEducationWithZeroInstitutionId() {
		return eduDAO.getEducationWithZeroInstitutionId();
	}

	@Override
	public List<Education> getEducationWithNonZeroInstitutionId() {
		return eduDAO.getEducationWithNonZeroInstitutionId();
	}
	
}
