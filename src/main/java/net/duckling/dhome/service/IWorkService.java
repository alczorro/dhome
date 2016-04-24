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

import net.duckling.dhome.domain.institution.Institution;
import net.duckling.dhome.domain.people.Work;

/**
 * 工作经历服务层
 * 
 * @author lvly
 * @since 2012-08-30
 * */
public interface IWorkService {
	/**
	 * 根据用户名，取得工作经历
	 * 
	 * @param uid
	 *            UserId
	 * @return 返回所有匹配的工作经历
	 * */
	List<Work> getWorksByUID(int uid);

	/**
	 * 根据 work ID 获得工作经历
	 * 
	 * @param id
	 * @return
	 */
	Work getWork(int id);

	/**
	 * 查询指定名称的单位
	 * 
	 * @return
	 */
	Institution getInstitutionByName(String name);

	/**
	 * 创建Institution对象
	 * 
	 * @param name
	 * @return
	 */
	int createInstitution(String name);

	/**
	 * 更新Work对象
	 * 
	 * @param work
	 * @return
	 */
	boolean updateWorkById(Work work);

	/**
	 * 创建Work对象
	 * 
	 * @param work
	 * @return
	 */
	int createWork(Work work);

	/**
	 * 删除Work对象
	 * 
	 * @param id
	 */
	void delteWork(int id);

	/**
	 * 给定单位名前缀查询符合前缀的官方的（DSN）单位列表
	 * 
	 * @param prefix
	 *            单位名的前缀
	 * @return
	 */
	List<Institution> getDsnInstitutionsByPrefixName(String prefix);
	/**
	 * 根据用户输入的机构名称insName，在官方机构名称库中搜索是否存在这样的机构，
	 * 是则返回对应的insId，否则返回0
	 * @param insName 用户输入的机构名称
	 * @return 正确的InsId或者0
	 */
	int searchOfficalInstitution(String insName);
	/**
	 * 获取InsId=0的工作经历
	 * @return
	 */
	List<Work> getWorkWithZeroInstitutionId();
	/**
	 * 获取InsId>0的工作经历
	 * @return
	 */
	List<Work> getWorkWithNonZeroInstitutionId();
}
