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

public class SearchInstitutionPaperCondition {
	private String publicationYear;
	private int if_min;
	private int if_max;
	private int citation_min;
	private int citation_max;
	private int year_min;
	private int year_max;
	private Date minImp;
	private Date maxImp;
	private int departmentId;
	private int order=ORDER_PUBLICATION_YEAR;
	private int orderType=ORDER_TYPE_DESC;
	
	public static final int ORDER_TYPE_ASC=1;
	public static final int ORDER_TYPE_DESC=2;
	//按第一作者排序
	public static final int ORDER_FIRST_AUTHOR=1;
	//按发表年份排序
	public static final int ORDER_PUBLICATION_YEAR=2;
	//按引用次数排序
	public static final int ORDER_CITATION=3;
	//按影响因子排序
	public static final int ORDER_IF=5;
	//默认，按照ID排序
	public static final int ORDER_ID=4;
	//按首字母排序
	public static final int ORDER_ENG=6;
	
	
	private String keyword;
	private String keyword2;//作者
	private String keyword3;//期刊
	//作者表中的是否认证
	private int status;
	
	
	public String getPublicationYear() {
		return publicationYear;
	}
	public void setPublicationYear(String publicationYear) {
		this.publicationYear = publicationYear;
	}
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getKeyword2() {
		return keyword2;
	}
	public void setKeyword2(String keyword2) {
		this.keyword2 = keyword2;
	}
	public String getKeyword3() {
		return keyword3;
	}
	public void setKeyword3(String keyword3) {
		this.keyword3 = keyword3;
	}
	public int getIf_min() {
		return if_min;
	}
	public void setIf_min(int if_min) {
		this.if_min = if_min;
	}
	public int getIf_max() {
		return if_max;
	}
	public void setIf_max(int if_max) {
		this.if_max = if_max;
	}
	public int getCitation_min() {
		return citation_min;
	}
	public void setCitation_min(int citation_min) {
		this.citation_min = citation_min;
	}
	public int getCitation_max() {
		return citation_max;
	}
	public void setCitation_max(int citation_max) {
		this.citation_max = citation_max;
	}
	public int getYear_min() {
		return year_min;
	}
	public void setYear_min(int year_min) {
		this.year_min = year_min;
	}
	public int getYear_max() {
		return year_max;
	}
	public void setYear_max(int year_max) {
		this.year_max = year_max;
	}
	public Date getMinImp() {
		return minImp;
	}
	public void setMinImp(Date minImp) {
		this.minImp = minImp;
	}
	public Date getMaxImp() {
		return maxImp;
	}
	public void setMaxImp(Date maxImp) {
		this.maxImp = maxImp;
	}
	public int getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	
	
}
