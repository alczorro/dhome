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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.common.repository.DAOUtils;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.dao.IInstitutionHomeDAO;
import net.duckling.dhome.domain.institution.Institution;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionHomeDiscover;
import net.duckling.dhome.domain.people.SimpleUser;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * 机构主页JDBC实现
 * @author lvly
 * @since 2012-9-17
 */
@Component
public class InstitutionHomeDAO extends BaseDao implements IInstitutionHomeDAO{
	private static final String AUDIT_SQL=" and (`status` ='"+SimpleUser.STATUS_AUDIT_OK+"' or `status` is null) ";
	private static final String COMPLETE_SQL=" and `step`='"+SimpleUser.STEP_COMPLETE+"' ";
	private static final String HOME_VALID_SQL=" and (`home_status`='"+InstitutionHome.STATUS_VALID+"' or `home_status` is null) ";
	private static final String NAME = "name";
	private static final Logger LOG = Logger.getLogger(InstitutionHomeDAO.class);
	
	@Override
	public int createInstitutionDAO(InstitutionHome home) {
		home.setHomeStatus(InstitutionHome.STATUS_VALID);
		return insert(home);
	}

	@Override
	public boolean isExists(int institutitonId) {
		InstitutionHome home=new InstitutionHome();
		home.setInstitutionId(institutitonId);
		return findAndReturnIsExist(home);
	}
	@Override
	public InstitutionHome getInstitutionByDomain(String domain) {
		InstitutionHome home=new InstitutionHome();
		home.setDomain(domain);
		return findAndReturnOnly(home);
	}

	@Override
	public int getInstitutionIdByDomain(String domain) {
		InstitutionHome home = new InstitutionHome();
		home.setDomain(domain);
		List<InstitutionHome> result = findByProperties(home);
		if(CommonUtils.isNull(result)){
			LOG.info("No Institution found in institution_home by domain="+domain);
			return 0;
		}else if(result.size()>1){
			LOG.info("More than one institution be found in institution_home by domain="+domain);
		}
		return CommonUtils.first(result).getInstitutionId();
	}
	@Override
	public void updateInstitutionHome(InstitutionHome home) {
		update(home);
	}
	@Override
	public void updateInstitutionHomeForZeroFieldById(InstitutionHome home){
		String sql = "update institution_home set institution_id=:institutionId, name=:name, introduction=:introduction," +
				"domain=:domain,logo_id=:logoId,creator=:creator,create_time=:createTime,last_editor=:lastEditor," +
				"last_edit_time=:lastEditTime,paper_count=:paperCount,citation_count=:citationCount,hindex=:hIndex," +
				"gindex=:gIndex,home_status=:homeStatus where id=:id";
		Map<String, Object> map = generateMapFromHome(home);
		this.getNamedParameterJdbcTemplate().update(sql, map);
	}
	@Override
	public InstitutionHome getInstitutionByInstitutionId(int institutionId) {
		InstitutionHome home=new InstitutionHome();
		home.setInstitutionId(institutionId);
		return findAndReturnOnly(home,HOME_VALID_SQL);
	}
	@Override
	public List<InstitutionHome> getInstitutionsByLastest(int offset, int size) {
		InstitutionHome home=new InstitutionHome();
		return findByProperties(home,HOME_VALID_SQL+" order by create_time desc"+getLimit(offset, size));

	}
	private String getLimit(int offset,int size){
		return " limit "+offset+","+size;
	}
	@Override
	public List<InstitutionHomeDiscover> getInstitutionsByMemberCount(int offset, int size) {
		DAOUtils<InstitutionHomeDiscover> daoUtils=new DAOUtils<InstitutionHomeDiscover>(InstitutionHomeDiscover.class);
		String sql=" select * ,(select count(*) from institution_people np,simple_user s  where np.institution_id=h.institution_id and  s.id=np.uid "+AUDIT_SQL+COMPLETE_SQL+") as member_count "+
				   " from institution_home h where 1=1 "+
				   HOME_VALID_SQL+
				   " order by member_count desc"+getLimit(offset, size);
		return getJdbcTemplate().query(sql, daoUtils.getRowMapper(null));

	}
	@Override
	public List<InstitutionHomeDiscover> getInstitutionsByPaperCount(int offset, int size) {
		DAOUtils<InstitutionHomeDiscover> daoUtils=new DAOUtils<InstitutionHomeDiscover>(InstitutionHomeDiscover.class);
		String baseSQL="select * from institution_home h where 1=1 "+HOME_VALID_SQL+" order by paper_count desc"+getLimit(offset,size);
		return getJdbcTemplate().query(baseSQL, daoUtils.getRowMapper(null));
		
	}
	@Override
	public List<InstitutionHome> getInstitutionsByKeyword(String keyword, int offset, int size) {
		String sql=" select * from institution_home h "+
				   " where 1=1 " +
				   HOME_VALID_SQL+
				   " and (h.name like :keyword or " +
				   " h.introduction like :keyword) "+
				   getLimit(offset, size);
		DAOUtils<InstitutionHome> daoUtils=new DAOUtils<InstitutionHome>(InstitutionHome.class);
		Map<String,String> map=new HashMap<String,String>();
		map.put("keyword", "%"+keyword+"%");
		return getNamedParameterJdbcTemplate().query(sql, map, daoUtils.getRowMapper(null));
	}
	@Override
	public int getInstitutionsByKeywordCount(String keyword) {
		String sql=" select count(*) from institution_home h "+
				   " where 1=1 " +
				   HOME_VALID_SQL+
				   "and (h.name like :keyword or " +
				   " h.introduction like :keyword) ";
		Map<String,String> map=new HashMap<String,String>();
		map.put("keyword", "%"+keyword+"%");
		return getNamedParameterJdbcTemplate().queryForInt(sql, map);
	}
	@Override
	public boolean isValidHome(String domain) {
		String sql=" select count(*) from institution_home h "+
				   " where h.domain=:domain "+
				   HOME_VALID_SQL;
		Map<String,String> map=new HashMap<String,String>();
		map.put("domain", domain);
		return getNamedParameterJdbcTemplate().queryForInt(sql, map)>0;
	}

	@Override
	public List<Institution> searchForInstitutionBySimilarName(String name) {
		String sql = "select * from institution where id in (select institution_id from institution_name_mapping where alias_name like :name)";
		Map<String, String> map = new HashMap<String, String>();
		map.put(NAME, "%"+name+"%");
		DAOUtils<Institution> daoUtils=new DAOUtils<Institution>(Institution.class);
		return this.getNamedParameterJdbcTemplate().query(sql, map, daoUtils.getRowMapper(null));
	}

	@Override
	public int createAliasInstitutionName(String name, int insId, boolean isFull) {
		String sql = "insert into institution_name_mapping(alias_name,institution_id,is_full) " +
				"select :name,:insId,:isFull from dual where not exists(" +
				"select * from institution_name_mapping where institution_name_mapping.alias_name=" +
				":name and institution_name_mapping.institution_id=:insId)";
		Map<String, String> map = new HashMap<String, String>();
		map.put(NAME, name.toLowerCase());
		map.put("insId", insId+"");
		map.put("isFull", ""+(isFull?1:0));
		return this.getNamedParameterJdbcTemplate().update(sql, map);
	}

	@Override
	public void deleteAliasInstitutionName(String name, int insId) {
		//只有name不是官方机构名时才会删除
		if(!checkTypeOfInstitutionName(name,insId, true)){
			String sql = "delete from institution_name_mapping where alias_name=:name and institution_id=:insId and is_full=0";
			Map<String, String> map = new HashMap<String, String>();
			map.put(NAME, name.toLowerCase());
			map.put("insId", insId+"");
			this.getNamedParameterJdbcTemplate().update(sql, map);
		}
	}

	@Override
	public boolean checkTypeOfInstitutionName(String name, int insId, boolean officalOrCustom) {
		String sql = "select count(*) from institution_name_mapping where alias_name=:name and institution_id=:insId and is_full=1";
		Map<String, String> map = new HashMap<String, String>();
		map.put(NAME, name.toLowerCase());
		map.put("insId", insId+"");
		return this.getNamedParameterJdbcTemplate().queryForInt(sql, map) > 0;
	}
	
	@Override
	public void updateStatisticsForInstitutionHomeHasNoPerson() {
		String select = "select a.id from institution_home a left join institution_people b " +
				"on a.institution_id=b.institution_id where b.institution_id is null";
		List<Integer> result = this.getJdbcTemplate().queryForList(select, Integer.class);
		String sql = "update institution_home set paper_count=0,citation_count=0,hindex=0,gindex=0 where 1=1 " + getCond(result);
		this.getJdbcTemplate().update(sql);
	}

	private Map<String, Object> generateMapFromHome(InstitutionHome home){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", home.getId());
		map.put("institutionId", home.getInstitutionId());
		map.put(NAME, home.getName());
		map.put("introduction", home.getIntroduction());
		map.put("domain", home.getDomain());
		map.put("logoId", home.getLogoId());
		map.put("creator", home.getCreator());
		map.put("createTime", home.getCreateTime());
		map.put("lastEditor", home.getLastEditor());
		map.put("lastEditTime", home.getLastEditTime());
		map.put("paperCount", home.getPaperCount());
		map.put("citationCount", home.getCitationCount());
		map.put("hIndex", home.getHindex());
		map.put("gIndex", home.getGindex());
		map.put("homeStatus", home.getHomeStatus());
		return map;
	}
	
	private String getCond(List<Integer> result){
		StringBuilder sql = new StringBuilder();
		if(null != result && !result.isEmpty()){
			sql.append(" and id in (");
			for(int id : result){
				sql.append(id+",");
			}
			sql.replace(sql.lastIndexOf(","), sql.length(), ")");
		}
		return sql.toString();
	}
}
