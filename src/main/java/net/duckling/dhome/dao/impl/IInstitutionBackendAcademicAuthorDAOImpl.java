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
import net.duckling.dhome.dao.IInstitutionBackendAcademicAuthorDAO;
import net.duckling.dhome.domain.institution.InstitutionAcademicAuthor;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class IInstitutionBackendAcademicAuthorDAOImpl extends BaseDao implements
		IInstitutionBackendAcademicAuthorDAO {

	private static final RowMapper<InstitutionAcademicAuthor> rowMapper=new RowMapper<InstitutionAcademicAuthor>() {
		@Override
		public InstitutionAcademicAuthor mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionAcademicAuthor au=new InstitutionAcademicAuthor();
			au.setAcedemicId(rs.getInt("academic_assignment_id"));
			au.setUmtId(rs.getString("umt_id"));
			au.setTrueName(rs.getString("true_name"));
			au.setCstnetId(rs.getString("cstnet_id"));
//			au.setAuthorCompany(rs.getString("company"));
//			try{
//				au.setOrder(rs.getInt("order"));
//				au.setCommunicationAuthor(rs.getBoolean("communication_author"));
//				au.setAuthorStudent(rs.getBoolean("author_student"));
//				au.setAuthorTeacher(rs.getBoolean("author_teacher"));
//				au.setAcedemicId(rs.getInt("academic_assignment_id"));
//			}catch(SQLException e){
//				
//			}
			return au;
		} 
	};
//	
//	@Override
//	public List<InstitutionAcademicAuthor> search(String keyword) {
//		String sql="select * from institution_member_from_vmt where name like :keyword or email like :keyword or company like :keyword";
//		Map<String,Object> param=new HashMap<String,Object>();
//		param.put("keyword", "%"+keyword+"%");
//		return getNamedParameterJdbcTemplate().query(sql,param,rowMapper);
//	}

	@Override
	public List<InstitutionAcademicAuthor> getByAcedemicId(int aceId) {
		String sql="select * "+ 
				"from  institution_member_from_vmt a left join institution_academic_author_ref r "+ 
				"on r.author_id=a.umt_id "+ 
				"where r.academic_assignment_id=:aceId "
				+ "order by a.umt_id  ";	
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("aceId", aceId);
		return getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper);
	}

	@Override
	public List<InstitutionAcademicAuthor> getByAcedemicIds(List<Integer> aceIds) {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		StringBuffer sb=new StringBuffer("select * "); 
		sb.append("from  institution_member_from_vmt a left join institution_academic_author_ref r ");
		sb.append("on r.author_id=a.umt_id ");
		sb.append("where r.academic_assignment_id in( ");
		int index=0;
		for(int aceId:aceIds){
			sb.append(":aceId"+index).append(",");
			paramMap.put("aceId"+index, aceId);
			index++;
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(") ");
		sb.append("order by r.academic_assignment_id");
		return getNamedParameterJdbcTemplate().query(sb.toString(),paramMap,rowMapper);
	}

//	@Override
//	public void createAuthor(InstitutionAcademicAuthor author) {
//		String sql="insert into institution_author(`name`,`email`,`company`) values(:name,:email,:company)";
//		Map<String,Object> map=new HashMap<String,Object>();
//		map.put("name",author.getAuthorName());
//		map.put("email",author.getAuthorEmail());
//		map.put("company",author.getAuthorCompany());
//		
//		KeyHolder keyHolder = new GeneratedKeyHolder();
//	    getNamedParameterJdbcTemplate().update(sql, new MapSqlParameterSource(map), keyHolder);
//	    author.setId(keyHolder.getKey().intValue());
//	}

	
	@Override
	public void createRef(final int aceId, final String[] authorIds) {
		String sql="insert into institution_academic_author_ref(`academic_assignment_id`,`author_id`) values(?,?)";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
			public int getBatchSize() {
				return authorIds.length;
			}
			public void setValues(PreparedStatement pst, int index)
					throws SQLException {
				int i = 0;
				pst.setInt(++i, aceId);
				pst.setString(++i, authorIds[index]);
			}
		});
	}

	@Override
	public void deleteRef(int aceId) {
		String sql="delete from institution_academic_author_ref where academic_assignment_id=:aceId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("aceId", aceId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}
	
	@Override
	public InstitutionAcademicAuthor getById(int aceId, String authorId) {
		String sql="select * "+ 
				"from  institution_member_from_vmt a left join institution_academic_author_ref r "+ 
				"on r.author_id=a.umt_id "+ 
				"where r.academic_assignment_id=:aceId "+ 
				"and r.author_id=:authorId "+
				"limit 1";
				Map<String,Object> paramMap=new HashMap<String,Object>();
				paramMap.put("aceId", aceId);
				paramMap.put("authorId", authorId);
				return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper));
	
	}


	@Override
	public InstitutionAcademicAuthor getById(String authorId) {
		String sql="select * "+ 
				   "from  institution_member_from_vmt "+ 
				   "where umt_id=:id "+
				   "limit 1";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("id", authorId);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper));
	}
	
	
//	@Override
//	public boolean isExist(InstitutionAcademicAuthor author) {
//		String sql="select * "+ 
//				   "from  institution_author "+ 
//				   "where name=:name and email=:email "+
//				   "limit 1";
//		Map<String,Object> paramMap=new HashMap<String,Object>();
//		paramMap.put("name", author.getAuthorName());
//		paramMap.put("email", author.getAuthorEmail());
//		paramMap.put("company", author.getAuthorCompany());
//		InstitutionAcademicAuthor temp = CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap, new RowMapper<InstitutionAcademicAuthor>() {
//			@Override
//			public InstitutionAcademicAuthor mapRow(ResultSet rs, int rowNum)
//					throws SQLException {
//				InstitutionAcademicAuthor au=new InstitutionAcademicAuthor();
//				au.setId(rs.getInt("id"));
//				au.setAuthorName(rs.getString("name"));
//				au.setAuthorEmail(rs.getString("email"));
//				au.setAuthorCompany(rs.getString("company"));
//				return au;
//			} 
//		}));
//		
//		if(temp != null){
//			author.setId(temp.getId());
//			return true;
//		}else{
//			return false;
//		}
//	}

}
