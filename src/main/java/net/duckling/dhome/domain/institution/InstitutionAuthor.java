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

public class InstitutionAuthor {
	private int id;
	private String authorName;
	private String authorEmail;
	private String authorCompany;
	private int order;
	private boolean communicationAuthor;
	private boolean authorStudent;
	private boolean authorTeacher;
	private int paperId;
	private String authorType;
	private int topicId;
	private int copyrightId;
	private int patentId;
//	private int userId;
	private int institutionId;
	private int status;
	private String cstnetId; //员工邮箱
	
	//下表id，单位相同的人下表即为一致
	private int subscriptIndex;
	public int getSubscriptIndex() {
		return subscriptIndex;
	}
	public void setSubscriptIndex(int subscriptIndex) {
		this.subscriptIndex = subscriptIndex;
	}
	public int getPaperId() {
		return paperId;
	}
	public void setPaperId(int paperId) {
		this.paperId = paperId;
	}
	private int creator;
	public int getCreator() {
		return creator;
	}
	public void setCreator(int creator) {
		this.creator = creator;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getAuthorEmail() {
		return authorEmail;
	}
	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}
	public String getAuthorCompany() {
		return authorCompany;
	}
	public void setAuthorCompany(String authorCompany) {
		this.authorCompany = authorCompany;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public boolean isCommunicationAuthor() {
		return communicationAuthor;
	}
	public void setCommunicationAuthor(boolean communicationAuthor) {
		this.communicationAuthor = communicationAuthor;
	}
	public boolean isAuthorStudent() {
		return authorStudent;
	}
	public void setAuthorStudent(boolean authorStudent) {
		this.authorStudent = authorStudent;
	}
	public boolean isAuthorTeacher() {
		return authorTeacher;
	}
	public void setAuthorTeacher(boolean authorTeacher) {
		this.authorTeacher = authorTeacher;
	}
	public String getAuthorType() {
		return authorType;
	}
	public void setAuthorType(String authorType) {
		this.authorType = authorType;
	}
	public int getTopicId() {
		return topicId;
	}
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	public int getCopyrightId() {
		return copyrightId;
	}
	public void setCopyrightId(int copyrightId) {
		this.copyrightId = copyrightId;
	}
	public int getPatentId() {
		return patentId;
	}
	public void setPatentId(int patentId) {
		this.patentId = patentId;
	}
	public String getCstnetId() {
		return cstnetId;
	}
	public void setCstnetId(String cstnetId) {
		this.cstnetId = cstnetId;
	}
	public int getInstitutionId() {
		return institutionId;
	}
	public void setInstitutionId(int institutionId) {
		this.institutionId = institutionId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
