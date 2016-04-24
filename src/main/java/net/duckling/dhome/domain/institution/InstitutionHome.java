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

import java.util.Date;

/**
 * @title: InstitutionHome.java
 * @package net.duckling.dhome.domain.institution
 * @description: 机构主页 数据结构
 * @author clive
 * @date 2012-9-27 上午11:12:57
 */
public class InstitutionHome {
    private int id;
    private int institutionId;
    private String name;
    private String introduction;
    private String domain;
    private int logoId;
    private int creator;
    private Date createTime;
    private int lastEditor;
    private Date lastEditTime;
    private int paperCount;
    private int citationCount;
    private int hindex;
    private int gindex;
    /**状态！ 跟simpleuser的审核状态类似*/
    private String homeStatus;
	public static final String STATUS_VALID="valid";
	public static final String STATUS_INVALID="invalid";
    

    public String getHomeStatus() {
		return homeStatus;
	}

	public void setHomeStatus(String homeStatus) {
		this.homeStatus = homeStatus;
	}

	public int getLastEditor() {
        return lastEditor;
    }

    public void setLastEditor(int lastEditor) {
        this.lastEditor = lastEditor;
    }
    /**
     * 获得最后编辑的时间
     * @return
     */
    public Date getLastEditTime() {
        return (Date)lastEditTime.clone();
    }
    /**
     * 设置最后编辑的时间
     * @param lastEditTime
     */
    public void setLastEditTime(Date lastEditTime) {
    	if(lastEditTime==null){
    		this.lastEditTime=null;
    		return;
    	}
        this.lastEditTime = (Date)lastEditTime.clone();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(int institutionId) {
        this.institutionId = institutionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getLogoId() {
        return logoId;
    }

    public void setLogoId(int logoId) {
        this.logoId = logoId;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }
    /**
     * 获得创建时间
     * @return
     */
    public Date getCreateTime() {
    	if(createTime==null){
    		return null;
    	}
        return (Date)createTime.clone();
    }
    /**
     * 设置创建时间
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = (Date)createTime.clone();
    }

    public int getPaperCount() {
        return paperCount;
    }

    public void setPaperCount(int paperCount) {
        this.paperCount = paperCount;
    }

    public int getCitationCount() {
        return citationCount;
    }

    public void setCitationCount(int citationCount) {
        this.citationCount = citationCount;
    }

    public int getHindex() {
        return hindex;
    }

    public void setHindex(int hindex) {
        this.hindex = hindex;
    }

    public int getGindex() {
        return gindex;
    }

    public void setGindex(int gindex) {
        this.gindex = gindex;
    }

}
