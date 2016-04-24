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
package net.duckling.dhome.domain.object;

/**
 * 辅助对象，用于后台审核。合并Work和Education已提供前台需要的字段
 * @author Yangxp
 *
 */
public class InstitutionName {
	
	public static final String WORK = "work";
	public static final String EDUCATION = "education";
	/**
	 * 在work/education表中的ID
	 */
	private int sourceId;
	/**
	 * 类型：work或education
	 */
	private String sourceType;
	/**
	 * 用户输入的机构名，存在work或education中
	 */
	private String institutionName;
	/**
	 * work或education中的机构ID
	 */
	private int institutionId;
	/**
	 * 机构ID对应的官方机构名
	 */
	private String officalInstitutionName;
	
	
	/**
	 * 创建InstitutionName对象
	 * @param id 原始ID，即在work/education表中的ID
	 * @param type 类型：work或education
	 * @param name 用户输入的机构名
	 * @param insId 机构ID
	 * @return
	 */
	public static InstitutionName build(int id, String type, String name, int insId, String officalName){
		InstitutionName insName = new InstitutionName();
		insName.setSourceId(id);
		insName.setInstitutionId(insId);
		insName.setInstitutionName(name);
		insName.setSourceType(type);
		insName.setOfficalInstitutionName(officalName);
		return insName;
	}
	
	public int getSourceId() {
		return sourceId;
	}
	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
	public int getInstitutionId() {
		return institutionId;
	}
	public void setInstitutionId(int institutionId) {
		this.institutionId = institutionId;
	}
	public String getInstitutionName() {
		return institutionName;
	}
	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getOfficalInstitutionName() {
		return officalInstitutionName;
	}

	public void setOfficalInstitutionName(String officalInstitutionName) {
		this.officalInstitutionName = officalInstitutionName;
	}
}
