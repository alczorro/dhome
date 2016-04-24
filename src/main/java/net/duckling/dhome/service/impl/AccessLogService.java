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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.MemCacheKeyGenerator;
import net.duckling.dhome.dao.IAccessLogDAO;
import net.duckling.dhome.domain.object.AccessLog;
import net.duckling.dhome.service.IAccessLogService;
import net.duckling.falcon.api.cache.ICacheService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2012-12-13
 */
@Service
public class AccessLogService implements IAccessLogService{
	private static final int DEFAULT_LIMIT=20;
	
	@Autowired
	private IAccessLogDAO logDAO;
	@Autowired
	private ICacheService cacheService;
	
	@Override
	public void updateAccessLog(int uid, String domain) {
		logDAO.updateAccessLog(uid,domain);
	}
	private List<AccessLog> getCacheVisitors(int uid){
		String key=MemCacheKeyGenerator.getAccessLogListKey(uid);
		List<AccessLog> visitors=(List<AccessLog>)cacheService.get(key);
		if(CommonUtils.isNull(visitors)){
			visitors=getAccessLogs(uid);
		}
		if(visitors==null){
			visitors=new ArrayList<AccessLog>();
		}
		return visitors;
	}
	@Override
	public synchronized void addAccessLog(int visitorUid, int visitedUid, String visitorDomain,String ip) {
		AccessLog log=new AccessLog();
		log.setVisitedUid(visitedUid);
		log.setVisitorDomain(visitorDomain);
		log.setVisitorUid(visitorUid);
		log.setVisitTime(new Timestamp(System.currentTimeMillis()));
		log.setVisitorIp(ip);
		String key=MemCacheKeyGenerator.getAccessLogListKey(visitedUid);
		List<AccessLog> visitors=getCacheVisitors(visitedUid);
		addToList(visitors,log);
		if(!log.isGuest()){
			cacheService.set(key, visitors);
		}
		if(logDAO.addAccessLogs(log)){
			plusOneAccessLogCount(visitedUid);
		}
	}
	private void addToList(List<AccessLog> visitors,AccessLog log){
		boolean isUpdate=false;
		if(log.isSelf()){
			return;
		}
		if(!log.isGuest()){
			for(AccessLog l:visitors){
				if(l.equals(log)){
					l.setAccessCount(l.getAccessCount()+1);
					l.setVisitTime(new Date());
					isUpdate=true;
					break;
				}
			}
			if(!isUpdate){
				visitors.add(log);
			}
			
		}
		sort(visitors);
	}
	private void sort(List<AccessLog> visitors){
		if(CommonUtils.isNull(visitors)){
			return;
		}
		Collections.sort(visitors, logComparator);
		removeMoreThanSize(visitors);
	}
	private static Comparator<AccessLog> logComparator= new Comparator<AccessLog>() {
		@Override
		public int compare(AccessLog o1, AccessLog o2) {
			if(o1==null||o1.getVisitTime()==null){
				return -1;
			}
			if(o2==null||o2.getVisitTime()==null){
				return 1;
			}
			if(o1.equals(o2)){
				return 0;
			}
			return o1.getVisitTime().after(o2.getVisitTime())?-1:1;
		}
	};
	
	/**
	 * 缓存也不能无限加，超过数量，则删掉
	 * */
	private List<AccessLog> removeMoreThanSize(List<AccessLog> logs){
		if(CommonUtils.isNull(logs)){
			return logs;
		}
		for(int i=logs.size()-1;i>=DEFAULT_LIMIT;i--){
			logs.remove(i);
		}
		return logs;
	}

	@Override
	public List<AccessLog> getAccessLogs(int visitedUid) {
		String key=MemCacheKeyGenerator.getAccessLogListKey(visitedUid);
		List<AccessLog> logs=(List<AccessLog>)cacheService.get(key);
		if(CommonUtils.isNull(logs)){
			logs= logDAO.getAccessLogs(visitedUid, DEFAULT_LIMIT);
			cacheService.set(key, logs);
		}
		sort(logs);
		return logs;
		
	}
	@Override
	public int getAccessLogCount(int visitedUid) {
		String key=MemCacheKeyGenerator.getAccessLogCountKey(visitedUid);
		Integer count=(Integer)cacheService.get(key);
		if(count==null||count==0){
			count=logDAO.getAccessLogCount(visitedUid);
			cacheService.set(key, count);
		}
		return count;
	}
	private void plusOneAccessLogCount(int visitedUid){
		String key=MemCacheKeyGenerator.getAccessLogCountKey(visitedUid);
		Integer count=(Integer)cacheService.get(key);
		if(count==null||count==0){
			count=logDAO.getAccessLogCount(visitedUid);
		}else{
			count++;
		}
		cacheService.set(key, count);
		
	}
}
