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

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.common.repository.DAOUtils;
import net.duckling.dhome.dao.IInterestDAO;
import net.duckling.dhome.domain.object.InterestCount;
import net.duckling.dhome.domain.people.Dictionary;
import net.duckling.dhome.domain.people.Interest;
import net.duckling.dhome.domain.people.SimpleUser;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
/**
 * 研究兴趣的数据库接口实现类
 * @author Yangxp
 * @since 2012-09-28
 */
@Repository
public class InterestDAO extends BaseDao implements IInterestDAO {
	private static final String KEY_WORD="keyword";
	private static final String AUDIT_SQL=" and (`status` ='"+SimpleUser.STATUS_AUDIT_OK+"' or `status`='"+SimpleUser.STATUS_AUDIT_NEED+"' or `status` is null)";
	private static final String COMPLETE_SQL=" and `step`='"+SimpleUser.STEP_COMPLETE+"' ";

	
	/**
	 * 批量更新研究兴趣
	 * @author Yangxp
	 *
	 */
	private static class InterestBatchStatementSetter implements BatchPreparedStatementSetter {
		private List<String> interests;
		private int uid;
		/**
		 * 构造函数
		 * @param uid
		 * @param interests
		 */
		public InterestBatchStatementSetter(int uid, List<String> interests){
			this.interests = interests;
			this.uid = uid;
		}
		@Override
		public void setValues(PreparedStatement ps, int i) throws SQLException {
			String interest = interests.get(i);
			int j = 0;
			ps.setInt(++j, uid);
			ps.setString(++j, interest);
			ps.setInt(++j, uid);
			ps.setString(++j, interest);
		}

		@Override
		public int getBatchSize() {
			return (null == interests || interests.isEmpty()) ? 0 : interests.size();
		}

	}
	
	/**
	 * 批量更新研究兴趣
	 * @author Yangxp
	 *
	 */
	private static class InterestDicBatchStatementSetter implements BatchPreparedStatementSetter {
		private List<String> interests;
		/**
		 * 构造函数
		 * @param interests
		 */
		public InterestDicBatchStatementSetter(List<String> interests){
			this.interests = interests;
		}
		@Override
		public void setValues(PreparedStatement ps, int i) throws SQLException {
			String interest = interests.get(i);
			int j = 0;
			ps.setString(++j, interest);
			ps.setString(++j, interest);
		}

		@Override
		public int getBatchSize() {
			return (null == interests || interests.isEmpty()) ? 0 : interests.size();
		}

	}
	
	private static RowMapper<Interest> rowMapper = new RowMapper<Interest>() {
		@Override
		public Interest mapRow(ResultSet rs, int index) throws SQLException {
			Interest inte = new Interest();
			inte.setKeyword(rs.getString(KEY_WORD));
			inte.setUid(rs.getInt("uid"));
			inte.setId(rs.getInt("id"));
			return inte;
		}
	};
	
	private static RowMapper<Dictionary> dicRowMapper = new RowMapper<Dictionary>() {
		@Override
		public Dictionary mapRow(ResultSet rs, int index) throws SQLException {
			Dictionary intedic = new Dictionary();
			intedic.setKeyWord(rs.getString(KEY_WORD));
			intedic.setStatus(rs.getInt("status"));
			intedic.setId(rs.getInt("id"));
			return intedic;
		}
	};
	
	@Override
	public int[] batchCreate(final int uid, final List<String> interests) {
		String sql = "insert into interest(uid,keyword) select ?,? from dual where not exists(select" +
				" * from interest where interest.uid=? and interest.keyword=?)";
		String dicSql = "insert into interest_dic(keyword,status) select ?,0 from dual where not exists(select" +
				" * from interest_dic where interest_dic.keyword=?)";
		this.getJdbcTemplate().batchUpdate(dicSql, new InterestDicBatchStatementSetter(interests));
		return this.getJdbcTemplate().batchUpdate(sql, new InterestBatchStatementSetter(uid, interests));
	}

	@Override
	public boolean deleteByUid(int uid) {
		String sql = "delete from interest where uid="+uid;
		int result = this.getJdbcTemplate().update(sql);
		return result >0;
	}

	@Override
	public List<Interest> getInterest(int uid) {
		String sql  = "select * from interest where uid="+uid;
		return this.getJdbcTemplate().query(sql, rowMapper);
	}

	@Override
	public List<Dictionary> searchInterest(String keyword) {
		String sql = "select * from interest_dic where keyword like :keyword group by keyword";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(KEY_WORD, "%"+keyword+"%");
		return this.getNamedParameterJdbcTemplate().query(sql, params, dicRowMapper);
	}
	@Override
	public List<InterestCount> getInterestAll() {
		String sql=" select ii.`keyword` as key_word,count(ii.`keyword`) as count "+
				   " from simple_user ui,interest ii  "+
				   " where ii.uid=ui.id and (ui.`status` ='auditOK' or ui.`status`='auditNeed' or ui.`status` is null) "+
				   COMPLETE_SQL+
				   " group by ii.`keyword` ";
		
		return getJdbcTemplate().query(sql, new DAOUtils<InterestCount>(InterestCount.class).getRowMapper(null));
	}
	@Override
	public int getCount(String keyword){
		String sql = " select count(*) from interest i,simple_user u "+
				 " where i.uid=u.id  and keyword=:keyword"+
				 AUDIT_SQL+
				 COMPLETE_SQL;
		Map<String,String> map=new HashMap<String,String>();
		map.put(KEY_WORD, keyword);
		return getNamedParameterJdbcTemplate().queryForInt(sql, map);
	}
}
