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
package net.duckling.dhome.dao.impl;

import java.util.ArrayList;
import java.util.List;

import net.duckling.dhome.dao.IInstitutionPaperDAO;
import net.duckling.dhome.domain.object.PaperStatistics;
import net.duckling.dhome.domain.object.PaperYear;
import net.duckling.dhome.domain.people.Paper;

public class MockInstitutionPaperDAO implements IInstitutionPaperDAO {

	@Override
	public List<Paper> getPaperSortByCiteTime(int insId, int offset, int size) {
		return null;
	}

	@Override
	public List<Paper> getPaperSortByYear(int insId, String year, int offset,
			int size) {
		return null;
	}

	@Override
	public List<Paper> getPaperSortByAuthor(int insId, int uid, int offset,
			int size) {
		return null;
	}

	@Override
	public List<String> getYearsOfAllPaper(int insId) {
		if(insId == 0){
			List<String> result = new ArrayList<String>();
			result.add("2012");
			result.add("0000");
			return result;
		}
		return null;
	}

	@Override
	public List<Integer> getPaperAuthorIds(int insId) {
		return null;
	}

	@Override
	public int getPaperAmount(int insId, String year) {
		return 0;
	}

	@Override
	public int getAllPaperCitedTimes(int insId, String year) {
		return 0;
	}

	@Override
	public List<Integer> getPaperCitedTimes(int insId) {
		List<Integer> result = new ArrayList<Integer>();
		result.add(0);
		result.add(0);
		result.add(1);
		result.add(1);
		result.add(5);
		return result;
	}

	@Override
	public boolean isAllYearInfoEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<PaperYear> getAllPaperYear() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PaperStatistics> getAllDSNPaperStatistics() {
		return new ArrayList<PaperStatistics>();
	}

	@Override
	public void updateIntYear(List<PaperYear> paperYears) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<PaperStatistics> getAllNotDSNPaperStatistics() {
		return new ArrayList<PaperStatistics>();
	}

}
