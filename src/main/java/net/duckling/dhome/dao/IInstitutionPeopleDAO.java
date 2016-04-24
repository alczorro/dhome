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
package net.duckling.dhome.dao;

import java.util.List;

import net.duckling.dhome.domain.people.SimpleUser;

/**
 * 一个和user和institution的关联表
 * 
 * @author lvly
 * @since 2012-9-18
 */
public interface IInstitutionPeopleDAO {

	/**
	 * 创建关联关系，默认自今字段为false
	 * 
	 * @param userid
	 * @param institutionId
	 *            机构id
	 * @return generated id
	 * */
	int createInstitutionPeople(int userId, int institutionId);

	/**
	 * 判断是否有相同记录
	 * 
	 * @param userid
	 * @param institutionId
	 *            机构id
	 * @return isExist?
	 * */
	boolean isExists(int userId, int institutionId);

	/**
	 * 获得一个机构下的研究队伍
	 * 
	 * @param size
	 *            数量
	 * @param offset
	 *            偏移量
	 * @param institution
	 *            机构
	 * @return 获得一个机构主页下的所有研究队伍
	 * */
	List<SimpleUser> getPeoplesByInstitituionId(int institutionId, int offset, int size);

	/**
	 * 获取当前有效的机构ID，只有拥有研究人员的机构才是有效的机构
	 * 
	 * @return
	 */
	List<Integer> getInstitutionHasPerson();

	/**
	 * 获得一个机构下的所有研究队伍数量
	 * 
	 * @return int
	 */
	int getMembersSize(int institutionId);

	/**
	 * @param institutionId
	 * @param uid
	 */
	void deleteMember(int institutionId, int uid);

	/**
	 * 判断这个人是否是这个机构主页的人
	 * @param uid userId
	 * @param institutionId
	 * @return  isMember?
	 * */
	boolean isMember(int uid, int institutionId);

}
