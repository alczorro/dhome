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

import net.duckling.dhome.domain.people.Menu;
import net.duckling.dhome.domain.people.SimpleUser;
/**
 * 菜单服务接口
 * @author Zhaojuan
 *
 */
public interface IMenuService {
	/**
	 * 根据某个user的uid得到当前user的按顺序排列的menu
	 * @param uid
	 * @return
	 */
	Menu getMenu(int uid);
	
	/**
	 * 为新用户创建默认菜单
	 * @param uid
	 * @return
	 */
	void createDefautMenuForNewUser(SimpleUser simpleUser,String domain);
}
