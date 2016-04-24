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

import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.SearchInstitutionPaperCondition;
import net.duckling.dhome.domain.object.PageResult;

public interface IInstitutionBackendPaperAuthorDAO {
	List<InstitutionAuthor> search(String keyword);
	
	List<InstitutionAuthor> getByPaperId(int paperId);
	 List<InstitutionAuthor> getByCstnetId(String cstnetId);
	
	List<InstitutionAuthor> getByPaperIds(List<Integer> paperId);

	void createAuthor(InstitutionAuthor author);
	void insertAuthorUser(String cstnetId,int institutionId,String[] authorId,int status);
	void cancelAuthorUser(final int institutionId, final String[] authorId);

	void createRef(int id, int[] uids, int[]order,boolean[] communicationAuthors,
			boolean[] authorStudents, boolean[] authorTeacher);

	void deleteRef(int paperId);


	InstitutionAuthor getById(int paperId, int authorId);
	InstitutionAuthor getById(int authorId);
	
	
	/**
	 * 判断作者是否存在
	 * @param author
	 * @return
	 */
	boolean isExist(InstitutionAuthor author);
	
	List<InstitutionAuthor> getAuthors(int institutionId, int offset, int size,SearchInstitutionPaperCondition condition);
	int getAuthorsCount(int institutionId,SearchInstitutionPaperCondition condition);
	void updateAuthorPropelling(int authorId);
	
	List<InstitutionAuthor> getAuthorsByUid(int userId);
	/**
	 * 合并论文库
	 * @param author
	 * @return
	 */
	int insertAuthor(String name);
	InstitutionAuthor getAuthor(String name);
	void insertRef(int paperId,int authorId,int order);
}
