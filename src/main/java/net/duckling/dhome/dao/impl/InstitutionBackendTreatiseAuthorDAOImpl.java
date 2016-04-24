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
import net.duckling.dhome.dao.IInstitutionBackendTreatiseAuthorDAO;
import net.duckling.dhome.domain.institution.InstitutionTreatiseAuthor;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class InstitutionBackendTreatiseAuthorDAOImpl extends BaseDao implements
		IInstitutionBackendTreatiseAuthorDAO {
	
	private static final RowMapper<InstitutionTreatiseAuthor> rowMapper=new RowMapper<InstitutionTreatiseAuthor>() {
		@Override
		public InstitutionTreatiseAuthor mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionTreatiseAuthor au=new InstitutionTreatiseAuthor();
			au.setId(rs.getInt("id"));
			au.setAuthorName(rs.getString("name"));
			au.setAuthorEmail(rs.getString("email"));
			au.setAuthorCompany(rs.getString("company"));
			try{
				au.setOrder(rs.getInt("order"));
				au.setCommunicationAuthor(rs.getBoolean("communication_author"));
				au.setAuthorStudent(rs.getBoolean("author_student"));
				au.setAuthorTeacher(rs.getBoolean("author_teacher"));
				au.setTreatiseId(rs.getInt("treatise_id"));
			}catch(SQLException e){
				
			}
			return au;
		} 
	};
	
	@Override
	public List<InstitutionTreatiseAuthor> search(String keyword) {
		String sql="select * from institution_author where name like :keyword or email like :keyword or company like :keyword";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("keyword", "%"+keyword+"%");
		return getNamedParameterJdbcTemplate().query(sql,param,rowMapper);
	}

	@Override
	public List<InstitutionTreatiseAuthor> getByTreatiseId(int treatiseId) {
		String sql="select * "+ 
				"from  institution_author a left join institution_treatise_author_ref r "+ 
				"on r.author_id=a.id "+ 
				"where r.treatise_id=:treatiseId "
				+ "order by a.id  ";	
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("treatiseId", treatiseId);
		return getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper);
	}

	@Override
	public List<InstitutionTreatiseAuthor> getByTreatiseIds(
			List<Integer> treatiseIds) {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		StringBuffer sb=new StringBuffer("select * "); 
		sb.append("from  institution_author a left join institution_treatise_author_ref r ");
		sb.append("on r.author_id=a.id ");
		sb.append("where r.treatise_id in( ");
		int index=0;
		for(int treatiseId:treatiseIds){
			sb.append(":treatiseId"+index).append(",");
			paramMap.put("treatiseId"+index, treatiseId);
			index++;
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(") ");
		sb.append("order by r.treatise_id");
		return getNamedParameterJdbcTemplate().query(sb.toString(),paramMap,rowMapper);
	}

	@Override
	public void createAuthor(InstitutionTreatiseAuthor author) {
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
	public InstitutionTreatiseAuthor getById(int treatiseId, int authorId) {
		String sql="select * "+ 
				"from  institution_author a left join institution_treatise_author_ref r "+ 
				"on r.author_id=a.id "+ 
				"where r.treatise_id=:treatiseId "+ 
				"and r.author_id=:authorId "+
				"limit 1";
				Map<String,Object> paramMap=new HashMap<String,Object>();
				paramMap.put("treatiseId", treatiseId);
				paramMap.put("authorId", authorId);
				return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper));
	}

	@Override
	public InstitutionTreatiseAuthor getById(int authorId) {
		String sql="select * "+ 
				   "from  institution_author "+ 
				   "where id=:id "+
				   "limit 1";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("id", authorId);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper));
	}

	@Override
	public void createRef(final int treatiseId, final int[] authorIds, final int[] order,
			final boolean[] communicationAuthors, final boolean[] authorStudents,
			final boolean[] authorTeacher) {
		String sql="insert into institution_treatise_author_ref(`treatise_id`,`author_id`,`order`,`communication_author`,`author_student`,`author_teacher`) values(?,?,?,?,?,?)";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return authorIds.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i, treatiseId);
						pst.setInt(++i, authorIds[index]);
						pst.setInt(++i, order[index]);
						pst.setString(++i, Boolean.toString(communicationAuthors[index]));
						pst.setString(++i, Boolean.toString(authorStudents[index]));
						pst.setString(++i, Boolean.toString(authorTeacher[index]));
					}
				});

	}

	@Override
	public void deleteRef(int treatiseId) {
		String sql="delete from institution_treatise_author_ref where treatise_id=:treatiseId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("treatiseId", treatiseId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);

	}
	
	@Override
	public boolean isExist(InstitutionTreatiseAuthor author) {
		String sql="select * "+ 
				   "from  institution_author "+ 
				   "where name=:name and email=:email "+
				   "limit 1";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("name", author.getAuthorName());
		paramMap.put("email", author.getAuthorEmail());
		paramMap.put("company", author.getAuthorCompany());
		InstitutionTreatiseAuthor temp = CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap, new RowMapper<InstitutionTreatiseAuthor>() {
			@Override
			public InstitutionTreatiseAuthor mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				InstitutionTreatiseAuthor au=new InstitutionTreatiseAuthor();
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
	public void updateRef(int authorId, int tid,int order) {
		StringBuffer sb=new StringBuffer();
		sb.append("update institution_treatise_author_ref set ");
		sb.append("`order`=:authorOrder ");
		sb.append("where author_id=:authorId and treatise_id=:tid");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("authorId", authorId);
		params.put("authorOrder", order);
		params.put("tid", tid);
		getNamedParameterJdbcTemplate().update(sb.toString(),params);
		
	}
	@Override
	public int insertAuthor(String name,String company) {
		String sql="insert into institution_author(`name`,`company`) " +
				"select :name,:company from dual where not exists(select * from institution_author where name=:name and company=:company)";
		Map<String,Object> map=new HashMap<String,Object>();
		boolean flag=(company==null&&"".equals(company));
		map.put("name",name);
		if(!flag){
			map.put("company",company);
		}
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
	    getNamedParameterJdbcTemplate().update(getSql(flag), new MapSqlParameterSource(map), keyHolder);
	    if(keyHolder.getKey()!=null){
	    	return keyHolder.getKey().intValue();
	    }else{
	    	return getAuthorId(name,company);
	    }
	    
	}
	public String getSql(boolean isNull){
		String sql="insert into institution_author(`name`,`company`) " +
				"select :name,:company from dual where not exists(select * from institution_author where name=:name and company=:company)";
		String sql2="insert into institution_author(`name`) " +
				"select :name from dual where not exists(select * from institution_author where name=:name)";
		return (isNull?sql2:sql);
	}
	@Override
	public int getAuthorId(String name,String company) {
		String sql="select id from institution_author where name=:name and company=:company";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("name",name);
		map.put("company",company);
		return getNamedParameterJdbcTemplate().queryForInt(sql,map);
	}
	@Override
	public void insertRef(int treatiseId,int authorId,int order) {
		String sql="insert into institution_treatise_author_ref(`treatise_id`,`author_id`,`order`) " +
				"select :treatiseId,:authorId,:order from dual where not exists(select * from institution_treatise_author_ref where " +
				"treatise_id=:treatiseId and `order`=:order)";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("treatiseId",treatiseId);
		map.put("authorId",authorId);
		map.put("order",order);
		KeyHolder keyHolder = new GeneratedKeyHolder();
	    getNamedParameterJdbcTemplate().update(sql, new MapSqlParameterSource(map), keyHolder);
	    if(keyHolder.getKey()==null){
	    	updateImportRef(treatiseId,authorId,order);
	    }
	}
	@Override
	public void updateImportRef(int treatiseId,int authorId,int order) {
		String sql="update institution_treatise_author_ref set treatise_id=:treatiseId,author_id=:authorId,`order`=:order " +
				"where treatise_id=:treatiseId and `order`=:order";
				
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("treatiseId",treatiseId);
		map.put("authorId",authorId);
		map.put("order",order);
		
		getNamedParameterJdbcTemplate().update(sql,map);
	}
}
