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

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.duckling.dhome.domain.institution.InstitutionTraining;
import net.duckling.dhome.domain.institution.InstitutionTraining;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;

public interface IInstitutionTrainingDAO {
	List<InstitutionTraining> getTraining(int id, int i, int pageSize, SearchInstitutionCondition condition);
	List<InstitutionTraining> getTrainingsByUID(int uid);

	int getTrainingCount(int insId, SearchInstitutionCondition condition);
	void deleteTraining(int[] id);
	void updateTraining(int id,InstitutionTraining training);
	void insert(List<InstitutionTraining> training);
	int insert(InstitutionTraining training);
	InstitutionTraining getTrainingById(int id);
	InstitutionTraining getById(int id);
	Map<Integer, Integer> getDegreesMap(int institutionId);
	
	List<InstitutionTraining> search(String keyword);
	
	Map<Integer, Integer> getDegreesByUser(int userId);
	List<InstitutionTraining> getTrainingByUser(int userId, int offset, int pageSize, SearchInstitutionCondition condition);
	int getTrainingCountByUser(int userId, SearchInstitutionCondition condition);
	void deleteTrainingUser(int[] id);
	void insertTrainingUser(int trainingId, int userId);
	
	 void deleteUserRef(int uid,int topicId);
	 void createUserRef(int uid,int topicId);
	 
	 List<InstitutionTraining> getStudentByUser(int userId);
	 
	 int getTrainingCount(int insId, int departId, int year);
	 //根据userId查询学生
	 List<InstitutionTraining> getTrainingsByUserId(int userId,Date startTime, Date endTime);
}
