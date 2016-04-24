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

import java.util.ArrayList;
import java.util.List;

import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.dao.IInterestDAO;
import net.duckling.dhome.domain.object.InterestCount;
import net.duckling.dhome.domain.people.Dictionary;
import net.duckling.dhome.domain.people.Interest;
import net.duckling.dhome.service.IInterestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 研究兴趣服务
 * @author Yangxp
 *
 */
@Service
public class InterestService implements IInterestService {

	@Autowired
	private IInterestDAO interestDAO;
	@Override
	public List<InterestCount> getInterestAll() {
		return interestDAO.getInterestAll();
	}
	public List<InterestCount> removeNull(List<InterestCount> list){
		if(!CommonUtils.isNull(list)){
			List<InterestCount> result=new ArrayList<InterestCount>(); 
			for(InterestCount ins:list){	
				if(!CommonUtils.isNull(ins.getKeyWord())){
					result.add(ins);
				}
			}
			return result;
		}
		return list;
	}
	@Override
	public int[] batchCreate(int uid, List<String> interests) {
		return interestDAO.batchCreate(uid, interests);
	}

	@Override
	public boolean deleteByUid(int uid) {
		return interestDAO.deleteByUid(uid);
	}

	@Override
	public List<Interest> getInterest(int uid) {
		return interestDAO.getInterest(uid);
	}

	@Override
	public List<Dictionary> searchInterest(String keyword) {
		return interestDAO.searchInterest(keyword);
	}

}
