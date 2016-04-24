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
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.duckling.dhome.common.config.AppConfig;
import net.duckling.dhome.common.umt.UmtClient;
import net.duckling.dhome.dao.IInstitutionAcademicDAO;
import net.duckling.dhome.dao.IInstitutionBackendAwardDAO;
import net.duckling.dhome.dao.IInstitutionBackendPaperDAO;
import net.duckling.dhome.dao.IInstitutionBackendTreatiseDAO;
import net.duckling.dhome.dao.IInstitutionCopyrightDAO;
import net.duckling.dhome.dao.IInstitutionDepartDAO;
import net.duckling.dhome.dao.IInstitutionMemberFromVmtDAO;
import net.duckling.dhome.dao.IInstitutionPatentDAO;
import net.duckling.dhome.dao.IInstitutionPeriodicalDAO;
import net.duckling.dhome.dao.IInstitutionTopicDAO;
import net.duckling.dhome.dao.IInstitutionTrainingDAO;
import net.duckling.dhome.dao.ISimpleUserDAO;
import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.dhome.domain.institution.InstitutionMemberFromVmt;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IInstitutionBackendService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 成员
 * @author liyanzhao
 *
 */
@Service
public class InstitutionBackendServiceImpl implements IInstitutionBackendService{
	@Autowired
	private IInstitutionMemberFromVmtDAO vmtMemberDAO;
	@Autowired
	private IInstitutionDepartDAO deptDAO;
	
	@Autowired
	private ISimpleUserDAO simpleUserDao;
	
	@Autowired
	private AppConfig config;
	@Autowired
	private IInstitutionBackendPaperDAO paperDAO;
	@Autowired
	private IInstitutionBackendTreatiseDAO treatiseDAO;
	@Autowired
	private IInstitutionBackendAwardDAO awardDAO;
	@Autowired
	private IInstitutionCopyrightDAO copyrightDAO;
	@Autowired
	private IInstitutionPatentDAO patentDAO;
	@Autowired
	private IInstitutionTopicDAO topicDAO;
	@Autowired
	private IInstitutionPeriodicalDAO periodicalDAO;
	@Autowired
	private IInstitutionAcademicDAO academicDAO;
	@Autowired
	private IInstitutionTrainingDAO trainingDAO;
	
	private UmtClient umt;
	
	/**
	 * 初始化
	 */
	@PostConstruct
	public void init(){
		umt=new UmtClient(config.getUmtServerURL());
	}
	/**
	 * 销毁
	 */
	@PreDestroy
	public void destroy(){
		umt=null;
	}
	
	@Override
	public PageResult<InstitutionMemberFromVmt> getVmtMember(int insId,
			int page,SearchInstitutionCondition condition) {
		PageResult<InstitutionMemberFromVmt> result=new PageResult<InstitutionMemberFromVmt>();
		if(condition.getPaging()!=0){
			result.setPageSize(condition.getPaging());
		}
		result.setAllSize(vmtMemberDAO.getVmtMemberCount(insId,condition));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(vmtMemberDAO.getVmtMember(insId,(page-1)*result.getPageSize(),result.getPageSize(),condition));
		result.setCurrentPage(page);
		return result;
	}
	@Override
	public int getVmtMemberCount(int institutionId) {
		return vmtMemberDAO.getVmtMemberCount(institutionId,new SearchInstitutionCondition());
	}
	@Override
	public void deleteMember(int insId, String[] umtId){
		vmtMemberDAO.deleteMember(insId, umtId);
	}
	@Override
	public boolean isAdmin(int insId, String email) {
		return vmtMemberDAO.isAdmin(insId,email);
	}
	@Override
	public boolean isMember(int insId,String email) {
		return vmtMemberDAO.isMember(insId,email);
	}
	@Override
	public List<InstitutionDepartment> getVmtDepartment(List<Integer> deptId) {
		return deptDAO.getDepartsByIds(deptId);
	}
	
	@Override
	public InstitutionMemberFromVmt getVmtMemberByUmtId(int institutionId,
			String umtId) {
		return vmtMemberDAO.getVmtMemberByUmtId(institutionId,umtId);
	}
	
	@Override
	public InstitutionDepartment getVmtDepartmentById(int departId) {
		return deptDAO.getDepartById(departId);
	}
	
	@Override
	public Map<String, Integer> getTitlesMap(int insId,int deptId) {
		return vmtMemberDAO.getTitlesMap(insId,deptId);
	}
	
	@Override
	public List<InstitutionDepartment> getVmtDepartment(int insId) {
		return deptDAO.getDepartsByInsId(insId);
	}
	
	@Override
	public List<InstitutionDepartment> getVmtDepartmentOrderPinyin(int insId) {
		return deptDAO.getDepartsOrderPinyin(insId);
	}
	

	@Override
	public void batchUpdateDepartment(int insId,List<InstitutionDepartment> depts){
		deptDAO.batchUpdate(insId, depts);
	}
	
	@Override
	public void insertMember(InstitutionMemberFromVmt member) {
//		SimpleUser user = new SimpleUser();
//		user.setZhName(member.getTrueName());
//		user.setEmail(member.getCstnetId());
//		user.setStatus(SimpleUser.STATUS_AUDIT_OK);
//		user.setIsAdmin(false);
//		int uid = simpleUserDao.registAccount(user);
//		
//		String password = getRandomString();
//		if(umt.isExistUMTRegister(member.getCstnetId())){
//			return false;
//		}else{
//			umt.createAccount(String.valueOf(uid), member.getCstnetId(), password);
//		}
	}
	
	@Override
	public void updateMember(InstitutionMemberFromVmt member) {
		
	}

	  public static String getRandomString() {    
	       StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyz");    
	       StringBuffer sb = new StringBuffer();    
	       Random r = new Random();    
	       int range = buffer.length();    
	       for (int i = 0; i < 6; i ++) {    
	           sb.append(buffer.charAt(r.nextInt(range)));    
	       }    
	       return sb.toString();    
	   }
	@Override
	public Map<String, Integer> getDegreesMap(int insId, int dept) {
		return vmtMemberDAO.getDegreesMap(insId, dept);
	}
	@Override
	public Map<String, Integer> getAgesMap(int insId, int dept) {
		return vmtMemberDAO.getAgesMap(insId, dept);
	}
	@Override
	public int getVmtMemberCount(int institutionId,
			SearchInstitutionCondition condition) {
		return vmtMemberDAO.getVmtMemberCount(institutionId, condition);
	}
	
	@Override
	public void updateBaseMember(InstitutionMemberFromVmt member) {
		vmtMemberDAO.updateBaseMember(member);
	}
	@Override
	public void updateDeptShortName(int id, String shortName,int listRank) {
		deptDAO.updateDeptShortName(id, shortName,listRank);
	}
	@Override
	public int getMemberCount(int insId,int departId, int year) {
		return vmtMemberDAO.getMemberCount(insId, departId, year);
	}
	@Override
	public int getPaperCount(int ins, int departId, int year) {
		return paperDAO.getPaperCount(ins, departId, year);
	}
	@Override
	public int getTreatiseCount(int ins, int departId, int year) {
		return treatiseDAO.getTreatiseCount(ins, departId, year);
	}
	@Override
	public int getAwardCount(int ins, int departId, int year) {
		return awardDAO.getAwardCount(ins, departId, year);
	}
	@Override
	public int getCopyrightCount(int ins, int departId, int year) {
		return copyrightDAO.getCopyrightCount(ins, departId, year);
	}
	@Override
	public int getPatentCount(int ins, int departId, int year) {
		return patentDAO.getPatentCount(ins, departId, year);
	}
	@Override
	public int getTopicCount(int ins, int departId, int year) {
		return topicDAO.getTopicCount(ins, departId, year);
	}
	@Override
	public int getAcademicCount(int ins, int departId, int year) {
		return academicDAO.getAcademicCount(ins, departId, year);
	}
	@Override
	public int getPeriodicalCount(int ins, int departId, int year) {
		return periodicalDAO.getPeriodicalCount(ins, departId, year);
	}
	@Override
	public int getTrainingCount(int ins, int departId, int year) {
		return trainingDAO.getTrainingCount(ins, departId, year);
	}    
}
