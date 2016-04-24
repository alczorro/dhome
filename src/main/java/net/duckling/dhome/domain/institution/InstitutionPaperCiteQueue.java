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

public class InstitutionPaperCiteQueue {
	/**
	 * 批量加入
	 */
	public final static int  APPEND_TYPE_BATCH=0;
	/**
	 * 手动单个加入， 执行任务时，单个加入优先执行
	 */
	public final static int  APPEND_TYPE_SINGLE=1;
	
	private int paperId;
	private int uid;
	private Date lastAccess;
	private int appendType;
	
	public int getPaperId() {
		return paperId;
	}
	public void setPaperId(int paperId) {
		this.paperId = paperId;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public Date getLastAccess() {
		return lastAccess;
	}
	public void setLastAccess(Date lastAccess) {
		this.lastAccess = lastAccess;
	}
	public int getAppendType() {
		return appendType;
	}
	public void setAppendType(int appendType) {
		this.appendType = appendType;
	}
	
	
}
