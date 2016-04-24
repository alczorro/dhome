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
/**
 * 
 */
package net.duckling.dhome.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.PinyinUtil;
import net.duckling.dhome.dao.IInstitutionDAO;
import net.duckling.dhome.dao.IInstitutionHomeDAO;
import net.duckling.dhome.dao.IInstitutionPeopleDAO;
import net.duckling.dhome.domain.institution.Institution;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionHomeDiscover;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionPeopleService;
import net.duckling.dhome.service.IInstitutionScholarEventService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 机构主页创建服务类
 * @author lvly
 * @since 2012-9-17
 */
@Service
public class InstitutionHomeService implements IInstitutionHomeService{
	private static final Logger LOG = Logger.getLogger(InstitutionHomeService.class);

	@Autowired
	private IInstitutionHomeDAO homeDAO;
	@Autowired
	private IInstitutionPeopleDAO peopleDAO;
	@Autowired
	private IInstitutionDAO institutionDAO;
	@Autowired
	private IInstitutionPeopleService peopleService;
	@Autowired
	private IInstitutionScholarEventService ises;


	@Override
	public synchronized int createInstitutionHome(int userId, Institution institution) {
		if(!peopleDAO.isExists(userId, institution.getId())){
			if(userId>0){
				peopleDAO.createInstitutionPeople(userId, institution.getId());
			}else{
				LOG.error("createInstitutionHome but userId is "+userId);
			}
		}
		if(!homeDAO.isExists(institution.getId())){
			InstitutionHome home=new InstitutionHome();
			home.setInstitutionId(institution.getId());
			home.setCreator(userId);
			home.setCreateTime(new Date());
			if(CommonUtils.isNull(institution.getShortName())){
				String shortName=PinyinUtil.getShortPinyin(institution.getZhName());
				home.setDomain(shortName+institution.getId());
			}else{
				home.setDomain(institution.getShortName());
			}
			home.setLastEditor(userId);
			home.setLastEditTime(new Date());
			home.setName(institution.getZhName());
			return homeDAO.createInstitutionDAO(home);
		}
		
		return -1;
	}
	
	@Override
	public int createInstitutionHomeForAdmin(int adminUserId, Institution institution) {
		if(!homeDAO.isExists(institution.getId())){
			InstitutionHome home=new InstitutionHome();
			home.setInstitutionId(institution.getId());
			home.setCreator(adminUserId);
			home.setCreateTime(new Date());
			if(CommonUtils.isNull(institution.getShortName())){
				String shortName=PinyinUtil.getShortPinyin(institution.getZhName());
				home.setDomain(shortName+institution.getId());
			}else{
				home.setDomain(institution.getShortName());
			}
			home.setLastEditor(adminUserId);
			home.setLastEditTime(new Date());
			home.setName(institution.getZhName());
			return homeDAO.createInstitutionDAO(home);
		}
		
		return -1;
	}

	@Override
	public int getInstitutionIdByDomain(String domain) {
		InstitutionHome ih = homeDAO.getInstitutionByDomain(domain);
		return ih.getInstitutionId();
	}
	@Override
	public InstitutionHome getInstitutionByDomain(String domain) {
		return homeDAO.getInstitutionByDomain(domain);
	}
	@Override
	public void updateInstitutionHome(InstitutionHome home) {
		homeDAO.updateInstitutionHome(home);
		
	}
	@Override
	public void updateInstitutionHomeForZeroFieldById(InstitutionHome home) {
		homeDAO.updateInstitutionHomeForZeroFieldById(home);
	}

	@Override
	public InstitutionHome getInstitutionByInstitutionId(int institutionId) {
		return homeDAO.getInstitutionByInstitutionId(institutionId);
	}
	@Override
	public List<InstitutionHomeDiscover> getInstitutionsByLastest(int offset, int size) {
		List<InstitutionHome> homes=homeDAO.getInstitutionsByLastest(offset,size);
		if(CommonUtils.isNull(homes)){
			return null;
		}
		List<InstitutionHomeDiscover> result=new ArrayList<InstitutionHomeDiscover>();
		for(InstitutionHome home:homes){
			InstitutionHomeDiscover discover=new InstitutionHomeDiscover(home);
			discover.setMemberCount(peopleService.getMembersSize(home.getInstitutionId()));
			discover.setActivityCount(ises.getCount(home.getInstitutionId()));
			result.add(discover);
		}
		return result; 
	}
	@Override
	public List<InstitutionHomeDiscover> getInstitutionsByMemberCount(int offset, int size) {
		List<InstitutionHomeDiscover> discovers=homeDAO.getInstitutionsByMemberCount(offset, size);
		if(CommonUtils.isNull(discovers)){
			return null;
		}
		for(InstitutionHomeDiscover discover:discovers){
			discover.setActivityCount(ises.getCount(discover.getInstitutionId()));
		}
		return discovers;
	}
	@Override
	public List<InstitutionHomeDiscover> getInstitutionsByPaperCount(int offset, int size) {
		List<InstitutionHomeDiscover> discovers= homeDAO.getInstitutionsByPaperCount(offset,size);
		if(CommonUtils.isNull(discovers)){
			return null;
		}
		for(InstitutionHomeDiscover discover:discovers){
			discover.setActivityCount(ises.getCount(discover.getInstitutionId()));
			discover.setMemberCount(peopleService.getMembersSize(discover.getInstitutionId()));
		}
		return discovers;
	}
	@Override
	public List<InstitutionHomeDiscover> getInstitutionsByKeyword(String keyword,int offset,int size) {
		List<InstitutionHome> homes=homeDAO.getInstitutionsByKeyword(keyword,offset,size);
		if(CommonUtils.isNull(homes)){
			return null;
		}
		List<InstitutionHomeDiscover> result=new ArrayList<InstitutionHomeDiscover>();
		for(InstitutionHome home:homes){
			InstitutionHomeDiscover discover=new InstitutionHomeDiscover(home);
			discover.setMemberCount(peopleService.getMembersSize(home.getInstitutionId()));
			discover.setActivityCount(ises.getCount(home.getInstitutionId()));
			result.add(discover);
		}
		return result;
	}
	@Override
	public int getInstitutionsByKeywordCount(String keyword) {
		return homeDAO.getInstitutionsByKeywordCount(keyword);
	}
	@Override
	public boolean isValidHome(String domain) {
		return homeDAO.isValidHome(domain);
	}


	@Override
	public List<Institution> searchForInstitutionBySimilarName(String name) {
		return homeDAO.searchForInstitutionBySimilarName(name);
	}

	@Override
	public Institution getInstitutionById(int insId) {
		return institutionDAO.getInstitution(insId);
	}

	@Override
	public int createAliasInstitutionName(String name, int insId, boolean isFull) {
		return homeDAO.createAliasInstitutionName(name, insId, isFull);
	}


	@Override
	public void deleteAliasInstitutionName(String name, int insId) {
		homeDAO.deleteAliasInstitutionName(name,insId);
	}

	@Override
	public boolean checkTypeOfInstitutionName(String name, int insId,
			boolean officalOrCustom) {
		return homeDAO.checkTypeOfInstitutionName(name, insId, officalOrCustom);
	}
}
