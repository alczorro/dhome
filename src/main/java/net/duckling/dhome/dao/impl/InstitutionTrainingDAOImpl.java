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
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.dao.IInstitutionTrainingDAO;
import net.duckling.dhome.domain.institution.InstitutionTraining;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class InstitutionTrainingDAOImpl  extends BaseDao implements IInstitutionTrainingDAO {

	
	private static final RowMapper<InstitutionTraining> rowMapper=new RowMapper<InstitutionTraining>(){
		
		@Override
		public InstitutionTraining mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionTraining training=new InstitutionTraining();
			training.setId(rs.getInt("id"));
			training.setDegree(rs.getInt("degree"));
			training.setMajor(rs.getString("major"));
			training.setGraduationDate(rs.getDate("graduation_date"));
			training.setEnrollmentDate(rs.getDate("enrollment_date"));
			training.setInstitutionId(rs.getInt("institution_id"));
			training.setStudentName(rs.getString("student_name"));
			training.setNationality(rs.getString("nationality"));
			training.setRemark(rs.getString("remark"));
			training.setUmtId(rs.getString("umt_id"));
			training.setDepartId(rs.getInt("depart_id"));
			training.setClassName(rs.getString("class_name"));
			return training;
		}};
		
		@Override
		public List<InstitutionTraining> getTraining(int id, int offset, int size, SearchInstitutionCondition condition) {
			
//			String sql="select * from institution_training where institution_id=:id limit :offset,:size";
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("insId", id);
			param.put("offset",offset);
			param.put("size", size);
			param.put("degree", condition.getDegree());
			param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
//			return getNamedParameterJdbcTemplate().query(sql, param, rowMapper);
			return getNamedParameterJdbcTemplate().query(getListSql(false, condition), param, rowMapper);
		}

	@Override
	public int getTrainingCount(int insId, SearchInstitutionCondition condition) {
		
//		String sql="select count(*) from institution_training where institution_id=:insId ";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("insId", insId);
		param.put("degree", condition.getDegree());
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
//		return getNamedParameterJdbcTemplate().queryForInt(sql,param);
		return getNamedParameterJdbcTemplate().queryForInt(getListSql(true, condition),param);
	}

	private String getListSql(boolean isCount,SearchInstitutionCondition condition){
		StringBuffer sb=new StringBuffer();
		sb.append(" select ").append(isCount?"count(*)":"*").append(" from institution_training ");
		sb.append(" where institution_id=:insId");
		//关键字
		if(!CommonUtils.isNull(condition.getKeyword())){
			sb.append(" and student_name like :keyword ");
		}
		//等级
		if(condition.getDegree()!=0){
			sb.append(" and degree=:degree ");
		}
		if(!isCount){
			//排序
			sb.append(" order by "+ getOrderColumnByStatus(condition.getOrder())+getOrderTypeByType(condition.getOrderType()));
			sb.append(" limit :offset,:size");
		}
		return sb.toString();
	}
	
	@Override
	public void deleteTraining(final int[] id) {
		// TODO Auto-generated method stub
		String sql="delete from institution_training where id=?";
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
	public void updateTraining(int id, InstitutionTraining training) {
		// TODO Auto-generated method stub
		String sql="Update institution_training set "
				+"student_name=:student_name,"
				+"nationality=:nationality,"
				+"degree=:degree,"
				+"enrollment_date=:enrollment_date,"
				+"graduation_date=:graduation_date,"
				+"major=:major,"
				+"umt_id=:umtId,"
				+"depart_id=:departId,"
				+"class_name=:className,"
				+"remark=:remark "
				+"where id=:id";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("student_name", training.getStudentName());
		param.put("nationality", training.getNationality());
		param.put("degree", training.getDegree());
		param.put("enrollment_date", training.getEnrollmentDate());
		param.put("graduation_date", training.getGraduationDate());
		param.put("major", training.getMajor());
		param.put("umtId", training.getUmtId());
		param.put("departId", training.getDepartId());
		param.put("className", training.getClassName());
		param.put("remark", training.getRemark());
		param.put("id", id);
		getNamedParameterJdbcTemplate().update(sql, param);
	}

	@Override
	public void insert(List<InstitutionTraining> training) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int insert(InstitutionTraining training) {
		// TODO Auto-generated method stub
//		List<InstitutionTraining> list = new ArrayList<InstitutionTraining>();
//		list.add(training);
//		insert(list);
		StringBuffer sb=new StringBuffer();
		sb.append("insert into institution_training(");
		sb.append("`student_name`,");
		sb.append("`nationality`,");
		sb.append("`degree`,");
		sb.append("`enrollment_date`,");
		sb.append("`major`,");
		sb.append("`umt_id`,");
		sb.append("`depart_id`,");
		sb.append("`class_name`,");
		sb.append("`remark`,");
		sb.append("`graduation_date`,");
		sb.append("`institution_id`) ");
		
		sb.append("values(");
		
		sb.append(":student_name,");
		sb.append(":nationality,");
		sb.append(":degree,");
		sb.append(":enrollment_date,");
		sb.append(":major,");
		sb.append(":umtId,");
		sb.append(":departId,");
		sb.append(":className,");
		sb.append(":remark,");
		sb.append(":graduation_date,");
		sb.append(":institution_id) ");
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("student_name",training.getStudentName());
		paramMap.put("nationality",training.getNationality());
		paramMap.put("degree",training.getDegree());
		paramMap.put("enrollment_date",training.getEnrollmentDate());
		paramMap.put("major",training.getMajor());
		paramMap.put("umtId",training.getUmtId());
		paramMap.put("departId",training.getDepartId());
		paramMap.put("className",training.getClassName());
		paramMap.put("remark",training.getRemark());
		paramMap.put("graduation_date",training.getGraduationDate());
		paramMap.put("institution_id",training.getInstitutionId());
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getNamedParameterJdbcTemplate().update(sb.toString(),new MapSqlParameterSource(paramMap), keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public InstitutionTraining getTrainingById(int id) {
		String sql="select * from institution_training where id=:id ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("id",id);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap,rowMapper));
	}
	
	@Override
	public InstitutionTraining getById(int id) {
		String sql="select * from institution_training where id=:id ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("id",id);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap,rowMapper));
	}
	
	@Override
	public List<InstitutionTraining> search(String keyword) {
		String sql="select * from institution_training where student_name like :keyword";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("keyword", "%"+keyword+"%");
		return getNamedParameterJdbcTemplate().query(sql,param,rowMapper);
	}

	@Override
	public Map<Integer, Integer> getDegreesMap(int institutionId) {
		String sql="select degree,count(*) c from institution_training "+
				"where institution_id=:insId "+
				"group by degree ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", institutionId);
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
	public List<InstitutionTraining> getTrainingsByUID(int uid) {
		String sql="select * from institution_training_user u "+
				"left join institution_training t "+
				"on u.training_id=t.id "+
				"where u.user_id=:uid ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("uid", uid);
		return getNamedParameterJdbcTemplate().query(sql, paramMap,rowMapperUID);
	}

	
    private static final RowMapper<InstitutionTraining> rowMapperUID=new RowMapper<InstitutionTraining>(){
		
		@Override
		public InstitutionTraining mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionTraining training=new InstitutionTraining();
			training.setId(rs.getInt("training_id"));
			training.setDegree(rs.getInt("degree"));
			training.setMajor(rs.getString("major"));
			training.setGraduationDate(rs.getDate("graduation_date"));
			training.setEnrollmentDate(rs.getDate("enrollment_date"));
			training.setInstitutionId(rs.getInt("institution_id"));
			training.setStudentName(rs.getString("student_name"));
			training.setNationality(rs.getString("nationality"));
			training.setRemark(rs.getString("remark"));
			return training;
		}};
	
	@Override
	public Map<Integer, Integer> getDegreesByUser(int userId) {
		String sql="select r.degree,count(*) c from institution_training r,institution_training_user u "+
				"where u.user_id=:userId "+
				"and u.training_id=r.id "+
				"group by r.degree ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId", userId);
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
	public List<InstitutionTraining> getTrainingByUser(int userId,
			int offset, int pageSize, SearchInstitutionCondition condition) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("userId", userId);
		param.put("offset",offset);
		param.put("size", pageSize);
		param.put("degree", condition.getDegree());
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
//		return getNamedParameterJdbcTemplate().query(sql, param, rowMapper);
		return getNamedParameterJdbcTemplate().query(getListSqlByUser(false, condition), param, rowMapper);
	}

	@Override
	public int getTrainingCountByUser(int userId,
			SearchInstitutionCondition condition) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("userId", userId);
		param.put("degree", condition.getDegree());
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
//		return getNamedParameterJdbcTemplate().queryForInt(sql,param);
		return getNamedParameterJdbcTemplate().queryForInt(getListSqlByUser(true, condition),param);
	}
	private String getListSqlByUser(boolean isCount,SearchInstitutionCondition condition){
		StringBuffer sb=new StringBuffer();
		sb.append(" select ").append(isCount?"count(*)":"r.*").append(" from institution_training r,institution_training_user u");
		sb.append(" where r.id=u.training_id");
		sb.append(" and u.user_id=:userId");
		//关键字
		if(!CommonUtils.isNull(condition.getKeyword())){
			sb.append(" and name like :keyword ");
		}
		//学位
		if(condition.getDegree()!=0){
			sb.append(" and degree=:degree ");
		}
		if(!isCount){
			//排序
			sb.append(" order by "+ getOrderColumnByStatus(condition.getOrder())+getOrderTypeByType(condition.getOrderType()));
			sb.append(" limit :offset,:size");
		}
		return sb.toString();
	}

	@Override
	public void deleteTrainingUser(final int[] id) {
		String sql="delete from institution_training_user where training_id=?";
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
	public void insertTrainingUser(int trainingId, int userId) {
		String sql="insert into institution_training_user(`user_id`,`training_id`)"+
				" values(:userId,:trainingId)";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId",userId);
		paramMap.put("trainingId",trainingId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}
	
	
	@Override
	public void createUserRef(int userId, int trainingId) {
		String sql="insert into institution_training_user(`user_id`,`training_id`)"+
				" values(:userId,:trainingId)";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId",userId);
		paramMap.put("trainingId",trainingId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}

	@Override
	public void deleteUserRef(int uid,int trainingId) {
		String sql="delete from institution_training_user where user_id=:uid and training_id=:trainingId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("trainingId", trainingId);
		getNamedParameterJdbcTemplate().update(sql, params);
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
	public List<InstitutionTraining> getStudentByUser(int userId) {
		String sql="select * from institution_training a ,institution_member_from_vmt b where" +
				" a.umt_id=b.umt_id and b.uid=:userId";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("userId", userId);
		return getNamedParameterJdbcTemplate().query(sql,param,rowMapper);
	}
	
	@Override
	public int getTrainingCount(int insId, int departId, int year) {
		String sql="select count(*) from institution_training where institution_id=:insId ";
		StringBuilder sb=new StringBuilder();
		sb.append(sql);
		if(departId!=-1){
			sb.append(" and depart_id=:departId");
		}
		if(year!=-1){
			
			sb.append(" and (EXTRACT(year FROM enrollment_date)<=:year and EXTRACT(year FROM graduation_date)>=:year)");
		}
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		paramMap.put("departId", departId);
		paramMap.put("year", year);
		return getNamedParameterJdbcTemplate().queryForInt(sb.toString(),paramMap);
	}

	@Override
	public List<InstitutionTraining> getTrainingsByUserId(int userId,Date startTime, Date endTime) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from institution_training t,institution_member_from_vmt m where t.umt_id=m.umt_id ");
		sb.append("and t.enrollment_date>=:startTime and t.graduation_date<=:endTime ");
		sb.append("and m.uid=:userId ");
		
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId", userId);
		paramMap.put("startTime", new java.sql.Date(startTime.getTime()));
		paramMap.put("endTime", new java.sql.Date(endTime.getTime()));
		return getNamedParameterJdbcTemplate().query(sb.toString(), paramMap, rowMapper);
	}

}
