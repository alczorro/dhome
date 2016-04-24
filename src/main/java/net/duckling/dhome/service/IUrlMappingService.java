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

import net.duckling.dhome.domain.people.UrlMapping;

/**
 * 管理urlmapping的服务层
 * @author lvly
 * @since 2012-9-10
 */
public interface IUrlMappingService {
	 String URL_MAPPING = "dhome.domain-custom.mapping";
	/**
	 * 判断url是否被使用过
	 * @param url
	 * @return
	 */
	boolean isUrlUsed(String url,int userId);

	/**
	 * 
	 * 更新urlMapping内容
	 * @param url 域名
	 * @param domain domain
	 * @param userId userID
	 * @param status 状态，启用/未启用
	 */ 
	void updateUrlMapping(String url, String domain, int userId, String status,int urlId);
	
	/**
	 * 根据UserId获得当前有效的域名服务
	 * */
	UrlMapping getUrlMappingByUrl(String url);

	/**
	 * 新增一个urlMapping
	 * @param url
	 * @param domain
	 * @param userId
	 * @param checkStatus
	 */
	int addUrlMapping(String url, String domain, int userId, String checkStatus);

	/**
	 * 根据用户id获取urlMapping的队列
	 * @param userId
	 * @return
	 */
	UrlMapping getUrlMappingByUserId(int userId);

}
