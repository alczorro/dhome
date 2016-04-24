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
package net.duckling.dhome.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.dao.IInstitutionMemberFromVmtDAO;
import net.duckling.dhome.domain.institution.InstitutionMemberFromVmt;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.vmt.api.domain.VmtUser;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class InstitutionMemberFromVmtDAOImpl extends BaseDao implements IInstitutionMemberFromVmtDAO {
	@Override
	public void insert(InstitutionMemberFromVmt vmtMember) {
		List<InstitutionMemberFromVmt> ms=new ArrayList<InstitutionMemberFromVmt>();
		ms.add(vmtMember);
		insert(ms);
	}
	@Override
	public void displayChanged(final int institutionId, final List<Integer> sonDeptId, final String display) {
		String sql="update institution_member_from_vmt set display=? where depart_id=? and institution_id=?";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return sonDeptId.size();
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setString(++i, display);
						pst.setInt(++i, sonDeptId.get(index));
						pst.setInt(++i, institutionId);
					}

				});
	}
	@Override
	public void removeAllMemberByInsId(int institutionId) {
		String sql="delete from institution_member_from_vmt where institution_id=:insId";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("insId", institutionId);
		getNamedParameterJdbcTemplate().update(sql, param);
		
	}
	@Override
	public void insert(List<InstitutionMemberFromVmt> vmtMember) {
		if(CommonUtils.isNull(vmtMember)){
			return;
		}
		StringBuffer sb=new StringBuffer();
		sb.append("insert into institution_member_from_vmt(");
		sb.append("`institution_id`,");
		sb.append("`cstnet_id`,");
		sb.append("`umt_id`,");
		sb.append("`true_name`,");
		sb.append("`display`,");
		sb.append("`user_type`,");
		sb.append("`sex`").append(",");
		sb.append("`office_address`").append(",");
		sb.append("`office_telephone`").append(",");
		sb.append("`mobile_phone`").append(",");
		sb.append("`title`").append(",");
		sb.append("`uid`,");
		sb.append("`depart_id`)");
		sb.append(" values");
		int index=0;
		Map<String,Object> params=new HashMap<String,Object>();
		for(InstitutionMemberFromVmt vmt:vmtMember){
			sb.append("(");
			sb.append(":insId"+index).append(",");
			sb.append(":cstnetId"+index).append(",");
			sb.append(":umtId"+index).append(",");
			sb.append(":trueName"+index).append(",");
			sb.append(":display"+index).append(",");
			sb.append(":user_type"+index).append(",");
			sb.append(":sex"+index).append(",");
			sb.append(":office_address"+index).append(",");
			sb.append(":office_telephone"+index).append(",");
			sb.append(":mobile_phone"+index).append(",");
			sb.append(":title"+index).append(",");
			sb.append(":uid"+index).append(",");
			sb.append(":departId"+index);
			sb.append("),");
			params.put("insId"+index, vmt.getInstitutionId());
			params.put("cstnetId"+index,vmt.getCstnetId());
			params.put("umtId"+index, vmt.getUmtId());
			params.put("trueName"+index, vmt.getTrueName());
			params.put("display"+index, vmt.getDisplay());
			params.put("user_type"+index,vmt.getUserType());
			params.put("sex"+index,vmt.getSex());
			params.put("office_address"+index,vmt.getOfficeAddress());
			params.put("office_telephone"+index,vmt.getOfficeTelephone());
			params.put("mobile_phone"+index,vmt.getMobilePhone());
			params.put("title"+index,vmt.getTitle());
			params.put("departId"+index,vmt.getDepartId());
			params.put("uid"+index,vmt.getUid());
			index++;
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		getNamedParameterJdbcTemplate().update(sb.toString(),params);
	}
	@Override
	public void removeMembersByUmtIds(int insId,List<String> userUmtIds) {
		if(CommonUtils.isNull(userUmtIds)){
			return;
		}
		String sql="delete from institution_member_from_vmt where institution_id=:insId and umt_id in(";
		int index=0;
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		for(String umtId:userUmtIds){
			sql+=":umtId"+index+",";
			paramMap.put("umtId"+index, umtId);
			index++;
		}
		sql=sql.substring(0,sql.length()-1);
		sql+=")";
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}
	
	@Override
	public void moveTo(final int insId, final List<String> userUmtIds, final int departId) {
		String sql="update institution_member_from_vmt set depart_id=? ";
		sql+=" where institution_id=? ";
		sql+=" and umt_id=? ";
		
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return userUmtIds.size();
					}

					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i, departId);
						pst.setInt(++i, insId);
						pst.setString(++i, userUmtIds.get(index));
					}

				});
	}
	@Override
	public void updateVmtUser(final int insId,final List<VmtUser> users) {
		String sql="update institution_member_from_vmt "
				+"set true_name=?,"
				+"display=?,"
				+"sex=?,"
				+"office_address=?,"
				+"office_telephone=?,"
				+"mobile_phone=?,"
				+"title=? "
				+"where umt_id=? and institution_id=? ";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return users.size();
					}

					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setString(++i,users.get(index).getName());
						pst.setString(++i, users.get(index).isVisible()?InstitutionMemberFromVmt.DISPLAY_SHOW:InstitutionMemberFromVmt.DISPLAY_HIDE);
						pst.setString(++i, users.get(index).getSex());
						pst.setString(++i, users.get(index).getOffice());
						pst.setString(++i,users.get(index).getOfficePhone());
						pst.setString(++i, users.get(index).getTelephone());
						pst.setString(++i, users.get(index).getTitle());
						pst.setString(++i, users.get(index).getUmtId());
						pst.setInt(++i, insId);
					}
				});
	}
	
	@Override
	public void updateMember(final List<InstitutionMemberFromVmt> member) {
		String sql="update institution_member_from_vmt "
				+"set sex=?,"
				+"institution_id=?,"
				+"true_name=?,"
				+"depart_id=?,"
				+"office_address=?,"
				+"office_telephone=?,"
				+"mobile_phone=?,"
				+"sn=?,"
				+"birth=?,"
				+"birth_place=?,"
				+"age=?,"
				+"job_age=?,"
				+"job_type=?,"
				+"level_code=?,"
				+"job_level=?,"
				+"salary_level=?,"
				+"home_address=?,"
				+"job_date=?,"
				+"political_status=?,"
				+"duty_type=?,"
				+"job_status=?,"
				+"duty_grade=?,"
				+"salary_base=?,"
				+"salary_job=?,"
				+"retire_date=?,"
				+"leave_date=?,"
				+"party_date=?,"
				+"graduate_school=?,"
				+"graduate_date=?,"
				+"major=?,"
				+"highest_graduate_code=?,"
				+"highest_graduate=?,"
				+"highest_degree_code=?,"
				+"highest_degree=?,"
				+"degree_date=?,"
				+"degree_company=?,"
				+"company_join_date=?,"
				+"company_join_way=?,"
				+"industry_join_date=?,"
				+"district_join_date=?,"
				+"company_join_status=?,"
				+"company_join_type=?,"
				+"company_before=?,"
				+"company_before_relation=?,"
				+"company_before_type=?,"
				+"district_before=?,"
				+"administration_duty=?,"
				+"administration_duty_desc=?,"
				+"administration_status=?,"
				+"administration_institution=?,"
				+"administration_aproved_by=?,"
				+"administration_aproved_date=?,"
				+"administration_aproved_file=?,"
				+"administration_retain_date=?,"
				+"staff_level=?,"
				+"staff_leved_aproved_date=?,"
				+"staff_leved_aproved_file=?,"
				+"staff_leved_aproved_company=?,"
				+"technical_duty=?,"
				+"technical_title=?,"
				+"technical_duty_company=?,"
				+"technical_duty_evaluate_date=?,"
				+"technical_duty_end_date=?,"
				+"technical_duty_level=?,"
				+"technical_duty_begin_date=?,"
				+"technical_title_major=?,"
				+"duty_company=?,"
				+"duty_change_type=?,"
				+"duty_change_file=?,"
				+"has_knowledge_innovation=?,"
				+"has_archive=?,"
				+"has_contract=?,"
				+"duty_name=?,"
				+"duty_level=?,"
				+"duty_date=? "
				+"where cstnet_id=lower(?) ";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return member.size();
					}

					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setString(++i, member.get(index).getSex());
						pst.setInt(++i, member.get(index).getInstitutionId());
						pst.setString(++i, member.get(index).getTrueName());
						pst.setInt(++i, member.get(index).getDepartId());
						pst.setString(++i, member.get(index).getOfficeAddress());
						pst.setString(++i, member.get(index).getOfficeTelephone());
						pst.setString(++i,member.get(index).getMobilePhone());
						
						pst.setString(++i,member.get(index).getSn());
						if(member.get(index).getBirth()==null){
							pst.setDate(++i, null);
						}else{
							pst.setDate(++i,new java.sql.Date(member.get(index).getBirth().getTime()));
						}
						pst.setString(++i, member.get(index).getBirthPlace());
						pst.setInt(++i, member.get(index).getAge());
						pst.setInt(++i,member.get(index).getJobAge());
						pst.setString(++i, member.get(index).getJobType());
						pst.setString(++i, member.get(index).getLevelCode());
						pst.setString(++i, member.get(index).getJobLevel());
						
						pst.setString(++i,member.get(index).getSalaryLevel());
						pst.setString(++i, member.get(index).getHomeAddress());
						if(member.get(index).getJobDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i,new java.sql.Date(member.get(index).getJobDate().getTime()));
						}
						pst.setString(++i, member.get(index).getPoliticalStatus());
						pst.setString(++i,member.get(index).getDutyType());
						pst.setString(++i, member.get(index).getJobStatus());
						pst.setString(++i, member.get(index).getDutyGrade());
						if(member.get(index).getSalaryBase()==null||member.get(index).getSalaryBase().equals("")){
							pst.setString(++i, "0");
						}else{
							pst.setString(++i, member.get(index).getSalaryBase());
						}
						if(member.get(index).getSalaryJob()==null||member.get(index).getSalaryJob().equals("")){
							pst.setString(++i, "0");
						}else{
							pst.setString(++i,member.get(index).getSalaryJob());
						}
						if(member.get(index).getRetireDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i,new java.sql.Date( member.get(index).getRetireDate().getTime()));
						}
						if(member.get(index).getLeaveDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getLeaveDate().getTime()));
						}
						if(member.get(index).getPartyDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getPartyDate().getTime()));
						}
						pst.setString(++i, member.get(index).getGraduateSchool());
						if(member.get(index).getGraduateDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getGraduateDate().getTime()));
						}
						pst.setString(++i, member.get(index).getMajor());
						pst.setString(++i, member.get(index).getHighestGraduateCode());
						pst.setString(++i, member.get(index).getHighestGraduate());
						
						pst.setString(++i, member.get(index).getHighestDegreeCode());
						pst.setString(++i, member.get(index).getHighestDegree());
						if(member.get(index).getDegreeDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getDegreeDate().getTime()));
						}
						pst.setString(++i, member.get(index).getDegreeCompany());
						if( member.get(index).getCompanyJoinDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getCompanyJoinDate().getTime()));
						}
						pst.setString(++i,member.get(index).getCompanyJoinWay());
						if( member.get(index).getIndustryJoinDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getIndustryJoinDate().getTime()));
						}
						if(member.get(index).getDistrictJoinDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getDistrictJoinDate().getTime()));
						}
						pst.setString(++i, member.get(index).getCompanyJoinStatus());
						pst.setString(++i, member.get(index).getCompanyJoinType());
						pst.setString(++i,member.get(index).getCompanyBefore());
						pst.setString(++i, member.get(index).getCompanyBeforeRelation());
						pst.setString(++i, member.get(index).getCompanyBeforeType());
						pst.setString(++i, member.get(index).getDistrictBefore());
						
						
						pst.setString(++i, member.get(index).getAdministrationDuty());
						pst.setString(++i,member.get(index).getAdministrationDutyDesc());
						pst.setString(++i, member.get(index).getAdministrationStatus());
						pst.setString(++i, member.get(index).getAdministrationInstitution());
						pst.setString(++i, member.get(index).getAdministrationAprovedBy());
						if(member.get(index).getAdministrationAprovedDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getAdministrationAprovedDate().getTime()));
						}
						pst.setString(++i,member.get(index).getAdministrationAprovedFile());
						if(member.get(index).getAdministrationRetainDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getAdministrationRetainDate().getTime()));
						}
						pst.setString(++i, member.get(index).getStaffLevel());
						if(member.get(index).getStaffLevedAprovedDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getStaffLevedAprovedDate().getTime()));
						}
						pst.setString(++i,member.get(index).getStaffLevedAprovedFile());
						pst.setString(++i, member.get(index).getStaffLevedAprovedCompany());
						pst.setString(++i, member.get(index).getTechnicalDuty());
						pst.setString(++i, member.get(index).getTechnicalTitle());
						pst.setString(++i,member.get(index).getTechnicalDutyCompany());
						if(member.get(index).getTechnicalDutyEvaluateDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getTechnicalDutyEvaluateDate().getTime()));
						}
						if(member.get(index).getTechnicalDutyEndDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getTechnicalDutyEndDate().getTime()));
						}
						pst.setString(++i, member.get(index).getTechnicalDutyLevel());
						if( member.get(index).getTechnicalDutyBeginDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getTechnicalDutyBeginDate().getTime()));
						}
						pst.setString(++i,member.get(index).getTechnicalTitleMajor());
						pst.setString(++i, member.get(index).getDutyCompany());
						pst.setString(++i, member.get(index).getDutyChangeType());
						pst.setString(++i, member.get(index).getDutyChangeFile());
						pst.setString(++i, member.get(index).getHasKnowledgeInnovation());
						pst.setString(++i,member.get(index).getHasArchive());
						pst.setString(++i, member.get(index).getHasContract());
						pst.setString(++i, member.get(index).getDutyName());
						pst.setString(++i, member.get(index).getDutyLevel());
						if(member.get(index).getDutyDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getDutyDate().getTime()));
						}
						pst.setString(++i, member.get(index).getCstnetId());
					}
				});
	}
	@Override
	public void insertMembers(final List<InstitutionMemberFromVmt> member) {
		String sql="insert into institution_member_from_vmt(`institution_id`,`umt_id`,`true_name`,`display`,`depart_id`,`sex`,`office_address`," +
				"`office_telephone`,`mobile_phone`,`sn`,`birth`,`birth_place`,`age`,`job_age`,`job_type`,`level_code`,`job_level`," +
				"`salary_level`,`home_address`,`job_date`,`political_status`,`duty_type`,`job_status`,`duty_grade`,`salary_base`," +
				"`salary_job`,`retire_date`,`leave_date`,`party_date`,`graduate_school`,`graduate_date`,`major`,`highest_graduate_code`," +
				"`highest_graduate`,`highest_degree_code`,`highest_degree`,`degree_date`,`degree_company`,`company_join_date`," +
				"`company_join_way`,`industry_join_date`,`district_join_date`,`company_join_status`,`company_join_type`," +
				"`company_before`,`company_before_relation`,`company_before_type`,`district_before`,`administration_duty`," +
				"`administration_duty_desc`,`administration_status`,`administration_institution`,`administration_aproved_by`," +
				"`administration_aproved_date`,`administration_aproved_file`,`administration_retain_date`,`staff_level`," +
				"`staff_leved_aproved_date`,`staff_leved_aproved_file`,`staff_leved_aproved_company`,`technical_duty`,`technical_title`," +
				"`technical_duty_company`,`technical_duty_evaluate_date`,`technical_duty_end_date`,`technical_duty_level`," +
				"`technical_duty_begin_date`,`technical_title_major`,`duty_company`,`duty_change_type`,`duty_change_file`," +
				"`has_knowledge_innovation`,`has_archive`,`has_contract`,`duty_name`,`duty_level`,`duty_date`) values(?,?,?,?,?,?," +
				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY update "
				+"institution_id=?,"
				+"true_name=?,"
				+"display=?,"
				+"depart_id=?,"
				+"sex=?,"
				+"office_address=?,"
				+"office_telephone=?,"
				+"mobile_phone=?,"
				+"sn=?,"
				+"birth=?,"
				+"birth_place=?,"
				+"age=?,"
				+"job_age=?,"
				+"job_type=?,"
				+"level_code=?,"
				+"job_level=?,"
				+"salary_level=?,"
				+"home_address=?,"
				+"job_date=?,"
				+"political_status=?,"
				+"duty_type=?,"
				+"job_status=?,"
				+"duty_grade=?,"
				+"salary_base=?,"
				+"salary_job=?,"
				+"retire_date=?,"
				+"leave_date=?,"
				+"party_date=?,"
				+"graduate_school=?,"
				+"graduate_date=?,"
				+"major=?,"
				+"highest_graduate_code=?,"
				+"highest_graduate=?,"
				+"highest_degree_code=?,"
				+"highest_degree=?,"
				+"degree_date=?,"
				+"degree_company=?,"
				+"company_join_date=?,"
				+"company_join_way=?,"
				+"industry_join_date=?,"
				+"district_join_date=?,"
				+"company_join_status=?,"
				+"company_join_type=?,"
				+"company_before=?,"
				+"company_before_relation=?,"
				+"company_before_type=?,"
				+"district_before=?,"
				+"administration_duty=?,"
				+"administration_duty_desc=?,"
				+"administration_status=?,"
				+"administration_institution=?,"
				+"administration_aproved_by=?,"
				+"administration_aproved_date=?,"
				+"administration_aproved_file=?,"
				+"administration_retain_date=?,"
				+"staff_level=?,"
				+"staff_leved_aproved_date=?,"
				+"staff_leved_aproved_file=?,"
				+"staff_leved_aproved_company=?,"
				+"technical_duty=?,"
				+"technical_title=?,"
				+"technical_duty_company=?,"
				+"technical_duty_evaluate_date=?,"
				+"technical_duty_end_date=?,"
				+"technical_duty_level=?,"
				+"technical_duty_begin_date=?,"
				+"technical_title_major=?,"
				+"duty_company=?,"
				+"duty_change_type=?,"
				+"duty_change_file=?,"
				+"has_knowledge_innovation=?,"
				+"has_archive=?,"
				+"has_contract=?,"
				+"duty_name=?,"
				+"duty_level=?,"
				+"duty_date=? ";
				
				
				
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return member.size();
					}

					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i, member.get(index).getInstitutionId());
						pst.setString(++i, String.valueOf(member.get(index).getSn()));
						pst.setString(++i, member.get(index).getTrueName());
						pst.setString(++i, "show");
						pst.setInt(++i, member.get(index).getDepartId());
						pst.setString(++i, member.get(index).getSex());
						pst.setString(++i, member.get(index).getOfficeAddress());
						pst.setString(++i, member.get(index).getOfficeTelephone());
						pst.setString(++i,member.get(index).getMobilePhone());
						
						pst.setString(++i,member.get(index).getSn());
						if(member.get(index).getBirth()==null){
							pst.setDate(++i, null);
						}else{
							pst.setDate(++i,new java.sql.Date(member.get(index).getBirth().getTime()));
						}
						pst.setString(++i, member.get(index).getBirthPlace());
						pst.setInt(++i, member.get(index).getAge());
						pst.setInt(++i,member.get(index).getJobAge());
						pst.setString(++i, member.get(index).getJobType());
						pst.setString(++i, member.get(index).getLevelCode());
						pst.setString(++i, member.get(index).getJobLevel());
						
						pst.setString(++i,member.get(index).getSalaryLevel());
						pst.setString(++i, member.get(index).getHomeAddress());
						if(member.get(index).getJobDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i,new java.sql.Date(member.get(index).getJobDate().getTime()));
						}
						pst.setString(++i, member.get(index).getPoliticalStatus());
						pst.setString(++i,member.get(index).getDutyType());
						pst.setString(++i, member.get(index).getJobStatus());
						pst.setString(++i, member.get(index).getDutyGrade());
						if(member.get(index).getSalaryBase()==null||member.get(index).getSalaryBase().equals("")){
							pst.setString(++i, "0");
						}else{
							pst.setString(++i, member.get(index).getSalaryBase());
						}
						if(member.get(index).getSalaryJob()==null||member.get(index).getSalaryJob().equals("")){
							pst.setString(++i, "0");
						}else{
							pst.setString(++i,member.get(index).getSalaryJob());
						}
						
						if(member.get(index).getRetireDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i,new java.sql.Date( member.get(index).getRetireDate().getTime()));
						}
						if(member.get(index).getLeaveDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getLeaveDate().getTime()));
						}
						if(member.get(index).getPartyDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getPartyDate().getTime()));
						}
						pst.setString(++i, member.get(index).getGraduateSchool());
						if(member.get(index).getGraduateDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getGraduateDate().getTime()));
						}
						pst.setString(++i, member.get(index).getMajor());
						pst.setString(++i, member.get(index).getHighestGraduateCode());
						pst.setString(++i, member.get(index).getHighestGraduate());
						
						pst.setString(++i, member.get(index).getHighestDegreeCode());
						pst.setString(++i, member.get(index).getHighestDegree());
						if(member.get(index).getDegreeDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getDegreeDate().getTime()));
						}
						pst.setString(++i, member.get(index).getDegreeCompany());
						if( member.get(index).getCompanyJoinDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getCompanyJoinDate().getTime()));
						}
						pst.setString(++i,member.get(index).getCompanyJoinWay());
						if( member.get(index).getIndustryJoinDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getIndustryJoinDate().getTime()));
						}
						if(member.get(index).getDistrictJoinDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getDistrictJoinDate().getTime()));
						}
						pst.setString(++i, member.get(index).getCompanyJoinStatus());
						pst.setString(++i, member.get(index).getCompanyJoinType());
						pst.setString(++i,member.get(index).getCompanyBefore());
						pst.setString(++i, member.get(index).getCompanyBeforeRelation());
						pst.setString(++i, member.get(index).getCompanyBeforeType());
						pst.setString(++i, member.get(index).getDistrictBefore());
						
						
						pst.setString(++i, member.get(index).getAdministrationDuty());
						pst.setString(++i,member.get(index).getAdministrationDutyDesc());
						pst.setString(++i, member.get(index).getAdministrationStatus());
						pst.setString(++i, member.get(index).getAdministrationInstitution());
						pst.setString(++i, member.get(index).getAdministrationAprovedBy());
						if(member.get(index).getAdministrationAprovedDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getAdministrationAprovedDate().getTime()));
						}
						pst.setString(++i,member.get(index).getAdministrationAprovedFile());
						if(member.get(index).getAdministrationRetainDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getAdministrationRetainDate().getTime()));
						}
						pst.setString(++i, member.get(index).getStaffLevel());
						if(member.get(index).getStaffLevedAprovedDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getStaffLevedAprovedDate().getTime()));
						}
						pst.setString(++i,member.get(index).getStaffLevedAprovedFile());
						pst.setString(++i, member.get(index).getStaffLevedAprovedCompany());
						pst.setString(++i, member.get(index).getTechnicalDuty());
						pst.setString(++i, member.get(index).getTechnicalTitle());
						pst.setString(++i,member.get(index).getTechnicalDutyCompany());
						if(member.get(index).getTechnicalDutyEvaluateDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getTechnicalDutyEvaluateDate().getTime()));
						}
						if(member.get(index).getTechnicalDutyEndDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getTechnicalDutyEndDate().getTime()));
						}
						pst.setString(++i, member.get(index).getTechnicalDutyLevel());
						if( member.get(index).getTechnicalDutyBeginDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getTechnicalDutyBeginDate().getTime()));
						}
						pst.setString(++i,member.get(index).getTechnicalTitleMajor());
						pst.setString(++i, member.get(index).getDutyCompany());
						pst.setString(++i, member.get(index).getDutyChangeType());
						pst.setString(++i, member.get(index).getDutyChangeFile());
						pst.setString(++i, member.get(index).getHasKnowledgeInnovation());
						pst.setString(++i,member.get(index).getHasArchive());
						pst.setString(++i, member.get(index).getHasContract());
						pst.setString(++i, member.get(index).getDutyName());
						pst.setString(++i, member.get(index).getDutyLevel());
						if(member.get(index).getDutyDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getDutyDate().getTime()));
						}
						
						
						//update
						pst.setInt(++i, member.get(index).getInstitutionId());
						pst.setString(++i, member.get(index).getTrueName());
						pst.setString(++i, "show");
						pst.setInt(++i, member.get(index).getDepartId());
						pst.setString(++i, member.get(index).getSex());
						pst.setString(++i, member.get(index).getOfficeAddress());
						pst.setString(++i, member.get(index).getOfficeTelephone());
						pst.setString(++i,member.get(index).getMobilePhone());
						
						pst.setString(++i,member.get(index).getSn());
						if(member.get(index).getBirth()==null){
							pst.setDate(++i, null);
						}else{
							pst.setDate(++i,new java.sql.Date(member.get(index).getBirth().getTime()));
						}
						pst.setString(++i, member.get(index).getBirthPlace());
						pst.setInt(++i, member.get(index).getAge());
						pst.setInt(++i,member.get(index).getJobAge());
						pst.setString(++i, member.get(index).getJobType());
						pst.setString(++i, member.get(index).getLevelCode());
						pst.setString(++i, member.get(index).getJobLevel());
						
						pst.setString(++i,member.get(index).getSalaryLevel());
						pst.setString(++i, member.get(index).getHomeAddress());
						if(member.get(index).getJobDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i,new java.sql.Date(member.get(index).getJobDate().getTime()));
						}
						pst.setString(++i, member.get(index).getPoliticalStatus());
						pst.setString(++i,member.get(index).getDutyType());
						pst.setString(++i, member.get(index).getJobStatus());
						pst.setString(++i, member.get(index).getDutyGrade());
						if(member.get(index).getSalaryBase()==null||member.get(index).getSalaryBase().equals("")){
							pst.setString(++i, "0");
						}else{
							pst.setString(++i, member.get(index).getSalaryBase());
						}
						if(member.get(index).getSalaryJob()==null||member.get(index).getSalaryJob().equals("")){
							pst.setString(++i, "0");
						}else{
							pst.setString(++i,member.get(index).getSalaryJob());
						}
//						pst.setString(++i, member.get(index).getSalaryBase());
//						pst.setString(++i,member.get(index).getSalaryJob());
						if(member.get(index).getRetireDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i,new java.sql.Date( member.get(index).getRetireDate().getTime()));
						}
						if(member.get(index).getLeaveDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getLeaveDate().getTime()));
						}
						if(member.get(index).getPartyDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getPartyDate().getTime()));
						}
						pst.setString(++i, member.get(index).getGraduateSchool());
						if(member.get(index).getGraduateDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getGraduateDate().getTime()));
						}
						pst.setString(++i, member.get(index).getMajor());
						pst.setString(++i, member.get(index).getHighestGraduateCode());
						pst.setString(++i, member.get(index).getHighestGraduate());
						
						pst.setString(++i, member.get(index).getHighestDegreeCode());
						pst.setString(++i, member.get(index).getHighestDegree());
						if(member.get(index).getDegreeDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getDegreeDate().getTime()));
						}
						pst.setString(++i, member.get(index).getDegreeCompany());
						if( member.get(index).getCompanyJoinDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getCompanyJoinDate().getTime()));
						}
						pst.setString(++i,member.get(index).getCompanyJoinWay());
						if( member.get(index).getIndustryJoinDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getIndustryJoinDate().getTime()));
						}
						if(member.get(index).getDistrictJoinDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getDistrictJoinDate().getTime()));
						}
						pst.setString(++i, member.get(index).getCompanyJoinStatus());
						pst.setString(++i, member.get(index).getCompanyJoinType());
						pst.setString(++i,member.get(index).getCompanyBefore());
						pst.setString(++i, member.get(index).getCompanyBeforeRelation());
						pst.setString(++i, member.get(index).getCompanyBeforeType());
						pst.setString(++i, member.get(index).getDistrictBefore());
						
						
						pst.setString(++i, member.get(index).getAdministrationDuty());
						pst.setString(++i,member.get(index).getAdministrationDutyDesc());
						pst.setString(++i, member.get(index).getAdministrationStatus());
						pst.setString(++i, member.get(index).getAdministrationInstitution());
						pst.setString(++i, member.get(index).getAdministrationAprovedBy());
						if(member.get(index).getAdministrationAprovedDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getAdministrationAprovedDate().getTime()));
						}
						pst.setString(++i,member.get(index).getAdministrationAprovedFile());
						if(member.get(index).getAdministrationRetainDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getAdministrationRetainDate().getTime()));
						}
						pst.setString(++i, member.get(index).getStaffLevel());
						if(member.get(index).getStaffLevedAprovedDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getStaffLevedAprovedDate().getTime()));
						}
						pst.setString(++i,member.get(index).getStaffLevedAprovedFile());
						pst.setString(++i, member.get(index).getStaffLevedAprovedCompany());
						pst.setString(++i, member.get(index).getTechnicalDuty());
						pst.setString(++i, member.get(index).getTechnicalTitle());
						pst.setString(++i,member.get(index).getTechnicalDutyCompany());
						if(member.get(index).getTechnicalDutyEvaluateDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getTechnicalDutyEvaluateDate().getTime()));
						}
						if(member.get(index).getTechnicalDutyEndDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getTechnicalDutyEndDate().getTime()));
						}
						pst.setString(++i, member.get(index).getTechnicalDutyLevel());
						if( member.get(index).getTechnicalDutyBeginDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getTechnicalDutyBeginDate().getTime()));
						}
						pst.setString(++i,member.get(index).getTechnicalTitleMajor());
						pst.setString(++i, member.get(index).getDutyCompany());
						pst.setString(++i, member.get(index).getDutyChangeType());
						pst.setString(++i, member.get(index).getDutyChangeFile());
						pst.setString(++i, member.get(index).getHasKnowledgeInnovation());
						pst.setString(++i,member.get(index).getHasArchive());
						pst.setString(++i, member.get(index).getHasContract());
						pst.setString(++i, member.get(index).getDutyName());
						pst.setString(++i, member.get(index).getDutyLevel());
						if(member.get(index).getDutyDate()==null){
							pst.setDate(++i,null);
						}else{
							pst.setDate(++i, new java.sql.Date( member.get(index).getDutyDate().getTime()));
						}
					}
				});
	}
	@Override
	public void updateUserId(final String cstnetId, final int userId) {
		String sql="update institution_member_from_vmt set uid=:uid where cstnet_id=:cstnetId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("uid", userId);
		paramMap.put("cstnetId", cstnetId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}
	
	@Override
	public void updateBaseMember(InstitutionMemberFromVmt member) {
		String sql="update institution_member_from_vmt set true_name=:trueName,sex=:sex,technical_title=:technicalTitle,office_address=:officeAddress,office_telephone=:officeTelephone,mobile_phone=:mobilePhone where umt_id=:umtId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("trueName", member.getTrueName());
		paramMap.put("sex", member.getSex());
		paramMap.put("technicalTitle", member.getTechnicalTitle());
		paramMap.put("officeAddress", member.getOfficeAddress());
		paramMap.put("officeTelephone", member.getOfficeTelephone());
		paramMap.put("mobilePhone", member.getMobilePhone());
		paramMap.put("umtId", member.getUmtId());
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}
	
	@Override
	public void parentChanged(int orgParentId, int newId) {
		String sql="update institution_member_from_vmt set depart_id=:newId where depart_id=:orgParentId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("newId", newId);
		paramMap.put("orgParentId", orgParentId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}
	@Override
	public void removeMembersByDeptIds(final List<Integer> deptIds) {
		String sql="delete from institution_member_from_vmt where depart_id=? ";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return deptIds.size();
					}

					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i,deptIds.get(index));
					}
				});
	}
	@Override
	public void resetIsAdmin(int institutionId) {
		String sql="update institution_member_from_vmt set user_type='member' where user_type='admin' and institution_id=:insId";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("insId", institutionId);
		getNamedParameterJdbcTemplate().update(sql,map);
	}
	@Override
	public void deleteMember(final int insId,final String[] umtId){
		String sql="update institution_member_from_vmt set display='delete' where umt_id=? and institution_id=?";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return umtId.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setString(++i,umtId[index]);
						pst.setInt(++i,insId);
					}
				});
	}
	@Override
	public void setAdmins(final int insId,final List<VmtUser> admins) {
		String sql="update institution_member_from_vmt set user_type='admin' where umt_id=? and institution_id=?";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return admins.size();
					}

					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setString(++i,admins.get(index).getUmtId());
						pst.setInt(++i,insId);
					}
				});
		
	}
	public List<InstitutionMemberFromVmt> getVmtMember(int insId, int offset,int size,SearchInstitutionCondition condition) {
		String sql="select * from institution_member_from_vmt where institution_id=:insId and display!='delete' order by cstnet_id asc limit :offset,:size";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("insId", insId);
		param.put("offset",offset);
		param.put("size", size);
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
		param.put("title", condition.getTitle());
		param.put("departId", condition.getMemberDpetId());
		param.put("jobStatus", condition.getJobStatus());
		return getNamedParameterJdbcTemplate().query(getListSql(false, condition), param, rowMapper);
	}
	
	@Override
	public int getVmtMemberCount(int insId,SearchInstitutionCondition condition) {
		String sql="select count(*) from institution_member_from_vmt where institution_id=:insId and display!='delete'";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("insId", insId);
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
		param.put("title", condition.getTitle());
		param.put("departId", condition.getMemberDpetId());
		param.put("jobStatus", condition.getJobStatus());
		return getNamedParameterJdbcTemplate().queryForInt(getListSql(true, condition),param);
	}
	
	private String getListSql(boolean isCount,SearchInstitutionCondition condition){
		StringBuffer sb=new StringBuffer();
		sb.append(" select ").append(isCount?"count(*)":"*").append(" from institution_member_from_vmt ");
		sb.append(" where institution_id=:insId and display!='delete' and (job_status!='不在职' or job_status is null) ");
		//关键字
		if(!CommonUtils.isNull(condition.getKeyword())){
			sb.append(" and true_name like :keyword ");
		}
		//工作状态
		if(!CommonUtils.isNull(condition.getJobStatus())){
			sb.append(" and job_status = :jobStatus ");
		}
		if(!CommonUtils.isNull(condition.getTitle())){
			if(condition.getTitle().equals("其他")){
				sb.append(" and (technical_title is null or technical_title='') ");
			}else{
				sb.append(" and technical_title=:title ");
			}
		}
		if(condition.getMemberDpetId()!=-1){
			sb.append(" and depart_id=:departId ");
		}
		if(!isCount){
			//排序
			sb.append(" order by convert(true_name using gbk) collate gbk_chinese_ci "+ getOrderTypeByType(condition.getOrderType()));
			sb.append(" limit :offset,:size");
		}
		return sb.toString();
	}
	
	private String getOrderTypeByType(int orderType){
		switch(orderType){
			case SearchInstitutionCondition.ORDER_TYPE_ASC:{
				return " asc ";
			}
			case SearchInstitutionCondition.ORDER_TYPE_DESC:{
				return " desc ";
			}
			default:{
				return " asc ";
			}
		}
	}
	
	
	@Override
	public boolean isAdmin(int insId, String email) {
		String sql="select count(*) from institution_member_from_vmt where institution_id=:insId and cstnet_id=:cstnetId and user_type='admin' and display!='delete'";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("insId", insId);
		param.put("cstnetId", email);
		return getNamedParameterJdbcTemplate().queryForInt(sql, param)>0;
	}
	
	@Override
	public boolean isMember(int insId,String email) {
		String sql="select count(*) from institution_member_from_vmt where institution_id=:insId and cstnet_id=:cstnetId and display!='delete'";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("insId", insId);
		param.put("cstnetId", email);
		return getNamedParameterJdbcTemplate().queryForInt(sql, param)>0;
	}
	
	@Override
	public boolean isMember(String email) {
		String sql="select count(*) from institution_member_from_vmt where cstnet_id=:cstnetId and display!='delete'";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("cstnetId", email);
		return getNamedParameterJdbcTemplate().queryForInt(sql, param)>0;
	}
	
	@Override
	public InstitutionMemberFromVmt getVmtMemberByUmtId(int institutionId,
			String umtId) {
		String sql="select * from institution_member_from_vmt where institution_id=:insId and umt_id=:umtId and display!='delete'";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId",institutionId);
		paramMap.put("umtId",umtId);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap,rowMapper));
	}
	
	@Override
	public Integer getInstituionId(int userId) {
		String sql="select institution_id from institution_member_from_vmt where uid=? order by institution_id desc limit 1";
		List<Integer> list = getJdbcTemplate().queryForList(sql, Integer.class, userId);
		return list.size() > 0 ? list.get(0) : null;
	}
	
	
	private static final RowMapper<InstitutionMemberFromVmt> rowMapper=new RowMapper<InstitutionMemberFromVmt>(){
	
	@Override
	public InstitutionMemberFromVmt mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		InstitutionMemberFromVmt member=new InstitutionMemberFromVmt();
		member.setCstnetId(rs.getString("cstnet_id"));
		member.setDepartId(rs.getInt("depart_id"));
		member.setDisplay(rs.getString("display"));
		member.setInstitutionId(rs.getInt("institution_id"));
		member.setMobilePhone(rs.getString("mobile_phone"));
		member.setOfficeAddress(rs.getString("office_address"));
		member.setOfficeTelephone(rs.getString("office_telephone"));
		member.setSex(rs.getString("sex"));
		member.setTitle(rs.getString("title"));
		member.setTrueName(rs.getString("true_name"));
		member.setUmtId(rs.getString("umt_id"));
		member.setUserType(rs.getString("user_type"));
		member.setUid(rs.getInt("uid"));
		member.setJobStatus(rs.getString("job_status"));
		member.setTechnicalTitle(rs.getString("technical_title"));
		member.setSn(rs.getString("sn"));
		member.setBirth(rs.getDate("birth"));
		member.setBirthPlace(rs.getString("birth_place"));
		member.setAge(rs.getInt("age"));
		member.setJobAge(rs.getInt("job_age"));
		member.setJobType(rs.getString("job_type"));
		member.setLevelCode(rs.getString("level_code"));
		member.setJobLevel(rs.getString("job_level"));
		member.setSalaryLevel(rs.getString("salary_level"));
		member.setHomeAddress(rs.getString("home_address"));
		member.setJobDate(rs.getDate("job_date"));
		member.setPoliticalStatus(rs.getString("political_status"));
		member.setDutyType(rs.getString("duty_type"));
		member.setDutyGrade(rs.getString("duty_grade"));
		member.setSalaryBase(rs.getString("salary_base"));
		member.setSalaryJob(rs.getString("salary_job"));
		member.setRetireDate(rs.getDate("retire_date"));
		member.setLeaveDate(rs.getDate("leave_date"));
		member.setPartyDate(rs.getDate("party_date"));
		member.setGraduateSchool(rs.getString("graduate_school"));
		member.setGraduateDate(rs.getDate("graduate_date"));
		member.setMajor(rs.getString("major"));
		member.setHighestGraduateCode(rs.getString("highest_graduate_code"));
		member.setHighestGraduate(rs.getString("highest_graduate"));
		member.setHighestDegreeCode(rs.getString("highest_degree_code"));
		member.setHighestDegree(rs.getString("highest_degree"));
		member.setDegreeDate(rs.getDate("degree_date"));
		member.setDegreeCompany(rs.getString("degree_company"));
		member.setCompanyJoinDate(rs.getDate("company_join_date"));
		member.setCompanyJoinStatus(rs.getString("company_join_status"));
		member.setCompanyJoinWay(rs.getString("company_join_way"));
		member.setIndustryJoinDate(rs.getDate("industry_join_date"));
		member.setDistrictJoinDate(rs.getDate("district_join_date"));
		member.setCompanyJoinType(rs.getString("company_join_type"));
		member.setCompanyBefore(rs.getString("company_before"));
		member.setCompanyBeforeRelation(rs.getString("company_before_relation"));
		member.setCompanyBeforeType(rs.getString("company_before_type"));
		member.setDistrictBefore(rs.getString("district_before"));
		member.setAdministrationAprovedBy(rs.getString("administration_aproved_by"));
		member.setAdministrationDuty(rs.getString("administration_duty"));
		member.setAdministrationDutyDesc(rs.getString("administration_duty_desc"));
		member.setAdministrationAprovedFile(rs.getString("administration_aproved_file"));
		member.setAdministrationStatus(rs.getString("administration_status"));
		member.setAdministrationRetainDate(rs.getDate("administration_retain_date"));
		member.setAdministrationInstitution(rs.getString("administration_institution"));
		member.setAdministrationAprovedDate(rs.getDate("administration_aproved_date"));
		member.setStaffLevel(rs.getString("staff_level"));
		member.setStaffLevedAprovedFile(rs.getString("staff_leved_aproved_file"));
		member.setStaffLevedAprovedDate(rs.getDate("staff_leved_aproved_date"));
		member.setStaffLevedAprovedCompany(rs.getString("staff_leved_aproved_company"));
		member.setTechnicalDuty(rs.getString("technical_duty"));
		member.setTechnicalDutyBeginDate(rs.getDate("technical_duty_begin_date"));
		member.setTechnicalDutyCompany(rs.getString("technical_duty_company"));
		member.setTechnicalDutyLevel(rs.getString("technical_duty_level"));
		member.setTechnicalDutyEndDate(rs.getDate("technical_duty_end_date"));
		member.setTechnicalDutyEvaluateDate(rs.getDate("technical_duty_evaluate_date"));
		member.setTechnicalTitleMajor(rs.getString("technical_title_major"));
		member.setDutyCompany(rs.getString("duty_company"));
		member.setDutyChangeFile(rs.getString("duty_change_type"));
		member.setDutyChangeType(rs.getString("duty_change_file"));
		member.setHasKnowledgeInnovation(rs.getString("has_knowledge_innovation"));
		member.setHasArchive(rs.getString("has_archive"));
		member.setHasContract(rs.getString("has_contract"));
		member.setDutyName(rs.getString("duty_name"));
		member.setDutyLevel(rs.getString("duty_level"));
		member.setDutyDate(rs.getDate("duty_date"));
		return member;
	}};

	/**
	 * 机构职称统计
	 */
	@Override
	public Map<String, Integer> getTitlesMap(int insId,int dept) {
		String sql="select technical_title,count(*) c from institution_member_from_vmt "+
				"where institution_id=:insId and display!='delete'  and job_status!='不在职' ";
		if(dept != -1){
			sql += " and depart_id=:deptId ";
		}
		sql +=	"group by technical_title order by technical_title ASC";
		
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		paramMap.put("deptId", dept);
		final Map<String,Integer> result=new LinkedHashMap<String,Integer>();
		getNamedParameterJdbcTemplate().query(sql, paramMap, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String key = rs.getString("technical_title");
				if(key == null || key.equals("null") || key.equals(""))
					key = "其他";
				
				if(result.containsKey(key)){
					result.put(key, result.get(key) + rs.getInt("c"));
				}else{
					result.put(key, rs.getInt("c"));
				}
				return null;
			}
		});
		return result;
	}
	
	@Override
	public Map<String, Integer> getDegreesMap(int insId, int dept) {
		String sql="select highest_degree,count(*) c from institution_member_from_vmt "+
				"where institution_id=:insId and display!='delete'  and job_status!='不在职' ";
		if(dept != -1){
			sql += " and depart_id=:deptId ";
		}
		sql +=	"group by highest_degree order by highest_degree ";
		
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		paramMap.put("deptId", dept);
		final Map<String,Integer> result=new LinkedHashMap<String,Integer>();
		getNamedParameterJdbcTemplate().query(sql, paramMap, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String key = rs.getString("highest_degree");
				if(key == null || key.equals("null") || key.equals(""))
					key = "其他";
				
				if(result.containsKey(key)){
					result.put(key, result.get(key) + rs.getInt("c"));
				}else{
					result.put(key, rs.getInt("c"));
				}
				return null;
			}
		});
		return result;
	}
	
	@Override
	public Map<String, Integer> getAgesMap(int insId, int dept) {
		String sql="select age,count(*) c from institution_member_from_vmt "+
				"where institution_id=:insId and display!='delete'  and job_status!='不在职' ";
		if(dept != -1){
			sql += " and depart_id=:deptId ";
		}
		sql +=	"group by age order by age";
		
		Map<String,Object> paramMap=new LinkedHashMap<String,Object>();
		paramMap.put("insId", insId);
		paramMap.put("deptId", dept);
		final Map<String,Integer> result=new LinkedHashMap<String,Integer>();
		getNamedParameterJdbcTemplate().query(sql, paramMap, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String key = rs.getString("age");
				if(key == null || key.equals("null") || key.equals(""))
					key = "其他";
				if(result.containsKey(key)){
					result.put(key, result.get(key) + rs.getInt("c"));
				}else{
					result.put(key, rs.getInt("c"));
				}
				return null;
			}
		});
		return result;
	}
	/**
	 * 搜索人员
	 */
	@Override
	public List<InstitutionMemberFromVmt> searchMember(String keyword) {
		String sql="select * from institution_member_from_vmt where true_name like :keyword or cstnet_id like :keyword";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("keyword", "%"+keyword+"%");
		return getNamedParameterJdbcTemplate().query(sql,param,rowMapper);
	}
	/**
	 * 所有人员
	 */
	@Override
	public List<InstitutionMemberFromVmt> getAllUser() {
		String sql="select * from institution_member_from_vmt where 1=1";
		return getJdbcTemplate().query(sql,rowMapper);
	}
	/**
	 * 通过邮箱cstnetId查用户
	 */
	@Override
	public InstitutionMemberFromVmt getMemberByCstnetId(int institutionId,String cstnetId){
		String sql="select * from institution_member_from_vmt where cstnet_id=:cstnetId";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("cstnetId", cstnetId);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql,param,rowMapper));
	}
	@Override
	public void updateCstnetId(final List<InstitutionMemberFromVmt> members) {
		String sql="update institution_member_from_vmt "
				+"set cstnet_id=?, umt_id= ?"
				+"where sn=? and institution_id=? ";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return members.size();
					}

					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setString(++i,members.get(index).getCstnetId());
						pst.setString(++i,members.get(index).getUmtId());
						pst.setString(++i,members.get(index).getSn());
						pst.setInt(++i, members.get(index).getInstitutionId());
					}
				});
	}
	@Override
	public InstitutionMemberFromVmt getMemberBySn(int institutionId, String sn) {
		String sql="select * from institution_member_from_vmt where sn=:sn and institution_id=:institutionId";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("sn", sn);
		param.put("institutionId", institutionId);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql,param,rowMapper));
	}
	@Override
	public int getMemberCount(int insId,int departId, int year) {
		String sql="select count(*) from institution_member_from_vmt where institution_id=:insId";
		StringBuilder sb=new StringBuilder();
		sb.append(sql);
		if(departId!=-1){
			sb.append(" and depart_id=:departId");
		}
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("insId", insId);
		param.put("departId", departId);
		return getNamedParameterJdbcTemplate().queryForInt(sb.toString(),param);
	}
	@Override
	public void updateMemberStatus(final List<InstitutionMemberFromVmt> member) {
		String sql="update institution_member_from_vmt set job_status='不在职' where sn=? and institution_id=?";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return member.size();
					}

					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setString(++i,member.get(index).getSn());
						pst.setInt(++i, member.get(index).getInstitutionId());
						
					}
				});
	}
	@Override
	public InstitutionMemberFromVmt getMemberByUid(int insId, int uid) {
		String sql="select * from institution_member_from_vmt where institution_id=:insId and uid=:uid ";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("insId", insId);
		param.put("uid", uid);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql,param,rowMapper));
	}
}
