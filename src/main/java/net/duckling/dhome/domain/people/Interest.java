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

/**
 * @group: net.duckling
 * @title: Interest.java
 * @description: 研究兴趣
 * @author clive
 * @date 2012-8-4 下午5:39:08
 */
public class Interest implements Serializable{

	private static final long serialVersionUID = -8352326742138233042L;
	private int id;
	/**
	 * @Fields uid : 用户唯一数字标示
	 */
	private int uid;
	/**
	 * @Fields keywords : 描述研究兴趣的关键词,如:data mining
	 */
	private String keyword;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}
	@Override
	public String toString(){
		return keyword;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keywords) {
		this.keyword = keywords;
	}

}
