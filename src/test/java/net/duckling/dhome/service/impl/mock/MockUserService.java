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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.vlabs.umt.oauth.AccessToken;
import cn.vlabs.umt.oauth.common.exception.OAuthProblemException;

import net.duckling.dhome.domain.people.DetailedUser;
import net.duckling.dhome.domain.people.Discipline;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IUserService;

public class MockUserService implements IUserService {
	@Override
	public void updateSimpleUserLastEditTimeByUid(int uid) {
		// TODO Auto-generated method stub
		
	}
	private Map<String, SimpleUser> users;
	private Map<Integer, DetailedUser> dUsers;

	@Override
	public List<SimpleUser> getSimpleUserByDiscipline(int first, int second, int offset, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SimpleUser> getSimpleUserByInterest(String keyword, int offset, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateSimpleUserStatusByUid(SimpleUser su) {
		// TODO Auto-generated method stub
		return false;
	}

	public MockUserService() {
		users = new HashMap<String, SimpleUser>();
		SimpleUser su1 = new SimpleUser();
		// su1.setDomain("xiaoming");
		su1.setEmail("xiaoming@163.com");
		users.put(su1.getEmail(), su1);

		SimpleUser su2 = new SimpleUser();
		su2.setEmail("yxp@163.com");
		su2.setId(108);
		users.put(su2.getEmail(), su2);

		dUsers = new HashMap<Integer, DetailedUser>();
		DetailedUser du1 = new DetailedUser();
		du1.setUid(1);
		dUsers.put(du1.getUid(), du1);

		DetailedUser du2 = new DetailedUser();
		du2.setUid(108);
		du2.setFirstClassDiscipline(1);
		du2.setSecondClassDiscipline(11);
		du2.setIntroduction("introduction");
		dUsers.put(du2.getUid(), du2);
	}

	@Override
	public int getUserCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SimpleUser getSimpleUser(String email) {
		return users.get(email);
	}

	@Override
	public DetailedUser getDetailedUser(int uid) {
		return dUsers.get(uid);
	}

	@Override
	public boolean updateSimpleUserByUid(SimpleUser su) {
		int uid = su.getId();
		for (Map.Entry<String, SimpleUser> entry : users.entrySet()) {
			SimpleUser temp = entry.getValue();
			if (temp.getId() == uid) {
				temp.setZhName(su.getZhName());
				temp.setPinyin(su.getPinyin());
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean updateDetailedUserByUid(DetailedUser du) {
		int uid = du.getUid();
		DetailedUser temp = dUsers.get(uid);
		if (null != temp) {
			temp.setFirstClassDiscipline(du.getFirstClassDiscipline());
			temp.setSecondClassDiscipline(du.getSecondClassDiscipline());
			temp.setIntroduction(du.getIntroduction());
			dUsers.put(uid, temp);
			return true;
		}
		return false;
	}

	@Override
	public SimpleUser getSimpleUserByUid(int uid) {
		if (uid == 12) {
			SimpleUser su = new SimpleUser();
			su.setId(12);
			su.setZhName("test");
			return su;
		}
		for (Map.Entry<String, SimpleUser> entry : users.entrySet()) {
			SimpleUser temp = entry.getValue();
			if (temp.getId() == uid) {
				return temp;
			}
		}
		return null;
	}

	@Override
	public SimpleUser getSimpleUserByImgId(int imgId) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.duckling.dhome.service.IUserService#getRootDiscipline()
	 */
	@Override
	public List<Discipline> getRootDiscipline() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.duckling.dhome.service.IUserService#getChildDiscipline(int)
	 */
	@Override
	public List<Discipline> getChildDiscipline(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.duckling.dhome.service.IUserService#getDisciplineName(int)
	 */
	@Override
	public String getDisciplineName(int id) {
		switch (id) {
		case 1:
			return "第一学科";
		case 11:
			return "第二学科";
		default:
			return null;
		}
	}

	@Override
	public boolean isUmtUser(String email) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SimpleUser> getAllUsers(String status, String keyword, int offset, int size) {
		List<SimpleUser> su = new ArrayList<SimpleUser>();
		if (offset <= 0) {
			su.addAll(users.values());
		}
		return su;
	}

	@Override
	public List<SimpleUser> getSimpleUsersByUid(List<Integer> uids) {
		List<SimpleUser> result = new ArrayList<SimpleUser>();
		if (null != uids && !uids.isEmpty()) {
			for (int uid : uids) {
				SimpleUser su = new SimpleUser();
				su.setId(uid);
				su.setZhName("user" + uid);
				result.add(su);
			}
		}
		return result;
	}

	@Override
	public int create(int uid, String module, int step) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStep(int uid, String module) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateStep(int uid, String module, int step) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<SimpleUser> getUserByEmails(List<String> email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SimpleUser getSimpleUser(int uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SimpleUser> getAllUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCorrectUserInfo(String uid, String password) {
		// TODO Auto-generated method stub
		return false;
	}

//	@Override
//	public AccessToken umtPasswordAccessToken(String userName, String password)
//			throws OAuthProblemException {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
