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
import net.duckling.dhome.dao.IEducationDAO;
import net.duckling.dhome.domain.people.Education;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
/**教育经历的jdbc实现*/
@Component
public class EducationDAO extends BaseDao implements IEducationDAO{
	
	private static final Logger LOG = Logger.getLogger(EducationDAO.class);
	
	@Override
	public int createEducation(Education edu) {
		return insert(edu);
	}

	@Override
	public List<Education> getEdusByUID(int uid) {
		Education edu=new Education();
		edu.setUid(uid);
		return findByProperties(edu,"order by end_time desc,id desc");
	}
	
	@Override
	public Education getEducation(int id){
		Education edu=new Education();
		edu.setId(id);
		List<Education> list = findByProperties(edu);
		if(null == list || list.isEmpty()){
			return null;
		}else if(list.size()>1){
			LOG.error("there exist more than one object while query for Education by id="+id);
		}
		return list.get(0);
	}

	@Override
	public boolean updateEducationById(Education edu) {
		String sql = "update education set uid=:uid,insitution_id=:institutionId,degree=:degree," +
				"department=:department,begin_time=:beginTime,end_time=:endTime, " +
				"alias_institution_name=:aliasInstitutionName,degree2=:edu_degree,graduation_project=:graduation_project, graduation_project_cid=:graduation_project_cid,tutor=:tutor where id=:id";
		Map<String, Object> map = getMap(edu);
		int result = this.getNamedParameterJdbcTemplate().update(sql, map);
		return result>0;
	}

	@Override
	public void deleteEducation(int id) {
		String sql = "delete from education where id="+id;
		this.getJdbcTemplate().update(sql);
	}
	@Override
	public int getCitedEducationCount(int institutionId, int uid) {
		Education edu=new Education();
		edu.setUid(uid);
		edu.setInsitutionId(institutionId);
		return getCount(edu);
	}

	@Override
	public List<Education> getEducationWithZeroInstitutionId() {
		String sql = "select * from education where insitution_id = 0 order by begin_time desc";
		DAOUtils<Education> utils = new DAOUtils<Education>(Education.class);
		return this.getJdbcTemplate().query(sql, utils.getRowMapper(null));
	}

	@Override
	public List<Education> getEducationWithNonZeroInstitutionId() {
		String sql = "select * from education where insitution_id >0 order by convert(alias_institution_name using gb2312) asc";
		DAOUtils<Education> dao = new DAOUtils<Education>(Education.class);
		return this.getJdbcTemplate().query(sql, dao.getRowMapper(null));
	}
	
	private Map<String, Object> getMap(Education edu){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("id", edu.getId());
		map.put("uid", edu.getUid());
		map.put("institutionId", edu.getInsitutionId());
		map.put("department", edu.getDepartment());
		map.put("degree", edu.getDegree());
		map.put("beginTime", edu.getBeginTime());
		map.put("endTime", edu.getEndTime());
		map.put("aliasInstitutionName", edu.getAliasInstitutionName());
		map.put("edu_degree", edu.getDegree2());
		map.put("graduation_project", edu.getGraduationProject());
		map.put("graduation_project_cid", edu.getGraduationProjectCid());
		map.put("tutor", edu.getTutor());
		return map;
	}
}
