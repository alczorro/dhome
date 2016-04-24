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

import net.duckling.dhome.dao.TempField;


/**
 * @group: net.duckling
 * @title: Home.java
 * @description: 个人主页类,用于控制前端显示和数据的综合类，
 * @author clive
 * @date 2012-8-4 下午7:55:27
 */
public class Home implements Serializable {
	@TempField
    private static final long serialVersionUID = 1L;
    
	public  static final String LANGUAGE_EN="en_US";
	public  static final String LANGUAGE_ZH="zh_CN";
	
	/**
	 * @Fields id : 个人主页id
	 */
	private int id;

	/**
	  * @Fields uid : 个人主页的属主
	  */
	private int uid;
	/**
	  * @Fields url : 个人主页的URL地址
	  */
	private String url;
	/**
	  * @Fields template : 个人主页所选用的模板
	  */
	@TempField
	private Template template;
	/**
	  * @Fields menu : 菜单
	  */
	@TempField
	private Menu menu;
	/**
	  * @Fields theme : 主题样式
	  */
	
	private int themeid;
	
	/**
	 * @Fields language : 语言 zh_CN/en_US
	 */
	private String language;
	
	public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}
    public int getThemeid() {
		return themeid;
	}

	public void setThemeid(int themeid) {
		this.themeid = themeid;
	}

}
