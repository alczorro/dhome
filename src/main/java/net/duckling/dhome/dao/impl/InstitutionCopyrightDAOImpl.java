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
import net.duckling.dhome.dao.IInstitutionCopyrightDAO;
import net.duckling.dhome.domain.institution.InstitutionAward;
import net.duckling.dhome.domain.institution.InstitutionCopyright;
import net.duckling.dhome.domain.institution.InstitutionPatent;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;

@Repository
public class InstitutionCopyrightDAOImpl extends BaseDao implements IInstitutionCopyrightDAO {

private static final RowMapper<InstitutionCopyright> rowMapper=new RowMapper<InstitutionCopyright>(){
		
		@Override
		public InstitutionCopyright mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionCopyright copyright=new InstitutionCopyright();
			copyright.setId(rs.getInt("id"));
			copyright.setName(rs.getString("name"));
			copyright.setType(rs.getInt("type"));
			copyright.setGrade(rs.getInt("grade"));
			copyright.setYear(rs.getInt("year"));
			copyright.setInstitutionId(rs.getInt("institution_id"));
			copyright.setCompanyOrder(rs.getInt("company_order"));
			copyright.setDepartId(rs.getInt("depart_id"));
			copyright.setRegisterNo(rs.getString("register_no"));
			return copyright;
		}};
	
	@Override
	public List<InstitutionCopyright> getCopyright(int id, int offset, int size, 
			SearchInstitutionCondition condition) {
//		String sql="select * from institution_copyright where institution_id=:id limit :offset,:size";
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
	public int getCopyrightCount(int insId, SearchInstitutionCondition condition) {
//		String sql="select count(*) from institution_copyright where institution_id=:insId ";
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
		sb.append(" select ").append(isCount?"count(*)":"*").append(" from institution_copyright ");
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
	public List<InstitutionCopyright> getCopyrightByUser(int userId, int offset, int size, 
			SearchInstitutionCondition condition) {
//		String sql="select * from institution_copyright where institution_id=:id limit :offset,:size";
//		System.out.println("...."+getListSqlByUser(false, condition));
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
	public int getCopyrightCountByUser(int userId, SearchInstitutionCondition condition) {
//		String sql="select count(*) from institution_copyright where institution_id=:insId ";
//		System.out.println("/////"+getListSqlByUser(true, condition));
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("userId", userId);
		param.put("grade", condition.getGrade());
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
//		return getNamedParameterJdbcTemplate().queryForInt(sql,param);
		return getNamedParameterJdbcTemplate().queryForInt(getListSqlByUser(true, condition),param);
	}
	private String getListSqlByUser(boolean isCount,SearchInstitutionCondition condition){
		StringBuffer sb=new StringBuffer();
		sb.append(" select ").append(isCount?"count(*)":"r.*").append(" from institution_copyright r,institution_copyright_user u");
		sb.append(" where r.id=u.copyright_id");
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
	public void deleteCopyright(final int[] id) {
		String sql="delete from institution_copyright where id=?";
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
	public void updateCopyright(int id, InstitutionCopyright copyright) {
		String sql="Update institution_copyright set "
				+"name=:name,"
				+"type=:type,"
				+"grade=:grade,"
				+"year=:year,"
				+"depart_id=:departId,"
				+"register_no=:registerNo,"
				+"company_order=:company_order"
				+" where id=:id";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("name", copyright.getName());
		param.put("type", copyright.getType());
		param.put("grade", copyright.getGrade());
		param.put("year", copyright.getYear());
		param.put("departId", copyright.getDepartId());
		param.put("registerNo", copyright.getRegisterNo());
		param.put("company_order", copyright.getCompanyOrder());
		param.put("id", id);
		getNamedParameterJdbcTemplate().update(sql, param);
	}

	@Override
	public void insert(List<InstitutionCopyright> topic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int creat(InstitutionCopyright copyright) {
		StringBuffer sb=new StringBuffer();
		sb.append("insert into institution_copyright(");
		sb.append("`name`,");
		sb.append("`type`,");
		sb.append("`grade`,");
		sb.append("`year`,");
		sb.append("`depart_id`,");
		sb.append("`register_no`,");
		sb.append("`institution_id`,");
		sb.append("`company_order`)");
		
		sb.append("values(");
		
		sb.append(":name,");
		sb.append(":type,");
		sb.append(":grade,");
		sb.append(":year,");
		sb.append(":departId,");
		sb.append(":registerNo,");
		sb.append(":institutionId,");
		sb.append(":companyOrder) ");
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("name",copyright.getName());
		paramMap.put("type",copyright.getType());
		paramMap.put("grade",copyright.getGrade());
		paramMap.put("year",copyright.getYear());
		paramMap.put("departId",copyright.getDepartId());
		paramMap.put("registerNo",copyright.getRegisterNo());
		paramMap.put("institutionId",copyright.getInstitutionId());
		paramMap.put("companyOrder",copyright.getCompanyOrder());
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getNamedParameterJdbcTemplate().update(sb.toString(),new MapSqlParameterSource(copyrightMap(copyright)), keyHolder);
		return keyHolder.getKey().intValue();
		
	}
	public Map<String, Object> copyrightMap(InstitutionCopyright copyright){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", copyright.getId());
		params.put("name", copyright.getName());
		params.put("type",copyright.getType());
		params.put("grade",copyright.getGrade());
		params.put("companyOrder",copyright.getCompanyOrder());
		params.put("institutionId",copyright.getInstitutionId());
		params.put("year",copyright.getYear());
		params.put("departId",copyright.getDepartId());
		params.put("registerNo",copyright.getRegisterNo());
		return params;
	}

	@Override
	public InstitutionCopyright getCopyrightById(int id) {
		String sql="select * from institution_copyright where id=:id ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("id",id);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap,rowMapper));
	}

	@Override
	public Map<Integer, Integer> getGradesMap(int institutionId) {
		String sql="select grade,count(*) c from institution_copyright "+
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
		String sql="select r.grade,count(*) c from institution_copyright r,institution_copyright_user u "+
				"where u.user_id=:userId "+
				"and u.copyright_id=r.id "+
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
	public void deleteCopyrightUser(final int[] id) {
		String sql="delete from institution_copyright_user where copyright_id=?";
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
	public void insertCopyrightUser(int copyrightId, int userId) {
		String sql="insert into institution_copyright_user(`user_id`,`copyright_id`)"+
				" values(:userId,:copyrightId)";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId",userId);
		paramMap.put("copyrightId",copyrightId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}

	@Override
	public List<InstitutionCopyright> getCopyrightsByUID(int uid) {
		String sql="select * from institution_copyright_user u "+
				"left join institution_copyright c "+
				"on u.copyright_id=c.id "+
				"where u.user_id=:uid ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("uid", uid);
		return getNamedParameterJdbcTemplate().query(sql, paramMap,rowMapperUID);
	}

private static final RowMapper<InstitutionCopyright> rowMapperUID=new RowMapper<InstitutionCopyright>(){
		
		@Override
		public InstitutionCopyright mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionCopyright copyright=new InstitutionCopyright();
			copyright.setId(rs.getInt("copyright_id"));
			copyright.setName(rs.getString("name"));
			copyright.setType(rs.getInt("type"));
			copyright.setGrade(rs.getInt("grade"));
			copyright.setYear(rs.getInt("year"));
			copyright.setInstitutionId(rs.getInt("institution_id"));
			copyright.setCompanyOrder(rs.getInt("company_order"));
			copyright.setPersonalOrder(rs.getInt("author_order"));
			return copyright;
		}};
		
		
		
		@Override
		public void createUserRef(int userId, int copyrightId,int authorOrder) {
			String sql="insert into institution_copyright_user(`user_id`,`copyright_id`,`author_order`)"+
					" values(:userId,:copyrightId,:authorOrder)";
			Map<String,Object> paramMap=new HashMap<String,Object>();
			paramMap.put("userId",userId);
			paramMap.put("copyrightId",copyrightId);
			paramMap.put("authorOrder",authorOrder);
			getNamedParameterJdbcTemplate().update(sql, paramMap);
		}

		@Override
		public void deleteUserRef(int uid,int copyrightId) {
			String sql="delete from institution_copyright_user where user_id=:uid and copyright_id=:copyrightId";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("uid", uid);
			params.put("copyrightId", copyrightId);
			getNamedParameterJdbcTemplate().update(sql, params);
		}

		@Override
		public void updateUserRef(int uid, int copyrightId, int authorOrder) {
			StringBuffer sb=new StringBuffer();
			sb.append("update institution_copyright_user set ");
			sb.append("`author_order`=:authorOrder ");
			sb.append("where user_id=:uid and copyright_id=:copyrightId");

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("uid", uid);
			params.put("copyrightId", copyrightId);
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
				case SearchInstitutionCondition.ORDER_YEAR:{
					return "year";
				}
				default:{
					return "id";
				}
			}
		}
		@Override
		public int getCopyrightCount(int insId, int departId, int year) {
			String sql="select count(*) from institution_copyright where institution_id=:insId ";
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
		public List<InstitutionCopyright> getCopyrightsByAuthorId(int authorId,
				int startYear, int endYear) {
			StringBuffer sb = new StringBuffer();
			sb.append("select * from institution_copyright p,institution_copyright_author_ref r where p.id=r.copyright_id ");
			sb.append("and p.year>=:startYear and p.year<=:endYear ");
			sb.append("and r.author_id=:authorId ");
			
			Map<String, Object> paramMap=new HashMap<String,Object>();
			paramMap.put("authorId", authorId);
			paramMap.put("startYear", startYear);
			paramMap.put("endYear", endYear);
			return getNamedParameterJdbcTemplate().query(sb.toString(), paramMap, rowMapper);
		}
}
