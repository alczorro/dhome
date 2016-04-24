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
package net.duckling.dhome.web.tag;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import net.duckling.dhome.common.facade.ApplicationFacade;
import net.duckling.dhome.domain.people.Menu;
import net.duckling.dhome.domain.people.MenuItem;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IMenuService;
import net.duckling.dhome.service.impl.HomeService;
import net.duckling.dhome.service.impl.MenuService;

import org.apache.log4j.Logger;

/**
 * 左菜单项动态加载
 * @author zhaojuan
 * 
 */
public class LeftMenuLoaderTag extends TagSupport implements TryCatchFinally {
	private static final Logger LOG = Logger.getLogger(LeftMenuLoaderTag.class);
	private static final long serialVersionUID = 1L;


	private String isAdmin = null;
	private String domain = null;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}
	@Override
	public int doStartTag() throws JspException {
		IMenuService menuService = (MenuService) ApplicationFacade.getAnnotationBean(MenuService.class);
		IHomeService homeService = (HomeService) ApplicationFacade.getAnnotationBean(HomeService.class);
		if (menuService == null || homeService == null) {
			LOG.error("Can not get menu/home service from application context");
			return 0;
		}
		SimpleUser su = homeService.getSimpleUserByDomain(domain);
		Menu menu = menuService.getMenu(su.getId());
		if (menu == null) {
			menuService.createDefautMenuForNewUser(su, domain);
			menu = menuService.getMenu(su.getId());
		}
		if (getIsAdmin().equals("true")) {
			List<MenuItem> items = menu.getMenuItems();
			for (MenuItem item : items) {
				String url = item.getUrl();
				int index = item.getUrl().lastIndexOf("/");
				if (index > 0) {
					String newUrl = null;
					if (url.lastIndexOf(".html") > 0) {
						newUrl = url.substring(0, index) + "/admin/p"
								+ item.getUrl().substring(index, url.lastIndexOf(".html"));
					} else if (url.lastIndexOf(".dhome") > 0) {
						newUrl = url.substring(0, index) + "/admin"
								+ item.getUrl().substring(index, url.lastIndexOf(".dhome"));
					}
					item.setUrl(newUrl);
				}
			}
			pageContext.setAttribute("menu", menu);
		}else{
			pageContext.setAttribute("menu", getGuestMenu(menu));
		}
		return SKIP_BODY;
	}
	/**如果是游客那就只能看到已发布的内容*/
	private Menu getGuestMenu(Menu menu){
		if(menu==null){
			return null;
		}
		List<MenuItem> menuItems=menu.getMenuItems();
		List<MenuItem> result=new ArrayList<MenuItem>();
		if(menuItems!=null){
			for(MenuItem item:menuItems){
				if(item.getStatus()==MenuItem.MENU_ITEM_STATUS_SHOWING){
					result.add(item);
				}
			}
		}
		menu.setMenuItems(result);
		return menu;
	}

	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	@Override
	public void doCatch(Throwable arg0) {
		LOG.error("",arg0);
	}

	@Override
	public void doFinally() {
	}
}
