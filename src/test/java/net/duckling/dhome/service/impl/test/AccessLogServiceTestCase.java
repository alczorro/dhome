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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import net.duckling.dhome.dao.impl.StubAccessLogDAO;
import net.duckling.dhome.domain.object.AccessLog;
import net.duckling.dhome.service.IAccessLogService;
import net.duckling.dhome.service.impl.AccessLogService;
import net.duckling.dhome.service.impl.mock.MockCacheService;
import net.duckling.dhome.testutil.SetFieldUtils;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author lvly
 * @since 2012-12-27
 */
public class AccessLogServiceTestCase {
	private IAccessLogService accessLogService;
	@Before
	public void start(){
		accessLogService=new AccessLogService();
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("logDAO", new StubAccessLogDAO());
		param.put("cacheService", new MockCacheService());
		SetFieldUtils.setValues(accessLogService, param);
		
	}
	@Test
	public void test_addAccessLog(){
		accessLogService.addAccessLog(3, 1, "testDomain", "127.0.0.1");
		Assert.assertEquals(1,accessLogService.getAccessLogCount(1));
		
		accessLogService.addAccessLog(2, 1, "testDomain", "127.0.0.1");
		Assert.assertEquals(2,accessLogService.getAccessLogCount(1));
	}
	@Test
	public void test_getAccessLogs(){
		test_addAccessLog();
		List<AccessLog> list=accessLogService.getAccessLogs(1);
		Assert.assertEquals(2, list.size());
		Assert.assertEquals(3, list.get(0).getVisitorUid());
		Assert.assertEquals(2, list.get(1).getVisitorUid());
	}

}
