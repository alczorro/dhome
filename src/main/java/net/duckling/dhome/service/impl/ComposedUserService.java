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
package net.duckling.dhome.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.dao.IDisciplineDAO;
import net.duckling.dhome.dao.IHomeDAO;
import net.duckling.dhome.dao.ISimpleUserDAO;
import net.duckling.dhome.domain.people.ComposedUser;
import net.duckling.dhome.domain.people.Discipline;
import net.duckling.dhome.domain.people.DisciplineStructrue;
import net.duckling.dhome.domain.people.Interest;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IComposedUserService;
import net.duckling.dhome.service.IInterestService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author zhaojuan
 *
 */
@Service
public class ComposedUserService implements IComposedUserService{
	private static final Logger LOG = Logger.getLogger(ComposedUserService.class);

	@Autowired
	private ISimpleUserDAO simpleUserDAO;
	@Autowired
	private IHomeDAO homeDAO;
	@Autowired
	private IDisciplineDAO disciplineDAO;
	@Autowired
	private IInterestService interestService;
	
	private List<ComposedUser> simpleUser2ComposedUser(List<SimpleUser> simpleUsers){
		return generateComposedUser(simpleUsers);
	}
	@Override
	public List<ComposedUser> getAllComposedUsers(int offset, int size) {
		return simpleUser2ComposedUser(simpleUserDAO.getAllUsers(offset, size));
	}
	public void setHomeDAO(IHomeDAO homeDAO) {
		this.homeDAO = homeDAO;
	}
	public void setSimpleUserDAO(ISimpleUserDAO simpleUserDAO) {
		this.simpleUserDAO = simpleUserDAO;
	}
	
	@Override
	public List<ComposedUser> getLatestComposedUsers(int offset, int size) {
		List<SimpleUser> simpleUsers = simpleUserDAO.getLatestUsers(offset, size);
		return generateComposedUser(simpleUsers);
	}
	@Override
	public DisciplineStructrue getDiscipline() {
		DisciplineStructrue ds=new DisciplineStructrue();
		if(DisciplineStructrue.getInstance()!=null){
			return DisciplineStructrue.getInstance();
		}
		convert2DisciplineStructrue(disciplineDAO.getRoot(),ds);
		DisciplineStructrue.setInstance(ds);
		return ds;
	}
	private void convert2DisciplineStructrue(List<Discipline> sonsList,DisciplineStructrue ds){
		List<DisciplineStructrue> sons=new ArrayList<DisciplineStructrue>();
		if(!CommonUtils.isNull(sonsList)){
			for(Discipline dis:sonsList){
				DisciplineStructrue dss=new DisciplineStructrue(dis);
				sons.add(dss);
				convert2DisciplineStructrue(disciplineDAO.getChild(dis.getId()),dss);
			}
			ds.setSons(sons);
		}
	}
	@Override
	public List<ComposedUser> getComposedUsersByDiscipline(int first,int second,int offset,int size) {
		List<SimpleUser> sus=simpleUserDAO.getSimpleUserByDiscipline(first,second,offset,size);
		return simpleUser2ComposedUser(sus);
	}
	public List<ComposedUser> getComposedUsersByInterest(String keyword, int offset, int size) {
		String finalWord=keyword;
		if(!CommonUtils.isNull(finalWord)){
			finalWord=finalWord.replace("\\", "\\\\");
		}
		List<SimpleUser> sus=simpleUserDAO.getSimpleUserByInterest(finalWord,offset,size);
		return simpleUser2ComposedUser(sus);
	};
	
	@Override
	public List<ComposedUser> searchComposedUsers(String keyword, int offset,
			int size) {
		List<SimpleUser> simpleUsers = simpleUserDAO.searchUsers(keyword, offset, size);
		return generateComposedUser(simpleUsers);
	}
	private List<ComposedUser> generateComposedUser(List<SimpleUser> simpleUsers){
		List<ComposedUser> composedUsers = new ArrayList<ComposedUser>();
		if(null != simpleUsers){
			for(SimpleUser su:simpleUsers){		
				ComposedUser cu = new ComposedUser();
				cu.setSimpleUser(su);
				String url = homeDAO.getUrlFromUid(su.getId());
				cu.setUrl(url);
				cu.setInterest(getEncodeInterest(interestService.getInterest(su.getId())));
				composedUsers.add(cu);
			}
		}
		return composedUsers;
	}
	private List<Interest> getEncodeInterest(List<Interest> list){
		if(CommonUtils.isNull(list)){
			return null;
		}
		for(Interest ins:list){
			try {
				ins.setKeyword(URLEncoder.encode(URLEncoder.encode(ins.getKeyword(), "utf8"), "utf8"));
			} catch (UnsupportedEncodingException e) {
				LOG.error("keyword:"+ins.getKeyword(),e);
			}
		}
		return list;
	}

	@Override
	public int getSearchComposedUsersCount(String keyword) {
		
		return simpleUserDAO.getSearchComposedUserCount(keyword);
	}
}
