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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.dao.IAccessLogDAO;
import net.duckling.dhome.domain.object.AccessLog;

import org.springframework.stereotype.Component;

/**
 * @author lvly
 * @since 2012-12-13
 */
@Component
public class AccessLogDAO extends BaseDao implements IAccessLogDAO{
	private static final String ORDER_BY=" order by visit_time desc "; 
	/**访问间隔记录时间暂定为20分钟*/
	private static final long LIMIT_TIME=20*60*1000;
	@Override
	public void updateAccessLog(int uid, String domain) {
		String updateSql="update `access_log` set `visitor_domain`=:visitorDomain where visitor_uid=:visitorUid";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("visitorDomain", domain);
		param.put("visitorUid", uid);
		getNamedParameterJdbcTemplate().update(updateSql, param);
		
	}
	@Override
	public boolean addAccessLogs(final AccessLog accessLog) {
		AccessLog log=null;
		String ip=accessLog.getVisitorIp();
		accessLog.setAccessCount(0);
		accessLog.setVisitTime(null);
		if(!accessLog.isGuest()){
			accessLog.setVisitorIp(null);
		}
		Date now=new Date();
		log=findAndReturnOnly(accessLog,ORDER_BY);
		if(log==null||log.getVisitTime()==null){
			accessLog.setVisitTime(now);
			accessLog.setAccessCount(1);
			accessLog.setVisitorIp(ip);
			insert(accessLog);
		}else{
			if(now.getTime()-log.getVisitTime().getTime()>LIMIT_TIME){
				log.setVisitTime(now);
				log.setVisitorIp(ip);
				log.setAccessCount(log.getAccessCount()+1);
				update(log);
			}else{
				return false;
			}
			
		}
		return true;
		
	}

	@Override
	public List<AccessLog> getAccessLogs(int visitedUid, int limit) {
		AccessLog log=new AccessLog();
		log.setVisitedUid(visitedUid);
		return findByProperties(log,  ORDER_BY+" limit 0,"+limit);
	}
	@Override
	public int getAccessLogCount(int visitedUid) {
		String sql="select sum(access_count) from access_log where visited_uid=:visitedUid and visited_uid!=visitor_uid";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("visitedUid", visitedUid);
		return getNamedParameterJdbcTemplate().queryForInt(sql, param);
		
	}
	

}
