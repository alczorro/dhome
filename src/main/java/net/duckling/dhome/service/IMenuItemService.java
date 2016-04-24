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

import net.duckling.dhome.domain.people.MenuItem;

/**
 * 菜单项的操作接口
 * @author zhaojuan
 *
 */
public interface IMenuItemService {
	/**
	 * 根据uid获得user的menuItem list
	 * @param uid
	 * @return
	 */
	List<MenuItem> getMenuItemsByUId(int uid);
	/**
	 * 根据id查询MenuItem
	 * @param id
	 * @return
	 */
	MenuItem getMenuItemById(int id);
	
	/**
	 * 根据url查询MenuItem
	 * @param url
	 * @return
	 */
	MenuItem getMenuItemByUrl(String url);
	
	/**
	 * 增加一条菜单项
	 * @param menuItem
	 * @return
	 */
	int addMenuItem(MenuItem menuItem);
	
	/**
	 * 更新一条菜单项
	 * @param menuItem
	 * @return
	 */
	int updateMenuItem(MenuItem menuItem);
	
	
	/**
	 * 更新一条菜单项的状态
	 * @param id
	 * @param updateStatus
	 * @return
	 */
	boolean updateMenuItemStatus(int id,int updateStatus);
	
	
	/**
	 * 批量更新菜单项的状态
	 * @param menuItems
	 * @return
	 */
	boolean updateBatchMenuItemSequence(List<MenuItem> menuItems);
}
