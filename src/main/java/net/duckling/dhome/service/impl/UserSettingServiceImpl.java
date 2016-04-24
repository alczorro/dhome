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
package net.duckling.dhome.service.impl;

import net.duckling.dhome.dao.IUserSettingDAO;
import net.duckling.dhome.domain.people.UserSetting;
import net.duckling.dhome.service.IUserSettingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2013-9-5
 */
@Service
public class UserSettingServiceImpl implements IUserSettingService {
	@Autowired
	private IUserSettingDAO settingDAO;
	
	@Override
	public UserSetting getSetting(int uid, String key) {
		UserSetting us=settingDAO.getSetting(uid, key);
		if(us==null){
			us=new UserSetting(); 
		}
		return us;
	}

	@Override
	public void updateSetting(int uid, String key, String value) {
		if(settingDAO.isExists(uid, key)){
			settingDAO.updateSetting(uid, key, value);
		}else{
			settingDAO.insertSetting(uid, key, value);
		}
	}

}
