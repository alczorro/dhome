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
package net.duckling.dhome.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.dao.IFavoriteUrlDAO;
import net.duckling.dhome.domain.object.FavoriteUrl;

/**
 * @author lvly
 * @since 2012-12-21
 */
@Component
public class FavoriteUrlDAO extends BaseDao implements IFavoriteUrlDAO{

	@Override
	public void addFavoriteUrl(FavoriteUrl url) {
		insert(url);
	}

	@Override
	public List<FavoriteUrl> getFavoritesUrl(int uid) {
		FavoriteUrl url=new FavoriteUrl();
		url.setUid(uid);
		return findByProperties(url,"order by id");
	}

	@Override
	public void deleteFavorite(int id) {
		FavoriteUrl url=new FavoriteUrl();
		url.setId(id);
		remove(url);
	}

	@Override
	public void updateFavorite(FavoriteUrl url) {
		update(url);
	}
	@Override
	public FavoriteUrl getFavoriteUrlById(int id) {
		FavoriteUrl url=new FavoriteUrl();
		url.setId(id);
		return findAndReturnOnly(url);
	}

}
