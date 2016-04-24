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
package net.duckling.dhome.dao;

import java.util.List;

import net.duckling.dhome.domain.people.UrlMapping;

/**
 * 
 * 自定义域名映射，jdbc 实现类
 * @author lvly
 * @since 2012-9-5
 */
public interface IUrlMappingDAO {
	/**
	 * 根据url找出来用户和domain，比如www.lvlongyun.com找出对应的用户来
	 * @param url root访问地址
	 * @return UrlMapping
	 * */
	UrlMapping getURLFromAll(String url);

	/**
	 * 更新Mapping的内容
	 * @param mapping
	 */
	void updateUrlMapping(UrlMapping mapping);

	/**
	 * 根据用户id获得url-mapping
	 * @param userId
	 * @return
	 */
	List<UrlMapping> getUrlsMappingByUid(int userId);

	/**
	 * 新增一个urlMapping
	 * @param mapping
	 * @return
	 */
	int addUrlMapping(UrlMapping mapping);

	/**
	 * 只在有效的url里面查询
	 * @param rootUrl
	 * @return
	 */
	UrlMapping getURLByValid(String url);
}
