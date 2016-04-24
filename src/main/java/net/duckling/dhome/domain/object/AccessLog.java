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
package net.duckling.dhome.domain.object;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 访问日志
 * 
 * @author lvly
 * @since 2012-12-13
 */
public class AccessLog implements Serializable{
	
	private static final long serialVersionUID = -2714797645540537092L;
	/** 主键 */
	private int id;
	/** 访问者id */
	private int visitorUid;
	/** 被访问者id */
	private int visitedUid;
	/** 访问者的域 */
	private String visitorDomain;
	/** 访问时间 */
	private Date visitTime;
	/**访问者ip*/
	private String visitorIp;
	/**访问次数 */
	private int accessCount;
	/**
	 * 判断是否是游客
	 * @return isGuest 
	 * */
	public boolean isGuest(){
		return this.visitorUid<0;
	}
	/**
	 * 判断是否是自己访问自己
	 * @return isSelf
	 * */
	public boolean isSelf(){
		return this.visitorUid==this.visitedUid;
	}
	
	public int getAccessCount() {
		return accessCount;
	}

	public void setAccessCount(int count) {
		this.accessCount = count;
	}

	public String getVisitorIp() {
		return visitorIp;
	}

	public void setVisitorIp(String visitorIp) {
		this.visitorIp = visitorIp;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVisitorUid() {
		return visitorUid;
	}

	public void setVisitorUid(int visitorUid) {
		this.visitorUid = visitorUid;
	}

	public int getVisitedUid() {
		return visitedUid;
	}

	public void setVisitedUid(int visitedUid) {
		this.visitedUid = visitedUid;
	}

	public String getVisitorDomain() {
		return visitorDomain;
	}

	public void setVisitorDomain(String visitorDomain) {
		this.visitorDomain = visitorDomain;
	}

	public Date getVisitTime() {
		if(visitTime!=null){
			return (Date) visitTime.clone();
		}
		return null;
	}

	public void setVisitTime(Date visitTime) {
		if(visitTime!=null){
			this.visitTime=(Date)visitTime.clone();
		}else{
			this.visitTime=null;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AccessLog) {
			AccessLog log = (AccessLog) obj;
			return this.getVisitedUid() == log.getVisitedUid() && this.getVisitorUid() == log.getVisitorUid();
		}
		return false;

	}
	

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.getVisitedUid()).append(this.getVisitorUid()).toHashCode();
	}

}
