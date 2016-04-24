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
package net.duckling.dhome.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.domain.institution.DatabasePaper;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.InstitutionPublication;

@Repository
public class DatabasePaperImpl extends BaseDao implements IDatabasePaper {
	private RowMapper<DatabasePaper> rowMapper= new RowMapper<DatabasePaper>() {
		@Override
		public DatabasePaper mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			DatabasePaper paper=new DatabasePaper();
			paper.setId(rs.getInt("id"));
			paper.setTitle(rs.getString("title"));
			paper.setAuthor1(rs.getString("author1"));
			paper.setAuthor1_company(rs.getString("author1_company"));
			paper.setAuthor2(rs.getString("author2"));
			paper.setAuthor2_company(rs.getString("author2_company"));
			paper.setAuthor3(rs.getString("author3"));
			paper.setAuthor3_company(rs.getString("author3_company"));
			paper.setAuthor4(rs.getString("author4"));
			paper.setAuthor4_company(rs.getString("author4_company"));
			paper.setAuthor_amount(rs.getInt("author_amount"));
			paper.setAward_standard(rs.getString("award_standard"));
			paper.setDepartment(rs.getString("department"));
			paper.setDos(rs.getString("dos"));
			paper.setIfs(rs.getString("if"));
			paper.setIssue_number(rs.getString("issue_number"));
			paper.setPage_number(rs.getString("page_number"));
			paper.setPublication_category(rs.getString("publication_category"));
			paper.setPublication_date(rs.getString("publication_date"));
			paper.setPublication_series(rs.getString("publication_series"));
			paper.setReel_number(rs.getString("reel_number"));
			paper.setRemark(rs.getString("remark"));
			paper.setSend(rs.getString("send"));
			paper.setSn(rs.getInt("sn"));
			paper.setUser_name(rs.getString("user_name"));
			paper.setPublication_name(rs.getString("publication_name"));
			return paper;
		}
	};

	@Override
	public Map<Integer,DatabasePaper> insertPaper(List<DatabasePaper> dbaPaper) {
		String sql="insert into institution_paper(`id`,`title`,`doi`,`publication_id`,`publication_year`,`publication_page`,"
	+"`volume_number`,`series`,`creator`,`depart_id`,`author_total`,`if`,`award_standard`,`pay_total`,`remark`,`status`)" +
	" values(:id,:title,:doi,:publicationId,:publicationYear,:publicationPage,:volumeNumber,:series,:creator," +
	":departId,:authorTotal,:if,:awardStandard,:payTotal,:remark,:status)";
		Map<Integer,DatabasePaper> map = new HashMap<Integer,DatabasePaper>();
		for (DatabasePaper databasePaper : dbaPaper) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			getNamedParameterJdbcTemplate().update(sql,new MapSqlParameterSource(per2Map(databasePaper,getPubId(databasePaper.getPublication_name()))), keyHolder);
			map.put(keyHolder.getKey().intValue(), databasePaper);
		}
		return map;
	}
	public Map<String, Object> per2Map(DatabasePaper databasePaper,int publicationId){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id",databasePaper.getId());
		params.put("title",databasePaper.getTitle());
		params.put("doi",databasePaper.getDos());
		params.put("publicationId",publicationId);
		if(databasePaper.getPublication_date().equals("")){
			params.put("publicationYear",0);
		}else{
			params.put("publicationYear",Integer.parseInt(databasePaper.getPublication_date()));
		}
		
		params.put("publicationPage",databasePaper.getPage_number());
		params.put("volumeNumber",databasePaper.getReel_number());
		params.put("series",databasePaper.getIssue_number());
		params.put("creator",databasePaper.getUser_name());
		params.put("departId",databasePaper.getDepartId());
		params.put("authorTotal",databasePaper.getAuthor_amount());
		params.put("if",databasePaper.getIfs());
		params.put("awardStandard",databasePaper.getAward_standard());
		params.put("payTotal",databasePaper.getSend());
		params.put("remark",databasePaper.getRemark());
		params.put("status",1);
		return params;
	}

	@Override
	public List<DatabasePaper> getPaperTemp() {
		String sql="select * from institution_paper_temp";
		return getJdbcTemplate().query(sql,rowMapper);
	}
	@Override
	public int getPubId(String pubName) {
		String sql="select id from institution_publication where pub_name=:pubName";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("pubName", pubName);
		return getNamedParameterJdbcTemplate().queryForInt(sql,param);
	}
	private RowMapper<InstitutionPublication> rowMapperId= new RowMapper<InstitutionPublication>() {
		@Override
		public InstitutionPublication mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionPublication pub=new InstitutionPublication();
			pub.setId(rs.getInt("id"));
			return pub;
		}
	};

	@Override
	public void insert1(List<DatabasePaper> list) {
		String sql="insert into institution_paper_author_ref(`author_id`,`paper_id`,`order`,`communication_author`)" +
				"values(:authorId,:paperId,:order,:communicationAuthor)";
			for (DatabasePaper databasePaper : list) {
				if(databasePaper.getAuthor1()!=null&&!"".equals(databasePaper.getAuthor1())){
					Map<String,Object> paramMap=new HashMap<String,Object>();
					paramMap.put("authorId",getAuthorId(databasePaper.getAuthor1(),databasePaper.getAuthor1_company()));
					paramMap.put("paperId",databasePaper.getId());
					paramMap.put("order",1);
					paramMap.put("communicationAuthor",String.valueOf((databasePaper.getAuthor1().equals(databasePaper.getAuthor4()==null?"":databasePaper.getAuthor4().trim()))?true:false));
					getNamedParameterJdbcTemplate().update(sql, paramMap);
				}
			}
		
	}
	@Override
	public void insert2(List<DatabasePaper> list) {
		String sql="insert into institution_paper_author_ref(`author_id`,`paper_id`,`order`,`communication_author`)" +
				"values(:authorId,:paperId,:order,:communicationAuthor)";
		
		for (DatabasePaper databasePaper : list) {
			if(databasePaper.getAuthor2()!=null&&!"".equals(databasePaper.getAuthor2())){
				Map<String,Object> paramMap=new HashMap<String,Object>();
				paramMap.put("authorId",getAuthorId(databasePaper.getAuthor2(),databasePaper.getAuthor2_company()));
				paramMap.put("paperId",databasePaper.getId());
				paramMap.put("order",2);
				paramMap.put("communicationAuthor",String.valueOf((databasePaper.getAuthor2().equals(databasePaper.getAuthor4()==null?"":databasePaper.getAuthor4().trim()))?true:false));
				getNamedParameterJdbcTemplate().update(sql, paramMap);
			}
		}
		
	}
	@Override
	public void insert3(List<DatabasePaper> list) {
		String sql="insert into institution_paper_author_ref(`author_id`,`paper_id`,`order`,`communication_author`)" +
				"values(:authorId,:paperId,:order,:communicationAuthor)";
		for (DatabasePaper databasePaper : list) {
//			author4=databasePaper.getAuthor4()==null?"":databasePaper.getAuthor4().trim();
			if(databasePaper.getAuthor3()!=null&&!"".equals(databasePaper.getAuthor3())){
				Map<String,Object> paramMap=new HashMap<String,Object>();
				paramMap.put("authorId",getAuthorId(databasePaper.getAuthor3(),databasePaper.getAuthor3_company()));
				paramMap.put("paperId",databasePaper.getId());
				paramMap.put("order",3);
				paramMap.put("communicationAuthor",String.valueOf((databasePaper.getAuthor3().equals(databasePaper.getAuthor4()==null?"":databasePaper.getAuthor4().trim()))?true:false));
				getNamedParameterJdbcTemplate().update(sql, paramMap);
			}
		}
		
	}
	@Override
	public void insert4(List<DatabasePaper> list) {
		String sql="insert into institution_paper_author_ref(`author_id`,`paper_id`,`communication_author`)" +
				"select :authorId,:paperId,:communicationAuthor from dual where not exists" +
				"(select * from institution_paper_author_ref where author_id=:authorId and paper_id=:paperId)";
		for (DatabasePaper databasePaper : list) {
			if(databasePaper.getAuthor4()!=null&&!"".equals(databasePaper.getAuthor4())){
				if(!databasePaper.getAuthor4().equals(databasePaper.getAuthor3())&&!databasePaper.getAuthor4().equals(databasePaper.getAuthor2())&&!databasePaper.getAuthor4().equals(databasePaper.getAuthor1())){
					Map<String,Object> paramMap=new HashMap<String,Object>();
					paramMap.put("authorId",getAuthorId(databasePaper.getAuthor4(),databasePaper.getAuthor4_company()));
					paramMap.put("paperId",databasePaper.getId());
		//			paramMap.put("order",3);
					paramMap.put("communicationAuthor","true");
					getNamedParameterJdbcTemplate().update(sql, paramMap);
				}
				
			}
		}
		
	}
	@Override
	public int getAuthorId(String authorName,String authorCompany) {
		String sql="select id from institution_author where name=:name and company=:company";
		Map<String,Object> param=new HashMap<String,Object>();
		
		try{
			param.put("name", authorName.trim());
			param.put("company", authorCompany==null?"":authorCompany.trim());
			return getNamedParameterJdbcTemplate().queryForInt(sql,param);
		}catch (EmptyResultDataAccessException e) {
			    return 0;
			    }
	}
	@Override
	public void insertPaperRef(final List<DatabasePaper> papers, int insId) {
		String sql="insert into institution_paper_ref(paper_id,institution_id,first_author_name) select ?,?,? from dual where not exists" +
				"(select * from institution_paper_ref where paper_id=? and institution_id=?)";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return papers.size();
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i, papers.get(index).getId());
						pst.setInt(++i,1799);
						pst.setString(++i,papers.get(index).getAuthor1());
						pst.setInt(++i, papers.get(index).getId());
						pst.setInt(++i,1799);
					}

				});
		
	}
	@Override
	public void insertAuthor(String name,String company) {
		String sql="insert into institution_author(`name`,`company`,`institution_id`) select :name,:company,'1799' from dual where not exists(select * from institution_author where name=:name and company=:company)";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("name",name.trim());
		paramMap.put("company",company.trim());
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}

}
