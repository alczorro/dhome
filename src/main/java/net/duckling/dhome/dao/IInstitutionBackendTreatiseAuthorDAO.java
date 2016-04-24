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

import net.duckling.dhome.domain.institution.InstitutionTreatiseAuthor;

public interface IInstitutionBackendTreatiseAuthorDAO {

	List<InstitutionTreatiseAuthor> search(String keyword);
	List<InstitutionTreatiseAuthor> getByTreatiseId(int treatiseId);
	List<InstitutionTreatiseAuthor> getByTreatiseIds(List<Integer> treatiseIds);
	void createAuthor(InstitutionTreatiseAuthor author);
	InstitutionTreatiseAuthor getById(int treatiseId,int authorId);
	InstitutionTreatiseAuthor getById(int authorId);
	
	void createRef(int treatiseId,int[] authorIds,int[]order,boolean[] communicationAuthors,
			boolean[] authorStudents, boolean[] authorTeacher);
	
	void deleteRef(int treatiseId);
	void updateRef(int authorId,int tidd,int order);
	
	/**
	 * 判断作者是否存在
	 * @param author
	 * @return
	 */
	boolean isExist(InstitutionTreatiseAuthor author);
	
	int insertAuthor(String name,String company);
	void insertRef(int treatiseId,int authorId,int order);
	int getAuthorId(String name,String company);
	void updateImportRef(int treatiseId,int authorId,int order);
}
