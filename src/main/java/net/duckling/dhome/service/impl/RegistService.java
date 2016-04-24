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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.duckling.dhome.common.config.AppConfig;
import net.duckling.dhome.common.umt.UmtClient;
import net.duckling.dhome.common.util.DateUtils;
import net.duckling.dhome.common.util.UrlUtil;
import net.duckling.dhome.dao.IDetailedUserDAO;
import net.duckling.dhome.dao.IDisciplineDAO;
import net.duckling.dhome.dao.IEducationDAO;
import net.duckling.dhome.dao.IHomeDAO;
import net.duckling.dhome.dao.IInstitutionDAO;
import net.duckling.dhome.dao.ISimpleUserDAO;
import net.duckling.dhome.dao.IWorkDAO;
import net.duckling.dhome.domain.institution.Institution;
import net.duckling.dhome.domain.people.DetailedUser;
import net.duckling.dhome.domain.people.Discipline;
import net.duckling.dhome.domain.people.Education;
import net.duckling.dhome.domain.people.Home;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.Work;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IRegistService;
import net.duckling.falcon.api.cache.ICacheService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 注册模块服务层实现类
 * 
 * @author lvly
 * @since 2012-08-06
 * */
@Service
public class RegistService implements IRegistService {
	@Autowired
	private ISimpleUserDAO simpleUserDAO;
	@Autowired
	private IDetailedUserDAO detailedUserDAO;
	@Autowired
	private IEducationDAO educationDAO;
	@Autowired
	private IWorkDAO workDAO;
	@Autowired
	private IInstitutionDAO institutionDAO;
	@Autowired
	private AppConfig config;
	@Autowired
	private	IHomeDAO homeDAO;
	@Autowired
	private IDisciplineDAO disciplineDAO;
	@Autowired
	private ICacheService cacheService;
	@Autowired
	private IInstitutionHomeService institutionHomeService;
	
	private UmtClient umt;
	/** 此处的key定义和userService一致**/
	private static final String MEMKEY_SIMPLEUSER = "dhome.uid-user.simpleuser";
	private static final String MEMKEY_DETAILEDUSER = "dhome.uid-user.detaileduser";
	
	@PostConstruct
	public void init(){
		umt=new UmtClient(config.getUmtServerURL());
	}
	@PreDestroy
	public void destroy(){
		umt=null;
	}
	
	@Override
	public int createSimpleUser(SimpleUser user,String password, boolean umtExist) {
		boolean flag = true;
		user.setEmail(user.getEmail().toLowerCase());
		if(!umtExist){
			flag=umt.createAccount(user.getEmail(), user.getZhName(),password );
		}
		if(flag){
			int uid = simpleUserDAO.registAccount(user);
			if(uid > 0){
				cacheService.set(MEMKEY_SIMPLEUSER+"."+uid, simpleUserDAO.getUser(uid));
			}
			return uid;
		}
		return -1;
	}

	@Override
	public boolean hasCreateHomePage(String email) {
		SimpleUser su=simpleUserDAO.getUser(email);
		return su!=null;
	}

	@Override
	public boolean isEmailUsed(String email) {
		return simpleUserDAO.isEmailUsed(email);
	}
	@Override
	public int createDetailedUser(DetailedUser user) {
		int id = detailedUserDAO.createDetailedUser(user);
		user.setId(id);
		cacheService.set(MEMKEY_DETAILEDUSER+"."+user.getUid(), user);
		return id;
	}
	@Override
	public boolean isDomainUsed(String domain) {
		if(!UrlUtil.canUse(domain)){
			return true;
		}
		return homeDAO.isDomainUsed(domain);
	}

	@Override
	public int createEducation(int uid, String degree, String department, String institution) {
		Education edu=new Education(uid,degree,department);
		edu.setEndTime(DateUtils.getMaxDate());
		Institution ins=institutionDAO.getInstitutionByName(institution);
		if(null!=ins && ins.getId()>0){
			institutionHomeService.createInstitutionHome(uid, ins);
			edu.setInsitutionId(ins.getId());
		}else{
			//此处逻辑修改，按照用户输入的机构名查找对应的官方机构，
			edu.setInsitutionId(institutionDAO.searchOfficalInstitution(institution));
		}
		edu.setAliasInstitutionName(institution);
		return educationDAO.createEducation(edu);
	}
	@Override
	public int createWork(int uid, String position, String department, String institution) {
		Work work=new Work(uid,position,department);
		work.setEndTime(DateUtils.getMaxDate());
		Institution ins=institutionDAO.getInstitutionByName(institution);
		if(null!=ins && ins.getId()>0){
			institutionHomeService.createInstitutionHome(uid, ins);
			work.setInstitutionId(ins.getId());
		}else{
			//此处逻辑修改，按照用户输入的机构名查找对应的官方机构，
			work.setInstitutionId(institutionDAO.searchOfficalInstitution(institution));
		}
		work.setAliasInstitutionName(institution);
		return workDAO.createWork(work);
	}
	@Override
	public void updateSimpleUser(SimpleUser user) {
		simpleUserDAO.updateAccount(user);
		cacheService.set(MEMKEY_SIMPLEUSER+"."+user.getId(), user);
	}
	@Override
	public int createHome(Home home) {
		return homeDAO.createHome(home);
	}
	@Override
	public List<Discipline> getRootDiscipline() {
		return disciplineDAO.getRoot();
	}
	@Override
	public List<Discipline> getChildDiscipline(int id) {
		return disciplineDAO.getChild(id);
	}
}
