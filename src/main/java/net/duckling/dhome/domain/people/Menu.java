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
package net.duckling.dhome.domain.people;

import java.io.Serializable;
import java.util.List;

import net.duckling.dhome.dao.TempField;

/**
 * @group: net.duckling
 * @title: Menu.java
 * @description: 菜单，包含若干个菜单项
 * @author clive
 * @date 2012-8-4 下午7:55:33
 */
public class Menu implements Serializable {
	 @TempField
    private static final long serialVersionUID = 1L;

	/**
	 * @Fields uid : 菜单属主的标号
	 */
	private int uid;
	/**
	 * @Fields menuItems : 菜单所包含的菜单项列表
	 */
	private List<MenuItem> menuItems;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public List<MenuItem> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}

}
