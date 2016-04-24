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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.dao.IInstitutionPatentDAO;
import net.duckling.dhome.domain.institution.InstitutionAward;
import net.duckling.dhome.domain.institution.InstitutionPatent;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;

@Repository
public class InstitutionPatentDAOImpl extends BaseDao implements IInstitutionPatentDAO {

private static final RowMapper<InstitutionPatent> rowMapper=new RowMapper<InstitutionPatent>(){
		
		@Override
		public InstitutionPatent mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionPatent patent=new InstitutionPatent();
			patent.setId(rs.getInt("id"));
			patent.setName(rs.getString("name"));
			patent.setType(rs.getInt("type"));
			patent.setGrade(rs.getInt("grade"));
			patent.setYear(rs.getInt("year"));
			patent.setInstitutionId(rs.getInt("institution_id"));
			patent.setCompanyOrder(rs.getInt("company_order"));
			patent.setDepartId(rs.getInt("depart_id"));
			patent.setApplyNo(rs.getString("apply_no"));
			patent.setStatus(rs.getInt("status"));
			return patent;
		}};
	
	@Override
	public List<InstitutionPatent> getPatent(int id, int offset, int size, 
			SearchInstitutionCondition condition) {
//		String sql="select * from institution_patent where institution_id=:id limit :offset,:size";
//		System.out.println(getListSql(true, condition)+"=====");
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("insId", id);
		param.put("offset",offset);
		param.put("size", size);
		param.put("grade", condition.getGrade());
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
//		return getNamedParameterJdbcTemplate().query(sql, param, rowMapper);
		return getNamedParameterJdbcTemplate().query(getListSql(false, condition), param, rowMapper);
	}

	@Override
	public int getPatentCount(int insId, SearchInstitutionCondition condition) {
//		String sql="select count(*) from institution_patent where institution_id=:insId ";
//		System.out.println(getListSql(true, condition)+"///////");
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("insId", insId);
		param.put("grade", condition.getGrade());
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
//		return getNamedParameterJdbcTemplate().queryForInt(sql,param);
		return getNamedParameterJdbcTemplate().queryForInt(getListSql(true, condition),param);
	}
	private String getListSql(boolean isCount,SearchInstitutionCondition condition){
		StringBuffer sb=new StringBuffer();
		sb.append(" select ").append(isCount?"count(*)":"*").append(" from institution_patent ");
		sb.append(" where institution_id=:insId");
		//关键字
		if(!CommonUtils.isNull(condition.getKeyword())){
			sb.append(" and name like :keyword ");
		}
		//等级
		if(condition.getGrade()!=-1){
			sb.append(" and grade=:grade ");
		}
		if(!isCount){
			//排序
			sb.append(" order by "+ getOrderColumnByStatus(condition.getOrder())+getOrderTypeByType(condition.getOrderType()));
			sb.append(" limit :offset,:size");
		}
		return sb.toString();
	}
	
	@Override
	public List<InstitutionPatent> getPatentByUser(int userId, int offset, int size, 
			SearchInstitutionCondition condition) {
//		String sql="select * from institution_patent where institution_id=:id limit :offset,:size";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("userId", userId);
		param.put("offset",offset);
		param.put("size", size);
		param.put("grade", condition.getGrade());
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
//		return getNamedParameterJdbcTemplate().query(sql, param, rowMapper);
		return getNamedParameterJdbcTemplate().query(getListSqlByUser(false, condition), param, rowMapper);
	}

	@Override
	public int getPatentCountByUser(int userId, SearchInstitutionCondition condition) {
//		String sql="select count(*) from institution_patent where institution_id=:insId ";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("userId", userId);
		param.put("grade", condition.getGrade());
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
//		return getNamedParameterJdbcTemplate().queryForInt(sql,param);
		return getNamedParameterJdbcTemplate().queryForInt(getListSqlByUser(true, condition),param);
	}
	private String getListSqlByUser(boolean isCount,SearchInstitutionCondition condition){
		StringBuffer sb=new StringBuffer();
		sb.append(" select ").append(isCount?"count(*)":"r.*").append(" from institution_patent r,institution_patent_user u");
		sb.append(" where r.id=u.patent_id");
		sb.append(" and u.user_id=:userId");
		//关键字
		if(!CommonUtils.isNull(condition.getKeyword())){
			sb.append(" and name like :keyword ");
		}
		//等级
		if(condition.getGrade()!=-1){
			sb.append(" and grade=:grade ");
		}
		if(!isCount){
			//排序
			sb.append(" order by "+ getOrderColumnByStatus(condition.getOrder())+getOrderTypeByType(condition.getOrderType()));
			sb.append(" limit :offset,:size");
		}
		return sb.toString();
	}

	@Override
	public void deletePatent(final int[] id) {
		String sql="delete from institution_patent where id=?";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return id.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i,id[index]);
					}
				});
	}

	@Override
	public void updatePatent(int id, InstitutionPatent patent) {
		String sql="Update institution_patent set "
				+"name=:name,"
				+"type=:type,"
				+"grade=:grade,"
				+"year=:year,"
				+"depart_id=:departId,"
				+"apply_no=:applyNo,"
				+"status=:status,"
				+"company_order=:company_order"
				+" where id=:id";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("name", patent.getName());
		param.put("type", patent.getType());
		param.put("grade", patent.getGrade());
		param.put("year", patent.getYear());
		param.put("departId", patent.getDepartId());
		param.put("applyNo", patent.getApplyNo());
		param.put("status", patent.getStatus());
		param.put("company_order", patent.getCompanyOrder());
		param.put("id", id);
		getNamedParameterJdbcTemplate().update(sql, param);
	}

	@Override
	public void insert(List<InstitutionPatent> topic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int creat(InstitutionPatent patent) {
		StringBuffer sb=new StringBuffer();
		sb.append("insert into institution_patent(");
		sb.append("`name`,");
		sb.append("`type`,");
		sb.append("`grade`,");
		sb.append("`year`,");
		sb.append("`depart_id`,");
		sb.append("`apply_no`,");
		sb.append("`institution_id`,");
		sb.append("`status`,");
		sb.append("`company_order`)");
		
		sb.append("values(");
		
		sb.append(":name,");
		sb.append(":type,");
		sb.append(":grade,");
		sb.append(":year,");
		sb.append(":departId,");
		sb.append(":applyNo,");
		sb.append(":institutionId,");
		sb.append(":status,");
		sb.append(":companyOrder) ");
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("name",patent.getName());
		paramMap.put("type",patent.getType());
		paramMap.put("grade",patent.getGrade());
		paramMap.put("year",patent.getYear());
		paramMap.put("departId",patent.getDepartId());
		paramMap.put("applyNo",patent.getApplyNo());
		paramMap.put("institutionId",patent.getInstitutionId());
		paramMap.put("status",patent.getStatus());
		paramMap.put("companyOrder",patent.getCompanyOrder());
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getNamedParameterJdbcTemplate().update(sb.toString(),new MapSqlParameterSource(patentMap(patent)), keyHolder);
		return keyHolder.getKey().intValue();
		
	}
	public Map<String, Object> patentMap(InstitutionPatent patent){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", patent.getId());
		params.put("name", patent.getName());
		params.put("type",patent.getType());
		params.put("grade",patent.getGrade());
		params.put("companyOrder",patent.getCompanyOrder());
		params.put("institutionId",patent.getInstitutionId());
		params.put("year",patent.getYear());
		params.put("departId",patent.getDepartId());
		params.put("applyNo",patent.getApplyNo());
		params.put("status",patent.getStatus());
		return params;
	}

	@Override
	public InstitutionPatent getPatentById(int id) {
		String sql="select * from institution_patent where id=:id ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("id",id);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap,rowMapper));
	}

	@Override
	public Map<Integer, Integer> getGradesMap(int institutionId) {
		String sql="select grade,count(*) c from institution_patent "+
				"where institution_id=:insId "+
				"group by grade ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", institutionId);
		final Map<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
		getNamedParameterJdbcTemplate().query(sql, paramMap, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				result.put(rs.getInt("grade"), rs.getInt("c"));
				return null;
			}
		});
		return result;
	}
	@Override
	public Map<Integer, Integer> getGradesMapByUser(int userId) {
		String sql="select r.grade,count(*) c from institution_patent r,institution_patent_user u "+
				"where u.user_id=:userId "+
				"and u.patent_id=r.id "+
				"group by r.grade ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId", userId);
		final Map<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
		getNamedParameterJdbcTemplate().query(sql, paramMap, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				result.put(rs.getInt("grade"), rs.getInt("c"));
				return null;
			}
		});
		return result;
	}

	@Override
	public void deletePatentUser(final int[] id) {
		String sql="delete from institution_patent_user where patent_id=?";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return id.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i,id[index]);
					}
				});
	}

	@Override
	public void insertPatentUser(int patentId, int userId) {
		String sql="insert into institution_patent_user(`user_id`,`patent_id`)"+
				" values(:userId,:patentId)";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId",userId);
		paramMap.put("patentId",patentId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}

	@Override
	public List<InstitutionPatent> getPatentsByUID(int uid) {
		String sql="select * from institution_patent_user u "+
				"left join institution_patent c "+
				"on u.patent_id=c.id "+
				"where u.user_id=:uid ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("uid", uid);
		return getNamedParameterJdbcTemplate().query(sql, paramMap,rowMapperUID);
	}

private static final RowMapper<InstitutionPatent> rowMapperUID=new RowMapper<InstitutionPatent>(){
		
		@Override
		public InstitutionPatent mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionPatent patent=new InstitutionPatent();
			patent.setId(rs.getInt("patent_id"));
			patent.setName(rs.getString("name"));
			patent.setType(rs.getInt("type"));
			patent.setGrade(rs.getInt("grade"));
			patent.setYear(rs.getInt("year"));
			patent.setInstitutionId(rs.getInt("institution_id"));
			patent.setCompanyOrder(rs.getInt("company_order"));
			patent.setPersonalOrder(rs.getInt("author_order"));
			return patent;
		}};
		
		
		
		@Override
		public void createUserRef(int userId, int patentId,int authorOrder) {
			String sql="insert into institution_patent_user(`user_id`,`patent_id`,`author_order`)"+
					" values(:userId,:patentId,:authorOrder)";
			Map<String,Object> paramMap=new HashMap<String,Object>();
			paramMap.put("userId",userId);
			paramMap.put("patentId",patentId);
			paramMap.put("authorOrder",authorOrder);
			getNamedParameterJdbcTemplate().update(sql, paramMap);
		}

		@Override
		public void deleteUserRef(int uid,int patentId) {
			String sql="delete from institution_patent_user where user_id=:uid and patent_id=:patentId";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("uid", uid);
			params.put("patentId", patentId);
			getNamedParameterJdbcTemplate().update(sql, params);
		}

		@Override
		public void updateUserRef(int uid, int patentId, int authorOrder) {
			StringBuffer sb=new StringBuffer();
			sb.append("update institution_patent_user set ");
			sb.append("`author_order`=:authorOrder ");
			sb.append("where user_id=:uid and patent_id=:patentId");

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("uid", uid);
			params.put("patentId", patentId);
			params.put("authorOrder", authorOrder);
			getNamedParameterJdbcTemplate().update(sb.toString(),params);
		}
		
		private String getOrderTypeByType(int orderType){
			switch(orderType){
				case SearchInstitutionCondition.ORDER_TYPE_ASC:{
					return " asc ";
				}
				case SearchInstitutionCondition.ORDER_TYPE_DESC:{
					return " desc ";
				}
				default:{
					return " desc ";
				}
			}
		}
		
		private String getOrderColumnByStatus(int order){
			switch(order){
				case SearchInstitutionCondition.ORDER_ID:{
					return "id";
				}
				default:{
					return "id";
				}
			}
		}
		@Override
		public int getPatentCount(int insId, int departId, int year) {
			String sql="select count(*) from institution_patent where institution_id=:insId ";
			StringBuilder sb=new StringBuilder();
			sb.append(sql);
			if(departId!=-1){
				sb.append(" and depart_id=:departId");
			}
			if(year!=-1){
				sb.append(" and year=:year");
			}
			Map<String,Object> paramMap=new HashMap<String,Object>();
			paramMap.put("insId", insId);
			paramMap.put("departId", departId);
			paramMap.put("year", year);
			return getNamedParameterJdbcTemplate().queryForInt(sb.toString(),paramMap);
		}

		@Override
		public List<InstitutionPatent> getPatentsByAuthorId(int authorId,
				int startYear, int endYear) {
			StringBuffer sb = new StringBuffer();
			sb.append("select * from institution_patent p,institution_patent_author_ref r where p.id=r.patent_id ");
			sb.append("and p.year>=:startYear and p.year<=:endYear ");
			sb.append("and r.author_id=:authorId ");
			
			Map<String, Object> paramMap=new HashMap<String,Object>();
			paramMap.put("authorId", authorId);
			paramMap.put("startYear", startYear);
			paramMap.put("endYear", endYear);
			return getNamedParameterJdbcTemplate().query(sb.toString(), paramMap, rowMapper);
		}
}
