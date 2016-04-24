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
/**
 * 
 */
package net.duckling.dhome.service;

import java.util.List;

import net.duckling.dhome.domain.people.Friend;

/**
 * @author lvly
 * @since 2012-10-11
 */
public interface IFriendService {
	
	/**
	 * 根据用户的所属单位查找simpleUser
	 * @param userId 用户id
	 * @param offset 偏移
	 * @param size 数量
	 * */
	List<Friend> getFriendsByInstitution(int userId,int offset,int size);
	
	/**
	 *根据用户的学科查找
	 *@param userId 用户id
	 * @param offset 偏移
	 * @param size 数量
 */
	List<Friend> getFriendsByDiscipline(int userId,int offset,int size);
	
	/**
	 * 根据用户的研究兴趣查找
	 * @param userId 用户id
	 * @param offset 偏移
	 * @param size 数量
	 * */
	List<Friend> getFriendsByInterest(int userId,int offset,int size);
	/**
	 * 查找访问记录
	 * @param userId 用户id
	 * @param offset 便宜
	 * @param size 数量
	 * @return
	 */
	List<Friend> getFriendsByAccess(int userId);
}
