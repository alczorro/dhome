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
 * 主页样式主题类
 * 
 * @author zhaojuan
 * @date 2012-8-23
 * 
 */
public class Theme implements Serializable{
    
	@TempField
    private static final long serialVersionUID = 1L;

	/**
	 * 样式编号
	 */
	private int id;
	/**
	 * 样式标题
	 */
	private String title;
	/**
	 * 样式描述
	 */
	private String descript;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}
	public static final int THEME_S_SIMPLE=1;     /**单页简单式主题 */
    public static final int THEME_M_TRADITION=2;  /**多页两栏传统主题 */
    public static final int THEME_M_NAMECRARD=3;  /**多页名片式主题 */
    public static final int THEME_M_TREND=4;  /**多页名片式主题 */
    public static final int THEME_M_STEADY=5;  /**成熟主题 */
    public static final int THEME_S_FASHION=6;  /**成熟主题 */
    public static final int THEME_M_IAP=7;  /**多页大气所专用主题 */
    public static final int DEFAULT_THEME=1;/**默认单页简单式主题 */
    public static final int THEME_M_IAP_SCIENCE=8;/**大气所科研主题 */
}
