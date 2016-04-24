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
import java.util.Map;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.dao.IInstitutionVmtDAO;
import net.duckling.dhome.domain.institution.InstitutionVmtInfo;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class InstitutionVmtDAOImpl extends BaseDao implements
		IInstitutionVmtDAO {
	private static final RowMapper<InstitutionVmtInfo> rowMapper = new RowMapper<InstitutionVmtInfo>() {
		@Override
		public InstitutionVmtInfo mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionVmtInfo info = new InstitutionVmtInfo();
			info.setUrl(rs.getString("url"));
			info.setId(rs.getInt("id"));
			info.setDn(rs.getString("vmt_dn"));
			info.setGroupOrOrg(rs.getString("vmt_group_or_org"));
			info.setInstitutionId(rs.getInt("institution_id"));
			info.setName(rs.getString("vmt_name"));
			info.setSymbol(rs.getString("vmt_symbol"));
			return info;
		}
	};

	@Override
	public InstitutionVmtInfo getVmtInfoByInstitutionId(int insId) {
		String sql="select * from `institution_ref_vmt` "+
				" where institution_id=:insId";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("insId", insId);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, param, rowMapper));
	}

	@Override
	public int insertTeamInfo(InstitutionVmtInfo info) {
		String sql = "insert into institution_ref_vmt value( "
				+ "`institution_id`," + "`url`," + "`vmt_dn`,"
				+ "`vmt_symbol`," + "`vmt_name`," + "`vmt_group_or_org`) "
				+ "values (:insId,:url,:vmtDn,:vmtSymbol,:vmtName,:groupOrOrg)";

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("insId", info.getInstitutionId());
		param.put("url", info.getUrl());
		param.put("vmtDn", info.getDn());
		param.put("vmtSymbol", info.getSymbol());
		param.put("vmtName", info.getName());
		param.put("groupOrOrg", info.getGroupOrOrg());
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getNamedParameterJdbcTemplate().update(sql,
				new MapSqlParameterSource(param), keyHolder);
		info.setId(keyHolder.getKey().intValue());
		return info.getId();
	}

	@Override
	public InstitutionVmtInfo getVmtInfoByDN(String dn) {
		String sql = "select * from institution_ref_vmt where vmt_dn=:vmtDn";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("vmtDn", dn);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, param, rowMapper));
	}
	@Override
	public void update(int institutionRefId, InstitutionVmtInfo needUpdate) {
		String sql="update institution_ref_vmt set vmt_name=:vmtName where id=:id ";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("id",institutionRefId);
		paramMap.put("vmtName", needUpdate.getName());
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}
}
