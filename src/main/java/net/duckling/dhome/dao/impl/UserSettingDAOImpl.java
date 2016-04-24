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
import java.util.Map;

import org.springframework.stereotype.Component;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.dao.IUserSettingDAO;
import net.duckling.dhome.domain.people.UserSetting;


/**
 * @author lvly
 * @since 2013-9-5
 */
@Component
public class UserSettingDAOImpl  extends BaseDao implements IUserSettingDAO{
	@Override
	public UserSetting getSetting(int uid, String key) {
		UserSetting us=new UserSetting();
		us.setUid(uid);
		us.setKey(key);
		return findAndReturnOnly(us);
	}
	
	@Override
	public void deleteSetting(int uid, String key) {
		UserSetting us=new UserSetting();
		us.setUid(uid);
		us.setKey(key);
		remove(us);
	}

	@Override
	public boolean isExists(int uid, String key) {
		UserSetting us=new UserSetting();
		us.setUid(uid);
		us.setKey(key);
		return findAndReturnIsExist(us);
	}

	@Override
	public int insertSetting(int uid, String key, String value) {
		UserSetting us=new UserSetting();
		us.setUid(uid);
		us.setKey(key);
		us.setValue(value);
		return insert(us);
	}

	@Override
	public void updateSetting(int uid, String key, String value) {
		String sql="update `user_setting` set value=:value where `uid`=:uid and `key`=:key";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("value", value);
		param.put("uid", uid);
		param.put("key", key);
		getNamedParameterJdbcTemplate().update(sql, param);
	}
	

}
