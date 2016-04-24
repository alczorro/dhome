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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 奖助学金
 * @author Brett
 *
 */
public class InstitutionGrants {

	private Integer id;
	private Integer institutionId;
	private Integer userId;
	private Integer studentId;
	/**
	 * 课题号
	 */
	private String topicNo; 
	private String studentName; 
	private String className; 
	/**
	 * 学位
	 */
	private Integer degree; 
	/**
	 * 研究所奖学金（研究所支付）
	 */
	private BigDecimal scholarship1; 
	/**
	 * 研究所奖学金（课题支付）
	 */
	private BigDecimal scholarship2;
	/**
	 * 助研酬金（课题支付）
	 */
	private BigDecimal assistantFee;
	/**
	 * 总计
	 */
	private BigDecimal sumFee;
	private Date startTime;
	private Date endTime;
	
	private String tutor;

	/**
	 * 0新建状态 1已发放
	 */
	private int status;
	/**
	 * 处理批号
	 */
	private String batchNo;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getInstitutionId() {
		return institutionId;
	}
	public void setInstitutionId(Integer institutionId) {
		this.institutionId = institutionId;
	}
	public Integer getStudentId() {
		return studentId;
	}
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getTopicNo() {
		return topicNo;
	}
	public void setTopicNo(String topicNo) {
		this.topicNo = topicNo;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Integer getDegree() {
		return degree;
	}
	
	public String getDegreeName(){
		if(degree == 1){
			return "本科";
		}else if(degree == 2){
			return "硕士";
		}else if(degree == 3){
			return "博士";
		}
		
		return "未知";
	}
	public void setDegree(Integer degree) {
		this.degree = degree;
	}
	public BigDecimal getScholarship1() {
		return scholarship1;
	}
	public void setScholarship1(BigDecimal scholarship1) {
		this.scholarship1 = scholarship1;
	}
	public BigDecimal getScholarship2() {
		return scholarship2;
	}
	public void setScholarship2(BigDecimal scholarship2) {
		this.scholarship2 = scholarship2;
	}
	public BigDecimal getAssistantFee() {
		return assistantFee;
	}
	public void setAssistantFee(BigDecimal assistantFee) {
		this.assistantFee = assistantFee;
	}
	public BigDecimal getSumFee() {
		return sumFee;
	}
	public void setSumFee(BigDecimal sumFee) {
		this.sumFee = sumFee;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getTutor() {
		return tutor;
	}
	public void setTutor(String tutor) {
		this.tutor = tutor;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	
	/**
	 * 生成批号
	 * @param userId
	 * @return
	 */
	public static String generateBatchNo(){
		Random rand = new Random();
		int num = rand.nextInt(999);
		String rn = String.format("%1$,04d", num);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhh");  
        String nowdate = sdf.format(new Date()); 
        return nowdate + rn;
	}
}