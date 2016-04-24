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
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import net.duckling.dhome.dao.IInstitutionPeriodicalSearchDAO;
import net.duckling.dhome.domain.institution.InstitutionPeriodical;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.common.util.CommonUtils;

@Repository
public class InstitutionPeriodicalSearchDAOImpl extends BaseDao implements
		IInstitutionPeriodicalSearchDAO {


private static final RowMapper<InstitutionPeriodical> rowMapper=new RowMapper<InstitutionPeriodical>(){
		
		@Override
		public InstitutionPeriodical mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionPeriodical periodical=new InstitutionPeriodical();
			periodical.setId(rs.getInt("id"));
			periodical.setInstitutionId(rs.getInt("institution_id"));
			periodical.setName(rs.getInt("periodical_name"));
			periodical.setPosition(rs.getInt("position"));
			periodical.setStartYear(rs.getInt("start_year"));
			periodical.setStartMonth(rs.getInt("start_month"));
			periodical.setEndYear(rs.getInt("end_year"));
			periodical.setEndMonth(rs.getInt("end_month"));
			periodical.setAuthor(rs.getString("au_name"));
			return periodical;
		}};
	
	@Override
	public List<InstitutionPeriodical> getPeriodicalByKeyword(int offset, int size, 
			String keyword) {
		String sql="SELECT d.* from (SELECT a.*,group_concat(c.true_name) as au_name from institution_periodical_assignment a "+
			"left join (institution_periodical_author_ref b join institution_member_from_vmt c on b.author_id=c.umt_id) on a.id=b.periodical_assignment_id "+
			"GROUP BY a.id having au_name like :keyword"+
			") d limit :offset,:size" ;
//		System.out.println(getListSql(true, condition)+"=====");
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("keyword", "%"+CommonUtils.killNull(keyword)+"%");
		param.put("offset",offset);
		param.put("size", size);
		return getNamedParameterJdbcTemplate().query(sql, param, rowMapper);
	}

	@Override
	public List<InstitutionPeriodical> getPeriodicalByInit(int offset, int size,
			String userName) {
		String sql="SELECT d.* from (SELECT a.*,group_concat(c.true_name) as au_name from institution_periodical_assignment a "+
				"left join (institution_periodical_author_ref b join institution_member_from_vmt c on b.author_id=c.umt_id) on a.id=b.periodical_assignment_id "+
				"GROUP BY a.id having au_name like :keyword"+
				") d limit :offset,:size" ;
//			System.out.println(getListSql(true, condition)+"=====");
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("keyword", "%"+CommonUtils.killNull(userName)+"%");
			param.put("offset",offset);
			param.put("size", size);
			return getNamedParameterJdbcTemplate().query(sql, param, rowMapper);
	}

	@Override
	public void insertPeriodical(final String[] periodicalId,final int userId) {
//		String sql="insert into institution_periodical_assignment_user(`user_id`,`periodical_assignment_id`) values(?,?)";
		String sql="insert into institution_periodical_assignment_user(`user_id`,`periodical_assignment_id`) select ?,? from dual where not exists" +
				"(select * from institution_periodical_assignment_user where periodical_assignment_id=? and user_id=?)";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return periodicalId.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i,userId);
						pst.setInt(++i,Integer.parseInt(periodicalId[index]));
						pst.setInt(++i,Integer.parseInt(periodicalId[index]));
						pst.setInt(++i,userId);
					}
				});
	}
	@Override
	public List<Integer> getExistPeriodicalIds(int uid) {
		String sql="select periodical_assignment_id from institution_periodical_assignment_user where user_id=:uid";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("uid", uid);
		
		List<Integer> ids= getNamedParameterJdbcTemplate().query(sql, paramMap,new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("periodical_assignment_id");
			}
		});
		return ids;
	}

}
