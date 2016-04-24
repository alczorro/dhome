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

import java.io.Serializable;
import java.util.Date;

/**
 * 学术活动
 * 
 * @author Yangxp
 * @since 2012-09-25
 */
public class ScholarEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String title;
    private String reporter;
    private Date startTime;
    private Date endTime;
    private int creator;
    private Date createTime;
    private String introduction;
    private int logoId;
    private int institutionId;
    private String place;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

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

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }
    /**
     * 获得开始时间
     * @return
     */
    public Date getStartTime() {
    	if(startTime==null){
    		return null;
    	}
        return (Date)startTime.clone();
    }
    /**
     * 设置开始时间
     * @param startTime
     */
    public void setStartTime(Date startTime) {
    	if(startTime==null){
    		this.startTime=null;
    		return;
    	}
        this.startTime = (Date)startTime.clone();
    }
    /**
     * 获得结束时间
     * @return
     */
    public Date getEndTime() {
    	if(endTime==null){
    		return null;
    	}
        return (Date)endTime.clone();
    }
    /**
     * 设置结束时间
     * @param endTime
     */
    public void setEndTime(Date endTime) {
    	if(endTime==null){
    		this.endTime=null;
    		return;
    	}
        this.endTime = (Date)endTime.clone();
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
    	if(this.createTime==null){
    		return null;
    	}
        return (Date)createTime.clone();
    }
    /**
     * 设置创建时间
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
    	if(createTime==null){
    		this.createTime=null;
    		return;
    	}
        this.createTime = (Date)createTime.clone();
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getLogoId() {
        return logoId;
    }

    public void setLogoId(int logoId) {
        this.logoId = logoId;
    }

    public int getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(int institutionId) {
        this.institutionId = institutionId;
    }

}
