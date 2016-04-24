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

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.dao.IEmailSendLogDAO;
import net.duckling.dhome.domain.object.EmailSendLog;

import org.springframework.stereotype.Component;

/**
 * @author lvly
 * @since 2012-10-30
 */
@Component
public class EmailSendLogDAO extends BaseDao implements IEmailSendLogDAO {

	@Override
	public boolean isSended(String email, String type) {
		EmailSendLog log=new EmailSendLog();
		log.setType(type);
		log.setEmail(email);
		return findAndReturnIsExist(log);
	}

	@Override
	public void writeLog(String email, String type, int uid) {
		EmailSendLog log=new EmailSendLog();
		log.setType(type);
		log.setEmail(email);
		log.setUid(uid);
		insert(log);
	}

}
