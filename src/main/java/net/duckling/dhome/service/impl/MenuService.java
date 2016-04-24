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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.dhome.common.util.DefaultValuePageUtils;
import net.duckling.dhome.dao.IMenuItemDAO;
import net.duckling.dhome.dao.IPageDAO;
import net.duckling.dhome.domain.people.CustomPage;
import net.duckling.dhome.domain.people.Education;
import net.duckling.dhome.domain.people.Menu;
import net.duckling.dhome.domain.people.MenuItem;
import net.duckling.dhome.domain.people.Paper;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.Work;
import net.duckling.dhome.service.IEducationService;
import net.duckling.dhome.service.IMenuService;
import net.duckling.dhome.service.IPaperService;
import net.duckling.dhome.service.IWorkService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 菜单服务
 * 
 * @author Zhaojuan
 * 
 */
@Service
public class MenuService implements IMenuService {
    private static final String[] MENU_ITEM_DEFAULT_ITEMS = { "首页", "学术论文", "联系方式","留言板" };
    private static final String[] MENU_ITEM_DEFAULT_URLSUFFIX = { "index.html", "paper.dhome", "contact.html","msgboard.dhome" };
    @Autowired
    private IMenuItemDAO menuItemDAO;
    @Autowired
    private IPageDAO pageDAO;
    @Autowired
    private IWorkService workService;
    @Autowired
    private IEducationService educationService;
    @Autowired
    private IPaperService paperService;

    @Override
    public Menu getMenu(int uid) {
        Menu menu = null;
        List<MenuItem> menuItems = menuItemDAO.getMenuItemsByUId(uid);
        if (menuItems != null && !menuItems.isEmpty()) {
            menu = new Menu();
            menu.setUid(uid);
            Collections.sort(menuItems);
            /** 按sequence从小到大排序 */
            menu.setMenuItems(menuItems);
        }
        return menu;
    }

    @Override
    public void createDefautMenuForNewUser(SimpleUser simpleUser, String domain) {
        List<MenuItem> mItems = getDefaultMenuItems();
        for (MenuItem menuItem : mItems) {
            String keyWord = menuItem.getUrl().replace(".html", "");

            menuItem.setUid(simpleUser.getId());
            String pageUrl = "/people/" + domain + "/" + menuItem.getUrl();
            menuItem.setUrl(pageUrl);
            menuItem.setStatus(MenuItem.MENU_ITEM_STATUS_SHOWING);
            menuItemDAO.addMenuItem(menuItem);

            CustomPage page = new CustomPage();
            page.setUrl(menuItem.getUrl());
            page.setKeyWord(keyWord);
            page.setTitle(menuItem.getTitle());
            page.setUid(menuItem.getUid());
            Map<String, Object> map = new HashMap<String, Object>();
            if (keyWord.equals("index")) {
                List<Paper> papers = paperService.getPapers(menuItem.getUid(), 0, 0);
                map.put("${papers}", papers);
                page.setContent(DefaultValuePageUtils.getHTML(map, DefaultValuePageUtils.INDEX_TEMP));
            } else if (keyWord.equals("profile")) {
                List<Work> works = workService.getWorksByUID(menuItem.getUid());
                List<Education> edus = educationService.getEducationsByUid(menuItem.getUid());
                map.put("${works}", works);
                map.put("${edus}", edus);
                page.setContent(DefaultValuePageUtils.getHTML(map, DefaultValuePageUtils.PROFILE_TEMP));
            } else if (keyWord.equals("contact")) {
                page.setContent(DefaultValuePageUtils.getHTML(map, DefaultValuePageUtils.CONTACT_TEMP));
            }
            pageDAO.createPage(page);
        }

    }

    private List<MenuItem> getDefaultMenuItems() {
        List<MenuItem> mItems = new ArrayList<MenuItem>();
        String[] keys = MENU_ITEM_DEFAULT_ITEMS;
        for (int i = 0; i < keys.length; i++) {

            MenuItem item = new MenuItem();
            item.setTitle(keys[i]);
            item.setStatus(MenuItem.MENU_ITEM_STATUS_SHOWING);
            item.setUrl(MENU_ITEM_DEFAULT_URLSUFFIX[i]);
            item.setSequence(i + 1);
            mItems.add(item);
        }
        return mItems;
    }

    public void setMenuItemDAO(IMenuItemDAO menuItemDAO) {
        this.menuItemDAO = menuItemDAO;
    }

}
