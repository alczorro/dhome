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

import net.duckling.dhome.dao.IEmailSendLogDAO;
import net.duckling.dhome.service.IEmailSendLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2012-10-30
 */
@Service
public class EmailSendLogService implements IEmailSendLogService{
	
	@Autowired
	private IEmailSendLogDAO emailSendLogDAO;
	@Override
	public boolean isSended(String email, String type) {
		return emailSendLogDAO.isSended(email, type);
	}

	@Override
	public void writeLog(String email, String type, int uid) {
		emailSendLogDAO.writeLog(email, type, uid);
	}
	
}
