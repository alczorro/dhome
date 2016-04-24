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
package net.duckling.dhome.web;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import cn.vlabs.rest.IFileSaver;
import net.duckling.dhome.common.clb.IClbClient;
import net.duckling.dhome.domain.people.PageImg;
import net.duckling.dhome.service.impl.ClbFileService;

public class MockClbFileService extends ClbFileService {

	@Override
	public int createFile(String fileName, InputStream is) {
		// TODO Auto-generated method stub
		return super.createFile(fileName, is);
	}

	@Override
	public int createFile(String fileName, int length, InputStream is) {
		// TODO Auto-generated method stub
		return super.createFile(fileName, length, is);
	}

	@Override
	public int createFile(File file) {
		// TODO Auto-generated method stub
		return super.createFile(file);
	}


	@Override
	public IClbClient getFileStorageInstance() {
		// TODO Auto-generated method stub
		return super.getFileStorageInstance();
	}

@Override
public int getUidFromResourceId(int clbId) {
	// TODO Auto-generated method stub
	return super.getUidFromResourceId(clbId);
}

	@Override
	public List<PageImg> getPageImgsByUid(int uid) {
		// TODO Auto-generated method stub
		return super.getPageImgsByUid(uid);
	}

}
