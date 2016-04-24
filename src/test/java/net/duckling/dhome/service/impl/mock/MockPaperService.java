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
package net.duckling.dhome.service.impl.mock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.dhome.domain.people.Paper;
import net.duckling.dhome.service.IPaperService;

public class MockPaperService implements IPaperService {
	
	Map<Integer, Paper> papers = new HashMap<Integer, Paper>();

	@Override
	public int create(Paper paper) {
		int id = papers.size()+1;
		paper.setId(id);
		papers.put(id, paper);
		return id;
	}

	@Override
	public int[] batchCreate(List<Paper> papers) {
		if(null == papers || papers.isEmpty()){
			return new int[0];
		}
		int[] result = new int[papers.size()];
		int i=0;
		for(Paper paper : papers){
			result[i++] = create(paper);
		}
		return result;
	}

	@Override
	public List<Paper> getPapers(int uid, int offset, int size) {
		return null;
	}
	@Override
	public List<Paper> getEnPapers(int uid, int offset, int size) {
		return null;
	}


	@Override
	public boolean deletePaper(int paperId) {
		papers.remove(new Integer(paperId));
		return true;
	}

	@Override
	public boolean updateSequence(int uid, int paperId, int sequence) {
		Paper paper = papers.get(paperId);
		paper.setSequence(sequence);
		papers.put(paperId, paper);
		return true;
	}

	@Override
	public Paper getPaper(int paperid) {
		return papers.get(paperid);
	}

	@Override
	public boolean updateById(Paper paper) {
		int id = paper.getId();
		Paper temp = papers.get(id);
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
		temp.setUid(paper.getUid());
		temp.setVolumeIssue(paper.getVolumeIssue());
		return false;
	}

	@Override
	public List<Long> getExistDsnPaperIds(int uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPaperCount(int uid) {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
