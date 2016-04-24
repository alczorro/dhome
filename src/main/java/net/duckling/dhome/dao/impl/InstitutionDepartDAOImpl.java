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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.common.util.CommonUtils;
import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.dao.IInstitutionDepartDAO;
import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.vmt.api.domain.VmtDepart;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class InstitutionDepartDAOImpl extends BaseDao implements IInstitutionDepartDAO {
	private static final RowMapper<InstitutionDepartment> rowMapper=new RowMapper<InstitutionDepartment>(){
		@Override
		public InstitutionDepartment mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionDepartment ins=new InstitutionDepartment();
			ins.setInstitutionId(rs.getInt("institution_id"));
			ins.setId(rs.getInt("id"));
			ins.setName(rs.getString("depart_name"));
			ins.setParentId(rs.getInt("parent_id"));
			ins.setSymbol(rs.getString("depart_symbol"));
			ins.setListRank(rs.getInt("list_rank"));
			ins.setDisplay(rs.getString("display"));
			ins.setShortName(rs.getString("depart_name_short"));
			return ins;
		}
	};
	@Override
	public void removeByInstitutionId(int insId) {
		String sql="delete from institution_depart where institution_id=:insId";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("insId", insId);
		getNamedParameterJdbcTemplate().update(sql, param);
	}
	@Override
	public int insertDepart(InstitutionDepartment depart) {
		String sql="insert into institution_depart( "
				+"institution_id,"
				+"depart_symbol,"
				+"depart_name,"
				+"list_rank,"
				+"display,"
				+"parent_id) "
				+"values(:institution_id,:depart_symbol,:depart_name,:list_rank,:display,:parent_id)";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("institution_id", depart.getInstitutionId());
		param.put("depart_symbol", depart.getSymbol());
		param.put("depart_name",depart.getName());
		param.put("parent_id",depart.getParentId());
		param.put("display",depart.getDisplay());
		param.put("list_rank", depart.getListRank());
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getNamedParameterJdbcTemplate().update(sql, new MapSqlParameterSource(param), keyHolder);
		depart.setId(keyHolder.getKey().intValue());
	    return depart.getId();
	}
	@Override
	public InstitutionDepartment getDepartBySymbol(int institutionId,
			String symbol) {
		String sql="select * from institution_depart where institution_id=:insId and depart_symbol=:deptSymbol limit 1";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("deptSymbol", symbol);
		paramMap.put("insId",institutionId);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql, paramMap,rowMapper));
	}
	@Override
	public void updateDept(VmtDepart dept, int institutionId) {
		String sql="update  institution_depart set depart_name=:deptName,list_rank=:listRank,display=:display where depart_symbol=:deptSymbol and institution_id=:insId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("deptName", dept.getName());
		paramMap.put("deptSymbol", dept.getSymbol());
		paramMap.put("insId", institutionId);
		paramMap.put("listRank", dept.getListRank());
		paramMap.put("display", dept.isVisible()?InstitutionDepartment.DISPLAY_SHOW:InstitutionDepartment.DISPLAY_HIDE);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}
	
	@Override
	public void batchUpdate(final int institutionId,final List<InstitutionDepartment> datas){
		String sql="replace into institution_depart(`id`,`institution_id`,`depart_symbol`,`depart_name`,`parent_id`,`display`,`list_rank`,`depart_name_short`) values(?,?,?,?,?,?,?,?)";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
			public int getBatchSize() {
				return datas.size();
			}
			public void setValues(PreparedStatement pst, int index)
					throws SQLException {
				int i = 0;
				InstitutionDepartment dept = datas.get(index);
				pst.setInt(++i, datas.get(index).getId());
				pst.setInt(++i,institutionId);
				pst.setString(++i, dept.getSymbol());
				pst.setString(++i, dept.getName());
				pst.setInt(++i, dept.getParentId());
				pst.setString(++i, dept.getDisplay());
				pst.setInt(++i, index + 1);
				pst.setString(++i, dept.getShortName());
			}
		});
	}
	
	@Override
	public void updateDeptShortName(int id,String shortName,int listRank){
		String sql="update  institution_depart set depart_name_short=:shortName,list_rank=:listRank where id=:id";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("shortName", shortName);
		paramMap.put("listRank", listRank);
		paramMap.put("id", id);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}
	
	@Override
	public void moveDepart(int deptId, int toDeptId) {
		String sql="update  institution_depart set parent_id=:parentId where id=:id";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("id",deptId);
		paramMap.put("parentId", toDeptId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}
	@Override
	public void parentChanged(int orgParentId, int newId) {
		String sql="update institution_depart set parent_id=:newId where parent_id=:orgParentId";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("newId", newId);
		paramMap.put("orgParentId", orgParentId);
		getNamedParameterJdbcTemplate().update(sql, paramMap);
	}
	@Override
	public List<InstitutionDepartment> getDepartsByParentIds(int pid) {
		String sql="select * from institution_depart where parent_id=:id";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("id", pid);
		return getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper);
	}
	@Override
	public void removeDepartByIds(final List<Integer> ids) {
		String sql="delete from institution_depart where id=? ";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return ids.size();
					}

					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i,ids.get(index));
					}
				});
	}
	@Override
	public void removeDepartByIds(int id) {
		List<Integer> ids=new ArrayList<Integer>();
		ids.add(id);
		removeDepartByIds(ids);
		
	}
	@Override
	public void displayChanged(final int institutionId, final List<Integer> sonDeptId,
			final String display) {
		String sql="update institution_depart set display=? where id=? and institution_id=?";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return sonDeptId.size();
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setString(++i, display);
						pst.setInt(++i, sonDeptId.get(index));
						pst.setInt(++i, institutionId);
					}

				});
	}
	@Override
	public List<InstitutionDepartment> getDepartsByIds(List<Integer> deptId) {
		StringBuffer sql=new StringBuffer();
		sql.append("select * from institution_depart where id in(");
		int index=0;
		Map<String,Object> map=new HashMap<String,Object>();
		for(int id:deptId){
			sql.append(":id"+index).append(",");
			map.put("id"+index,id);
			index++;
		}
		sql.deleteCharAt(sql.lastIndexOf(",")).append(")");
		return getNamedParameterJdbcTemplate().query(sql.toString(), map, rowMapper);
	}
	@Override
	public InstitutionDepartment getDepartById(int departId) {
		String sql="select * from institution_depart where id=:id";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("id",departId);
		return CommonUtils.first(getNamedParameterJdbcTemplate().query(sql.toString(), paramMap, rowMapper));
	}
	@Override
	public List<InstitutionDepartment> getDepartsByInsId(int insId) {
		String sql="select * from institution_depart where institution_id=:insId order by list_rank ASC";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId",insId);
		return getNamedParameterJdbcTemplate().query(sql.toString(), paramMap, rowMapper);
	}
	
	@Override
	public List<InstitutionDepartment> getDepartsOrderPinyin(int insId) {
		String sql="select * from institution_depart where institution_id=:insId order by convert(depart_name_short using gbk) collate gbk_chinese_ci ASC";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("insId",insId);
		return getNamedParameterJdbcTemplate().query(sql.toString(), paramMap, rowMapper);
	}
}
