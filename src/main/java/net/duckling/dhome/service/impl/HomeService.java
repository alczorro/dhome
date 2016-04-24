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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.duckling.common.util.CommonUtils;
import net.duckling.dhome.dao.IHomeDAO;
import net.duckling.dhome.dao.ISimpleUserDAO;
import net.duckling.dhome.domain.people.Home;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IHomeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * 域名服务层
 * 
 * @author lvly
 * 
 */
@Service
public class HomeService implements IHomeService {
    @Autowired
    private IHomeDAO homeDAO;
    @Autowired
    private ISimpleUserDAO simpleUserDAO;

    public void setHomeDAO(IHomeDAO homeDAO) {
        this.homeDAO = homeDAO;
    }

    @Override
    public String getDomain(int uid) {
        return homeDAO.getUrlFromUid(uid);
    }

    private int findDomainOwner(String domain) {
        return homeDAO.getUidFromDomain(domain);
    }

    @Override
    public SimpleUser getSimpleUserByDomain(String domain) {
        int uid = this.findDomainOwner(domain);
        return simpleUserDAO.getUser(uid);
    }

    @Override
    public Home getHomeByDomain(String domain) {
        return homeDAO.getHomeByDomain(domain);
    }

    @Override
    public boolean updateHome(Home home) {
        return homeDAO.updateHome(home);
    }

    @Override
    public Map<Integer, String> getDomainByUID(List<Integer> uids) {
        return homeDAO.getDomainByUID(uids);
    }
    @Override
    public List<Home> getDomainsByUids(List<Integer> uids) {
    	if(CommonUtils.isNull(uids)){
    		return Collections.emptyList();
    	}
    	return homeDAO.getDomainsByUids(uids);
    }

}
