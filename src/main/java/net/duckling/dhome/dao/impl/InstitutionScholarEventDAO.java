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
import java.util.Date;
import java.util.List;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.dao.IInstitutionScholarEventDAO;
import net.duckling.dhome.domain.institution.ScholarEvent;
import net.duckling.dhome.domain.institution.ScholarEventDetail;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
/**
 * 机构学术活动的数据库接口实现类
 * @author Yangxp
 * @since 2012-09-25
 */
@Repository
public class InstitutionScholarEventDAO extends BaseDao implements IInstitutionScholarEventDAO {

	private static final Logger LOG = Logger.getLogger(InstitutionScholarEventDAO.class);
	
	private RowMapper<ScholarEventDetail> rowMapper = new ScholarEventDetailRowMapper();
	/**
	 * 学术活动 RowMapper
	 * @author Yangxp
	 *
	 */
	public static class ScholarEventDetailRowMapper implements RowMapper<ScholarEventDetail>{

		@Override
		public ScholarEventDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			ScholarEventDetail sed = new ScholarEventDetail();
			sed.setId(rs.getInt("id"));
			sed.setTitle(rs.getString("title"));
			sed.setReporter(rs.getString("reporter"));
			sed.setStartTime(new Date(rs.getTimestamp("start_time").getTime()));
			sed.setEndTime(new Date(rs.getTimestamp("end_time").getTime()));
			sed.setCreator(rs.getInt("creator"));
			sed.setCreateTime(new Date(rs.getTimestamp("create_time").getTime()));
			sed.setIntroduction(rs.getString("introduction"));
			sed.setLogoId(rs.getInt("logo_id"));
			sed.setInstitutionId(rs.getInt("institution_id"));
			sed.setPlace(rs.getString("place"));
			sed.setCreatorName(rs.getString("zh_name"));
			return sed;
		}
		
	};
	
	@Override
	public int create(ScholarEvent se) {
		return insert(se);
	}

	@Override
	public void updateByID(ScholarEvent se) {
		update(se);
	}

	@Override
	public void remove(int id) {
		String sql = "delete from scholar_event where id="+id;
		this.getJdbcTemplate().update(sql);
	}

	@Override
	public ScholarEvent getScholarEventByID(int id) {
		ScholarEvent se = new ScholarEvent();
		se.setId(id);
		return findAndReturnOnly(se);
	}

	@Override
	public List<ScholarEventDetail> getAllScholarEventOfInstitution(int insId, int offset, int size) {
		String sql = getBaseSQL(insId) +" order by start_time desc "
				+getLimit(offset, size);
		return this.getJdbcTemplate().query(sql, rowMapper);
	}

	@Override
	public List<ScholarEventDetail> getUpcomingScholarEventOfInstitution(int insId,
			int offset, int size) {
		String sql = getBaseSQL(insId) +" and start_time>=now() and end_time >= now() order by start_time, end_time "
			+getLimit(offset, size);
		return this.getJdbcTemplate().query(sql, rowMapper);
	}

	@Override
	public List<ScholarEventDetail> getOngoingScholarEventOfInstitution(
			int insId, int offset, int size) {
		String sql = getBaseSQL(insId) +" and start_time <=now() and end_time>= now() order by start_time, end_time "
				+getLimit(offset, size);
		return this.getJdbcTemplate().query(sql, rowMapper);
	}

	@Override
	public List<ScholarEventDetail> getExpiredScholarEventOfInstitution(int insId,
			int offset, int size) {
		String sql = getBaseSQL(insId) +" and end_time < now() order by end_time desc, start_time "
				+getLimit(offset, size);
		return this.getJdbcTemplate().query(sql, rowMapper);
	}
	
	private String getBaseSQL(int insId){
		return "select a.*, b.zh_name from scholar_event a inner join simple_user b on " +
				"a.creator=b.id where institution_id="+insId+" ";
	}
	
	private String getLimit(int offset, int size){
		if(offset<0 || size<0){
			LOG.error("Invalid params while getLimit by offset="+offset+" and size="+size);
			return " limit 0,0";
		}
		return " limit " + offset + ", "+size;
	}
	@Override
	public int getActivityCount(int institutionId) {
		ScholarEvent se = new ScholarEvent();
		se.setInstitutionId(institutionId);
		return getCount(se);
	}

}
