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
package net.duckling.dhome.service.impl.mock;

import java.util.List;

import net.duckling.dhome.domain.object.FavoriteUrl;
import net.duckling.dhome.service.IFavoriteUrlService;

/**
 * @author lvly
 * @since 2012-12-24
 */
public class StubFavoriteUrlService implements IFavoriteUrlService {

	@Override
	public FavoriteUrl getFavoriteUrlById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addFavorite(FavoriteUrl url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<FavoriteUrl> getFavoritesByUid(int uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFavorite(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateFavorite(FavoriteUrl fav) {
		// TODO Auto-generated method stub
		
	}


}
