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
import net.duckling.dhome.domain.institution.InstitutionGrants;
import net.duckling.dhome.domain.institution.InstitutionJobApplication;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class InstitutionJobApplicationDAOImpl extends BaseDao implements IInstitutionJobApplicationDAO{
	private RowMapper<InstitutionJobApplication> rowMapper= new RowMapper<InstitutionJobApplication>() {
			@Override
			public InstitutionJobApplication mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				InstitutionJobApplication item=new InstitutionJobApplication();
				item.setId(rs.getInt("id"));
				item.setStartTime(rs.getDate("start_date"));
				item.setEndTime(rs.getDate("end_date"));
				item.setInstitutionId(rs.getInt("institution_id"));
				item.setYear(rs.getInt("year"));
				item.setTitle(rs.getString("title"));
				item.setDeadline(rs.getDate("deadline"));
				return item;
			}
		};
	
	
	
	@Override
	public List<InstitutionJobApplication> getList(int insId, int offset, int size) {
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		paramMap.put("offset", offset);
		paramMap.put("size", size);
		return getNamedParameterJdbcTemplate().query(getListSql(false), paramMap, rowMapper);
	}
	
	@Override
	public int getListCount(int insId) {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		return getNamedParameterJdbcTemplate().queryForInt(getListSql(true),paramMap);
	}
	private String getListSql(boolean isCount){
		StringBuilder sb=new StringBuilder();
		sb.append(" select ").append(isCount?"count(*)":"t1.*").append(" from institution_job_application t1 ");
		sb.append(" where institution_id=:insId");
		if(!isCount){
			//排序
			sb.append(" order by t1.id desc ");
			sb.append(" limit :offset,:size");
		}
		return sb.toString();
	}
	
	@Override
	public void creat(InstitutionJobApplication jobApplication) {
		String sql="insert into institution_job_application (institution_id,title,year,start_date,end_date,deadline) " +
				"values(:insId,:title,:year,:startDate,:endDate,:deadline)";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", jobApplication.getInstitutionId());
		paramMap.put("title", jobApplication.getTitle());
		paramMap.put("year", jobApplication.getYear());
		paramMap.put("startDate", jobApplication.getStartTime());
		paramMap.put("endDate", jobApplication.getEndTime());
		paramMap.put("deadline", jobApplication.getDeadline());
		getNamedParameterJdbcTemplate().update(sql,paramMap);
	}
	
	@Override
	public void delete(int applicationId) {
		String sql="delete from institution_job_application where id=:applicationId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("applicationId", applicationId);
		getNamedParameterJdbcTemplate().update(sql,paramMap);
	}

	@Override
	public List<InstitutionJobApplication> getAllList(int insId) {
		String sql="select * from institution_job_application where institution_id=:insId";
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		return getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper);
	}

	
}
