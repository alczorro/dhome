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


/**
 * @group: net.duckling
 * @title: Paper.java
 * @description: 论文
 * @author clive
 * @date 2012-8-4 下午5:58:16
 */
public class Paper {

	/**
	  * @Fields id : 发表物唯一数字标示
	  */
	private int id;
	/**
	 * @Fields uid : 发表物所属用户的ID
	 */
	private int uid;
	/**
	  * @Fields title : 发表物名称
	  */
	private String title;
	/**
	  * @Fields authors : 作者列表
	  */
	private String authors;
	/**
	  * @Fields source : 发表源
	  */
	private String source;
	/**
	  * @Fields volume : 卷号(期刊号)
	  */
	private String volumeIssue;

	/**
	  * @Fields publishedTime : 发表时间
	  */
	private String publishedTime;
	/**
	 * @Fields timeCited : 被引用次数
	 */
	private int timeCited;

	/**
	  * @Fields abstraction : 摘要
	  */
	private String summary;
	/**
	 * @Fields language ：发表物所用语言
	 */
	private String language;
	/**
	 * @Fields authorKeywords : 关键词
	 */
	private String keywords;
	/**
	  * @Fields localFulltextURL : 本地全文URL
	  */
	private String localFulltextURL;
	/**
	  * @Fields remoteFulltextURL : 远程全文URL
	  */
	private String paperURL;
	/**
	 * @Fields clbId : 发表物存储在CLB中的ID
	 */
	private int clbId;
	
	/**
	  * @Fields sequence : 发表物的显示顺序
	  */
	private int sequence;
	/**
	 * @Fields dsnPaperId ：发表物在DSN中的ID
	 */
	private long dsnPaperId;
	/**
	 * @Fields pages：发表物的页数
	 */
	private String pages;
	/**
	 * @Fields year：发表年份
	 */
	private int year;
	
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthors() {
		return authors;
	}
	public void setAuthors(String authors) {
		this.authors = authors;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getVolumeIssue() {
		return volumeIssue;
	}
	public void setVolumeIssue(String volumeIssue) {
		this.volumeIssue = volumeIssue;
	}
	public String getPublishedTime() {
		return publishedTime;
	}
	public void setPublishedTime(String publishedTime) {
		this.publishedTime = publishedTime;
	}
	public int getTimeCited() {
		return timeCited;
	}
	public void setTimeCited(int timeCited) {
		this.timeCited = timeCited;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getLocalFulltextURL() {
		return localFulltextURL;
	}
	public void setLocalFulltextURL(String localFulltextURL) {
		this.localFulltextURL = localFulltextURL;
	}
	public String getPaperURL() {
		return paperURL;
	}
	public void setPaperURL(String paperURL) {
		this.paperURL = paperURL;
	}
	public int getClbId() {
		return clbId;
	}
	public void setClbId(int clbId) {
		this.clbId = clbId;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public long getDsnPaperId() {
		return dsnPaperId;
	}
	public void setDsnPaperId(long dsnPaperId) {
		this.dsnPaperId = dsnPaperId;
	}
	public String getPages() {
		return pages;
	}
	public void setPages(String pages) {
		this.pages = pages;
	}
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	/**
	 * 生成Paper对象
	 * @param uid 用户ID
	 * @param title 标题
	 * @param authors 作者
	 * @param source 期刊/会议
	 * @param volumeIssue 期卷号
	 * @param publishedTime 发表时间
	 * @param timeCited 引用次数
	 * @param summary 摘要
	 * @param language 语言
	 * @param keywords 关键词
	 * @param localFulltextURL 本地URL
	 * @param paperURL 论文链接
	 * @param clbId CLB中的ID
	 * @param sequence 排序编号
	 * @param dsnPaperId DSN中的Paper ID
	 * @return
	 */
	public static Paper build(int uid, String title, String authors,
			String source, String volumeIssue, String publishedTime, String timeCited,
			String summary, String language, String keywords, String localFulltextURL,
			String paperURL, int clbId, int sequence, long dsnPaperId, String pages){
		Paper paper = new Paper();
		paper.setUid(uid);
		paper.setTitle(title);
		paper.setAuthors(authors);
		paper.setSource(source);
		paper.setVolumeIssue(volumeIssue);
		paper.setPublishedTime(publishedTime);
		int tc = (null==timeCited || "".equals(timeCited))?0:Integer.parseInt(timeCited);
		paper.setTimeCited(tc);
		paper.setSummary(summary);
		paper.setLanguage(language);
		paper.setKeywords(keywords);
		paper.setLocalFulltextURL(localFulltextURL);
		paper.setPaperURL(paperURL);
		paper.setClbId(clbId);
		paper.setSequence(sequence);
		paper.setDsnPaperId(dsnPaperId);
		paper.setPages(pages);
		return paper;
	}
}
