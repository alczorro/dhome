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
package net.duckling.dhome.domain.institution;

import java.util.Date;

/**
 * 发现里面用到的机构主页实体，相当于大杂烩,放了很多别的信息
 * @author lvly
 * @since 2012-9-25
 */
public class InstitutionHomeDiscover{
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
    private int activityCount;
	private int memberCount;

    public int getLastEditor() {
        return lastEditor;
    }

    public void setLastEditor(int lastEditor) {
        this.lastEditor = lastEditor;
    }

    public Date getLastEditTime() {
    	if(lastEditTime==null){
    		return null;
    	}
        return (Date)lastEditTime.clone();
    }

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

    public Date getCreateTime() {
    	if(createTime==null){
    		return null;
    	}
        return (Date)createTime.clone();
    }

    public void setCreateTime(Date createTime) {
    	if(createTime==null){
    		return;
    	}
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
	

	public int getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}

	public int getActivityCount() {
		return activityCount;
	}

	public void setActivityCount(int activityCount) {
		this.activityCount = activityCount;
	}

	public InstitutionHomeDiscover(InstitutionHome home){
		this.citationCount=home.getCitationCount();
		this.createTime=home.getCreateTime();
		this.creator=home.getCreator();
		this.domain=home.getDomain();
		this.gindex=home.getGindex();
		this.hindex=home.getHindex();
		this.id=home.getId();
		this.institutionId=home.getInstitutionId();
		this.introduction=home.getIntroduction();
		this.lastEditor=home.getLastEditor();
		this.lastEditTime=home.getLastEditTime();
		this.logoId=home.getLogoId();
		this.name=home.getName();
		this.paperCount=home.getPaperCount();
	}
	public InstitutionHomeDiscover(){
		
	}

}
