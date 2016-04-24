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

import java.util.List;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.dao.IUrlMappingDAO;
import net.duckling.dhome.domain.people.UrlMapping;

import org.springframework.stereotype.Component;

/**
 * @author lvly
 * @since 2012-9-5
 */
@Component
public class UrlMappingDAO extends BaseDao implements IUrlMappingDAO {
	@Override
	public UrlMapping getURLFromAll(String url) {
		UrlMapping mapping = new UrlMapping();
		mapping.setUrl(url);
		return findAndReturnOnly(mapping);
	}
	@Override
	public UrlMapping getURLByValid(String url) {
		UrlMapping mapping = new UrlMapping();
		mapping.setUrl(url);
		mapping.setStatus(UrlMapping.STATUS_VALID);
		return findAndReturnOnly(mapping);
	}
	@Override
	public void updateUrlMapping(UrlMapping mapping) {
		super.update(mapping);
	}
	@Override
	public List<UrlMapping> getUrlsMappingByUid(int userId) {
		UrlMapping mapping=new UrlMapping();
		mapping.setUid(userId);
		return findByProperties(mapping);
	}
	@Override
	public int addUrlMapping(UrlMapping mapping) {
		return super.insert(mapping);
	}

}
