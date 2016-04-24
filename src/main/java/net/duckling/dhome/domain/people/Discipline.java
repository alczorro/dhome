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
package net.duckling.dhome.domain.people;

/**
 * 学科分类
 * @author lvly
 * @since 2012-8-21
 */
public class Discipline {
	/**
	 * 主键
	 * */
	private int id;
	/**一级学科名字*/
	private String name;
	/**父id，是二级学科的父学科的id*/
	private int parentId;
	public  static final  int ROOT_PARENT_ID=0;
	public int getId() {
		return id;
	}
	/**默认生成的，父id是0*/
	public Discipline(){
		this.parentId=ROOT_PARENT_ID;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	
	

}
