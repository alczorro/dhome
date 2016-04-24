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

import net.duckling.dhome.domain.people.Education;

/**教育背景数据持久层
 * @author lvly
 * @since 2012-08-09
 * */
public interface IEducationDAO {
	
	/**创建一个教育背景实例
	 * @param edu 教育背景实例
	 * @return 返回新生成的ID
	 * */
	int createEducation(Education edu);

	/**
	 * 返回和用户相关的所有教育经历
	 * @param uid UserId
	 * @return 返回教育经历
	 */
	List<Education> getEdusByUID(int uid);
	/**
	 * 根据ID获取Education对象
	 * @param id
	 * @return
	 */
	Education getEducation(int id);
	/**
	 * 根据ID更新Education记录
	 * @param edu
	 * @return
	 */
	boolean updateEducationById(Education edu);
	/**
	 * 删除Education记录
	 * @param id
	 */
	void deleteEducation(int id);
	
	/**
	 * 返回用户工作经历引用该机构的次数
	 * 
	 * @param institutionId
	 *            机构id
	 * @param uid
	 *            userId
	 * */
	int getCitedEducationCount(int institutionId, int uid);
	/**
	 * 获取InsId=0的教育背景
	 * @return
	 */
	List<Education> getEducationWithZeroInstitutionId();
	/**
	 * 获取InsId>0的教育背景
	 * @return
	 */
	List<Education> getEducationWithNonZeroInstitutionId();
}
