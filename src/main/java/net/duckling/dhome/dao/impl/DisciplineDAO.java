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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.common.repository.DAOUtils;
import net.duckling.dhome.dao.IDisciplineDAO;
import net.duckling.dhome.domain.people.Discipline;

import org.springframework.stereotype.Component;

/**
 * 学科数据访问层jdbc实现
 * @author lvly
 * @since 2012-8-21
 */
@Component
public class DisciplineDAO extends BaseDao implements IDisciplineDAO{
	@Override
	public List<Discipline> getRoot() {
		String sql = "select * from discipline where parent_id ="+Discipline.ROOT_PARENT_ID;
		DAOUtils<Discipline> daoUtil=new DAOUtils<Discipline>(Discipline.class);
		Map<String,String> paramMap = new HashMap<String,String>();
		return getNamedParameterJdbcTemplate().query(sql, paramMap, daoUtil.getRowMapper(null));
	}

	@Override
	public List<Discipline> getChild(int id) {
		if(id==0){
			return new ArrayList<Discipline>();
		}
		Discipline dis=new Discipline();
		dis.setParentId(id);
		return findByProperties(dis);
	}
	@Override
	public String getName(int id) {
		if(id==0){
			return "";
		}
		Discipline dis=new Discipline();
		dis.setId(id);
		return findAndReturnOnly(dis).getName();
	}

}
