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
import net.duckling.dhome.dao.IInstitutionAchievementsDAO;
import net.duckling.dhome.dao.IInstitutionGrantsDAO;
import net.duckling.dhome.dao.IInstitutionJobApplicationDAO;
import net.duckling.dhome.domain.institution.InstitutionAchievements;
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
public class InstitutionAchievementsDAOImpl extends BaseDao implements IInstitutionAchievementsDAO{
	private RowMapper<InstitutionAchievements> rowMapper= new RowMapper<InstitutionAchievements>() {
			@Override
			public InstitutionAchievements mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				InstitutionAchievements item=new InstitutionAchievements();
				item.setId(rs.getInt("id"));
				item.setStartTime(rs.getDate("start_date"));
				item.setEndTime(rs.getDate("end_date"));
				item.setInstitutionId(rs.getInt("institution_id"));
				item.setYear(rs.getInt("year"));
				item.setTitle(rs.getString("title"));
				return item;
			}
		};
	
	
	
	@Override
	public List<InstitutionAchievements> getList(int insId, int offset, int size) {
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
		sb.append(" select ").append(isCount?"count(*)":"t1.*").append(" from institution_achievements t1 ");
		sb.append(" where institution_id=:insId");
		if(!isCount){
			//排序
			sb.append(" order by t1.id desc ");
			sb.append(" limit :offset,:size");
		}
		return sb.toString();
	}
	
	@Override
	public void creat(InstitutionAchievements achievements) {
		String sql="insert into institution_achievements (institution_id,title,year,start_date,end_date) " +
				"values(:insId,:title,:year,:startDate,:endDate)";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", achievements.getInstitutionId());
		paramMap.put("title", achievements.getTitle());
		paramMap.put("year", achievements.getYear());
		paramMap.put("startDate", achievements.getStartTime());
		paramMap.put("endDate", achievements.getEndTime());
		getNamedParameterJdbcTemplate().update(sql,paramMap);
	}
	
	@Override
	public void delete(int applicationId) {
		String sql="delete from institution_achievements where id=:applicationId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("applicationId", applicationId);
		getNamedParameterJdbcTemplate().update(sql,paramMap);
	}

	@Override
	public List<InstitutionAchievements> getAllList(int insId) {
		String sql="select * from institution_achievements where institution_id=:insId";
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		return getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper);
	}
	@Override
	public InstitutionAchievements getById(int achievementsId) {
		String sql="select * from institution_achievements where id=:achievementsId";
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("achievementsId", achievementsId);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper));
	}

	
}
