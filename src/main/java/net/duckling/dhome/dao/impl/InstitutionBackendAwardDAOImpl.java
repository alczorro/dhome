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
import net.duckling.dhome.dao.IInstitutionBackendAwardDAO;
import net.duckling.dhome.domain.institution.InstitutionAttachment;
import net.duckling.dhome.domain.institution.InstitutionAward;
import net.duckling.dhome.domain.institution.InstitutionTopic;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class InstitutionBackendAwardDAOImpl extends BaseDao implements
		IInstitutionBackendAwardDAO {

   private final int OBJ_TYPE = 1; //附件表
	
   public static final RowMapper<InstitutionAward> rowMapper = new RowMapper<InstitutionAward>() {
	   
		@Override
		public InstitutionAward mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionAward award = new InstitutionAward();
			award.setId(rs.getInt("id"));
			award.setAwardName(rs.getInt("vid_award_name"));
			award.setCompanyOrder(rs.getInt("company_order"));
			award.setInstitutionId(rs.getInt("institution_id"));
			award.setName(rs.getString("name"));
			award.setGrade(rs.getInt("vid_grade"));
			award.setGrantBody(rs.getString("granting_body"));
			award.setType(rs.getInt("vid_type"));
			award.setYear(rs.getInt("year"));
			award.setDepartId(rs.getInt("depart_id"));
			return award;
		}
	};
	
	@Override
	public void insert(List<InstitutionAward> awards) {
		if(CommonUtils.isNull(awards)){
			return;
		}
		StringBuffer sb=new StringBuffer();
		sb.append("insert into institution_award(");
		sb.append("`institution_id`,");
		sb.append("`name`,");
		sb.append("`vid_award_name`,");
		sb.append("`granting_body`,");
		sb.append("`vid_type`,");
		sb.append("`vid_grade`,");
		sb.append("`year`,");
		sb.append("`company_order`)");
		sb.append(" values");
		int index=0;
		Map<String,Object> params=new HashMap<String,Object>();
		for(InstitutionAward award:awards){
			sb.append("(");
			sb.append(":insId"+index).append(",");
			sb.append(":name"+index).append(",");
			sb.append(":awardname"+index).append(",");
			sb.append(":grant"+index).append(",");
			sb.append(":type"+index).append(",");
			sb.append(":grade"+index).append(",");
			sb.append(":year"+index).append(",");
			sb.append(":order"+index);
			sb.append("),");
			
			params.put("insId"+index, award.getInstitutionId());
			params.put("name"+index,award.getName());
			params.put("awardname"+index,award.getAwardName());
			params.put("grant"+index,award.getGrantBody());
			params.put("type"+index,award.getType());
			params.put("grade"+index,award.getGrade());
			params.put("year"+index,award.getYear());
			params.put("order"+index, award.getCompanyOrder());
			index++;
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		getNamedParameterJdbcTemplate().update(sb.toString(),params);
	}

	@Override
	public void insert(InstitutionAward award) {
		List<InstitutionAward> list = new ArrayList<InstitutionAward>();
		list.add(award);
		insert(list);
	}

	@Override
	public List<InstitutionAward> getAwards(int insId, int offset,
			int pageSize,SearchInstitutionCondition condition) {
		//String sql="select * from institution_award where institution_id=:insId limit :offset,:size";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("insId", insId);
		param.put("offset",offset);
		param.put("size", pageSize);
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
		param.put("year", condition.getYear());
		param.put("grade", condition.getGrade());
		return getNamedParameterJdbcTemplate().query(getListSql(false, condition), param, rowMapper);
	}
	
	@Override
	public int getAwardCount(int insId,SearchInstitutionCondition condition) {
		String sql="select count(*) from institution_award where institution_id=:insId";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("insId", insId);
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
		param.put("year", condition.getYear());
		param.put("grade", condition.getGrade());
		return getNamedParameterJdbcTemplate().queryForInt(getListSql(true, condition),param);
	}
	
	private String getListSql(boolean isCount,SearchInstitutionCondition condition){
		StringBuffer sb=new StringBuffer();
		sb.append(" select ").append(isCount?"count(*)":"*").append(" from institution_award ");
		sb.append(" where institution_id=:insId");
		//关键字
		if(!CommonUtils.isNull(condition.getKeyword())){
			sb.append(" and name like :keyword ");
		}
		//发布时间
		if(condition.getYear()!=0){
			sb.append(" and year=:year ");
		}
		
		if(condition.getGrade()!= -1){
			sb.append(" and vid_grade=:grade ");
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
			case SearchInstitutionCondition.ORDER_YEAR:{
				return "year";
			}
			default:{
				return "id";
			}
		}
	}
	
	@Override
	public InstitutionAward getAward(int awardId) {
		String sql="select * from institution_award where id=:id";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("id", awardId);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, param, rowMapper));	
	}


	@Override
	public void delete(int insId, int[] awardIds) {
		if(CommonUtils.isNull(awardIds)){
			return;
		}
		String sql="delete from institution_award where institution_id=:insId and id in(";
		int index=0;
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		for(int id :awardIds){
			sql+=":id"+index+",";
			paramMap.put("id"+index, id);
			index++;
		}
		sql=sql.substring(0,sql.length()-1);
		sql+=")";
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}

	@Override
	public void delete(int insId, int awardId) {
		String sql="delete from institution_award where institution_id=:insId and id =:id";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		paramMap.put("id", awardId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);

	}

	@Override
	public void deleteAll(int insId) {
		String sql="delete from institution_award where institution_id=:insId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}

	@Override
	public int create(InstitutionAward award) {
		StringBuffer sb = new StringBuffer();
		sb.append("insert into institution_award(");
		sb.append("`institution_id`,");
		sb.append("`name`,");
		sb.append("`vid_award_name`,");
		sb.append("`granting_body`,");
		sb.append("`vid_type`,");
		sb.append("`vid_grade`,");
		sb.append("`year`,");
		sb.append("`depart_id`,");
		sb.append("`company_order`)");
		sb.append(" values");
		sb.append("(");
		sb.append(":insId").append(",");
		sb.append(":name").append(",");
		sb.append(":awardname").append(",");
		sb.append(":grant").append(",");
		sb.append(":type").append(",");
		sb.append(":grade").append(",");
		sb.append(":year").append(",");
		sb.append(":departId").append(",");
		sb.append(":order");
		sb.append(")");

		KeyHolder keyHolder = new GeneratedKeyHolder();
		getNamedParameterJdbcTemplate().update(sb.toString(),new MapSqlParameterSource(award2Map(award)), keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public void update(InstitutionAward award) {
		StringBuffer sb=new StringBuffer();
		sb.append("update institution_award set ");
		sb.append("`name`=:name,");
		sb.append("`vid_award_name`=:awardname,");
		sb.append("`granting_body`=:grant,");
		sb.append("`vid_type`=:type,");
		sb.append("`vid_grade`=:grade,");
		sb.append("`year`=:year,");
		sb.append("`depart_id`=:departId,");
		sb.append("`company_order`=:order ");
		sb.append("where id=:id");
		getNamedParameterJdbcTemplate().update(sb.toString(),award2Map(award));
	}

	public Map<String, Object> award2Map(InstitutionAward award){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", award.getId());
		params.put("insId", award.getInstitutionId());
		params.put("name",award.getName());
		params.put("awardname",award.getAwardName());
		params.put("grant",award.getGrantBody());
		params.put("type",award.getType());
		params.put("grade",award.getGrade());
		params.put("year",award.getYear());
		params.put("departId",award.getDepartId());
		params.put("order", award.getCompanyOrder());
		return params;
	}

	/**
	 * 建立附件关联
	 */
	@Override
	public void createClbRef(final int awardId, final int[] clbIds,final String[] fileNames) {
		String sql="insert into attachments(`obj_id`,`obj_type`,`clb_id`,`file_name`) values(?,?,?,?)";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return clbIds.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i, awardId);
						pst.setInt(++i, OBJ_TYPE);
						pst.setInt(++i, clbIds[index]);
						pst.setString(++i, fileNames[index]);
					}
				});		
	}
	
	/**
	 * 删除附件关联
	 */
	@Override
	public void deleteClbRef(int awardId) {
		String sql="delete from attachments where obj_id=:awardId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("awardId", awardId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);		
	}
	
	/**
	 * 获取附件资料
	 * @param oid
	 * @return
	 */
	@Override
	public List<InstitutionAttachment> getAttachments(int oid){
		String sql="select * from attachments where obj_id=:oid and obj_type =:otype";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("oid", oid);
		param.put("otype", OBJ_TYPE);
		return getNamedParameterJdbcTemplate().query(sql, param, new RowMapper<InstitutionAttachment>() {
			@Override
			public InstitutionAttachment mapRow(ResultSet rs, int rowNum) throws SQLException {
				InstitutionAttachment attachment = new InstitutionAttachment();
				attachment.setId(rs.getInt("id"));
				attachment.setClbId(rs.getInt("clb_id"));
				attachment.setConnectId(rs.getInt("obj_id"));
				attachment.setCreateTime(rs.getTimestamp("create_time"));
				attachment.setFileName(rs.getString("file_name"));
				attachment.setType(rs.getInt("obj_type"));
				return attachment;
			}
		});
	}

	@Override
	public Map<Integer, Integer> getYearsMap(int institutionId) {
		String sql="select year,count(*) c from institution_award "+
				"where institution_id=:insId "+
				"group by year "+
				"order by year ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", institutionId);
		final Map<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
		getNamedParameterJdbcTemplate().query(sql, paramMap, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				result.put(rs.getInt("year"), rs.getInt("c"));
				return null;
			}
		});
		return result;
	}

	@Override
	public List<InstitutionAward> getAwardsByUID(int uid) {
		String sql="select * from institution_award_user u "+
				"left join institution_award a "+
				"on u.award_id=a.id "+
				"where u.user_id=:uid ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("uid", uid);
		return getNamedParameterJdbcTemplate().query(sql, paramMap,rowMapperUid);
	}
	
	 public static final RowMapper<InstitutionAward> rowMapperUid = new RowMapper<InstitutionAward>() {
			@Override
			public InstitutionAward mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				InstitutionAward award = new InstitutionAward();
				award.setId(rs.getInt("id"));
				award.setAwardName(rs.getInt("vid_award_name"));
				award.setCompanyOrder(rs.getInt("company_order"));
				award.setInstitutionId(rs.getInt("institution_id"));
				award.setName(rs.getString("name"));
				award.setGrade(rs.getInt("vid_grade"));
				award.setGrantBody(rs.getString("granting_body"));
				award.setType(rs.getInt("vid_type"));
				award.setYear(rs.getInt("year"));
				award.setDepartId(rs.getInt("depart_id"));
				return award;
			}
		};

	@Override
	public List<InstitutionAward> getAwardByUser(int userId, int offset,
			int pageSize, SearchInstitutionCondition condition) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("userId", userId);
		param.put("offset",offset);
		param.put("size", pageSize);
		param.put("grade", condition.getGrade());
		param.put("year", condition.getYear());
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
		return getNamedParameterJdbcTemplate().query(getListSqlByUser(false, condition), param, rowMapper);
	}

	@Override
	public int getAwardCountByUser(int userId,
			SearchInstitutionCondition condition) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("userId", userId);
		param.put("year", condition.getYear());
		param.put("grade", condition.getGrade());
		param.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
		return getNamedParameterJdbcTemplate().queryForInt(getListSqlByUser(true, condition),param);
	}
	private String getListSqlByUser(boolean isCount,SearchInstitutionCondition condition){
		StringBuffer sb=new StringBuffer();
		sb.append(" select ").append(isCount?"count(*)":"r.*").append(" from institution_award r,institution_award_user u");
		sb.append(" where r.id=u.award_id");
		sb.append(" and u.user_id=:userId");
		//关键字
		if(!CommonUtils.isNull(condition.getKeyword())){
			sb.append(" and name like :keyword ");
		}
		//年份
		if(condition.getYear()!=0){
			sb.append(" and year=:year ");
		}
		
		if(condition.getGrade()!= -1){
			sb.append(" and vid_grade=:grade ");
		}
		
		if(!isCount){
			//排序
			sb.append(" order by "+ getOrderColumnByStatus(condition.getOrder())+getOrderTypeByType(condition.getOrderType()));
			sb.append(" limit :offset,:size");
		}
		return sb.toString();
	}

	@Override
	public Map<Integer, Integer> getYearsMapByUser(int userId) {
		String sql="select r.year,count(*) c from institution_award r,institution_award_user u "+
				"where u.user_id=:userId "+
				"and u.award_id=r.id "+
				"group by r.year ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId", userId);
		final Map<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
		getNamedParameterJdbcTemplate().query(sql, paramMap, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				result.put(rs.getInt("year"), rs.getInt("c"));
				return null;
			}
		});
		return result;
	}

	@Override
	public void deleteAwardUser(final int[] id) {
		String sql="delete from institution_award_user where award_id=?";
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
	public void insertAwardUser(int awardId, int userId) {
		String sql="insert into institution_award_user(`user_id`,`award_id`)"+
				" values(:userId,:awardId)";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId",userId);
		paramMap.put("awardId",awardId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}

	@Override
	public void deleteUserRef(int uid, int awardId) {
		String sql="delete from institution_award_user where user_id=:uid and award_id=:awardId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("awardId", awardId);
		getNamedParameterJdbcTemplate().update(sql, params);
	}

	@Override
	public void createUserRef(int uid, int awardId, int authorOrder) {
		String sql="insert into institution_award_user(`user_id`,`award_id`,`author_order`)"+
				" values(:userId,:awardId,:authorOrder)";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId",uid);
		paramMap.put("awardId",awardId);
		paramMap.put("authorOrder",authorOrder);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}

	@Override
	public void updateUserRef(int uid, int awardId, int authorOrder) {
		StringBuffer sb=new StringBuffer();
		sb.append("update institution_award_user set ");
		sb.append("`author_order`=:authorOrder ");
		sb.append("where user_id=:uid and award_id=:awardId");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("awardId", awardId);
		params.put("authorOrder", authorOrder);
		getNamedParameterJdbcTemplate().update(sb.toString(),params);
	}

	@Override
	public Map<Integer, Integer> getGradesMap(int insId) {
		String sql="select vid_grade,count(*) c from institution_award "+
				"where institution_id=:insId "+
				"group by vid_grade "+
				"order by vid_grade ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId",insId);
		final Map<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
		getNamedParameterJdbcTemplate().query(sql, paramMap, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				result.put(rs.getInt("vid_grade"), rs.getInt("c"));
				return null;
			}
		});
		return result;
	}
	
	@Override
	public int getAwardCount(int insId, int departId, int year) {
		String sql="select count(*) from institution_award where institution_id=:insId ";
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
	public List<InstitutionAward> getAwardsByAuthorId(int authorId,
			int startYear, int endYear) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from institution_award p,institution_award_author_ref r where p.id=r.award_id ");
		sb.append("and p.year>=:startYear and p.year<=:endYear ");
		sb.append("and r.author_id=:authorId ");
		
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("authorId", authorId);
		paramMap.put("startYear", startYear);
		paramMap.put("endYear", endYear);
		return getNamedParameterJdbcTemplate().query(sb.toString(), paramMap, rowMapperUID);
	}
private static final RowMapper<InstitutionAward> rowMapperUID=new RowMapper<InstitutionAward>(){
		
	@Override
	public InstitutionAward mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		InstitutionAward award = new InstitutionAward();
		award.setId(rs.getInt("id"));
		award.setAwardName(rs.getInt("vid_award_name"));
		award.setCompanyOrder(rs.getInt("company_order"));
		award.setInstitutionId(rs.getInt("institution_id"));
		award.setName(rs.getString("name"));
		award.setGrade(rs.getInt("vid_grade"));
		award.setGrantBody(rs.getString("granting_body"));
		award.setType(rs.getInt("vid_type"));
		award.setYear(rs.getInt("year"));
		award.setDepartId(rs.getInt("depart_id"));
		
		award.setAuthorOrder(rs.getInt("order"));
		return award;
		}};
		
}
