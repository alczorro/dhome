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
package net.duckling.dhome.common.repository;

public enum ETable {
	PAGE(EField.Page.class),SIMPLE_USER(EField.SimpleUser.class), Menu_Item(EField.MenuItem.class);
	private EField[] values;
	private ETable(Class<? extends EField> table){
		this.values = table.getEnumConstants();
	}
	public EField[] getTableFields(){
		return values.clone();
	}
}
