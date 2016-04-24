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
import java.util.Map;

import net.duckling.dhome.domain.people.SimpleUser;

/**注册用户模块
 * @author lvly
 * @since  2012-08-06
 * **/
public interface ISimpleUserDAO {
	
	/**通过email查询一个用户，
	 * @param email 邮箱地址
	 * @return simpleUser 一个用户实例
	 * */
	SimpleUser getUser(String email);
	
	/**通过email查询一个用户，
	 * @param id UserID
	 * @return simpleUser 一个用户实例
	 * */
	SimpleUser getUser(int uid);
	/**
	 * 更新一个用户的信息，默认用id查
	 * @param user 需要更新的对象
	 * 
	 * */
	int updateAccount(SimpleUser user);
	/**注册一个用户
	 * @param user 需要插入的账户
	 * @retutn id 新生成的id
	 * 
	 * */ 
	int registAccount(SimpleUser user);
	
	/**验证邮箱时候已经使用
	 * @param email 需要判定的邮箱
	 * @return flag 是否被使用
	 * */
	boolean isEmailUsed(String email);
	
	/**
	 * 得到所有注册用户
	 * @param offset 偏移量
	 * @param size 需要的用户数
	 */
	List<SimpleUser> getAllUsers(int offset, int size);
	List<SimpleUser> getAllUser();
	/**
 	 * 根据所选学科返回用户
 	 * @param first 一级学科id
 	 * @param  second 二级学科id
 	 * @param offset 偏移量
	 * @param size 数量

 	 * @return 
 	 */
	List<SimpleUser> getSimpleUserByDiscipline(int first, int second, int offset, int size);
	/**
	 * 得到最新注册的用户
	 * @param offset 偏移量
	 * @param size 需要的用户数
	 * @return
	 */
	List<SimpleUser> getLatestUsers(int offset, int size);
	/**
	 * 根据指定关键词keyword搜索用户，搜索的列为zh_name, pinyin
	 * @param keyword 关键词
	 * @param offset 偏移量
	 * @param size 需要返回的用户数
	 * @return
	 */
	List<SimpleUser> searchUsers(String keyword, int offset, int size);

	/**
	 * 返回所有用户的数量
	 * @return count
	 */
	int getCount();
	/**
	 * 查询指定审核状态下，包含查询关键词的所有用户。若参数中的status不是
	 * 合法的状态，则默认查询所有用户； 若参数中的keyword为空，则默认不进
	 * 行关键词搜索，只根据审核状态搜索。
	 * @param status 审核状态标志
	 * @param keyword 根据用户关键词检索用户
	 * @param offset 偏移量
	 * @param size 用户数量
	 * @return 用户列表
	 */
	List<SimpleUser> getAllUsers(String status, String keyword, int offset, int size);
	/**
	 * 通过UID批量获取SimpleUser对象
	 * @param uids
	 * @return
	 */
	List<SimpleUser> getUsers(List<Integer> uids);

	/**
	 * 根据imgId查找用户，主要看是否在审核中
	 * @param imgId
	 * @return simpleUser
	 */
	SimpleUser getSimpleUserByImgId(int imgId);

	/**
	 * 返回按关键字搜索返回的记录数量
	 * @param keyword
	 * @return
	 */
	int getSearchComposedUserCount(String keyword);
	
	/**
	 * 根据兴趣关键字查询用户
	 * @param keyword
	 * @param offset
	 * @param size
	 * @return
	 */
	List<SimpleUser> getSimpleUserByInterest(String keyword, int offset, int size);
	
	/**
	 * 更新用户的最后更新时间
	 * @param uid
	 * */
	int updateSimpleUserLastEditTimeByUid(int uid);

	List<SimpleUser> getUsersByEmails(List<String> email);
//	List<SimpleUser> searchUser(String keyword);
	/**
	 * 更新用户的登录邮箱
	 * @param su
	 * */
	void updateSimpleUserEmailByUid(int uid,String newPw);
}
