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
package net.duckling.dhome.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import net.duckling.dhome.service.IFriendService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.impl.mock.MockHomeService;
import net.duckling.dhome.service.impl.mock.StubAccessLogService;
import net.duckling.dhome.service.impl.mock.StubFriendService;
import net.duckling.dhome.testutil.SetFieldUtils;

import org.junit.Before;
import org.junit.Test;

/**
 * 相关人员controller测试类
 * @author lvly
 * @since 2012-10-15
 */
public class FriendControllerTestCase {
	FriendController controller=new FriendController();
	
	@Before
	public void before(){
		IHomeService homeService=new MockHomeService();
		IFriendService friendService=new StubFriendService();
		SetFieldUtils.setValue(controller, "friendService", friendService);
		SetFieldUtils.setValue(controller, "homeService", homeService);
		SetFieldUtils.setValue(controller, "accessLogService", new StubAccessLogService());
	}
	
	@Test
	public void test_getFriend(){
		Map<String,Object> result=controller.getFriend("test");
		//验证排重，返回6个，其中成对重复
	}

}
