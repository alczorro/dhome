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

import net.duckling.dhome.domain.people.Paper;
/**
 * Paper的数据库接口类
 * @author Yangxp
 *
 */
public interface IPaperDAO {
	/**
	 * 创建Paper记录
	 * @param paper
	 * @return
	 */
	int create(Paper paper);
	/**
	 * 批量创建Paper记录
	 * @param papers
	 * @return
	 */
	int[] batchCreate(List<Paper> papers);
	/**
	 * 获取用户的所有论文
	 * @param uid 用户ID
	 * @param offset 偏移量（起始值）
	 * @param size 数量
	 * @return 论文集合
	 */
	List<Paper> getPapers(int uid, int offset, int size);
	/**
	 * 获取用户的英文版论文
	 * @param uid 用户ID
	 * @param offset 偏移量（起始值）
	 * @param size 数量
	 * @return 论文集合
	 */
	List<Paper> getEnPapers(int uid, int offset, int size);
	/**
	 * 从Paper表中删除用户的某篇论文记录
	 * @param paperId 被删除的论文ID
	 * @return true or false
	 */
	boolean deletePaper(int paperId);
	/**
	 * 批量更新Paper记录的sequence列
	 * @param uid 用户ID
	 * @param sortedPaper 包含最新sequence信息的Paper列表
	 * @return
	 */
	int batchUpdateSequenceByID(List<Paper> sortedPaper);
	/**
	 * 获取指定Paper对象
	 * @param paperid Paper ID
	 * @return
	 */
	Paper getPaper(int paperid);
	/**
	 * 根据Paper ID更新指定的Paper对象
	 * @param paper 包含最新信息的Paper对象
	 * @return true or false
	 */
	boolean updateById(Paper paper);
	/**
	 * 获取指定用户已经添加的论文DsnPaperID
	 * @param uid 用户ID
	 * @return
	 */
	List<Long> getExistDsnPaperIds(int uid);
	/**
	 * 获取用户的论文数量
	 * @param uid
	 * @return
	 */
	int getPaperCount(int uid);
	
}
