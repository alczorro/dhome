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
/**
 * 
 */
package net.duckling.dhome.service.impl.mock;

import java.util.ArrayList;
import java.util.List;

import net.duckling.dhome.domain.people.Paper;
import net.duckling.dhome.service.IInstitutionPaperService;

/**
 * @author lvly
 * @since 2012-9-21
 */
public class StubInstitutionPaperService implements IInstitutionPaperService{

	private List<Paper> papers = null;
	
	public StubInstitutionPaperService(){
		papers = new ArrayList<Paper>();
		Paper paper = new Paper();
		paper.setId(0);
		paper.setAuthors("test");
		paper.setTitle("test Paper");
		papers.add(paper);
	}
	
	@Override
	public List<Paper> getPaperSortByCiteTime(int insId, int offset, int size) {
		if(insId==0 || insId==12){
			return papers;
		}
		return null;
	}

	@Override
	public List<Paper> getPaperSortByYear(int insId, String year, int offset, int size) {
		if(insId==0){
			return papers;
		}
		return null;
	}

	@Override
	public List<Paper> getPaperSortByAuthor(int insId, int uid, int offset, int size) {
		if(insId==0){
			return papers;
		}
		return null;
	}

	@Override
	public List<String> getYearsOfAllPaper(int insId) {
		if(insId==0){
			List<String> years = new ArrayList<String>();
			years.add("2012");
			years.add("2009");
			return years;
		}
		return null;
	}

	@Override
	public List<Integer> getPaperAuthorIds(int insId) {
		if(insId==0){
			List<Integer> ids = new ArrayList<Integer>();
			ids.add(12);
			ids.add(11);
			return ids;
		}
		return null;
	}

	@Override
	public int getPaperAmount(int insId, String year) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAllPaperCitedTimes(int insId, String year) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getGIndex(int insId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHIndex(int insId) {
		// TODO Auto-generated method stub
		return 0;
	}

}
