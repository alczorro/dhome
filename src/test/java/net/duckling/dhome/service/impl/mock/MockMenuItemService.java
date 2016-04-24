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
package net.duckling.dhome.service.impl.mock;

import java.util.List;

import net.duckling.dhome.domain.people.MenuItem;
import net.duckling.dhome.service.IMenuItemService;

/**
 * @author lvly
 * @since 2012-8-22
 */
public class MockMenuItemService implements IMenuItemService{

	@Override
	public List<MenuItem> getMenuItemsByUId(int uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem getMenuItemById(int id) {
		return new MenuItem();
	}

	@Override
	public MenuItem getMenuItemByUrl(String url) {
		MenuItem item= new MenuItem();
		item.setStatus(MenuItem.MENU_ITEM_STATUS_SHOWING);
		return item;
	}

	@Override
	public int addMenuItem(MenuItem menuItem) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateMenuItem(MenuItem menuItem) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean updateMenuItemStatus(int id, int updateStatus) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateBatchMenuItemSequence(List<MenuItem> menuItems) {
		// TODO Auto-generated method stub
		return false;
	}

}
