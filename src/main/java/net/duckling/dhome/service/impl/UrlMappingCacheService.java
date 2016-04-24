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
package net.duckling.dhome.service.impl;

import net.duckling.falcon.api.cache.*;
import net.duckling.dhome.common.util.MemCacheKeyGenerator;
import net.duckling.dhome.dao.IUrlMappingDAO;
import net.duckling.dhome.domain.people.UrlMapping;
import net.duckling.dhome.service.IUrlMappingCacheService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * url-mapping缓存操作服务层
 * 
 * @author lvly
 * @since 2012-9-13
 */
@Service
public class UrlMappingCacheService implements IUrlMappingCacheService {
	

	private static final Logger LOG = Logger.getLogger(UrlMappingCacheService.class);

	@Autowired
	private IUrlMappingDAO urlDAO;
	@Autowired
	private ICacheService cacheService;

	@Override
	public UrlMapping getUrlMapping(String url) {
		UrlMapping urlMapping = (UrlMapping)cacheService.get(MemCacheKeyGenerator.getUrlMappingKey(url));
		if (urlMapping == null) {
			urlMapping = urlDAO.getURLByValid(url);
			updateUrlMapping(urlMapping);
		}
		return urlMapping;
	}

	@Override
	public void updateUrlMapping(UrlMapping mapping) {
		if (mapping == null || mapping.getUrl() == null) {
			LOG.debug("the key or mapping is null!");
			return;
		}
		cacheService.set(MemCacheKeyGenerator.getUrlMappingKey(mapping.getUrl()), mapping);
	}

	@Override
	public void deleteUrlMapping(String url) {
		cacheService.remove(MemCacheKeyGenerator.getUrlMappingKey(url));
	}

}
