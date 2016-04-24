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
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.dao.IPaperDAO;
import net.duckling.dhome.domain.people.Paper;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
/**
 * Paper实体的DAO类
 * @author Yangxp
 * @date 2012-08-01
 */
@Repository
public class PaperDAO extends BaseDao implements IPaperDAO {

	private static final Logger LOG = Logger.getLogger(PaperDAO.class);

	/*** 创建Paper记录，该SQL语句在插入前会判断(uid,dsn_paper_id)是否存在，若存在则不进行插入 **/
	private static final String SQL_CREATE = "insert into paper(uid,title,authors,source,volumeIssue,publishedTime,"
			+ "timeCited,summary,language,keywords,localFulltextURL,paperURL,clbId,"
			+ "sequence, dsnPaperId, pages,year) select :uid,:title,:authors,:source,:volumeIssue,:publishedTime,:timeCited,:summary,:language"
			+ ",:keywords,:localFulltextURL,:paperURL,:clbId,:sequence,:dsnPaperId,:pages, :year from dual where not exists (select * from "
			+ " paper where paper.dsnPaperId!=0 and paper.dsnPaperId=:dsnPaperId and paper.uid=:uid)";
	private static final String SQL_GET_MY_PAPER = "select * from paper where uid=? order by sequence desc";
	private static final String SQL_GET_MY_EN_PAPER = "select * from paper where uid=? and length(title)=char_length(title) order by sequence desc";
	private static final String SQL_QUERY_PAPER = "select * from paper where id=?";
	private static final String SQL_DELETE = "delete from paper where id=?";

	private static RowMapper<Paper> rowMapper = new RowMapper<Paper>() {
		@Override
		public Paper mapRow(ResultSet rs, int index) throws SQLException {
			Paper paper = new Paper();
			paper.setId(rs.getInt("id"));
			paper.setUid(rs.getInt("uid"));
			paper.setTitle(rs.getString("title"));
			paper.setAuthors(rs.getString("authors"));
			paper.setSource(rs.getString("source"));
			paper.setVolumeIssue(rs.getString("volumeIssue"));
			paper.setPublishedTime(rs.getString("publishedTime"));
			paper.setTimeCited(rs.getInt("timeCited"));
			paper.setSummary(rs.getString("summary"));
			paper.setLanguage(rs.getString("language"));
			paper.setKeywords(rs.getString("keywords"));
			paper.setLocalFulltextURL(rs.getString("localFulltextURL"));
			paper.setPaperURL(rs.getString("paperURL"));
			paper.setClbId(rs.getInt("clbId"));
			paper.setSequence(rs.getInt("sequence"));
			paper.setDsnPaperId(rs.getLong("dsnPaperId"));
			paper.setPages(rs.getString("pages"));
			paper.setYear(rs.getInt("year"));
			return paper;
		}
	};

	@Override
	public int create(Paper paper) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int sequence = getMaxSequence(paper.getUid()) + 1;
		Map<String, Object>[] params = buildParameters(Arrays.asList(new Paper[] { paper }), sequence);
		SqlParameterSource sps = new MapSqlParameterSource(params[0]);
		getNamedParameterJdbcTemplate().update(SQL_CREATE, sps, keyHolder);
		Number key = keyHolder.getKey();
		return (key != null) ? key.intValue() : -1;
	}

	@Override
	public synchronized int[] batchCreate(final List<Paper> papers) {
		if (null == papers || papers.isEmpty()) {
			return new int[0];
		} else {
			int sequence = getMaxSequence(papers.get(0).getUid()) + 1;
			Map<String, Object>[] params = buildParameters(papers, sequence);
			return getNamedParameterJdbcTemplate().batchUpdate(SQL_CREATE, params);
		}
	}

	@Override
	public List<Paper> getPapers(int uid, int offset, int size) {
		String sql = SQL_GET_MY_PAPER + getLimit(offset, size);
		return this.getJdbcTemplate().query(sql, new Object[] { uid }, rowMapper);
	}

	@Override
	public List<Paper> getEnPapers(int uid, int offset, int size) {
		String sql = SQL_GET_MY_EN_PAPER + getLimit(offset, size);
		return this.getJdbcTemplate().query(sql, new Object[] { uid }, rowMapper);
	}

	@Override
	public boolean deletePaper(int paperId) {
		int result = this.getJdbcTemplate().update(SQL_DELETE, new Object[] { paperId });
		return result > 0;
	}
	
	@Override
	public int batchUpdateSequenceByID(final List<Paper> sortedPaper) {
		String sql = "update paper set sequence=? where id=?";
		this.getJdbcTemplate().batchUpdate(sql,new PaperBatchStatementSetter(sortedPaper));
		return 1;
	}
	/**
	 * 批量更新论文顺序
	 * @author Yangxp
	 *
	 */
	public static class PaperBatchStatementSetter implements BatchPreparedStatementSetter {
		private List<Paper> sortedPaper;
		/**
		 * 构造函数
		 * @param sortedPaper
		 */
		public PaperBatchStatementSetter(List<Paper> sortedPaper){
			this.sortedPaper=sortedPaper;
		}
		@Override
		public void setValues(PreparedStatement ps, int i) throws SQLException {
			Paper paper = sortedPaper.get(i);
			int j = 0;
			ps.setInt(++j, paper.getSequence());
			ps.setInt(++j, paper.getId());
		}

		@Override
		public int getBatchSize() {
			return (null == sortedPaper || sortedPaper.isEmpty()) ? 0 : sortedPaper.size();
		}

	}

	@Override
	public Paper getPaper(int paperid) {
		List<Paper> list = this.getJdbcTemplate().query(SQL_QUERY_PAPER, new Object[] { paperid }, rowMapper);
		if (null == list || list.isEmpty()) {
			return null;
		} else if (list.size() > 1) {
			LOG.error("there are more than one object while query for paperid=" + paperid);
		}
		return list.get(0);
	}

	@Override
	public boolean updateById(Paper paper) {
		String sql = "update paper set uid=:uid, title=:title, authors=:authors, source=:source, volumeIssue=:volumeIssue"
				+ ", publishedTime=:publishedTime, paperURL=:paperURL, summary=:summary, localFulltextURL=:localFulltextURL, clbId=:clbId " +
				", pages=:pages, year=:year where id=:id";
		Map<String, Object>[] params = buildParameters(Arrays.asList(new Paper[] { paper }), 0);
		params[0].put("id", paper.getId());
		SqlParameterSource sps = new MapSqlParameterSource(params[0]);
		int result = getNamedParameterJdbcTemplate().update(sql, sps);
		return result > 0;
	}

	@Override
	public List<Long> getExistDsnPaperIds(int uid) {
		String sql = "select dsnPaperId from paper where uid=? and dsnPaperId > 0";
		return getJdbcTemplate().queryForList(sql, new Object[] { uid }, Long.class);
	}

	@Override
	public int getPaperCount(int uid) {
		String sql = "select count(*) from paper where uid=?";
		return this.getJdbcTemplate().queryForInt(sql, new Object[]{uid});
	}

	private Map<String, Object>[] buildParameters(List<Paper> papers, int sequence) {
		int len = (null != papers) ? papers.size() : 0;
		HashMap<String, Object>[] maps = new HashMap[len];
		if (null != papers && papers.size() > 0) {
			for (int i = 0; i < len; i++) {
				Paper paper = papers.get(i);
				maps[i] = new HashMap<String, Object>();
				maps[i].put("uid", paper.getUid());
				maps[i].put("title", paper.getTitle());
				maps[i].put("authors", paper.getAuthors());
				maps[i].put("source", paper.getSource());
				maps[i].put("volumeIssue", paper.getVolumeIssue());
				maps[i].put("publishedTime", paper.getPublishedTime());
				maps[i].put("timeCited", paper.getTimeCited());
				maps[i].put("summary", paper.getSummary());
				maps[i].put("language", paper.getLanguage());
				maps[i].put("keywords", paper.getKeywords());
				maps[i].put("localFulltextURL", paper.getLocalFulltextURL());
				maps[i].put("paperURL", paper.getPaperURL());
				maps[i].put("clbId", paper.getClbId());
				maps[i].put("sequence", sequence + i);
				maps[i].put("dsnPaperId", paper.getDsnPaperId());
				maps[i].put("pages", paper.getPages());
				
				maps[i].put("year", filterYear(paper.getPublishedTime()));
			}
		}
		return maps;
	}
	
	private int filterYear(String publishedTime){
		int year = getIntYear(publishedTime);
		int curYear = Calendar.getInstance().get(Calendar.YEAR);
		return (year > curYear) ? 0 : year; //输入的年份大于当前年份，则年份信息非法，置为未知年份(0)
	}

	private int getMaxSequence(int uid) {
		String sql = "select max(sequence) from paper where uid='" + uid + "'";
		return getJdbcTemplate().queryForInt(sql);
	}
	
	private String getLimit(int offset, int size){
		if(offset<0 || size<0){
			LOG.error("Invalid params while getLimit by offset="+offset+" and size="+size);
			return " limit 0,0";
		}else if(offset ==0 && size ==0){//查询全部
			return " ";
		}
		return " limit " + offset + ", "+size;
	}
	
	private int getIntYear(String year) {
        String temp = "0";
        Pattern pat = Pattern.compile("\\d{4}");
        Matcher match = pat.matcher(year);
        if (match.find()) {
            temp = match.group();
        }
        return Integer.valueOf(temp);
    }

}
