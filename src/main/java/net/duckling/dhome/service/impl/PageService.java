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
package net.duckling.dhome.service.impl;

import java.util.Date;
import java.util.List;

import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.UrlUtil;
import net.duckling.dhome.dao.IPageDAO;
import net.duckling.dhome.domain.people.CustomPage;
import net.duckling.dhome.service.IPageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2012-8-14
 */
@Service
public class PageService implements IPageService {
	@Autowired
	private IPageDAO pageDAO;

	@Override
	public List<CustomPage> getPagesByUid(int uid) {
		return pageDAO.getPagesByUid(uid);
	}

	@Override
	public int createPage(CustomPage page) {
		return pageDAO.createPage(page);
	}

	@Override
	public CustomPage getPage(int pid) {
		return pageDAO.findByPid(pid);
	}

	@Override
	public CustomPage getPageByUrl(String url) {
		return pageDAO.getPageByUrl(url);
	}

	@Override
	public boolean isUrlUsed(String domain, String keyWord, int pid) {
		List<CustomPage> pages = pageDAO.getPages(domain, keyWord);
			if (!UrlUtil.canUse(domain)) {
				return true;
			}
			if (pages == null) {
				return false;
			}
			if (pages.size() > 1) {
				return true;
			}
			if (pages.size() == 1) {
				return pid > 0 ? pid != CommonUtils.first(pages).getId() : true;
			}
		return false;
	}

	@Override
	public void updatePage(CustomPage page) {
		page.setLastEditTime(new Date());
		pageDAO.updatePage(page);
	}

	@Override
	public List<CustomPage> getValidPagesByUid(int uid) {
		return pageDAO.getValidPagesByUid(uid);
	}
	@Override
	public CustomPage getLastestEditPage(int uid) {
		CustomPage page=pageDAO.getLastestEditPage(uid);
		return page==null?new CustomPage():page;
	}
}
