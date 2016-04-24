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
package net.duckling.dhome.service.impl;

import java.util.List;

import net.duckling.dhome.dao.IMenuItemDAO;
import net.duckling.dhome.domain.people.MenuItem;
import net.duckling.dhome.service.IMenuItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 菜单项服务
 * @author Zhaojuan
 *
 */
@Service
public class MenuItemService implements IMenuItemService{
	@Autowired 
	private IMenuItemDAO menuItemDAO;
	
	@Override
	public List<MenuItem> getMenuItemsByUId(int uid) {
		return menuItemDAO.getMenuItemsByUId(uid);
	}

	@Override
	public int addMenuItem(MenuItem menuItem) {
		
		return menuItemDAO.addMenuItem(menuItem);
	}

	@Override
	public int updateMenuItem(MenuItem menuItem) {	
		return menuItemDAO.updateMenuItem(menuItem);
	}


	@Override
	public boolean updateMenuItemStatus(int id, int updateStatus) {
		
		return menuItemDAO.updateMenuItemStatus(id, updateStatus);
	}

	@Override
	public MenuItem getMenuItemById(int id) {

		return menuItemDAO.getMenuItemById(id);
	}

	@Override
	public boolean updateBatchMenuItemSequence(List<MenuItem> menuItems) {
		
		return menuItemDAO.updateBatchMenuItemSequence(menuItems);
	}
	@Override
	public MenuItem getMenuItemByUrl(String url) {
		
		return menuItemDAO.getMenuItemByUrl(url);
	}
	
	public void setMenuItemDAO(IMenuItemDAO menuItemDAO) {
		this.menuItemDAO = menuItemDAO;
	}




}
