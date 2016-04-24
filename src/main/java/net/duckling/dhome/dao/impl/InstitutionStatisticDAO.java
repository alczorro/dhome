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

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.dao.IInstitutionStatisticDAO;
import net.duckling.dhome.domain.institution.InstitutionPublicationStatistic;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * 机构论文统计信息的数据库接口
 * @author Yangxp
 *
 */
@Repository
public class InstitutionStatisticDAO extends BaseDao implements IInstitutionStatisticDAO {
	
	private static final String CREATE_NO_DUPLICATE = "insert into institution_publication_statistic(" +
			"year,institution_id,annual_paper_count,annual_citation_count,total_paper_count," +
			"total_citation_count) values(:year,:insId,:apc,:acc,:tpc,:tcc) on duplicate key update" +
			" year=:year, institution_id=:insId, annual_paper_count=:apc, annual_citation_count=:acc," +
			"total_paper_count=:tpc, total_citation_count=:tcc";
	
	private static RowMapper<InstitutionPublicationStatistic> rowMapper = new RowMapper<InstitutionPublicationStatistic>() {
		@Override
		public InstitutionPublicationStatistic mapRow(ResultSet rs, int index) throws SQLException {
			InstitutionPublicationStatistic ips = new InstitutionPublicationStatistic();
			ips.setId(rs.getInt("id"));
			ips.setAnnualCitationCount(rs.getInt("annual_citation_count"));
			ips.setAnnualPaperCount(rs.getInt("annual_paper_count"));
			ips.setInstitutionId(rs.getInt("institution_id"));
			ips.setTotalCitationCount(rs.getInt("total_citation_count"));
			ips.setTotalPaperCount(rs.getInt("total_paper_count"));
			ips.setYear(rs.getInt("year"));
			return ips;
		}
	};
	
	@Override
	public int create(InstitutionPublicationStatistic is) {
		return insert(is);
	}

	@Override
	public int[] batchCreate(List<InstitutionPublicationStatistic> statList) {
		Map<String,Object>[] paramsMap = generateParamsMap(statList);
		return this.getNamedParameterJdbcTemplate().batchUpdate(CREATE_NO_DUPLICATE, paramsMap);
	}

	@Override
	public void clear() {
		String sql = "delete from institution_publication_statistic";
		this.getJdbcTemplate().update(sql);
	}

	@Override
	public void updateById(InstitutionPublicationStatistic is) {
		update(is);
	}

	@Override
	public InstitutionPublicationStatistic getStatisticById(int id) {
		InstitutionPublicationStatistic is = new InstitutionPublicationStatistic();
		is.setId(id);
		return findAndReturnOnly(is);
	}

	@Override
	public List<InstitutionPublicationStatistic> getStatisticsByInstitutionId(int insId) {
		String sql = "select * from institution_publication_statistic where institution_id="+insId+" order by year";
		return this.getJdbcTemplate().query(sql, rowMapper);
	}

	private Map<String, Object>[] generateParamsMap(List<InstitutionPublicationStatistic> list){
		Map[] result = new HashMap[0];
		if(null != list && !list.isEmpty()){
			result = new HashMap[list.size()];
			int i=0;
			for(InstitutionPublicationStatistic ins : list){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("year", ins.getYear());
				map.put("insId", ins.getInstitutionId());
				map.put("apc", ins.getAnnualPaperCount());
				map.put("acc", ins.getAnnualCitationCount());
				map.put("tpc", ins.getTotalPaperCount());
				map.put("tcc", ins.getTotalCitationCount());
				result[i++] = map;
			}
		}
		return result;
	}
}
