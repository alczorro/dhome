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

import net.duckling.dhome.dao.IInstitutionTopicSearchDAO;
import net.duckling.dhome.domain.institution.InstitutionTopic;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.common.util.CommonUtils;

@Repository
public class InstitutionTopicSearchDAOImpl extends BaseDao implements
		IInstitutionTopicSearchDAO {


private static final RowMapper<InstitutionTopic> rowMapper=new RowMapper<InstitutionTopic>(){
		
		@Override
		public InstitutionTopic mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionTopic topic=new InstitutionTopic();
			topic.setId(rs.getInt("id"));
			topic.setStart_year(Integer.toString(rs.getInt("start_year")));
			topic.setStart_month(Integer.toString(rs.getInt("start_month")));
			topic.setEnd_year(Integer.toString(rs.getInt("end_year")));
			topic.setEnd_month(Integer.toString(rs.getInt("end_month")));
			topic.setName(rs.getString("name"));
			topic.setType(rs.getInt("type"));
			topic.setRole(rs.getString("role"));
			topic.setProject_cost(rs.getInt("project_cost"));
			topic.setPersonal_cost(rs.getInt("personal_cost"));
			topic.setInstitution_id(rs.getInt("institution_id"));
			topic.setFunds_from(rs.getInt("funds_from"));
			topic.setTopic_no(rs.getString("topic_no"));
			topic.setAuthor(rs.getString("au_name"));
			return topic;
		}};
	
	@Override
	public List<InstitutionTopic> getTopicByKeyword(int offset, int size, 
			String keyword) {
		String sql="SELECT d.* from (SELECT a.*,group_concat(c.name) as au_name from institution_topic a "+
			"left join (institution_topic_author_ref b join institution_author c on b.author_id=c.id) on a.id=b.topic_id "+
			"GROUP BY a.id having a.name like :keyword or au_name like :keyword) d "+
			"limit :offset,:size" ;
//		System.out.println(getListSql(true, condition)+"=====");
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("keyword", "%"+CommonUtils.killNull(keyword)+"%");
		param.put("offset",offset);
		param.put("size", size);
		return getNamedParameterJdbcTemplate().query(sql, param, rowMapper);
	}

	@Override
	public List<InstitutionTopic> getTopicByInit(int offset, int size,
			String userName) {
		String sql="SELECT d.* from (SELECT a.*,group_concat(c.name) as au_name from institution_topic a "+
				"left join (institution_topic_author_ref b join institution_author c on b.author_id=c.id) on a.id=b.topic_id "+
				"GROUP BY a.id having au_name like :keyword) d "+
				"limit :offset,:size" ;
//			System.out.println(getListSql(true, condition)+"=====");
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("keyword", "%"+CommonUtils.killNull(userName)+"%");
			param.put("offset",offset);
			param.put("size", size);
			return getNamedParameterJdbcTemplate().query(sql, param, rowMapper);
	}

	@Override
	public void insertTopic(final String[] topicId,final int userId) {
//		String sql="insert into institution_topic_user(`user_id`,`topic_id`) values(?,?)";
		String sql="insert into institution_topic_user(`user_id`,`topic_id`) select ?,? from dual where not exists" +
				"(select * from institution_topic_user where topic_id=? and user_id=?)";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return topicId.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i,userId);
						pst.setInt(++i,Integer.parseInt(topicId[index]));
						pst.setInt(++i,Integer.parseInt(topicId[index]));
						pst.setInt(++i,userId);
					}
				});
	}
	@Override
	public List<Integer> getExistTopicIds(int uid) {
		String sql="select topic_id from institution_topic_user where user_id=:uid";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("uid", uid);
		
		List<Integer> ids= getNamedParameterJdbcTemplate().query(sql, paramMap,new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("topic_id");
			}
		});
		return ids;
	}

}
