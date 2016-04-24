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
package net.duckling.dhome.dao;

import java.util.List;

import net.duckling.dhome.domain.people.CustomPage;
/**
 * CustomPage的持久层
 * @author lvly
 * @since 2012-8-14
 */
public interface IPageDAO {

	/**
	 * 更新一个页面内容，必须制定id，要不然搁你查啊？
	 * @param page 页面实例
	 * @return int 返回中标多少记录
	 */
	int updatePage(CustomPage page);
	/**
	 * 根据Id获取页面
	 * @param pid pageID
	 * @return 页面实体
	 * */
	CustomPage findByPid(int pid);
	/**
	 * 获取当前用户的所有页面信息
	 * @param uid userId
	 * @return 当前用户相关的页面信息
	 * */
	List<CustomPage> getPagesByUid(int uid);

	/**
	 * 创建页面
	 * @param page 页面对象
	 * @return
	 */
	int createPage(CustomPage page);

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
	 * @return 返回一个匹配队列
	 */
	 List<CustomPage> getPages(String domain,String keyWord);
	
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
