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
package net.duckling.dhome.dao;

import java.util.List;

import net.duckling.dhome.domain.object.AccessLog;

/**
 * @author lvly
 * @since 2012-12-13
 */
public interface IAccessLogDAO {
	/**
	 * 增加访问日志
	 * 
	 * @param accessLog
	 *            访问日志实体类
	 * @return boolean 是否增加访问数量，数据库单记录count+1，或者新插记录都算
	 */
	boolean addAccessLogs(AccessLog log);

	/**
	 * 获取访问日志
	 * 
	 * @param visitedUid
	 *            被访问的用户uid
	 * @param limit
	 *            取数
	 * @return
	 */
	List<AccessLog> getAccessLogs(int visitedUid, int limit);

	/**
	 * 获取访问日志总数
	 * @param visitedUid
	 * @return
	 */
	int getAccessLogCount(int visitedUid);

	/**
	 * @param uid
	 * @param domain
	 */
	void updateAccessLog(int uid, String domain);
}
