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

import net.duckling.common.util.CommonUtils;
import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.dao.IInstitutionBackendAwardAuthorDAO;
import net.duckling.dhome.domain.institution.InstitutionAwardAuthor;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class InstitutionBackendAwardAuthorDAOImpl extends BaseDao implements
		IInstitutionBackendAwardAuthorDAO {
	
	private static final RowMapper<InstitutionAwardAuthor> rowMapper=new RowMapper<InstitutionAwardAuthor>() {
		@Override
		public InstitutionAwardAuthor mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionAwardAuthor au=new InstitutionAwardAuthor();
			au.setId(rs.getInt("id"));
			au.setAuthorName(rs.getString("name"));
			au.setAuthorEmail(rs.getString("email"));
			au.setAuthorCompany(rs.getString("company"));
			try{
				au.setOrder(rs.getInt("order"));
				au.setCommunicationAuthor(rs.getBoolean("communication_author"));
				au.setAuthorStudent(rs.getBoolean("author_student"));
				au.setAuthorTeacher(rs.getBoolean("author_teacher"));
				au.setAwardId(rs.getInt("award_id"));
			}catch(SQLException e){
				
			}
			return au;
		} 
	};
	
	@Override
	public List<InstitutionAwardAuthor> search(String keyword) {
		String sql="select * from institution_author where name like :keyword or email like :keyword or company like :keyword";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("keyword", "%"+keyword+"%");
		return getNamedParameterJdbcTemplate().query(sql,param,rowMapper);
	}

	@Override
	public List<InstitutionAwardAuthor> getByAwardId(int awardId) {
		String sql="select * "+ 
				"from  institution_author a left join institution_award_author_ref r "+ 
				"on r.author_id=a.id "+ 
				"where r.award_id=:awardId "
				+ "order by a.id  ";	
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("awardId", awardId);
		return getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper);
	}

	@Override
	public List<InstitutionAwardAuthor> getByAwardIds(
			List<Integer> awardIds) {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		StringBuffer sb=new StringBuffer("select * "); 
		sb.append("from  institution_author a left join institution_award_author_ref r ");
		sb.append("on r.author_id=a.id ");
		sb.append("where r.award_id in( ");
		int index=0;
		for(int awardId:awardIds){
			sb.append(":awardId"+index).append(",");
			paramMap.put("awardId"+index, awardId);
			index++;
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(") ");
		sb.append("order by r.award_id");
		return getNamedParameterJdbcTemplate().query(sb.toString(),paramMap,rowMapper);
	}

	@Override
	public void createAuthor(InstitutionAwardAuthor author) {
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
	public InstitutionAwardAuthor getById(int awardId, int authorId) {
		String sql="select * "+ 
				"from  institution_author a left join institution_award_author_ref r "+ 
				"on r.author_id=a.id "+ 
				"where r.award_id=:awardId "+ 
				"and r.author_id=:authorId "+
				"limit 1";
				Map<String,Object> paramMap=new HashMap<String,Object>();
				paramMap.put("awardId", awardId);
				paramMap.put("authorId", authorId);
				return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper));
	}

	@Override
	public InstitutionAwardAuthor getById(int authorId) {
		String sql="select * "+ 
				   "from  institution_author "+ 
				   "where id=:id "+
				   "limit 1";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("id", authorId);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper));
	}

	@Override
	public void createRef(final int awardId, final int[] authorIds, final int[] order,
			final boolean[] communicationAuthors, final boolean[] authorStudents,
			final boolean[] authorTeacher) {
		String sql="insert into institution_award_author_ref(`award_id`,`author_id`,`order`,`communication_author`,`author_student`,`author_teacher`) values(?,?,?,?,?,?)";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return authorIds.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i, awardId);
						pst.setInt(++i, authorIds[index]);
						pst.setInt(++i, order[index]);
						pst.setString(++i, Boolean.toString(communicationAuthors[index]));
						pst.setString(++i, Boolean.toString(authorStudents[index]));
						pst.setString(++i, Boolean.toString(authorTeacher[index]));
					}
				});

	}

	@Override
	public void deleteRef(int awardId) {
		String sql="delete from institution_award_author_ref where award_id=:awardId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("awardId", awardId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);

	}

	
	@Override
	public boolean isExist(InstitutionAwardAuthor author) {
		String sql="select * "+ 
				   "from  institution_author "+ 
				   "where name=:name and email=:email "+
				   "limit 1";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("name", author.getAuthorName());
		paramMap.put("email", author.getAuthorEmail());
		paramMap.put("company", author.getAuthorCompany());
		InstitutionAwardAuthor temp = CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap, new RowMapper<InstitutionAwardAuthor>() {
			@Override
			public InstitutionAwardAuthor mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				InstitutionAwardAuthor au=new InstitutionAwardAuthor();
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
	public void updateRef(int authorId, int awardId,int order) {
		StringBuffer sb=new StringBuffer();
		sb.append("update institution_award_author_ref set ");
		sb.append("`order`=:authorOrder ");
		sb.append("where author_id=:authorId and award_id=:awardId");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("authorId", authorId);
		params.put("authorOrder", order);
		params.put("awardId", awardId);
		getNamedParameterJdbcTemplate().update(sb.toString(),params);
		
	}
}
