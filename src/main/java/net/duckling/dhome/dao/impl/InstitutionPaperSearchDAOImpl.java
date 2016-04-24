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

import net.duckling.dhome.dao.IInstitutionPaperSearchDAO;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.common.util.CommonUtils;

@Repository
public class InstitutionPaperSearchDAOImpl extends BaseDao implements
		IInstitutionPaperSearchDAO {


private static final RowMapper<InstitutionPaper> rowMapper=new RowMapper<InstitutionPaper>(){
		
		@Override
		public InstitutionPaper mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionPaper paper=new InstitutionPaper();
			paper.setId(rs.getInt("id"));
			paper.setTitle(rs.getString("title"));
			paper.setDoi(rs.getString("doi"));
			paper.setIssn(rs.getString("issn"));
			paper.setPublicationId(rs.getInt("publication_id"));
			paper.setPublicationYear(rs.getInt("publication_year"));
			paper.setPublicationMonth(rs.getInt("publication_month"));
			paper.setPublicationPage(rs.getString("publication_page"));
//			paper.setEndPage(rs.getInt("end_page"));
			paper.setVolumeNumber(rs.getString("volume_number"));
			paper.setSeries(rs.getString("series"));
			paper.setDisciplineOrientationId(rs.getInt("discipline_orientation_id"));
			paper.setKeywordDisplay(rs.getString("keyword"));
			paper.setSummary(rs.getString("summary"));
			paper.setOriginalLink(rs.getString("original_link"));
			paper.setCitation(rs.getString("citation"));
			paper.setCitationQueryTime(rs.getString("citation_query_time"));
			paper.setOriginalFileName(rs.getString("original_file_name"));
			paper.setClbId(rs.getInt("original_clb_id"));
			paper.setAnnualAwardMarks(rs.getString("annual_award_marks"));
			paper.setPerformanceCalculationYear(rs.getInt("performance_calculation_year"));
			paper.setAuthor(rs.getString("au_name"));
			return paper;
		}};
	
	@Override
	public List<InstitutionPaper> getPaperByKeyword(int offset, int size, 
			String keyword) {
		String sql="SELECT a.*,group_concat(c.name) as au_name from institution_paper_author_ref b,institution_author c," +
			"institution_paper a where a.id=b.paper_id and c.id=b.author_id " +
			"GROUP BY a.id having a.title like :keyword or au_name like :keyword "+
			"limit :offset,:size" ;
//		System.out.println(getListSql(true, condition)+"=====");
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("keyword", "%"+CommonUtils.killNull(keyword)+"%");
		param.put("offset",offset);
		param.put("size", size);
		return getNamedParameterJdbcTemplate().query(sql, param, rowMapper);
	}

	@Override
	public List<InstitutionPaper> getPaperByInit(int offset, int size,
			String userName) {
//		String sql="SELECT d.* from (SELECT a.*,group_concat(c.name) as au_name from institution_paper a "+
//				"left join (institution_paper_author_ref b join institution_author c on b.author_id=c.id) on a.id=b.paper_id "+
//				"GROUP BY a.id having au_name like :keyword"+
//				") d limit :offset,:size" ;
		String sql="SELECT a.*,group_concat(c.name) as au_name from institution_paper_author_ref b,institution_author c," +
				"institution_paper a where a.id=b.paper_id and c.id=b.author_id " +
				"GROUP BY a.id having au_name like :keyword "+
				"limit :offset,:size" ;
//			System.out.println(getListSql(true, condition)+"=====");
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("keyword", "%"+CommonUtils.killNull(userName)+"%");
			param.put("offset",offset);
			param.put("size", size);
			return getNamedParameterJdbcTemplate().query(sql, param, rowMapper);
	}
	@Override
	public int getPaperCountByInit(int institutionId,String userName) {
//		String sql="SELECT count(*) from (SELECT a.id,group_concat(c.name) as au_name from institution_paper_author_ref b,institution_author c," +
//				"institution_paper a where a.id=b.paper_id and c.id=b.author_id " +
//				"GROUP BY a.id having au_name like :keyword "+
//				") d" ;
//		String sql="SELECT count(*) from (SELECT a.id from institution_paper_author_ref b,institution_author c," +
//				"institution_paper a where a.id=b.paper_id and c.id=b.author_id " +
//				"GROUP BY a.id having group_concat(c.name) like :keyword ) d";
		String sql="SELECT count(*) from institution_paper_author_ref b,institution_author c," +
				"institution_paper a where a.id=b.paper_id and c.id=b.author_id and c.name like :keyword ";
//			System.out.println(getListSql(true, condition)+"=====");
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("keyword", "%"+CommonUtils.killNull(userName)+"%");
//			param.put("institutionId",institutionId);
			return getNamedParameterJdbcTemplate().queryForInt(sql, param);
	}
	@Override
	public List<InstitutionPaper> getPapersByInit(int institutionId,int offset, int size,
			String userName) {
//		String sql="SELECT d.* from (SELECT a.*,group_concat(c.name) as au_name from institution_paper a "+
//				"left join (institution_paper_author_ref b join institution_author c on b.author_id=c.id) on a.id=b.paper_id "+
//				"GROUP BY a.id having au_name like :keyword"+
//				") d limit :offset,:size" ;
//		String sql="SELECT a.*,group_concat(c.name) as au_name from institution_paper_author_ref b,institution_author c," +
//				"institution_paper a where a.id=b.paper_id and c.id=b.author_id " +
//				"GROUP BY a.id having au_name like :keyword "+
//				"limit :offset,:size" ;
		String sql="SELECT a.* from institution_paper_author_ref b,institution_author c," +
				"institution_paper a where a.id=b.paper_id and c.id=b.author_id and c.name like :keyword "+
				"limit :offset,:size" ;
//			System.out.println(getListSql(true, condition)+"=====");
			Map<String,Object> param=new HashMap<String,Object>();
//			param.put("institutionId",institutionId);
			param.put("keyword", "%"+CommonUtils.killNull(userName)+"%");
			param.put("offset",offset);
			param.put("size", size);
			return getNamedParameterJdbcTemplate().query(sql, param, rowMappers);
	}
private static final RowMapper<InstitutionPaper> rowMappers=new RowMapper<InstitutionPaper>(){
		
		@Override
		public InstitutionPaper mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionPaper paper=new InstitutionPaper();
			paper.setId(rs.getInt("id"));
			paper.setTitle(rs.getString("title"));
			paper.setDoi(rs.getString("doi"));
			paper.setIssn(rs.getString("issn"));
			paper.setPublicationId(rs.getInt("publication_id"));
			paper.setPublicationYear(rs.getInt("publication_year"));
			paper.setPublicationMonth(rs.getInt("publication_month"));
			paper.setPublicationPage(rs.getString("publication_page"));
//			paper.setEndPage(rs.getInt("end_page"));
			paper.setVolumeNumber(rs.getString("volume_number"));
			paper.setSeries(rs.getString("series"));
			paper.setDisciplineOrientationId(rs.getInt("discipline_orientation_id"));
			paper.setKeywordDisplay(rs.getString("keyword"));
			paper.setSummary(rs.getString("summary"));
			paper.setOriginalLink(rs.getString("original_link"));
			paper.setCitation(rs.getString("citation"));
			paper.setCitationQueryTime(rs.getString("citation_query_time"));
			paper.setOriginalFileName(rs.getString("original_file_name"));
			paper.setClbId(rs.getInt("original_clb_id"));
			paper.setAnnualAwardMarks(rs.getString("annual_award_marks"));
			paper.setPerformanceCalculationYear(rs.getInt("performance_calculation_year"));
//			paper.setAuthor(rs.getString("au_name"));
			return paper;
		}};
	@Override
	public List<InstitutionPaper> getPaperByKeywordInit(int offset, int size,
			List<InstitutionAuthor> authors) {
		StringBuffer sb = new StringBuffer();
		
//		String sql="SELECT d.* from (SELECT a.*,group_concat(c.name) as au_name from institution_paper a "+
//				"left join (institution_paper_author_ref b join institution_author c on b.author_id=c.id) on a.id=b.paper_id ";
		String sql="SELECT a.*,group_concat(c.name) as au_name from institution_paper_author_ref b,institution_author c," +
				"institution_paper a where a.id=b.paper_id and c.id=b.author_id " ;
		sb.append(sql).append("GROUP BY a.id having au_name like :keyword1");
		for(int i=2;i<=authors.size();i++){
			sb.append(" or au_name like :keyword").append(i);
		}
		sb.append(" limit :offset,:size");
//			System.out.println(getListSql(true, condition)+"=====");
			Map<String,Object> param=new HashMap<String,Object>();
			int j=1;
			for (InstitutionAuthor author : authors) {
				param.put("keyword"+(j++), "%"+CommonUtils.killNull(author.getAuthorName())+"%");
			}
			param.put("offset",offset);
			param.put("size", size);
			return getNamedParameterJdbcTemplate().query(sb.toString(), param, rowMapper);
	}
	@Override
	public void insertPaper(final String[] paperId,final int userId) {
//		String sql="insert into institution_paper_user(`user_id`,`paper_id`) values(?,?)";
		String sql="insert into institution_paper_user(`user_id`,`paper_id`) select ?,? from dual where not exists" +
				"(select * from institution_paper_user where paper_id=? and user_id=?)";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return paperId.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i,userId);
						pst.setInt(++i,Integer.parseInt(paperId[index]));
						pst.setInt(++i,Integer.parseInt(paperId[index]));
						pst.setInt(++i,userId);
					}
				});
	}

	@Override
	public List<Integer> getExistPaperIds(int uid) {
		String sql="select paper_id from institution_paper_user where user_id=:uid";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("uid", uid);
		
		List<Integer> ids= getNamedParameterJdbcTemplate().query(sql, paramMap,new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("paper_id");
			}
		});
		return ids;
	}

	@Override
	public void deletePaperUser(final String[] paperId, final int userId) {
		String sql="delete from institution_paper_user where user_id=? and paper_id=?";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return paperId.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i,userId);
						pst.setInt(++i,Integer.parseInt(paperId[index]));
					}
				});
	}


}
