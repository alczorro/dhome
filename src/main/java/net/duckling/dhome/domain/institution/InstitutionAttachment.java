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

import java.sql.Timestamp;

/**
 * 附件
 * @author liyanzhao
 *
 */
public class InstitutionAttachment {
	private final int IMAGE_TYPE = 1;
	private int id;
	private int clbId;
	private String fileName;
	private Timestamp createTime;
	private int type;
	private int connectId; //关联ID
	private int fileType; // 1 图片
	private int objId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getClbId() {
		return clbId;
	}
	public void setClbId(int clbId) {
		this.clbId = clbId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
		if(validatePrefix(fileName))
			setFileType(IMAGE_TYPE);
	}
	
	private boolean validatePrefix(String fileName){
		if(fileName == null)
			return false;
		
		String lower=fileName.toLowerCase();
		return lower.endsWith(".bmp")||lower.endsWith(".jpg")||lower.endsWith(".jpeg") || lower.endsWith(".png") || lower.endsWith("gif");
	}
	
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getConnectId() {
		return connectId;
	}
	public void setConnectId(int connectId) {
		this.connectId = connectId;
	}
	public int getFileType() {
		return fileType;
	}
	public void setFileType(int fileType) {
		this.fileType = fileType;
	}
	public int getObjId() {
		return objId;
	}
	public void setObjId(int objId) {
		this.objId = objId;
	}
	
}
