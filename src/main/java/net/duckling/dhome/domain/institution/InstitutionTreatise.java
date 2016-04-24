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
 * 论著
 * @author liyanzhao
 *
 */
public class InstitutionTreatise {
	private int id;
	private String name; //论著名称
	private int publisher;  //出版社
	private String language; //语言
	private int institutionId; //机构主页ID
	private int companyOrder; //单位排序
	private int year;
	private int departId;//部门Id;
	
    /*
     * 查询员工论著时，该员工在作者中的排序
     */
	private int authorOrder;
	
	private String author;
	
	 /*
     * 批量导入所需
     */
	private String publisherName;
	private String departShortName;
	private String authorCompany;
	private int author2Order=2;
	private String author2;
	private String author2Company;
	private int author3Order=3;
	private String author3;
	private String author3Company;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getInstitutionId() {
		return institutionId;
	}
	
	public void setInstitutionId(int institutionId) {
		this.institutionId = institutionId;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public int getAuthorOrder() {
		return authorOrder;
	}
	
	public void setAuthorOrder(int authorOrder) {
		this.authorOrder = authorOrder;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPublisher() {
		return publisher;
	}
	public void setPublisher(int publisher) {
		this.publisher = publisher;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public int getCompanyOrder() {
		return companyOrder;
	}
	public void setCompanyOrder(int companyOrder) {
		this.companyOrder = companyOrder;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getDepartId() {
		return departId;
	}
	public void setDepartId(int departId) {
		this.departId = departId;
	}
	public String getDepartShortName() {
		return departShortName;
	}
	public void setDepartShortName(String departShortName) {
		this.departShortName = departShortName;
	}
	public String getAuthorCompany() {
		return authorCompany;
	}
	public void setAuthorCompany(String authorCompany) {
		this.authorCompany = authorCompany;
	}
	public int getAuthor2Order() {
		return author2Order;
	}
	public void setAuthor2Order(int author2Order) {
		this.author2Order = author2Order;
	}
	public String getAuthor2() {
		return author2;
	}
	public void setAuthor2(String author2) {
		this.author2 = author2;
	}
	public String getAuthor2Company() {
		return author2Company;
	}
	public void setAuthor2Company(String author2Company) {
		this.author2Company = author2Company;
	}
	public int getAuthor3Order() {
		return author3Order;
	}
	public void setAuthor3Order(int author3Order) {
		this.author3Order = author3Order;
	}
	public String getAuthor3() {
		return author3;
	}
	public void setAuthor3(String author3) {
		this.author3 = author3;
	}
	public String getAuthor3Company() {
		return author3Company;
	}
	public void setAuthor3Company(String author3Company) {
		this.author3Company = author3Company;
	}
	public String getPublisherName() {
		return publisherName;
	}
	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}
	
}
