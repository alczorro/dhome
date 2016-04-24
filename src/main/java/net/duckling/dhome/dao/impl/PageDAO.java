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
import net.duckling.dhome.dao.IPageDAO;
import net.duckling.dhome.domain.people.CustomPage;
import net.duckling.dhome.domain.people.MenuItem;

import org.springframework.stereotype.Repository;

/**
 * pageDAO的jdbc实现
 * 
 * @author lvly
 * @since 2012-8-14
 */
@Repository
public class PageDAO extends BaseDao implements IPageDAO {

	@Override
	public CustomPage findByPid(int pid) {
		CustomPage page = new CustomPage();
		page.setId(pid);
		return findAndReturnOnly(page);
	}

	@Override
	public List<CustomPage> getPagesByUid(int uid) {
		CustomPage page = new CustomPage();
		page.setUid(uid);
		return findByProperties(page);
	}

	@Override
	public int createPage(CustomPage page) {
		return insert(page);
	}

	@Override
	public CustomPage getPageByUrl(String url) {
		CustomPage page = new CustomPage();
		page.setUrl(url);
		return findAndReturnOnly(page);
	}

	@Override
	public List<CustomPage> getPages(String domain, String keyWord) {
		CustomPage page = new CustomPage();
		page.setUrl("/people/" + domain + "/" + keyWord + ".html");
		return findByProperties(page);
	}

	@Override
	public List<CustomPage> getValidPagesByUid(int uid) {
		String sql = "select * from custom_page p,menu_item i where p.url=i.url and i.uid=:uid and i.status="
				+ MenuItem.MENU_ITEM_STATUS_SHOWING + " order by i.sequence";
		DAOUtils<CustomPage> daoUtil = new DAOUtils<CustomPage>(CustomPage.class);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("uid", uid);
		return getNamedParameterJdbcTemplate().query(sql, paramMap, daoUtil.getRowMapper(null));
	}

	@Override
	public int updatePage(CustomPage page) {
		return update(page);
	}
	@Override
	public CustomPage getLastestEditPage(int uid) {
		CustomPage page=new CustomPage();
		page.setUid(uid);
		return findAndReturnOnly(page, "order by last_edit_time desc");
	}

}
