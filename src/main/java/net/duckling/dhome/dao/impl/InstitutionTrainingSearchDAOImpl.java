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

import net.duckling.dhome.dao.IInstitutionTrainingSearchDAO;
import net.duckling.dhome.domain.institution.InstitutionTraining;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.common.util.CommonUtils;

@Repository
public class InstitutionTrainingSearchDAOImpl extends BaseDao implements
		IInstitutionTrainingSearchDAO {


private static final RowMapper<InstitutionTraining> rowMapper=new RowMapper<InstitutionTraining>(){
		
		@Override
		public InstitutionTraining mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionTraining training=new InstitutionTraining();
			training.setId(rs.getInt("id"));
			training.setDegree(rs.getInt("degree"));
			training.setMajor(rs.getString("major"));
			training.setGraduationDate(rs.getDate("graduation_date"));
			training.setEnrollmentDate(rs.getDate("enrollment_date"));
			training.setInstitutionId(rs.getInt("institution_id"));
			training.setStudentName(rs.getString("student_name"));
			training.setNationality(rs.getString("nationality"));
			training.setRemark(rs.getString("remark"));
			training.setUmtId(rs.getString("umt_id"));
			return training;
		}};
	
	@Override
	public List<InstitutionTraining> getTrainingByKeyword(int offset, int size, 
			String keyword) {
		String sql="SELECT * from institution_training a left join institution_member_from_vmt b on a.umt_id=b.umt_id "+
			"where a.student_name like :keyword or b.true_name like :keyword limit :offset,:size";
//		System.out.println(getListSql(true, condition)+"=====");
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("keyword", "%"+CommonUtils.killNull(keyword)+"%");
		param.put("offset",offset);
		param.put("size", size);
		return getNamedParameterJdbcTemplate().query(sql, param, rowMapper);
	}

	@Override
	public List<InstitutionTraining> getTrainingByInit(int offset, int size,
			String userName) {
		String sql="SELECT * from institution_training a left join institution_member_from_vmt b on a.umt_id=b.umt_id "+
				"where a.student_name like :keyword or b.true_name like :keyword limit :offset,:size";
//			System.out.println(getListSql(true, condition)+"=====");
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("keyword", "%"+CommonUtils.killNull(userName)+"%");
			param.put("offset",offset);
			param.put("size", size);
			return getNamedParameterJdbcTemplate().query(sql, param, rowMapper);
	}

	@Override
	public void insertTraining(final String[] trainingId,final int userId) {
//		String sql="insert into institution_training_user(`user_id`,`training_id`) values(?,?)";
		String sql="insert into institution_training_user(`user_id`,`training_id`) select ?,? from dual where not exists" +
				"(select * from institution_training_user where training_id=? and user_id=?)";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return trainingId.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i,userId);
						pst.setInt(++i,Integer.parseInt(trainingId[index]));
						pst.setInt(++i,Integer.parseInt(trainingId[index]));
						pst.setInt(++i,userId);
					}
				});
	}
	@Override
	public List<Integer> getExistTrainingIds(int uid) {
		String sql="select training_id from institution_training_user where user_id=:uid";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("uid", uid);
		
		List<Integer> ids= getNamedParameterJdbcTemplate().query(sql, paramMap,new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("training_id");
			}
		});
		return ids;
	}

}
