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

import java.util.Date;

import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.web.helper.PageCharHelper;

/**
 * @group: net.duckling
 * @title: CustomPage.java
 * @description: 用户在编辑器中编辑的页面
 * @author clive
 * @date 2012-8-5 上午11:12:23
 */
public class CustomPage {

    /**
     * @Fields id : 页面唯一数字标示
     */
    private int id;
    /**
     * @Fields uid : 创建者的uid
     */
    private int uid;
    /**
     * @Fields createTime : 创建时间
     */
    private Date createTime;
    /**
     * @Fields content : 页面内容
     */
    private String content;
    /**
     * @Fields title : 标题
     */
    private String title;

    /**
     * @Fields usedFor : 页面用途，如“课程”，“项目”等
     */
    private String usedFor;

    /**
     * @Fields url : 页面路径
     */
    private String url;
    /***
     * @Fields keyWord : 页面的key
     * */
    private String keyWord;
    /**
     * @Fields lastEditTime
     * */
    private Date lastEditTime;

    public Date getLastEditTime() {
        return lastEditTime!=null? (Date)lastEditTime.clone():null;
    }

    public void setLastEditTime(Date lastEditTime) {
    	if(lastEditTime!=null){
    		this.lastEditTime = (Date) lastEditTime.clone();
    	}else{
    		this.lastEditTime=null;
    	}
    	
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsedFor() {
        return usedFor;
    }

    public void setUsedFor(String usedFor) {
        this.usedFor = usedFor;
    }

    public int getId() {
        return id;
    }

    public void setId(int pageId) {
        this.id = pageId;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Date getCreateTime() {
        return createTime!=null?(Date) createTime.clone():null;
    }

    public void setCreateTime(Date createTime) {
    	if(createTime==null){
    		this.createTime=null;
    		return;
    	}
        this.createTime = (Date) createTime.clone();
    }

    public String getContent() {
    	if(CommonUtils.isNull(content)){
    		return content;
    	}
        return PageCharHelper.deleteEnter(content);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
