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
package net.duckling.dhome.service.impl.mock;

import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IMailService;

public class MockMailService implements IMailService {
@Override
public void sendEmail(String title, String content, String[] address) {
	// TODO Auto-generated method stub
	
}
	@Override
	public void sendRegistMail(String tmpType, SimpleUser user, String domain) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendRegistMailToAdmin(String tmpType, SimpleUser user,
			String domain) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendCheckHomePageMail(String tmpType, SimpleUser user,
			String domain) {
		// TODO Auto-generated method stub

	}

}
