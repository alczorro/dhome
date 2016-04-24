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
import net.duckling.dhome.common.util.TranslateUtils;
import net.duckling.dhome.util.translateBean.Son;
import net.duckling.dhome.util.translateBean.Super;

import org.junit.Test;

/**
 * @author lvly
 * @since 2012-10-22
 */
public class TranslateUtilsTestCase {
	
	@Test
	public void test_translateStr(){
		String str="<script> alert('1');alert(\"2\")</script>";
		Assert.assertEquals("&lt;script&gt; alert(&acute;1&acute;);alert(&quot;2&quot;)&lt;/script&gt;", TranslateUtils.translateStr(str));
	}
	@Test
	public void test_tranlateObj()throws Exception{
		Super supe=new Super();
		supe.setId(123);
		supe.setStrin("<string>");
		supe.setWokao("'wokao'");
		supe.setWomao("<>'\"");
		Son son=new Son();
		son.setBool(true);
		son.setDoubl(0.01);
		son.setFloa(0.1f);
		son.setLon(10000);
		son.setName("<*&^%*'>\"");
		son.setShor((short)123);
		son.setByt((byte)12);
		son.setSize(23);
		supe.setSon(son);
		supe=TranslateUtils.translateObj(supe);
		Assert.assertEquals(supe.getId(), 123);
		Assert.assertEquals(supe.getStrin(),"&lt;string&gt;");
		Assert.assertEquals(supe.getWokao(),"&acute;wokao&acute;");
		Assert.assertEquals(supe.getWomao(),"&lt;&gt;&acute;&quot;");
		Son son2=supe.getSon();
		Assert.assertEquals(son2.getDoubl(), 0.01);
		Assert.assertEquals(son2.getFloa(), 0.1f);
		Assert.assertEquals(son2.getLon(), 10000);
		Assert.assertEquals(son2.getName(), "&lt;*&^%*&acute;&gt;&quot;");
		Assert.assertEquals(son2.getShor(),123);
		Assert.assertEquals(son2.getSize(),23);
		Assert.assertEquals(son2.getByt(),12);
		
		
	}

}
