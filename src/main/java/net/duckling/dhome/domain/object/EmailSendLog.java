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

import java.util.Date;

/**
 * 发送邮件记录
 * @author lvly
 * @since 2012-10-30
 */
public class EmailSendLog {
	public static final String TYPE_REGIST_TO_USER="regist_to_user";
	
	private int id;
	private int uid;
	private String email;
	private String type;
	private Date sendTime;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getSendTime() {
		if(this.sendTime==null){
			return null;
		}
		return (Date)sendTime.clone();
	}
	public void setSendTime(Date sendTime) {
		if(this.sendTime==null){
			this.sendTime=null;
			return;
		}
		this.sendTime=(Date)sendTime.clone();
	}
	
	

}
