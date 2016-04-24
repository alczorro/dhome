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
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionHomeDiscover;

/**
 * 机构的Home服务
 * @author Yangxp
 *
 */
public interface IInstitutionHomeService {
	/**
	 * 通过域名获取某机构的ID
	 * @param domain
	 * @return
	 */
	int getInstitutionIdByDomain(String domain);
	/**
	 * 创建机构主页
	 * @param userId  用户id
	 * @param institution 机构id
	 * @return 返回生成的id
	 * */
	int createInstitutionHome(int userId,Institution institution);
	/**
	 * 管理员创建机构主页
	 * @param userId  用户id
	 * @param institution 机构id
	 * @return 返回生成的id
	 * */
	int createInstitutionHomeForAdmin(int adminUserId, Institution institution);
	
	/**
	 * 根据机构主页的域名，返回机构主页
	 * @param domain 机构主页域名
	 * @return 机构主页
	 */
	InstitutionHome getInstitutionByDomain(String domain);
	
	/**
	 * 更新机构主页的信息
	 * @param home
	 */
	void updateInstitutionHome(InstitutionHome home);
	/**
	 * 更新机构主页的信息，能够更新整型域为0
	 * @param home
	 */
	void updateInstitutionHomeForZeroFieldById(InstitutionHome home);
	/**
	 * 根据机构id查询机构主页id
	 * @param institutionId
	 * @return
	 */
	InstitutionHome getInstitutionByInstitutionId(int institutionId);
	
	/**
	 * 根据最晚创建，获得机构主页，用于发现
	 * @param offset
	 * @param size
	 * @return
	 */
	List<InstitutionHomeDiscover> getInstitutionsByLastest(int offset, int size);
	/**
	 * 根据论文数量排序
	 * @param offset
	 * @param size
	 * @return
	 */
	List<InstitutionHomeDiscover> getInstitutionsByPaperCount(int offset, int size);
	
	/**
	 * 根据研究队伍数量排序
	 * @param offset
	 * @param size
	 * @return
	 */
	List<InstitutionHomeDiscover> getInstitutionsByMemberCount(int offset, int size);
	
	/**
	 * 机构主页根据关键字搜索
	 * @param keyword 关键字
	 * @param offset 偏移量
	 * @param size 数量
	 * @return
	 */
	List<InstitutionHomeDiscover> getInstitutionsByKeyword(String keyword,int offset,int size);
	
	/**
	 * 根据关键字查询的机构主页记录数量
	 * @param keyword
	 * @return
	 */
	int getInstitutionsByKeywordCount(String keyword);
	
	/**
	 * 如果根据这个domain查不到home，或者这个home被管理员屏蔽掉，那就算无效
	 * @param domain 机构主页域名
	 * @return
	 */
	boolean isValidHome(String domain);
	/**
	 * 搜索与用户输入的机构名匹配的官方机构名
	 * @param name
	 * @return
	 */
	List<Institution> searchForInstitutionBySimilarName(String name);
	/**
	 * 获得Institution对象
	 * @param insId Institution的ID
	 * @return
	 */
	Institution getInstitutionById(int insId);
	/**
	 * 在Institution_name_mapping中创建机构的别名
	 * @param name
	 * @param insId
	 * @param isFull
	 * @return
	 */
	int createAliasInstitutionName(String name, int insId, boolean isFull);
	/**
	 * 在institution_name_mapping中取消机构的某个别名
	 * @param name 待取消的别名
	 * @param insId 机构ID
	 */
	void deleteAliasInstitutionName(String name, int insId);
	/**
	 * 判断name是否为机构(insId)的官方名称或者别名
	 * @param name 待判定名称
	 * @param insId 机构ID
	 * @param officalOrCustom true:官方名称，false:机构别名
	 * @return
	 */
	boolean checkTypeOfInstitutionName(String name, int insId, boolean officalOrCustom);
}
