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

import net.duckling.dhome.domain.institution.InstitutionAwardAuthor;
import net.duckling.dhome.domain.institution.InstitutionTreatiseAuthor;

public interface IInstitutionBackendAwardAuthorDAO {

	List<InstitutionAwardAuthor> search(String keyword);
	List<InstitutionAwardAuthor> getByAwardId(int awardId);
	List<InstitutionAwardAuthor> getByAwardIds(List<Integer> awardIds);
	void createAuthor(InstitutionAwardAuthor author);
	InstitutionAwardAuthor getById(int awardId,int authorId);
	InstitutionAwardAuthor getById(int authorId);
	
	void createRef(int awardId,int[] authorIds,int[]order,boolean[] communicationAuthors,
			boolean[] authorStudents, boolean[] authorTeacher);
	
	void deleteRef(int awardId);
	
	void updateRef(int authorId,int awardId,int order);
	
	/**
	 * 判断作者是否存在
	 * @param author
	 * @return
	 */
	boolean isExist(InstitutionAwardAuthor author);
}
