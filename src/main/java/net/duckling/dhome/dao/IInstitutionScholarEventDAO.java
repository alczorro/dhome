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

import net.duckling.dhome.domain.institution.ScholarEvent;
import net.duckling.dhome.domain.institution.ScholarEventDetail;

/**
 * 机构的学术活动 数据库接口
 * @author Yangxp
 * @since 2012-09-21
 */
public interface IInstitutionScholarEventDAO {
	/**
	 * 创建学术活动
	 * @param se
	 * @return
	 */
	int create(ScholarEvent se);
	/**
	 * 根据ID更新学术活动
	 * @param se
	 */
	void updateByID(ScholarEvent se);
	/**
	 * 删除学术活动
	 * @param id
	 */
	void remove(int id);
	/**
	 * 获取指定ID的学术活动
	 * @param id
	 * @return
	 */
	ScholarEvent getScholarEventByID(int id);
	/**
	 * 获取指定机构的所有学术活动, ScholarEventDetail相比
	 * ScholarEvent增加了creatorName字段
	 * @param insId 机构ID
	 * @param offset TODO
	 * @param size TODO
	 * @return
	 */
	List<ScholarEventDetail> getAllScholarEventOfInstitution(int insId, int offset, int size);
	/**
	 * 获取指定机构的即将发生的学术活动，离今天越近的活动越靠前
	 * @param insId 机构ID
	 * @param offset 偏移量（即从第几条开始取数据）
	 * @param size 数据量大小
	 * @return
	 */
	List<ScholarEventDetail> getUpcomingScholarEventOfInstitution(int insId, int offset, int size);
	/**
	 * 获取指定机构的正在进行的学术活动，离今天越近的活动越靠前
	 * @param insId 机构ID
	 * @param offset 偏移量（即从第几条开始取数据）
	 * @param size 数据量大小
	 * @return
	 */
	List<ScholarEventDetail> getOngoingScholarEventOfInstitution(int insId, int offset, int size);
	/**
	 * 获取指定机构的已经过期的学术活动，离今天越近的活动越靠前
	 * @param insId 机构ID
	 * @param offset 偏移量（即从第几条开始取数据）
	 * @param size 数据量大小
	 * @return
	 */
	List<ScholarEventDetail> getExpiredScholarEventOfInstitution(int insId, int offset, int size);
	
	/**
	 * 时间控件
	 * @param institutionId机构id
	 * @return int
	 */
	int getActivityCount(int institutionId);
}
