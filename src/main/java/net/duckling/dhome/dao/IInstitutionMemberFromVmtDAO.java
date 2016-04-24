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
package net.duckling.dhome.dao;

import java.util.List;
import java.util.Map;

import net.duckling.dhome.domain.institution.InstitutionMemberFromVmt;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.vmt.api.domain.VmtUser;

public interface IInstitutionMemberFromVmtDAO {
	void insert(List<InstitutionMemberFromVmt> vmtMember);
	void insert(InstitutionMemberFromVmt vmtMember);
	void removeAllMemberByInsId(int institutionId);
	void removeMembersByUmtIds(int institutionId,List<String> userDns);
	void moveTo(int institutionId, List<String> userUmtIds,int i);
	void parentChanged(int orgParentId, int newId);
	void removeMembersByDeptIds(List<Integer> deptIds);
	void updateVmtUser(int insId, List<VmtUser> users);
	void updateUserId(final String cstnetId, final int userId);
	void displayChanged(int institutionId, List<Integer> sonDeptId,String display);
	void resetIsAdmin(int institutionId);
	void setAdmins(int insId, List<VmtUser> admins);
	List<InstitutionMemberFromVmt> getVmtMember(int insId, int i, int pageSize,SearchInstitutionCondition condition);
	int getVmtMemberCount(int insId,SearchInstitutionCondition condition);
	boolean isAdmin(int insId, String email);
	boolean isMember(int insId,String email) ;
	boolean isMember(String email);
	InstitutionMemberFromVmt getVmtMemberByUmtId(int institutionId, String umtId);
	void deleteMember(int insId, String[] umtId);
	
	Integer getInstituionId(int userId);
	void updateBaseMember(InstitutionMemberFromVmt member);
	/**
	 * 机构论文统计
	 * @param insId
	 * @return
	 */
	Map<String, Integer> getTitlesMap(int insId,int dept); //职称
	Map<String, Integer> getDegreesMap(int insId,int dept); //学历
	Map<String, Integer> getAgesMap(int insId,int dept);  //年龄
	
	
	void updateMember(List<InstitutionMemberFromVmt> member);
	void insertMembers(List<InstitutionMemberFromVmt> member);
	List<InstitutionMemberFromVmt> searchMember(String keyword);
	List<InstitutionMemberFromVmt> getAllUser();
	InstitutionMemberFromVmt getMemberByCstnetId(int institutionId,String cstnetId);
	void updateMemberStatus(List<InstitutionMemberFromVmt> member);
	
	/**
	 * 更新用户cstnetId
	 * @param member
	 */
	public void updateCstnetId(final List<InstitutionMemberFromVmt> members);
	
	/**
	 * 通过arp编码获取
	 * @param institutionId
	 * @param sn arp编码
	 * @return
	 */
	InstitutionMemberFromVmt getMemberBySn(int institutionId,String sn);
	/**
	 * 根据部门查询数量
	 * @param member
	 */
	int getMemberCount(int ins,int departId,int year);
	InstitutionMemberFromVmt getMemberByUid(int ins,int uid);
}
