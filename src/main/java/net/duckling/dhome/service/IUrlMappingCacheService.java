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

import net.duckling.dhome.domain.people.UrlMapping;

/**
 * 管理urlmapping的缓存服务层
 * @author lvly
 * @since 2012-9-13
 */
public interface IUrlMappingCacheService {
	/**
	 * 根据url从缓存里获得完整的UrlMapping对象,如果缓存里面没有则，从数据库里面取
	 * @param url www.lvlongyun.com之类
	 * @return the object of UrlMapping
	 * */
	
	UrlMapping getUrlMapping(String url);
	
	/**把修改过的urlmapping放到缓存里更新,如果没有，则添加
	 * @param mapping the object of UrlMapping
	 * */
	
	void updateUrlMapping(UrlMapping mapping);
	
	/**
	 * 根据url从缓存里删除得完整的UrlMapping对象
	 * @param url www.lvlongyun.com之类
	 * */
	void deleteUrlMapping(String url);
}
