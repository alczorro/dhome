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

import net.duckling.dhome.domain.people.DetailedUser;

/**用户详细信息的基本数据持久层
 * 
 * @author lvly
 * @since 2012-08-08
 * */
public interface IDetailedUserDAO {
	/**创建一个用户的详细信息实例*/
	int createDetailedUser(DetailedUser dUser);
	/**
	 * 获取用户详细信息
	 * @param uid 用户ID
	 * @return
	 */
	DetailedUser getUser(int uid);
	/**
	 * 根据Uid更新DetailedUser对象
	 * @param du
	 * @return
	 */
	boolean updateDetailedUserByUid(DetailedUser du);
}
