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

import net.duckling.dhome.domain.institution.InstitutionPeriodical;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;

/**
 * 期刊任职数据库接口
 * @author liyanzhao
 *
 */
public interface IInstitutionPeriodicalDAO {
	
	void insert(List<InstitutionPeriodical> periodicals);
	void insert(InstitutionPeriodical periodical);
	List<InstitutionPeriodical> getPeriodicals(int insId,int offset,int pageSize,SearchInstitutionCondition condition);
	List<InstitutionPeriodical> getPeriodicalsByUID(int uid);

	InstitutionPeriodical getPeriodical(int perId);
	
	int getPeriodicalCount(int insId,SearchInstitutionCondition condition);
	void delete(int insId, int[] perId);
	void delete(int insId,int perId);
    void deleteAll(int insId);
	int create(InstitutionPeriodical per);
	void update(InstitutionPeriodical per);
	
    void deleteUserRef(int uid,int perId);
    void createUserRef(int uid,int perId);
	
	List<InstitutionPeriodical> getPeriodicalByUser(int userId, int i, int pageSize, SearchInstitutionCondition condition);
	int getPeriodicalCountByUser(int userId, SearchInstitutionCondition condition);
//	Map<Integer, Integer> getFundsFromsMapByUser(int userId);
	void deletePeriodicalUser(int[] id);
	void insertPeriodicalUser(int periodicalId, int userId);
	Map<Integer, Integer> getPositionsMap(int insId);
	
	int getPeriodicalCount(int insId, int departId, int year);
}
