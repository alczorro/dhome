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

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import net.duckling.common.util.CommonUtils;
import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.dao.IInstitutionBackendPatentAuthorDAO;
import net.duckling.dhome.domain.institution.InstitutionAuthor;

@Repository
public class InstitutionBackendPatentAuthorDAOImpl extends BaseDao implements
		IInstitutionBackendPatentAuthorDAO {
	private RowMapper<InstitutionAuthor> rowMapper=new RowMapper<InstitutionAuthor>() {
		@Override
		public InstitutionAuthor mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionAuthor au=new InstitutionAuthor();
			au.setAuthorName(rs.getString("name"));
			au.setOrder(rs.getInt("order"));
			au.setPatentId(rs.getInt("patent_id"));
			au.setAuthorEmail(rs.getString("email"));
			au.setAuthorCompany(rs.getString("company"));
			au.setId(rs.getInt("author_id"));
			return au;
		} 
	};

	@Override
	public List<InstitutionAuthor> getAuthorListByPatentId(int patentId) {
		String sql="select * from institution_author a,institution_patent_author_ref b "
				+"where a.id=b.author_id and b.patent_id=:patent_id";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("patent_id", patentId);
		return getNamedParameterJdbcTemplate().query(sql, param, rowMapper);
	}

	@Override
	public List<InstitutionAuthor> getAuthorListByPatentIds(
			List<Integer> patentIds) {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		StringBuffer sb=new StringBuffer("select * "); 
		sb.append("from  institution_author a join institution_patent_author_ref b ");
		sb.append("on b.author_id=a.id ");
		sb.append("where b.patent_id in( ");
		int index=0;
		for(int patentId:patentIds){
			sb.append(":patentId"+index).append(",");
			paramMap.put("patentId"+index, patentId);
			index++;
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(") ");
		sb.append("order by b.patent_id ");
		return getNamedParameterJdbcTemplate().query(sb.toString(),paramMap,rowMapper);
	}

	@Override
	public void createAuthor(InstitutionAuthor author) {
		String sql="insert into institution_author(`name`,`email`,`company`) values(:name,:email,:company)";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("name",author.getAuthorName());
		map.put("email",author.getAuthorEmail());
		map.put("company",author.getAuthorCompany());
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getNamedParameterJdbcTemplate().update(sql, new MapSqlParameterSource(map), keyHolder);
		author.setId(keyHolder.getKey().intValue());

	}

	@Override
	public void createRef(final int patentId, final int[] uid, final int[] order) {
		String sql="insert into institution_patent_author_ref(`author_id`,`patent_id`,`order`) values(?,?,?)";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return uid.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i,uid[index]);
						pst.setInt(++i, patentId);
						pst.setInt(++i, order[index]);
					}
				});

	}

	@Override
	public void deleteRef(int patentId) {
		String sql="delete from institution_patent_author_ref where patent_id=:patentId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("patentId", patentId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
		
	}
	
	
	
	@Override
	public boolean isExist(InstitutionAuthor author) {
		String sql="select * "+ 
				   "from  institution_author "+ 
				   "where name=:name and email=:email "+
				   "limit 1";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("name", author.getAuthorName());
		paramMap.put("email", author.getAuthorEmail());
		paramMap.put("company", author.getAuthorCompany());
		InstitutionAuthor temp = CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap, new RowMapper<InstitutionAuthor>() {
			@Override
			public InstitutionAuthor mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				InstitutionAuthor au=new InstitutionAuthor();
				au.setId(rs.getInt("id"));
				au.setAuthorName(rs.getString("name"));
				au.setAuthorEmail(rs.getString("email"));
				au.setAuthorCompany(rs.getString("company"));
				return au;
			} 
		}));
		
		if(temp != null){
			author.setId(temp.getId());
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void updateRef(int authorId, int patentId,int order) {
		StringBuffer sb=new StringBuffer();
		sb.append("update institution_patent_author_ref set ");
		sb.append("`order`=:authorOrder ");
		sb.append("where author_id=:authorId and patent_id=:patentId");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("authorId", authorId);
		params.put("authorOrder", order);
		params.put("patentId", patentId);
		getNamedParameterJdbcTemplate().update(sb.toString(),params);
		
	}

}
