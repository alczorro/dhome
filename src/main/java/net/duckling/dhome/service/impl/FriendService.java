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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.domain.institution.Member;
import net.duckling.dhome.domain.object.AccessLog;
import net.duckling.dhome.domain.people.DetailedUser;
import net.duckling.dhome.domain.people.Education;
import net.duckling.dhome.domain.people.Friend;
import net.duckling.dhome.domain.people.Interest;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.Work;
import net.duckling.dhome.service.IAccessLogService;
import net.duckling.dhome.service.IEducationService;
import net.duckling.dhome.service.IFriendService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionPeopleService;
import net.duckling.dhome.service.IInterestService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.IWorkService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2012-10-11
 */
@Service
public class FriendService implements IFriendService {
	@Autowired
	private IWorkService workService;
	@Autowired
	private IEducationService eduService;
	@Autowired
	private IInstitutionPeopleService peopleService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IHomeService homeService;
	@Autowired
	private IInterestService interestService;
	@Autowired
	private IAccessLogService logService;
	
	private void addWorkFriend(List<Integer> distinctUserId,List<Friend> result,int userId,int offset,int size){
		List<Work> works = workService.getWorksByUID(userId);
		if (!CommonUtils.isNull(works)) {
			for (Work work : works) {
				if(work.getInstitutionId()==0){
					continue;
				}
				List<Member> members = peopleService.getPeoplesByInstitutionId(work.getInstitutionId(), offset, size);
				if (CommonUtils.isNull(members)) {
					continue;
				}
				for (Member member : members) {
					if (result.size() == size) {
						return;
					}
					if (distinctUserId.contains(member.getId())) {
						continue;
					}
					distinctUserId.add(member.getId());
					result.add(convertMember2Friend(member));
				}
			}
		}
	}
	private void addEduFriends(List<Integer> distinctUserId,List<Friend> result,int userId,int offset,int size){
		List<Education> edus = eduService.getEducationsByUid(userId);
		if (!CommonUtils.isNull(edus)) {
			for (Education edu : edus) {
				if(edu.getInsitutionId()==0){
					continue;
				}
				List<Member> members = peopleService.getPeoplesByInstitutionId(edu.getInsitutionId(), offset, size);
				if (CommonUtils.isNull(members)) {
					continue;
				}
				for (Member member : members) {
					if (result.size() == size) {
						return;
					}
					if (distinctUserId.contains(member.getId())) {
						continue;
					}
					distinctUserId.add(member.getId());
					result.add(convertMember2Friend(member));
				}
			}
		}
	}
	@Override
	public List<Friend> getFriendsByInstitution(int userId, int offset, int size) {
		List<Friend> result = new ArrayList<Friend>();
		List<Integer> distinctUserId = new ArrayList<Integer>();
		distinctUserId.add(userId);
		addWorkFriend(distinctUserId,result,userId,offset,size);
		addEduFriends(distinctUserId,result,userId,offset,size);
		return result;
	}

	private List<Friend> convertSimpleUserList2FriendList(List<SimpleUser> users,int userId) {
		if (CommonUtils.isNull(users)) {
			return new ArrayList<Friend>();
		}
		List<Friend> friends = new ArrayList<Friend>();
		int index=0;
		for (SimpleUser s : users) {
			if(s.getId()==userId){
				continue;
			}
			Friend f = new Friend();
			f.setSimpleUser(s);
			f.setDomain(homeService.getDomain(s.getId()));
			friends.add(f);
			if(++index==20){
				return friends;
			}
		}
		return friends;
	}

	private Friend convertMember2Friend(Member member) {
		Friend friend = new Friend();
		SimpleUser user = new SimpleUser();
		user.setAuditPropose(member.getAuditPropose());
		user.setEmail(member.getEmail());
		user.setEnName(member.getEnName());
		user.setId(member.getId());
		user.setImage(member.getImage());
		user.setIsAdmin(member.getIsAdmin());
		user.setPinyin(member.getPinyin());
		user.setSalutation(member.getSalutation());
		user.setStatus(member.getStatus());
		user.setStep(member.getStep());
		user.setZhName(member.getZhName());
		friend.setSimpleUser(user);
		friend.setDomain(member.getDomain());
		return friend;
	}

	@Override
	public List<Friend> getFriendsByDiscipline(int userId, int offset, int size) {
		DetailedUser dUser = userService.getDetailedUser(userId);
		if(dUser==null){
			return null;
		}
		List<SimpleUser> result = userService.getSimpleUserByDiscipline(dUser.getFirstClassDiscipline(),dUser.getSecondClassDiscipline(), offset, size);
		return convertSimpleUserList2FriendList(result,userId);
	}
	@Override
	public List<Friend> getFriendsByAccess(int userId) {
		Collection<AccessLog> result=logService.getAccessLogs(userId);
		return convertAccessLogList2FriendList(result);
	}
	

	/**
	 * @param result
	 * @return
	 */
	private List<Friend> convertAccessLogList2FriendList(Collection<AccessLog> result) {
		if(result!=null&&!result.isEmpty()){
			List<Friend> friends=new ArrayList<Friend>();
			for(AccessLog log:result){
				if(log.getVisitorUid()<0||log.getVisitorUid()==log.getVisitedUid()){
					continue;
				}
				Friend f=new Friend();
				f.setDomain(log.getVisitorDomain());
				f.setSimpleUser(userService.getSimpleUserByUid(log.getVisitorUid()));
				friends.add(f);
			}
			return friends;
		}
		return null;
	}
	@Override
	public List<Friend> getFriendsByInterest(int userId, int offset, int size) {
		List<Interest> interests=interestService.getInterest(userId);
		if(CommonUtils.isNull(interests)){
			return null;
		}
		List<Integer> distinctUserId = new ArrayList<Integer>();
		distinctUserId.add(userId);
		List<Friend> result=new ArrayList<Friend>();
		for(Interest interest:interests){
			List<SimpleUser> users=userService.getSimpleUserByInterest(interest.getKeyword(), offset, size);
			if(CommonUtils.isNull(users)){
				continue;
			}
			for(Iterator<SimpleUser> it=users.iterator();it.hasNext();){
				SimpleUser u=it.next();
				if(distinctUserId.contains(u.getId())){
					it.remove();
					continue;
				}else{
					distinctUserId.add(u.getId());
				}
			}
			result.addAll(convertSimpleUserList2FriendList(users,userId));
		}
		if(result.size()>size){
			return result.subList(0, size);
		}
		else{
			return result;
		}
	}

}
