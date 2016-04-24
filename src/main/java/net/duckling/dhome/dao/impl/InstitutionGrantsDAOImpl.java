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

import net.duckling.common.util.CommonUtils;
import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.dao.IInstitutionGrantsDAO;
import net.duckling.dhome.domain.institution.InstitutionGrants;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class InstitutionGrantsDAOImpl extends BaseDao implements IInstitutionGrantsDAO{
	private RowMapper<InstitutionGrants> rowMapper= new RowMapper<InstitutionGrants>() {
			@Override
			public InstitutionGrants mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				InstitutionGrants item=new InstitutionGrants();
				item.setId(rs.getInt("id"));
				item.setTopicNo(rs.getString("topic_no"));
				item.setStudentId(rs.getInt("student_id"));
				item.setStudentName(rs.getString("student_name"));
				item.setClassName(rs.getString("class_name"));
				item.setDegree(rs.getInt("degree"));
				item.setTutor(rs.getString("tutor"));
				item.setScholarship1(rs.getBigDecimal("scholarship1"));
				item.setScholarship2(rs.getBigDecimal("scholarship2"));
				item.setAssistantFee(rs.getBigDecimal("assistant_fee"));
				item.setSumFee(rs.getBigDecimal("sum_fee"));
				item.setStartTime(rs.getDate("start_time"));
				item.setEndTime(rs.getDate("end_time"));
				item.setInstitutionId(rs.getInt("institution_id"));
				item.setUserId(rs.getInt("user_id"));
				item.setStatus(rs.getInt("status"));
				item.setBatchNo(rs.getString("batch_no"));
				return item;
			}
		};
	
	@Override
	public int create(InstitutionGrants grants) {
		StringBuffer sb=new StringBuffer();
		sb.append("insert into institution_grants(");
		sb.append("`topic_no`,");
		sb.append("`student_id`,");
		sb.append("`student_name`,");
		sb.append("`class_name`,");
		sb.append("`degree`,");
		sb.append("`tutor`,");
		sb.append("`scholarship1`,");
		sb.append("`scholarship2`,");
		sb.append("`assistant_fee`,");
		sb.append("`sum_fee`,");
		sb.append("`start_time`,");
		sb.append("`end_time`,");
		sb.append("`institution_id`,");
		sb.append("`user_id`) ");
		sb.append("values(");
		sb.append(":topic_no,");
		sb.append(":student_id,");
		sb.append(":student_name,");
		sb.append(":class_name,");
		sb.append(":degree,");
		sb.append(":tutor,");
		sb.append(":scholarship1,");
		sb.append(":scholarship2,");
		sb.append(":assistant_fee,");
		sb.append(":sum_fee,");
		sb.append(":start_time,");
		sb.append(":end_time,");
		sb.append(":institution_id,");
		sb.append(":user_id) ");
		KeyHolder keyHolder = new GeneratedKeyHolder();
        getNamedParameterJdbcTemplate().update(sb.toString(), new MapSqlParameterSource(buildParamMap(grants)), keyHolder);
        return keyHolder.getKey().intValue();
	}
		
	@Override
	public void update(InstitutionGrants grants) {
		StringBuffer sb=new StringBuffer();
		sb.append("update institution_grants set ");
		sb.append("`topic_no`=:topic_no,");
		sb.append("`student_name`=:student_name,");
		sb.append("`class_name`=:class_name,");
		sb.append("`degree`=:degree,");
		sb.append("`tutor`=:tutor,");
		sb.append("`scholarship1`=:scholarship1,");
		sb.append("`scholarship2`=:scholarship2,");
		sb.append("`assistant_fee`=:assistant_fee,");
		sb.append("`sum_fee`=:sum_fee,");
		sb.append("`start_time`=:start_time,");
		sb.append("`end_time`=:end_time,");
		sb.append("`institution_id`=:institution_id,");
		sb.append("`user_id`=:user_id ");
		sb.append("where id=:id");
		getNamedParameterJdbcTemplate().update(sb.toString(),buildParamMap(grants));
	}
	
	@Override
	public void updateStatus(final int uid, final int[] ids) {
		String sql="update institution_grants set status = 1,batch_no=? where id=?";
		final String batchNo = InstitutionGrants.generateBatchNo();
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return ids.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setString(++i, batchNo);
						pst.setInt(++i, ids[index]);
					}
				});
	}
	
	@Override
	public List<InstitutionGrants> getList(Integer userId, Integer degree, Integer status, Integer studentId, int offset, int size) {
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId", userId);
		paramMap.put("degree", degree);
		paramMap.put("status", status);
		paramMap.put("studentId", studentId);
		paramMap.put("offset", offset);
		paramMap.put("size", size);
		return getNamedParameterJdbcTemplate().query(getListSql(false,userId, degree, status, studentId), paramMap, rowMapper);
	}
	
	@Override
	public int getListCount(Integer userId, Integer degree, Integer status, Integer studentId) {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId", userId);
		paramMap.put("degree", degree);
		paramMap.put("status", status);
		paramMap.put("studentId", studentId);
		return getNamedParameterJdbcTemplate().queryForInt(getListSql(true, userId, degree, status, studentId),paramMap);
	}
	
	@Override
	public List<InstitutionGrants> getListLasted(Integer userId, Integer degree, Integer status, int offset, int size) {
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId", userId);
		paramMap.put("degree", degree);
		paramMap.put("status", status);
		paramMap.put("offset", offset);
		paramMap.put("size", size);
		return getNamedParameterJdbcTemplate().query(getListSqlLasted(false,userId, degree, status), paramMap, rowMapper);
	}
	
	@Override
	public List<InstitutionGrants> getListByBatchNo(Integer userId, String batchNo) {
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId", userId);
		paramMap.put("batchNo", batchNo);
		String sql = "select t1.* from institution_grants t1 where" + 
					" t1.user_id =:userId and t1.batch_no =:batchNo order by t1.id desc ";
		return getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper);
	}
	
	@Override
	public List<InstitutionGrants> getListByBatchNo(Integer userId, String batchNo, int offset, int size) {
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId", userId);
		paramMap.put("batchNo", batchNo);
		paramMap.put("offset", offset);
		paramMap.put("size", size);
		String sql = "select t1.* from institution_grants t1 where" + 
				" t1.user_id =:userId and t1.batch_no =:batchNo order by t1.id desc limit :offset,:size ";
		return getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper);
	}
	
	@Override
	public int getListCountLasted(Integer userId, Integer degree, Integer status) {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId", userId);
		paramMap.put("degree", degree);
		paramMap.put("status", status);
		return getNamedParameterJdbcTemplate().queryForInt(getListSqlLasted(true, userId, degree, status),paramMap);
	}
	
	@Override
	public void delete(final int userId, final int[] id) {
		String sql="delete from institution_grants where user_id=? and id=?";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return id.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i, userId);
						pst.setInt(++i, id[index]);
					}
				});
	}
	
	
	
	@Override
	public InstitutionGrants getById(int id) {
		String sql="select * from institution_grants t1,simple_user t2 where t1.user_id=t2.id and t1.id=:id";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("id", id);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper));
	}
	
	@Override
	public Map<Integer, Integer> getDegreesCount(Integer insId, Integer userId) {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		String sql="select degree,count(*) c from institution_grants where 1=1";
		if(insId!=null){
			sql += " and institution_id=:insId";
			paramMap.put("insId",insId);
		}
		if(userId!=null){
			sql += " and user_id=:userId";
			paramMap.put("userId",userId);
		}
		sql+=" group by degree order by degree ";
		final Map<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
		getNamedParameterJdbcTemplate().query(sql, paramMap, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				result.put(rs.getInt("degree"), rs.getInt("c"));
				return null;
			}
		});
		return result;
	}
	
	@Override
	public Map<String, Integer> getStatusCount(Integer userId) {
		String sql="select status,count(*) c from institution_grants "+
				"where user_id=:userId "+
				"group by status "+
				"order by status ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId",userId);
		final Map<String,Integer> result=new LinkedHashMap<String,Integer>();
		getNamedParameterJdbcTemplate().query(sql, paramMap, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				result.put(rs.getString("status"), rs.getInt("c"));
				return null;
			}
		});
		return result;
	}
	
	private String getListSql(boolean isCount, Integer userId, Integer degree, Integer status, Integer studentId){
		StringBuilder sb=new StringBuilder();
		sb.append(" select ").append(isCount?"count(*)":"t1.*").append(" from institution_grants t1 ");
		sb.append(" where 1=1");
		if(userId != null){
			sb.append(" and t1.user_id =:userId ");
		}
		if(degree != null){
			sb.append(" and t1.degree =:degree ");
		}
		if(status != null){
			sb.append(" and t1.status =:status ");
		}
		if(studentId != null){
			sb.append(" and t1.student_id =:studentId ");
		}
		if(!isCount){
			//排序
			sb.append(" order by t1.id desc ");
			sb.append(" limit :offset,:size");
		}
		return sb.toString();
	}
	
	private String getListSqlLasted(boolean isCount, Integer userId, Integer degree, Integer status){
		StringBuilder sb=new StringBuilder();
		sb.append(" select t1.*").append(" from (select * from institution_grants ORDER BY id desc) as t1 ");
		sb.append(" where 1=1 ");
		if(userId != null){
			sb.append(" and t1.user_id =:userId ");
		}
		if(degree != null){
			sb.append(" and t1.degree =:degree ");
		}
		if(status != null){
			sb.append(" and t1.status =:status ");
		}
		sb.append(" group by t1.student_id");
		if(isCount){
			return "select count(*) from (" + sb.toString() + ") as cc ";
		}else{
			//排序
			sb.append(" order by t1.id desc ");
			sb.append(" limit :offset,:size");
		}
		return sb.toString();
	}
	
	private Map<String,Object> buildParamMap(InstitutionGrants grants){
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("id", grants.getId());
		paramMap.put("topic_no", grants.getTopicNo());
		paramMap.put("student_id", grants.getStudentId());
		paramMap.put("student_name", grants.getStudentName());
		paramMap.put("class_name", grants.getClassName());
		paramMap.put("degree", grants.getDegree());
		paramMap.put("tutor", grants.getTutor());
		paramMap.put("scholarship1", grants.getScholarship1());
		paramMap.put("scholarship2", grants.getScholarship2());
		paramMap.put("assistant_fee", grants.getAssistantFee());
		paramMap.put("sum_fee", grants.getSumFee());
		paramMap.put("start_time", grants.getStartTime());
		paramMap.put("end_time", grants.getEndTime());
		paramMap.put("institution_id", grants.getInstitutionId());
		paramMap.put("user_id", grants.getUserId());
		
		return paramMap;
	}

	@Override
	public List<String> getBatchList(Integer userId, Integer degree, int offset,
			int size) {
		final List<String> batchs = new ArrayList<String>();
		
		StringBuilder sb=new StringBuilder();
		sb.append("select DISTINCT batch_no from institution_grants ");
		sb.append(" where status=1 and user_id =:userId ");
		if(degree != null){
			sb.append(" and degree =:degree ");
		}
			//排序
		sb.append(" order by batch_no desc ");
		sb.append(" limit :offset,:size");
		
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId", userId);
		paramMap.put("degree", degree);
		paramMap.put("offset", offset);
		paramMap.put("size", size);
		
		getNamedParameterJdbcTemplate().query(sb.toString(), paramMap , new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				batchs.add(rs.getString("batch_no"));
				return null;
			}
		});
		return batchs;
	}

	@Override
	public List<InstitutionGrants> getStudentByUser(int userId) {
		String sql="select * from institution_grants where status=1 and user_id =:userId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId", userId);
		return getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper);
	}
	@Override
	public int getBatchCount(Integer userId, Integer degree) {
		StringBuilder sb=new StringBuilder();
		sb.append("select count(DISTINCT batch_no) from institution_grants ");
		sb.append(" where status=1 and  user_id =:userId ");
		if(degree != null){
			sb.append(" and degree =:degree ");
		}
		
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId", userId);
		paramMap.put("degree", degree);
		return getNamedParameterJdbcTemplate().queryForInt(sb.toString(), paramMap);
	}

	@Override
	public int getBatchGrantsCount(Integer userId, String batchNo) {
	    String sql = "select count(*) from institution_grants where user_id =:userId and batch_no=:batchNo ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId", userId);
		paramMap.put("batchNo", batchNo);
		return getNamedParameterJdbcTemplate().queryForInt(sql, paramMap);
	}

}
