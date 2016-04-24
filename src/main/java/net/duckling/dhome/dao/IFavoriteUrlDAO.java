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

import net.duckling.dhome.domain.object.FavoriteUrl;

/**
 * 社交网络，数据持久层
 * @author lvly
 * @since 2012-12-21
 */
public interface IFavoriteUrlDAO {
	
	/***
	 * 新增社交网络
	 * @param url 社交网络实体类
	 * */
	void addFavoriteUrl(FavoriteUrl url);
	
	/**
	 * 获得社交网络
	 * @param uid 实体人的uid
	 * @return 
	 * */
	List<FavoriteUrl> getFavoritesUrl(int uid);
	
	/**
	 * 删除社交网络
	 * @param id 社交网络主键
	 * */
	void deleteFavorite(int id);
	
	/**
	 * 更新社交网络
	 * @param url 
	 * */
	void updateFavorite(FavoriteUrl url);

	/**
	 * 获取一条社交网络，用于编辑
	 *@param id 主键Id
	 *@return object of favoriteUrl
	 * */
	FavoriteUrl getFavoriteUrlById(int id);
}
