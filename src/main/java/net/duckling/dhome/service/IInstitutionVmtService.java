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

import net.duckling.dhome.domain.institution.InstitutionMemberFromVmt;
import net.duckling.dhome.domain.institution.InstitutionVmtInfo;
import net.duckling.vmt.api.domain.TreeNode;
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

public interface IInstitutionVmtService {
	boolean syncOrg(TreeNode node,InstitutionVmtInfo vmtInfo);
	boolean syncGroup(TreeNode node,InstitutionVmtInfo vmtInfo);
	InstitutionVmtInfo getVmtInfoByInstitutionId(int insId);
	/**
	 * 删除用户
	 * */
	void removeVmtUser(MQUnlinkUserMessage message);
	/**
	 * 添加新用户
	 * */
	void addVmtUser(MQLinkUserMessage message);
	/**
	 * 移动用户
	 * */
	void moveVmtUser(MQMoveUserMessage message);
	/**
	 * 更新用户
	 * */
	void updateVmtUser(MQUpdateUserMessage message);
	/**
	 * 更新部门
	 **/
	void updateVmtDepart(MQUpdateDepartMessage message);
	/**
	 * 移动部门
	 * */
	void moveVmtDepart(MQMoveDepartMessage message);
	
	/**
	 * 删除部门
	 * */
	void removeVmtDepart(MQDeleteDepartMessage message);
	
	/**
	 * 创建部门
	 * */
	void createVmtDepart(MQCreateDepartMessage message);
	void updateVmtGroup(MQUpdateGroupMessage message);
	void updateVmtOrg(MQUpdateOrgMessage message);
	
	
	/**
	 * 获取机构ID
	 * @param userId
	 * @return
	 */
	Integer getInstituionId(int userId);
	
	void updateMember(final List<InstitutionMemberFromVmt> member);
	void insertMembers(final List<InstitutionMemberFromVmt> member);
	void updateMemberStatus(final List<InstitutionMemberFromVmt> member);
	
	/**
	 * 更新用户ID和机构关联
	 * @param cstnetId
	 * @param userId
	 */
	void updateUserId(final String cstnetId, final int userId);
	
	/**
	 * 判断机构是否存在该用户
	 * @param email
	 * @return
	 */
	boolean isMember(int insId,String email);
	boolean isMember(String email);
	List<InstitutionMemberFromVmt> searchMember(String key);
	 List<InstitutionMemberFromVmt> getAllUser();
	 InstitutionMemberFromVmt getMemberByCstnetId(int institutionId,String cstnetId);
	 InstitutionMemberFromVmt getVmtMemberByUmtId(int institutionId,String umtId);

}
