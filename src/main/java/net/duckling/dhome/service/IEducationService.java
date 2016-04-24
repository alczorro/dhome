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

import net.duckling.dhome.domain.people.Education;

/**
 *  教育背景服务层类
 * @author lvly
 * @since 2012-8-14
 */
public interface IEducationService {
	
	/**
	 * 返回和用户有关的教育背景
	 * @param uid UserId
	 * @return 返回所有和用户有关的教育背景
	 */
	List<Education> getEducationsByUid(int uid);
	/**
	 * 根据ID获得Education对象
	 * @param id
	 * @return
	 */
	Education getEducation(int id);
	/**
	 * 创建Education对象
	 * @param edu
	 * @return
	 */
	int createEducation(Education edu);
	/**
	 * 更新Education对象
	 * @param edu
	 * @return
	 */
	boolean updateEducationById(Education edu);
	/**
	 * 删除Education对象
	 * @param id
	 */
	void deleteEducation(int id);
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
