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
import java.util.Map;

import cn.vlabs.umt.oauth.AccessToken;
import cn.vlabs.umt.oauth.common.exception.OAuthProblemException;

import net.duckling.dhome.domain.people.DetailedUser;
import net.duckling.dhome.domain.people.Discipline;
import net.duckling.dhome.domain.people.SimpleUser;
/**
 * 用户服务的接口类
 * @author Yangxp
 *
 */
public interface IUserService {
	/**
	 * 通过Email获取SimpleUser对象
	 * @param email
	 * @return
	 */
	SimpleUser getSimpleUser(String email);
	/**
	 * 通过uid获取SimpleUser对象
	 * @param email
	 * @return
	 */
	SimpleUser getSimpleUser(int uid);
	/**
	 * 通过uid获取DetailedUser对象
	 * @param uid
	 * @return
	 */
	DetailedUser getDetailedUser(int uid);
	/**
	 * 根据uid更新SimpleUser对象,不包含管理员操作字段，比如auditPropose和status
	 * @param su
	 * @return
	 */
	boolean updateSimpleUserByUid(SimpleUser su);
	/**
	 * 根据uid更新DetailedUser对象
	 * @param du
	 * @return
	 */
	boolean updateDetailedUserByUid(DetailedUser du);
	
	/**
	 * 通过uid获取SimpleUser对象
	 * @param uid
	 * @return SimpleUser 对象
	 */
	SimpleUser getSimpleUserByUid(int uid);
	/**
	 * 根据UID批量获取SimpleUser
	 * @param uids
	 * @return
	 */
	List<SimpleUser> getSimpleUsersByUid(List<Integer> uids);

	/**
	 * 获得一级学科内容
	 * @return
	 */
	List<Discipline> getRootDiscipline();

	/**
	 * 
	 * 获得子级学科内容
	 * @param id 父id
	 * @return 所有子项
	 */
	List<Discipline> getChildDiscipline(int id);
	/**
	 * 获得学科名称
	 * @param id 学科id
	 * @return 学科名称
	 */
	String getDisciplineName(int id);
	
	/**获得所有用户的数量*/
	int getUserCount();
	
	/**
	 * 判断用户
	 * @param email 
	 * @return 是否是umt注册账户
	 * */
	boolean isUmtUser(String email);
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
	 * 根据imgId查找用户，主要看是否在审核中
	 * @param imgId
	 * @return simpleUser
	 */
	SimpleUser getSimpleUserByImgId(int imgId);
	
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
	 * 创建用户指南记录
	 * @param uid 用户ID
	 * @param module 模块名或页面名
	 * @param step 步骤
	 * @return
	 */
	int create(int uid, String module, int step);
	/**
	 * 查询某用户在某个模块进行到第几步
	 * @param uid 用户ID
	 * @param module 模块名
	 * @return
	 */
	int getStep(int uid, String module);
	/**
	 * 更新用户在指定模块的步骤
	 * @param uid 用户ID
	 * @param module 模块名
	 * @param step 步骤
	 */
	void updateStep(int uid, String module, int step);
 	
	
	/**
	 * 根据兴趣关键字查询用户
	 * @param keyword
	 * @param offset
	 * @param size
	 * @return
	 */
	List<SimpleUser> getSimpleUserByInterest(String keyword, int offset, int size);
	/**
	 * 更新用户的各个字段，包括管理员审核，和审核意见
	 * @param su
	 * @return
	 */
	boolean updateSimpleUserStatusByUid(SimpleUser su);
	/**
	 * 更新用户的最后更新时间
	 * @param su
	 * */
	void updateSimpleUserLastEditTimeByUid(int uid);
	List<SimpleUser> getUserByEmails(List<String> email);
	
	List<SimpleUser> getAllUser();
//	List<SimpleUser> searchUser(String key);
	/**
	 * 验证UMT用户账户和密码信息
	 * @param uid
	 * @param password
	 * @return
	 */
	boolean isCorrectUserInfo(String uid, String password);
//	AccessToken umtPasswordAccessToken(String userName,String password) throws OAuthProblemException;

}
