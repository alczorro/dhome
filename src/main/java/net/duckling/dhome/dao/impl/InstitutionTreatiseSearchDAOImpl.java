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

import net.duckling.dhome.dao.IInstitutionTreatiseSearchDAO;
import net.duckling.dhome.domain.institution.InstitutionTreatise;
import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.common.util.CommonUtils;

@Repository
public class InstitutionTreatiseSearchDAOImpl extends BaseDao implements
		IInstitutionTreatiseSearchDAO {


private static final RowMapper<InstitutionTreatise> rowMapper=new RowMapper<InstitutionTreatise>(){
		
		@Override
		public InstitutionTreatise mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionTreatise treatise=new InstitutionTreatise();
			treatise.setId(rs.getInt("id"));
			treatise.setInstitutionId(rs.getInt("institution_id"));
			treatise.setName(rs.getString("name"));
			treatise.setLanguage(rs.getString("language"));
			treatise.setCompanyOrder(rs.getInt("company_order"));
			treatise.setPublisher(rs.getInt("vid_publisher"));
			treatise.setYear(rs.getInt("year"));
			treatise.setAuthor(rs.getString("au_name"));
			return treatise;
		}};
	
	@Override
	public List<InstitutionTreatise> getTreatiseByKeyword(int offset, int size, 
			String keyword) {
		String sql="SELECT d.* from (SELECT a.*,group_concat(c.name) as au_name from institution_treatise a "+
			"left join (institution_treatise_author_ref b join institution_author c on b.author_id=c.id) on a.id=b.treatise_id "+
			"GROUP BY a.id having a.name like :keyword or au_name like :keyword"+
			") d limit :offset,:size" ;
//		System.out.println(getListSql(true, condition)+"=====");
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("keyword", "%"+CommonUtils.killNull(keyword)+"%");
		param.put("offset",offset);
		param.put("size", size);
		return getNamedParameterJdbcTemplate().query(sql, param, rowMapper);
	}

	@Override
	public List<InstitutionTreatise> getTreatiseByInit(int offset, int size,
			String userName) {
		String sql="SELECT d.* from (SELECT a.*,group_concat(c.name) as au_name from institution_treatise a "+
				"left join (institution_treatise_author_ref b join institution_author c on b.author_id=c.id) on a.id=b.treatise_id "+
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
	public void insertTreatise(final String[] treatiseId,final int userId) {
//		String sql="insert into institution_treatise_user(`user_id`,`treatise_id`) values(?,?)";
		String sql="insert into institution_treatise_user(`user_id`,`treatise_id`) select ?,? from dual where not exists" +
				"(select * from institution_treatise_user where treatise_id=? and user_id=?)";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return treatiseId.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i,userId);
						pst.setInt(++i,Integer.parseInt(treatiseId[index]));
						pst.setInt(++i,Integer.parseInt(treatiseId[index]));
						pst.setInt(++i,userId);
					}
				});
	}
	@Override
	public List<Integer> getExistTreatiseIds(int uid) {
		String sql="select treatise_id from institution_treatise_user where user_id=:uid";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("uid", uid);
		
		List<Integer> ids= getNamedParameterJdbcTemplate().query(sql, paramMap,new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("treatise_id");
			}
		});
		return ids;
	}

}
