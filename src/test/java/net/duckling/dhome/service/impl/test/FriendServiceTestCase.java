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

import java.util.HashMap;
import java.util.Map;

import net.duckling.dhome.service.IEducationService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionPeopleService;
import net.duckling.dhome.service.IInterestService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.IWorkService;
import net.duckling.dhome.service.impl.FriendService;
import net.duckling.dhome.service.impl.mock.MockHomeService;
import net.duckling.dhome.service.impl.mock.MockInterestService;
import net.duckling.dhome.service.impl.mock.MockUserService;
import net.duckling.dhome.service.impl.mock.MockWorkService;
import net.duckling.dhome.service.impl.mock.StubInstitutionPeopleService;
import net.duckling.dhome.testutil.SetFieldUtils;
import net.duckling.dhome.web.MockEducationService;

import org.junit.Before;
import org.junit.Test;

/**
 * 相关人员服务类测试
 * 
 * @author lvly
 * @since 2012-10-15
 */
public class FriendServiceTestCase {
	FriendService service = new FriendService();

	@Before
	public void before() {
		IWorkService workService=new MockWorkService();
		IEducationService eduService=new MockEducationService();
		IInstitutionPeopleService peopleService=new StubInstitutionPeopleService();
		IUserService userService=new MockUserService();
		IHomeService homeService=new MockHomeService();
		IInterestService interestService=new MockInterestService();
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("workService", workService);
		param.put("homeService", homeService);
		param.put("interestService", interestService);
		param.put("peopleService",peopleService);
		param.put("userService",userService);
		param.put("eduService",eduService);
		SetFieldUtils.setValues(service, param);
	}
	@Test
	public void test_getFriendsByInstitution(){
		service.getFriendsByInstitution(0, 0, 15);
	}
	@Test
	public void test_getFriendsByDiscipline(){
		service.getFriendsByDiscipline(0, 0, 10);
	}
	@Test
	public void test_getFriendsByInterest(){
		service.getFriendsByInterest(0, 0, 10);
	}

}
