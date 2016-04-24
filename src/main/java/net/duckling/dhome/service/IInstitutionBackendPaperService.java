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

import net.duckling.dhome.domain.institution.DatabasePaper;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.InstitutionDisciplineOrientation;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.InstitutionPatent;
import net.duckling.dhome.domain.institution.InstitutionPublication;
import net.duckling.dhome.domain.institution.SearchInstitutionPaperCondition;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.domain.people.Paper;
import net.duckling.dhome.domain.people.SimpleUser;

public interface IInstitutionBackendPaperService {
	
	List<InstitutionPublication> getAllPubs();
	
	List<InstitutionDisciplineOrientation> getAllDiscipline();
	List<InstitutionPaper> getAllPapers();

	int create(InstitutionPaper paper,int institutionId,int[] uids,int[] order, boolean[] communicationAuthors, boolean[] authorStudents, boolean[] authorTeacher);

	/***
	 * 分页显示论文
	 * @param institutionId 机构主页Id
	 * @param page 页码
	 * @param condition 
	 * @return 分页对象
	 * */
	PageResult<InstitutionPaper> getPapers(int institutionId, int page, SearchInstitutionPaperCondition condition);
	List<InstitutionPaper> getPapers(int insId,SearchInstitutionPaperCondition condition);
	
	List<InstitutionPaper> getPapersByUID(int uid);
	List<InstitutionPaper> getEnPapersByUID(int uid);

	void updatePaper(InstitutionPaper paper);

	int getPapersCount(int institutionId);
	int getPapersCount(int institutionId,SearchInstitutionPaperCondition condition);
	
	//获取论文总引用次数
	int getPapersCite(int insId);
	/**
	 * 用户论文列表
	 * @param userId
	 * @param page 页码
	 * @param condition
	 * @return
	 */
	PageResult<InstitutionPaper> getPapersByUser(int userId, int page, SearchInstitutionPaperCondition condition);
	List<InstitutionPaper> getAllPapersByUser(int userId, SearchInstitutionPaperCondition condition);
	
	int getPapersCountByUser(int userId);

	void delete(int institutionId, int[] paperId);

	List<InstitutionPublication> getPubsByIds(List<Integer> pubId);
	InstitutionPublication getPubsById(int pubId);

	List<String> getAllKeyword(String queryString);
	List<String> getAllSponsor(String queryString);
	List<InstitutionPublication> getPubsByKey(String keyword);

	List<InstitutionAuthor> searchAuthor(String trim);

	void createAuthor(InstitutionAuthor author);
	void insertAuthorUser(String cstnetId,int institutionId,String[] authorId,int status);
	void cancelAuthorUser(int institutionId, String[] authorId);

	boolean isMyPaper(int institutionId, int paperId);

	InstitutionPaper getPaperById(int paperId);

	List<InstitutionAuthor> getAuthorsByPaperId(int paperId);
	List<InstitutionAuthor> getAuthorsByCstnetId(String cstnetId);

	void update(InstitutionPaper paper, int institutionId, int[] uid,
			int[] order, boolean[] communicationAuthors,
			boolean[] authorStudents, boolean[] authorTeacher);

	Map<Integer, List<InstitutionAuthor>> getListAuthorsMap(
			List<Integer> paperIds);

	InstitutionAuthor getAuthorsById(int paperId, int authorId);
	InstitutionAuthor getAuthorById(int authorId);

	Map<Integer, Integer> getPublicationYearsMap(int institutionId);
	
	Map<Integer, Integer> getPublicationYearsMapByUser(int userId);
	void deletePaperUser(int[] id);
	void insertPaperUser(int paperId, int userId);
	/**
	 * 删除与员工关联
	 * @param id
	 */
	void deleteUserRef(int uid,int paperId);
	
	void createUserRef(int uid,int paperId);
	 List<InstitutionPaper> getPaperByKeyword(int offset, int size, String keyword);
	 List<InstitutionPaper> getPaperByInit(int offset, int size,String userName);
	 void insertPaper(final String[] paperId,final int userId);
	 void deletePaperUser(final String[] paperId, final int userId);
	 List<InstitutionPaper> getPaperByKeywordInit(int offset, int size,List<InstitutionAuthor> authors) ;
	 List<Integer> getExistPaperIds(int uid);
	 
	 Map<String, Map<String, Integer>> getPaperStatisticsForDept();
	 Map<String, Map<String, Integer>> getPaperStatisticsForYear();
	 
	 Map<Integer,DatabasePaper> insertPaper(List<DatabasePaper> dbaPaper);
		List<DatabasePaper> getPaperTemp();
		int getPubId(String pubName);
		void insert1(List<DatabasePaper> list);
		void insert2(List<DatabasePaper> list);
		void insert3(List<DatabasePaper> list);
		void insert4(List<DatabasePaper> list);
		void insertPaperRef(List<DatabasePaper> papers, int insId);
		void insertAuthor(List<DatabasePaper> dbaPaper);
		/**
		 * 作者用户关联
		 * @param institutionId 机构主页Id
		 * @param page 页码
		 */
		PageResult<InstitutionAuthor> getAuthors(int institutionId, int page,SearchInstitutionPaperCondition condition);
		int getAuthorsCount(int institutionId,SearchInstitutionPaperCondition condition);
		PageResult<InstitutionPaper> getPapersByKey(int institutionId, int page,String userName);
		
		void updateStatus(int paperId,int status);
		void updateAuthorPropelling(int authorId);
		
		//按照期刊和作者搜索论文
		PageResult<InstitutionPaper> getPapersByPub(int institutionId, int page, SearchInstitutionPaperCondition condition);
		PageResult<InstitutionPaper> getPapersByAuthor(int institutionId, int page, SearchInstitutionPaperCondition condition);
		
		List<InstitutionPaper> getPaperByPub(int institutionId, SearchInstitutionPaperCondition condition);
		List<InstitutionPaper> getPaperByAuthor(int institutionId, SearchInstitutionPaperCondition condition);
		
		List<InstitutionDepartment> getDepartments(int insId);
		/**
		 * 合并论文库
		 * @param page 页码
		 */
		void movePaper(List<Paper> papers,int uid);
		
		List<InstitutionPaper> getPaperByTitle(String title);
		int getPaperByDoi(String doi);
}
