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
import net.duckling.dhome.dao.IInstitutionBackendTopicAuthorDAO;
import net.duckling.dhome.domain.institution.InstitutionAuthor;

@Repository
public class InstitutionBackendTopicAuthorDAOImpl extends BaseDao implements
		IInstitutionBackendTopicAuthorDAO {
	private RowMapper<InstitutionAuthor> rowMapper=new RowMapper<InstitutionAuthor>() {
		@Override
		public InstitutionAuthor mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionAuthor au=new InstitutionAuthor();
			au.setAuthorName(rs.getString("name"));
			au.setAuthorType(rs.getString("author_type"));
			au.setTopicId(rs.getInt("topic_id"));
			au.setAuthorEmail(rs.getString("email"));
			au.setAuthorCompany(rs.getString("company"));
			au.setId(rs.getInt("author_id"));
			return au;
		} 
	};

	@Override
	public List<InstitutionAuthor> getAuthorListByTopicId(int topicId) {
		String sql="select * from institution_author a,institution_topic_author_ref b "
				+"where a.id=b.author_id and b.topic_id=:topic_id";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("topic_id", topicId);
		return getNamedParameterJdbcTemplate().query(sql, param, rowMapper);
	}
	
	@Override
	public InstitutionAuthor getById(int topicId,int authorId) {
		String sql="select * "+ 
		"from  institution_author a left join institution_topic_author_ref r "+ 
		"on r.author_id=a.id "+ 
		"where r.topic_id=:topicId "+ 
		"and r.author_id=:authorId "+
		"limit 1";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("topicId", topicId);
		paramMap.put("authorId", authorId);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper));
	}

	@Override
	public List<InstitutionAuthor> getAuthorListByTopicIds(List<Integer> topicIds) {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		StringBuffer sb=new StringBuffer("select * "); 
		sb.append("from  institution_author a join institution_topic_author_ref b ");
		sb.append("on b.author_id=a.id ");
		sb.append("where b.topic_id in( ");
		int index=0;
		for(int topicId:topicIds){
			sb.append(":topicId"+index).append(",");
			paramMap.put("topicId"+index, topicId);
			index++;
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(") ");
		sb.append("order by b.topic_id ");
		return getNamedParameterJdbcTemplate().query(sb.toString(),paramMap,rowMapper);
	}

	@Override
	public void deleteAuthorsByTopicIds(final int[] topicIds) {
		String sql="delete from institution_topic where topic_id=?";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return topicIds.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i,topicIds[index]);
					}
				});
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

//	@Override
//	public void updateRef(final int topicId, final int[] uid, final String[] authorType) {
//		String sql="update institution_topic_author_ref set author_id=? and author_type=? where topic_id=?";
//		getJdbcTemplate().batchUpdate(sql,
//				new BatchPreparedStatementSetter() {
//					public int getBatchSize() {
//						return uid.length;
//					}
//					public void setValues(PreparedStatement pst, int index)
//							throws SQLException {
//						int i = 0;
//						pst.setInt(++i,uid[index]);
//						pst.setString(++i, authorType[index]);
//						pst.setInt(++i, topicId);
//					}
//				});
//		
//	}

	@Override
	public void createRef(final int topicId, final int[] uid, final String[] authorType) {
		String sql="insert into institution_topic_author_ref(`author_id`,`topic_id`,`author_type`) values(?,?,?)";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return uid.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i,uid[index]);
						pst.setInt(++i, topicId);
						pst.setString(++i, authorType[index]);
					}
				});
	}

	@Override
	public void deleteRef(int topicId) {
		String sql="delete from institution_topic_author_ref where topic_id=:topicId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("topicId", topicId);
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

}
