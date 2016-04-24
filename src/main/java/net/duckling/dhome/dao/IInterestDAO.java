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

import net.duckling.dhome.domain.object.InterestCount;
import net.duckling.dhome.domain.people.Dictionary;
import net.duckling.dhome.domain.people.Interest;
/**
 * 用户研究兴趣的DAO接口
 * @author Yangxp
 *
 */
public interface IInterestDAO {
	/**
	 * 创建用户的研究兴趣
	 * @param interests
	 * @return
	 */
	int[] batchCreate(int uid, List<String> interests);
	/**
	 * 删除用户的所有研究兴趣
	 * @param uid
	 * @return
	 */
	boolean deleteByUid(int uid);
	/**
	 * 查询用户的所有研究兴趣
	 * @param uid
	 * @return
	 */
	List<Interest> getInterest(int uid);

	/**
	 * 查询指定关键词的研究兴趣
	 * @param keyword
	 * @return
	 */
	List<Dictionary> searchInterest(String keyword);
	/**
	 * 返回所有研究兴趣加上计数，当然要审核通过的
	 * 
	 * @return
	 */
	List<InterestCount> getInterestAll();
	/**
	 * 获得某个关键词的使用频率，当然要审核通过的
	 * @param keyword
	 * @return
	 */
	int getCount(String keyword);

}
