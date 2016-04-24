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

import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.dao.IUrlMappingDAO;
import net.duckling.dhome.domain.people.UrlMapping;
import net.duckling.dhome.service.IUrlMappingCacheService;
import net.duckling.dhome.service.IUrlMappingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2012-9-10
 */
@Service
public class UrlMappingService implements IUrlMappingService {
    @Autowired
    private IUrlMappingDAO urlDAO;
    @Autowired
    private IUrlMappingCacheService cacheService;
    
   

    @Override
    public boolean isUrlUsed(String url, int uid) {
        UrlMapping mapping = urlDAO.getURLFromAll(url);
        if (mapping == null) {
            return false;
        }
        if (mapping.getUid() == uid) {
            return false;
        }
        return true;
    }

    @Override
    public void updateUrlMapping(String url, String domain, int userId, String status, int urlId) {
        UrlMapping mapping = new UrlMapping();
        mapping.setDomain(domain);
        mapping.setUrl(url);
        mapping.setUid(userId);
        mapping.setStatus(status);
        mapping.setId(urlId);
        urlDAO.updateUrlMapping(mapping);
        cacheService.updateUrlMapping(mapping);

    }

    @Override
    public UrlMapping getUrlMappingByUrl(String url) {
    	return cacheService.getUrlMapping(url);
    }

    @Override
    public int addUrlMapping(String url, String domain, int userId, String status) {
        UrlMapping mapping = new UrlMapping();
        mapping.setUrl(url);
        mapping.setDomain(domain);
        mapping.setUrl(url);
        mapping.setUid(userId);
        mapping.setStatus(status);
        int id= urlDAO.addUrlMapping(mapping);
        if(id>0){
        	cacheService.updateUrlMapping(mapping);
        }
        return id;
    }
    @Override
    public UrlMapping getUrlMappingByUserId(int userId) {
    	return CommonUtils.first(urlDAO.getUrlsMappingByUid(userId));
    }
}
