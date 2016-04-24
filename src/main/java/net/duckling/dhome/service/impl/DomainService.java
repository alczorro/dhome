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


import java.util.List;

import net.duckling.dhome.dao.IHomeDAO;
import net.duckling.dhome.dao.IMenuItemDAO;
import net.duckling.dhome.dao.IPageDAO;
import net.duckling.dhome.dao.IUrlMappingDAO;
import net.duckling.dhome.domain.people.CustomPage;
import net.duckling.dhome.domain.people.Home;
import net.duckling.dhome.domain.people.MenuItem;
import net.duckling.dhome.domain.people.UrlMapping;
import net.duckling.dhome.service.IDomainService;
import net.duckling.dhome.service.IUrlMappingCacheService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2012-8-28
 */
@Service
public class DomainService implements IDomainService {
    @Autowired
    private IPageDAO pageDAO;
    @Autowired
    private IMenuItemDAO menuItemDAO;
    @Autowired
    private IHomeDAO homeDAO;
    @Autowired
    private IUrlMappingDAO mappingDAO;
    @Autowired
    private IUrlMappingCacheService cacheService;

    @Override
    public void updateDomain(String oldDomain, String domain, int uid) {
    	if(oldDomain.equals(domain)){
    		return;
    	}
        Home home = homeDAO.getHomeByDomain(oldDomain);
        home.setUrl(domain);
        homeDAO.updateHome(home);
        List<CustomPage> customPages = pageDAO.getPagesByUid(uid);
        for (CustomPage page : customPages) {
            page.setUrl(updateUrl(page.getUrl(),domain));
            pageDAO.updatePage(page);
        }
        List<MenuItem> menuItems = menuItemDAO.getMenuItemsByUId(uid);
        for (MenuItem item : menuItems) {
            item.setUrl(updateUrl(item.getUrl(),domain));
            menuItemDAO.updateMenuItem(item);
        }
        List<UrlMapping> mappings = mappingDAO.getUrlsMappingByUid(uid);
        for (UrlMapping mapping : mappings) {
            mapping.setDomain(domain);
            mappingDAO.updateUrlMapping(mapping);
            cacheService.updateUrlMapping(mapping);
        }
    }
    
    private String updateUrl(String url,String domain){
    	String[] str=url.split("/");
    	str[2]=domain;
    	String result="";
    	for(int i=1;i<str.length;i++){
    		result+="/"+str[i];
    	}
    	return result;
    	
    }
}
