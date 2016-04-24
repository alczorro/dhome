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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.duckling.dhome.dao.IInstitutionPaperCiteQueueDAO;
import net.duckling.dhome.domain.institution.InstitutionPaperCiteQueue;
import net.duckling.dhome.service.IInstitutionPaperCiteQueueService;

@Service
public class InstitutionPaperCiteQueueService  implements IInstitutionPaperCiteQueueService{

	@Autowired
    private IInstitutionPaperCiteQueueDAO institutionPaperCiteQueueDAO;
	
	@Override
	public InstitutionPaperCiteQueue getFirst() {
		return institutionPaperCiteQueueDAO.getFirst();
	}

	@Override
	public void add(InstitutionPaperCiteQueue model) {
		InstitutionPaperCiteQueue existed = institutionPaperCiteQueueDAO.getByPaperId(model.getPaperId());
		//判断是否存在
		if(existed==null){
			institutionPaperCiteQueueDAO.insert(model);
		}else{
			//手动单次更新优先
			if(InstitutionPaperCiteQueue.APPEND_TYPE_BATCH == existed.getAppendType()&&
					InstitutionPaperCiteQueue.APPEND_TYPE_SINGLE==model.getAppendType()){
				institutionPaperCiteQueueDAO.update(model);
			}
		}
	}

	@Override
	public void delete(int paperId) {
		institutionPaperCiteQueueDAO.delete(paperId);
	}
	
	@Override
	public int getCountByBatch(){
		return institutionPaperCiteQueueDAO.getCountByBatchType();
	}

}
