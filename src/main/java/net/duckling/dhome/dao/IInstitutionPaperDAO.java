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

import net.duckling.dhome.domain.object.PaperStatistics;
import net.duckling.dhome.domain.object.PaperYear;
import net.duckling.dhome.domain.people.Paper;

/**
 * 机构主页的论文的数据库接口
 * @author Yangxp
 *
 */
public interface IInstitutionPaperDAO {
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
	 * @param insId
	 * @return
	 */
	List<Integer> getPaperAuthorIds(int insId);
	/**
	 * 获取某机构在特定年份内发表的论文数量
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
	 * 获取某机构所有论文的被引次数列表，被引次数就是降序排列
	 * @param insId
	 * @return
	 */
	List<Integer> getPaperCitedTimes(int insId);
	/**
	 * 检查数据库中是否所有的整数年份信息缺失
	 * @return true or false
	 */
	boolean isAllYearInfoEmpty();
	/**
	 * 获取所有论文的ID和发表年份信息
	 * @return 
	 */
	List<PaperYear> getAllPaperYear();
	/**
	 * 获取论文的统计信息，包括：机构ID，年份，论文数，被引频次; <br>
	 * 论文来源为：dsn
	 * @return
	 */
	List<PaperStatistics> getAllDSNPaperStatistics();
	/**
	 * 获取论文的统计信息，包括：机构ID，年份，论文数，被引频次; <br>
	 * 论文来源为：非dsn
	 * @return
	 */
	List<PaperStatistics> getAllNotDSNPaperStatistics();
	/**
	 * 更新paper表中的year信息
	 * @param paperYears
	 */
	void updateIntYear(List<PaperYear> paperYears);
	
}
