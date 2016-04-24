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
package net.duckling.dhome.service.impl.test;

import junit.framework.Assert;
import net.duckling.dhome.dao.IEducationDAO;
import net.duckling.dhome.dao.IInstitutionPeopleDAO;
import net.duckling.dhome.dao.IWorkDAO;
import net.duckling.dhome.dao.impl.StubEducationDAO;
import net.duckling.dhome.dao.impl.StubInstitutionPeopleDAO;
import net.duckling.dhome.dao.impl.StubWorkDAO;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInterestService;
import net.duckling.dhome.service.impl.InstitutionPeopleService;
import net.duckling.dhome.service.impl.mock.MockInterestService;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lvly
 * @since 2012-9-21
 */
public class InstitutionPeopleServiceTestCase {
	InstitutionPeopleService peopleService=new InstitutionPeopleService();
	IInstitutionPeopleDAO peopleDAO=new StubInstitutionPeopleDAO();
	IEducationDAO eduDAO=new StubEducationDAO();
	IHomeService homeService=new StubHomeService();
	IWorkDAO workDAO=new StubWorkDAO();
	IInterestService interestService=new MockInterestService();
	@Before
	public void before() throws Exception {
		peopleService.setInterestService(interestService);
		peopleService.setPeopleDAO(peopleDAO);
		peopleService.setEduDAO(eduDAO);
		peopleService.setHomeService(homeService);
		peopleService.setWorkDAO(workDAO);
	}
	@Test
	public void test_getMemberSize(){
		Assert.assertEquals(12,peopleService.getMembersSize(12));
	}
	@Test
	public void test_deleteMember(){
		peopleService.deleteMember(12, 112);//uid,institutionId
	}
	@Test
	public void test_getPeoplesByInstitutionId(){
		peopleService.getPeoplesByInstitutionId(112, 0, 6);
	}
	@Test
	public void test_isMember(){
		Assert.assertTrue(peopleService.isMember(12, 112));
		Assert.assertFalse(peopleService.isMember(-1, 112));
	}
}
