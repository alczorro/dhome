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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.dao.IInstitutionBackendPatentFileDAO;
import net.duckling.dhome.domain.institution.InstitutionAttachment;

@Repository
public class InstitutionBackendPatentFileDAOImpl extends BaseDao implements
		IInstitutionBackendPatentFileDAO {

	private RowMapper<InstitutionAttachment> rowMapper=new RowMapper<InstitutionAttachment>() {
		@Override
		public InstitutionAttachment mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InstitutionAttachment au=new InstitutionAttachment();
			au.setId(rs.getInt("id"));
			au.setClbId(rs.getInt("clb_id"));
			au.setFileName(rs.getString("file_name"));
			au.setObjId(rs.getInt("obj_id"));
			au.setType(rs.getInt("obj_type"));
			return au;
		} 
	};
	@Override
	public List<InstitutionAttachment> getAttachment(int objType, int objId) {
		String sql="select * from attachments where obj_type=:obj_type and obj_id=:obj_id";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("obj_type", objType);
		param.put("obj_id", objId);
		return getNamedParameterJdbcTemplate().query(sql, param, rowMapper);
	}

	@Override
	public void insertAttachments(final int objId, final int[] clbIds,
			final String[] fileNames) {
		String sql="insert into attachments(`obj_id`,`obj_type`,`clb_id`,`file_name`) values(?,?,?,?)";
		getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return clbIds.length;
					}
					public void setValues(PreparedStatement pst, int index)
							throws SQLException {
						int i = 0;
						pst.setInt(++i, objId);
						pst.setInt(++i, 2);
						pst.setInt(++i, clbIds[index]);
						pst.setString(++i, fileNames[index]);
					}
				});		

	}

	@Override
	public void deleteAttachments(int objId) {
		String sql="delete from attachments where obj_id=:obj_id and obj_type=:obj_type";
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("obj_id", objId);
		paramMap.put("obj_type", 2);
		getNamedParameterJdbcTemplate().update(sql, paramMap);		

	}

}
