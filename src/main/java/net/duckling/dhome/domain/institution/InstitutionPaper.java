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

import java.util.List;
import java.util.Map;

import net.duckling.dhome.common.dsn.DsnClient;

import org.apache.log4j.Logger;

/**
 * 机构论文（大气所）
 * */
public class InstitutionPaper {
	public static final String PAPER_ID = "id";
	/**
	 * id
	 * */	
	private int id;
	private int institutionId;
	/**
	 * 标题
	 * */
	private String title;
	/**
	 * doi
	 * */
	private String doi;
	/**
	 * issn
	 * */
	private String issn;
	/**
	 * 刊物名称
	 * */
	private String publicationName;
	private int publicationId;


	/**
	 * 发表年份
	 * */
	private int publicationYear;
	
	/**
	 * 发表月份
	 * */
	private int publicationMonth;
	
	
	/**
	 * 起始页
	 * */
	private int startPage;
	
	/**
	 * 中止页
	 * */
	private int endPage;
	
	private String publicationPage;
	
	
	/**
	 * 卷号
	 * */
	private String volumeNumber;
	
	
	/**
	 * 期号
	 * */
	private String series;
	
	/**
	 * 学科方向
	 * */
	private int disciplineOrientationId;
	
	
	/**
	 * 关键字
	 * */
	private String keywordDisplay;
	
	/*
	 * 资助单位
	 */
	private String sponsor;
	
	/**
	 * 摘要
	 * */
	private String summary;
	
	/**
	 * 原文标题
	 * */
	private String originalFileName;
	
	public String getOriginalFileName() {
		return originalFileName;
	}


	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}


	/**
	 * 原文链接
	 * */
	private String originalLink;
	
	/**
	 * 引用次数
	 * */
	private String citation;
	
	/**
	 * 引用次数查询时间
	 * */
	private String citationQueryTime;
	
	/**
	 * 原文clbId
	 * */
	private int clbId;
	
	
	/**
	 * 年度奖励标示
	 * */
	private String annualAwardMarks;
	
	
	/**
	 * 绩效计算年份
	 * */
	private int performanceCalculationYear;
	
	/**
	 * Hash值
	 * */
	private String hash;
	
	InstitutionPublication publication;  //刊物
	List<InstitutionAuthor> authors; //作者
	private String author;  //全部作者拼接
	private String departName;//部门名
	private int departId;
	private String authorAmount;
	private String ifs;
	private String awardStandard;
	private String payTotal;
	private String remark;
	private int creator;
	private String creatorName;
	
	private int status;
	
	public InstitutionPublication getPublication() {
		return publication;
	}


	public void setPublication(InstitutionPublication publication) {
		this.publication = publication;
	}


	public List<InstitutionAuthor> getAuthors() {
		return authors;
	}


	public void setAuthors(List<InstitutionAuthor> authors) {
		this.authors = authors;
	}


	public String getHash() {
		return hash;
	}


	public void setHash(String hash) {
		this.hash = hash;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	public String getDoi() {
		return doi;
	}


	public void setDoi(String doi) {
		this.doi = doi;
	}


	public String getIssn() {
		return issn;
	}


	public void setIssn(String issn) {
		this.issn = issn;
	}


	public int getPublicationYear() {
		return publicationYear;
	}


	public void setPublicationYear(int publicationYear) {
		this.publicationYear = publicationYear;
	}


	public int getPublicationMonth() {
		return publicationMonth;
	}


	public void setPublicationMonth(int publicationMonth) {
		this.publicationMonth = publicationMonth;
	}


	public int getStartPage() {
		return startPage;
	}


	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}


	public int getEndPage() {
		return endPage;
	}


	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}


	public String getVolumeNumber() {
		return volumeNumber;
	}


	public void setVolumeNumber(String volumeNumber) {
		this.volumeNumber = volumeNumber;
	}


	public String getSeries() {
		return series;
	}


	public void setSeries(String series) {
		this.series = series;
	}


	public int getDisciplineOrientationId() {
		return disciplineOrientationId;
	}


	public void setDisciplineOrientationId(int disciplineOrientationId) {
		this.disciplineOrientationId = disciplineOrientationId;
	}


	public String getKeywordDisplay() {
		return keywordDisplay;
	}


	public void setKeywordDisplay(String keyword) {
		this.keywordDisplay = keyword;
	}


	public String getSummary() {
		return summary;
	}


	public void setSummary(String summary) {
		this.summary = summary;
	}


	public String getOriginalLink() {
		return originalLink;
	}


	public void setOriginalLink(String originalLink) {
		this.originalLink = originalLink;
	}


	public String getCitation() {
		return citation;
	}
	public int getCitationInt(){
		try{
			return Integer.parseInt(citation);
		}catch(RuntimeException e){
			return 0;
		}
	}


	public void setCitation(String citation) {
		this.citation = citation;
	}


	public String getCitationQueryTime() {
		return "".equals(citationQueryTime)?null:citationQueryTime;
	}


	public void setCitationQueryTime(String citationQueryTime) {
		this.citationQueryTime = citationQueryTime;
	}


	public int getClbId() {
		return clbId;
	}


	public void setClbId(int clbId) {
		this.clbId = clbId;
	}


	public String getAnnualAwardMarks() {
		return annualAwardMarks;
	}


	public void setAnnualAwardMarks(String annualAwardMarks) {
		this.annualAwardMarks = annualAwardMarks;
	}


	public int getPerformanceCalculationYear() {
		return performanceCalculationYear;
	}

	public void setPerformanceCalculationYear(int performanceCalculationYear) {
		this.performanceCalculationYear = performanceCalculationYear;
	}
	public int getPublicationId() {
		return publicationId;
	}


	public void setPublicationId(int publicationId) {
		this.publicationId = publicationId;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public String getSponsor() {
		return sponsor;
	}


	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}


	public String getDepartName() {
		return departName;
	}


	public void setDepartName(String departName) {
		this.departName = departName;
	}
	public String getPublicationPage() {
		return publicationPage;
	}
	public void setPublicationPage(String publicationPage) {
		this.publicationPage = publicationPage;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getPublicationName() {
		return publicationName;
	}
	public void setPublicationName(String publicationName) {
		this.publicationName = publicationName;
	}
	public int getDepartId() {
		return departId;
	}
	public void setDepartId(int departId) {
		this.departId = departId;
	}


	public String getAuthorAmount() {
		return authorAmount;
	}


	public void setAuthorAmount(String authorAmount) {
		this.authorAmount = authorAmount;
	}
	public int getAuthorAmountInt(){
		try{
			return Integer.parseInt(authorAmount);
		}catch(RuntimeException e){
			return 0;
		}
	}

	public String getIfs() {
		return ifs;
	}


	public void setIfs(String ifs) {
		this.ifs = ifs;
	}


	public String getAwardStandard() {
		return awardStandard;
	}


	public void setAwardStandard(String awardStandard) {
		this.awardStandard = awardStandard;
	}


	public String getPayTotal() {
		return payTotal;
	}


	public void setPayTotal(String payTotal) {
		this.payTotal = payTotal;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public int getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(int institutionId) {
		this.institutionId = institutionId;
	}
	public int getCreator() {
		return creator;
	}
	public void setCreator(int creator) {
		this.creator = creator;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	
}
