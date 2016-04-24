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
import java.util.List;

import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.PinyinUtil;
import net.duckling.dhome.dao.IHomeDAO;
import net.duckling.dhome.dao.IInstitutionDepartDAO;
import net.duckling.dhome.dao.IInstitutionMemberFromVmtDAO;
import net.duckling.dhome.dao.IInstitutionVmtDAO;
import net.duckling.dhome.dao.ISimpleUserDAO;
import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.dhome.domain.institution.InstitutionMemberFromVmt;
import net.duckling.dhome.domain.institution.InstitutionVmtInfo;
import net.duckling.dhome.domain.people.Home;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.Theme;
import net.duckling.dhome.service.IInstitutionVmtService;
import net.duckling.vmt.api.domain.TreeNode;
import net.duckling.vmt.api.domain.VmtDepart;
import net.duckling.vmt.api.domain.VmtGroup;
import net.duckling.vmt.api.domain.VmtOrg;
import net.duckling.vmt.api.domain.VmtUser;
import net.duckling.vmt.api.domain.message.MQCreateDepartMessage;
import net.duckling.vmt.api.domain.message.MQDeleteDepartMessage;
import net.duckling.vmt.api.domain.message.MQLinkUserMessage;
import net.duckling.vmt.api.domain.message.MQMoveDepartMessage;
import net.duckling.vmt.api.domain.message.MQMoveUserMessage;
import net.duckling.vmt.api.domain.message.MQUnlinkUserMessage;
import net.duckling.vmt.api.domain.message.MQUpdateDepartMessage;
import net.duckling.vmt.api.domain.message.MQUpdateGroupMessage;
import net.duckling.vmt.api.domain.message.MQUpdateOrgMessage;
import net.duckling.vmt.api.domain.message.MQUpdateUserMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstitutionVmtServiceImpl implements IInstitutionVmtService{
	private static final Logger LOG=Logger.getLogger(InstitutionVmtServiceImpl.class);
	@Autowired
	private IInstitutionVmtDAO vmtDAO;
	@Autowired
	private IInstitutionDepartDAO departDAO;
	
	@Autowired
	private ISimpleUserDAO simpleUserDAO;
	
	@Autowired
	private IHomeDAO homeDAO;
	
	@Autowired
	private IInstitutionMemberFromVmtDAO fromVmtDAO;
	@Override
	public void updateVmtGroup(MQUpdateGroupMessage message) {
		String teamDN=message.getGroup().getDn();
		InstitutionVmtInfo vmtInfo=vmtDAO.getVmtInfoByDN(teamDN);
		if(vmtInfo==null){
			LOG.info("update.group:can't find the vmt_ref_info that ["+teamDN+"]");
			return;
		}
		fromVmtDAO.resetIsAdmin(vmtInfo.getInstitutionId());
		fromVmtDAO.setAdmins(vmtInfo.getInstitutionId(),message.getAdmins());
		InstitutionVmtInfo needUpdate=new InstitutionVmtInfo();
		needUpdate.setName(message.getGroup().getName());
		vmtDAO.update(vmtInfo.getId(),needUpdate);
	}
	@Override
	public void updateVmtOrg(MQUpdateOrgMessage message) {
		String teamDN=message.getOrg().getDn();
		InstitutionVmtInfo vmtInfo=vmtDAO.getVmtInfoByDN(teamDN);
		if(vmtInfo==null){
			LOG.info("update.org:can't find the vmt_ref_info that ["+teamDN+"]");
			return;
		}
		fromVmtDAO.resetIsAdmin(vmtInfo.getInstitutionId());
		fromVmtDAO.setAdmins(vmtInfo.getInstitutionId(),message.getAdmins());
		InstitutionVmtInfo needUpdate=new InstitutionVmtInfo();
		needUpdate.setName(message.getOrg().getName());
		vmtDAO.update(vmtInfo.getId(),needUpdate);
	}
	private boolean isAdmin(String[] admins,String umtId){
		if(CommonUtils.isNull(admins)){
			return false;
		}
		for(String admin:admins){
			if(admin.equals(umtId)){
				return true;
			}
		}
		return false;
	}
	
	private void recursion(TreeNode node,InstitutionVmtInfo vmtInfo,String groupOrOrg,InstitutionDepartment parent,String[] admins){
		List<TreeNode> nodes=node.getChildren();
		if(CommonUtils.isNull(nodes)){
			return;
		}
		List<InstitutionMemberFromVmt> members=new ArrayList<InstitutionMemberFromVmt>();
		for(TreeNode tn:nodes){
			if(tn.getData() instanceof VmtDepart){
				VmtDepart dept=(VmtDepart)tn.getData();
				InstitutionDepartment inDe=new InstitutionDepartment();
				inDe.setInstitutionId(vmtInfo.getInstitutionId());
				inDe.setName(dept.getName());
				inDe.setListRank(dept.getListRank());
				inDe.setDisplay(dept.isVisible()?InstitutionDepartment.DISPLAY_SHOW:InstitutionDepartment.DISPLAY_HIDE);
				if(parent!=null){
					inDe.setParentId(parent.getId());
				}
				inDe.setSymbol(dept.getSymbol());
				departDAO.insertDepart(inDe);
				recursion(tn, vmtInfo, groupOrOrg, inDe,admins);
			}else if(tn.getData() instanceof VmtUser){
				VmtUser vmtUser=(VmtUser)tn.getData();
				InstitutionMemberFromVmt fromVmt= assembleMemberFromVmt(vmtUser, parent, vmtInfo);
				fromVmt.setUserType(isAdmin(admins, vmtUser.getUmtId())?InstitutionMemberFromVmt.USER_TYPE_ADMIN:InstitutionMemberFromVmt.USER_TYPE_MEMBER);
				members.add(fromVmt);
			}
		}
		addVmtMembers(members);
	}
	
	/**
	 * 如果sn（arp编号）不为空，则通过sn关联用户，并更新cstnet_id邮箱（考虑之前没有邮箱的用户）
	 * @param vmtMembers
	 */
	private void addVmtMembers(List<InstitutionMemberFromVmt> vmtMembers){
		List<InstitutionMemberFromVmt> insertList = new ArrayList<InstitutionMemberFromVmt>();
		List<InstitutionMemberFromVmt> updateList = new ArrayList<InstitutionMemberFromVmt>();
		for(InstitutionMemberFromVmt item : vmtMembers){
			if(item.getSn()!=null){
				InstitutionMemberFromVmt member = fromVmtDAO.getMemberBySn(item.getInstitutionId(), item.getSn());
				if(member!=null && member.getCstnetId()==null){
					updateList.add(item);
				}else{
					insertList.add(item);
				}
			}else{
				insertList.add(item);
			}
		}
		if(insertList.size()>0){
			fromVmtDAO.insert(insertList);
		}
		if(updateList.size()>0){
			fromVmtDAO.updateCstnetId(updateList);
		}
	}
	
	private SimpleUser createUser(InstitutionMemberFromVmt fromVmt){
		SimpleUser u=new SimpleUser();
		u.setEmail(fromVmt.getCstnetId());
		u.setEnName(PinyinUtil.getPinyinMingXing(fromVmt.getTrueName()));
		u.setSalutation(fromVmt.getTitle());
		u.setStatus(SimpleUser.STATUS_AUDIT_OK);
		u.setStep(null);
		u.setPinyin(PinyinUtil.getPinyin(fromVmt.getTrueName()));
		u.setZhName(fromVmt.getTrueName());
		fromVmt.setUid(simpleUserDAO.registAccount(u));
		u.setId(fromVmt.getUid());
		return u;
	}
	private Home createHome(SimpleUser u,String domain){
		if(homeDAO.isDomainUsed(domain)){
			return createHome(u, domain+"1");
		}
		Home home=new Home();
		home.setLanguage(Home.LANGUAGE_ZH);
		home.setThemeid(Theme.DEFAULT_THEME);
		home.setUrl(domain);
		home.setUid(u.getId());
		homeDAO.createHome(home);
		return home;
	}
	
	public void generateUID(InstitutionMemberFromVmt fromVmt){
		SimpleUser u=simpleUserDAO.getUser(fromVmt.getCstnetId());
		if(u!=null){
			fromVmt.setUid(u.getId());
		}else{
			//用户创建用户
			u=createUser(fromVmt);
			
			//createHome(u,PinyinUtil.getShortPinyin(fromVmt.getTrueName()));
		}
	}
	
	@Override
	public InstitutionVmtInfo getVmtInfoByInstitutionId(int insId){
		return vmtDAO.getVmtInfoByInstitutionId(insId);
	}
	@Override
	public boolean syncOrg(TreeNode node,InstitutionVmtInfo vmtInfo) {
		if(CommonUtils.isNull(node.getChildren())){
			return false;
		}
		departDAO.removeByInstitutionId(vmtInfo.getInstitutionId());
		fromVmtDAO.removeAllMemberByInsId(vmtInfo.getInstitutionId());
		recursion(node, vmtInfo,"org",null,((VmtOrg)node.getData()).getAdmins());
		return true;
	}
	@Override
	public boolean syncGroup(TreeNode node, InstitutionVmtInfo vmtInfo) {
		if(CommonUtils.isNull(node.getChildren())){
			return false;
		}
		departDAO.removeByInstitutionId(vmtInfo.getInstitutionId());
		fromVmtDAO.removeAllMemberByInsId(vmtInfo.getInstitutionId());
		recursion(node, vmtInfo, "group", null,((VmtGroup)node.getData()).getAdmins());
		return true;
	}
	@Override
	public void addVmtUser(MQLinkUserMessage message) {
		String dn=message.getOrg().getDn();
		if(CommonUtils.isNull(dn)){
			dn=message.getGroup().getDn();
		}
		if(CommonUtils.isNull(dn)){
			LOG.error("can't find team dn!");
			return;
		}
		InstitutionVmtInfo vmtInfo=vmtDAO.getVmtInfoByDN(dn);
		if(vmtInfo==null){
			LOG.info("can't find the vmt_ref_info that ["+dn+"]");
			return;
		}
		List<VmtUser> vmtUsers=message.getUsers();
		if(CommonUtils.isNull(vmtUsers)){
			LOG.info("can't find users!");
			return;
		}
		boolean isAddToDepartment=message.getDept()!=null;
		InstitutionDepartment dept=null;
		//添加到部门
		if(isAddToDepartment){
			VmtDepart vmtDept=message.getDept();
			dept=departDAO.getDepartBySymbol(vmtInfo.getInstitutionId(),vmtDept.getSymbol());
			
		}
		List<InstitutionMemberFromVmt> vmtMembers=new ArrayList<InstitutionMemberFromVmt>();
		for(VmtUser u:vmtUsers){
			InstitutionMemberFromVmt vmtMember= assembleMemberFromVmt(u, dept, vmtInfo);
			vmtMembers.add(vmtMember);
		}
		addVmtMembers(vmtMembers);
	}
	@Override
	public void removeVmtUser(MQUnlinkUserMessage message) {
		String dn=message.getOrg().getDn();
		if(CommonUtils.isNull(dn)){
			dn=message.getGroup().getDn();
		}
		if(CommonUtils.isNull(dn)){
			LOG.error("can't find team dn!");
			return;
		}
		InstitutionVmtInfo vmtInfo=vmtDAO.getVmtInfoByDN(dn);
		if(vmtInfo==null){
			LOG.info("can't find the vmt_ref_info that ["+dn+"]");
			return;
		}
		List<VmtUser> vmtUsers=message.getUsers();
		if(CommonUtils.isNull(vmtUsers)){
			LOG.info("the need remove user list is empty!");
			return;
		}
		List<String> userUmtId=new ArrayList<String>();
		for(VmtUser u:vmtUsers){
			userUmtId.add(u.getUmtId());
		}
		
		fromVmtDAO.removeMembersByUmtIds(vmtInfo.getInstitutionId(),userUmtId);
	}
	@Override
	public void moveVmtUser(MQMoveUserMessage message) {
		String orgDn=message.getOrg().getDn();
		
		InstitutionVmtInfo vmtInfo=vmtDAO.getVmtInfoByDN(orgDn);
		if(vmtInfo==null){
			LOG.info("can't find the vmt_ref_info that ["+orgDn+"]");
			return;
		}
		boolean isMoveToRoot=message.getDept()==null;
		InstitutionDepartment vmtDept=null;
		if(!isMoveToRoot){
			String deptDn=message.getDept().getDn();
			vmtDept=departDAO.getDepartBySymbol(vmtInfo.getInstitutionId(), message.getDept().getSymbol());
			if(vmtDept==null){
				LOG.info("can't find the vmt_dept that ["+deptDn+"]");
				return;
			}
		}
		List<String> userUmtIds=new ArrayList<String>();
		for(VmtUser u:message.getUser()){
			userUmtIds.add(u.getUmtId());
		}
		fromVmtDAO.moveTo(vmtInfo.getInstitutionId(),userUmtIds,isMoveToRoot?0:vmtDept.getId());
	}
	@Override
	public void updateVmtUser(MQUpdateUserMessage message) {
		VmtUser user=message.getUser();
		if(user==null){
			LOG.error("user list is empty!");
			return;
		}
		String teamDN=getTeamDN(user.getDn());
		InstitutionVmtInfo vmtInfo=vmtDAO.getVmtInfoByDN(teamDN);
		if(vmtInfo==null){
			LOG.info("can't not find institution["+teamDN+"] ");
			return;
		}
		List<VmtUser> users=new ArrayList<VmtUser>();
		users.add(user);
		fromVmtDAO.updateVmtUser(vmtInfo.getInstitutionId(),users);
	}
	@Override
	public void updateVmtDepart(MQUpdateDepartMessage message) {
		VmtDepart dept=message.getDept();
		String teamDN=getTeamDN(dept.getDn());
		InstitutionVmtInfo vmtInfo=vmtDAO.getVmtInfoByDN(teamDN);
		if(vmtInfo==null){
			LOG.info("can't find the vmt_ref_info that ["+teamDN+"]");
			return;
		}
		InstitutionDepartment localDept=departDAO.getDepartBySymbol(vmtInfo.getInstitutionId(), message.getDept().getSymbol());
		if(localDept==null){
			LOG.info("can't find the dept that ["+message.getDept().getSymbol()+"]");
			return;
		}
		List<Integer> sonDeptId=new ArrayList<Integer>();
		remember(sonDeptId, localDept.getId());
		fromVmtDAO.displayChanged(vmtInfo.getInstitutionId(),sonDeptId,message.getDept().isVisible()?InstitutionDepartment.DISPLAY_SHOW:InstitutionDepartment.DISPLAY_HIDE);
		departDAO.updateDept(dept,vmtInfo.getInstitutionId());
		departDAO.displayChanged(vmtInfo.getInstitutionId(),sonDeptId,message.getDept().isVisible()?InstitutionDepartment.DISPLAY_SHOW:InstitutionDepartment.DISPLAY_HIDE);
	}
	private static String getTeamDN(String DN){
		String[] dns=DN.split(",");
		return dns[dns.length-2]+","+dns[dns.length-1];
	}
	private static String getLastSymbol(String DN){
		String[] dns=DN.split(",");
		return dns[0].split("=")[1];
	}
	@Override
	public void moveVmtDepart(MQMoveDepartMessage message) {
		String teamDN=getTeamDN(message.getDept().getDn());
		InstitutionVmtInfo vmtInfo=vmtDAO.getVmtInfoByDN(teamDN);
		if(vmtInfo==null){
			LOG.info("can't find the vmt_ref_info that ["+teamDN+"]");
			return;
		}
		boolean isMoveToRoot=message.getTargetDept()==null;
		boolean isContainSelf=message.isContainSelf();
		InstitutionDepartment dept=departDAO.getDepartBySymbol(vmtInfo.getInstitutionId(),message.getDept().getSymbol());
		if(dept==null){
			LOG.info("dept is not found["+message.getDept().getSymbol()+"]");
			return;
		}
		InstitutionDepartment targetDept=null;
		if(!isMoveToRoot){
			targetDept=departDAO.getDepartBySymbol(vmtInfo.getInstitutionId(),message.getTargetDept().getSymbol());
			if(targetDept==null){
				LOG.info("target dept is not fouded!");
				return;
			}
		}
		//包含自己
		if(isContainSelf){
			departDAO.moveDepart(dept.getId(),isMoveToRoot?0:targetDept.getId());
		}
		//只移动子节点,则需要删除空壳子，不知道为什么
		else{
			departDAO.parentChanged(dept.getId(),isMoveToRoot?0:targetDept.getId());
			fromVmtDAO.parentChanged(dept.getId(),isMoveToRoot?0:targetDept.getId());
			departDAO.removeDepartByIds(dept.getId());
		}
	}
	@Override
	public void removeVmtDepart(MQDeleteDepartMessage message) {
		String teamDN=getTeamDN(message.getDept().getDn());
		InstitutionVmtInfo vmtInfo=vmtDAO.getVmtInfoByDN(teamDN);
		if(vmtInfo==null){
			LOG.info("can't find the vmt_ref_info that ["+teamDN+"]");
			return;
		}
		InstitutionDepartment dept=departDAO.getDepartBySymbol(vmtInfo.getInstitutionId(),message.getDept().getSymbol());
		if(dept==null){
			LOG.info("can't find the depart dn is ["+message.getDept().getDn()+"]");
			return;
		}
		List<Integer> deptIds=new ArrayList<Integer>();
		remember(deptIds,dept.getId());
		departDAO.removeDepartByIds(deptIds);
		fromVmtDAO.removeMembersByDeptIds(deptIds);
	}
	
	private void remember(List<Integer> deptIds,int currentDeptId){
		deptIds.add(currentDeptId);
		List<InstitutionDepartment> sons=departDAO.getDepartsByParentIds(currentDeptId);
		if(CommonUtils.isNull(sons)){
			return;
		}
		for(InstitutionDepartment dept:sons){
			remember(deptIds,dept.getId());
		}
	}
	
	@Override
	public void createVmtDepart(MQCreateDepartMessage message) {
		String teamDN=getTeamDN(message.getDept().getDn());
		InstitutionVmtInfo vmtInfo=vmtDAO.getVmtInfoByDN(teamDN);
		if(vmtInfo==null){
			LOG.info("can't find the vmt_ref_info that ["+teamDN+"]");
			return;
		}
		boolean isRoot=teamDN.equals(message.getParentDn());
		InstitutionDepartment vmtDept=new InstitutionDepartment();
		vmtDept.setInstitutionId(vmtInfo.getInstitutionId());
		vmtDept.setName(message.getDept().getName());
		vmtDept.setSymbol(message.getDept().getSymbol());
		if(!isRoot){
			InstitutionDepartment targetDept=departDAO.getDepartBySymbol(vmtInfo.getInstitutionId(),getLastSymbol(message.getParentDn()));
			if(targetDept==null){
				LOG.error("can't find the target Dept["+message.getParentDn()+"]");
				return;
			}
			vmtDept.setParentId(targetDept.getId());
		}
		departDAO.insertDepart(vmtDept);
	}
	
	private InstitutionMemberFromVmt assembleMemberFromVmt(VmtUser vmtUser, InstitutionDepartment dept, InstitutionVmtInfo vmtInfo){
		InstitutionMemberFromVmt fromVmt=new InstitutionMemberFromVmt();
		fromVmt.setCstnetId(vmtUser.getCstnetId().toLowerCase());
		fromVmt.setInstitutionId(vmtInfo.getInstitutionId());
		fromVmt.setTrueName(vmtUser.getName());
		fromVmt.setUmtId(vmtUser.getUmtId());
		fromVmt.setSex(vmtUser.getSex());
		fromVmt.setOfficeAddress(vmtUser.getOffice());
		fromVmt.setOfficeTelephone(vmtUser.getOfficePhone());
		fromVmt.setTitle(vmtUser.getTitle());
		fromVmt.setMobilePhone(vmtUser.getTelephone());
		fromVmt.setDisplay(vmtUser.isVisible()?InstitutionMemberFromVmt.DISPLAY_SHOW:InstitutionMemberFromVmt.DISPLAY_HIDE);
		fromVmt.setSn(vmtUser.getCustom1());
		if(dept!=null){
			fromVmt.setDepartId(dept.getId());
		}
		generateUID(fromVmt);
		return fromVmt;
	}
	
	@Override
	public Integer getInstituionId(int userId) {
		return fromVmtDAO.getInstituionId(userId);
	}
	@Override
	public void updateMember(List<InstitutionMemberFromVmt> member) {
		fromVmtDAO.updateMember(member);
	}
	@Override
	public void updateMemberStatus(List<InstitutionMemberFromVmt> member) {
		fromVmtDAO.updateMemberStatus(member);
	}
	@Override
	public void insertMembers(List<InstitutionMemberFromVmt> member) {
		fromVmtDAO.insertMembers(member);
	}
	
	@Override
	public void updateUserId(final String cstnetId, final int userId){
		fromVmtDAO.updateUserId(cstnetId, userId);
	}
	
	@Override
	public boolean isMember(int insId,String email) {
		return fromVmtDAO.isMember(insId,email);
	}
	
	@Override
	public boolean isMember(String email) {
		return fromVmtDAO.isMember(email);
	}
	
	@Override
	public List<InstitutionMemberFromVmt> searchMember(String key) {
		if(CommonUtils.isNull(key)){
			return null;
		}
		return fromVmtDAO.searchMember(key);
	}
	@Override
	public List<InstitutionMemberFromVmt> getAllUser() {
		return fromVmtDAO.getAllUser();
	}
	@Override
	public InstitutionMemberFromVmt getMemberByCstnetId(int institutionId,
			String cstnetId) {
		return fromVmtDAO.getMemberByCstnetId(institutionId, cstnetId);
	}
	@Override
	public InstitutionMemberFromVmt getVmtMemberByUmtId(int institutionId,
			String umtId) {
		return fromVmtDAO.getVmtMemberByUmtId(institutionId, umtId);
	}
	
}
