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
import net.duckling.dhome.dao.IDetailedUserDAO;
import net.duckling.dhome.domain.people.DetailedUser;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
/**
 * 用户详细信息
 * @author Yangxp
 *
 */
@Component
public class DetailedUserDAO extends BaseDao implements IDetailedUserDAO {
	
	private static final Logger LOG = Logger.getLogger(DetailedUserDAO.class);
	
	private RowMapper<DetailedUser> rowMapper = new DetailedUserRowMapper();
	/**
	 * DetailedUser RowMapper
	 * @author Yangxp
	 *
	 */
	public static class DetailedUserRowMapper implements RowMapper<DetailedUser>{

		@Override
		public DetailedUser mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			DetailedUser du = new DetailedUser();
			du.setUid(rs.getInt("uid"));
			du.setBirthday(rs.getString("birthday"));
			du.setGender(rs.getString("gender"));
			du.setBlogUrl(rs.getString("blog_url"));
			du.setWeiboUrl(rs.getString("weibo_url"));
			du.setIntroduction(rs.getString("introduction"));
			du.setFirstClassDiscipline(rs.getInt("first_class_discipline"));
			du.setSecondClassDiscipline(rs.getInt("second_class_discipline"));
			return du;
		}
		
	};
	
	@Override
	public int createDetailedUser(DetailedUser dUser) {
		return insert(dUser);
	}

	@Override
	public DetailedUser getUser(int uid) {
		String sql = "select * from detailed_user where uid="+uid;
		List<DetailedUser> list = this.getJdbcTemplate().query(sql, rowMapper);
		if(null == list || list.isEmpty()){
			return null;
		}else if(list.size()>1){
			LOG.error("there exists more than one object while query for detailedUser by uid="+uid);
		}
		return list.get(0);
	}

	@Override
	public boolean updateDetailedUserByUid(DetailedUser du) {
		String sql = "update detailed_user set birthday=:birthday, gender=:gender, blog_url=:blogUrl," +
				"weibo_url=:weiboUrl, introduction=:introduction, first_class_discipline=:firstClassDiscipline," +
				"second_class_discipline=:secondClassDiscipline where uid=:uid";
		Map<String, Object> params = getMapParams(du);
		SqlParameterSource sps = new MapSqlParameterSource(params);
		int result = getNamedParameterJdbcTemplate().update(sql, sps);
		return result>0;
	}
	
	private Map<String, Object> getMapParams(DetailedUser du){
		Map<String, Object> result = new HashMap<String,Object>();
		result.put("birthday", du.getBirthday());
		result.put("gender", du.getGender());
		result.put("blogUrl", du.getBlogUrl());
		result.put("weiboUrl", du.getWeiboUrl());
		result.put("introduction", du.getIntroduction());
		result.put("firstClassDiscipline", du.getFirstClassDiscipline());
		result.put("secondClassDiscipline", du.getSecondClassDiscipline());
		result.put("uid", ""+du.getUid());
		return result;
	}
}
