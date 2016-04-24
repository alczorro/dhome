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

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.dao.IResourceInfoDAO;
import net.duckling.dhome.domain.people.PageImg;

import org.springframework.stereotype.Component;

/**
 * 用户引用图片的jdbc实现
 * @author lvly
 * @since 2012-8-15
 */
@Component
public class ResourceInfoDAO extends BaseDao implements IResourceInfoDAO{
	@Override
	public List<PageImg> getResourceInfosByUid(int uid) {
		PageImg img=new PageImg();
		img.setUid(uid);
		return findByProperties(img);
	}
	@Override
	public int createResourceInfo(int uid, int clbId) {
		PageImg img=new PageImg();
		img.setUid(uid);
		img.setClbId(clbId);
		return insert(img);
	}
	@Override
	public int getUidFromResourceId(int clbId) {
		PageImg img=new PageImg();
		img.setClbId(clbId);
		img= findAndReturnOnly(img);
		return img==null?-1:img.getUid();
	}

}
