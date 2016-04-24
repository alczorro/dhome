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

public class SearchInstitutionCondition {
	private int year; //按年份过滤
	private int grade = -1;//按等级过滤
	private int fundsFrom;//按资金来源过滤
	private int publisher = -1; //按出版社
	private int position = -1; //按职位
	private String title; //按职称
	private int deptId; //按部门
	private int deptName; //按部门名称
	private int degree;
	private int order=ORDER_ID;
	private int orderType;
	private int memberDpetId = -1;
	private int paging;//员工分页
	private String jobStatus;//员工在职状态
	
	public static final int ORDER_TYPE_ASC=1;
	public static final int ORDER_TYPE_DESC=2;
	
	//按第一作者排序
	public static final int ORDER_FIRST_AUTHOR=1;
	//按发表年份排序
	public static final int ORDER_YEAR=2;
	//默认，按照ID排序
	public static final int ORDER_ID=4;
	
	
	private String keyword;
	
	
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public int getFundsFrom() {
		return fundsFrom;
	}
	public void setFundsFrom(int fundsFrom) {
		this.fundsFrom = fundsFrom;
	}
	public int getDegree() {
		return degree;
	}
	public void setDegree(int degree) {
		this.degree = degree;
	}
	public int getPublisher() {
		return publisher;
	}
	public void setPublisher(int publisher) {
		this.publisher = publisher;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getMemberDpetId() {
		return memberDpetId;
	}
	public void setMemberDpetId(int memberDpetId) {
		this.memberDpetId = memberDpetId;
	}
	public int getDeptName() {
		return deptName;
	}
	public void setDeptName(int deptName) {
		this.deptName = deptName;
	}
	public int getDeptId() {
		return deptId;
	}
	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}
	public int getPaging() {
		return paging;
	}
	public void setPaging(int paging) {
		this.paging = paging;
	}
	public String getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	
}
