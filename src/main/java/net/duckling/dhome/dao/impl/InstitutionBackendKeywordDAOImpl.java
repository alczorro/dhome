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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.dao.IInstitutionBackendKeywordDAO;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class InstitutionBackendKeywordDAOImpl extends BaseDao implements IInstitutionBackendKeywordDAO{
	@Override
	public List<String> search(String queryStr) {
		String sql="select distinct keyword from institution_paper_keyword where keyword like :keyword limit 0,10";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("keyword", "%"+queryStr+"%");
		return getNamedParameterJdbcTemplate().query(sql, paramMap, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return (rs.getString("keyword"));
			}
		});
	}
	@Override
	public void insert(final String[] keyword, final int institutionId) {
		String sql="insert into institution_paper_keyword(`keyword`,`paper_id`) values(?,?)";
		getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				int index=0;
				ps.setString(++index, keyword[i]);
				ps.setInt(++index,institutionId);
			}
			
			@Override
			public int getBatchSize() {
				return keyword.length;
			}
		});
	}
	@Override
	public void delete(int paperId) {
		String sql="delete from institution_paper_keyword where paper_id=:pid";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("pid",paperId);
		getNamedParameterJdbcTemplate().update(sql,map);
	}
}
