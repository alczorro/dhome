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

import net.duckling.dhome.domain.people.CustomPage;
import net.duckling.dhome.service.IPageService;

/**
 * @author lvly
 * @since 2012-8-22
 */
public class MockPageService implements IPageService {
	@Override
	public CustomPage getLastestEditPage(int uid) {
		// TODO Auto-generated method stub
		return new CustomPage();
	}
	@Override
	public List<CustomPage> getPagesByUid(int uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int createPage(CustomPage page) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CustomPage getPage(int pid) {
		return new CustomPage();
	}

	@Override
	public CustomPage getPageByUrl(String url) {
		return new CustomPage();
	}

	@Override
	public boolean isUrlUsed(String domain, String keyWord, int pid) {
		return true;
	}

	@Override
	public void updatePage(CustomPage page) {
		// TODO Auto-generated method stub

	}
	@Override
	public List<CustomPage> getValidPagesByUid(int uid) {
		// TODO Auto-generated method stub
		return null;
	}

}
