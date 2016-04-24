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

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.duckling.dhome.dao.TempField;

/**
 * @group: net.duckling
 * @title: SimpleUser.java
 * @description: 用户基本信息，会经常使用
 * @author clive
 * @date 2012-8-4 下午6:18:52
 */

public class SimpleUser implements Serializable{
//	public SimpleUser() {
//		super();
//	}
//
//	public SimpleUser(int id, String zhName, String email) {
//		super();
//		this.id = id;
//		this.zhName = zhName;
//		this.email = email;
//	}
	/**
	 * 
	 */
	@TempField
	private static final long serialVersionUID = 1L;
	/**
	 * @Fields id : 用户唯一数字标示
	 */
	private int id;
	/**
	 * @Fields zhName : 中文名字
	 */
	private String zhName;
	/**
	 * @Fields enName : 英文名字
	 */
	private String enName;
	/**
	 * @Fields email : 电子邮件
	 */
	private String email;
	/**
	 * @Fields imageId : 头像的clb数字标示
	 */
	private int image;
	/**
	 * @Fields entitle : 称呼语，如Mr Dr Mis Sir等
	 */
	private String salutation;
	
	/**
	 * @Fields step:
	 * */
	private String step;
	/**论文*/
	public static final String STEP_CONFIG_PAPER="configPaper";
	/**上传照片*/
	public static final String STEP_CONFIG_PHOTO="configPhoto";
	/**工作经历*/
	public static final String STEP_CONFIG_CAREER="configCareer";
	/**完成*/
	public static final String STEP_COMPLETE="complete";
	/**
	 * @Fields pinyin: 中文名称的拼音写法 全拼;首字母
	 * */
	private String pinyin;
	/**
	 * @Fields isAdmin: 是否是管理员账户 ，请用true和false控制
	 * */
	private Boolean isAdmin;
	/**
	 * @Fields status: 审核状态
	 * */
	private String status;
	/**待审核*/
	public static final String STATUS_AUDIT_NEED="auditNeed";
	/**审核中*/
	public static final String STATUS_AUDIT_ING="auditIng";
	/**审核未通过*/
	public static final String STATUS_AUDIT_NOT="auditNot";
	/**审核通过*/
	public static final String STATUS_AUDIT_OK="auditOK";
	/**删除*/
	public static final String STATUS_AUDIT_DELETE="auditDelete";
	
	/**
	 * @Fields auditPropose:审核建议
	 * */
	private String auditPropose;
	
	/**最后更新时间*/
	private Date lastEditTime;
	
	/**
	 * 用户所属组织ID，值为null表示该用户不属于任何组织.
	 */
	private Integer institutionId;
	
	/**是否迁移*/
	private Integer isMove;
	
	public Date getLastEditTime() {
		return lastEditTime==null?null:(Date)lastEditTime.clone();
	}

	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = (lastEditTime==null)?null:(Date)lastEditTime.clone();
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public String getAuditPropose() {
		return auditPropose;
	}

	public void setAuditPropose(String auditPropose) {
		this.auditPropose = auditPropose;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getZhName() {
		return zhName;
	}

	public void setZhName(String zhName) {
		this.zhName = zhName;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int imageId) {
		this.image = imageId;
	}
	
	public Integer getIsMove() {
		return isMove;
	}

	public void setIsMove(Integer isMove) {
		this.isMove = isMove;
	}

	/**
	 * 获取当前可用的用户审核状态集合
	 * @return
	 */
	public static Set<String> getUserAuditStatus(){
		Set<String> result = new HashSet<String>();
		result.add(STATUS_AUDIT_DELETE);
		result.add(STATUS_AUDIT_NEED);
		result.add(STATUS_AUDIT_ING);
		result.add(STATUS_AUDIT_NOT);
		result.add(STATUS_AUDIT_OK);
		return result;
	}
	
	public boolean isAuditDeleted(){
		return STATUS_AUDIT_DELETE.equals(this.status);
	}
	public boolean registInProcess(){
		return !STEP_COMPLETE.equals(this.step)&&this.step!=null;
	}

	public Integer getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(Integer institutionId) {
		this.institutionId = institutionId;
	}
}
