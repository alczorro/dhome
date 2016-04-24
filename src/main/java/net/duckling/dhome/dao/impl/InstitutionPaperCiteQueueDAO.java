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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.dao.IInstitutionPaperCiteQueueDAO;
import net.duckling.dhome.domain.institution.InstitutionPaperCiteQueue;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

/**
 * 论文引用抓取队列
 * 
 * @author brett
 * */
@Component
public class InstitutionPaperCiteQueueDAO extends BaseDao implements IInstitutionPaperCiteQueueDAO{
	
	private static class InsRowMapper implements RowMapper<InstitutionPaperCiteQueue>{	
		@Override
		public InstitutionPaperCiteQueue mapRow(ResultSet rs, int index) throws SQLException {
			InstitutionPaperCiteQueue model = new InstitutionPaperCiteQueue();
			model.setPaperId(rs.getInt("paper_id"));
			model.setUid(rs.getInt("uid"));
			model.setLastAccess(rs.getTimestamp("last_access"));
			model.setAppendType(rs.getInt("append_type"));
			return model;
		}
	}
	
	private InsRowMapper rowMapper = new InsRowMapper();

	@Override
	public InstitutionPaperCiteQueue getFirst() {
		String sql = "select * from institution_paper_cite_queue order by  append_type desc,paper_id asc limit 1";
		Map<String, Object> params = new HashMap<String, Object>();
		List<InstitutionPaperCiteQueue> list = this.getNamedParameterJdbcTemplate().query(sql, params, rowMapper);
		if(list == null || list.size() == 0){
			return null;
		}
		return list.get(0);
	}
	
	@Override
	public InstitutionPaperCiteQueue getByPaperId(int paperId) {
		String sql = "select * from institution_paper_cite_queue where paper_id=:paperId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("paperId", paperId);
		List<InstitutionPaperCiteQueue> list = this.getNamedParameterJdbcTemplate().query(sql, params, rowMapper);
		if(list == null || list.size() == 0){
			return null;
		}
		return list.get(0);
	}

	@Override
	public void insert(InstitutionPaperCiteQueue model) {
		String sql="insert into institution_paper_cite_queue(`paper_id`,`uid`,`last_access`,`append_type`) values(:paperId,:uid,:lastAccess,:appendType)";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("paperId", model.getPaperId());
		paramMap.put("uid", model.getUid());
		paramMap.put("lastAccess", model.getLastAccess());
		paramMap.put("appendType", model.getAppendType());
		getNamedParameterJdbcTemplate().update(sql,new MapSqlParameterSource(paramMap));
	}
	
	@Override
	public void update(InstitutionPaperCiteQueue model) {
		String sql="update institution_paper_cite_queue set `uid`=:uid,`last_access`=:lastAccess,`append_type`=:appendType where `paper_id`=:paperId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("paperId", model.getPaperId());
		paramMap.put("uid", model.getUid());
		paramMap.put("lastAccess", model.getLastAccess());
		paramMap.put("appendType", model.getAppendType());
		getNamedParameterJdbcTemplate().update(sql,new MapSqlParameterSource(paramMap));
	}

	@Override
	public void delete(int paperId) {
		String sql="delete from institution_paper_cite_queue where paper_id=:paperId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("paperId", paperId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}
	
	@Override
	public int getCountByBatchType(){
		String sql = " select count(*) from institution_paper_cite_queue "+
				 " where append_type=:appendType";
		Map<String,Integer> map=new HashMap<String,Integer>();
		map.put("appendType", InstitutionPaperCiteQueue.APPEND_TYPE_BATCH);
		return getNamedParameterJdbcTemplate().queryForInt(sql, map);
	}
}
