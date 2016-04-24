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
/**
 * 
 */
package net.duckling.dhome.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.common.repository.DAOUtils;
import net.duckling.dhome.dao.IInstitutionPeopleDAO;
import net.duckling.dhome.domain.people.SimpleUser;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

/**
 * 
 * 用户和机构主页关联关系表
 * @author lvly
 * @since 2012-9-18
 */
@Component
public class InstitutionPeopleDAO extends BaseDao implements IInstitutionPeopleDAO {
	private static final String AUDIT_SQL=" and (`status` ='"+SimpleUser.STATUS_AUDIT_OK+"' or `status` is null)";
	private static final String COMPLETE_SQL=" and `step`='"+SimpleUser.STEP_COMPLETE+"' ";

	@Override
	public int createInstitutionPeople(int userId, int institutionId) {
		if(userId<0){
			return -1;
		}
		String sql="insert into institution_people(uid,institution_id) values(:uid,:institution_id)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getNamedParameterJdbcTemplate().update(sql, new MapSqlParameterSource(getParamMap(userId, institutionId)), keyHolder);
		return keyHolder.getKey().intValue();
	}
	@Override
	public boolean isExists(int userId, int institutionId) {
		if(userId<0){
			return true;
		}
		String sql="select count(*) from institution_people where uid=:uid and institution_id=:institution_id";
		int count=getNamedParameterJdbcTemplate().queryForInt(sql, getParamMap(userId, institutionId));
		return count>0;
	}
	private Map<String,Object> getParamMap(int uid,int institutionId){
		Map<String,Object> map=new HashMap<String,Object>();
		if(uid>0){
			map.put("uid", uid);
		}
		map.put("institution_id", institutionId);
		return map;
	}
	private String getLimit(int offset,int size){
		return "limit "+offset+","+size;
	}
	@Override
	public List<SimpleUser> getPeoplesByInstitituionId(int institutionId, int offset, int size) {
		String sql="select s.* from simple_user s,institution_people p where p.institution_id=:institution_id and s.id=p.uid "+AUDIT_SQL+COMPLETE_SQL+" order by s.weight desc, s.image desc,s.regist_time desc "+getLimit(offset,size);
		DAOUtils<SimpleUser> daoUtils=new DAOUtils<SimpleUser>(SimpleUser.class);
		return getNamedParameterJdbcTemplate().query(sql, getParamMap(0, institutionId), daoUtils.getRowMapper(null));
	}
	
	@Override
	public List<Integer> getInstitutionHasPerson() {
		String sql = "select distinct(institution_id) from institution_people where institution_id !=0";
		return this.getJdbcTemplate().queryForList(sql, Integer.class);
	}
	@Override
	public int getMembersSize(int institutionId) {
		String sql = "select count(*) from simple_user s, institution_people p where s.id=p.uid and  institution_id=" +institutionId+AUDIT_SQL+COMPLETE_SQL;
		return getJdbcTemplate().queryForInt(sql);
	}
	@Override
	public void deleteMember(int institutionId, int uid) {
		String sql = "delete from institution_people where institution_id=" +institutionId+" and uid="+uid;
		getJdbcTemplate().update(sql);

		
	}
	@Override
	public boolean isMember(int uid, int institutionId) {
		String sql = "select count(*) from  simple_user s, institution_people p where s.id=p.uid and institution_id=" +institutionId+" and uid="+uid +AUDIT_SQL+COMPLETE_SQL;
		return getJdbcTemplate().queryForInt(sql)>0;
	}
	
}
