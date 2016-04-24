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

import java.util.List;

import net.duckling.dhome.dao.IAccessLogDAO;
import net.duckling.dhome.domain.object.AccessLog;

/**
 * @author lvly
 * @since 2012-12-27
 */
public class StubAccessLogDAO implements IAccessLogDAO{

	@Override
	public boolean addAccessLogs(AccessLog log){
		return true;
	}

	@Override
	public List<AccessLog> getAccessLogs(int visitedUid, int limit) {
		return null;
	}

	@Override
	public int getAccessLogCount(int visitedUid) {
		return visitedUid;
	}
	@Override
	public void updateAccessLog(int uid, String domain) {
		// TODO Auto-generated method stub
		
	}

}
