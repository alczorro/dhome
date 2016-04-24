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
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.dao.IInstitutionPublicationDAO;
import net.duckling.dhome.domain.institution.InstitutionPublication;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class InstitutionPublicationDAOImpl extends BaseDao implements IInstitutionPublicationDAO{
	
	@Override
	public int create(InstitutionPublication pub) {
		String sql ="insert into institution_publication(`pub_name`,`institution_id`,`issn`,`abbr_title`,`publication_type`) values(:pubName,:insId,:issn,:abbrTitle,:publicationType)";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("pubName", CommonUtils.trim(pub.getPubName()));
		param.put("issn", pub.getIssn());
		param.put("abbrTitle", pub.getAbbrTitle());
		param.put("insId", pub.getInstitutionId());
		param.put("publicationType", pub.getPublicationType());
		KeyHolder keyHolder = new GeneratedKeyHolder();
        getNamedParameterJdbcTemplate().update(sql, new MapSqlParameterSource(param), keyHolder);
        return keyHolder.getKey().intValue();
	}
	@Override
	public List<InstitutionPublication> all() {
		String sql="select * from institution_publication";
		return getNamedParameterJdbcTemplate().query(sql,new HashMap<String,Object>(), rowMapper);
	}
	private RowMapper<InstitutionPublication> rowMapper=new RowMapper<InstitutionPublication>(){
		@Override
		public InstitutionPublication mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionPublication pub=new InstitutionPublication();
			pub.setId(rs.getInt("id"));
			pub.setInstitutionId(rs.getInt("institution_id"));
			pub.setPubName(rs.getString("pub_name"));
			pub.setPublicationType(rs.getString("publication_type"));
			pub.setIfs(rs.getString("if2013_2014"));
			return pub;
		}
	};
	@Override
	public List<InstitutionPublication> getPubsByIds(List<Integer> pubId) {
		StringBuffer sb=new StringBuffer();
		sb.append("select * from institution_publication where id in(");
		for(int pid:pubId){
			sb.append(pid).append(",");
		};
		sb.deleteCharAt(sb.length()-1).append(")");
		return getNamedParameterJdbcTemplate().query(sb.toString(),new HashMap<String,Object>(), rowMapper);
	}
	
	@Override
	public InstitutionPublication getPubsById(int pubId) {
		StringBuffer sb=new StringBuffer();
		sb.append("select * from institution_publication where id="+pubId);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sb.toString(),new HashMap<String,Object>(), rowMapper));
	}
	@Override
	public List<InstitutionPublication> getPubsByKey(String keyword) {
		String sql="select * from institution_publication where pub_name like :pubName";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("pubName", "%"+keyword+"%");
		return getNamedParameterJdbcTemplate().query(sql,paramMap,rowMapper);
	}
	@Override
	public int insert(String pubName) {
		String sql ="insert into institution_publication(`pub_name`) select :pubName from dual " +
				"where not exists(select * from institution_publication where pub_name=:pubName)";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("pubName", CommonUtils.trim(pubName));
		KeyHolder keyHolder = new GeneratedKeyHolder();
        getNamedParameterJdbcTemplate().update(sql, new MapSqlParameterSource(param), keyHolder);
        if(keyHolder.getKey()==null){
        	return 0;
        }else{
        	return keyHolder.getKey().intValue();
        }
	}
	@Override
	public InstitutionPublication getIdByName(String pubName) {
		StringBuffer sb=new StringBuffer();
		sb.append("select * from institution_publication where pub_name='"+pubName+"'");
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sb.toString(),new HashMap<String,Object>(), rowMapper));
	}

}
