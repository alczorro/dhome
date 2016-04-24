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

import net.duckling.common.util.CommonUtils;
import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.dao.IInstitutionGrantsDAO;
import net.duckling.dhome.dao.IInstitutionJobApplicationDAO;
import net.duckling.dhome.dao.IInstitutionJobApplyDAO;
import net.duckling.dhome.domain.institution.InstitutionGrants;
import net.duckling.dhome.domain.institution.InstitutionJobApplication;
import net.duckling.dhome.domain.institution.InstitutionJobApply;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class InstitutionJobApplyDAOImpl extends BaseDao implements IInstitutionJobApplyDAO{
	private RowMapper<InstitutionJobApply> rowMapper= new RowMapper<InstitutionJobApply>() {
			@Override
			public InstitutionJobApply mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				InstitutionJobApply item=new InstitutionJobApply();
				item.setId(rs.getInt("id"));
				item.setApplyTime(rs.getDate("apply_time"));
				item.setInstitutionId(rs.getInt("institution_id"));
				item.setApplicationId(rs.getInt("application_id"));
				item.setJobId(rs.getInt("job_id"));
				item.setJobPerformance(rs.getString("job_performance"));
				item.setStatus(rs.getInt("status"));
				item.setUserId(rs.getInt("user_id"));
				item.setRemark(rs.getString("remark"));
				item.setUpdateTime(rs.getDate("update_time"));
				return item;
			}
		};
	
	
	
	@Override
	public List<InstitutionJobApply> getListByApplicationId(int applicationId,int insId, int offset, int size) {
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		paramMap.put("applicationId", applicationId);
		paramMap.put("offset", offset);
		paramMap.put("size", size);
		return getNamedParameterJdbcTemplate().query(getListSql(false), paramMap, rowMapper);
	}
	
	@Override
	public int getListCountByApplicationId(int insId,int applicationId) {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		paramMap.put("applicationId", applicationId);
		return getNamedParameterJdbcTemplate().queryForInt(getListSql(true),paramMap);
	}
	private String getListSql(boolean isCount){
		StringBuilder sb=new StringBuilder();
		sb.append(" select ").append(isCount?"count(*)":"t1.*").append(" from institution_job_apply t1 ");
		sb.append(" where institution_id=:insId and application_id=:applicationId and status=1");
		if(!isCount){
			//排序
			sb.append(" order by t1.id desc ");
			sb.append(" limit :offset,:size");
		}
		return sb.toString();
	}
	
//	@Override
//	public void creat(InstitutionJobApplication jobApplication) {
//		String sql="insert into institution_job_application (institution_id,title,year,start_date,end_date) " +
//				"values(:insId,:title,:year,:startDate,:endDate)";
//		Map<String,Object> paramMap=new HashMap<String,Object>();
//		paramMap.put("insId", jobApplication.getInstitutionId());
//		paramMap.put("title", jobApplication.getTitle());
//		paramMap.put("year", jobApplication.getYear());
//		paramMap.put("startDate", jobApplication.getStartTime());
//		paramMap.put("end_date", jobApplication.getEndTime());
//		getNamedParameterJdbcTemplate().queryForInt(sql,paramMap);
//	}
	
	@Override
	public void delete(int insId,int applicationId) {
		String sql="delete from institution_job_apply where application_id=:applicationId and institution_id=:insId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		paramMap.put("applicationId", applicationId);
		getNamedParameterJdbcTemplate().update(sql,paramMap);
	}

	@Override
	public List<InstitutionJobApply> getListByUserId(int insId,
			int userId, int offset, int size) {
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		paramMap.put("userId", userId);
		paramMap.put("offset", offset);
		paramMap.put("size", size);
		return getNamedParameterJdbcTemplate().query(getListSqlUser(false), paramMap, rowMapper);
	}

	@Override
	public int getListCountByUserId(int insId, int userId) {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		paramMap.put("userId", userId);
		return getNamedParameterJdbcTemplate().queryForInt(getListSqlUser(true),paramMap);
	}
	private String getListSqlUser(boolean isCount){
		StringBuilder sb=new StringBuilder();
		sb.append(" select ").append(isCount?"count(*)":"t1.*").append(" from institution_job_apply t1 ");
		sb.append(" where institution_id=:insId and user_id=:userId");
		if(!isCount){
			//排序
			sb.append(" order by t1.id desc ");
			sb.append(" limit :offset,:size");
		}
		return sb.toString();
	}

	@Override
	public void insert(int insId, int userId, int applicationId,int status,
			InstitutionJobApply jobApply) {
		String sql="insert into institution_job_apply (application_id,institution_id,user_id,job_id,job_performance,remark,apply_time,status,update_time) " +
				"select :applicationId,:insId,:userId,:jobId,:jobPerformance,:remark,SYSDATE(),:status,SYSDATE() from dual " +
				"where not exists(select * from institution_job_apply where user_id=:userId and application_id=:applicationId)";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		paramMap.put("applicationId", applicationId);
		paramMap.put("userId", userId);
		paramMap.put("jobId", jobApply.getJobId());
		paramMap.put("jobPerformance", jobApply.getJobPerformance());
		paramMap.put("remark", jobApply.getRemark());
		paramMap.put("status", status);
		getNamedParameterJdbcTemplate().update(sql,paramMap);
	}

	@Override
	public InstitutionJobApply getById(int applyId) {
		String sql="select * from institution_job_apply where id=:applyId";
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("applyId", applyId);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper));
	}

	@Override
	public void deleteById(int applyId) {
		String sql="delete from institution_job_apply where id=:id";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("id", applyId);
		getNamedParameterJdbcTemplate().update(sql,paramMap);
	}

	@Override
	public void update(int applyId, int status,InstitutionJobApply jobApply) {
		String sql="update institution_job_apply set job_id=:jobId,job_performance=:jobPerformance,remark=:remark," +
				"status=:status,update_time=SYSDATE() where id=:applyId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
//		paramMap.put("insId", insId);
//		paramMap.put("applicationId", applicationId);
		paramMap.put("applyId", applyId);
		paramMap.put("jobId", jobApply.getJobId());
		paramMap.put("jobPerformance", jobApply.getJobPerformance());
		paramMap.put("remark", jobApply.getRemark());
		paramMap.put("status", status);
		getNamedParameterJdbcTemplate().update(sql,paramMap);
	}

	@Override
	public List<InstitutionJobApply> getIdByUser(int userId, int insId) {
		String sql="select * from institution_job_apply where institution_id=:insId and user_id=:userId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId", userId);
		paramMap.put("insId", insId);
		return getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper);
	}
	

}
