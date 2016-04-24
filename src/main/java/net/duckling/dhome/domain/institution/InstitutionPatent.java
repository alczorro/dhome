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

public class InstitutionPatent {
	private int id;
	private String name;
	private int type;
	private int grade;
	private int personalOrder;
	private int companyOrder;
	private int institutionId;
	private int year;
	private String author;
	private int departId;
	private String applyNo;
	private int status;
	
	//批量导入
	private String departShortName;
	private String author1;
	private String author1Company;
	private int authorOrder1=1;
	private String author2;
	private String author2Company;
	private int authorOrder2=2;
	private String author3;
	private String author3Company;
	private int authorOrder3=3;
	private String author4;
	private String author4Company;
	private int authorOrder4=4;
	private String author5;
	private String author5Company;
	private int authorOrder5=5;
	private String author6;
	private String author6Company;
	private int authorOrder6=6;
	private String author7;
	private String author7Company;
	private int authorOrder7=7;
	
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
	public int getPersonalOrder() {
		return personalOrder;
	}
	public void setPersonalOrder(int personalOrder) {
		this.personalOrder = personalOrder;
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
	public String getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAuthor1() {
		return author1;
	}
	public void setAuthor1(String author1) {
		this.author1 = author1;
	}
	public String getAuthor1Company() {
		return author1Company;
	}
	public void setAuthor1Company(String author1Company) {
		this.author1Company = author1Company;
	}
	public int getAuthorOrder1() {
		return authorOrder1;
	}
	public void setAuthorOrder1(int authorOrder1) {
		this.authorOrder1 = authorOrder1;
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
	public int getAuthorOrder2() {
		return authorOrder2;
	}
	public void setAuthorOrder2(int authorOrder2) {
		this.authorOrder2 = authorOrder2;
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
	public int getAuthorOrder3() {
		return authorOrder3;
	}
	public void setAuthorOrder3(int authorOrder3) {
		this.authorOrder3 = authorOrder3;
	}
	public String getAuthor4() {
		return author4;
	}
	public void setAuthor4(String author4) {
		this.author4 = author4;
	}
	public String getAuthor4Company() {
		return author4Company;
	}
	public void setAuthor4Company(String author4Company) {
		this.author4Company = author4Company;
	}
	public int getAuthorOrder4() {
		return authorOrder4;
	}
	public void setAuthorOrder4(int authorOrder4) {
		this.authorOrder4 = authorOrder4;
	}
	public String getAuthor5() {
		return author5;
	}
	public void setAuthor5(String author5) {
		this.author5 = author5;
	}
	public String getAuthor5Company() {
		return author5Company;
	}
	public void setAuthor5Company(String author5Company) {
		this.author5Company = author5Company;
	}
	public int getAuthorOrder5() {
		return authorOrder5;
	}
	public void setAuthorOrder5(int authorOrder5) {
		this.authorOrder5 = authorOrder5;
	}
	public String getAuthor6() {
		return author6;
	}
	public void setAuthor6(String author6) {
		this.author6 = author6;
	}
	public String getAuthor6Company() {
		return author6Company;
	}
	public void setAuthor6Company(String author6Company) {
		this.author6Company = author6Company;
	}
	public int getAuthorOrder6() {
		return authorOrder6;
	}
	public void setAuthorOrder6(int authorOrder6) {
		this.authorOrder6 = authorOrder6;
	}
	public String getAuthor7() {
		return author7;
	}
	public void setAuthor7(String author7) {
		this.author7 = author7;
	}
	public String getAuthor7Company() {
		return author7Company;
	}
	public void setAuthor7Company(String author7Company) {
		this.author7Company = author7Company;
	}
	public int getAuthorOrder7() {
		return authorOrder7;
	}
	public void setAuthorOrder7(int authorOrder7) {
		this.authorOrder7 = authorOrder7;
	}
	public String getDepartShortName() {
		return departShortName;
	}
	public void setDepartShortName(String departShortName) {
		this.departShortName = departShortName;
	}
	
}
