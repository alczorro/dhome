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

import net.duckling.dhome.domain.institution.InstitutionPublicationStatistic;

/**
 * 机构论文的统计服务
 * @author Yangxp
 * @since 2012-09-18
 */
public interface IInstitutionPubStatisticService {
	/**
	 * 创建一条某机构某年的论文统计信息
	 * @param is 新创建的对象
	 * @return
	 */
	int create(InstitutionPublicationStatistic is);
	/**
	 * 更新某条论文统计信息
	 * @param is 待更新的对象，必须包含有效的id值
	 */
	void updateById(InstitutionPublicationStatistic is);
	/**
	 * 查询某条论文统计信息
	 * @param id
	 * @return
	 */
	InstitutionPublicationStatistic getStatisticById(int id);
	/**
	 * 查询某机构的所有论文统计信息
	 * @param insId 机构ID
	 * @return
	 */
	List<InstitutionPublicationStatistic> getStatisticsByInstitutionId(int insId);
}
