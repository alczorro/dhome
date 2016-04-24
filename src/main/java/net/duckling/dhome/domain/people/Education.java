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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @group: net.duckling
 * @title: Education.java
 * @description: 教育背景
 * @author clive
 * @date 2012-8-4 下午6:06:26
 */
public class Education {
	/**
	 * @Fields id : 自增ID
	 */
	private int id;
	/**
	 * @Fields uid : 用户唯一数字标示
	 */
	private int uid;
	/**
	 * @Fields insitutionId : 教育单位数字标示
	 */
	private int insitutionId;
	/**
	 * @Fields degree : 学历
	 */
	private String degree;

	/**
	 * @Fields department : 部门/系
	 */
	private String department;
	/**
	 * @Fields beginTime : 教育经历开始时间
	 */
	private Date beginTime;
	/**
	 * @Fields endTime : 教育经历结束时间
	 */
	private Date endTime;
	/**
	 * @Fields aliasInstitutionName : 用户输入的机构名称
	 */
	private String aliasInstitutionName;
	
	private String degree2; //学位
	private String graduationProject; //毕业论文
	private int graduationProjectCid; //毕业沦为Id
	private String tutor; //导师
	
	@TempField
	private String userName;
	@TempField
	private String institutionName;//官方机构名
	/**机构主页域名*/
	@TempField
	private String domain;
	
	public String getDegree2() {
		return degree2;
	}
	public void setDegree2(String eduDegree) {
		this.degree2 = eduDegree;
	}
	public String getGraduationProject() {
		return graduationProject;
	}
	public void setGraduationProject(String graduationProject) {
		this.graduationProject = graduationProject;
	}
	public int getGraduationProjectCid() {
		return graduationProjectCid;
	}
	public void setGraduationProjectCid(int graduationProjectCid) {
		this.graduationProjectCid = graduationProjectCid;
	}
	public String getTutor() {
		return tutor;
	}
	public void setTutor(String tutor) {
		this.tutor = tutor;
	}
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

	public int getInsitutionId() {
		return insitutionId;
	}

	public void setInsitutionId(int institutionId) {
		this.insitutionId = institutionId;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
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
		this.beginTime =(Date)beginTime.clone();
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
		if(endTime==null){
			this.endTime=null;
			return;
		}
		this.endTime = (Date)endTime.clone();
	}

	public String getAliasInstitutionName() {
		return aliasInstitutionName;
	}
	public void setAliasInstitutionName(String aliasInstitutionName) {
		this.aliasInstitutionName = aliasInstitutionName;
	}
	public Education() {

	}
	/**额的神啊，构造方法还得写注释
	 * @param uid userId
	 * @param degree 学位
	 * @param department 部门/院校
	 * */
	public Education(int uid, String degree, String department) {
		this.uid = uid;
		this.degree = degree;
		this.department = department;
	}
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Education)) {
			return false;
		}
		Education anotherEdu = (Education) object;
		return new EqualsBuilder()
				.appendSuper(super.equals(object))
				.append(this.getUid(), anotherEdu.getUid())
				.append(this.getDegree(), anotherEdu.getDegree())
				.append(this.getDepartment(),anotherEdu.getDepartment()).isEquals();
	}
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.appendSuper(super.hashCode())
				.append(this.getUid())
				.append(this.getDegree())
				.append(this.getDepartment()).hashCode();
	}

}
