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

import java.util.HashMap;
import java.util.Map;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.dao.IUserGuideDAO;
import net.duckling.dhome.domain.people.UserGuide;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

/**
 * 用户指南的DAO实现
 * @author Yangxp
 * @since 2012-10-11
 */

@Repository
public class UserGuideDAO extends BaseDao implements IUserGuideDAO {

	private static final Logger LOG = Logger.getLogger(UserGuideDAO.class);
	
	@Override
	public int create(UserGuide ug) {
		return insert(ug);
	}

	@Override
	public void updateStep(int uid, String module, int step) {
		String sql = "update user_guide set step=:step where uid=:uid and module=:module";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("step", step);
		paramMap.put("uid", uid);
		paramMap.put("module", module);
		this.getNamedParameterJdbcTemplate().update(sql, paramMap);
	}

	@Override
	public int getStep(int uid, String module) {
		String sql = "select step from user_guide where uid=:uid and module=:module";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("uid", uid);
		paramMap.put("module", module);
		try{
			return this.getNamedParameterJdbcTemplate().queryForInt(sql, paramMap);
		}catch(EmptyResultDataAccessException e){
			LOG.info("No user_guide info can be found with uid="+uid+", module="+module);
			return 0;
		}
	}

}
