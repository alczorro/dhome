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
package net.duckling.dhome.service;

import java.util.List;

import net.duckling.dhome.domain.people.CustomPage;

/**
 * CustomPage的服务层
 * 
 * @author lvly
 * @since 2012-8-14
 */

public interface IPageService {
	/**
	 * 根据uid获取所有页面
	 * 
	 * @param uid
	 *            UserId
	 * @return 用户的所有页面
	 * */
	List<CustomPage> getPagesByUid(int uid);

	/**
	 * 创建一个Page
	 * 
	 * @param page
	 * */
	int createPage(CustomPage page);

	/**
	 * 根据id获取页面
	 * 
	 * @param pid
	 *            PageId
	 * @return
	 * */
	CustomPage getPage(int pid);

	/**
	 * 根据访问路径获取Page信息
	 * @param url uri信息应该是这样的 /people/{domain}/page/{pageName}
	 * @return 一个页面的实体类
	 */
	CustomPage getPageByUrl(String url);

	/**
	 * 判定一个url是否被使用
	 * @param domain 当前登录用户的domain
	 * @param keyWord  keyWord
	 * @param pid pageId,如果为负数，代表不计较pid
	 * @return 返回"被使用了吗？"
	 */
	boolean isUrlUsed(String domain,String keyWord,int pid);

	/**
	 * 更新一个页面内容，必须制定id，要不然搁你查啊？
	 * @param page 页面实例
	 */
	void updatePage(CustomPage page);

	/**
	 * 返回有效的页面，未公布和删除 不显示
	 * @param id UserId
	 * @return
	 */
	List<CustomPage> getValidPagesByUid(int uid);
	/**
	 * 查询一个人所有页面的最后更新的页面
	 * @param uid
	 * */
	CustomPage getLastestEditPage(int uid);

}
