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
package net.duckling.dhome.domain.institution;



/**
 * @group: net.duckling
 * @title: Insitution.java
 * @description: 研究单位
 * @author clive
 * @date 2012-8-4 下午4:02:28
 */
public class Institution {
	/**
	  * @Fields id : 研究单位数字标示
	  */
	private int id;
	/**
	  * @Fields zhName : 研究单位中文名称
	  */
	private String zhName;
	/**
	  * @Fields enName : 研究单位英文名称
	  */
	private String enName;
	/**
	  * @Fields shortName : 研究单位简称
	  */
	private String shortName;
	/**
	  * @Fields address : 研究单位地址
	  */
	private String address;
	/**
	  * @Fields status : 标记是否为官方认证的研究单位
	  */
	private String status;

	/**
	  * @Fields pinyin : 拼音  pinyin;py
	  */
	private String pinyin;
	
	
	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getZhName() {
		return zhName;
	}

	public void setZhName(String zhName) {
		this.zhName = zhName;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		return 17+3*id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		Institution other = (Institution) obj;
		if (id != other.id){
			return false;
		}
		return true;
	}

}
