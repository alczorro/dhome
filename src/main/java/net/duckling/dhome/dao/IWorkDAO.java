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

import net.duckling.dhome.domain.people.Work;

/**工作经历数据持久层
 * @author lvly
 * @since 2012-08-09
 * */
public interface IWorkDAO {
	
	/**
	 * 创建一个工作经历的实例
	 * @param work 工作经历实例
	 * @return
	 */
	int createWork(Work work);
	
	/**根据用户id选择工作经历
	 * @param uid UserID
	 * */
	List<Work> getWorksByUID(int uid);
	/**
	 * 根据work ID获得工作经历
	 * @param id
	 * @return
	 */
	Work getWork(int id);
	/**
	 * 更新Work记录
	 * @param work
	 * @return
	 */
	boolean updateWorkById(Work work);
	/**
	 * 删除Work对象
	 * @param id
	 */
	void deleteWork(int id);
	/**
	 * 返回用户工作经历引用该机构的次数
	 * 
	 * @param institutionId
	 *            机构id
	 * @param uid
	 *            userId
	 * */
	int getCitedWorkCount(int institutionId, int uid);
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
