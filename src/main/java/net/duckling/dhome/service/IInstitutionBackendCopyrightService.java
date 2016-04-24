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
import net.duckling.dhome.domain.institution.InstitutionCopyright;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.PageResult;

public interface IInstitutionBackendCopyrightService {

	PageResult<InstitutionCopyright> getCopyright(int institutionId,int page, SearchInstitutionCondition condition);
	List<InstitutionCopyright> getCopyrightsByUID(int uid);

	int getCopyrightCount(int insId);
	int getCopyrightCount(int insId,SearchInstitutionCondition condition);
	PageResult<InstitutionCopyright> getCopyrightByUser(int userId,int page, SearchInstitutionCondition condition);
	int getCopyrightCountByUser(int userId);
	void updateCopyright(int id, InstitutionCopyright copyright);
//	 void insertCopyright(InstitutionCopyright copyright);
	 InstitutionCopyright getCopyrightById(int id);
	 List<InstitutionAuthor> getAuthorsByCopyrightId(int copyrightId);
	 Map<Integer, List<InstitutionAuthor>> getListAuthorsMap(
				List<Integer> copyrightIds);
	 void deleteByCopyrightId(int[] copyrightIds);
	 void createAuthor(InstitutionAuthor author);
//	 void updateRef(final int copyrightId, final int[] uid, final String[] authorType);
	 void createRef(final int copyrightId, final int[] uid, final int[] order);
	 void deleteRef(int copyrightId);
	 
	 List<InstitutionAttachment> getAttachment(int objType,int objId);
	 void insertAttachments(int objId, int[] clbIds,String[] fileNames);
	 int insert(InstitutionCopyright copyright,int[] uid, int[] order, int[] clbIds,String[] fileNames);
	 void deleteAttachments(int objId);
	 Map<Integer, Integer> getGradesMap(int institutionId);
	 Map<Integer, Integer> getGradesMapByUser(int userId);
	 void deleteCopyrightUser(int[] copyrightId);
	 void insertCopyrightUser(int copyrightId, int userId);
	 List<InstitutionCopyright> getCopyrightByKeyword(int offset, int size, String keyword);
	 List<InstitutionCopyright> getCopyrightByInit(int offset, int size,String userName);
	 void insertCopyright(final String[] copyrightId,final int userId);
	 List<Integer> getExistCopyrightIds(int uid);
	 
	 
	 /**
		 * 删除与员工关联
		 * @param id
		 */
	void deleteUserRef(int uid,int copyrightId);
	
	void createUserRef(int uid,int copyrightId,int authorOrder);
	void updateUserRef(int uid,int copyrightId,int authorOrder);
	void update(InstitutionCopyright copyright,int authorId,int order);
	int create(InstitutionCopyright copyright,int institutionId,int authorId,int order);
	
	InstitutionAuthor getById(int copyrightId, int authorId);
}
