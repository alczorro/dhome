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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.duckling.dhome.dao.IThemeDAO;
import net.duckling.dhome.domain.people.Theme;
import net.duckling.dhome.service.IThemeService;
/**
 * 主题服务
 * @author Zhanjuan
 *
 */
@Service
public class ThemeService implements IThemeService{
	@Autowired 
	private IThemeDAO themeDAO;
	@Override
	public List<Theme> getAllThemes() {
		return themeDAO.getAllThemes();
	}
	/**
	 * setter
	 * @param themeDAO
	 */
	public void setThemeDAO(IThemeDAO themeDAO) {
		this.themeDAO = themeDAO;
	}
	

}
