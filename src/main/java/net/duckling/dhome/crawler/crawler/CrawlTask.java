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
package net.duckling.dhome.crawler.crawler;


/**
 * 这个类是采集任务类
 * @author juan
 */
public class CrawlTask {
	private int crawlTaskId;
	private String query;
	private String sourceUrl;
	private String sourceName;
	private String timelag; // 更新间隔
	private String type = "TI"; //类型 TS 主题 DO doi
	private String sysTag;
	private String status;
	private String startYear;
	private String endYear;
	private String fetchStatus;  //抓全文的状态
	private int threads;
    private int isCrawlFullTextUrl;
    
    public static final String TS="TS";
    public static final String DO="DO";
    public static final String TI="TI";
    
	
	public int getCrawlTaskId() {
		return crawlTaskId;
	}

	public void setCrawlTaskId(int crawlTaskId) {
		this.crawlTaskId = crawlTaskId;
	}

	public String getQuery() {
		//TI抓取方式标题不能包含特别的关键字
		if(TI.equals(this.getType())){
			return removeWord(query);
		}
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStartYear() {
		return startYear;
	}

	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}

	public String getEndYear() {
		return endYear;
	}

	public void setEndYear(String endYear) {
		this.endYear = endYear;
	}
	

	public String getTimelag() {
		return timelag;
	}

	public void setTimelag(String timelag) {
		this.timelag = timelag;
	}

	public String getSysTag() {
		return sysTag;
	}

	public void setSysTag(String sysTag) {
		this.sysTag = sysTag;
	}

	public String getFetchStatus() {
		return fetchStatus;
	}

	public void setFetchStatus(String fetchStatus) {
		this.fetchStatus = fetchStatus;
	}

	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}

	public int getIsCrawlFullTextUrl() {
		return isCrawlFullTextUrl;
	}

	public void setIsCrawlFullTextUrl(int isCrawlFullTextUrl) {
		this.isCrawlFullTextUrl = isCrawlFullTextUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 过滤单词and|in|or|not|near|same|atmosphere，不区分大小写
	 * @param word
	 * @return
	 */
	private String removeWord(String word){
		return word.replaceAll("(^|\\s)(?i)(and|in|or|not|near|same|atmosphere)($|\\s)", " ");
	}
}
