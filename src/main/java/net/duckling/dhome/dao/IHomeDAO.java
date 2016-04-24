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

import net.duckling.dhome.domain.people.Home;

/**
 * 域名管理持久化对象
 * @author lvly
 *@since 2012-08-13
 */
public interface IHomeDAO {
	/**创建域名信息
	 * @param home 创建域名
	 * */
	int createHome(Home home);
	/**判断域名是否被使用
	 * @param domain 域名
	 * */
	boolean isDomainUsed(String domain);
	/**
	 * 根据uid 获得自定义域名
	 *@param uid 用户主键 
	 * */
	String getUrlFromUid(int uid);
	
	/**
	 * 根据domain 获得uid
	 *@param domian 用户域名
	 *@return uid 用户主键 
	 * */
	int getUidFromDomain(String domain);
	
	/**
	 * @function 通过domain查询Home对象
	 * @param domain 个人域名
	 * @return Home 对象
	 * @authoer zhaojuan
	 */
	Home getHomeByDomain(String domain);
	/**
	 * @function 更新Home对象
	 * @param Home home对象
	 * @return 操作成功 true 还是失败 false
	 * @authoer zhaojuan
	 */
	boolean updateHome(Home home);
	/**
	 * 批量查询用户域名
	 * @param uids 用户ID
	 * @return 域名列表
	 */
	Map<Integer, String> getDomainByUID(List<Integer> uids);
	List<Home> getDomainsByUids(List<Integer> uids);
}
