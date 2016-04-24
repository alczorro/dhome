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
import net.duckling.dhome.dao.IInstitutionDAO;
import net.duckling.dhome.dao.ISimpleUserDAO;
import net.duckling.dhome.dao.IWorkDAO;
import net.duckling.dhome.domain.institution.Institution;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.people.Work;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionPeopleService;
import net.duckling.dhome.service.IWorkService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**工作服务层实现类*/
@Service
public class WorkService implements IWorkService{
	@Autowired
	private IWorkDAO workDAO;
	@Autowired
	private IInstitutionDAO institutionDAO;
	@Autowired
	private ISimpleUserDAO	simpleDAO;
	@Autowired
	private IInstitutionHomeService institutionHomeService;
	@Autowired 
	private IInstitutionPeopleService peopleService;
	
	@Override
	public List<Work> getWorksByUID(int uid) {
		List<Work> works= workDAO.getWorksByUID(uid);
		if(CommonUtils.isNull(works)){
			return null;
		}
		for(Work work:works){
			work.setUserName(simpleDAO.getUser(work.getUid()).getZhName());
			InstitutionHome home=institutionHomeService.getInstitutionByInstitutionId(work.getInstitutionId());
			//此处添加institutionId>0的条件是因为逻辑更改后，查询insId=0的机构主页会返回某个莫名其妙的机构，而不是返回空
			if(work.getInstitutionId()>0 && home!=null){
				work.setDomain(home.getDomain());
				work.setInstitutionName(institutionDAO.getInstitution(work.getInstitutionId()).getZhName());
			}
			
		}
		return works;
	}
	
	@Override
	public Work getWork(int id){
		Work work = workDAO.getWork(id);
		if(null!=work && work.getInstitutionId()>0){
			work.setInstitutionName(institutionDAO.getInstitution(work.getInstitutionId()).getZhName());
		}
		return work;
	}
	
	@Override
	public Institution getInstitutionByName(String name){
		return institutionDAO.getInstitutionByName(name);
	}

	@Override
	public int createInstitution(String name) {
		return institutionDAO.createInstitution(name);
	}
	
	@Override
	public boolean updateWorkById(Work work){
		Work oldWork=getWork(work.getId());
		int oldInsId = oldWork.getInstitutionId();
		int insId = work.getInstitutionId();
		//此处加入insId>0是因为修改工作经历添加的逻辑后，导致work的insId可能为0，
		if(oldInsId!=insId){
			if(insId>0){
				institutionHomeService.createInstitutionHome(work.getUid(), institutionDAO.getInstitution(insId));
				//如果新的机构名称不是别名，自然也不可能是官方名，则创建官方名称
				if(institutionHomeService.checkTypeOfInstitutionName(work.getAliasInstitutionName(), insId, false)){
					institutionHomeService.createAliasInstitutionName(work.getAliasInstitutionName(), insId, true);
				}
			}
			if(oldInsId>0){//取消用户与旧机构的关联关系
				peopleService.deleteMember(work.getUid(), oldInsId);
			}
		}
		return workDAO.updateWorkById(work);
	}

	@Override
	public int createWork(Work work){
		int insId = work.getInstitutionId();
		//此处加入insId>0是因为修改工作经历添加的逻辑后，导致work的insId可能为0，
		if(insId>0){
			//建立机构主页，没有，则不创建在，institution_people里面加一条
			institutionHomeService.createInstitutionHome(work.getUid(), institutionDAO.getInstitution(insId));
			institutionHomeService.createAliasInstitutionName(work.getAliasInstitutionName(), insId, true);
		}
		return workDAO.createWork(work);
	}
	
	@Override
	public void delteWork(int id) {
		Work work=getWork(id);
		peopleService.deleteMember(work.getUid(),work.getInstitutionId());
		workDAO.deleteWork(id);
	}

	@Override
	public List<Institution> getDsnInstitutionsByPrefixName(String prefix) {
		return institutionDAO.getDsnInstitutionsByPrefixName(prefix);
	}

	@Override
	public int searchOfficalInstitution(String insName) {
		return institutionDAO.searchOfficalInstitution(insName);
	}

	@Override
	public List<Work> getWorkWithZeroInstitutionId() {
		return workDAO.getWorkWithZeroInstitutionId();
	}

	@Override
	public List<Work> getWorkWithNonZeroInstitutionId() {
		return workDAO.getWorkWithNonZeroInstitutionId();
	} 
}
