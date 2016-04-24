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

import java.util.LinkedHashMap;
import java.util.List;








import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.duckling.dhome.dao.IInstitutionOptionValDAO;
import net.duckling.dhome.domain.institution.InstitutionOptionVal;
import net.duckling.dhome.service.IInstitutionOptionValService;
import net.duckling.falcon.api.cache.ICacheService;

@Service
public class InstitutionOptionValService implements IInstitutionOptionValService{

	@Autowired
	IInstitutionOptionValDAO  optionValDAO;
	@Autowired
	private ICacheService cacheService;

	@SuppressWarnings("unchecked")
	@Override
	public List<InstitutionOptionVal> getListByOptionId(int optionId, int institutionId) {
		String cacheKey = getListCacheKey(optionId, institutionId);
		List<InstitutionOptionVal> result = (List<InstitutionOptionVal>)cacheService.get(cacheKey);
		if(result== null || result.size()==0){
			refreshListCache(optionId, institutionId);
			result = (List<InstitutionOptionVal>)cacheService.get(cacheKey);
		}
		return result;
	}
	
	public Map<Integer, InstitutionOptionVal> getMapByOptionId(int optionId, int institutionId) {
		Map<Integer, InstitutionOptionVal> result = new LinkedHashMap<Integer, InstitutionOptionVal>();
		List<InstitutionOptionVal> list = getListByOptionId(optionId, institutionId);
		for(InstitutionOptionVal item : list){
			result.put(item.getId(), item);
		}
		return result;
	}
	
	@Override
	public int add(InstitutionOptionVal val) {
		int result = optionValDAO.add(val);
		refreshListCache(val.getOptionId(), val.getInstitutionId());
		return result;
	}

	@Override
	public void edit(InstitutionOptionVal val) {
		optionValDAO.edit(val);
		refreshListCache(val.getOptionId(), val.getInstitutionId());
	}

	@Override
	public InstitutionOptionVal getById(int id) {
		return optionValDAO.getById(id);
	}

	@Override
	public void delete(int id, int institutionId) {
		InstitutionOptionVal obj = getById(id);
		optionValDAO.delete(id, institutionId);
		refreshListCache(obj.getOptionId(), institutionId);
	}
	
	private void refreshListCache(int optionId, int institutionId){
		String cacheKey = getListCacheKey(optionId, institutionId);
		cacheService.remove(cacheKey);
		cacheService.set(cacheKey, getListFromDb(optionId, institutionId));
	}
	private String getListCacheKey(int optionId, int institutionId){
		return "institutionOption" + optionId + "_" + institutionId;
	}
	private List<InstitutionOptionVal> getListFromDb(int optionId, int institutionId){
		return optionValDAO.getListByOptionId(optionId, institutionId);
	}
}
