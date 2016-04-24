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

import net.duckling.dhome.dao.TempField;

/**
 * @group: net.duckling
 * @title: WorkExperience.java
 * @description: 工作经历
 * @author clive
 * @date 2012-8-4 下午5:39:46
 */
public class Work {
	/**
	 * @Fields id : 自增ID
	 */
	private int id;
	/**
	 * @Fields uid : 用户唯一数字标示
	 */
	private int uid;
	/**
	 * @Fields institutionId : 研究单位数字标示
	 */
	private int institutionId;
	/**
	 * @Fields department : 研究部门名称
	 */
	private String department;
	/**
	 * @Fields beginTime : 工作经历开始时间
	 */
	private Date beginTime;
	/**
	 * @Fields position:职称
	 * */
	private String position;
	/**
	 * @Fields endTime : 工作经历结束时间
	 */
	private Date endTime;
	/**
	 *  @Fields aliasInstitutionName : 用户输入的机构名称
	 */
	private String aliasInstitutionName;
	/**
	 * @Fields firstClassDiscipline : 研究方向一级学科
	 */
	@TempField
	private String firstClassDiscipline;
	/**
	 * @Fields secondClassDiscipline : 研究方向二级学科
	 */
	@TempField
	private String secondClassDiscipline;
	/**
	 * @Fields isCarryOn : 判断是否为还在进行中的工作经历,true表示还在进行中
	 */
	@TempField
	private boolean isCarryOn;
	@TempField
	private String institutionName;//官方机构名
	@TempField
	private String userName;
	/**机构主页域名*/
	@TempField
	private String domain;
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getInstitutionName() {
		return institutionName;
	}
	public void setInstitutionName(String institution) {
		this.institutionName = institution;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * 额的神啊，构造方法还得写注释啊
	 * @param uid userId
	 * @param position 职称
	 * @param department 部门
	 */
	public Work(int uid, String position, String department) {
		this.uid=uid;
		this.position=position;
		this.department=department;
	}
	public Work(){
		
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public boolean isCarryOn() {
		return isCarryOn;
	}

	public void setCarryOn(boolean isCarryOn) {
		this.isCarryOn = isCarryOn;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(int institutionId) {
		this.institutionId = institutionId;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	/**
	 * 获得开始时间
	 * @return
	 */
	public Date getBeginTime() {
		return beginTime!=null?(Date)beginTime.clone():null;
	}
	/**
	 * 设置开始时间
	 * @param beginTime
	 */
	public void setBeginTime(Date beginTime) {
		if(beginTime==null){
			this.beginTime=null;
			return;
		}
		this.beginTime = (Date)beginTime.clone();
	}
	/**
	 * 获得结束时间
	 * @return
	 */
	public Date getEndTime() {
		return endTime!=null?(Date)endTime.clone():null;
	}
	/**
	 * 设置结束时间
	 * @param endTime
	 */
	public void setEndTime(Date endTime) {
		if(endTime!=null){
			this.endTime = (Date)endTime.clone();
		}else{
			this.endTime=null;
		}
		
	}
	
	public String getAliasInstitutionName() {
		return aliasInstitutionName;
	}
	public void setAliasInstitutionName(String aliasInstitutionName) {
		this.aliasInstitutionName = aliasInstitutionName;
	}
	public String getFirstClassDiscipline() {
		return firstClassDiscipline;
	}

	public void setFirstClassDiscipline(String firstClassDiscipline) {
		this.firstClassDiscipline = firstClassDiscipline;
	}

	public String getSecondClassDiscipline() {
		return secondClassDiscipline;
	}

	public void setSecondClassDiscipline(String secondClassDiscipline) {
		this.secondClassDiscipline = secondClassDiscipline;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

}
