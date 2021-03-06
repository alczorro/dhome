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
 * 页面所引用的图片信息，用于Ue中的图片管理
 * @author lvly
 * @since 2012-8-15
 */
public class PageImg {
	/**@Field id
	 * 
	 * 主键*/
	private int id;
	/**@Field uid
	 * 
	 * 用户ID*/
	private int uid;
	/**@Field clbId
	 * 
	 * 存于clb中的文件id*/
	private int clbId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getClbId() {
		return clbId;
	}
	public void setClbId(int clbId) {
		this.clbId = clbId;
	}
	
}
