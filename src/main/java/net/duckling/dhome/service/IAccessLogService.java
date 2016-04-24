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

import net.duckling.dhome.domain.object.AccessLog;

/**
 * 访问日志服务层
 * @author lvly
 * @since 2012-12-13
 */
public interface IAccessLogService {
	/**
	 * 更新冗余数据
	 * @param uid
	 * @param domain
	 */
	void updateAccessLog(int uid,String domain);
	
	/**
	 * 增加访问日志
	 * @param visitorUid 访问者uid
	 * @param visitedUid 被访问者uid
	 * @param visitorDomain 访问者的域
	 * @param visitorIp 访问者的IP
	 */
	void addAccessLog(int visitorUid,int visitedUid,String visitorDomain,String ip);
	
	/**
	 * 取得访问人数
	 * @param visitedUid 访问id
	 * @return int
	 * */
	int getAccessLogCount(int visitedUid);
	
	/**
	 *获取访问日志
	 * @param visitedUid 被访问的用户uid
	 * @return
	 */
	List<AccessLog> getAccessLogs(int visitedUid);
	
}
