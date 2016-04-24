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
package net.duckling.dhome.service.impl;

import java.util.List;

import net.duckling.dhome.common.util.GHIndexCaculator;
import net.duckling.dhome.dao.IInstitutionPaperDAO;
import net.duckling.dhome.domain.people.Paper;
import net.duckling.dhome.service.IInstitutionPaperService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 机构论文服务
 * @author Yangxp
 * @since 2012-09-25
 */
@Service
public class InstitutionPaperServiceImpl implements IInstitutionPaperService {

	@Autowired
	private IInstitutionPaperDAO paperDAO;
	
	public void setPaperDAO(IInstitutionPaperDAO paperDAO) {
		this.paperDAO = paperDAO;
	}

	@Override
	public List<Paper> getPaperSortByCiteTime(int insId, int offset, int size) {
		return paperDAO.getPaperSortByCiteTime(insId, offset, size);
	}

	@Override
	public List<Paper> getPaperSortByYear(int insId, String year, int offset,
			int size) {
		return paperDAO.getPaperSortByYear(insId, year, offset, size);
	}

	@Override
	public List<Paper> getPaperSortByAuthor(int insId, int uid, int offset,
			int size) {
		return paperDAO.getPaperSortByAuthor(insId, uid, offset, size);
	}

	@Override
	public List<String> getYearsOfAllPaper(int insId) {
		return paperDAO.getYearsOfAllPaper(insId);
	}

	@Override
	public List<Integer> getPaperAuthorIds(int insId) {
		return paperDAO.getPaperAuthorIds(insId);
	}

	@Override
	public int getPaperAmount(int insId, String year) {
		return paperDAO.getPaperAmount(insId, year);
	}

	@Override
	public int getAllPaperCitedTimes(int insId, String year) {
		return paperDAO.getAllPaperCitedTimes(insId, year);
	}

	@Override
	public int getGIndex(int insId) {
		List<Integer> citeNums = paperDAO.getPaperCitedTimes(insId);
		return GHIndexCaculator.caculateGIndex(citeNums);
	}

	@Override
	public int getHIndex(int insId) {
		List<Integer> citeNums = paperDAO.getPaperCitedTimes(insId);
		return GHIndexCaculator.caculateHIndex(citeNums);
	}
}
