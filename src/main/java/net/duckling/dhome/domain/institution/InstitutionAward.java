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

/**
 * 奖励
 * @author liyanzhao
 *
 */
public class InstitutionAward {
	
	private int id;
	private String name;// 成果名称
	private String grantBody; //授予单位
	private int awardName; //奖励名称
	private String awardZhName;//奖励名称
	private int type; //类别
	private String typeName;
	private int grade; //等级
	private String gradeName;
	private int year; //年份
	private int companyOrder; //单位排序
	private int institutionId; //机构主页ID
	private int departId;
	 /*
     * 查询员工论著时，该员工在作者中的排序
     */
	private int authorOrder;
	
	private String author;

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGrantBody() {
		return grantBody;
	}
	public void setGrantBody(String grantBody) {
		this.grantBody = grantBody;
	}
	public int getAwardName() {
		return awardName;
	}
	public void setAwardName(int awardName) {
		this.awardName = awardName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getCompanyOrder() {
		return companyOrder;
	}
	public void setCompanyOrder(int companyOrder) {
		this.companyOrder = companyOrder;
	}
	public int getInstitutionId() {
		return institutionId;
	}
	public void setInstitutionId(int institutionId) {
		this.institutionId = institutionId;
	}
	public int getAuthorOrder() {
		return authorOrder;
	}
	public void setAuthorOrder(int authorOrder) {
		this.authorOrder = authorOrder;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getAwardZhName() {
		return awardZhName;
	}
	public void setAwardZhName(String awardZhName) {
		this.awardZhName = awardZhName;
	}
	public int getDepartId() {
		return departId;
	}
	public void setDepartId(int departId) {
		this.departId = departId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	
}
