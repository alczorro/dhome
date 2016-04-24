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

import net.duckling.dhome.domain.institution.InstitutionAttachment;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionPatent;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.PageResult;

public interface IInstitutionBackendPatentService {

	PageResult<InstitutionPatent> getPatent(int institutionId,int page, SearchInstitutionCondition condition);
	List<InstitutionPatent> getPatentsByUID(int uid);

	int getPatentCount(int insId);
	int getPatentCount(int insId,SearchInstitutionCondition condition);
	PageResult<InstitutionPatent> getPatentByUser(int userId,int page, SearchInstitutionCondition condition);
	int getPatentCountByUser(int userId);
	void updatePatent(int id, InstitutionPatent patent);
//	 void insertPatent(InstitutionPatent patent);
	 InstitutionPatent getPatentById(int id);
	 List<InstitutionAuthor> getAuthorsByPatentId(int patentId);
	 Map<Integer, List<InstitutionAuthor>> getListAuthorsMap(
				List<Integer> patentIds);
	 void deleteByPatentId(int[] patentIds);
	 void createAuthor(InstitutionAuthor author);
//	 void updateRef(final int patentId, final int[] uid, final String[] authorType);
	 void createRef(final int patentId, final int[] uid, final int[] order);
	 void deleteRef(int patentId);
	 
		/**
		 * 删除与员工关联
		 * @param id
		 */
	void deleteUserRef(int uid,int patentId);
	
	void createUserRef(int uid,int patentId,int authorOrder);
	void updateUserRef(int uid,int patentId,int authorOrder);
	void update(InstitutionPatent patent,int authorId,int order);
	int create(InstitutionPatent patent,int institutionId,int authorId,int order);
	
	 
	 List<InstitutionAttachment> getAttachment(int objType,int objId);
	 void insertAttachments(int objId, int[] clbIds,String[] fileNames);
	 int insert(InstitutionPatent patent,int[] uid, int[] order, int[] clbIds,String[] fileNames);
	 void deleteAttachments(int objId);
	 Map<Integer, Integer> getGradesMap(int institutionId);
	 Map<Integer, Integer> getGradesMapByUser(int userId);
	 void deletePatentUser(int[] patentId);
	 void insertPatentUser(int patentId, int userId);
	 List<InstitutionPatent> getPatentByKeyword(int offset, int size, String keyword);
	 List<InstitutionPatent> getPatentByInit(int offset, int size,String userName);
	 void insertPatent(final String[] patentId,final int userId);
	 List<Integer> getExistPatentIds(int uid);
}
