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

public class InstitutionTopic {
	public final static String ROLE_ADMIN="admin";
	public final static String ROLE_MEMBER="member";
	private int id;
	private String start_year;
	private String start_month;
	private String end_year;
	private String end_month;
	private String name;
	private int type;
	private String role;
	private int project_cost;
	private int personal_cost;
	private int institution_id;
	private int  funds_from;
	private String topic_no;
	private int departId;
	
	private String author;
	
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getFunds_from() {
		return funds_from;
	}
	public void setFunds_from(int funds_from) {
		this.funds_from = funds_from;
	}
	public String getTopic_no() {
		return topic_no;
	}
	public void setTopic_no(String topic_no) {
		this.topic_no = topic_no;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public void setStart_year(String start_year) {
		this.start_year = start_year;
	}
	public void setStart_month(String start_month) {
		this.start_month = start_month;
	}
	public void setEnd_year(String end_year) {
		this.end_year = end_year;
	}
	public void setEnd_month(String end_month) {
		this.end_month = end_month;
	}
	
	public String getStart_year() {
		return start_year;
	}
	public String getStart_month() {
		return start_month;
	}
	public String getEnd_year() {
		return end_year;
	}
	public String getEnd_month() {
		return end_month;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public int getProject_cost() {
		return project_cost;
	}
	public void setProject_cost(int project_cost) {
		this.project_cost = project_cost;
	}
	public int getPersonal_cost() {
		return personal_cost;
	}
	public void setPersonal_cost(int personal_cost) {
		this.personal_cost = personal_cost;
	}
	public int getInstitution_id() {
		return institution_id;
	}
	public void setInstitution_id(int institution_id) {
		this.institution_id = institution_id;
	}
	public int getDepartId() {
		return departId;
	}
	public void setDepartId(int departId) {
		this.departId = departId;
	}
	
}
