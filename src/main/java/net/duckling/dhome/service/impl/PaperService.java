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

import java.util.ArrayList;
import java.util.List;

import net.duckling.dhome.dao.IPaperDAO;
import net.duckling.dhome.domain.people.Paper;
import net.duckling.dhome.service.IPaperService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 论文相关操作的Service，涉及paper表
 * @author Yangxp yangxiaopeng@cnic.cn
 * @since 2012-08-05
 */
@Service
public class PaperService implements IPaperService {

	@Autowired
	private IPaperDAO paperDAO;
	
	public void setPaperDAO(IPaperDAO paperDAO){
		this.paperDAO = paperDAO;
	}

	@Override
	public int create(Paper paper) {
		return paperDAO.create(paper);
	}

	@Override
	public int[] batchCreate(List<Paper> papers) {
		return paperDAO.batchCreate(papers);
	}

	@Override
	public List<Paper> getPapers(int uid, int offset, int size) {
		return paperDAO.getPapers(uid, offset, size);
	}

	@Override
	public List<Paper> getEnPapers(int uid, int offset, int size) {
		return paperDAO.getEnPapers(uid, offset, size);
	}

	@Override
	public boolean deletePaper(int paperId) {
		return paperDAO.deletePaper(paperId);
	}

	@Override
	public boolean updateSequence(int uid, int paperId, int sequence) {
		if (sequence == 0) {
			return true;
		}
		List<Paper> papers = getPapers(uid, 0, 0);
		List<Paper> sortedPaper = sortBySequence(papers, paperId, sequence);
		int result = paperDAO.batchUpdateSequenceByID(sortedPaper);
		return result > 0;
	}

	@Override
	public Paper getPaper(int paperid) {
		return paperDAO.getPaper(paperid);
	}

	@Override
	public boolean updateById(Paper paper) {
		return paperDAO.updateById(paper);
	}

	@Override
	public List<Long> getExistDsnPaperIds(int uid) {
		return paperDAO.getExistDsnPaperIds(uid);
	}
	@Override
	public int getPaperCount(int uid) {
		return paperDAO.getPaperCount(uid);
	}

	/**
	 * 将papers集合中的对象进行排序，将paperId所指的对象移动sequence个位置。
	 * 若sequence>0，则向后移动；反之，则向前移动。
	 * @param papers 待排序的Paper集合
	 * @param paperId 需要移动的Paper对象ID
	 * @param sequence 移动的位移量
	 * @return 排序后的Paper集合
	 */
	public List<Paper> sortBySequence(List<Paper> papers, int paperId, int sequence) {
		List<Paper> result = new ArrayList<Paper>();
		int size = papers.size();
		int curPaperIndex = getCurPaperIndex(papers, paperId);
		int start = 0;
		int end = curPaperIndex;
		if (sequence > 0) {
			start = (end + sequence) >= size ? size - 1 : end + sequence;
		} else {
			start = (curPaperIndex + sequence) < 0 ? 0 : curPaperIndex + sequence;
		}
		updatePaperSequence(result, papers, start, end);
		return result;
	}

	private int getCurPaperIndex(List<Paper> papers, int paperId) {
		int size = papers.size();
		int i = 0;
		while (i < size && paperId != papers.get(i).getId()) {
			i++;
		}
		return i;
	}

	/**
	 * 按照start和end的位置，将oldlist中处于两位置之间的元素进行顺序移动。 移动的规则根据start和end的大小决定。<br/>
	 * 1) start>end, 将start位置的元素移到最前面，其他元素依次向后移一位；<br/>
	 * 2）start<end, 将start位置的元素移到最后面，其他元素依次向前移一位。<br/>
	 * 
	 * @param newlist
	 *            存放移动位置后的Paper集合，没有移动的元素不包含在内
	 * @param oldlist
	 *            待处理的Paper集合
	 * @param start
	 *            起始位置
	 * @param end
	 *            结束位置
	 */
	private void updatePaperSequence(List<Paper> newlist, List<Paper> oldlist, int start, int end) {
		int temp = oldlist.get(start).getSequence();
		int i = start;
		boolean cond = (start < end) ? (i <= end - 1) : (i >= end + 1);
		while (cond) {
			Paper paper = oldlist.get(i);
			int step = (start < end) ? (i + 1) : (i - 1);
			paper.setSequence(oldlist.get(step).getSequence());
			newlist.add(paper);
			i = step;
			cond = (start < end) ? (i <= end - 1) : (i >= end + 1);
		}
		Paper paper = oldlist.get(end);
		paper.setSequence(temp);
		newlist.add(paper);
	}

}
