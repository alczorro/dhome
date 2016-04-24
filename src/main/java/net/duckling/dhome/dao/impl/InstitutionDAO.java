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
import net.duckling.dhome.common.util.PinyinUtil;
import net.duckling.dhome.dao.IInstitutionDAO;
import net.duckling.dhome.domain.institution.Institution;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 * 对研究机构的jdbc实现类
 * 
 * @author lvly
 * */
@Component
public class InstitutionDAO extends BaseDao implements IInstitutionDAO {
	
	private static final Logger LOG = Logger.getLogger(InstitutionDAO.class);
	
	private static class InsRowMapper implements RowMapper<Institution>{	
		@Override
		public Institution mapRow(ResultSet rs, int index) throws SQLException {
			Institution ins = new Institution();
			ins.setId(rs.getInt("id"));
			ins.setZhName(rs.getString("zh_name"));
			ins.setEnName(rs.getString("en_name"));
			return ins;
		}
		
	}
	
	private InsRowMapper rowMapper = new InsRowMapper();
	
	@Override
	public int createInstitution(String name) {
		Institution ins = new Institution();
		ins.setZhName(name);
		ins.setPinyin(PinyinUtil.getPinyin(name));
		return insert(ins);
	}

	@Override
	public Institution getInstitutionByName(String name) {
		Institution ins = new Institution();
		ins.setZhName(name);
		List<Institution> list = findByProperties(ins);
		return CommonUtils.first(list);
	}

	@Override
	public Institution getInstitution(int institutionId) {
		Institution ins = new Institution();
		ins.setId(institutionId);
		return CommonUtils.first(findByProperties(ins));
	}

	@Override
	public List<Institution> getDsnInstitutionsByPrefixName(String prefix) {
		String sql = "select * from institution where id in (select distinct(institution_id) from institution_name_mapping where alias_name like :name)";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "%"+prefix+"%");
		return this.getNamedParameterJdbcTemplate().query(sql, params, rowMapper);
	}

	@Override
	public int searchOfficalInstitution(String insName) {
		String sql = "select distinct(institution_id) from institution_name_mapping where alias_name = ?";
		List<Integer> result = this.getJdbcTemplate().queryForList(sql, new Object[]{insName}, Integer.class);
		int insId = 0;
		if(null==result || result.isEmpty()){
			LOG.info("No result for institutionName = "+insName+" while search offical institution");
		}else if(result.size()>1){
			LOG.info("More than one result has been found for institutionName = "+insName+" while search offical institution");
			insId = result.get(0);
		}else{
			insId = result.get(0);
		}
		return insId;
	}
}
