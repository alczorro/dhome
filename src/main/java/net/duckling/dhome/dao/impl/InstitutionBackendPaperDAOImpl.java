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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.duckling.common.util.CommonUtils;
import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.dao.IInstitutionBackendPaperDAO;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.SearchInstitutionPaperCondition;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class InstitutionBackendPaperDAOImpl extends BaseDao implements IInstitutionBackendPaperDAO{
	private RowMapper<InstitutionPaper> rowMapper= new RowMapper<InstitutionPaper>() {
		@Override
		public InstitutionPaper mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionPaper paper=new InstitutionPaper();
			paper.setId(rs.getInt("id"));
			paper.setInstitutionId(rs.getInt("institution_id"));
			paper.setTitle(rs.getString("title"));
			paper.setDoi(rs.getString("doi"));
			paper.setIssn(rs.getString("issn"));
			paper.setPublicationId(rs.getInt("publication_id"));
			paper.setPublicationYear(rs.getInt("publication_year"));
			paper.setPublicationMonth(rs.getInt("publication_month"));
//			paper.setStartPage(rs.getInt("start_page"));
//			paper.setEndPage(rs.getInt("end_page"));
			paper.setPublicationPage(rs.getString("publication_page"));
			paper.setVolumeNumber(rs.getString("volume_number"));
			paper.setSeries(rs.getString("series"));
			paper.setDisciplineOrientationId(rs.getInt("discipline_orientation_id"));
			paper.setKeywordDisplay(rs.getString("keyword"));
			paper.setSponsor(rs.getString("sponsor"));
			paper.setSummary(rs.getString("summary"));
			paper.setOriginalLink(rs.getString("original_link"));
			paper.setCitation(rs.getString("citation"));
			paper.setCitationQueryTime(rs.getString("citation_query_time"));
			paper.setOriginalFileName(rs.getString("original_file_name"));
			paper.setClbId(rs.getInt("original_clb_id"));
			paper.setAnnualAwardMarks(rs.getString("annual_award_marks"));
			paper.setPerformanceCalculationYear(rs.getInt("performance_calculation_year"));
			paper.setStatus(rs.getInt("status"));
			paper.setDepartId(rs.getInt("depart_id"));
			paper.setIfs(rs.getString("if"));
			paper.setAuthorAmount(rs.getString("author_total"));
			paper.setAwardStandard(rs.getString("award_standard"));
			paper.setPayTotal(rs.getString("pay_total"));
			paper.setRemark(rs.getString("remark"));
			paper.setCreator(rs.getInt("creator"));
			return paper;
		}
	};
	@Override
	public void update(InstitutionPaper paper) {
		StringBuffer sb=new StringBuffer();
		sb.append("update institution_paper set ");
		sb.append("`title`=:title,");
		sb.append("`doi`=:doi,");
		sb.append("`issn`=:issn,");
		sb.append("`publication_id`=:publication_id,");
		sb.append("`publication_year`=:publication_year,");
		sb.append("`publication_month`=:publication_month,");
		sb.append("`publication_page`=:publication_page,");
//		sb.append("`end_page`=:end_page,");
		sb.append("`volume_number`=:volume_number,");
		sb.append("`series`=:series,");
		sb.append("`discipline_orientation_id`=:discipline_orientation_id,");
		sb.append("`keyword`=:keyword,");
		sb.append("`sponsor`=:sponsor,");
		sb.append("`summary`=:summary,");
		sb.append("`original_link`=:original_link,");
		sb.append("`citation`=:citation,");
		sb.append("`citation_query_time`=:citation_query_time,");
		sb.append("`original_file_name`=:original_file_name,");
		sb.append("`original_clb_id`=:original_clb_id,");
		sb.append("`annual_award_marks`=:annual_award_marks,");
		sb.append("`performance_calculation_year`=:performance_calculation_year,");
		sb.append("`depart_id`=:departId,");
		sb.append("`author_total`=:authorAmount,");
		sb.append("`status`=:status,");
		sb.append("`institution_id`=:institutionId,");
		sb.append("`hash`=:hash ");
		sb.append("where id=:id");
		getNamedParameterJdbcTemplate().update(sb.toString(),fromPaper(paper));
	}
	private Map<String,Object> fromPaper(InstitutionPaper paper){
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("title",paper.getTitle());
		paramMap.put("doi",paper.getDoi());
		paramMap.put("issn",paper.getIssn());
		paramMap.put("publication_id",paper.getPublicationId());
		paramMap.put("publication_year",paper.getPublicationYear());
		paramMap.put("publication_month",paper.getPublicationMonth());
//		paramMap.put("start_page",paper.getStartPage());
//		paramMap.put("end_page",paper.getEndPage());
		paramMap.put("publication_page",paper.getPublicationPage());
		paramMap.put("volume_number",paper.getVolumeNumber());
		paramMap.put("series",paper.getSeries());
		paramMap.put("discipline_orientation_id",paper.getDisciplineOrientationId());
		paramMap.put("keyword",paper.getKeywordDisplay());
		paramMap.put("sponsor",paper.getSponsor());
		paramMap.put("summary",paper.getSummary());
		paramMap.put("original_link",paper.getOriginalLink());
		paramMap.put("citation",paper.getCitationInt());
		paramMap.put("citation_query_time",paper.getCitationQueryTime());
		paramMap.put("original_file_name",paper.getOriginalFileName());
		paramMap.put("original_clb_id",paper.getClbId());
		paramMap.put("annual_award_marks",paper.getAnnualAwardMarks());
		paramMap.put("performance_calculation_year",paper.getPerformanceCalculationYear());
		paramMap.put("departId",paper.getDepartId());
		paramMap.put("authorAmount",paper.getAuthorAmountInt());
		paramMap.put("status",paper.getStatus());
		paramMap.put("institutionId",paper.getInstitutionId());
		paramMap.put("hash",paper.getHash());
		paramMap.put("id", paper.getId());
		paramMap.put("creator", paper.getCreator());
		return paramMap;
	}
	@Override
	public int create(InstitutionPaper paper) {
		StringBuffer sb=new StringBuffer();
		sb.append("insert into institution_paper(");
		sb.append("`title`,");
		sb.append("`doi`,");
		sb.append("`issn`,");
		sb.append("`publication_id`,");
		sb.append("`publication_year`,");
		sb.append("`publication_month`,");
//		sb.append("`start_page`,");
		sb.append("`publication_page`,");
		sb.append("`volume_number`,");
		sb.append("`series`,");
		sb.append("`discipline_orientation_id`,");
		sb.append("`keyword`,");
		sb.append("`sponsor`,");
		sb.append("`summary`,");
		sb.append("`original_link`,");
		sb.append("`citation`,");
		sb.append("`citation_query_time`,");
		sb.append("`original_file_name`,");
		sb.append("`original_clb_id`,");
		sb.append("`annual_award_marks`,");
		sb.append("`performance_calculation_year`,");
		sb.append("`depart_id`,");
		sb.append("`author_total`,");
		sb.append("`status`,");
		sb.append("`creator`,");
		sb.append("`institution_id`,");
		sb.append("`hash`) ");
		
		sb.append("values(");
		
		sb.append(":title,");
		sb.append(":doi,");
		sb.append(":issn,");
		sb.append(":publication_id,");
		sb.append(":publication_year,");
		sb.append(":publication_month,");
//		sb.append(":start_page,");
		sb.append(":publication_page,");
		sb.append(":volume_number,");
		sb.append(":series,");
		sb.append(":discipline_orientation_id,");
		sb.append(":keyword,");
		sb.append(":sponsor,");
		sb.append(":summary,");
		sb.append(":original_link,");
		sb.append(":citation,");
		sb.append(":citation_query_time,");
		sb.append(":original_file_name,");
		sb.append(":original_clb_id,");
		sb.append(":annual_award_marks,");
		sb.append(":performance_calculation_year,");
		sb.append(":departId,");
		sb.append(":authorAmount,");
		sb.append(":status,");
		sb.append(":creator,");
		sb.append(":institutionId,");
		sb.append(":hash) ");
		KeyHolder keyHolder = new GeneratedKeyHolder();
        getNamedParameterJdbcTemplate().update(sb.toString(), new MapSqlParameterSource(fromPaper(paper)), keyHolder);
        return keyHolder.getKey().intValue();
	}
	@Override
	public void addRef(int institutionId, int paperId,String authorName) {
		String sql="insert into institution_paper_ref(`institution_id`,`paper_id`,`first_author_name`) values(:insId,:paperId,:name)";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("insId", institutionId);
		map.put("paperId", paperId);
		map.put("name",authorName);
		getNamedParameterJdbcTemplate().update(sql, map);
	}
	@Override
	public void updateRef(int institutionId, int paperId, String firstAuthorName) {
		String sql="update institution_paper_ref set first_author_name=:name where `institution_id`=:insId and `paper_id`=:paperId";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("insId", institutionId);
		map.put("paperId", paperId);
		map.put("name",firstAuthorName);
		getNamedParameterJdbcTemplate().update(sql, map);		
	}
	public boolean isExist(int institutionId,int paperId){
		String sql="select count(*) from institution_paper_ref where institution_id=:insId and paper_id=:paperId";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("insId",institutionId);
		map.put("paperId", paperId);
		return getNamedParameterJdbcTemplate().queryForInt(sql, map)>0;
	}
	
	
	@Override
	public int getPaperCount(int institutionId,SearchInstitutionPaperCondition condition) {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", institutionId);
		paramMap.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
		paramMap.put("departId", condition.getDepartmentId());
//		paramMap.put("pubYear", condition.getPublicationYear());
		return getNamedParameterJdbcTemplate().queryForInt(getListSql(true, condition),paramMap);
	}
	
	@Override
	public List<InstitutionPaper> getPapers(int institutionId,SearchInstitutionPaperCondition condition, int offset,
			int size) {
		
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", institutionId);
		paramMap.put("offset", offset);
		paramMap.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
		paramMap.put("size", size);
		paramMap.put("departId", condition.getDepartmentId());
		return getNamedParameterJdbcTemplate().query(getListSql(false,condition), paramMap, rowMapper);
	}
	
	@Override
	public int getPaperCountByUser(int userId,SearchInstitutionPaperCondition condition) {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId", userId);
		paramMap.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
//		paramMap.put("pubYear", condition.getPublicationYear());
		return getNamedParameterJdbcTemplate().queryForInt(getListSqlByUser(true, condition),paramMap);
	}
	
	@Override
	public List<InstitutionPaper> getPapersByUser(int userId,SearchInstitutionPaperCondition condition, int offset, int size) {
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId", userId);
		paramMap.put("offset", offset);
		paramMap.put("keyword", "%"+CommonUtils.killNull(condition.getKeyword())+"%");
		paramMap.put("size", size);
		paramMap.put("pubYear", condition.getPublicationYear());
		return getNamedParameterJdbcTemplate().query(getListSqlByUser(false,condition), paramMap, rowMapper);
	}
	
	
	@Override
	public void delete(final int institutionId, final int[] paperId) {
		String sql="delete from institution_paper_ref where paper_id=? and institution_id=?";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return paperId.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i, paperId[index]);
						pst.setInt(++i, institutionId);
					}
				});
	}
	@Override
	public InstitutionPaper getPaperById(int paperId) {
		String sql="select * from institution_paper where id=:id";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("id", paperId);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper));
	}
	
	@Override
	public int isHashExitsReturnId(String hash) {
		String sql="select id from institution_paper where hash=:hash";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("hash", hash);
		
		List<Integer> ids= getNamedParameterJdbcTemplate().query(sql, paramMap,new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("id");
			}
		});
		if(CommonUtils.isNull(ids)){
			return 0;
		}
		return CommonUtils.first(ids);
	}
	@Override
	public Map<Integer, Integer> getPublicationYearsMap(int institutionId) {
		String sql="select p.publication_year y,count(*) c from institution_paper_ref r "+
					"left join institution_paper p "+
					"on r.paper_id=p.id "+
					"where r.institution_id=:insId "+
					"group by p.publication_year "+
					"order by y ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", institutionId);
		final Map<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
		getNamedParameterJdbcTemplate().query(sql, paramMap, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				result.put(rs.getInt("y"), rs.getInt("c"));
				return null;
			}
		});
		return result;
	}
	
	private String getListSql(boolean isCount,SearchInstitutionPaperCondition condition){
		StringBuilder sb=new StringBuilder();
//		sb.append(" select ").append(isCount?"count(*)":"p.*").append(" from institution_paper_ref r,institution_paper p,institution_publication u ");
//		sb.append(" where p.id=r.paper_id and p.publication_id=u.id");
		sb.append(" select ").append(isCount?"count(*)":"p.*").append(" from institution_paper p left join institution_paper_ref r on p.id=r.paper_id left join institution_publication u on p.publication_id=u.id");
//		sb.append(" where p.id=r.paper_id and p.publication_id=u.id");
		sb.append(" where r.institution_id=:insId");
		sb.append(getConditionSql(isCount, condition));
		return sb.toString();
	}
	
	private String getListSqlByUser(boolean isCount,SearchInstitutionPaperCondition condition){
		StringBuilder sb=new StringBuilder();
		sb.append(" select ").append(isCount?"count(*)":"p.*").append(" from institution_paper_user pu,institution_paper p,institution_publication u,institution_paper_ref r ");
		sb.append(" where p.id=pu.paper_id and p.publication_id=u.id and p.id=r.paper_id");
		sb.append(" and pu.user_id=:userId");
		sb.append(getConditionSql(isCount, condition));
		return sb.toString();
	}
	
	private String getOrderTypeByType(int orderType){
		switch(orderType){
			case SearchInstitutionPaperCondition.ORDER_TYPE_ASC:{
				return " asc ";
			}
			case SearchInstitutionPaperCondition.ORDER_TYPE_DESC:{
				return " desc ";
			}
			default:{
				return " desc ";
			}
		}
	}
	private String getOrderColumnByStatus(int order){
		switch(order){
			case SearchInstitutionPaperCondition.ORDER_IF:{
				return "if2013_2014";
			}
			case SearchInstitutionPaperCondition.ORDER_CITATION:{
				return "citation";
			}
			case SearchInstitutionPaperCondition.ORDER_FIRST_AUTHOR:{
				return "first_author_name";
			}
			case SearchInstitutionPaperCondition.ORDER_ID:{
				return "id";
			}
			case SearchInstitutionPaperCondition.ORDER_PUBLICATION_YEAR:{
				return "publication_year";
			}
			case SearchInstitutionPaperCondition.ORDER_ENG:{
				return "convert(title using gbk) collate gbk_chinese_ci";
			}
			default:{
				return "publication_year";
			}
		}
	}
	@Override
	public List<InstitutionPaper> getPapersByUID(int uid) {
		String sql="select * from institution_paper p "+
				"left join institution_paper_user u "+
				"on u.paper_id=p.id "+
				"where u.user_id=:uid order by p.publication_year desc";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("uid", uid);
		return getNamedParameterJdbcTemplate().query(sql, paramMap,rowMapper);
	}
	@Override
	public List<InstitutionPaper> getEnPapersByUID(int uid) {
		String sql="select * from institution_paper_user u "+
				"left join institution_paper p "+
				"on u.paper_id=p.id "+
				"where u.user_id=:uid and length(p.title)=char_length(p.title)";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("uid", uid);
		return getNamedParameterJdbcTemplate().query(sql, paramMap,rowMapper);
	}
	
	@Override
	public void createUserRef(int userId, int paperId) {
		String sql="insert into institution_paper_user(`user_id`,`paper_id`) select :userId,:paperId from dual where "+
				"not exists(select * from institution_paper_user where user_id=:userId and paper_id=:paperId)";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId",userId);
		paramMap.put("paperId",paperId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}

	@Override
	public void deleteUserRef(int uid,int paperId) {
		String sql="delete from institution_paper_user where user_id=:uid and paper_id=:paperId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("paperId", paperId);
		getNamedParameterJdbcTemplate().update(sql, params);
	}

	@Override
	public Map<Integer, Integer> getPublicationYearsMapByUser(int userId) {
		String sql="select r.publication_year,count(*) c from institution_paper r,institution_paper_user u "+
				"where u.user_id=:userId "+
				"and u.paper_id=r.id "+
				"group by r.publication_year ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId", userId);
		final Map<Integer,Integer> result=new LinkedHashMap<Integer,Integer>();
		getNamedParameterJdbcTemplate().query(sql, paramMap, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				result.put(rs.getInt("publication_year"), rs.getInt("c"));
				return null;
			}
		});
		return result;
	}
	@Override
	public void deletePaperUser(final int[] id) {
		String sql="delete from institution_paper_user where paper_id=?";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return id.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i,id[index]);
					}
				});
	}
	@Override
	public void insertPaperUser(int paperId, int userId) {
		String sql="insert into institution_paper_user(`user_id`,`paper_id`) select :userId,:paperId from dual where not " +
				"exists(select * from institution_paper_user where user_id=:userId and paper_id=:paperId)";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId",userId);
		paramMap.put("paperId",paperId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}

	
	/**
	 * 查询论文在各部门统计数据
	 */
	@Override
	public Map<String, Map<String, Integer>> getPaperStatisticsForDept() {
		final Map<String, Map<String, Integer>> result = new LinkedHashMap<String, Map<String,Integer>>();
		String sql ="select b.depart_name_short,count(*) c,sum(IF(a.citation>0,a.citation,0)) s from institution_paper a,institution_depart b,institution_paper_ref r where a.depart_id=b.id and a.id=r.paper_id and a.status=1 group by b.depart_name_short";
		getNamedParameterJdbcTemplate().query(sql, new HashMap<String,Object>() , new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String key = rs.getString("depart_name_short");
				if(key == null || key.equals("null") || key.equals(""))
					key = "其它";
				
				Map<String, Integer> map = new HashMap<String, Integer>();
				map.put("count", rs.getInt("c"));
				map.put("cite", rs.getInt("s"));
				
				result.put(key,map);
				return null;
			}
		});
		return result;
	}
	
	@Override
	public Map<String, Map<String, Integer>> getPaperStatisticsForYear() {
		final Map<String, Map<String, Integer>> result = new LinkedHashMap<String, Map<String,Integer>>();
		String sql ="select publication_year,count(*) c,sum(IF(citation>0,citation,0)) s from institution_paper p,institution_paper_ref r WHERE p.id=r.paper_id and publication_year!=0 and status=1 group by publication_year order by publication_year ASC";
		getNamedParameterJdbcTemplate().query(sql, new HashMap<String,Object>() , new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String key = rs.getString("publication_year");
				if(key == null || key.equals("null") || key.equals(""))
					key = "其它";
				
				Map<String, Integer> map = new HashMap<String, Integer>();
				map.put("count", rs.getInt("c"));
				map.put("cite", rs.getInt("s"));
				
				result.put(key,map);
				return null;
			}
		});
		return result;
	}

	@Override
	public void updateStatus(int paperId,int status) {
		String sql="update institution_paper set status=:status where id=:paperId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("status",status);
		paramMap.put("paperId",paperId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}
	
	@Override
	public int getPapersCite(int insId) {
		String sql="select sum(IF(citation>0,citation,0)) from institution_paper where status=1";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("insId",insId);
		return getNamedParameterJdbcTemplate().queryForInt(sql, map);
	}
	@Override
	public List<InstitutionPaper> getAllPapers() {
		String sql = "select * from institution_paper where 1=1";
		return getJdbcTemplate().query(sql, rowMapper);
	}
	@Override
	public int getPaperCountByPub(int institutionId,
			SearchInstitutionPaperCondition condition) {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", institutionId);
		paramMap.put("keyword3", "%"+CommonUtils.killNull(condition.getKeyword3())+"%");
		paramMap.put("ifMin", condition.getIf_min());
		paramMap.put("ifMax", condition.getIf_max());
		paramMap.put("departId", condition.getDepartmentId());
//		paramMap.put("pubYear", condition.getPublicationYear());
		return getNamedParameterJdbcTemplate().queryForInt(getListSqlByPub(true, condition),paramMap);
	}
	@Override
	public List<InstitutionPaper> getPapersByPub(int institutionId,
			SearchInstitutionPaperCondition condition, int offset, int size) {
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", institutionId);
		paramMap.put("offset", offset);
		paramMap.put("keyword3", "%"+CommonUtils.killNull(condition.getKeyword3())+"%");
		paramMap.put("size", size);
		paramMap.put("departId", condition.getDepartmentId());
//		paramMap.put("ifMin", condition.getIf_min());
//		paramMap.put("ifMax", condition.getIf_max());
//		paramMap.put("pubYear", condition.getPublicationYear());
		return getNamedParameterJdbcTemplate().query(getListSqlByPub(false,condition), paramMap, rowMapper);
	}
	
	private String getListSqlByPub(boolean isCount,SearchInstitutionPaperCondition condition){
		StringBuilder sb=new StringBuilder();
		sb.append(" select ").append(isCount?"count(*)":"p.*").append(" from institution_paper_ref r,institution_paper p,institution_publication u ");
		sb.append(" where p.id=r.paper_id and p.publication_id=u.id");
		sb.append(" and r.institution_id=:insId");
		sb.append(getConditionSql(isCount, condition));
		return sb.toString();
	}
	
	private String getConditionSql(boolean isCount, SearchInstitutionPaperCondition condition){
		StringBuilder sb=new StringBuilder();
		//关键字
		if(!CommonUtils.isNull(condition.getKeyword())){
			sb.append(" and p.title like :keyword ");
		}
		if(!CommonUtils.isNull(condition.getKeyword3())){
			sb.append(" and (u.abbr_title like :keyword3 or u.pub_name like :keyword3) ");
		}
		//导入时间
		if(!(condition.getMinImp()==null&&condition.getMaxImp()==null)){
			if(condition.getMaxImp()==null){
				sb.append(" and p.create_time>='"+new java.sql.Date(condition.getMinImp().getTime())+"'");
			}else if(condition.getMinImp()==null){
				sb.append(" and p.create_time<='"+new java.sql.Date(condition.getMaxImp().getTime())+"'");
			}else{
				sb.append(" and p.create_time>='"+new java.sql.Date(condition.getMinImp().getTime())+"' and p.create_time<='"+new java.sql.Date(condition.getMaxImp().getTime())+"'");
			}
		}
		//发布时间
//		if(condition.getPublicationYear()!=null&&!condition.getPublicationYear().equals("0")){
//			String[] years = condition.getPublicationYear().split("-");
//			sb.append(" and p.publication_year>="+years[0]+" and p.publication_year<="+years[1]);
//		}
		if(!(condition.getYear_min()==0&&condition.getYear_max()==0)){
			if(condition.getYear_max()==0){
				sb.append(" and p.publication_year>="+condition.getYear_min());
			}else{
				sb.append(" and p.publication_year>="+condition.getYear_min()+" and p.publication_year<="+condition.getYear_max());
			}
		}
		//影响因子
		if(!(condition.getIf_min()==0&&condition.getIf_max()==0)){
			if(condition.getIf_max()==0){
				sb.append(" and u.if2013_2014>="+condition.getIf_min());
			}else{
				sb.append(" and u.if2013_2014>="+condition.getIf_min()+" and u.if2013_2014<="+condition.getIf_max());
			}
		}
		//引用频次
		if(!(condition.getCitation_min()==0&&condition.getCitation_max()==0)){
			if(condition.getCitation_max()==0){
				sb.append(" and p.citation>="+condition.getCitation_min());
			}else{
				sb.append(" and p.citation>="+condition.getCitation_min()+" and p.citation<="+condition.getCitation_max());
			}
		}
		//部门
		if(condition.getDepartmentId()!=-1&&condition.getDepartmentId()!=0){
			sb.append(" and p.depart_id=:departId");
		}
		//是否认证
		if(condition.getStatus()!=0){
			if(condition.getStatus()==1){
				sb.append(" and p.status=1");
			}else{
				sb.append(" and p.status!=1");
			}
		}
		if(!isCount){
			//排序
			sb.append(" order by "+getOrderColumnByStatus(condition.getOrder())+getOrderTypeByType(condition.getOrderType()));
			sb.append(" limit :offset,:size");
		}
		return sb.toString();
	}
	@Override
	public List<InstitutionPaper> getPapersByAuthor(int institutionId,
			SearchInstitutionPaperCondition condition, int offset, int size) {
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("institutionId", institutionId);
		paramMap.put("offset", offset);
		paramMap.put("keyword2", "%"+CommonUtils.killNull(condition.getKeyword2())+"%");
		paramMap.put("size", size);
		paramMap.put("departId", condition.getDepartmentId());
//		paramMap.put("ifMin", condition.getIf_min());
//		paramMap.put("ifMax", condition.getIf_max());
//		paramMap.put("pubYear", condition.getPublicationYear());
		return getNamedParameterJdbcTemplate().query(getListSqlByAuthor(false,condition), paramMap, rowMapper);
	}
	@Override
	public int getPaperCountByAuthor(int institutionId,
			SearchInstitutionPaperCondition condition) {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("institutionId", institutionId);
		paramMap.put("keyword2", "%"+CommonUtils.killNull(condition.getKeyword2())+"%");
		paramMap.put("departId", condition.getDepartmentId());
//		paramMap.put("pubYear", condition.getPublicationYear());
//		paramMap.put("ifMin", condition.getIf_min());
//		paramMap.put("ifMax", condition.getIf_max());
		return getNamedParameterJdbcTemplate().queryForInt(getListSqlByAuthor(true, condition),paramMap);
	}
	private String getListSqlByAuthor(boolean isCount,SearchInstitutionPaperCondition condition){
//		String sql=" from (SELECT p.* from institution_paper_author_ref par,institution_author a,institution_paper p,institution_paper_ref r,institution_publication u " +
//				"where p.id=par.paper_id and a.id=par.author_id and r.paper_id=p.id and r.institution_id=:institutionId and p.publication_id=u.id and a.name like :keyword2 ";
//				
		String sql=" from (SELECT p.* from institution_paper p left join institution_paper_author_ref par " +
				"on p.id=par.paper_id left join institution_paper_ref r on r.paper_id=p.id left join institution_author a " +
				"on a.id=par.author_id left join institution_publication u on p.publication_id=u.id " +
				"where r.institution_id=:institutionId and a.name like :keyword2 ";
				
		StringBuilder sb=new StringBuilder();
		sb.append(" select ").append(isCount?"count(*)":"*").append(sql);
		//导入时间
		if(!(condition.getMinImp()==null&&condition.getMaxImp()==null)){
			if(condition.getMaxImp()==null){
				sb.append(" and p.create_time>='"+new java.sql.Date(condition.getMinImp().getTime())+"'");
			}else if(condition.getMinImp()==null){
				sb.append(" and p.create_time<='"+new java.sql.Date(condition.getMaxImp().getTime())+"'");
			}else{
				sb.append(" and p.create_time>='"+new java.sql.Date(condition.getMinImp().getTime())+"' and p.create_time<='"+new java.sql.Date(condition.getMaxImp().getTime())+"'");
			}
		}
		//发布时间
//		if(condition.getPublicationYear()!=null&&!condition.getPublicationYear().equals("0")){
//			String[] years = condition.getPublicationYear().split("-");
//			sb.append(" and p.publication_year>="+years[0]+" and p.publication_year<="+years[1]);
//		}
		if(!(condition.getYear_min()==0&&condition.getYear_max()==0)){
			if(condition.getYear_max()==0){
				sb.append(" and p.publication_year>="+condition.getYear_min());
			}else{
				sb.append(" and p.publication_year>="+condition.getYear_min()+" and p.publication_year<="+condition.getYear_max());
			}
		}
		//影响因子
		if(!(condition.getIf_min()==0&&condition.getIf_max()==0)){
			if(condition.getIf_max()==0){
				sb.append(" and u.if2013_2014>="+condition.getIf_min());
			}else{
				sb.append(" and u.if2013_2014>="+condition.getIf_min()+" and u.if2013_2014<="+condition.getIf_max());
			}
		}
		//引用频次
		if(!(condition.getCitation_min()==0&&condition.getCitation_max()==0)){
			if(condition.getCitation_max()==0){
				sb.append(" and p.citation>="+condition.getCitation_min());
			}else{
				sb.append(" and p.citation>="+condition.getCitation_min()+" and p.citation<="+condition.getCitation_max());
			}
		}
		//部门
		if(condition.getDepartmentId()!=-1&&condition.getDepartmentId()!=0){
			sb.append(" and p.depart_id=:departId");
		}
		//是否认证
		if(condition.getStatus()!=0){
			if(condition.getStatus()==1){
				sb.append(" and p.status=1");
			}else{
				sb.append(" and p.status!=1");
			}
		}
		sb.append(" group BY p.id");
		if(!isCount){
			//排序
			sb.append(" order by "+getOrderColumnByStatus(condition.getOrder())+getOrderTypeByType(condition.getOrderType()));
			sb.append(" limit :offset,:size");
		}
		sb.append(") s");
		return sb.toString();
	}
	@Override
	public int getPaperCount(int insId, int departId, int year) {
		String sql="select count(*) from institution_paper p,institution_paper_ref r where p.id=r.paper_id and r.institution_id=:insId and p.status=1";
		StringBuilder sb=new StringBuilder();
		sb.append(sql);
		if(departId!=-1){
			sb.append(" and p.depart_id=:departId");
		}
		if(year!=-1){
			sb.append(" and p.publication_year=:year");
		}
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId", insId);
		paramMap.put("departId", departId);
		paramMap.put("year", year);
		return getNamedParameterJdbcTemplate().queryForInt(sb.toString(),paramMap);
	}
	@Override
	public List<InstitutionPaper> getPapersByAuthorId(int authorId,int startYear,int endYear) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from institution_paper p,institution_paper_author_ref r where p.id=r.paper_id ");
		sb.append("and p.publication_year>=:startYear and p.publication_year<=:endYear ");
		sb.append("and (r.order=1 or r.communication_author=true or(r.order=2 and r.author_student=true) or (r.order=2 and r.author_teacher=true)) ");
		sb.append("and r.author_id=:authorId ");
//		String sql="select * from institution_paper p,institution_paper_ref r where p.id=r.paper_id and r.author_id=:authorId" +
//				"and (r.order=1 or r.communication_author=true or(r.order=2 and r.author_student=true) or " +
//				"(r.order=2 and r.author_teacher=true)) and p.publication_year>=:startYear and p.publication_year<:endYear";
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("authorId", authorId);
		paramMap.put("startYear", startYear);
		paramMap.put("endYear", endYear);
		return getNamedParameterJdbcTemplate().query(sb.toString(), paramMap, rowMapper);
	}
	@Override
	public List<InstitutionPaper> getOtherPapersByAuthorId(int authorId,
			int startYear, int endYear) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from institution_paper p,institution_paper_author_ref r where p.id=r.paper_id ");
		sb.append("and p.publication_year>=:startYear and p.publication_year<=:endYear ");
		sb.append(" and r.communication_author!=true and ((r.order!=1 and r.order!=2) or ((r.order=2 and r.author_student!=true) or (r.order=2 and r.author_teacher!=true))) ");
		sb.append("and r.author_id=:authorId ");
//		String sql="select * from institution_paper p,institution_paper_ref r where p.id=r.paper_id and r.author_id=:authorId" +
//				"and (r.order=1 or r.communication_author=true or(r.order=2 and r.author_student=true) or " +
//				"(r.order=2 and r.author_teacher=true)) and p.publication_year>=:startYear and p.publication_year<:endYear";
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("authorId", authorId);
		paramMap.put("startYear", startYear);
		paramMap.put("endYear", endYear);
		return getNamedParameterJdbcTemplate().query(sb.toString(), paramMap, rowMapper);
	}
	@Override
	public int getAllSciCount(int userId) {
		String sql="select count(*) from institution_paper p,institution_paper_user u,institution_publication a where p.id=u.paper_id " +
				"and p.publication_id=a.id and u.user_id=:userId and a.publication_type='SCI' ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("userId", userId);
		return getNamedParameterJdbcTemplate().queryForInt(sql,paramMap);
	}
	@Override
	public List<InstitutionPaper> getAllFirst(int authorId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from institution_paper p,institution_paper_author_ref r where p.id=r.paper_id ");
		sb.append(" and r.communication_author!=true and ((r.order!=1 and r.order!=2) or ((r.order=2 and r.author_student!=true) or (r.order=2 and r.author_teacher!=true))) ");
		sb.append("and r.author_id=:authorId ");
		
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("authorId", authorId);
		return getNamedParameterJdbcTemplate().query(sb.toString(), paramMap, rowMapper);
	}
	@Override
	public int move(InstitutionPaper paper) {
		StringBuffer sb=new StringBuffer();
		sb.append("insert into institution_paper(");
		sb.append("`title`,");
//		sb.append("`doi`,");
//		sb.append("`issn`,");
		sb.append("`publication_id`,");
		sb.append("`publication_year`,");
		sb.append("`publication_month`,");
//		sb.append("`start_page`,");
		sb.append("`publication_page`,");
		sb.append("`volume_number`,");
		sb.append("`series`,");
//		sb.append("`discipline_orientation_id`,");
		sb.append("`keyword`,");
//		sb.append("`sponsor`,");
		sb.append("`summary`,");
//		sb.append("`original_link`,");
//		sb.append("`citation`,");
//		sb.append("`citation_query_time`,");
//		sb.append("`original_file_name`,");
//		sb.append("`original_clb_id`,");
//		sb.append("`annual_award_marks`,");
//		sb.append("`performance_calculation_year`,");
		sb.append("`depart_id`,");
//		sb.append("`author_total`,");
		sb.append("`status`)");
//		sb.append("`institution_id`,");
//		sb.append("`hash`) ");
		
		sb.append(" select ");
		
		sb.append(":title,");
//		sb.append(":doi,");
//		sb.append(":issn,");
		sb.append(":publication_id,");
		sb.append(":publication_year,");
		sb.append(":publication_month,");
//		sb.append(":start_page,");
		sb.append(":publication_page,");
		sb.append(":volume_number,");
		sb.append(":series,");
//		sb.append(":discipline_orientation_id,");
		sb.append(":keyword,");
//		sb.append(":sponsor,");
		sb.append(":summary,");
//		sb.append(":original_link,");
//		sb.append(":citation,");
//		sb.append(":citation_query_time,");
//		sb.append(":original_file_name,");
//		sb.append(":original_clb_id,");
//		sb.append(":annual_award_marks,");
//		sb.append(":performance_calculation_year,");
		sb.append(":departId,");
//		sb.append(":authorAmount,");
		sb.append(":status ");
		sb.append("from dual where not exists(select * from institution_paper where title=:title)");
//		sb.append(":hash) ");
		KeyHolder keyHolder = new GeneratedKeyHolder();
        getNamedParameterJdbcTemplate().update(sb.toString(), new MapSqlParameterSource(fromPaper(paper)), keyHolder);
        if(keyHolder.getKey()==null){
        	return 0;
        }else{
        	return keyHolder.getKey().intValue();
        }
	}
	@Override
	public InstitutionPaper getPaperByTitle(String title) {
		String sql="select * from institution_paper where title=:title";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("title", title);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper));
	}
	@Override
	public List<InstitutionPaper> getPapersByAuthor(int authorId,
			int startYear, int endYear) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from institution_paper p,institution_paper_author_ref r where p.id=r.paper_id ");
		sb.append("and p.publication_year>=:startYear and p.publication_year<=:endYear ");
		sb.append("and r.author_id=:authorId ");
		
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("authorId", authorId);
		paramMap.put("startYear", startYear);
		paramMap.put("endYear", endYear);
		return getNamedParameterJdbcTemplate().query(sb.toString(), paramMap, rowMapper);
	}
	@Override
	public int getPaperByDoi(String doi) {
		String sql="select * from institution_paper where doi=:doi";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("doi", doi);
		
		List<Integer> ids= getNamedParameterJdbcTemplate().query(sql, paramMap,new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("id");
			}
		});
		if(CommonUtils.isNull(ids)){
			return 0;
		}
		return CommonUtils.first(ids);
	}
	@Override
	public List<InstitutionPaper> getPapersByTitle(String title) {
		String sql="select * from institution_paper where title=:title";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("title", title);
		return getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper);
	}
}
