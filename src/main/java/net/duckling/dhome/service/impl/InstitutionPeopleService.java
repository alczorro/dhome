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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.dao.IEducationDAO;
import net.duckling.dhome.dao.IInstitutionPeopleDAO;
import net.duckling.dhome.dao.IWorkDAO;
import net.duckling.dhome.domain.institution.Member;
import net.duckling.dhome.domain.people.Interest;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionPeopleService;
import net.duckling.dhome.service.IInterestService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2012-9-18
 */
@Service
public class InstitutionPeopleService implements IInstitutionPeopleService {
	private static final Logger LOG = Logger.getLogger(InstitutionPeopleService.class);
	@Autowired
	private IInstitutionPeopleDAO peopleDAO;
	@Autowired
	private IHomeService homeService;
	@Autowired
	private IEducationDAO eduDAO;
	@Autowired
	private IWorkDAO workDAO;
	@Autowired
	private IInterestService interestService;
	
	@Override
	public void addMember(int uid, int institutionId) {
		if(!peopleDAO.isExists(uid, institutionId)){
			peopleDAO.createInstitutionPeople(uid, institutionId);
		}
	}
	@Override
	public List<Member> getPeoplesByInstitutionId(int institutionId, int offset, int size) {
		List<SimpleUser> users = peopleDAO.getPeoplesByInstitituionId(institutionId, offset, size);
		List<Member> members = new ArrayList<Member>();
		if (!CommonUtils.isNull(users)) {
			for (SimpleUser u : users) {
				Member member = new Member(u);
				member.setDomain(homeService.getDomain(u.getId()));
				member.setInterest(getEncodeInterest(interestService.getInterest(u.getId())));
				members.add(member);
			}
		}
		return members;
	}
	private List<Interest> getEncodeInterest(List<Interest> list){
		if(CommonUtils.isNull(list)){
			return null;
		}
		for(Interest ins:list){
			try {
				ins.setKeyword(URLEncoder.encode(URLEncoder.encode(ins.getKeyword(), "utf8"), "utf8"));
			} catch (UnsupportedEncodingException e) {
				LOG.error("keyword:"+ins.getKeyword(),e);
			}
		}
		return list;
	}

	@Override
	public int getMembersSize(int institutionId) {
		return peopleDAO.getMembersSize(institutionId);
	}
	@Override
	public void deleteMember(int uid, int institutionId) {
		int citedWorkTimes=workDAO.getCitedWorkCount(institutionId, uid);
		int citedEduTimes=eduDAO.getCitedEducationCount(institutionId, uid);
		if(citedWorkTimes+citedEduTimes==1){
			peopleDAO.deleteMember(institutionId,uid);
		}
		
	}
	@Override
	public boolean isMember(int uid, int institutionId) {
		if(uid<0){
			return false;
		}
		return peopleDAO.isMember(uid,institutionId);
	}
	
	//-------setter for test---------------
	
	public void setPeopleDAO(IInstitutionPeopleDAO peopleDAO) {
		this.peopleDAO = peopleDAO;
	}

	public void setHomeService(IHomeService homeService) {
		this.homeService = homeService;
	}

	public void setEduDAO(IEducationDAO eduDAO) {
		this.eduDAO = eduDAO;
	}

	public void setWorkDAO(IWorkDAO workDAO) {
		this.workDAO = workDAO;
	}

	public void setInterestService(IInterestService interestService) {
		this.interestService = interestService;
	}
	
}
