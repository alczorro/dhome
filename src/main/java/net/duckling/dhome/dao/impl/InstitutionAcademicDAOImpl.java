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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.dao.IInstitutionAcademicDAO;
import net.duckling.dhome.domain.institution.InstitutionAcademic;
import net.duckling.dhome.domain.institution.InstitutionAward;
import net.duckling.dhome.domain.institution.InstitutionPeriodical;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class InstitutionAcademicDAOImpl extends BaseDao implements IInstitutionAcademicDAO {

	@Override
	public void insert(List<InstitutionAcademic> academics) {
		if(CommonUtils.isNull(academics)){
			return;
		}
		StringBuffer sb=new StringBuffer();
		sb.append("insert into institution_academic_assignment(");
		sb.append("`institution_id`,");
		sb.append("`organization_name`,");
		sb.append("`position`,");
		sb.append("`start_year`,");
		sb.append("`start_month`,");
		sb.append("`end_year`,");
		sb.append("`end_month`)");
		sb.append(" values");
		int index=0;
		Map<String,Object> params=new HashMap<String,Object>();
		for(InstitutionAcademic acm:academics){
			sb.append("(");
			sb.append(":insId"+index).append(",");
			sb.append(":name"+index).append(",");
			sb.append(":position"+index).append(",");
			sb.append(":startYear"+index).append(",");
			sb.append(":startMonth"+index).append(",");
			sb.append(":endYear"+index).append(",");
			sb.append(":endMonth"+index);
			sb.append("),");
			params.put("insId"+index, acm.getInstitutionId());
			params.put("name"+index,acm.getName());
			params.put("position"+index,acm.getPosition());
			params.put("startYear"+index,acm.getStartYear());
			params.put("startMonth"+index, acm.getStartMonth());
			params.put("endYear"+index,acm.getEndYear());
			params.put("endMonth"+index,acm.getEndMonth());
			index++;
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		getNamedParameterJdbcTemplate().update(sb.toString(),params);
	}

	@Override
	public void insert(InstitutionAcademic academic) {
		List<InstitutionAcademic> list = new ArrayList<InstitutionAcademic>();
		list.add(academic);
		insert(list);
	}

	@Override
	public List<InstitutionAcademic> getAcademics(int insId, int offset,
			int pageSize,SearchInstitutionCondition condition) {
		//String sql="select * from institution_academic_assignment where institution_id=:insId limit :offset,:size";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("insId", insId);
		param.put("offset",offset);
		param.put("size", pageSize);
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
		param.put("position", condition.getPosition());
		return getNamedParameterJdbcTemplate().query(getListSql(false, condition), param, rowMapper);	
	}
	

	@Override
	public int getAcademicCount(int insId,SearchInstitutionCondition condition) {
		//String sql="select count(*) from institution_academic_assignment where institution_id=:insId";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("insId", insId);
		param.put("position", condition.getPosition());
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");

		return getNamedParameterJdbcTemplate().queryForInt(getListSql(true, condition),param);
	}
	
	private String getListSql(boolean isCount,SearchInstitutionCondition condition){
		StringBuffer sb=new StringBuffer();
		sb.append(" select ").append(isCount?"count(*)":"*").append(" from institution_academic_assignment ");
		sb.append(" where institution_id=:insId");
		//关键字
		if(!CommonUtils.isNull(condition.getKeyword())){
			sb.append(" and organization_name like :keyword ");
		}
		//发布时间
		if(condition.getPosition() != -1){
			sb.append(" and position=:position ");
		}
		if(!isCount){
			//排序
			sb.append(" order by "+ getOrderColumnByStatus(condition.getOrder())+getOrderTypeByType(condition.getOrderType()));
			sb.append(" limit :offset,:size");
		}
		return sb.toString();
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

	/**
	 * 删除全部
	 */
	@Override
	public void deleteAll(int insId) {
		String sql="delete from institution_academic_assignment where institution_id=:insId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(int insId, int[] acmIds) {
		if(CommonUtils.isNull(acmIds)){
			return;
		}
		String sql="delete from institution_academic_assignment where institution_id=:insId and id in(";
		int index=0;
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		for(int id :acmIds){
			sql+=":id"+index+",";
			paramMap.put("id"+index, id);
			index++;
		}
		sql=sql.substring(0,sql.length()-1);
		sql+=")";
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}
	
	/**
	 * 单个删除
	 */
	@Override
	public void delete(int insId, int acmId) {
		String sql="delete from institution_academic_assignment where institution_id=:insId and id =:id";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		paramMap.put("id", acmId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}

	
	public static final RowMapper<InstitutionAcademic> rowMapper = new RowMapper<InstitutionAcademic>() {
		
		@Override
		public InstitutionAcademic mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionAcademic acm = new InstitutionAcademic();
			acm.setId(rs.getInt("id"));
			acm.setInstitutionId(rs.getInt("institution_id"));
			acm.setName(rs.getInt("organization_name"));
			acm.setPosition(rs.getInt("position"));
			acm.setStartYear(rs.getInt("start_year"));
			acm.setStartMonth(rs.getInt("start_month"));
			acm.setEndYear(rs.getInt("end_year"));
			acm.setEndMonth(rs.getInt("end_month"));
			acm.setDepartId(rs.getInt("depart_id"));
			return acm;
		}
		
	};


	@Override
	public int create(InstitutionAcademic acm) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("insert into institution_academic_assignment(");
		sb.append("`institution_id`,");
		sb.append("`organization_name`,");
		sb.append("`position`,");
		sb.append("`depart_id`,");
		sb.append("`start_year`,");
		sb.append("`start_month`,");
		sb.append("`end_year`,");
		sb.append("`end_month`)");
		sb.append(" values");
		sb.append("(");
		sb.append(":insId").append(",");
		sb.append(":name").append(",");
		sb.append(":position").append(",");
		sb.append(":departId").append(",");
		sb.append(":startYear").append(",");
		sb.append(":startMonth").append(",");
		sb.append(":endYear").append(",");
		sb.append(":endMonth");
		sb.append(")");

		KeyHolder keyHolder = new GeneratedKeyHolder();
		getNamedParameterJdbcTemplate().update(sb.toString(),new MapSqlParameterSource(acedemic2Map(acm)), keyHolder);
		return keyHolder.getKey().intValue();
	}
	

	@Override
	public void update(InstitutionAcademic acm) {
		StringBuffer sb=new StringBuffer();
		sb.append("update institution_academic_assignment set ");
		sb.append("`organization_name`=:name,");
		sb.append("`position`=:position,");
		sb.append("`depart_id`=:departId,");
		sb.append("`start_year`=:startYear,");
		sb.append("`start_month`=:startMonth,");
		sb.append("`end_year`=:endYear,");
		sb.append("`end_month`=:endMonth ");
		sb.append("where id=:id");
		getNamedParameterJdbcTemplate().update(sb.toString(),acedemic2Map(acm));
	}
	
	public Map<String, Object> acedemic2Map(InstitutionAcademic acm){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", acm.getId());
		params.put("insId", acm.getInstitutionId());
		params.put("name",acm.getName());
		params.put("position",acm.getPosition());
		params.put("departId",acm.getDepartId());
		params.put("departId",acm.getDepartId());
		params.put("startYear",acm.getStartYear());
		params.put("startMonth", acm.getStartMonth());
		params.put("endYear",acm.getEndYear());
		params.put("endMonth",acm.getEndMonth());
		return params;
	}

	@Override
	public InstitutionAcademic getAcademic(int acmId) {
		String sql="select * from institution_academic_assignment where id=:id";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("id", acmId);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, param, rowMapper));	
	}

	
	@Override
	public List<InstitutionAcademic> getAcademicsByUID(int uid) {
		String sql="select * from institution_academic_assignment_user u "+
				"left join institution_academic_assignment a "+
				"on u.academic_assignment_id=a.id "+
				"where u.user_id=:uid ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("uid", uid);
		return getNamedParameterJdbcTemplate().query(sql, paramMap,rowMapperUID);
	}
	
	
  public static final RowMapper<InstitutionAcademic> rowMapperUID = new RowMapper<InstitutionAcademic>() {
		
		@Override
		public InstitutionAcademic mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionAcademic acm = new InstitutionAcademic();
			acm.setId(rs.getInt("academic_assignment_id"));
			acm.setInstitutionId(rs.getInt("institution_id"));
			acm.setName(rs.getInt("organization_name"));
			acm.setPosition(rs.getInt("position"));
			acm.setStartYear(rs.getInt("start_year"));
			acm.setStartMonth(rs.getInt("start_month"));
			acm.setEndYear(rs.getInt("end_year"));
			acm.setEndMonth(rs.getInt("end_month"));
			acm.setDepartId(rs.getInt("depart_id"));
			return acm;
		}
		
	};

	@Override
	public List<InstitutionAcademic> getAcademicByUser(int userId, int offset,
			int pageSize, SearchInstitutionCondition condition) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("userId", userId);
		param.put("offset",offset);
		param.put("size", pageSize);
//		param.put("year", condition.getYear());
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
		return getNamedParameterJdbcTemplate().query(getListSqlByUser(false, condition), param, rowMapper);
	}

	@Override
	public int getAcademicCountByUser(int userId,
			SearchInstitutionCondition condition) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("userId", userId);
//		param.put("year", condition.getYear());
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
		return getNamedParameterJdbcTemplate().queryForInt(getListSqlByUser(true, condition),param);
	}
	private String getListSqlByUser(boolean isCount,SearchInstitutionCondition condition){
		StringBuffer sb=new StringBuffer();
		sb.append(" select ").append(isCount?"count(*)":"r.*").append(" from institution_academic_assignment r,institution_academic_assignment_user u");
		sb.append(" where r.id=u.academic_assignment_id");
		sb.append(" and u.user_id=:userId");
		//关键字
		if(!CommonUtils.isNull(condition.getKeyword())){
			sb.append(" and name like :keyword ");
		}
//		//年份
//		if(condition.getYear()!=0){
//			sb.append(" and year=:year ");
//		}
		if(!isCount){
			//排序
			sb.append(" order by "+ getOrderColumnByStatus(condition.getOrder())+getOrderTypeByType(condition.getOrderType()));
			sb.append(" limit :offset,:size");
		}
		return sb.toString();
	}


	@Override
	public void deleteAcademicUser(final int[] id) {
		String sql="delete from institution_academic_assignment_user where academic_assignment_id=?";
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
	public void insertAcademicUser(int academicId, int userId) {
		String sql="insert into institution_academic_assignment_user(`user_id`,`academic_assignment_id`)"+
				" values(:userId,:academicId)";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId",userId);
		paramMap.put("academicId",academicId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}
	
	
	@Override
	public void createUserRef(int userId, int academicId) {
		String sql="insert into institution_academic_assignment_user(`user_id`,`academic_assignment_id`)"+
				" values(:userId,:academicId)";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId",userId);
		paramMap.put("academicId",academicId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}

	@Override
	public void deleteUserRef(int uid,int acmId) {
		String sql="delete from institution_academic_assignment_user where user_id=:uid and academic_assignment_id=:acmId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("acmId", acmId);
		getNamedParameterJdbcTemplate().update(sql, params);
	}

	@Override
	public Map<Integer, Integer> getPositionsMap(int insId) {
		String sql="select position,count(*) c from institution_academic_assignment "+
				"where institution_id=:insId "+
				"group by position "+
				"order by position ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId",insId);
		final Map<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
		getNamedParameterJdbcTemplate().query(sql, paramMap, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				result.put(rs.getInt("position"), rs.getInt("c"));
				return null;
			}
		});
		return result;
	}
	@Override
	public int getAcademicCount(int insId, int departId, int year) {
		String sql="select count(*) from institution_academic_assignment where institution_id=:insId ";
		StringBuilder sb=new StringBuilder();
		sb.append(sql);
		if(departId!=-1){
			sb.append(" and depart_id=:departId");
		}
		if(year!=-1){
			sb.append(" and (start_year<=:year and end_year>=:year)");
		}
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		paramMap.put("departId", departId);
		paramMap.put("year", year);
		return getNamedParameterJdbcTemplate().queryForInt(sb.toString(),paramMap);
	}

	@Override
	public List<InstitutionAcademic> getAcademicsByAuthorId(int userId,
			int startYear, int endYear) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from institution_academic_assignment p,institution_academic_author_ref r,institution_member_from_vmt m where p.id=r.academic_assignment_id and m.umt_id=r.author_id ");
		sb.append("and p.start_year>=:startYear and p.end_year<=:endYear ");
		sb.append("and m.uid=:userId ");
		
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId", userId);
		paramMap.put("startYear", startYear);
		paramMap.put("endYear", endYear);
		return getNamedParameterJdbcTemplate().query(sb.toString(), paramMap, rowMapper);
	}

}
