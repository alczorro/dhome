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

import net.duckling.dhome.domain.people.Paper;

/**
 * 机构主页的论文服务
 * @author Yangxp
 *
 */
public interface IInstitutionPaperService {
	/**
	 * 获取某机构(insId)的论文。论文按被引用次数从大到小排序，每次从offset
	 * 向后取size条论文记录。
	 * @param insId 机构ID
	 * @param offset 偏移量
	 * @param size 论文条数
	 * @return
	 */
	List<Paper> getPaperSortByCiteTime(int insId, int offset, int size);
	/**
	 * 获取某机构(insId)在特定年份发表的论文。year为字符形式的数字, 论文选取规则如下：<br/>
	 * 1）若year<0, 则查询所有年份<br/>
	 * 2）若year=0, 则查询所有未知年份，即未能正确解析的年份<br/>
	 * 3）若year>0, 则查询特定年份，如2011 <br/>
	 * 选取后的论文按被引用次数从大到小排序，每次从offset
	 * 向后取size条论文记录。
	 * @param insId 机构ID
	 * @param year 年份
	 * @param offset 偏移量
	 * @param size 论文条数
	 * @return
	 */
	List<Paper> getPaperSortByYear(int insId, String year, int offset, int size);
	/**
	 * 获取某机构(insId)的论文。论文先按作者排序，再按被引用次数从大到小排序，每次从offset
	 * 向后取size条论文记录。
	 * @param insId 机构ID
	 * @param uid 作者ID
	 * @param offset 偏移量
	 * @param size 论文条数
	 * @return
	 */
	List<Paper> getPaperSortByAuthor(int insId, int uid, int offset, int size);
	/**
	 * 获取某机构(insId)中发表了论文的所有年份信息
	 * @param insId 机构ID
	 * @return
	 */
	List<String> getYearsOfAllPaper(int insId);
	/**
	 * 获取某机构(insId)中发表过论文的所有研究队伍成员
	 * @param insId 机构ID
	 * @return
	 */
	List<Integer> getPaperAuthorIds(int insId);
	/**
	 * 获取某机构在特定年份内发表的论文数量。year为字符形式的数字<br/>
	 * 1）若year<0, 则查询所有年份<br/>
	 * 2）若year=0, 则查询所有未知年份，即未能正确解析的年份<br/>
	 * 3）若year>0, 则查询特定年份，如2011 <br/>
	 * @param insId 机构ID
	 * @param year 年份
	 * @return
	 */
	int getPaperAmount(int insId, String year);
	/**
	 * 获取某机构在特定年份所有论文的被引次数总和。year为字符形式的数字<br/>
	 * 1）若year<0, 则查询所有年份<br/>
	 * 2）若year=0, 则查询所有未知年份，即未能正确解析的年份<br/>
	 * 3）若year>0, 则查询特定年份，如2011 <br/>
	 * @param insId 机构ID
	 * @param year 年份
	 * @return
	 */
	int getAllPaperCitedTimes(int insId, String year);
	/**
	 * 获取某机构的G-Index指数
	 * @param insId 机构ID
	 * @return
	 */
	int getGIndex(int insId);
	/**
	 * 获取某机构的H-Index指数
	 * @param insId 机构ID
	 * @return
	 */
	int getHIndex(int insId);
}