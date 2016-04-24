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

import net.duckling.dhome.dao.IInstitutionAwardSearchDAO;
import net.duckling.dhome.domain.institution.InstitutionAward;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.common.util.CommonUtils;

@Repository
public class InstitutionAwardSearchDAOImpl extends BaseDao implements
		IInstitutionAwardSearchDAO {


private static final RowMapper<InstitutionAward> rowMapper=new RowMapper<InstitutionAward>(){
		
		@Override
		public InstitutionAward mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionAward award=new InstitutionAward();
			award.setId(rs.getInt("id"));
			award.setAwardName(rs.getInt("vid_award_name"));
			award.setCompanyOrder(rs.getInt("company_order"));
			award.setInstitutionId(rs.getInt("institution_id"));
			award.setName(rs.getString("name"));
			award.setGrade(rs.getInt("vid_grade"));
			award.setGrantBody(rs.getString("granting_body"));
			award.setType(rs.getInt("vid_type"));
			award.setYear(rs.getInt("year"));
			award.setAuthor(rs.getString("au_name"));
			return award;
		}};
	
	@Override
	public List<InstitutionAward> getAwardByKeyword(int offset, int size, 
			String keyword) {
		String sql="SELECT d.* from (SELECT a.*,group_concat(c.name) as au_name from institution_award a "+
			"left join (institution_award_author_ref b join institution_author c on b.author_id=c.id) on a.id=b.award_id "+
			"GROUP BY a.id having a.name like :keyword or au_name like :keyword"+
			" ) d limit :offset,:size" ;
//		System.out.println(getListSql(true, condition)+"=====");
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("keyword", "%"+CommonUtils.killNull(keyword)+"%");
		param.put("offset",offset);
		param.put("size", size);
		return getNamedParameterJdbcTemplate().query(sql, param, rowMapper);
	}

	@Override
	public List<InstitutionAward> getAwardByInit(int offset, int size,
			String userName) {
		String sql="SELECT d.* from (SELECT a.*,group_concat(c.name) as au_name from institution_award a "+
				"left join (institution_award_author_ref b join institution_author c on b.author_id=c.id) on a.id=b.award_id "+
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
	public void insertAward(final String[] awardId,final int userId) {
//		String sql="insert into institution_award_user(`user_id`,`award_id`) values(?,?)";
		String sql="insert into institution_award_user(`user_id`,`award_id`) select ?,? from dual where not exists" +
				"(select * from institution_award_user where award_id=? and user_id=?)";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return awardId.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i,userId);
						pst.setInt(++i,Integer.parseInt(awardId[index]));
						pst.setInt(++i,Integer.parseInt(awardId[index]));
						pst.setInt(++i,userId);
					}
				});
	}
	@Override
	public List<Integer> getExistAwardIds(int uid) {
		String sql="select award_id from institution_award_user where user_id=:uid";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("uid", uid);
		
		List<Integer> ids= getNamedParameterJdbcTemplate().query(sql, paramMap,new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("award_id");
			}
		});
		return ids;
	}

}
