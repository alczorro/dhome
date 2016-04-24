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
import net.duckling.dhome.dao.IInstitutionBackendPaperAuthorDAO;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.SearchInstitutionPaperCondition;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.domain.people.SimpleUser;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class IInstitutionBackendPaperAuthorDAOImpl extends BaseDao implements IInstitutionBackendPaperAuthorDAO{
	private RowMapper<InstitutionAuthor> rowMapper=new RowMapper<InstitutionAuthor>() {
		@Override
		public InstitutionAuthor mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionAuthor au=new InstitutionAuthor();
			au.setId(rs.getInt("id"));
			au.setAuthorName(rs.getString("name"));
			au.setAuthorEmail(rs.getString("email"));
			au.setAuthorCompany(rs.getString("company"));
			au.setStatus(rs.getInt("status"));
			au.setCstnetId(rs.getString("cstnet_id"));
			au.setInstitutionId(rs.getInt("institution_id"));
			try{
				au.setOrder(rs.getInt("order"));
				au.setCommunicationAuthor(rs.getBoolean("communication_author"));
				au.setAuthorStudent(rs.getBoolean("author_student"));
				au.setAuthorTeacher(rs.getBoolean("author_teacher"));
				au.setPaperId(rs.getInt("paper_id"));
			}catch(SQLException e){
				
			}
			return au;
		} 
	};
	@Override
	public List<InstitutionAuthor> search(String keyword) {
		String sql="select * from institution_author where name like :keyword or email like :keyword or company like :keyword";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("keyword", "%"+keyword+"%");
		return getNamedParameterJdbcTemplate().query(sql,param,rowMapper);
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
	public void insertAuthorUser(final String cstnetId, final int institutionId, final String[] authorId,
			final int status) {
		String sql="update institution_author set cstnet_id=?,institution_id=?,status=? where id=?";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return authorId.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setString(++i, cstnetId);
						pst.setInt(++i, institutionId);
						pst.setInt(++i, status);
						pst.setInt(++i, Integer.parseInt(authorId[index]));
					}
				});
	}
	
	@Override
	public void cancelAuthorUser(final int institutionId, final String[] authorId) {
		String sql="update institution_author set cstnet_id=null,institution_id=?,status=null where id=?";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return authorId.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i, institutionId);
						pst.setInt(++i, Integer.parseInt(authorId[index]));
					}
				});
	}
	@Override
	public void createRef(final int paperId, final int[] uids,final int[] order, final boolean[] communicationAuthors,
			final boolean[] authorStudents, final boolean[] authorTeacher) {
		String sql="insert into institution_paper_author_ref(`paper_id`,`author_id`,`order`,`communication_author`,`author_student`,`author_teacher`) values(?,?,?,?,?,?)";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return uids.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i, paperId);
						pst.setInt(++i, uids[index]);
						pst.setInt(++i, order[index]);
						if(communicationAuthors.length > index){
							pst.setString(++i, Boolean.toString(communicationAuthors[index]));
						}else{
							pst.setString(++i,Boolean.toString(false));
						}
						
						if(authorStudents.length > index){
							pst.setString(++i, Boolean.toString(authorStudents[index]));
						}else{
							pst.setString(++i,Boolean.toString(false));
						}
						
						if(authorTeacher.length > index){
							pst.setString(++i, Boolean.toString(authorTeacher[index]));
						}else{
							pst.setString(++i,Boolean.toString(false));
						}
					}
				});
	}
	
	@Override
	public List<InstitutionAuthor> getByPaperId(int paperId) {
		String sql="select * "+ 
		"from  institution_author a left join institution_paper_author_ref r "+ 
		"on r.author_id=a.id "+ 
		"where r.paper_id=:paperId "+ 
		"order by a.id ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("paperId", paperId);
		return getNamedParameterJdbcTemplate().query(sql,paramMap,rowMapper);
	}
	@Override
	public List<InstitutionAuthor> getByCstnetId(String cstnetId) {
		String sql="select * "+ 
		"from  institution_author "+ 
		"where cstnet_id=:cstnetId ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("cstnetId", cstnetId);
		return getNamedParameterJdbcTemplate().query(sql,paramMap,rowMapper);
	}
	@Override
	public void deleteRef(int paperId) {
		String sql="delete from institution_paper_author_ref where paper_id=:paperId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("paperId", paperId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}
	@Override
	public List<InstitutionAuthor> getByPaperIds(List<Integer> paperIds) {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		StringBuffer sb=new StringBuffer("select * "); 
		sb.append("from  institution_author a left join institution_paper_author_ref r ");
		sb.append("on r.author_id=a.id ");
		sb.append("where r.paper_id in( ");
		int index=0;
		for(int paperId:paperIds){
			sb.append(":paperId"+index).append(",");
			paramMap.put("paperId"+index, paperId);
			index++;
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(") ");
		sb.append("order by r.paper_id,r.order ");
		return getNamedParameterJdbcTemplate().query(sb.toString(),paramMap,rowMapper);
	}
	@Override
	public InstitutionAuthor getById(int paperId,int authorId) {
		String sql="select * "+ 
		"from  institution_author a left join institution_paper_author_ref r "+ 
		"on r.author_id=a.id "+ 
		"where r.paper_id=:paperId "+ 
		"and r.author_id=:authorId "+
		"limit 1";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("paperId", paperId);
		paramMap.put("authorId", authorId);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper));
	}
	@Override
	public InstitutionAuthor getById(int authorId) {
		String sql="select * "+ 
				   "from  institution_author "+ 
				   "where id=:id "+
				   "limit 1";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("id", authorId);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper));
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
	public List<InstitutionAuthor> getAuthors(int institutionId,
			 int offset, int size,SearchInstitutionPaperCondition condition) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from institution_author where institution_id=:institutionId");
		if(condition.getStatus()==1){
			sb.append(" and status=:status");
		}
		if(condition.getStatus()==2){
			sb.append(" and status is null");
		}
		if(condition.getStatus()==-1){
			sb.append(" and status=:status");
		}
		//关键字
		if(!CommonUtils.isNull(condition.getKeyword())){
			sb.append(" and name like :keyword ");
		}
		sb.append(" limit :offset,:size");
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("institutionId", institutionId);
		paramMap.put("offset", offset);
		paramMap.put("status", condition.getStatus());
		paramMap.put("size", size);
		paramMap.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
		return getNamedParameterJdbcTemplate().query(sb.toString(), paramMap, rowMapper);
	}

	@Override
	public int getAuthorsCount(int institutionId, SearchInstitutionPaperCondition condition) {
		StringBuffer sb = new StringBuffer();
		sb.append("select count(*) from institution_author where institution_id=:institutionId");
		if(condition.getStatus()==1){
			sb.append(" and status=:status");
		}
		if(condition.getStatus()==2){
			sb.append(" and status is null");
		}
		if(condition.getStatus()==-1){
			sb.append(" and status=:status");
		}
		//关键字
		if(!CommonUtils.isNull(condition.getKeyword())){
			sb.append(" and name like :keyword ");
		}
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("institutionId", institutionId);
		paramMap.put("status", condition.getStatus());
		paramMap.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
		return getNamedParameterJdbcTemplate().queryForInt(sb.toString(), paramMap);
	}

	@Override
	public void updateAuthorPropelling(int authorId) {
		String sql="update institution_author set cstnet_id=null,status=null where id=:authorId";
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("authorId", authorId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}

	@Override
	public List<InstitutionAuthor> getAuthorsByUid(int userId) {
		String sql="select * from institution_author a,institution_member_from_vmt b " +
				"where a.cstnet_id=b.cstnet_id and b.uid=:userId";
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId", userId);
		return getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper);
	}

	@Override
	public int insertAuthor(String name) {
		String sql="insert into institution_author(`name`) select :name from dual where" +
				" not exists(select * from institution_author where name=:name and company is null)";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("name",name);
//		map.put("company",company);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		 getNamedParameterJdbcTemplate().update(sql, new MapSqlParameterSource(map), keyHolder);
		 if(keyHolder.getKey()==null){
	        	return 0;
	        }else{
	        	return keyHolder.getKey().intValue();
	        }
	}

	@Override
	public InstitutionAuthor getAuthor(String name) {
		String sql="select * "+ 
				   "from  institution_author "+ 
				   "where name=:name and company is null ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("name", name);
//		paramMap.put("company", company);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper));
	}

	@Override
	public void insertRef(int paperId, int authorId, int order) {
		String sql="insert into institution_paper_author_ref(`author_id`,`paper_id`,`order`) select :authorId,:paperId,:order from dual where "+
				"not exists(select * from institution_paper_author_ref where author_id=:authorId and paper_id=:paperId and `order`=:order)";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("authorId",authorId);
		paramMap.put("paperId",paperId);
		paramMap.put("order",order);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}
	

}
