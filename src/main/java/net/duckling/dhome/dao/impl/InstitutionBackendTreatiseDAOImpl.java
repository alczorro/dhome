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

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.dao.IInstitutionBackendTreatiseDAO;
import net.duckling.dhome.domain.institution.InstitutionAcademic;
import net.duckling.dhome.domain.institution.InstitutionTreatise;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;

@Repository
public class InstitutionBackendTreatiseDAOImpl extends BaseDao implements
		IInstitutionBackendTreatiseDAO {

   public static final RowMapper<InstitutionTreatise> rowMapper = new RowMapper<InstitutionTreatise>() {
		
		@Override
		public InstitutionTreatise mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionTreatise treatise = new InstitutionTreatise();
			treatise.setId(rs.getInt("id"));
			treatise.setInstitutionId(rs.getInt("institution_id"));
			treatise.setName(rs.getString("name"));
			treatise.setLanguage(rs.getString("language"));
			treatise.setCompanyOrder(rs.getInt("company_order"));
			treatise.setPublisher(rs.getInt("vid_publisher"));
			treatise.setYear(rs.getInt("year"));
			treatise.setDepartId(rs.getInt("depart_id"));
			return treatise;
		}
	};
	
	@Override
	public void insert(List<InstitutionTreatise> treatises) {
		if(CommonUtils.isNull(treatises)){
			return;
		}
		StringBuffer sb=new StringBuffer();
		sb.append("insert into institution_treatise(");
		sb.append("`institution_id`,");
		sb.append("`name`,");
		sb.append("`vid_publisher`,");
		sb.append("`language`,");
		sb.append("`year`,");
		sb.append("`depart_id`,");
		sb.append("`company_order`)");
		sb.append(" values");
		int index=0;
		Map<String,Object> params=new HashMap<String,Object>();
		for(InstitutionTreatise treatise:treatises){
			sb.append("(");
			sb.append(":insId"+index).append(",");
			sb.append(":name"+index).append(",");
			sb.append(":publisher"+index).append(",");
			sb.append(":language"+index).append(",");
			sb.append(":year"+index).append(",");
			sb.append(":departId"+index).append(",");
			sb.append(":order"+index);
			sb.append("),");
			
			params.put("insId"+index, treatise.getInstitutionId());
			params.put("name"+index,treatise.getName());
			params.put("publisher"+index,treatise.getPublisher());
			params.put("language"+index,treatise.getLanguage());
			params.put("year"+index,treatise.getYear());
			params.put("departId"+index,treatise.getDepartId());
			params.put("order"+index, treatise.getCompanyOrder());
			index++;
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		getNamedParameterJdbcTemplate().update(sb.toString(),params);
	}

	@Override
	public void insert(InstitutionTreatise treatise) {
		List<InstitutionTreatise> list = new ArrayList<InstitutionTreatise>();
		list.add(treatise);
		insert(list);
	}

	@Override
	public List<InstitutionTreatise> getTreatises(int insId, int offset,
			int pageSize,SearchInstitutionCondition condition) {
	//	String sql="select * from institution_treatise where institution_id=:insId limit :offset,:size";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("insId", insId);
		param.put("offset",offset);
		param.put("size", pageSize);
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
		param.put("publisher", condition.getPublisher());
		return getNamedParameterJdbcTemplate().query(getListSql(false, condition), param, rowMapper);
	}

	@Override
	public InstitutionTreatise getTreatise(int treatiseId) {
		String sql="select * from institution_treatise where id=:id";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("id", treatiseId);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, param, rowMapper));	
	}

	@Override
	public int getTreatiseCount(int insId,SearchInstitutionCondition condition) {
		String sql="select count(*) from institution_treatise where institution_id=:insId";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("insId", insId);
		param.put("publisher", condition.getPublisher());
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
		return getNamedParameterJdbcTemplate().queryForInt(getListSql(true, condition),param);
	}

	private String getListSql(boolean isCount,SearchInstitutionCondition condition){
		StringBuffer sb=new StringBuffer();
		sb.append(" select ").append(isCount?"count(*)":"*").append(" from institution_treatise ");
		sb.append(" where institution_id=:insId");
		//关键字
		if(!CommonUtils.isNull(condition.getKeyword())){
			sb.append(" and name like :keyword ");
		}
		//发布时间
		if(condition.getPublisher()!= -1){
			sb.append(" and vid_publisher=:publisher ");
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
	
	@Override
	public void delete(int insId, int[] treatiseIds) {
		if(CommonUtils.isNull(treatiseIds)){
			return;
		}
		String sql="delete from institution_treatise where institution_id=:insId and id in(";
		int index=0;
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		for(int id :treatiseIds){
			sql+=":id"+index+",";
			paramMap.put("id"+index, id);
			index++;
		}
		sql=sql.substring(0,sql.length()-1);
		sql+=")";
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}

	@Override
	public void delete(int insId, int treatiseId) {
		String sql="delete from institution_treatise where institution_id=:insId and id =:id";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		paramMap.put("id", treatiseId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);

	}

	@Override
	public void deleteAll(int insId) {
		String sql="delete from institution_treatise where institution_id=:insId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}

	@Override
	public int create(InstitutionTreatise treatise) {
		StringBuffer sb = new StringBuffer();
		sb.append("insert into institution_treatise(");
		sb.append("`institution_id`,");
		sb.append("`name`,");
		sb.append("`vid_publisher`,");
		sb.append("`language`,");
		sb.append("`year`,");
		sb.append("`depart_id`,");
		sb.append("`company_order`)");
		sb.append(" values");
		sb.append("(");
		sb.append(":insId").append(",");
		sb.append(":name").append(",");
		sb.append(":publisher").append(",");
		sb.append(":language").append(",");
		sb.append(":year").append(",");
		sb.append(":departId").append(",");
		sb.append(":order");
		sb.append(")");

		KeyHolder keyHolder = new GeneratedKeyHolder();
		getNamedParameterJdbcTemplate().update(sb.toString(),new MapSqlParameterSource(treatise2Map(treatise)), keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public void update(InstitutionTreatise treatise) {
		StringBuffer sb=new StringBuffer();
		sb.append("update institution_treatise set ");
		sb.append("`name`=:name,");
		sb.append("`vid_publisher`=:publisher,");
		sb.append("`language`=:language,");
		sb.append("`year`=:year,");
		sb.append("`depart_id`=:departId,");
		sb.append("`company_order`=:order ");
		sb.append("where id=:id");
		getNamedParameterJdbcTemplate().update(sb.toString(),treatise2Map(treatise));
	}

	public Map<String, Object> treatise2Map(InstitutionTreatise treatise){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", treatise.getId());
		params.put("insId", treatise.getInstitutionId());
		params.put("name",treatise.getName());
		params.put("publisher",treatise.getPublisher());
		params.put("language",treatise.getLanguage());
		params.put("order", treatise.getCompanyOrder());
		params.put("year", treatise.getYear());
		params.put("departId", treatise.getDepartId());
		return params;
	}

	@Override
	public List<InstitutionTreatise> getTreatisesByUID(int uid) {
		String sql="select * from institution_treatise_user u "+
				"left join institution_treatise t "+
				"on u.treatise_id=t.id "+
				"where u.user_id=:uid ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("uid", uid);
		return getNamedParameterJdbcTemplate().query(sql, paramMap,rowMapperUid);
	}
	
   public static final RowMapper<InstitutionTreatise> rowMapperUid = new RowMapper<InstitutionTreatise>() {
		
		@Override
		public InstitutionTreatise mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionTreatise treatise = new InstitutionTreatise();
			treatise.setId(rs.getInt("id"));
			treatise.setInstitutionId(rs.getInt("institution_id"));
			treatise.setName(rs.getString("name"));
			treatise.setLanguage(rs.getString("language"));
			treatise.setCompanyOrder(rs.getInt("company_order"));
			treatise.setPublisher(rs.getInt("vid_publisher"));
			treatise.setYear(rs.getInt("year"));
			treatise.setDepartId(rs.getInt("depart_id"));
			return treatise;
		}
	};

	@Override
	public List<InstitutionTreatise> getTreatiseByUser(int userId, int offset,
			int pageSize, SearchInstitutionCondition condition) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("userId", userId);
		param.put("offset",offset);
		param.put("size", pageSize);
	//	param.put("grade", condition.getGrade());
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
		return getNamedParameterJdbcTemplate().query(getListSqlByUser(false, condition), param, rowMapper);
	}
	
	@Override
	public int getTreatiseCountByUser(int userId,
			SearchInstitutionCondition condition) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("userId", userId);
	//	param.put("grade", condition.getGrade());
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
		return getNamedParameterJdbcTemplate().queryForInt(getListSqlByUser(true, condition),param);
	}
	private String getListSqlByUser(boolean isCount,SearchInstitutionCondition condition){
		StringBuffer sb=new StringBuffer();
		sb.append(" select ").append(isCount?"count(*)":"r.*").append(" from institution_treatise r,institution_treatise_user u");
		sb.append(" where r.id=u.treatise_id");
		sb.append(" and u.user_id=:userId");
		//关键字
		if(!CommonUtils.isNull(condition.getKeyword())){
			sb.append(" and name like :keyword ");
		}
	//	//等级
	//	if(condition.getGrade()!=0){
	//		sb.append(" and grade=:grade ");
	//	}
		if(!isCount){
			//排序
			sb.append(" order by "+ getOrderColumnByStatus(condition.getOrder())+getOrderTypeByType(condition.getOrderType()));
			sb.append(" limit :offset,:size");
		}
		return sb.toString();
	}
	
	@Override
	public void deleteTreatiseUser(final int[] id) {
		String sql="delete from institution_treatise_user where treatise_id=?";
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
	public void insertTreatiseUser(int treatiseId, int userId) {
		String sql="insert into institution_treatise_user(`user_id`,`treatise_id`)"+
				" values(:userId,:treatiseId)";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId",userId);
		paramMap.put("treatiseId",treatiseId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}
	
	@Override
	public void createUserRef(int userId, int treatiseId,int authorOrder) {
		String sql="insert into institution_treatise_user(`user_id`,`treatise_id`,`author_order`)"+
				" values(:userId,:treatiseId,:authorOrder)";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId",userId);
		paramMap.put("treatiseId",treatiseId);
		paramMap.put("authorOrder",authorOrder);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}

	@Override
	public void deleteUserRef(int uid,int tid) {
		String sql="delete from institution_treatise_user where user_id=:uid and treatise_id=:tid";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("tid", tid);
		getNamedParameterJdbcTemplate().update(sql, params);
	}

	@Override
	public void updateUserRef(int uid, int tid, int authorOrder) {
		StringBuffer sb=new StringBuffer();
		sb.append("update institution_treatise_user set ");
		sb.append("`author_order`=:authorOrder ");
		sb.append("where user_id=:uid and treatise_id=:tid");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("tid", tid);
		params.put("authorOrder", authorOrder);
		getNamedParameterJdbcTemplate().update(sb.toString(),params);
	}

	@Override
	public Map<Integer, Integer> getPublishersMap(int insId) {
		String sql="select vid_publisher,count(*) c from institution_treatise "+
				"where institution_id=:insId "+
				"group by vid_publisher "+
				"order by vid_publisher ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId",insId);
		final Map<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
		getNamedParameterJdbcTemplate().query(sql, paramMap, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				result.put(rs.getInt("vid_publisher"), rs.getInt("c"));
				return null;
			}
		});
		return result;
	}
	@Override
	public int getTreatiseId(String name) {
		String sql="select id from institution_treatise where name=:name";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("name",name);
		return getNamedParameterJdbcTemplate().queryForInt(sql,map);
	}
	@Override
	public List<InstitutionTreatise> insertTreatises(List<InstitutionTreatise> treatises) {
		if(CommonUtils.isNull(treatises)){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("insert into institution_treatise(");
		sb.append("`institution_id`,");
		sb.append("`name`,");
		sb.append("`vid_publisher`,");
		sb.append("`language`,");
		sb.append("`year`,");
		sb.append("`depart_id`,");
		sb.append("`company_order`)");
//		sb.append(" values");
//		sb.append("(");
//		sb.append(":insId").append(",");
//		sb.append(":name").append(",");
//		sb.append(":publisher").append(",");
//		sb.append(":language").append(",");
//		sb.append(":year").append(",");
//		sb.append(":departId").append(",");
//		sb.append(":order");
		sb.append(" select ");
		sb.append(":insId").append(",");
		sb.append(":name").append(",");
		sb.append(":publisher").append(",");
		sb.append(":language").append(",");
		sb.append(":year").append(",");
		sb.append(":departId").append(",");
		sb.append(":order ");
		sb.append("from dual where not exists(select * from institution_treatise where name=:name)");
		
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		for (InstitutionTreatise treatise : treatises) {
			getNamedParameterJdbcTemplate().update(sb.toString(),new MapSqlParameterSource(treatise2Map(treatise)), keyHolder);
			if(keyHolder.getKey()!=null){
				treatise.setId(keyHolder.getKey().intValue());
			}else{
				treatise.setId(getTreatiseId(treatise.getName()));
				updateTreatises(treatise);
			}
			
		}
		
		return treatises;
	}
	@Override
	public void updateTreatises(InstitutionTreatise treatise) {
		StringBuffer sb = new StringBuffer();
		sb.append("update institution_treatise ");
		sb.append("set `institution_id`=:insId,");
		sb.append("`vid_publisher`=:publisher,");
		sb.append("`language`=:language,");
		sb.append("`year`=:year,");
		sb.append("`depart_id`=:departId,");
		sb.append("`company_order`=:order");
		sb.append(" where name=:name");
		
		getNamedParameterJdbcTemplate().update(sb.toString(),treatise2Map(treatise));
	}

	@Override
	public int getTreatiseCount(int insId, int departId, int year) {
		String sql="select count(*) from institution_treatise where institution_id=:insId ";
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

}
