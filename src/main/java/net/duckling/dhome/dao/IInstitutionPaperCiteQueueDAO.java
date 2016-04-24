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
package net.duckling.dhome.dao;

import net.duckling.dhome.domain.institution.InstitutionPaperCiteQueue;

/**
 * 论文引用抓取队列数据库接口
 * @author brett
 *
 */
public interface IInstitutionPaperCiteQueueDAO {
	/**
	 * 获取一条抓取记录，手动单次增加的优先
	 * @return
	 */
	InstitutionPaperCiteQueue getFirst();
	void insert(InstitutionPaperCiteQueue model);
	void delete(int paperId);
	
	InstitutionPaperCiteQueue getByPaperId(int paperId);
	
	int getCountByBatchType();
	
	void update(InstitutionPaperCiteQueue model);
}
