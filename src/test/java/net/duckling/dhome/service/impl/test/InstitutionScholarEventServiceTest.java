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
import net.duckling.dhome.dao.IInstitutionScholarEventDAO;
import net.duckling.dhome.dao.impl.MockInstitutionScholarEventDAO;
import net.duckling.dhome.domain.institution.ScholarEvent;
import net.duckling.dhome.service.impl.InstitutionScholarEventService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InstitutionScholarEventServiceTest {
	private IInstitutionScholarEventDAO eventDAO;
	private InstitutionScholarEventService ises;
	
	@Before
	public void setUp(){
		eventDAO = new MockInstitutionScholarEventDAO();
		ises = new InstitutionScholarEventService();
		ises.setEventDAO(eventDAO);
	}
	
	@Test
	public void testCreate(){
		Assert.assertEquals(0, ises.create(new ScholarEvent()));
	}
	
	@Test
	public void testUpdateByID(){
		ises.updateByID(new ScholarEvent());
	}
	
	@Test
	public void testRemove(){
		ises.remove(0);
	}
	
	@Test
	public void testGetScholarEventByID(){
		Assert.assertNull(ises.getScholarEventByID(0));
	}
	
	@Test
	public void testGetAllScholarEventOfInstitution(){
		Assert.assertNull(ises.getAllScholarEventOfInstitution(0, 0, 0));
	}
	
	@Test
	public void testUpcomingScholarEvent(){
		Assert.assertNull(ises.getUpcomingScholarEventOfInstitution(0, 0, 0));
	}
	
	@Test
	public void testOngoingScholarEvent(){
		Assert.assertNull(ises.getOngoingScholarEventOfInstitution(0, 0, 0));
	}
	
	@Test
	public void testExpiredScholarEvent(){
		Assert.assertNull(ises.getExpiredScholarEventOfInstitution(0, 0, 0));
	}
	
	@Test
	public void testGetCount(){
		Assert.assertEquals(0, ises.getCount(0));
	}
	
	@After
	public void tearDown(){
		eventDAO = null;
		ises = null;
	}
}
