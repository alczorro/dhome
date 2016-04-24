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

import net.duckling.dhome.domain.institution.Institution;

/**研究单位/学校 持久层
 * @author lvly
 * @since 2012-08-09
 * */
public interface IInstitutionDAO {
	
	/**
	 * 创建一个教育背景
	 * @param name 名称
	 * @return 返回生成的主键
	 * */
	int createInstitution(String name);
	
	/**根据研究机构名字获取institution
	 * @param name 研究机构名称
	 * @return 研究机构实例，无则返回null
	 */
	Institution getInstitutionByName(String name);
	
	/**
	 * 根据主键institutionId获得实体
	 * @param institutionId id
	 * @return InstitutionId
	 * 
	 * */
	Institution getInstitution(int institutionId);
	/**
	 * 给定单位名前缀查询复合前缀的单位列表
	 * @param prefix 单位名的前缀
	 * @return
	 * @author zhaojuan
	 */
	List<Institution> getDsnInstitutionsByPrefixName(String prefix);
	/**
	 * 根据用户输入的机构名称insName，在官方机构名称库中搜索是否存在这样的机构，
	 * 是则返回对应的insId，否则返回0
	 * @param insName 用户输入的机构名称
	 * @return 正确的InsId或者0
	 */
	int searchOfficalInstitution(String insName);
	
}
