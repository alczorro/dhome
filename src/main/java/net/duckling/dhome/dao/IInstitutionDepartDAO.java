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

import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.vmt.api.domain.VmtDepart;

public interface IInstitutionDepartDAO {
	int insertDepart(InstitutionDepartment depart);

	void removeByInstitutionId(int insId);

	void updateDept(VmtDepart dept, int institutionId);

	InstitutionDepartment getDepartBySymbol(int institutionId, String symbol);


	void moveDepart(int deptId, int toDeptId);

	void parentChanged(int orgParentId, int newId);

	List<InstitutionDepartment> getDepartsByParentIds(int id);
	
	void removeDepartByIds(List<Integer> ids);
	void removeDepartByIds(int id);

	void displayChanged(int institutionId, List<Integer> sonDeptId,String string);

	List<InstitutionDepartment> getDepartsByIds(List<Integer> deptId);
	
	List<InstitutionDepartment> getDepartsByInsId(int insId);

	InstitutionDepartment getDepartById(int departId);
	
	void updateDeptShortName(int id,String shortName,int listRank);

	List<InstitutionDepartment> getDepartsOrderPinyin(int insId);

	void batchUpdate(int institutionId, List<InstitutionDepartment> datas);


}
