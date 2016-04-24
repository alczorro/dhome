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

import net.duckling.dhome.domain.institution.InstitutionGrants;
import net.duckling.dhome.domain.object.PageResult;

public interface IInstitutionGrantsService {
	
	PageResult<InstitutionGrants> getList(Integer userId, Integer degree, Integer status, Integer studentId, int page);
	
    int getBatchCount(Integer userId,Integer degree); //获取批次号
	
	int getListCount(Integer userId, Integer degree, Integer status);
	
	int create(InstitutionGrants grants);
	
	void update(InstitutionGrants grants);

	int getCount(Integer userId, Integer degree);
	
	void delete(final int userId, final int[] id);
	
	InstitutionGrants getById(int id);
	
	void updateStatus(final int uid, int[] ids);
	
	PageResult<InstitutionGrants> getListLasted(Integer userId, Integer degree, Integer status, int page);
	
	PageResult<String> getBatchList(Integer userId,Integer degree,int page);
	
	List<InstitutionGrants> getListByBatchNo(Integer userId, String batchNo);
	PageResult<InstitutionGrants> getListByBatchNo(Integer userId, String batchNo, int page);
	Map<String,List<InstitutionGrants>> getStudentByUser(int userId);

	/**
	 * 获取学位统计列表
	 * @param insId
	 * @param userId
	 * @return
	 */
	Map<Integer, Integer> getDegreesCount(Integer insId, Integer userId);
	
	Map<String, Integer> getStatusCount(Integer userId);

}
