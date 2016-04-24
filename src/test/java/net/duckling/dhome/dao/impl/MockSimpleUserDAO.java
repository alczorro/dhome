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
import java.util.List;
import java.util.Map;

import net.duckling.dhome.dao.ISimpleUserDAO;
import net.duckling.dhome.domain.people.SimpleUser;

public class MockSimpleUserDAO implements ISimpleUserDAO{
	@Override
	public int updateSimpleUserLastEditTimeByUid(int uid) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public List<SimpleUser> getSimpleUserByInterest(String keyword, int offset, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, SimpleUser> susers;
	@Override
	public int getSearchComposedUserCount(String keyword) {
		// TODO Auto-generated method stub
		return 0;
	}
	public MockSimpleUserDAO(){
		susers = new HashMap<String, SimpleUser>();
	}
	
	@Override
	public SimpleUser getUser(String email) {
		return susers.get(email);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int updateAccount(SimpleUser user) {
		SimpleUser temp = susers.get(user.getEmail());
		if(null == temp){
			temp = new SimpleUser();
		}
		updateUser(temp, user);
		susers.put(user.getEmail(), user);
		return user.getId();
	}

	@Override
	public int registAccount(SimpleUser user) {
		user.setId(susers.size()+1);
		susers.put(user.getEmail(), user);
		return susers.size();
	}

	@Override
	public boolean isEmailUsed(String email) {
		return susers.containsKey(email);
	}

	
	private void updateUser(SimpleUser old, SimpleUser newU){
		newU.setEmail(old.getEmail());
		newU.setEnName(old.getEnName());
		newU.setId(old.getId());
		newU.setImage(old.getImage());
		newU.setPinyin(old.getPinyin());
		newU.setSalutation(old.getSalutation());
		newU.setZhName(old.getZhName());
	}

	@Override
	public SimpleUser getUser(int uid) {
		for(Map.Entry<String, SimpleUser> entry : susers.entrySet()){
			SimpleUser su = entry.getValue();
			if(su.getId() == uid){
				return su;
			}
		}
		return null;
	}

	@Override
	public List<SimpleUser> getLatestUsers(int offset, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SimpleUser> searchUsers(String keyword, int offset, int size) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<SimpleUser> getSimpleUserByDiscipline(int first, int second, int offset, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SimpleUser> getAllUsers(int offset, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SimpleUser> getAllUsers(String status, String keyword,
			int offset, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SimpleUser> getUsers(List<Integer> uids) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SimpleUser getSimpleUserByImgId(int imgId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<SimpleUser> getUsersByEmails(List<String> email) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<SimpleUser> getAllUser() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void updateSimpleUserEmailByUid(int uid, String newPw) {
		// TODO Auto-generated method stub
		
	}
	

}
