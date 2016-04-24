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

import junit.framework.Assert;
import net.duckling.dhome.dao.IInstitutionPaperDAO;
import net.duckling.dhome.dao.impl.MockInstitutionPaperDAO;
import net.duckling.dhome.service.IInstitutionPaperService;
import net.duckling.dhome.service.impl.InstitutionPaperServiceImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InstitutionPaperServiceTest {
	private IInstitutionPaperDAO paperDAO;
	private IInstitutionPaperService ips;
	
	@Before
	public void setUp(){
		paperDAO = new MockInstitutionPaperDAO();
		ips = new InstitutionPaperServiceImpl();
	}
	
	@Test
	public void testGetPaperSortByCiteTime(){
		Assert.assertNull(ips.getPaperSortByCiteTime(0, 0, 0));
	}
	
	@Test
	public void testGetPaperSortByYear(){
		Assert.assertNull(ips.getPaperSortByYear(0, "0", 0, 0));
	}
	
	@Test
	public void testGetPaperSortByAuthor(){
		Assert.assertNull(ips.getPaperSortByAuthor(0, 0, 0, 0));
	}
	
	@Test
	public void testGetYearsOfAllPaper(){
		Assert.assertNotNull(ips.getYearsOfAllPaper(0));
	}
	
	@Test
	public void testGetPaperAuthorIds(){
		Assert.assertNull(ips.getPaperAuthorIds(0));
	}
	
	@Test
	public void testGetPaperAmount(){
		Assert.assertEquals(0, ips.getPaperAmount(0, "0"));
	}
	
	@Test
	public void testGetAllPaperCitedTimes(){
		Assert.assertEquals(0, ips.getAllPaperCitedTimes(0, "0"));
	}
	
	@Test
	public void testGetGIndex(){
		Assert.assertEquals(5, ips.getGIndex(0));
	}
	
	@Test
	public void testGetHIndex(){
		Assert.assertEquals(0, ips.getHIndex(0));
	}
	
	@After
	public void tearDown(){
		paperDAO = null;
		ips = null;
	}
}
