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

import net.duckling.dhome.dao.IPaperDAO;
import net.duckling.dhome.domain.people.Paper;

public class MockPaperDAO implements IPaperDAO {

	private List<Paper> papers = new ArrayList<Paper>();
	
	@Override
	public int create(Paper paper) {
		int size = papers.size();
		paper.setId(size+1);
		papers.add(paper);
		return size+1;
	}

	@Override
	public int[] batchCreate(List<Paper> papers) {
		if(null != papers && !papers.isEmpty()){
			int[] result = new int[papers.size()];
			int i=0;
			for(Paper paper : papers){
				int id = create(paper);
				result[i++] = id;
			}
			return result;
		}
		return new int[0];
	}

	@Override
	public List<Paper> getPapers(int uid, int offset, int size) {
		List<Paper> result = new ArrayList<Paper>();
		for(Paper paper : papers){
			if(paper.getUid() == uid){
				result.add(paper);
			}
		}
		return result;
	}
	@Override
	public List<Paper> getEnPapers(int uid, int offset, int size) {
		List<Paper> result = new ArrayList<Paper>();
		for(Paper paper : papers){
			if(paper.getUid() == uid){
				result.add(paper);
			}
		}
		return result;
	}


	@Override
	public boolean deletePaper(int paperId) {
		int index = -1;
		int size = papers.size();
		for(int i=0; i<size; i++){
			Paper paper = papers.get(i);
			if(paper.getId() == paperId){
				index = i;
				break;
			}
		}
		if(index>=0){
			papers.remove(index);
			return true;
		}
		return false;
	}

	@Override
	public int batchUpdateSequenceByID(List<Paper> sortedPaper) {
		if(null == sortedPaper || sortedPaper.isEmpty()){
			return 0;
		}else{
			for(Paper paper : sortedPaper){
				int id = paper.getId();
				for(Paper paper2: papers){
					if(paper2.getId() == id){
						paper2.setSequence(paper.getSequence());
						break;
					}
				}
			}
		}
		return 1;
	}

	@Override
	public Paper getPaper(int paperid) {
		for(Paper paper: papers){
			if(paper.getId() == paperid){
				return paper;
			}
		}
		return null;
	}

	@Override
	public boolean updateById(Paper paper) {
		int id = paper.getId();
		for(Paper temp: papers){
			if(temp.getId() == id){
				temp.setAuthors(paper.getAuthors());
				temp.setClbId(paper.getClbId());
				temp.setDsnPaperId(paper.getDsnPaperId());
				temp.setKeywords(paper.getKeywords());
				temp.setLanguage(paper.getLanguage());
				temp.setLocalFulltextURL(paper.getLocalFulltextURL());
				temp.setPaperURL(paper.getPaperURL());
				temp.setPublishedTime(paper.getPublishedTime());
				temp.setSequence(paper.getSequence());
				temp.setSource(paper.getSource());
				temp.setSummary(paper.getSummary());
				temp.setTimeCited(paper.getTimeCited());
				temp.setTitle(paper.getTitle());
				temp.setVolumeIssue(paper.getVolumeIssue());
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Long> getExistDsnPaperIds(int uid) {
		List<Long> result = new ArrayList<Long>();
		for(Paper paper : papers){
			if(paper.getUid() == uid){
				result.add(paper.getDsnPaperId());
			}
		}
		return result;
	}

	@Override
	public int getPaperCount(int uid) {
		// TODO Auto-generated method stub
		return 0;
	}

}
