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

import java.util.List;

import junit.framework.Assert;
import net.duckling.dhome.dao.IInstitutionStatisticDAO;
import net.duckling.dhome.dao.impl.MockInstitutionHomeDAO;
import net.duckling.dhome.dao.impl.MockInstitutionPaperDAO;
import net.duckling.dhome.dao.impl.MockInstitutionStatisticDAO;
import net.duckling.dhome.domain.institution.InstitutionPublicationStatistic;
import net.duckling.dhome.service.impl.SystemJobService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SystemJobServiceTest {
	
	private SystemJobService sjs;
	private IInstitutionStatisticDAO stateDAO;
	
	@Before
	public void setUp(){
		sjs = new SystemJobService();
		sjs.setPaperDAO(new MockInstitutionPaperDAO());
		sjs.setHomeDAO(new MockInstitutionHomeDAO());
		stateDAO = new MockInstitutionStatisticDAO();
		sjs.setStatDAO(stateDAO);
	}
	
	@Test
	public void testSynchronizePaperStatistics(){
		sjs.synchronizePaperStatistics();
		List<InstitutionPublicationStatistic> result = stateDAO.getStatisticsByInstitutionId(0);
		Assert.assertNotNull(result);
		Assert.assertTrue(result.isEmpty());
	}
	
	@After
	public void tearDown(){
		sjs = null;
	}
}
