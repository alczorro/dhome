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
package net.duckling.dhome.service;

import java.util.List;

import net.duckling.dhome.domain.institution.Member;

/**
 * 研究队伍服务层
 * @author lvly
 * @since 2012-9-18
 */
public interface IInstitutionPeopleService {
	/**
	 * 增加机构的研究人员
	 * @param uid 用户ID
	 * @param institutionId 机构ID
	 * @return
	 */
	void addMember(int uid, int institutionId);
	/**
	 * 获得一个机构下的研究队伍
	 * @param size 数量
	 * @param offset 偏移量
	 * @param institution 机构
	 * @return 获得一个机构主页下的所有研究队伍
	 * */
	List<Member> getPeoplesByInstitutionId(int institutionId, int offset, int size);

	/**
	 * 获得该机构下的所有用户数量
	 * @param institutionId 机构id
	 * @return int
	 */
	int getMembersSize(int institutionId);

	/**
	 * 删除研究队伍里得某一个人
	 * @param uid User Id
	 * @param institutionId Institution Id
	 */
	void deleteMember(int uid, int institutionId);
	/**
	 * 判断这个人是否是这个机构主页的人
	 * @param uid userId
	 * @param institutionId
	 * @return  isMember?
	 * */
	boolean isMember(int uid,int institutionId);

}
