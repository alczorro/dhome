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

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.dao.IKeyValueDAO;

import org.springframework.stereotype.Component;

/**
 * 键值对的JDBC实现
 * @author lvly
 * @since 2012-9-28
 */
@Component
public class KeyValueDAO extends BaseDao implements IKeyValueDAO {

	@Override
	public String getValue(String key) {
		String sql="select `prop_value` from key_value where `prop_key`=:key";
		Map<String,String> map=new HashMap<String,String>();
		map.put("key", key);
		List<String> list= getNamedParameterJdbcTemplate().queryForList(sql, map,String.class);
		return	CommonUtils.first(list);
	}
	@Override
	public void addKeyValue(String key,String value) {
		String sql="insert into key_value(`prop_key`,`prop_value`) values(:propKey,:propValue)";
		Map<String,String> map=new HashMap<String,String>();
		map.put("propKey", key);
		map.put("propValue", value);
		getNamedParameterJdbcTemplate().update(sql, map);
		
	}

}
