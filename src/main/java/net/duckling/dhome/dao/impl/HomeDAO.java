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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.dao.IHomeDAO;
import net.duckling.dhome.domain.people.Home;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
/**个人域名数据jdbc实现类*/
@Component
public class HomeDAO extends BaseDao implements IHomeDAO {
	
	private RowMapper<Home> rowMapper = new HomeRowMapper();
	
	/**
	 * Home RowMapper
	 * @author Yangxp
	 *
	 */
	public static class HomeRowMapper implements RowMapper<Home>{

		@Override
		public Home mapRow(ResultSet rs, int rowNum) throws SQLException {
			Home home = new Home();
			home.setId(rs.getInt("id"));
			home.setUid(rs.getInt("uid"));
			home.setThemeid(rs.getInt("template_id"));
			home.setThemeid(rs.getInt("themeid"));
			home.setLanguage(rs.getString("language"));
			home.setUrl(rs.getString("url"));
			return home;
		}
		
	};
	
	@Override
	public int createHome(Home home) {
		home.setLanguage(Home.LANGUAGE_ZH);
		return insert(home, "template,menu,frames");
	}

	@Override
	public boolean isDomainUsed(String domain) {
		Home home = new Home();
		home.setUrl(domain);
		return !CommonUtils.isNull(findByProperties(home));
	}

	@Override
	public String getUrlFromUid(int uid) {
		Home home = new Home();
		home.setUid(uid);
		home = CommonUtils.first(findByProperties(home));
		return home == null ? "" : home.getUrl();

	}

	@Override
	public int getUidFromDomain(String domain) {
		Home home = new Home();
		home.setUrl(domain);
		home = CommonUtils.first(findByProperties(home));
		return (home!=null)?home.getUid():0;
	}

	@Override
	public Home getHomeByDomain(String domain) {
		Home home = new Home();
		home.setUrl(domain);
		List<Home> homes=findByProperties(home);
		return CommonUtils.first(homes);
	}

	@Override
	public boolean updateHome(Home home) {		
		return update(home)>0;
	}

	@Override
	public Map<Integer, String> getDomainByUID(List<Integer> uids) {
		Map<Integer, String> result = new HashMap<Integer, String>();
		if(null != uids && ! uids.isEmpty()){
			String sql = "select * from home " + getUIDWhere(uids);
			List<Home> homes = this.getJdbcTemplate().query(sql, rowMapper);
			for(Home home : homes){
				result.put(home.getUid(), home.getUrl());
			}
		}
		return result;
	}
	
	private String getUIDWhere(List<Integer> uids){
		StringBuilder sbCond = new StringBuilder();
		sbCond.append("where uid in(");
		for(int uid : uids){
			sbCond.append(uid+",");
		}
		sbCond.replace(sbCond.lastIndexOf(","), sbCond.length(), ")");
		return sbCond.toString();
	}
	@Override
	public List<Home> getDomainsByUids(List<Integer> uids) {
		StringBuffer sb=new StringBuffer();
		sb.append("select * from home ");
		sb.append(" where uid in(");
		int index=0;
		Map<String,Object> paramMap=new HashMap<String,Object>();
		for(int uid:uids){
			sb.append(":uid").append(index).append(",");
			paramMap.put("uid"+index, uid);
			index++;
		}
		sb.deleteCharAt(sb.lastIndexOf(",")).append(")");
		return getNamedParameterJdbcTemplate().query(sb.toString(), paramMap, rowMapper);
	}
	
}
