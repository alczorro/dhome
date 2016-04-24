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

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.dao.IInstitutionTopicDAO;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.InstitutionTopic;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class InstitutionTopicDAOImpl  extends BaseDao implements IInstitutionTopicDAO {

	
	private static final RowMapper<InstitutionTopic> rowMapper=new RowMapper<InstitutionTopic>(){
				@Override
				public InstitutionTopic mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					return bulidBaseMapRow(rs);
		}};
	private static final RowMapper<InstitutionTopic> rowMapperRole=new RowMapper<InstitutionTopic>(){
			@Override
			public InstitutionTopic mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				InstitutionTopic topic = bulidBaseMapRow(rs);
				topic.setRole(rs.getString("u.role"));
				return topic;
	}};
		
		private static InstitutionTopic bulidBaseMapRow(ResultSet rs) throws SQLException{
			InstitutionTopic topic=new InstitutionTopic();
			topic.setId(rs.getInt("id"));
			topic.setStart_year(Integer.toString(rs.getInt("start_year")));
			topic.setStart_month(Integer.toString(rs.getInt("start_month")));
			topic.setEnd_year(Integer.toString(rs.getInt("end_year")));
			topic.setEnd_month(Integer.toString(rs.getInt("end_month")));
			topic.setName(rs.getString("name"));
			topic.setType(rs.getInt("type"));
			topic.setProject_cost(rs.getInt("project_cost"));
			topic.setPersonal_cost(rs.getInt("personal_cost"));
			topic.setInstitution_id(rs.getInt("institution_id"));
			topic.setFunds_from(rs.getInt("funds_from"));
			topic.setTopic_no(rs.getString("topic_no"));
			topic.setDepartId(rs.getInt("depart_id"));
			return topic;
		}
		
		@Override
		public List<InstitutionTopic> getTopic(int id, int offset, int size, SearchInstitutionCondition condition) {
			
//			String sql="select * from institution_topic where institution_id=:id limit :offset,:size";
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("insId", id);
			param.put("offset",offset);
			param.put("size", size);
			param.put("funds_from", condition.getFundsFrom());
			param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
//			return getNamedParameterJdbcTemplate().query(sql, param, rowMapper);
			return getNamedParameterJdbcTemplate().query(getListSql(false, condition), param, rowMapper);
		}

	@Override
	public int getTopicCount(int insId, SearchInstitutionCondition condition) {
		
//		String sql="select count(*) from institution_topic where institution_id=:insId ";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("insId", insId);
		param.put("funds_from", condition.getFundsFrom());
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
//		return getNamedParameterJdbcTemplate().queryForInt(sql,param);
		return getNamedParameterJdbcTemplate().queryForInt(getListSql(true, condition),param);
	}

	private String getListSql(boolean isCount,SearchInstitutionCondition condition){
		StringBuffer sb=new StringBuffer();
		sb.append(" select ").append(isCount?"count(*)":"*").append(" from institution_topic ");
		sb.append(" where institution_id=:insId");
		//关键字
		if(!CommonUtils.isNull(condition.getKeyword())){
			sb.append(" and name like :keyword ");
		}
		//资金来源
		if(condition.getFundsFrom()!=0){
			sb.append(" and funds_from=:funds_from ");
		}
		if(!isCount){
			//排序
			sb.append(" order by "+ getOrderColumnByStatus(condition.getOrder())+getOrderTypeByType(condition.getOrderType()));
			sb.append(" limit :offset,:size");
		}
		return sb.toString();
	}
	
	@Override
	public void deleteTopic(final int[] id) {
		// TODO Auto-generated method stub
		String sql="delete from institution_topic where id=?";
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
	public void updateTopic(int id, InstitutionTopic topic) {
		// TODO Auto-generated method stub
		String sql="Update institution_topic set "
				+"start_year=:start_year,"
				+"start_month=:start_month,"
				+"end_year=:end_year,"
				+"end_month=:end_month,"
				+"name=:name,"
				+"funds_from=:funds_from,"
				+"type=:type,"
				+"topic_no=:topic_no,"
				+"depart_id=:departId,"
				+"project_cost=:project_cost,"
				+"personal_cost=:personal_cost "
				+"where id=:id";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("start_year", topic.getStart_year());
		param.put("start_month", topic.getStart_month());
		param.put("end_year", topic.getEnd_year());
		param.put("end_month", topic.getEnd_month());
		param.put("name", topic.getName());
		param.put("funds_from", topic.getFunds_from());
		param.put("type", topic.getType());
		param.put("topic_no", topic.getTopic_no());
		param.put("departId", topic.getDepartId());
		param.put("project_cost", topic.getProject_cost());
		param.put("personal_cost", topic.getPersonal_cost());
		param.put("id", id);
		getNamedParameterJdbcTemplate().update(sql, param);
	}

	@Override
	public void insert(List<InstitutionTopic> topic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insert(InstitutionTopic topic) {
		// TODO Auto-generated method stub
//		List<InstitutionTopic> list = new ArrayList<InstitutionTopic>();
//		list.add(topic);
//		insert(list);
		StringBuffer sb=new StringBuffer();
		sb.append("insert into institution_topic(");
		sb.append("`topic_no`,");
		sb.append("`name`,");
		sb.append("`start_year`,");
		sb.append("`start_month`,");
		sb.append("`end_year`,");
		sb.append("`end_month`,");
		sb.append("`funds_from`,");
		sb.append("`type`,");
//		sb.append("`role`,");
		sb.append("`project_cost`,");
		sb.append("`institution_id`,");
		sb.append("`personal_cost`) ");
		
		sb.append("values(");
		
		sb.append(":topic_no,");
		sb.append(":name,");
		sb.append(":start_year,");
		sb.append(":start_month,");
		sb.append(":end_year,");
		sb.append(":end_month,");
		sb.append(":funds_from,");
		sb.append(":type,");
//		sb.append(":role,");
		sb.append(":project_cost,");
		sb.append(":institution_id,");
		sb.append(":personal_cost) ");
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("topic_no",topic.getTopic_no());
		paramMap.put("name",topic.getName());
		paramMap.put("start_year",topic.getStart_year());
		paramMap.put("start_month",topic.getStart_month());
		paramMap.put("end_year",topic.getEnd_year());
		paramMap.put("end_month",topic.getEnd_month());
		paramMap.put("funds_from",topic.getFunds_from());
		paramMap.put("type",topic.getType());
//		paramMap.put("role",topic.getRole());
		paramMap.put("project_cost",topic.getProject_cost());
		paramMap.put("institution_id",topic.getInstitution_id());
		paramMap.put("personal_cost",topic.getPersonal_cost());
		getNamedParameterJdbcTemplate().update(sb.toString(), paramMap);
	}
	
	@Override
	public int creat(InstitutionTopic topic) {
		StringBuffer sb=new StringBuffer();
		sb.append("insert into institution_topic(");
		sb.append("`topic_no`,");
		sb.append("`name`,");
		sb.append("`start_year`,");
		sb.append("`start_month`,");
		sb.append("`end_year`,");
		sb.append("`end_month`,");
		sb.append("`funds_from`,");
		sb.append("`type`,");
		sb.append("`depart_id`,");
		sb.append("`project_cost`,");
		sb.append("`institution_id`,");
		sb.append("`personal_cost`) ");
		
		sb.append("values(");
		
		sb.append(":topic_no,");
		sb.append(":name,");
		sb.append(":start_year,");
		sb.append(":start_month,");
		sb.append(":end_year,");
		sb.append(":end_month,");
		sb.append(":funds_from,");
		sb.append(":type,");
		sb.append(":departId,");
		sb.append(":project_cost,");
		sb.append(":institution_id,");
		sb.append(":personal_cost) ");
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("topic_no",topic.getTopic_no());
		paramMap.put("name",topic.getName());
		paramMap.put("start_year",topic.getStart_year());
		paramMap.put("start_month",topic.getStart_month());
		paramMap.put("end_year",topic.getEnd_year());
		paramMap.put("end_month",topic.getEnd_month());
		paramMap.put("funds_from",topic.getFunds_from());
		paramMap.put("type",topic.getType());
		paramMap.put("departId",topic.getDepartId());
		paramMap.put("project_cost",topic.getProject_cost());
		paramMap.put("institution_id",topic.getInstitution_id());
		paramMap.put("personal_cost",topic.getPersonal_cost());
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getNamedParameterJdbcTemplate().update(sb.toString(),new MapSqlParameterSource(topicMap(topic)), keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	public Map<String, Object> topicMap(InstitutionTopic topic){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", topic.getId());
		params.put("end_month", topic.getEnd_month());
		params.put("end_year",topic.getEnd_year());
		params.put("funds_from",topic.getFunds_from());
		params.put("name",topic.getName());
		params.put("institution_id",topic.getInstitution_id());
		params.put("personal_cost",topic.getPersonal_cost());
		params.put("project_cost",topic.getProject_cost());
		params.put("departId",topic.getDepartId());
		params.put("start_month",topic.getStart_month());
		params.put("start_year",topic.getStart_year());
		params.put("topic_no",topic.getTopic_no());
		params.put("type",topic.getType());
		return params;
	}

	@Override
	public InstitutionTopic getTopicById(int id) {
		String sql="select * from institution_topic where id=:id ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("id",id);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap,rowMapper));
	}
	@Override
	public InstitutionTopic getTopicRoleById(int id,int userId) {
		String sql="select r.*,u.role from institution_topic r,institution_topic_user u where r.id=u.topic_id and r.id=:id and user_id=:userId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("id",id);
		paramMap.put("userId",userId);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap,rowMapperRole));
	}

	@Override
	public Map<Integer, Integer> getFundsFromsMap(int institutionId) {
		String sql="select funds_from,count(*) c from institution_topic "+
				"where institution_id=:insId "+
				"group by funds_from ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", institutionId);
		final Map<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
		getNamedParameterJdbcTemplate().query(sql, paramMap, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				result.put(rs.getInt("funds_from"), rs.getInt("c"));
				return null;
			}
		});
		return result;
	}
	private String getListSqlByUser(boolean isCount,SearchInstitutionCondition condition){
		StringBuffer sb=new StringBuffer();
		sb.append(" select ").append(isCount?"count(*)":"r.*,u.role").append(" from institution_topic r,institution_topic_user u");
		sb.append(" where r.id=u.topic_id");
		sb.append(" and u.user_id=:userId");
		//关键字
		if(!CommonUtils.isNull(condition.getKeyword())){
			sb.append(" and name like :keyword ");
		}
		//资金来源
		if(condition.getFundsFrom()!=0){
			sb.append(" and funds_from=:funds_from ");
		}
		if(!isCount){
			//排序
			sb.append(" order by "+ getOrderColumnByStatus(condition.getOrder())+getOrderTypeByType(condition.getOrderType()));
			sb.append(" limit :offset,:size");
		}
		return sb.toString();
	}
	@Override
	public List<InstitutionTopic> getTopicByUser(int userId, int offset,
			int pageSize, SearchInstitutionCondition condition) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("userId", userId);
		param.put("offset",offset);
		param.put("size", pageSize);
		param.put("funds_from", condition.getFundsFrom());
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
//		return getNamedParameterJdbcTemplate().query(sql, param, rowMapper);
		return getNamedParameterJdbcTemplate().query(getListSqlByUser(false, condition), param, rowMapperRole);
	}

	@Override
	public int getTopicCountByUser(int userId,
			SearchInstitutionCondition condition) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("userId", userId);
		param.put("funds_from", condition.getFundsFrom());
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
//		return getNamedParameterJdbcTemplate().queryForInt(sql,param);
		return getNamedParameterJdbcTemplate().queryForInt(getListSqlByUser(true, condition),param);
	}

	@Override
	public Map<Integer, Integer> getFundsFromsMapByUser(int userId) {
		String sql="select r.funds_from,count(*) c from institution_topic r,institution_topic_user u "+
				"where u.user_id=:userId "+
				"and u.topic_id=r.id "+
				"group by r.funds_from ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId", userId);
		final Map<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
		getNamedParameterJdbcTemplate().query(sql, paramMap, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				result.put(rs.getInt("funds_from"), rs.getInt("c"));
				return null;
			}
		});
		return result;
	}

	@Override
	public void deleteTopicUser(final int[] id) {
		String sql="delete from institution_topic_user where topic_id=?";
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
	public void insertTopicUser(int topicId, int userId,String role) {
		String sql="insert into institution_topic_user(`user_id`,`topic_id`,`role`)"+
				" values(:userId,:topicId,:role)";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId",userId);
		paramMap.put("topicId",topicId);
		paramMap.put("role",role);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}
	

	@Override
	public List<InstitutionTopic> getTopicsByUID(int uid) {
		String sql="select * from institution_topic_user u "+
				"left join institution_topic t "+
				"on u.topic_id=t.id "+
				"where u.user_id=:uid ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("uid", uid);
		return getNamedParameterJdbcTemplate().query(sql, paramMap,rowMapperUID);
	}
	
	
   private static final RowMapper<InstitutionTopic> rowMapperUID=new RowMapper<InstitutionTopic>(){
		
		@Override
		public InstitutionTopic mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionTopic topic=new InstitutionTopic();
			topic.setId(rs.getInt("topic_id"));
			topic.setStart_year(Integer.toString(rs.getInt("start_year")));
			topic.setStart_month(Integer.toString(rs.getInt("start_month")));
			topic.setEnd_year(Integer.toString(rs.getInt("end_year")));
			topic.setEnd_month(Integer.toString(rs.getInt("end_month")));
			topic.setName(rs.getString("name"));
			topic.setType(rs.getInt("type"));
			topic.setRole(rs.getString("role"));
			topic.setProject_cost(rs.getInt("project_cost"));
			topic.setPersonal_cost(rs.getInt("personal_cost"));
			topic.setInstitution_id(rs.getInt("institution_id"));
			topic.setFunds_from(rs.getInt("funds_from"));
			topic.setTopic_no(rs.getString("topic_no"));
			return topic;
		}};
		
		
		@Override
		public void createUserRef(int userId, int topicId,String role) {
			String sql="insert into institution_topic_user(`user_id`,`topic_id`,`role`)"+
					" values(:userId,:topicId,:role)";
			Map<String,Object> paramMap=new HashMap<String,Object>();
			paramMap.put("userId",userId);
			paramMap.put("topicId",topicId);
			paramMap.put("role",role);
			getNamedParameterJdbcTemplate().update(sql, paramMap);
		}
		
		@Override
		public void updateUserRef(int uid, int topicId, String role) {
			StringBuffer sb=new StringBuffer();
			sb.append("update institution_topic_user set ");
			sb.append("`role`=:role ");
			sb.append("where user_id=:uid and topic_id=:topicId");

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("uid", uid);
			params.put("topicId", topicId);
			params.put("role", role);
			getNamedParameterJdbcTemplate().update(sb.toString(),params);
		}

		@Override
		public void deleteUserRef(int uid,int topicId) {
			String sql="delete from institution_topic_user where user_id=:uid and topic_id=:topicId";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("uid", uid);
			params.put("topicId", topicId);
			getNamedParameterJdbcTemplate().update(sql, params);
		}

		@Override
		public void updateRole(String role,int userId, int topicId) {
			String sql="Update institution_topic_user set role=:role where user_id=:userId and topic_id=:topicId";
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("role", role);
			param.put("userId", userId);
			param.put("topicId", topicId);
			getNamedParameterJdbcTemplate().update(sql, param);
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
		public int getTopicCount(int insId, int departId, int year) {
			String sql="select count(*) from institution_topic where institution_id=:insId ";
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
		public List<InstitutionTopic> getTopicsByAuthorId(int authorId,int userId,
				int startYear, int endYear) {
			StringBuffer sb = new StringBuffer();
			sb.append("select p.*,u.role from institution_topic p,institution_topic_user u,institution_topic_author_ref r where p.id=r.topic_id and u.topic_id=p.id ");
			sb.append("and p.start_year>=:startYear and p.end_year<=:endYear ");
			sb.append("and r.author_id=:authorId ");
			sb.append("and u.user_id=:userId ");
//			String sql="select * from institution_paper p,institution_paper_ref r where p.id=r.paper_id and r.author_id=:authorId" +
//					"and (r.order=1 or r.communication_author=true or(r.order=2 and r.author_student=true) or " +
//					"(r.order=2 and r.author_teacher=true)) and p.publication_year>=:startYear and p.publication_year<:endYear";
			Map<String, Object> paramMap=new HashMap<String,Object>();
			paramMap.put("authorId", authorId);
			paramMap.put("userId", userId);
			paramMap.put("startYear", startYear);
			paramMap.put("endYear", endYear);
			return getNamedParameterJdbcTemplate().query(sb.toString(), paramMap, rowMapperRole);
		}
}
