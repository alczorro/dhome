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
package net.duckling.dhome.domain.object;

import java.io.Serializable;

/**
 * 机构论文统计数据
 * @author Yangxp
 * @since 2012-03-21
 */
public class PaperStatistics implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int institutionId; //机构ID
	private int year; //年份
	private int count; //论文数
	private int timeCited; // 被引频次
	
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
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getTimeCited() {
		return timeCited;
	}
	public void setTimeCited(int timeCited) {
		this.timeCited = timeCited;
	}
	
	public static PaperStatistics build(int institutionId, int year, int count, int timeCited){
		PaperStatistics ps = new PaperStatistics();
		ps.setInstitutionId(institutionId);
		ps.setYear(year);
		ps.setCount(count);
		ps.setTimeCited(timeCited);
		return ps;
	}
	
}
