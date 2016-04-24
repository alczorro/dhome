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

import java.util.List;
import java.util.Map;

import net.duckling.dhome.domain.institution.InstitutionGrants;

public interface IInstitutionGrantsDAO {

	int create(InstitutionGrants paper);
	
	void update(InstitutionGrants paper);

	List<InstitutionGrants> getList(Integer userId, Integer degree, Integer status, Integer studentId, int offset, int size);
	
	List<String> getBatchList(Integer userId,Integer degree, int offset, int size); //获取批次号
	int getBatchCount(Integer userId,Integer degree); //获取批次号
	
	int getListCount(Integer userId, Integer degree, Integer status, Integer studentId);
	
	void delete(final int userId, final int[] id);
	
	InstitutionGrants getById(int id);
	
	void updateStatus(final int uid, final int[] ids);
	
	List<InstitutionGrants> getListLasted(Integer userId, Integer degree, Integer status, int offset, int size);
	
	int getListCountLasted(Integer userId, Integer degree, Integer status);
	
	/**
	 * 获取同一批次记录
	 * @param userId
	 * @param batchNo
	 * @return
	 */
	List<InstitutionGrants> getListByBatchNo(Integer userId, String batchNo);
	List<InstitutionGrants> getListByBatchNo(Integer userId, String batchNo, int offset, int size);
	int getBatchGrantsCount(Integer userId, String batchNo);
	List<InstitutionGrants> getStudentByUser(int userId);
	
	/**
	 * 获取学位统计列表
	 * @param insId
	 * @return
	 */
	Map<Integer, Integer> getDegreesCount(Integer insId, Integer userId);
	
	Map<String, Integer> getStatusCount(Integer userId);
}
