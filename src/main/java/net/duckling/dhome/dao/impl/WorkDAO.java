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

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.common.repository.DAOUtils;
import net.duckling.dhome.dao.IWorkDAO;
import net.duckling.dhome.domain.people.Work;

import org.springframework.stereotype.Component;

/**
 * IWorkDAO的jdbc实现类
 * 
 * @author cerc
 * 
 */
@Component
public class WorkDAO extends BaseDao implements IWorkDAO {
	@Override
	public int createWork(Work work) {
		return insert(work);
	}

	@Override
	public List<Work> getWorksByUID(int uid) {
		Work work=new Work();
		work.setUid(uid);
		return findByProperties(work,"order by end_time desc,id desc");
	}
	
	@Override
	public Work getWork(int id){
		Work work = new Work();
		work.setId(id);
		return findAndReturnOnly(work);
	}

	@Override
	public boolean updateWorkById(Work work) {
		String sql = "update work set uid=:uid,institution_id=:institutionId,department=:department," +
				"position=:position,begin_time=:beginTime,end_time=:endTime,alias_institution_name=:aliasInstitutionName" +
				" where id=:id";
		Map<String, Object> map = getMap(work);
		int result = this.getNamedParameterJdbcTemplate().update(sql, map);
		return result>0;
	}

	@Override
	public void deleteWork(int id) {
		String sql = "delete from work where id="+id;
		this.getJdbcTemplate().update(sql);
	}
	@Override
	public int getCitedWorkCount(int institutionId, int uid) {
		Work work=new Work();
		work.setInstitutionId(institutionId);
		work.setUid(uid);
		return getCount(work);
	}

	@Override
	public List<Work> getWorkWithZeroInstitutionId() {
		String sql = "select * from work where institution_id = 0 order by begin_time desc";
		DAOUtils<Work> utils = new DAOUtils<Work>(Work.class);
		return this.getJdbcTemplate().query(sql, utils.getRowMapper(null));
	}

	@Override
	public List<Work> getWorkWithNonZeroInstitutionId() {
		String sql = "select * from work where institution_id >0 order by convert(alias_institution_name using gb2312) asc";
		DAOUtils<Work> dao = new DAOUtils<Work>(Work.class);
		return this.getJdbcTemplate().query(sql, dao.getRowMapper(null));
	}
	
	private Map<String, Object> getMap(Work work){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("id", work.getId());
		map.put("uid", work.getUid());
		map.put("institutionId", work.getInstitutionId());
		map.put("department", work.getDepartment());
		map.put("position", work.getPosition());
		map.put("beginTime", work.getBeginTime());
		map.put("endTime", work.getEndTime());
		map.put("aliasInstitutionName", work.getAliasInstitutionName());
		return map;
	}
}
