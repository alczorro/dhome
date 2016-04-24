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

import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.SearchInstitutionPaperCondition;

public interface IInstitutionBackendPaperDAO {

	int create(InstitutionPaper paper);

	/**
	 * 添加论文和机构主页的关联关系
	 * @param institutionId 机构主页Id
	 * @param id 论文Id
	 * @param firstAuthorName 
	 * */
	void addRef(int institutionId, int id, String firstAuthorName);

	boolean isExist(int institutionId, int id);

	List<InstitutionPaper> getAllPapers();

	List<InstitutionPaper> getPapers(int institutionId, SearchInstitutionPaperCondition condition, int i, int pageSize);
	List<InstitutionPaper> getPapersByUID(int uid);
	List<InstitutionPaper> getEnPapersByUID(int uid);


	void delete(int institutionId, int[] paperId);

	InstitutionPaper getPaperById(int paperId);

	void update(InstitutionPaper paper);
	
	void updateRef(int institutionId,int paperId,String firstAuthorName);
	
	int isHashExitsReturnId(String hash);

	Map<Integer, Integer> getPublicationYearsMap(int institutionId);

	int getPaperCount(int institutionId,SearchInstitutionPaperCondition condition);
	
	int getPaperCountByUser(int userId,SearchInstitutionPaperCondition condition);
	
	int getPaperByDoi(String doi);
	
	/**
	 * 用户论文列表
	 * @param userId
	 * @param condition
	 * @param offset
	 * @param size
	 * @return
	 */
	List<InstitutionPaper> getPapersByUser(int userId,SearchInstitutionPaperCondition condition, int offset, int size);
	
	/**
	 * 以部门为标准，统计论文信息
	 * @param insId
	 * @return
	 */
	Map<String, Map<String, Integer>> getPaperStatisticsForDept();
	
	Map<String, Map<String, Integer>> getPaperStatisticsForYear();
	
    void deleteUserRef(int uid,int topicId);
    void createUserRef(int uid,int topicId);

	Map<Integer, Integer> getPublicationYearsMapByUser(int userId);
	void deletePaperUser(int[] id);
	void insertPaperUser(int paperId, int userId);
	
	void updateStatus(int paperId,int status);
	
	
	//获取论文总引用次数
	int getPapersCite(int insId);
	
	//按照期刊和作者搜索论文
	int getPaperCountByPub(int institutionId,SearchInstitutionPaperCondition condition);
	List<InstitutionPaper> getPapersByPub(int institutionId, SearchInstitutionPaperCondition condition, int i, int pageSize);
	
	int getPaperCountByAuthor(int institutionId,SearchInstitutionPaperCondition condition);
	List<InstitutionPaper> getPapersByAuthor(int institutionId, SearchInstitutionPaperCondition condition, int i, int pageSize);
	//根据论文部门和年份查询数量
	int getPaperCount(int ins, int departId, int year);
	
	//根据作者id查询论文
	List<InstitutionPaper> getPapersByAuthorId(int authorId,int startYear,int endYear);
	List<InstitutionPaper> getOtherPapersByAuthorId(int authorId,int startYear,int endYear);
	int getAllSciCount(int userid);
	List<InstitutionPaper> getAllFirst(int authorId);
	
	List<InstitutionPaper> getPapersByAuthor(int authorId,int startYear,int endYear);
	/**
	 * 合并论文库
	 * @return
	 */
	int move(InstitutionPaper paper);
	InstitutionPaper getPaperByTitle(String title);
	List<InstitutionPaper> getPapersByTitle(String title);
}
