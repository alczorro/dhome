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

public class InstitutionMemberFromVmt {
	private int id;
	private int uid;
	private String cstnetId;
	private String umtId;
	private int institutionId;
	private int departId;
	private String department;
	private String trueName;
	private String display;
	public static final String DISPLAY_SHOW="show";
	public static final String DISPLAY_HIDE="hide";
	public static final String DISPLAY_DELETE="delete";
	private String userType=USER_TYPE_MEMBER;
	public static final String USER_TYPE_ADMIN="admin";
	public static final String USER_TYPE_MEMBER="member";
	
	private String sex;
	
	public static final String SEX_MAN="male";
	public static final String SEX_WOMAN="female";
	
	private String officeAddress;
	private String officeTelephone;
	private String mobilePhone;
	private String title;
	
	
	private String sn;
	private Date birth;
	private String birthPlace;
	private int age;
	private int jobAge;
	private String jobType;
	private String levelCode;
	private String jobLevel;
	private String salaryLevel;
	private String homeAddress;
	private Date jobDate;
	private String politicalStatus;
	private String dutyType;
	private String jobStatus;
	private String dutyGrade;
	private String salaryBase;
	private String salaryJob;
	private Date retireDate;
	private Date leaveDate;
	private Date partyDate;
	private String graduateSchool;
	private Date graduateDate;
	private String major;
	private String highestGraduateCode;
	private String highestGraduate;
	private String highestDegreeCode;
	private String highestDegree;
	private Date degreeDate;
	private String degreeCompany;
	private Date companyJoinDate;
	private String companyJoinWay;
	private Date industryJoinDate;
	private Date districtJoinDate;
	private String companyJoinStatus;
	private String companyJoinType;
	private String companyBefore;
	private String companyBeforeRelation;
	private String companyBeforeType;
	private String districtBefore;
	private String administrationDuty;
	private String administrationDutyDesc;
	private String administrationStatus;
	private String administrationInstitution;
	private String administrationAprovedBy;
	private Date administrationAprovedDate;
	private String administrationAprovedFile;
	private Date administrationRetainDate;
	private String staffLevel;
	private Date staffLevedAprovedDate;
	private String staffLevedAprovedFile;
	private String staffLevedAprovedCompany;
	private String technicalDuty;
	private String technicalTitle;
	private String technicalDutyCompany;
	private Date technicalDutyEvaluateDate;
	private Date technicalDutyEndDate;
	private String technicalDutyLevel;
	private Date technicalDutyBeginDate;
	private String technicalTitleMajor;
	private String dutyCompany;
	private String dutyChangeType;
	private String dutyChangeFile;
	private String hasKnowledgeInnovation;
	private String hasArchive;
	private String hasContract;
	private String dutyName;
	private String dutyLevel;
	private Date dutyDate;
	//导入员工状态显示
	private String status;
	
	
	
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getOfficeAddress() {
		return officeAddress;
	}
	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}
	public String getOfficeTelephone() {
		return officeTelephone;
	}
	public void setOfficeTelephone(String officeTelephone) {
		this.officeTelephone = officeTelephone;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	public int getId() {
		return id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCstnetId() {
		return cstnetId;
	}
	public void setCstnetId(String cstnetId) {
		this.cstnetId = cstnetId;
	}
	public String getUmtId() {
		return umtId;
	}
	public void setUmtId(String umtId) {
		this.umtId = umtId;
	}
	public int getInstitutionId() {
		return institutionId;
	}
	public void setInstitutionId(int institutionId) {
		this.institutionId = institutionId;
	}
	public int getDepartId() {
		return departId;
	}
	public void setDepartId(int departId) {
		this.departId = departId;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	public String getBirthPlace() {
		return birthPlace;
	}
	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getJobAge() {
		return jobAge;
	}
	public void setJobAge(int jobAge) {
		this.jobAge = jobAge;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public String getLevelCode() {
		return levelCode;
	}
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
	public String getJobLevel() {
		return jobLevel;
	}
	public void setJobLevel(String jobLevel) {
		this.jobLevel = jobLevel;
	}
	public String getSalaryLevel() {
		return salaryLevel;
	}
	public void setSalaryLevel(String salaryLevel) {
		this.salaryLevel = salaryLevel;
	}
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	public Date getJobDate() {
		return jobDate;
	}
	public void setJobDate(Date jobDate) {
		this.jobDate = jobDate;
	}
	public String getPoliticalStatus() {
		return politicalStatus;
	}
	public void setPoliticalStatus(String politicalStatus) {
		this.politicalStatus = politicalStatus;
	}
	public String getDutyType() {
		return dutyType;
	}
	public void setDutyType(String dutyType) {
		this.dutyType = dutyType;
	}
	public String getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	public String getDutyGrade() {
		return dutyGrade;
	}
	public void setDutyGrade(String dutyGrade) {
		this.dutyGrade = dutyGrade;
	}
	public String getSalaryBase() {
		return salaryBase;
	}
	public void setSalaryBase(String salaryBase) {
		this.salaryBase = salaryBase;
	}
	public String getSalaryJob() {
		return salaryJob;
	}
	public void setSalaryJob(String salaryJob) {
		this.salaryJob = salaryJob;
	}
	public Date getRetireDate() {
		return retireDate;
	}
	public void setRetireDate(Date retireDate) {
		this.retireDate = retireDate;
	}
	public Date getLeaveDate() {
		return leaveDate;
	}
	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}
	public Date getPartyDate() {
		return partyDate;
	}
	public void setPartyDate(Date partyDate) {
		this.partyDate = partyDate;
	}
	public String getGraduateSchool() {
		return graduateSchool;
	}
	public void setGraduateSchool(String graduateSchool) {
		this.graduateSchool = graduateSchool;
	}
	public Date getGraduateDate() {
		return graduateDate;
	}
	public void setGraduateDate(Date graduateDate) {
		this.graduateDate = graduateDate;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getHighestGraduateCode() {
		return highestGraduateCode;
	}
	public void setHighestGraduateCode(String highestGraduateCode) {
		this.highestGraduateCode = highestGraduateCode;
	}
	public String getHighestGraduate() {
		return highestGraduate;
	}
	public void setHighestGraduate(String highestGraduate) {
		this.highestGraduate = highestGraduate;
	}
	public String getHighestDegreeCode() {
		return highestDegreeCode;
	}
	public void setHighestDegreeCode(String highestDegreeCode) {
		this.highestDegreeCode = highestDegreeCode;
	}
	public String getHighestDegree() {
		return highestDegree;
	}
	public void setHighestDegree(String highestDegree) {
		this.highestDegree = highestDegree;
	}
	public Date getDegreeDate() {
		return degreeDate;
	}
	public void setDegreeDate(Date degreeDate) {
		this.degreeDate = degreeDate;
	}
	public String getDegreeCompany() {
		return degreeCompany;
	}
	public void setDegreeCompany(String degreeCompany) {
		this.degreeCompany = degreeCompany;
	}
	public Date getCompanyJoinDate() {
		return companyJoinDate;
	}
	public void setCompanyJoinDate(Date companyJoinDate) {
		this.companyJoinDate = companyJoinDate;
	}
	public String getCompanyJoinWay() {
		return companyJoinWay;
	}
	public void setCompanyJoinWay(String companyJoinWay) {
		this.companyJoinWay = companyJoinWay;
	}
	public Date getIndustryJoinDate() {
		return industryJoinDate;
	}
	public void setIndustryJoinDate(Date industryJoinDate) {
		this.industryJoinDate = industryJoinDate;
	}
	public Date getDistrictJoinDate() {
		return districtJoinDate;
	}
	public void setDistrictJoinDate(Date districtJoinDate) {
		this.districtJoinDate = districtJoinDate;
	}
	public String getCompanyJoinStatus() {
		return companyJoinStatus;
	}
	public void setCompanyJoinStatus(String companyJoinStatus) {
		this.companyJoinStatus = companyJoinStatus;
	}
	public String getCompanyJoinType() {
		return companyJoinType;
	}
	public void setCompanyJoinType(String companyJoinType) {
		this.companyJoinType = companyJoinType;
	}
	public String getCompanyBefore() {
		return companyBefore;
	}
	public void setCompanyBefore(String companyBefore) {
		this.companyBefore = companyBefore;
	}
	public String getCompanyBeforeRelation() {
		return companyBeforeRelation;
	}
	public void setCompanyBeforeRelation(String companyBeforeRelation) {
		this.companyBeforeRelation = companyBeforeRelation;
	}
	public String getCompanyBeforeType() {
		return companyBeforeType;
	}
	public void setCompanyBeforeType(String companyBeforeType) {
		this.companyBeforeType = companyBeforeType;
	}
	public String getDistrictBefore() {
		return districtBefore;
	}
	public void setDistrictBefore(String districtBefore) {
		this.districtBefore = districtBefore;
	}
	public String getAdministrationDuty() {
		return administrationDuty;
	}
	public void setAdministrationDuty(String administrationDuty) {
		this.administrationDuty = administrationDuty;
	}
	public String getAdministrationDutyDesc() {
		return administrationDutyDesc;
	}
	public void setAdministrationDutyDesc(String administrationDutyDesc) {
		this.administrationDutyDesc = administrationDutyDesc;
	}
	public String getAdministrationStatus() {
		return administrationStatus;
	}
	public void setAdministrationStatus(String administrationStatus) {
		this.administrationStatus = administrationStatus;
	}
	public String getAdministrationInstitution() {
		return administrationInstitution;
	}
	public void setAdministrationInstitution(String administrationInstitution) {
		this.administrationInstitution = administrationInstitution;
	}
	public String getAdministrationAprovedBy() {
		return administrationAprovedBy;
	}
	public void setAdministrationAprovedBy(String administrationAprovedBy) {
		this.administrationAprovedBy = administrationAprovedBy;
	}
	public Date getAdministrationAprovedDate() {
		return administrationAprovedDate;
	}
	public void setAdministrationAprovedDate(Date administrationAprovedDate) {
		this.administrationAprovedDate = administrationAprovedDate;
	}
	public String getAdministrationAprovedFile() {
		return administrationAprovedFile;
	}
	public void setAdministrationAprovedFile(String administrationAprovedFile) {
		this.administrationAprovedFile = administrationAprovedFile;
	}
	public Date getAdministrationRetainDate() {
		return administrationRetainDate;
	}
	public void setAdministrationRetainDate(Date administrationRetainDate) {
		this.administrationRetainDate = administrationRetainDate;
	}
	public String getStaffLevel() {
		return staffLevel;
	}
	public void setStaffLevel(String staffLevel) {
		this.staffLevel = staffLevel;
	}
	public Date getStaffLevedAprovedDate() {
		return staffLevedAprovedDate;
	}
	public void setStaffLevedAprovedDate(Date staffLevedAprovedDate) {
		this.staffLevedAprovedDate = staffLevedAprovedDate;
	}
	public String getStaffLevedAprovedFile() {
		return staffLevedAprovedFile;
	}
	public void setStaffLevedAprovedFile(String staffLevedAprovedFile) {
		this.staffLevedAprovedFile = staffLevedAprovedFile;
	}
	public String getStaffLevedAprovedCompany() {
		return staffLevedAprovedCompany;
	}
	public void setStaffLevedAprovedCompany(String staffLevedAprovedCompany) {
		this.staffLevedAprovedCompany = staffLevedAprovedCompany;
	}
	public String getTechnicalDuty() {
		return technicalDuty;
	}
	public void setTechnicalDuty(String technicalDuty) {
		this.technicalDuty = technicalDuty;
	}
	public String getTechnicalTitle() {
		return technicalTitle;
	}
	public void setTechnicalTitle(String technicalTitle) {
		this.technicalTitle = technicalTitle;
	}
	public String getTechnicalDutyCompany() {
		return technicalDutyCompany;
	}
	public void setTechnicalDutyCompany(String technicalDutyCompany) {
		this.technicalDutyCompany = technicalDutyCompany;
	}
	public Date getTechnicalDutyEvaluateDate() {
		return technicalDutyEvaluateDate;
	}
	public void setTechnicalDutyEvaluateDate(Date technicalDutyEvaluateDate) {
		this.technicalDutyEvaluateDate = technicalDutyEvaluateDate;
	}
	public Date getTechnicalDutyEndDate() {
		return technicalDutyEndDate;
	}
	public void setTechnicalDutyEndDate(Date technicalDutyEndDate) {
		this.technicalDutyEndDate = technicalDutyEndDate;
	}
	public String getTechnicalDutyLevel() {
		return technicalDutyLevel;
	}
	public void setTechnicalDutyLevel(String technicalDutyLevel) {
		this.technicalDutyLevel = technicalDutyLevel;
	}
	public Date getTechnicalDutyBeginDate() {
		return technicalDutyBeginDate;
	}
	public void setTechnicalDutyBeginDate(Date technicalDutyBeginDate) {
		this.technicalDutyBeginDate = technicalDutyBeginDate;
	}
	public String getTechnicalTitleMajor() {
		return technicalTitleMajor;
	}
	public void setTechnicalTitleMajor(String technicalTitleMajor) {
		this.technicalTitleMajor = technicalTitleMajor;
	}
	public String getDutyCompany() {
		return dutyCompany;
	}
	public void setDutyCompany(String dutyCompany) {
		this.dutyCompany = dutyCompany;
	}
	public String getDutyChangeType() {
		return dutyChangeType;
	}
	public void setDutyChangeType(String dutyChangeType) {
		this.dutyChangeType = dutyChangeType;
	}
	public String getDutyChangeFile() {
		return dutyChangeFile;
	}
	public void setDutyChangeFile(String dutyChangeFile) {
		this.dutyChangeFile = dutyChangeFile;
	}
	public String getHasKnowledgeInnovation() {
		return hasKnowledgeInnovation;
	}
	public void setHasKnowledgeInnovation(String hasKnowledgeInnovation) {
		this.hasKnowledgeInnovation = hasKnowledgeInnovation;
	}
	public String getHasArchive() {
		return hasArchive;
	}
	public void setHasArchive(String hasArchive) {
		this.hasArchive = hasArchive;
	}
	public String getHasContract() {
		return hasContract;
	}
	public void setHasContract(String hasContract) {
		this.hasContract = hasContract;
	}
	public String getDutyName() {
		return dutyName;
	}
	public void setDutyName(String dutyName) {
		this.dutyName = dutyName;
	}
	public String getDutyLevel() {
		return dutyLevel;
	}
	public void setDutyLevel(String dutyLevel) {
		this.dutyLevel = dutyLevel;
	}
	public Date getDutyDate() {
		return dutyDate;
	}
	public void setDutyDate(Date dutyDate) {
		this.dutyDate = dutyDate;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
