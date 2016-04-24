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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.common.repository.DAOUtils;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.PinyinUtil;
import net.duckling.dhome.dao.ISimpleUserDAO;
import net.duckling.dhome.domain.people.SimpleUser;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/** 注册模块JDBC实现类 */
@Component
public class SimpleUserDAO extends BaseDao implements ISimpleUserDAO {
	
	private static final Logger LOG = Logger.getLogger(SimpleUserDAO.class);
	private static final String AUDIT_SQL=" and (`status` ='"+SimpleUser.STATUS_AUDIT_OK+"' or `status`='"+SimpleUser.STATUS_AUDIT_NEED+"' or `status` is null)";
	private static final String COMPLETE_SQL=" and `step`='"+SimpleUser.STEP_COMPLETE+"' ";
	private RowMapper<SimpleUser> rowMapper = new SimpleUserRowMapper();
	
	@Override
	public int updateSimpleUserLastEditTimeByUid(int uid) {
		String sql="update simple_user set last_edit_time=:lastEditTime where id=:id";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("id", uid);
		param.put("lastEditTime", new Date());
		return getNamedParameterJdbcTemplate().update(sql,param);
	}
	/**
	 * 用户基本信息 RowMapper
	 * @author Yangxp
	 *
	 */
	public static class SimpleUserRowMapper implements RowMapper<SimpleUser>{
		@Override
		public SimpleUser mapRow(ResultSet rs, int rowNum) throws SQLException {
			SimpleUser su = new SimpleUser();
			su.setEmail(rs.getString("email"));
			su.setEnName(rs.getString("en_name"));
			su.setId(rs.getInt("id"));
			su.setImage(rs.getInt("image"));
			su.setPinyin(rs.getString("pinyin"));
			su.setSalutation(rs.getString("salutation"));
			su.setStep(rs.getString("step"));
			su.setZhName(rs.getString("zh_name"));
			su.setAuditPropose(rs.getString("audit_propose"));
			su.setIsAdmin(rs.getBoolean("is_admin"));
			su.setStatus(rs.getString("status"));
			su.setIsMove(rs.getInt("is_move"));
			return su;
		}
		
	};
	
	@Override
	public SimpleUser getUser(int uid) {
		SimpleUser user=new SimpleUser();
		user.setId(uid);
		return findAndReturnOnly(user);
	}
	@Override
	public List<SimpleUser> getUsers(List<Integer> uids) {
		List<SimpleUser> result = new ArrayList<SimpleUser>();
		if(null != uids && !uids.isEmpty()){
			StringBuilder sql = new StringBuilder("select * from simple_user where id in(");
			for(int uid : uids){
				sql.append(uid+",");
			}
			sql.replace(sql.lastIndexOf(","), sql.length(), ")");
			result = this.getJdbcTemplate().query(sql.toString(), rowMapper);
		}
		return result;
	}
	@Override
	public int updateAccount(SimpleUser user) {
		user.setInstitutionId(null);
		user.setLastEditTime(new Date());
		return update(user);
	}
	@Override
	public boolean isEmailUsed(String email) {
		SimpleUser user=new SimpleUser();
		user.setEmail(email);
		return findAndReturnIsExist(user);
	}
	@Override
	public int registAccount(SimpleUser user) {
		if(user.getStatus()==null){
			user.setStatus(SimpleUser.STATUS_AUDIT_NEED);
		}
		user.setLastEditTime(new Date());
		return insert(user,"isAdmin,institutionId");
	}
	@Override
	public SimpleUser getUser(String email) {
		if(email==null){
			return null;
		}
		SimpleUser user=new SimpleUser();
		user.setEmail(email);
		List<SimpleUser> users=findByProperties(user);
		return CommonUtils.first(users);
	}
	@Override
	public List<SimpleUser> getAllUsers(int offset, int size) {		
		String sql = "select * from simple_user  where 1=1 "+COMPLETE_SQL+AUDIT_SQL+" order by weight desc,image desc,regist_time desc ";
		sql += getLimit(offset, size);
		return this.getJdbcTemplate().query(sql, rowMapper);
	}

	@Override
	public List<SimpleUser> getSimpleUserByDiscipline(int first, int second,int offset,int size) {
		if(first==0&&second==0){
			return null;
		}
		String sql="select * from simple_user s, detailed_user d where 1=1 "+COMPLETE_SQL+AUDIT_SQL+" and s.id=d.uid and first_class_discipline=:firstClassDiscipline "+AUDIT_SQL;
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("firstClassDiscipline", first);
		if(second!=0){
			sql+=" and second_class_discipline=:secondClassDescipline ";
			param.put("secondClassDescipline", second);
		}
		sql+=" order by weight desc,image desc  ";
		sql+=getLimit(offset,size);

		DAOUtils<SimpleUser> daoUtil=new DAOUtils<SimpleUser>(SimpleUser.class);
		return getNamedParameterJdbcTemplate().query(sql, param, daoUtil.getRowMapper(null));
	}

	@Override
	public List<SimpleUser> getLatestUsers(int offset, int size) {
		String sql = "select * from simple_user where 1=1"+COMPLETE_SQL+AUDIT_SQL+" order by regist_time desc ";
		sql += getLimit(offset, size);
		return this.getJdbcTemplate().query(sql, rowMapper);
	}
	@Override
	public List<SimpleUser> searchUsers(String keyword, int offset, int size) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from simple_user where 1=1 ");
		Map<String, Object> params = new HashMap<String, Object>();
		appendKeywordWhere(sql, keyword, params);
		sql.append(AUDIT_SQL);
		sql.append(COMPLETE_SQL);
		sql.append(" order by regist_time desc ");
		sql.append(getLimit(offset, size));
		return this.getNamedParameterJdbcTemplate().query(sql.toString(), params, rowMapper);
	}
	
	@Override
	public int getCount() {
		//已删除，审核不通过的 不包含
		String sql="select count(*) from simple_user where 1=1"+COMPLETE_SQL+" and `status`!='"+SimpleUser.STATUS_AUDIT_DELETE+"' and `status`!='"+SimpleUser.STATUS_AUDIT_NOT+"'";
		return this.getJdbcTemplate().queryForInt(sql);
	}
	@Override
	public List<SimpleUser> getAllUsers(String status, String keyword, int offset, int size) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from simple_user where 1=1");
		Map<String, Object> params = new HashMap<String, Object>();
		appendKeywordWhere(sql, keyword, params);
		sql.append(getStatusString(status));
		sql.append(" order by regist_time desc ");
		sql.append(getLimit(offset, size));
		return this.getNamedParameterJdbcTemplate().query(sql.toString(), params, rowMapper);
	}
	
	private void appendKeywordWhere(StringBuilder sql, String keyword, Map<String, Object> params){
		if(!isEmpty(keyword)){
			if(isEmpty(PinyinUtil.getPinyinMingXing(keyword))){
				sql.append(" and zh_name like :zh_name");
			}else{
				sql.append(" and (zh_name like :zh_name or pinyin like :pinyin)");
				params.put("pinyin", "%"+PinyinUtil.getPinyinOnly(keyword)+"%");
			}
			params.put("zh_name", "%"+keyword+"%");
		}
	}
	@Override
	public List<SimpleUser> getSimpleUserByInterest(String keyword, int offset, int size) {
		String sql=" select distinct s.* from interest i,simple_user s where i.keyword = :keyword and i.uid=s.id "+AUDIT_SQL+COMPLETE_SQL;
		sql += COMPLETE_SQL;
		sql+=" order by weight desc, regist_time desc ";
		sql+=getLimit(offset, size);
		Map<String,String> map=new HashMap<String,String>();
		map.put("keyword", keyword);
		return getNamedParameterJdbcTemplate().query(sql, map, rowMapper);
	}
	
	private String getLimit(int offset, int size){
		if(offset<0 || size<0){
			LOG.error("Invalid params while getLimit by offset="+offset+" and size="+size);
			return "limit 0,0";
		}
		return "limit " + offset + ", "+size;
	}
	
	private String getStatusString(String status){
		String statusStr = "";
		Set<String> statusList = SimpleUser.getUserAuditStatus();
		boolean cond = null != status && statusList.contains(status);
		if(null == status || SimpleUser.STATUS_AUDIT_NEED.equals(status)){
			statusStr = cond ?(" and (status='"+SimpleUser.STATUS_AUDIT_NEED+"' or status is null) "):" ";
		}else{
			statusStr = cond ?" and status='"+status+"' ":" ";
		}
		return statusStr;
	}
	@Override
	public SimpleUser getSimpleUserByImgId(int imgId) {
		SimpleUser u=new SimpleUser();
		u.setImage(imgId);
		return findAndReturnOnly(u);
	}
	@Override
	public int getSearchComposedUserCount(String keyword) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from simple_user where 1=1 ");
		Map<String, Object> params = new HashMap<String, Object>();
		appendKeywordWhere(sql, keyword, params);
		sql.append(AUDIT_SQL);
		sql.append(COMPLETE_SQL);
		return this.getNamedParameterJdbcTemplate().queryForInt(sql.toString(), params);
	}
	
	private boolean isEmpty(String param){
		return (null == param || "".equals(param))?true:false;
	}
	
	@Override
	public List<SimpleUser> getUsersByEmails(List<String> email) {
		StringBuffer sb=new StringBuffer();
		sb.append("select * from simple_user where email in(");
		int index=0;
		Map<String,Object> param=new HashMap<String,Object>();
		for(String e:email){
			sb.append(":email"+index).append(",");
			param.put("email"+index, e);
			index++;
		}
		sb.deleteCharAt(sb.lastIndexOf(",")).append(")");
		return getNamedParameterJdbcTemplate().query(sb.toString(), param, rowMapper);
	}
	@Override
	public List<SimpleUser> getAllUser() {
		String sql="select * from simple_user where 1=1";
		return getJdbcTemplate().query(sql,rowMapper);
	}
//	@Override
//	public List<SimpleUser> searchUser(String keyword) {
//		String sql="select * from simple_user where zh_name like :keyword or email like :keyword";
//		Map<String,Object> param=new HashMap<String,Object>();
//		param.put("keyword", "%"+keyword+"%");
//		return getNamedParameterJdbcTemplate().query(sql,param,rowMapper);
//	}
	@Override
	public void updateSimpleUserEmailByUid(int uid, String newEamil) {
		String sql="update simple_user set email=:email where id=:id";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("id", uid);
		param.put("email", newEamil);
		getNamedParameterJdbcTemplate().update(sql,param);
	}
}
