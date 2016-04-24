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
/**
 * 
 */
package net.duckling.dhome.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.dao.IInstitutionOptionValDAO;
import net.duckling.dhome.domain.institution.InstitutionOptionVal;

@Repository
public class InstitutionOptionValDAO extends BaseDao implements IInstitutionOptionValDAO{

	@Override
	public int add(InstitutionOptionVal val) {
		return insert(val);
	}

	@Override
	public List<InstitutionOptionVal> getListByOptionId(int optionId, int institutionId) {
		InstitutionOptionVal val=new InstitutionOptionVal();
		val.setOptionId(optionId);
		val.setInstitutionId(institutionId);
		return findByProperties(val,"order by rank");
	}

	@Override
	public void edit(InstitutionOptionVal val) {
		String sql="update institution_option_val set val=:val,rank=:rank where id=:id and institution_id=:institutionId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", val.getId());
		params.put("institutionId", val.getInstitutionId());
		params.put("val", val.getVal());
		params.put("rank", val.getRank());
		getNamedParameterJdbcTemplate().update(sql, params);
	}

	@Override
	public InstitutionOptionVal getById(int id) {
		InstitutionOptionVal val=new InstitutionOptionVal();
		val.setId(id);
		return findAndReturnOnly(val);
	}
	
	
	
	@Override
	public void delete(int id,int institutionId) {
		String sql="delete from institution_option_val where id=:id and institution_id=:institutionId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("institutionId", institutionId);
		getNamedParameterJdbcTemplate().update(sql, params);
	}

}
