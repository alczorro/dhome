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

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.common.repository.EField;
import net.duckling.dhome.common.repository.ETable;
import net.duckling.dhome.common.util.EnumUtils;
import net.duckling.dhome.dao.IMenuItemDAO;
import net.duckling.dhome.domain.people.MenuItem;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
/**
 * 菜单左栏项的jdbc实现
 * */
@Component
public class MenuItemDAO extends BaseDao implements IMenuItemDAO {
	private static final String TABLE = " menu_item ";

	private static final String UPDATED_VALUES = EnumUtils
			.getUpdateFields(ETable.Menu_Item);

	private static MenuItem readMenuItem(ResultSet rs) throws SQLException{
		MenuItem menuItem = new MenuItem();
		menuItem.setId(rs.getInt(EField.MenuItem.ID.toString()));
		menuItem.setUid(rs.getInt(EField.MenuItem.UID.toString()));
		menuItem.setTitle(rs.getString(EField.MenuItem.TITLE.toString()));
		menuItem.setSequence(rs.getInt(EField.MenuItem.SEQUENCE.toString()));
		menuItem.setStatus(rs.getInt(EField.MenuItem.STATUS.toString()));
		menuItem.setUrl(rs.getString(EField.MenuItem.URL.toString()));
		return menuItem;
	}
		
	private static RowMapper<MenuItem> rowMapper = new RowMapper<MenuItem>() {
		@Override
		public MenuItem mapRow(ResultSet rs, int arg1) throws SQLException {
			return readMenuItem(rs);
		}
	};

	private List<MenuItem> findByProperty(EField field, Object value) {
		String sql = "select * from "+TABLE+"where "+ EnumUtils.getKeyValuePhrase(field)+" and status!="+MenuItem.MENU_ITEM_STATUS_REMOVED;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(field.toString(), value);
		return getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper);
	}

	@Override
	public int addMenuItem(MenuItem menuItem) {
		int sequence = getMaxSequence(menuItem.getUid())+1;
		menuItem.setSequence(sequence);
		return insert(menuItem);	
	}

	@Override
	public List<MenuItem> getMenuItemsByUId(int uid) {
		return findByProperty(EField.MenuItem.UID, uid);
	}
	@Override
	public MenuItem getMenuItemById(int id) {
			 String sql = "select * from "+TABLE+" where id = ?";
				return getJdbcTemplate().query(sql,new Object[] { id },new MenuItemExtractor());
	}
	public static class MenuItemExtractor implements ResultSetExtractor<MenuItem>{
		@Override
		public MenuItem extractData(ResultSet rs) throws SQLException {
			if (rs.next()){
				return readMenuItem(rs);
			}
			return null;
		}
	} 
	public static class MenuItemBatchPreparedStatementSetter implements BatchPreparedStatementSetter{
		private List<MenuItem> sortedMenuItems;
		/**
		 * 构造函数
		 * @param sortedMenuItems
		 */
		public MenuItemBatchPreparedStatementSetter(List<MenuItem> sortedMenuItems){
			this.sortedMenuItems=sortedMenuItems;
		}
		@Override
		public void setValues(PreparedStatement ps, int i)
				throws SQLException {
			MenuItem menu = sortedMenuItems.get(i);
			int j=0;
			ps.setInt(++j, menu.getSequence());
			ps.setInt(++j, menu.getId());
		}

		@Override
		public int getBatchSize() {
			return (null == sortedMenuItems || sortedMenuItems.isEmpty())?0:sortedMenuItems.size();
		}
	}
	@Override
	public MenuItem getMenuItemByUrl(String url) {
		String sql = "select * from "+TABLE+" where url = ?";
		return getJdbcTemplate().query(sql,new Object[] { url },
				new MenuItemExtractor());
	}
	@Override
	public int updateMenuItem(MenuItem menuItem) {
		String sql = "update " + TABLE + " set " + UPDATED_VALUES
				+ " where id=:id";
		SqlParameterSource ps = new BeanPropertySqlParameterSource(menuItem);
		return getNamedParameterJdbcTemplate().update(sql, ps);
	}

	@Override
	public boolean updateMenuItemStatus(int id, int updateStatus) {

		String sql = "update"+ TABLE +"set status = ? where id=?";			
		int result = this.getJdbcTemplate().update(sql, new Object[]{updateStatus,id});
		return result>0;
	}

	@Override
	public boolean updateBatchMenuItemSequence(List<MenuItem> menuItems) {
		final List<MenuItem> sortedMenuItems = menuItems;
		String sql = "update "+ TABLE +" set sequence=? where id=?";
		this.getJdbcTemplate().batchUpdate(sql, new MenuItemBatchPreparedStatementSetter(sortedMenuItems));
		return true;
	}
	private int getMaxSequence(int uid){
		String sql = "select max(sequence) from "+ TABLE +" where uid='"+uid+"'";
		return getJdbcTemplate().queryForInt(sql);
	}



}
