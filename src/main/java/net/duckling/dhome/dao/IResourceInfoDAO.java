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

import net.duckling.dhome.domain.people.PageImg;

/**
 * 页面引用图片数据持久层
 * @author lvly
 * @since 2012-8-15
 */
public interface IResourceInfoDAO {
	/**
	 * 把用户使用过的图片全部加载进来
	 * @param uid 用户id
	 * @return 所有用户用过的图片集合
	 * */
	List<PageImg> getResourceInfosByUid(int uid);
	/**插入一个页面引用图片信息
	 * @param uid 用户id
	 * @param clbId clb文件id
	 * @return generateId
	 * */
	int createResourceInfo(int uid,int clbId);
	
	/**
	 * 根据imgId获取uid
	 * @param clbId
	 * @return
	 */
	int getUidFromResourceId(int clbId);
}
