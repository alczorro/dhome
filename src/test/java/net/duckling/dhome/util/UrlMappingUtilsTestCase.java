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
package net.duckling.dhome.util;

import junit.framework.Assert;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.common.util.UrlMappingUtil;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.UrlMapping;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * 单元测试
 * @author lvly
 * @since 2012-9-13
 */
public class UrlMappingUtilsTestCase {
	MockHttpServletRequest request=new MockHttpServletRequest();
	@Test
	public void test_getDefaultUrl(){
		String result=UrlMappingUtil.getDefaultUrl("/people/domain/index.html",null,"domain");
		Assert.assertEquals("/people/domain/index.html", result);
		result=UrlMappingUtil.getDefaultUrl("/index.html",null,"domain");
		Assert.assertEquals("/people/domain/index.html", result);
	}
	@Test
	public void test_isDefaultUrl(){
		
		request.setScheme("http");
		request.setServerName("www.escience.cn");
		request.setRequestURI("/dhome/people/wolegeca/index.html");
		request.setServerPort(80);
		Assert.assertEquals(true, UrlMappingUtil.isDefaultUrl(request));
	}
	@Test
	public void test_getDomain(){
		Assert.assertEquals("domain",UrlMappingUtil.getDomain("/people/domain/shit.com"));
	}
	@Test
	public void test_removePeople(){
		request.setScheme("http");
		request.setServerName("www.lvlongyun.cn");
		request.setRequestURI("/dhome/people/wolegeca/index.html");
		request.setServerPort(80);
		UrlMapping mapping=new UrlMapping();
		mapping.setDomain("wolegeca") ;
		mapping.setUrl("www.lvlongyun.com");
		mapping.setStatus("valid");
		mapping.setUid(12);
		SimpleUser user=new SimpleUser();
		user.setId(12);
		SessionUtils.setDomain(request, "wolegeca");
		SessionUtils.updateUser(request, user);
		Assert.assertEquals(false, UrlMappingUtil.isDefaultUrl(request));
		Assert.assertEquals("/index.html", UrlMappingUtil.removePeople("/people/wolegeca/index.html", request,mapping));
		
		request.setScheme("http");
		Assert.assertEquals("http://www.lvlongyun.com/people/wolegeca/index.html", UrlMappingUtil.removePeople("http://www.lvlongyun.com/people/wolegeca/index.html", request,mapping));
		
		SessionUtils.setDomain(request, null);
		Assert.assertEquals("/people/wolegeca/index.html", UrlMappingUtil.removePeople("/people/wolegeca/index.html", request,mapping));
		
		mapping=null;
		SessionUtils.updateUser(request, user);
		Assert.assertEquals("/people/wolegeca/index.html", UrlMappingUtil.removePeople("/people/wolegeca/index.html", request,mapping));
		request.setRequestURI("http://lvlongyun/dhome/people/wolegeca/index.html");
		Assert.assertEquals("/people/wolegeca/index.html", UrlMappingUtil.removePeople("/people/wolegeca/index.html", request,mapping));
	}
	@Test
	public void test_isMyself(){
		SessionUtils.setDomain(request, "domain");
		Assert.assertEquals(true, UrlMappingUtil.isMyself(request, "/people/domain/index.html", "domain"));
	}
}
