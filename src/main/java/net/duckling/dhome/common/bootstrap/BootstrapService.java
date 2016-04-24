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
package net.duckling.dhome.common.bootstrap;


/**
 * @group: net.duckling
 * @title: BootstrapService.java
 * @description: 自动建立数据库中的表，要求数据库已存在
 * @author clive
 * @date 2012-8-5 下午8:55:59
 */
public class BootstrapService {
	
	private BootstrapDao bootstrapDao;
	
	public BootstrapDao getBootstrapDao() {
		return bootstrapDao;
	}

	public void setBootstrapDao(BootstrapDao bootstrapDao) {
		this.bootstrapDao = bootstrapDao;
	}

	/**
	 * @description 先检查某个表是否存在，若不存在则运行数据库建表语句
	 */
	public void bootstrap(){
		if(!bootstrapDao.isTableExisted()){
			bootstrapDao.executeSQLFiles();
		}
		bootstrapDao.close();
	}

}
