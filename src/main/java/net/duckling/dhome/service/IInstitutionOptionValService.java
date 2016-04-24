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
package net.duckling.dhome.service;

import java.util.List;


import java.util.Map;

import net.duckling.dhome.domain.institution.InstitutionOptionVal;

public interface IInstitutionOptionValService{
	
	int add(InstitutionOptionVal val);

	/**
	 * 获取一个设置选项的列表, 有memchached缓存
	 * @param optionId
	 * @param institutionId
	 * @return
	 */
	List<InstitutionOptionVal> getListByOptionId(int optionId, int institutionId);
	
	Map<Integer, InstitutionOptionVal> getMapByOptionId(int optionId, int institutionId);

	void edit(InstitutionOptionVal val);

	InstitutionOptionVal getById(int id);
	
	void delete(int id,int institutionId);
}
