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

import net.duckling.dhome.domain.people.UserGuide;

/**
 * 用户指南的DAO接口
 * @author Yangxp
 * @since 2012-10-11
 */
public interface IUserGuideDAO {
	/**
	 * 创建用户指南记录
	 * @param ug
	 * @return
	 */
	int create(UserGuide ug);
	/**
	 * 更新用户步骤
	 * @param uid
	 * @param step
	 */
	void updateStep(int uid, String module, int step);
	/**
	 * 查询用户在指定模块进行到第几步
	 * @param uid
	 * @param module
	 * @return
	 */
	int getStep(int uid, String module);
}
