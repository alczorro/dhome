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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.dao.IInstitutionPaperDAO;
import net.duckling.dhome.domain.object.PaperStatistics;
import net.duckling.dhome.domain.object.PaperYear;
import net.duckling.dhome.domain.people.Paper;
import net.duckling.dhome.domain.people.SimpleUser;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
/**
 * 机构论文的数据库接口实现类
 * @author Yangxp
 * @since 2012-09-25
 */
@Repository
public class InstitutionPaperDAO extends BaseDao implements IInstitutionPaperDAO {

	private static final Logger LOG = Logger.getLogger(InstitutionPaperDAO.class);
	private static final String DSN_CONDITION = " and p.dsnPaperId>0 group by p.dsnPaperId ";
	private static final String NO_DSN_CONDITION = " and p.dsnPaperId=0 ";
	private static final String UNION = " union ";
	private static final String DSN_PAPER_STATISTICS = "select institution_id,b.year, count(distinct b.dsnPaperId) paper_count, sum(b.timeCited) timeCited " +
			"from paper b,institution_people a, simple_user u, menu_item m where b.uid=u.id and u.`status`='auditOK' and a.uid=u.id and " +
			"m.url like '%paper.dhome' and m.status=1 and m.uid=u.id and b.title !='' and b.title is not null and b.authors !='' " +
			"and b.authors is not null and b.dsnPaperId>0 group by a.institution_id, b.year";
	private static final String NODSN_PAPER_STATISTICS = "select institution_id,b.year, count(*) paper_count, sum(b.timeCited) timeCited " +
			"from paper b,institution_people a, simple_user u, menu_item m where b.uid=u.id and u.`status`='auditOK' and a.uid=u.id and " +
			"m.url like '%paper.dhome' and m.status=1 and m.uid=u.id and b.title !='' and b.title is not null and b.authors !='' " +
			"and b.authors is not null and b.dsnPaperId=0 group by a.institution_id, b.year";
	
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
			return paper;
		}
	};
	
	private static RowMapper<PaperYear> paperYearRowMapper = new RowMapper<PaperYear>() {
		@Override
		public PaperYear mapRow(ResultSet rs, int index) throws SQLException {
			PaperYear py = new PaperYear();
			py.setId(rs.getInt("id"));
			py.setPublishedTime(rs.getString("publishedTime"));
			py.setYear(rs.getInt("year"));
			return py;
		}
	};
	
	private static RowMapper<PaperStatistics> paperStatisticsRowMapper = new RowMapper<PaperStatistics>() {
		@Override
		public PaperStatistics mapRow(ResultSet rs, int index) throws SQLException {
			PaperStatistics ps = new PaperStatistics();
			ps.setInstitutionId(rs.getInt("institution_id"));
			ps.setCount(rs.getInt("paper_count"));
			ps.setTimeCited(rs.getInt("timeCited"));
			ps.setYear(rs.getInt("year"));
			return ps;
		}
	};
	
	/**
	 * 共享的部分SQL语句，包括表联接关系和通用过滤条件；涉及的表以及表别名分别为：<br/>
	 * paper p
	 * menu_item mi
	 * institution_people ip
	 * simple_user su
	 * @param insId 机构ID
	 * @return sql语句
	 */
	private String getSharedSqlPart(int insId){
		return "from paper p, menu_item mi, institution_people ip, simple_user su " + //涉及4张表
				"where p.uid = su.id and mi.uid=su.id and ip.uid=su.id " + //表的联接关系
				"and su.status='" + SimpleUser.STATUS_AUDIT_OK + "' " + //用户需审核通过
				"and mi.url like '%paper.dhome' and mi.status=1 " + //用户主页的“学术论文”需置为显示状态
				"and ip.institution_id=" + insId + " "+ //指定机构ID
				"and p.title !='' and p.title is not NULL " + //论文标题不能为空
				"and p.authors !='' and p.authors is not NULL "; //论文作者不能为空
	}
	
	@Override
	public List<Paper> getPaperSortByCiteTime(int insId, int offset, int size) {
		String baseSql = "select p.* " + getSharedSqlPart(insId);
		String dsnSql = baseSql + DSN_CONDITION; //dsn去重
		String noDsnSql = baseSql + NO_DSN_CONDITION; //非dsn不去重
		String sql =  dsnSql +UNION + noDsnSql + " order by timeCited desc ";
		sql += getLimit(offset, size);
		return this.getJdbcTemplate().query(sql, rowMapper);
	}

	@Override
	public List<Paper> getPaperSortByYear(int insId, String year, int offset,
			int size) {
		String baseSql = "select p.* " + getSharedSqlPart(insId);
		int yearNum = getIntYear(year);
		baseSql += getYearClause(yearNum);
		String dsnSql = baseSql + DSN_CONDITION; //dsn去重
		String noDsnSql = baseSql + NO_DSN_CONDITION; //非dsn不去重
		String orderby = getYearOrder(yearNum);
		String sql =  dsnSql +UNION + noDsnSql + orderby;
		String yearSql = "select * from ("+sql+") temp where year>0";
		String noYearSql = "select * from ("+sql+") temp where year=0";
		String finalSql = yearSql + UNION + noYearSql;
		finalSql += getLimit(offset, size);
		return this.getJdbcTemplate().query(finalSql, rowMapper);
	}
	
	@Override
	public List<Paper> getPaperSortByAuthor(int insId, int uid, int offset,
			int size) {
		String baseSql = "select p.*,su.zh_name " + getSharedSqlPart(insId);
		baseSql += (uid > 0)?" and p.uid="+uid : " ";
		String dsnSql = baseSql + DSN_CONDITION; //dsn去重
		String noDsnSql = baseSql + NO_DSN_CONDITION; //非dsn不去重
		String orderby = (uid > 0)?" order by timeCited desc ":" order by convert(authors using gbk), timeCited desc ";
		String sql =  dsnSql +UNION + noDsnSql + orderby;
		sql += getLimit(offset, size);
		return this.getJdbcTemplate().query(sql, rowMapper);
	}

	@Override
	public List<String> getYearsOfAllPaper(int insId) {
		String sql = "select distinct(year) " + getSharedSqlPart(insId);
		sql += " order by year desc";
		return this.getJdbcTemplate().queryForList(sql, String.class);
	}
	

	@Override
	public List<Integer> getPaperAuthorIds(int insId) {
		String sql = "select distinct(p.uid) " + getSharedSqlPart(insId);
		return this.getJdbcTemplate().queryForList(sql, Integer.class);
	}
	
	@Override
	public int getPaperAmount(int insId, String year) {
		int yearNum = getIntYear(year);
		String baseSql = getSharedSqlPart(insId)+ getYearClause(yearNum);
		String dsnSql ="select count(distinct p.dsnPaperId) "+ baseSql + " and p.dsnPaperId>0";//dsn去重
		String noDsnSql = "select count(*) "+ baseSql + NO_DSN_CONDITION;//非dsn不去重
		int result = 0;
		try{//分成两次执行是因为，union会将计数相同的项去除
			result += this.getJdbcTemplate().queryForInt(dsnSql);
			result += this.getJdbcTemplate().queryForInt(noDsnSql);
		}catch(DataAccessException e){
			LOG.error("数据访问出错!", e);
		}
		return result;
	}

	@Override
	public int getAllPaperCitedTimes(int insId, String year) {
		int yearNum = getIntYear(year);
		String baseSql = getSharedSqlPart(insId)+ getYearClause(yearNum);
		String dsnSql = "select sum(timeCited) from (select distinct p.dsnPaperId, p.timeCited"+ baseSql +" and p.dsnPaperId>0) as temp";
		String noDsnSql = "select sum(p.timeCited) "+baseSql + NO_DSN_CONDITION;
		int result = 0;
		try{
			result += this.getJdbcTemplate().queryForInt(dsnSql);
			result += this.getJdbcTemplate().queryForInt(noDsnSql);
		}catch(DataAccessException e){
			LOG.error("数据访问出错!", e);
		}
		return result;
	}

	@Override
	public List<Integer> getPaperCitedTimes(int insId) {
		String baseSql = "select p.uid, p.title, p.authors,p.timeCited " + getSharedSqlPart(insId);
		String dsnSql = baseSql + DSN_CONDITION;//dsn去重
		String noDsnSql = baseSql + NO_DSN_CONDITION;//非dsn不去重
		String sql = dsnSql + UNION + noDsnSql + " order by timeCited desc";
		sql  = "select timeCited from ("+sql+") as temp order by temp.timeCited+0 desc";
		return this.getJdbcTemplate().queryForList(sql, Integer.class);
	}
	
	@Override
	public boolean isAllYearInfoEmpty() {
		String sql = "select count(*) from paper where year>0";
		return this.getJdbcTemplate().queryForInt(sql) <= 0;
	}

	@Override
	public List<PaperYear> getAllPaperYear() {
		String sql = "select id,publishedTime,year from paper";
		return this.getJdbcTemplate().query(sql, paperYearRowMapper);
	}

	@Override
	public void updateIntYear(List<PaperYear> paperYears) {
		if(null != paperYears && !paperYears.isEmpty()){
			String sql = "update paper set year=:year where id=:id";
			Map<String, ?>[] params = getParamsMap(paperYears);
			this.getNamedParameterJdbcTemplate().batchUpdate(sql, params);
		}
	}

	private Map<String, ?>[] getParamsMap(List<PaperYear> paperYears) {
		Map<String, ?>[] result = new HashMap[paperYears.size()];
		int i=0;
		for(PaperYear paperYear: paperYears){
			Map<String, Object> year = new HashMap<String, Object>();
			year.put("id", paperYear.getId());
			year.put("year", paperYear.getYear());
			result[i++] = year;
		}
		return result;
	}

	@Override
	public List<PaperStatistics> getAllDSNPaperStatistics() {
		return this.getJdbcTemplate().query(DSN_PAPER_STATISTICS, paperStatisticsRowMapper);
	}
	
	@Override
	public List<PaperStatistics> getAllNotDSNPaperStatistics() {
		return this.getJdbcTemplate().query(NODSN_PAPER_STATISTICS, paperStatisticsRowMapper);
	}

	private String getYearClause(int year){
		return (year>=0)? (" and p.year="+year)+" ":" ";
	}
	
	private String getYearOrder(int year){
		String result = "";
		if(year >= 0){//具体年份和无法解析的年份
			result = " order by timeCited desc ";
		}else{//所有年份
			result = " order by year desc, timeCited desc ";
		}
		return result;
	}

	private String getLimit(int offset, int size){
		if(offset<0 || size<0){
			LOG.error("Invalid params while getLimit by offset="+offset+" and size="+size);
			return " limit 0,0";
		}
		return " limit " + offset + ", "+size;
	}
	
	private int getIntYear(String year){
		int result = 0;
		if(null == year || "".equals(year) || "-1".equals(year)){
			result = -1;
		}else{
			String temp = "0";
			Pattern pat = Pattern.compile("\\d{4}");
			Matcher match = pat.matcher(year);
			if(match.find()){
				temp = match.group();
			}
			result = Integer.valueOf(temp);
		}
		return (result <0)?-1:result;
	}

}
