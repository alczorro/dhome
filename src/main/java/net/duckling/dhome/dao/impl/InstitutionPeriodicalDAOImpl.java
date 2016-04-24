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
import net.duckling.dhome.dao.IInstitutionPeriodicalDAO;
import net.duckling.dhome.domain.institution.InstitutionPeriodical;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class InstitutionPeriodicalDAOImpl extends BaseDao implements
		IInstitutionPeriodicalDAO {

	@Override
	public int create(InstitutionPeriodical per) {
		StringBuffer sb = new StringBuffer();
		sb.append("insert into institution_periodical_assignment(");
		sb.append("`institution_id`,");
		sb.append("`periodical_name`,");
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
		getNamedParameterJdbcTemplate().update(sb.toString(),new MapSqlParameterSource(per2Map(per)), keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	public Map<String, Object> per2Map(InstitutionPeriodical per){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("insId", per.getInstitutionId());
		params.put("name", per.getName());
		params.put("position", per.getPosition());
		params.put("departId", per.getDepartId());
		params.put("startYear", per.getStartYear());
		params.put("startMonth", per.getStartMonth());
		params.put("endYear", per.getEndYear());
		params.put("endMonth", per.getEndMonth());
		params.put("id", per.getId());
		return params;
	}

	@Override
	public void insert(List<InstitutionPeriodical> periodicals) {
		if (CommonUtils.isNull(periodicals)) {
			return;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("insert into institution_periodical_assignment(");
		sb.append("`institution_id`,");
		sb.append("`periodical_name`,");
		sb.append("`position`,");
		sb.append("`start_year`,");
		sb.append("`start_month`,");
		sb.append("`end_year`,");
		sb.append("`end_month`)");
		sb.append(" values");
		int index = 0;
		Map<String, Object> params = new HashMap<String, Object>();
		for (InstitutionPeriodical per : periodicals) {
			sb.append("(");
			sb.append(":insId" + index).append(",");
			sb.append(":name" + index).append(",");
			sb.append(":position" + index).append(",");
			sb.append(":startYear" + index).append(",");
			sb.append(":startMonth" + index).append(",");
			sb.append(":endYear" + index).append(",");
			sb.append(":endMonth" + index);
			sb.append("),");

			params.put("insId" + index, per.getInstitutionId());
			params.put("name" + index, per.getName());
			params.put("position" + index, per.getPosition());
			params.put("startYear" + index, per.getStartYear());
			params.put("startMonth" + index, per.getStartMonth());
			params.put("endYear" + index, per.getEndYear());
			params.put("endMonth" + index, per.getEndMonth());
			index++;
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		getNamedParameterJdbcTemplate().update(sb.toString(), params);
	}

	@Override
	public void insert(InstitutionPeriodical periodical) {
		List<InstitutionPeriodical> list = new ArrayList<InstitutionPeriodical>();
		list.add(periodical);
		insert(list);
	}

	@Override
	public List<InstitutionPeriodical> getPeriodicals(int insId, int offset,
			int pageSize,SearchInstitutionCondition condition) {
		//String sql = "select * from institution_periodical_assignment where institution_id=:insId limit :offset,:size";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("insId", insId);
		param.put("offset", offset);
		param.put("size", pageSize);
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
		param.put("position", condition.getPosition());
		return getNamedParameterJdbcTemplate().query(getListSql(false, condition), param, rowMapper);
	}

	@Override
	public int getPeriodicalCount(int insId,SearchInstitutionCondition condition) {
		//String sql = "select count(*) from institution_periodical_assignment where institution_id=:insId";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("insId", insId);
		param.put("position", condition.getPosition());
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
		return getNamedParameterJdbcTemplate().queryForInt(getListSql(true, condition), param);
	}
	
	private String getListSql(boolean isCount,SearchInstitutionCondition condition){
		StringBuffer sb=new StringBuffer();
		sb.append(" select ").append(isCount?"count(*)":"*").append(" from institution_periodical_assignment ");
		sb.append(" where institution_id=:insId");
		//关键字
		if(!CommonUtils.isNull(condition.getKeyword())){
			sb.append(" and periodical_name like :keyword ");
		}
		//发布时间
		if(condition.getPosition()!= -1){
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
		String sql="delete from institution_periodical_assignment where institution_id=:insId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(int insId, int[] perIds) {
		if(CommonUtils.isNull(perIds)){
			return;
		}
		String sql="delete from institution_periodical_assignment where institution_id=:insId and id in(";
		int index=0;
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		for(int id :perIds){
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
	public void delete(int insId, int perId) {
		String sql="delete from institution_periodical_assignment where institution_id=:insId and id =:id";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		paramMap.put("id", perId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}

	public static final RowMapper<InstitutionPeriodical> rowMapper = new RowMapper<InstitutionPeriodical>() {

		@Override
		public InstitutionPeriodical mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionPeriodical per = new InstitutionPeriodical();
			per.setId(rs.getInt("id"));
			per.setInstitutionId(rs.getInt("institution_id"));
			per.setName(rs.getInt("periodical_name"));
			per.setPosition(rs.getInt("position"));
			per.setStartYear(rs.getInt("start_year"));
			per.setStartMonth(rs.getInt("start_month"));
			per.setEndYear(rs.getInt("end_year"));
			per.setEndMonth(rs.getInt("end_month"));
			per.setDepartId(rs.getInt("depart_id"));
			return per;
		}

	};

	@Override
	public void update(InstitutionPeriodical per) {
		StringBuffer sb=new StringBuffer();
		sb.append("update institution_periodical_assignment set ");
		sb.append("`periodical_name`=:name,");
		sb.append("`position`=:position,");
		sb.append("`depart_id`=:departId,");
		sb.append("`start_year`=:startYear,");
		sb.append("`start_month`=:startMonth,");
		sb.append("`end_year`=:endYear,");
		sb.append("`end_month`=:endMonth ");
		sb.append("where id=:id");
		getNamedParameterJdbcTemplate().update(sb.toString(),per2Map(per));
	}

	@Override
	public InstitutionPeriodical getPeriodical(int perId) {
		String sql = "select * from institution_periodical_assignment where id=:id";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", perId);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, param, rowMapper));
	}

	@Override
	public List<InstitutionPeriodical> getPeriodicalsByUID(int uid) {
		String sql="select * from institution_periodical_assignment_user u "+
				"left join institution_periodical_assignment a "+
				"on u.periodical_assignment_id=a.id "+
				"where u.user_id=:uid ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("uid", uid);
		return getNamedParameterJdbcTemplate().query(sql, paramMap,rowMapperUID);
	}
	

	public static final RowMapper<InstitutionPeriodical> rowMapperUID = new RowMapper<InstitutionPeriodical>() {

		@Override
		public InstitutionPeriodical mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionPeriodical per = new InstitutionPeriodical();
			per.setId(rs.getInt("periodical_assignment_id"));
			per.setInstitutionId(rs.getInt("institution_id"));
			per.setName(rs.getInt("periodical_name"));
			per.setPosition(rs.getInt("position"));
			per.setStartYear(rs.getInt("start_year"));
			per.setStartMonth(rs.getInt("start_month"));
			per.setEndYear(rs.getInt("end_year"));
			per.setEndMonth(rs.getInt("end_month"));
			return per;
		}

	};

	@Override
	public List<InstitutionPeriodical> getPeriodicalByUser(int userId, int offset,
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
	public int getPeriodicalCountByUser(int userId,
			SearchInstitutionCondition condition) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("userId", userId);
//		param.put("year", condition.getYear());
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
		return getNamedParameterJdbcTemplate().queryForInt(getListSqlByUser(true, condition),param);
	}
	private String getListSqlByUser(boolean isCount,SearchInstitutionCondition condition){
		StringBuffer sb=new StringBuffer();
		sb.append(" select ").append(isCount?"count(*)":"r.*").append(" from institution_periodical_assignment r,institution_periodical_assignment_user u");
		sb.append(" where r.id=u.periodical_assignment_id");
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
	public void deletePeriodicalUser(final int[] id) {
		String sql="delete from institution_periodical_assignment_user where periodical_assignment_id=?";
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
	public void insertPeriodicalUser(int periodicalId, int userId) {
		String sql="insert into institution_periodical_assignment_user(`user_id`,`periodical_assignment_id`)"+
				" values(:userId,:periodicalId)";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId",userId);
		paramMap.put("periodicalId",periodicalId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}
	
	
	@Override
	public void createUserRef(int userId, int periodicalId) {
		String sql="insert into institution_periodical_assignment_user(`user_id`,`periodical_assignment_id`)"+
				" values(:userId,:periodicalId)";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId",userId);
		paramMap.put("periodicalId",periodicalId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}

	@Override
	public void deleteUserRef(int uid,int perId) {
		String sql="delete from institution_periodical_assignment_user where user_id=:uid and periodical_assignment_id=:perId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("perId", perId);
		getNamedParameterJdbcTemplate().update(sql, params);
	}

	@Override
	public Map<Integer, Integer> getPositionsMap(int insId) {
		String sql="select position,count(*) c from institution_periodical_assignment "+
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
	public int getPeriodicalCount(int insId, int departId, int year) {
		String sql="select count(*) from institution_periodical_assignment where institution_id=:insId ";
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
}
