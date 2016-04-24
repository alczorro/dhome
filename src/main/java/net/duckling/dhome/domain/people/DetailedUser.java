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
 * @title: DetailedUser.java
 * @description: 用户详细信息
 * @author clive
 * @date 2012-8-4 下午6:35:03
 */
public class DetailedUser implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @Fields id : 自增ID
	 */
	private int id;

	/**
	 * @Fields uid :
	 */
	private int uid;
	/**
	 * @Fields birthday : 生日
	 */
	private String birthday;
	/**
	 * @Fields gender : 性别
	 */
	private String gender;
	/**
	 * @Fields weibo : 微博地址
	 */
	private String weiboUrl;
	/**
	 * @Fields blog : 博客地址
	 */
	private String blogUrl;
	/**
	  * @Fields introduction : 个人简介
	  */
	private String introduction;
	
	/**
	  * @Fields firstClassDiscipline : 一级研究领域
	  */
	private int firstClassDiscipline;
	
	/**
	  * @Fields secondClassDiscipline : 二级研究领域
	  */
	private int secondClassDiscipline;
	
	
	public int getFirstClassDiscipline() {
		return firstClassDiscipline;
	}

	public void setFirstClassDiscipline(int firstClassDiscipline) {
		this.firstClassDiscipline = firstClassDiscipline;
	}

	public int getSecondClassDiscipline() {
		return secondClassDiscipline;
	}

	public void setSecondClassDiscipline(int secondClassDiscipline) {
		this.secondClassDiscipline = secondClassDiscipline;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getWeiboUrl() {
		return weiboUrl;
	}

	public void setWeiboUrl(String weiboUrl) {
		this.weiboUrl = weiboUrl;
	}

	public String getBlogUrl() {
		return blogUrl;
	}

	public void setBlogUrl(String blogUrl) {
		this.blogUrl = blogUrl;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

}
