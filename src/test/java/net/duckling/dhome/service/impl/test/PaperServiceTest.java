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
package net.duckling.dhome.service.impl.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.duckling.dhome.dao.IPaperDAO;
import net.duckling.dhome.dao.impl.MockPaperDAO;
import net.duckling.dhome.domain.people.Paper;
import net.duckling.dhome.service.impl.PaperService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PaperServiceTest {

	private List<Paper> papers = null;
	private PaperService ps = null;
	private int uid = 108;
	
	@Before
	public void setUp(){
		papers = new ArrayList<Paper>();
		for(int i=0; i<6; i++){
			papers.add(buildPaperForSequence(i,i));
		}
		ps = new PaperService();
		IPaperDAO pDAO = new MockPaperDAO();
		ps.setPaperDAO(pDAO);
	}
	
	@Test
	public void testCreate(){
		Paper paper = Paper.build(uid, "paper1", "yxp", 
				"", "", "", "", "", "", "", "", "", 0, 0, 0,"");
		int id = ps.create(paper);
		Assert.assertTrue(id>0);
	}
	
	@Test
	public void testBatchCreate(){
		List<Paper> list = buildNoDsnPaper();
		int[] ids = ps.batchCreate(list);
		int start = ids[0];
		for(int i=0; i<ids.length; i++){
			Assert.assertEquals(start+i, ids[i]);
		}
	}
	
	@Test
	public void testGetPapers(){
		List<Paper> papers = ps.getPapers(uid, 0, 0);
		Assert.assertNotNull(papers);
		for(Paper paper : papers){
			Assert.assertEquals(uid, paper.getUid());
		}
	}
	
	@Test 
	public void testDeletePaper(){
		int id = createPaper();
		Assert.assertTrue(ps.deletePaper(id));
	}
	
	@Test
	public void testUpdateSequence(){
		List<Paper> list = buildNoDsnPaper();
		int[] ids = ps.batchCreate(list);
		int i=0;
		for(Paper paper: list){
			paper.setId(ids[i++]);
			paper.setSequence(i);
			Assert.assertTrue(ps.updateSequence(uid, ids[0], 1));
		}
	}
	
	@Test
	public void testUpdateSequence0(){
		Assert.assertTrue(ps.updateSequence(0, 0, 0));
	}
	
	@Test
	public void testGetPaper(){
		int id = createPaper();
		Paper paper = ps.getPaper(id);
		Assert.assertNotNull(paper);
		Assert.assertEquals(uid, paper.getUid());
		Assert.assertEquals(0, paper.getClbId());
		Assert.assertEquals(0, paper.getDsnPaperId());
	}
	
	@Test 
	public void testUpdateById(){
		int id = createPaper();
		Paper paper = ps.getPaper(id);
		paper.setAuthors("yxp");
		paper.setTitle("paper,paper");
		Assert.assertTrue(ps.updateById(paper));
		Paper paper2 = ps.getPaper(id);
		Assert.assertEquals("yxp", paper2.getAuthors());
		Assert.assertEquals("paper,paper", paper2.getTitle());
	}
	
	@Test
	public void testGetExistDsnPaperIds(){
		List<Paper> list2 = buildDsnPaper();
		ps.batchCreate(list2);
		List<Long> result = ps.getExistDsnPaperIds(uid);
		int i=1;
		for(long dsnId : result){
			Assert.assertEquals(dsnId, i++);
		}
	}
	
	@Test
	public void testSortBySequence1() {
		System.out.println("向后移动：");
		printPaperSequence(papers);
		List<Paper> result = ps.sortBySequence(papers, 0, 4);
		System.out.println();
		printPaperSequence(result);
	}
	
	@Test
	public void testSortBySequence2() {
		System.out.println("向前移动：");
		printPaperSequence(papers);
		List<Paper> result = ps.sortBySequence(papers, 5, -4);
		System.out.println();
		printPaperSequence(result);
	}

	@After
	public void tearDown(){
		papers.clear();
		papers = null;
	}
	
	private Paper buildPaperForSequence(int paperId, int sequence){
		Paper paper = Paper.build(uid, "test", "", "", "", "", 
				"0", "", "", "", "", "", 0, sequence, 1,"");
		paper.setId(paperId);
		return paper;
	}
	
	private void printPaperSequence(List<Paper> list){
		if(null!=list){
			for(Paper paper : list){
				System.out.println("("+paper.getId()+","+paper.getSequence()+") ");
			}
		}
	}
	
	private List<Paper> buildNoDsnPaper(){
		List<Paper> result = new ArrayList<Paper>();
		for(int i=0; i<3; i++){
			Paper paper = Paper.build(uid, "paper"+(i+2), "yxp", 
					"", "", "", "", "", "", "", "", "", 0, 0, 0,"");
			result.add(paper);
		}
		return result;
	}
	
	private List<Paper> buildDsnPaper(){
		List<Paper> result = new ArrayList<Paper>();
		for(int i=0; i<3; i++){
			Paper paper = Paper.build(uid, "paper"+(i+5), "yxp", 
					"", "", "", "", "", "", "", "", "", 0, 0, i+1,"");
			result.add(paper);
		}
		return result;
	}
	
	public int createPaper(){
		Paper paper = Paper.build(uid, "paper1", "yxp", 
				"", "", "", "", "", "", "", "", "", 0, 0, 0,"");
		return ps.create(paper);
	}
}
