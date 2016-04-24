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

import net.duckling.dhome.domain.people.Home;
import net.duckling.dhome.domain.people.SimpleUser;


/**
 * 域名管理服务层
 * 
 * @author lvly
 * @since 2012-08-13
 * */
public interface IHomeService {
	/**从用户id获取自定义域名
	 * @param uid 用户ID
	 * */
	String getDomain(int uid);
	
	/**
	 * @function 通过domain查询SimpleUser
	 * @param domain 个人域名
	 * @return simpleUser 对象
	 */
	SimpleUser getSimpleUserByDomain(String domain);
	
	/**
	 * @function 通过domain查询Home对象
	 * @param domain 个人域名
	 * @return Home 对象
	 * @authoer zhaojuan
	 */
	Home getHomeByDomain(String domain);
	/**
	 * @function 更新Home对象
	 * @param Home 对象
	 * @return 操作成功 true 还是失败 false
	 * @authoer zhaojuan
	 */
	boolean updateHome(Home home);
	/**
	 * 批量查询域名
	 * @param uids 用户ID列表
	 * @return 域名列表
	 */
	Map<Integer, String> getDomainByUID(List<Integer> uids);

	List<Home> getDomainsByUids(List<Integer> uids);
}
