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

import net.duckling.dhome.domain.institution.InstitutionMemberFromVmt;
import net.duckling.dhome.domain.institution.InstitutionTraining;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.PageResult;

public interface IInstitutionBackendTrainingService {
	PageResult<InstitutionTraining> getTraining(int institutionId,int page, SearchInstitutionCondition condition);
	List<InstitutionTraining> getTrainingsByUID(int uid);

	void deleteTraining(int[] id);
	 int getTrainingCount(int id);
	 int getTrainingCount(int id,SearchInstitutionCondition condition);
	 void updateTraining(int id, InstitutionTraining training);
	 int insertTraining(InstitutionTraining training);
	 InstitutionTraining getTrainingById(int id);
	 InstitutionTraining getById(int id);
//	 List<InstitutionAuthor> getAuthorsByTrainingId(int trainingId);
//	 Map<Integer, List<InstitutionAuthor>> getListAuthorsMap(
//				List<Integer> trainingIds);
//	 void deleteByTrainingId(int[] trainingIds);
//	 void createAuthor(InstitutionAuthor author);
//	 void updateRef(final int trainingId, final int[] uid, final String[] authorType);
//	 void createRef(final int trainingId, final int[] uid, final String[] authorType);
//	 void deleteRef(int trainingId);
	 Map<Integer, Integer> getDegreesMap(int institutionId);
	 
	 List<InstitutionTraining> search(String keyword);
	 
	 PageResult<InstitutionTraining> getTrainingByUser(int userId,int page, SearchInstitutionCondition condition);
	 int getTrainingCountByUser(int userId);
	 Map<Integer, Integer> getDegreesByUser(int userId);
	 void deleteTrainingUser(int[] trainingId);
	 void insertTrainingUser(int trainingId, int userId);
		 List<InstitutionTraining> getTrainingByKeyword(int offset, int size, String keyword);
		 List<InstitutionTraining> getTrainingByInit(int offset, int size,String userName);
		 void insertTraining(String[] trainingId,int userId);
		 List<Integer> getExistTrainingIds(int uid);
		 

			/**
			 * 删除与员工关联
			 * @param id
			 */
			void deleteUserRef(int uid,int trainingId);
			
			void createUserRef(int uid,int trainingId);
			void update(InstitutionTraining training);
			int create(InstitutionTraining training,int institutionId);
	//导师与员工关联
	Map<String, InstitutionMemberFromVmt> getAllMember(int ins);
	 List<InstitutionTraining> getStudentByUser(int userId);
	 InstitutionMemberFromVmt getMemberByUser(int ins,String email);
}
